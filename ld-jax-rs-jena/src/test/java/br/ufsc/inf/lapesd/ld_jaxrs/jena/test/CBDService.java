package br.ufsc.inf.lapesd.ld_jaxrs.jena.test;

import br.ufsc.inf.lapesd.ld_jaxrs.CBD;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Path("cbd/{resourceFile:.*}")
public class CBDService {
    private static final String dir = "cbd/";
    private static final Resource TLR =
            ResourceFactory.createResource("http://example.org/cbd#TLR");

    @GET
    @CBD
    public Resource get(@PathParam("resourceFile") String resourceFile) {
        InputStream stream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(dir + resourceFile);
        Model model = ModelFactory.createDefaultModel();
        RDFDataMgr.read(model, stream, Lang.TURTLE);
        ResIterator it = model.listSubjectsWithProperty(RDF.type, TLR);
        if (!it.hasNext()) throw new WebApplicationException(Response.Status.NOT_FOUND);
        return it.next();

    }
}
