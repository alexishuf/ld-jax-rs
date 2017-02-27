package br.ufsc.inf.lapesd.ld_jaxrs.jena;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.riot.RDFWriterRegistry;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.Produces;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Tests that could be replaced by compile-time assertions
 */
@Test
public class CompileTests {

    @Test
    public void testModelWriterContentTypes() {
        Set<String> declared = Stream.of(ModelMessageBodyWriter.class
                .getAnnotation(Produces.class).value()).collect(Collectors.toSet());
        Set<String> supported = RDFWriterRegistry.registeredGraphFormats().stream()
                .map(RDFFormat::getLang).filter(l -> l != Lang.RDFNULL)
                .map(Lang::getAltContentTypes).flatMap(List::stream)
                .collect(Collectors.toSet());
//        System.out.printf("Supported Content-Types: %s\n", supported.stream()
//                .map(ct -> "\"" + ct + "\"").reduce((l, r) -> l + ", " + r).orElse(""));
        Assert.assertEquals(declared, supported);
        Assert.assertEquals(ModelMessageBodyWriter.getSupportedContentTypes(), declared);
    }

    public void testModelReaderSupportsSomething() {
        Assert.assertFalse(ModelMessageBodyReader.getSupportedMediaTypes().isEmpty());
    }

}
