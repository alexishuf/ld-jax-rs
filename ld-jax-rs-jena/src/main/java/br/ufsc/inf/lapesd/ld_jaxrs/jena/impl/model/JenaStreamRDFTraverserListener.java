package br.ufsc.inf.lapesd.ld_jaxrs.jena.impl.model;

import br.ufsc.inf.lapesd.ld_jaxrs.core.traverser.TraverserListener;
import br.ufsc.inf.lapesd.ld_jaxrs.model.Triple;
import org.apache.jena.riot.system.StreamRDF;

/**
 * Wraps a {@link org.apache.jena.riot.system.StreamRDF} as a {@link TraverserListener}
 */
public class JenaStreamRDFTraverserListener implements TraverserListener {
    private StreamRDF streamRDF;

    @Override
    public void add(Triple triple) {
        streamRDF.triple(JenaTriple.toTriple(triple));
    }
}
