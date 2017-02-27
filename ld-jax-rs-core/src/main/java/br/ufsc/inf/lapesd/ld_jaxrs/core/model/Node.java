package br.ufsc.inf.lapesd.ld_jaxrs.core.model;

/**
 * Interface for accessing nodes independent from the implementing library and access method.
 */
public interface Node {
    boolean isBlankNode();
    boolean isResource();
    boolean isLiteral();
}
