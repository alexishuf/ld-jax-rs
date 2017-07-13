package br.ufsc.inf.lapesd.ld_jaxrs.jena;

import br.ufsc.inf.lapesd.ld_jaxrs.jena.test.DescribeService;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTestNg;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.core.Application;

public class JenaDescribeTraverserTest extends JerseyTestNg.ContainerPerClassTest {
    @Override
    protected void configureClient(ClientConfig config) {
        JenaProviders.getProviders().forEach(config::register);
    }

    @Override
    protected Application configure() {
        ResourceConfig application = new ResourceConfig();
        JenaProviders.getProviders().forEach(application::register);
        application.register(DescribeService.class);
        return application;
    }

    @Test
    public void testDescribe() {
        Model model = target("/describe/foaf-parkers.ttl").request("text/turtle")
                .get(Model.class);
        /* DESCRIBE is "informative", the following are reasonable assumptions */
        Assert.assertTrue(model.listStatements(null, FOAF.name, "Peter Parker").hasNext());
        Assert.assertFalse(model.listStatements(null, FOAF.name, "Benjamin Parker").hasNext());
    }
}
