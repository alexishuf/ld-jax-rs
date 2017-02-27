package br.ufsc.inf.lapesd.ld_jaxrs.jena;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParserRegistry;
import org.apache.jena.shared.JenaException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.stream.Collectors;

import static br.ufsc.inf.lapesd.ld_jaxrs.jena.Utils.simplify;

/**
 * Reads RDF from a stream into a Jena {@link Model}.
 */
@Provider
public class ModelMessageBodyReader implements MessageBodyReader<Model> {
    private static Set<String> supportedMediaTypes;

    public static Set<String> getSupportedMediaTypes() {
        if (supportedMediaTypes == null) {
            supportedMediaTypes = RDFLanguages.getRegisteredLanguages().stream()
                    .filter(l -> l != Lang.RDFNULL)
                    .filter(RDFParserRegistry::isTriples)
                    .flatMap(l -> l.getAltContentTypes().stream()).collect(Collectors.toSet());
        }
        return supportedMediaTypes;
    }

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations,
                              MediaType mediaType) {
        return aClass == Model.class && getSupportedMediaTypes().contains(simplify(mediaType));
    }

    @Override
    public Model readFrom(Class<Model> aClass, Type type, Annotation[] annotations,
                          MediaType mediaType, MultivaluedMap<String, String> multivaluedMap,
                          InputStream inputStream) throws IOException, WebApplicationException {
        Lang lang = Utils.toLangOrThrow(mediaType);
        try {
            Model model = ModelFactory.createDefaultModel();
            RDFDataMgr.read(model, inputStream, lang);
            return model;
        } catch (JenaException e) {
            if (Utils.hasIOExceptionCause(e)) throw new IOException(e);
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
