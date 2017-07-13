package br.ufsc.inf.lapesd.ld_jaxrs.model;

import javax.annotation.Nonnull;

/**
 * A blank node represented by an implementation-specific string.
 */
public class SPARQLBlankNode implements Node {
    private final @Nonnull String id;

    public SPARQLBlankNode(@Nonnull String id) {
        this.id = id;
    }

    @Nonnull
    public String getSPARQLString() {
        return id;
    }

    @Override
    public boolean isBlankNode() {
        return true;
    }

    @Override
    public boolean isResource() {
        return true;
    }

    @Override
    public boolean isLiteral() {
        return false;
    }

    @Override
    public String getURI() {
        return null;
    }

    @Override
    public String getLexicalForm() {
        throw new IllegalArgumentException("Not a literal");
    }

    @Override
    public String getLanguage() {
        throw new IllegalArgumentException("Not a literal");
    }

    @Override
    public String getDatatypeURI() {
        throw new IllegalArgumentException("Not a literal");
    }

    @Override
    public String toString() {
        return getSPARQLString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SPARQLBlankNode that = (SPARQLBlankNode) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
