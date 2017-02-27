package br.ufsc.inf.lapesd.ld_jaxrs.jena;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.stream.Collectors;

@Test
public class WriteAndReadTest {
    @DataProvider
    public static Object[][] writeAndReadData() {
        return ContentTypesTestData.get().stream()
                .map(i -> new Object[] {i.resourcePath, i.contentType})
                .collect(Collectors.toList()).toArray(new Object[0][]);
    }

    @Test(dataProvider = "writeAndReadData")
    public void testWriteAndRead(String resourceTTLPath, String contentType) {
        ClassLoader cl = getClass().getClassLoader();
        InputStream modelInput = cl.getResourceAsStream(resourceTTLPath);
        Model model = ModelFactory.createDefaultModel();
        try {
            RDFDataMgr.read(model, modelInput, Lang.TURTLE);

            final boolean[] closed = {false};
            ByteArrayOutputStream out = new ByteArrayOutputStream() {
                @Override
                public void close() throws IOException {
                    closed[0] = true;
                    super.close();
                }
            };
            new ModelMessageBodyWriter().writeTo(model, model.getClass(),
                    Model.class.getGenericSuperclass(), new Annotation[]{},
                    MediaType.valueOf(contentType), new MultivaluedHashMap<>(), out);
            Assert.assertFalse(closed[0]);

            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray()) {
                @Override
                public void close() throws IOException {
                    closed[0] = true;
                    super.close();
                }
            };
            Assert.assertFalse(closed[0]);
            Model readModel = new ModelMessageBodyReader().readFrom(Model.class,
                    Model.class.getGenericSuperclass(), new Annotation[]{},
                    MediaType.valueOf(contentType), new MultivaluedHashMap<>(), in);
            Assert.assertEquals(readModel.listStatements().toSet(), model.listStatements().toSet());
        } catch (IOException e) {
            Assert.fail("writeTo/readFrom failed", e);
        } finally {
            model.close();
        }
    }
}
