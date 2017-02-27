package br.ufsc.inf.lapesd.ld_jaxrs.jena.impl.model;

import br.ufsc.inf.lapesd.ld_jaxrs.core.model.Graph;
import br.ufsc.inf.lapesd.ld_jaxrs.core.model.Node;
import br.ufsc.inf.lapesd.ld_jaxrs.core.model.PropertySpec;
import br.ufsc.inf.lapesd.ld_jaxrs.core.model.Triple;
import br.ufsc.inf.lapesd.ld_jaxrs.core.traverser.TraverserListener;
import org.apache.commons.collections4.iterators.TransformIterator;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Wraps a {@link Model} as a {@link Graph}
 */
public final class JenaModelGraph implements Graph, TraverserListener {
    private final Model model;

    public JenaModelGraph(Model model) {
        this.model = model;
    }

    @Override
    public void add(Triple triple) {
        if (triple == null) throw new NullPointerException();
        if (!(triple instanceof JenaTriple))
            throw new IllegalArgumentException("Triple not a JenaTriple");
        model.add(((JenaTriple) triple).getStatement());
    }

    @Nonnull
    @Override
    public Iterator<Triple> query(@Nullable Node subject, @Nullable Node predicate,
                                  @Nullable Node object) {
        JenaNode subjectJN = (JenaNode) subject;
        JenaNode predicateJN = (JenaNode) predicate;
        JenaNode objectJN = (JenaNode) object;

        StmtIterator it = model.listStatements(subjectJN == null ? null : subjectJN.asResource(),
                predicateJN == null ? null : predicateJN.asProperty(),
                objectJN == null ? null : objectJN.asRDFNode());
        return new TransformIterator<>(it, JenaTriple::new);
    }

    @Nonnull
    @Override
    public Iterator<Node> querySubjects(PropertySpec... propertySpecs) {
        HashMap<Resource, Set<Iterator<Resource>>> observed = new HashMap<>();

        List<Iterator<Resource>> its = Arrays.stream(propertySpecs).filter(PropertySpec::nonTrivial)
                .map(this::toResourceIterator).collect(Collectors.toCollection(LinkedList::new));
        int threshold = its.size();
        Set<Node> result = new HashSet<>();

        while (!its.isEmpty()) {
            Iterator<Iterator<Resource>> it = its.iterator();
            while (it.hasNext()) {
                Iterator<Resource> rit = it.next();
                if (!rit.hasNext()) {
                    it.remove();
                    continue;
                }
                Resource res = rit.next();
                Set<Iterator<Resource>> iterators = observed.getOrDefault(res, null);
                if (iterators == null) observed.put(res, iterators = new HashSet<>());
                iterators.add(rit);
                if (iterators.size() == threshold)
                    result.add(new JenaNode(res));
            }
        }
        return result.iterator();
    }

    private ResIterator toResourceIterator(PropertySpec spec) {
        JenaNode predicate = (JenaNode) spec.getPredicate();
        JenaNode object = (JenaNode) spec.getObject();
        if (object == null) {
            assert predicate != null;
            return model.listSubjectsWithProperty(predicate.asProperty());
        } else if (predicate == null) {
            return model.listResourcesWithProperty(null, object.asRDFNode());
        } else {
            return model.listResourcesWithProperty(predicate.asProperty(), object.asRDFNode());
        }
    }

    @Nonnull
    @Override
    public Node getNode(@Nonnull String uri) {
        return new JenaNode(model.createResource(uri));
    }

    @Override
    public String toString() {
        return model.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JenaModelGraph that = (JenaModelGraph) o;

        return model.equals(that.model);
    }

    @Override
    public int hashCode() {
        return model.hashCode();
    }
}
