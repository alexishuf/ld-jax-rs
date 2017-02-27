package br.ufsc.inf.lapesd.ld_jaxrs.jena;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFLanguages;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

class Utils {
    static String simplify(MediaType mediaType) {
        return mediaType.getType() + "/" + mediaType.getSubtype();
    }

    static Lang toLangOrThrow(MediaType mediaType) {
        String simplified = Utils.simplify(mediaType);
        Lang lang = RDFLanguages.contentTypeToLang(simplified);
        if (lang == null) {
            throw new WebApplicationException("There is no Jena Lang registered for " + simplified,
                    Response.Status.INTERNAL_SERVER_ERROR);
        }
        return lang;
    }

    static boolean hasIOExceptionCause(Exception e) {
        for (Throwable cause = e.getCause(); cause != null; cause = cause.getCause()) {
            if (cause instanceof IOException) return true;
        }
        return false;
    }
}
