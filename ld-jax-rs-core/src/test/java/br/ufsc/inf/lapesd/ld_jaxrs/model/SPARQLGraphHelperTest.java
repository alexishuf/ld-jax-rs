package br.ufsc.inf.lapesd.ld_jaxrs.model;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SPARQLGraphHelperTest {
    @DataProvider
    public static Object[][] sparqlEscapesData() {
        return new Object[][] {
                {"asd \"coisa\"", "asd \\\"coisa\\\""},
                {"a\nb", "a\\nb"},
                {"a\bqq", "a\\bqq"}, /* spooky business */
                {"x\ry", "x\\ry"},
                {"x\ty", "x\\ty"},
                {"x\fy", "x\\fy"},
                {"'treco'", "\\'treco\\'"},
                {"c:\\windows", "c:\\\\windows"},
        };
    }

    @Test(dataProvider = "sparqlEscapesData")
    public void testSPARQLEscapes(String raw, String escaped) {
        Assert.assertEquals(SPARQLGraphHelper.escapeForSPARQL(raw), escaped);
    }
}