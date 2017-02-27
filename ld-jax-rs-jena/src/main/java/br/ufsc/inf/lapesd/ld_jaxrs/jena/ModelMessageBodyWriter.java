package br.ufsc.inf.lapesd.ld_jaxrs.jena;


import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.shared.JenaException;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link MessageBodyWriter} for a Jena {@link Model}. Writes the whole model in the syntax
 * specified by the requested media type.
 */
@Provider
@Produces({"application/n-quads", "application/ld+json", "application/rdf+thrift",
           "application/x-turtle", "application/x-trig", "application/rdf+xml", "text/turtle",
           "application/trix", "application/turtle", "text/n-quads", "application/rdf+json",
           "application/trix+xml", "application/trig", "text/trig", "application/n-triples",
           "text/nquads", "text/plain"})
public class ModelMessageBodyWriter implements MessageBodyWriter<Model> {

    private static Set<String> supportedContentTypes;

    public static Set<String> getSupportedContentTypes() {
        if (supportedContentTypes == null) {
            supportedContentTypes = Collections.unmodifiableSet(
                    Stream.of(ModelMessageBodyWriter.class.getAnnotation(Produces.class).value())
                            .collect(Collectors.toSet()));
        }
        return supportedContentTypes;
    }

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations,
                               MediaType mediaType) {
        return Model.class.isAssignableFrom(aClass)
                && getSupportedContentTypes().contains(Utils.simplify(mediaType));
    }

    @Override
    public long getSize(Model model, Class<?> aClass, Type type, Annotation[] annotations,
                        MediaType mediaType) {
        //deprecated in JAX-RS 2.0
        return -1;
    }

    @Override
    public void writeTo(Model model, Class<?> aClass, Type type, Annotation[] annotations,
                        MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap,
                        OutputStream outputStream) throws IOException, WebApplicationException {
        Lang lang = Utils.toLangOrThrow(mediaType);
        try {
            //TODO choose RDFFormat variants (e.g., turtle blocks)
            //TODO Consider parameters in mediaType (encoding?)
            RDFDataMgr.write(outputStream, model, lang);
        } catch (JenaException e) {
            if (Utils.hasIOExceptionCause(e)) throw new IOException(e);
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
