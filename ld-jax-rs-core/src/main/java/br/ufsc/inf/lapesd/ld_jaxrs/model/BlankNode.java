package br.ufsc.inf.lapesd.ld_jaxrs.model;

/**
 * A blank node
 */
public class BlankNode implements Node {
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
}
