package br.ufsc.inf.lapesd.ld_jaxrs.core.model;

/**
 * Interface for accessing triples independent from the implementing library and access method
 */
public interface Triple {
    Node getSubject();
    Node getPredicate();
    Node getObject();
}
