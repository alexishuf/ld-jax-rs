package br.ufsc.inf.lapesd.ld_jaxrs.model;

import javax.annotation.Nonnull;

public class SimpleTriple implements Triple {
    private final @Nonnull Node subject, predicate, object;

    public SimpleTriple(@Nonnull Node subject, @Nonnull Node predicate, @Nonnull Node object) {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }

    @Override
    public Node getSubject() {
        return subject;
    }

    @Override
    public Node getPredicate() {
        return predicate;
    }

    @Override
    public Node getObject() {
        return object;
    }
}
