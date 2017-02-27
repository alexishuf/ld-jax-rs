package br.ufsc.inf.lapesd.ld_jaxrs.core.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PropertySpec {
    private final Node predicate;
    private final Node object;

    public PropertySpec(@Nullable Node predicate, @Nullable Node object) {
        this.predicate = predicate;
        this.object = object;
    }

    @Nullable public Node getPredicate() {
        return predicate;
    }
    @Nullable public Node getObject() {
        return object;
    }
    public boolean nonTrivial() { return !isTrivial(); }
    public boolean isTrivial() { return predicate == null && object == null; }

    public boolean matches(@Nonnull Node predicate, @Nonnull Node object) {
        if (this.predicate != null && !this.predicate.equals(predicate)) return false;
        if (this.object != null && !this.object.equals(object)) return false;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PropertySpec rhs = (PropertySpec) o;

        if (predicate != null ? !predicate.equals(rhs.predicate) : rhs.predicate != null)
            return false;
        return object != null ? object.equals(rhs.object) : rhs.object == null;
    }

    @Override
    public int hashCode() {
        int result = predicate != null ? predicate.hashCode() : 0;
        result = 31 * result + (object != null ? object.hashCode() : 0);
        return result;
    }
}
