package br.ufsc.inf.lapesd.ld_jaxrs.core.traverser;

import br.ufsc.inf.lapesd.ld_jaxrs.CBD;
import br.ufsc.inf.lapesd.ld_jaxrs.TraverserPredicate;
import br.ufsc.inf.lapesd.ld_jaxrs.core.priv.uris.RDF;
import br.ufsc.inf.lapesd.ld_jaxrs.model.Graph;
import br.ufsc.inf.lapesd.ld_jaxrs.model.Node;
import br.ufsc.inf.lapesd.ld_jaxrs.model.PropertySpec;
import br.ufsc.inf.lapesd.ld_jaxrs.model.Triple;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Traverses a Node according to the Consise Bounded Description specified by a
 * {@link CBD} annotation.
 */
@TraversalStrategy(CBD.class)
public class CBDTraverser implements Traverser {
    private int maxPath = Integer.MAX_VALUE;
    private boolean reifications = true;
    private boolean traverseNamed = false;
    private int maxPathFromFirstNamed = Integer.MAX_VALUE;
    private int maxPathFromFirstBlank = Integer.MAX_VALUE;
    @Nonnull private TraverserPredicate traverserPredicate = new TraverserPredicate.True();

    public CBDTraverser(CBD annotation) {
        maxPath = annotation.maxPath();
        reifications = annotation.reifications();
        traverseNamed = annotation.traverseNamed();
        maxPathFromFirstNamed = annotation.maxPathFromFirstNamed();
        maxPathFromFirstBlank = annotation.maxPathFromFirstBlank();
        Class<? extends TraverserPredicate> predicateClass = annotation.traverserPredicate();
        try {
            Constructor<? extends TraverserPredicate> constructor;
            constructor = predicateClass.getConstructor();
            traverserPredicate = constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException
                | IllegalAccessException e) {
            throw new IllegalArgumentException("Failed to create TraverserPredicate", e);
        }
    }

    public CBDTraverser() {
    }

    public int getMaxPath() { return maxPath; }
    public boolean getReifications() { return reifications; }
    public boolean getTraverseNamed() { return traverseNamed; }
    public int getMaxPathFromFirstNamed() { return maxPathFromFirstNamed; }
    public int getMaxPathFromFirstBlank() { return maxPathFromFirstBlank; }
    public @Nonnull TraverserPredicate getTraverserPredicate() { return traverserPredicate; }

    public CBDTraverser setMaxPath(int maxPath) {
        this.maxPath = maxPath;
        return this;
    }
    public CBDTraverser setReifications(boolean reifications) {
        this.reifications = reifications;
        return this;
    }
    public CBDTraverser setTraverseNamed(boolean traverseNamed) {
        this.traverseNamed = traverseNamed;
        return this;
    }
    public CBDTraverser setMaxPathFromFirstNamed(int maxPathFromFirstNamed) {
        this.maxPathFromFirstNamed = maxPathFromFirstNamed;
        return this;
    }
    public CBDTraverser setMaxPathFromFirstBlank(int maxPathFromFirstBlank) {
        this.maxPathFromFirstBlank = maxPathFromFirstBlank;
        return this;
    }
    public CBDTraverser setTraverserPredicate(@Nonnull TraverserPredicate traverserPredicate) {
        this.traverserPredicate = traverserPredicate;
        return this;
    }

    public void traverse(@Nonnull Graph graph, @Nonnull Node node,
                         @Nonnull TraverserListener listener) {
        Set<Node> visited = new HashSet<>();
        ReificationCollector collector = reifications ? new ReificationCollector(graph) : null;
        traverse(graph, node, listener, visited, collector);

        if (collector != null) {
            for (Node reification : collector.collected)
                traverse(graph, reification, listener, visited, null);
        }
    }

    private void traverse(@Nonnull Graph graph, @Nonnull Node node,
                          @Nonnull TraverserListener listener,
                          @Nonnull Set<Node> visited,
                          @Nullable ReificationCollector adder) {
        Queue<State> queue = new LinkedList<>();
        queue.add(new State(node));
        while (!queue.isEmpty()) {
            State s = queue.remove();
            if (visited.contains(s.node)) continue;
            visited.add(s.node);

            Iterator<Triple> it = graph.query(s.node, null, null);
            while (it.hasNext()) {
                Triple triple = it.next();
                listener.add(triple);
                if (adder != null) adder.add(graph, triple);

                Object predicateState = shouldTraverse(s, triple.getObject());
                if (predicateState != null)
                    queue.add(s.next(triple.getObject(), predicateState));
            }
        }
    }

    private Object shouldTraverse(State current, Node node) {
        if (node.isLiteral())
            return null;
        boolean goodType = traverseNamed ? node.isResource() : node.isBlankNode();
        boolean goodDepths = current.depth < maxPath;
        if (goodDepths && current.firstNamedDepth >= 0)
            goodDepths = current.firstNamedDepth < maxPathFromFirstNamed;
        if (goodDepths && current.firstBlankDepth >= 0)
            goodDepths = current.firstBlankDepth < maxPathFromFirstBlank;
        if (goodDepths && goodType)
            return traverserPredicate.apply(current.predicateState, node);
        return null;
    }

    @Override
    public String toString() {
        return String.format("CBDTraverser@%h(maxPath=%d, reifications=%b)",
                this, maxPath, reifications);
    }

    private static final class ReificationCollector {
        Node subject, predicate, object;
        ArrayList<Node> collected = new ArrayList<>();

        ReificationCollector(Graph graph) {
            subject = graph.getNode(RDF.subject);
            predicate = graph.getNode(RDF.predicate);
            object = graph.getNode(RDF.object);
        }

        void add(Graph graph, Triple triple) {
            Iterator<Node> it = graph.querySubjects(new PropertySpec(subject, triple.getSubject()),
                    new PropertySpec(predicate, triple.getPredicate()),
                    new PropertySpec(object, triple.getObject()));
            while (it.hasNext())
                collected.add(it.next());
        }
    }

    private static final class State {
        final Node node;
        final int depth;
        final int firstNamedDepth;
        final int firstBlankDepth;
        final Object predicateState;

        public State(Node node) {
            this(node, 0, -1, -1, null);
        }

        State(Node node, int depth, int firstNamedDepth, int firstBlankDepth,
              Object predicateState) {
            this.node = node;
            this.depth = depth;
            this.firstNamedDepth = firstNamedDepth;
            this.firstBlankDepth = firstBlankDepth;
            this.predicateState = predicateState;
        }

        State next(Node node, Object predicateState) {
            int fnd = firstNamedDepth;
            if (fnd < 0 && node.isResource() && !node.isBlankNode())
                fnd = 0;
            else if (fnd >= 0)
                ++fnd;
            int fbd = firstBlankDepth;
            if (fbd < 0 && node.isBlankNode())
                fbd = 0;
            else if (fbd >= 0)
                ++fbd;

            return new State(node, depth + 1, fnd, fbd, predicateState);
        }

        @Override
        public String toString() {
            return String.format("(%s, depth=%d)", node, depth);
        }
    }
}
