package br.ufsc.inf.lapesd.ld_jaxrs.inmemory;

import br.ufsc.inf.lapesd.ld_jaxrs.core.traverser.TraverserListener;
import br.ufsc.inf.lapesd.ld_jaxrs.model.Graph;
import br.ufsc.inf.lapesd.ld_jaxrs.model.Node;
import br.ufsc.inf.lapesd.ld_jaxrs.model.PropertySpec;
import br.ufsc.inf.lapesd.ld_jaxrs.model.Triple;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class MemGraph implements Graph, TraverserListener {
    private HashSet<Triple> triples = new HashSet<>();
    private final SetMultimap<Node, Triple> subjectIndex = HashMultimap.create();
    private final SetMultimap<Node, Triple> predicateIndex = HashMultimap.create();
    private final SetMultimap<Node, Triple> objectIndex = HashMultimap.create();
    private final Map<String, Node> uriMap = new HashMap<>();

    public void add(Triple triple) {
        triples.add(triple);
        registerURI(triple.getSubject());
        registerURI(triple.getPredicate());
        registerURI(triple.getObject());
        subjectIndex.put(triple.getSubject(), triple);
        predicateIndex.put(triple.getPredicate(), triple);
        objectIndex.put(triple.getObject(), triple);
    }

    public MemGraph add(@Nonnull Node subject, @Nonnull Node predicate, @Nonnull Node object) {
        add(new MemTriple(subject, predicate, object));
        return this;
    }
    public MemGraph add(@Nonnull String subject, @Nonnull Node predicate, @Nonnull Node object) {
        add(new MemTriple(MemNode.createNamed(subject), predicate, object));
        return this;
    }
    public MemGraph add(@Nonnull String subject, @Nonnull String predicate, @Nonnull Node object) {
        add(new MemTriple(MemNode.createNamed(subject), MemNode.createNamed(predicate), object));
        return this;
    }
    public MemGraph add(@Nonnull String subject, @Nonnull String predicate, @Nonnull String object) {
        add(new MemTriple(MemNode.createNamed(subject), MemNode.createNamed(predicate),
                MemNode.createNamed(object)));
        return this;
    }
    public MemGraph add(@Nonnull Node subject, @Nonnull String predicate, @Nonnull Node object) {
        add(new MemTriple(subject, MemNode.createNamed(predicate), object));
        return this;
    }
    public MemGraph add(@Nonnull Node subject, @Nonnull String predicate, @Nonnull String object) {
        add(new MemTriple(subject, MemNode.createNamed(predicate), MemNode.createNamed(object)));
        return this;
    }
    public MemGraph add(@Nonnull Node subject, @Nonnull Node predicate, @Nonnull String object) {
        add(new MemTriple(subject, predicate, MemNode.createNamed(object)));
        return this;
    }

    private void registerURI(Node node) {
        if (node.isResource() && !node.isBlankNode()) {
            String uri = ((MemNode) node).getURI();
            uriMap.put(uri, node);
        }
    }

    @Nonnull
    public Iterator<Triple> query(@Nullable Node subject, @Nullable Node predicate, @Nullable Node object) {
        Set<Triple> set = null;
        if (subject != null) set = subjectIndex.get(subject);
        if (predicate != null) set = join(set, predicateIndex.get(predicate));
        if (object != null) set = join(set, objectIndex.get(object));

        return (set == null ? subjectIndex.values() : set).iterator();
    }

    private <T> Set<T> join(@Nullable Set<T> a, @Nonnull Set<T> b) {
        if (a == null) return b;
        HashSet<T> join = new HashSet<>(a);
        join.retainAll(b);
        return join;
    }

    @Nonnull
    public Iterator<Node> querySubjects(PropertySpec... properties) {
        Set<Node> set = null;
        for (PropertySpec spec : properties) {
            Set<Node> specSet;
            specSet = new HashSet<>();
            Iterator<Triple> it = query(null, spec.getPredicate(), spec.getObject());
            while (it.hasNext())
                specSet.add(it.next().getSubject());
            set = join(set, specSet);
        }
        return (set == null ? Collections.<Node>emptySet() : set).iterator();
    }

    @Nonnull
    public Node getNode(@Nonnull String uri) {
        if (!uriMap.containsKey(uri)) {
            MemNode node = MemNode.createNamed(uri);
            uriMap.put(uri, node);
            return node;
        }
        return uriMap.get(uri);
    }

    @Override
    public String toString() {
        String str = "";
        for (Triple triple : subjectIndex.values()) {
            str += triple.toString() + "; ";
        }
        return str.isEmpty() ? str : str.substring(0, str.length()-2);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MemGraph)) return false;
        MemGraph rhs = (MemGraph)o;
        return triples.equals(rhs.triples);
    }

    @Override
    public int hashCode() {
        return triples.hashCode();
    }
}
