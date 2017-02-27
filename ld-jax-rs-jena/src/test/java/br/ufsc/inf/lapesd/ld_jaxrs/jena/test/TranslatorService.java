package br.ufsc.inf.lapesd.ld_jaxrs.jena.test;

import org.apache.jena.rdf.model.Model;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("translate")
public class TranslatorService {

    @POST
    @Consumes("*/*")
    @Produces("*/*")
    public Model post(Model model) {
        return model;
    }
}
