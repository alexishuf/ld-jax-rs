package br.ufsc.inf.lapesd.ld_jaxrs.model;

import javax.annotation.Nonnull;

/**
 * A custom-made Literal node
 */
public class SimpleLiteral implements Node {
    private @Nonnull final String lexicalForm;
    private final String language, datatypeURI;

    private SimpleLiteral(@Nonnull String lexicalForm, String language, String datatypeURI) {
        this.lexicalForm = lexicalForm;
        this.language = language;
        this.datatypeURI = datatypeURI;
    }

    public static SimpleLiteral createPlain(String lexicalForm) {
        return new SimpleLiteral(lexicalForm, null, null);
    }
    public static SimpleLiteral createLang(String lexicalForm, String language) {
        return new SimpleLiteral(lexicalForm, language, null);
    }
    public static SimpleLiteral createTyped(String lexicalForm, String datatypeURI) {
        return new SimpleLiteral(lexicalForm, null, datatypeURI);
    }

    @Override
    public boolean isBlankNode() {
        return false;
    }

    @Override
    public boolean isResource() {
        return false;
    }

    @Override
    public boolean isLiteral() {
        return true;
    }

    @Override
    public String getURI() {
        throw new IllegalArgumentException("Not an URI Resource");
    }

    @Nonnull
    @Override
    public String getLexicalForm() {
        return lexicalForm;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
    public String getDatatypeURI() {
        return datatypeURI;
    }
}
