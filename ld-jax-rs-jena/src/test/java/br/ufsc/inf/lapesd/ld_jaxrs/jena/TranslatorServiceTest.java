package br.ufsc.inf.lapesd.ld_jaxrs.jena;

import br.ufsc.inf.lapesd.ld_jaxrs.jena.test.TranslatorService;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTestNg;
import org.glassfish.jersey.test.TestProperties;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.stream.Collectors;

public class TranslatorServiceTest extends JerseyTestNg.ContainerPerClassTest {
    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        ResourceConfig application = new ResourceConfig();
        JenaProviders.getProviders().forEach(application::register);
        application.register(TranslatorService.class);
        return application;
    }

    @Override
    protected void configureClient(ClientConfig config) {
        JenaProviders.getProviders().forEach(config::register);
    }

    @DataProvider
    public Object[][] translateData() {
        return ContentTypesTestData.get().stream()
                .flatMap(i -> ContentTypesTestData.getContentTypes().stream()
                        .map(ct -> new Object[] {i.resourcePath, i.contentType, ct}))
                .collect(Collectors.toList()).toArray(new Object[0][]);
    }

    @Test(dataProvider = "translateData")
    public void testTranslate(String resourcePath, String requestContentType,
                              String responseContentType) {
        MediaType requestMT = MediaType.valueOf(requestContentType);
        MediaType responseMT = MediaType.valueOf(responseContentType);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        Model request = ModelFactory.createDefaultModel();
        RDFDataMgr.read(request, inputStream, Lang.TURTLE);

        Model reply = target("translate").request(responseMT)
                .post(Entity.entity(request, requestMT), Model.class);
        Assert.assertEquals(reply.listStatements().toSet(), request.listStatements().toSet());
    }
}
