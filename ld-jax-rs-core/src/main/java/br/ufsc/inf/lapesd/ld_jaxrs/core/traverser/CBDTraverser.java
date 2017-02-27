package br.ufsc.inf.lapesd.ld_jaxrs.core.traverser;

import br.ufsc.inf.lapesd.ld_jaxrs.CBD;
import br.ufsc.inf.lapesd.ld_jaxrs.core.model.Graph;
import br.ufsc.inf.lapesd.ld_jaxrs.core.model.Node;
import br.ufsc.inf.lapesd.ld_jaxrs.core.model.PropertySpec;
import br.ufsc.inf.lapesd.ld_jaxrs.core.model.Triple;
import br.ufsc.inf.lapesd.ld_jaxrs.core.priv.uris.RDF;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Traverses a Node according to the Consise Bounded Description specified by a
 * {@link CBD} annotation.
 */
@TraversalStrategy(CBD.class)
public class CBDTraverser implements Traverser {
    private int maxPath = Integer.MAX_VALUE;
    private boolean reifications = true;

    public CBDTraverser(CBD annotation) {
        maxPath = annotation.maxPath();
        reifications = annotation.reifications();
    }

    public CBDTraverser() {
    }

    public int getMaxPath() {
        return maxPath;
    }

    public CBDTraverser setMaxPath(int maxPath) {
        this.maxPath = maxPath;
        return this;
    }

    public boolean isReifications() {
        return reifications;
    }

    public CBDTraverser setReifications(boolean reifications) {
        this.reifications = reifications;
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
        queue.add(new State(node, 0));
        while (!queue.isEmpty()) {
            State s = queue.remove();
            if (visited.contains(s.node)) continue;
            visited.add(s.node);

            Iterator<Triple> it = graph.query(s.node, null, null);
            while (it.hasNext()) {
                Triple triple = it.next();
                listener.add(triple);
                if (adder != null) adder.add(graph, triple);
                if (s.depth < maxPath && triple.getObject().isBlankNode())
                    queue.add(s.next(triple.getObject()));
            }
        }
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

        State(Node node, int depth) {
            this.node = node;
            this.depth = depth;
        }

        State next(Node node) {
            return new State(node, depth+1);
        }

        @Override
        public String toString() {
            return String.format("(%s, depth=%d)", node, depth);
        }
    }
}
