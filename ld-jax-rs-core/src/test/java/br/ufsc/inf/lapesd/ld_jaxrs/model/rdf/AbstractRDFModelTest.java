package br.ufsc.inf.lapesd.ld_jaxrs.model.rdf;

import br.ufsc.inf.lapesd.ld_jaxrs.core.model.Graph;

import java.io.InputStream;

public abstract class AbstractRDFModelTest {
    private String resDir = "ld_jaxrs/model/rdf/";

    protected abstract Graph parseTurtle(InputStream inputStream);

    public InputStream openResource(String fileName) {
        return Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(resDir + fileName);
    }
}
