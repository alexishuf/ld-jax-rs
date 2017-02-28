package br.ufsc.inf.lapesd.ld_jaxrs.inmemory;

import br.ufsc.inf.lapesd.ld_jaxrs.model.Node;
import br.ufsc.inf.lapesd.ld_jaxrs.model.Triple;

import javax.annotation.Nonnull;

public class MemTriple implements Triple {
    private Node subject, predicate, object;

    public MemTriple(@Nonnull Node subject, @Nonnull Node predicate, @Nonnull Node object) {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }

    @Nonnull public Node getSubject() { return subject; }
    @Nonnull public Node getPredicate() { return predicate; }
    @Nonnull public Node getObject() { return object; }

    @Override
    public String toString() {
        return String.format("%s %s %s",
                subject.toString(), predicate.toString(), object.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemTriple memTriple = (MemTriple) o;

        if (!subject.equals(memTriple.subject)) return false;
        if (!predicate.equals(memTriple.predicate)) return false;
        return object.equals(memTriple.object);
    }

    @Override
    public int hashCode() {
        int result = subject.hashCode();
        result = 31 * result + predicate.hashCode();
        result = 31 * result + object.hashCode();
        return result;
    }
}
