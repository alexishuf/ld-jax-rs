package br.ufsc.inf.lapesd.ld_jaxrs.model;

import javax.annotation.Nonnull;

/**
 * A Node represented by a URI string
 */
public class StringURINode implements Node {
    private final @Nonnull String uri;

    public StringURINode(@Nonnull String uri) {
        this.uri = uri;
    }

    @Override
    public @Nonnull String getURI() {
        return uri;
    }

    @Override
    public boolean isBlankNode() {
        return false;
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
        return getURI();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringURINode that = (StringURINode) o;

        return uri.equals(that.uri);
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }
}
