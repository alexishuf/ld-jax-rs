package br.ufsc.inf.lapesd.ld_jaxrs.jena;

import br.ufsc.inf.lapesd.ld_jaxrs.jena.test.CBDService;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTestNg;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import java.io.InputStream;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceWriteTest extends JerseyTestNg.ContainerPerClassTest {

    @DataProvider
    public static Object[][] writeOneResourceData() {
        return ContentTypesTestData.getContentTypes().stream().map(ct -> new Object[] {ct})
                .collect(Collectors.toSet()).toArray(new Object[0][]);
    }

    @Override
    protected void configureClient(ClientConfig config) {
        JenaProviders.getProviders().forEach(config::register);
    }

    @Override
    protected Application configure() {
        ResourceConfig application = new ResourceConfig();
        JenaProviders.getProviders().forEach(application::register);
        application.register(CBDService.class);
        return application;
    }

    @Test
    public void testProduces() {
        Assert.assertEquals(ResourceMessageBodyWriter.getSupportedContentTypes(),
                ModelMessageBodyWriter.getSupportedContentTypes());
        Set<String> actual = Stream.of(ResourceMessageBodyWriter.class
                .getAnnotation(Produces.class).value()).collect(Collectors.toSet());
        Assert.assertEquals(actual, ResourceMessageBodyWriter.getSupportedContentTypes());
    }

    @Test(dataProvider = "writeOneResourceData")
    public void testWriteOneResource(String contentType) {
        Model model = null, expected = null;
        try {
            model = target("cbd/foaf-parkers.ttl").request(contentType)
                .get(Model.class);
            InputStream stream = getClass().getClassLoader()
                    .getResourceAsStream("cbd/foaf-peter-parker.ttl");

            expected = ModelFactory.createDefaultModel();
            RDFDataMgr.read(expected, stream, Lang.TURTLE);
            Assert.assertTrue(model.isIsomorphicWith(expected));
        } finally {
            if (expected != null) expected.close();
            if (model != null) model.close();
        }
    }
}
