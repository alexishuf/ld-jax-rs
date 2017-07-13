package br.ufsc.inf.lapesd.ld_jaxrs.model;

/**
 * A Graph that is accessed over SPARQL. In such scenario, the regular Graph methods are slow
 * and a Traverser implementation may instead attempt to issue a single SPARQL query.
 */
public interface SPARQLGraph extends Graph {
    String getServiceURI();
}
