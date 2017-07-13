package br.ufsc.inf.lapesd.ld_jaxrs.model;

/**
 * Interface for accessing nodes independent from the implementing library and access method.
 */
public interface Node {
    boolean isBlankNode();
    boolean isResource();
    boolean isLiteral();

    /**
     * The node URI, without prefixes.
     *
     * @throws IllegalArgumentException if <code>!isResource() || isBlank()</code>
     */
    String getURI();

    /**
     * The lexical form, without surrounding quotes, of the literal.
     *
     * This does not include any character escapes inserted during SPARQL generation:
     * \t \r \n \b \f \" \' \\
     *
     * @throws IllegalArgumentException if <code>!isLiteral()</code>
     */
    String getLexicalForm();

    /**
     * The language tag (without the @), or null if not present.
     *
     * @throws IllegalArgumentException if <code>!isLiteral()</code>
     */
    String getLanguage();

    /**
     * The datatype URI of the literal, or null for plain literals.
     *
     * @throws IllegalArgumentException if <code>!isLiteral()</code>
     */
    String getDatatypeURI();
}
