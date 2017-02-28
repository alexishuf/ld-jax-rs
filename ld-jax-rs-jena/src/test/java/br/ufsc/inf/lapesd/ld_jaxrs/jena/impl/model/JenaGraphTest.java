package br.ufsc.inf.lapesd.ld_jaxrs.jena.impl.model;

import br.ufsc.inf.lapesd.ld_jaxrs.model.Graph;
import br.ufsc.inf.lapesd.ld_jaxrs.model.rdf.GraphTest;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.InputStream;

public class JenaGraphTest extends GraphTest {
    @Override
    protected Graph parseTurtle(InputStream inputStream) {
        Model model = ModelFactory.createDefaultModel();
        RDFDataMgr.read(model, inputStream, Lang.TURTLE);
        return new JenaModelGraph(model);
    }
}
