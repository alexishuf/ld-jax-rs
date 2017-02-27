package br.ufsc.inf.lapesd.ld_jaxrs.jena;

import br.ufsc.inf.lapesd.ld_jaxrs.core.traverser.Traverser;
import br.ufsc.inf.lapesd.ld_jaxrs.core.traverser.TraverserRegistry;
import br.ufsc.inf.lapesd.ld_jaxrs.jena.impl.model.JenaModelGraph;
import br.ufsc.inf.lapesd.ld_jaxrs.jena.impl.model.JenaNode;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * Writes a {@link Resource} to a JAX-RS message.
 */
@Provider
@Produces({"application/n-quads", "application/ld+json", "application/rdf+thrift",
           "application/x-turtle", "application/x-trig", "application/rdf+xml", "text/turtle",
           "application/trix", "application/turtle", "text/n-quads", "application/rdf+json",
           "application/trix+xml", "application/trig", "text/trig", "application/n-triples",
           "text/nquads", "text/plain"})
public class ResourceMessageBodyWriter implements MessageBodyWriter<Resource> {
    public static Set<String> getSupportedContentTypes() {
        return ModelMessageBodyWriter.getSupportedContentTypes();
    }

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations,
                               MediaType mediaType) {
        return Resource.class.isAssignableFrom(aClass)
                && getSupportedContentTypes().contains(Utils.simplify(mediaType));
    }

    @Override
    public long getSize(Resource resource, Class<?> aClass, Type type, Annotation[] annotations,
                        MediaType mediaType) {
        //deprecated in JAX-RS 2.0
        return -1;
    }

    @Override
    public void writeTo(Resource resource, Class<?> aClass, Type type, Annotation[] annotations,
                        MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap,
                        OutputStream outputStream) throws IOException, WebApplicationException {
        Traverser traverser = TraverserRegistry.get().selectOrWebApplicationException(annotations);
        Model out = ModelFactory.createDefaultModel();
        try {
            traverser.traverse(new JenaModelGraph(resource.getModel()), new JenaNode(resource),
                    new JenaModelGraph(out));
            new ModelMessageBodyWriter().writeTo(out, out.getClass(),
                    out.getClass().getGenericSuperclass(), annotations, mediaType,
                    multivaluedMap, outputStream);
        } finally {
            out.close();
        }
    }
}
