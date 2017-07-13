package br.ufsc.inf.lapesd.ld_jaxrs.jena;

import br.ufsc.inf.lapesd.ld_jaxrs.core.traverser.CBDTraverser;
import br.ufsc.inf.lapesd.ld_jaxrs.jena.impl.model.JenaModelGraph;
import br.ufsc.inf.lapesd.ld_jaxrs.jena.impl.model.JenaNode;
import br.ufsc.inf.lapesd.ld_jaxrs.jena.impl.model.JenaSPARQLServiceGraph;
import br.ufsc.inf.lapesd.ld_jaxrs.model.StringURINode;
import org.apache.jena.fuseki.embedded.FusekiEmbeddedServer;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.InputStream;

public class CBDTraverserOverSPARQLTest {
    private FusekiEmbeddedServer fuseki;
    private Model model;
    private Dataset dataset;
    private String serviceURI;

    @BeforeClass
    public void setUp() {
        model = ModelFactory.createDefaultModel();
        InputStream in = getClass().getClassLoader().getResourceAsStream("Alan_Turing.n3");
        RDFDataMgr.read(model, in, Lang.TURTLE);

        dataset = DatasetFactory.create(model);
        fuseki = FusekiEmbeddedServer.create()
                .add("/data", dataset)
                .setPort(0).setLoopback(true).build();
        fuseki.start();
        serviceURI = String.format("http://localhost:%d/data", fuseki.getPort());
    }

    @AfterClass
    public void tearDown() {
        fuseki.stop();
        fuseki.join();
        dataset.close();
        model.close();
    }

    @Test
    public void testSameCBDAsLocal() {
        JenaSPARQLServiceGraph graph = new JenaSPARQLServiceGraph(serviceURI,
                q -> QueryExecutionFactory.sparqlService(serviceURI, q));

        Model actual = ModelFactory.createDefaultModel();
        new CBDTraverser().traverse(graph,
                new StringURINode("http://dbpedia.org/resource/Alan_Turing"),
                new JenaModelGraph(actual));

        Model expected = ModelFactory.createDefaultModel();
        new CBDTraverser().traverse(new JenaModelGraph(model),
                new JenaNode(model.createResource("http://dbpedia.org/resource/Alan_Turing")),
                new JenaModelGraph(expected));

        Assert.assertTrue(actual.isIsomorphicWith(expected));
    }

}
