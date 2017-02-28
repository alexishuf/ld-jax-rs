package br.ufsc.inf.lapesd.ld_jaxrs.model.rdf;

import br.ufsc.inf.lapesd.ld_jaxrs.model.Graph;
import br.ufsc.inf.lapesd.ld_jaxrs.model.Node;
import org.testng.Assert;
import org.testng.annotations.Test;

public abstract class NodeTest extends AbstractRDFModelTest {
    @Test
    public void testEqualsInsideGraph() {
        Graph g = parseTurtle(openResource("statements.ttl"));
        Node a = g.getNode("http://example.org/ontology#r3");
        Node b = g.getNode("http://example.org/ontology#r3");
        Assert.assertEquals(a, b);
        Assert.assertEquals(a.hashCode(), b.hashCode());

        a = g.getNode("http://example.org/ontology#r2");
        b = g.getNode("http://example.org/ontology#r2");
        Assert.assertEquals(a, b);
        Assert.assertEquals(a.hashCode(), b.hashCode());

        a = g.getNode("http://example.org/ontology#r1");
        b = g.getNode("http://example.org/ontology#r1");
        Assert.assertEquals(a, b);
        Assert.assertEquals(a.hashCode(), b.hashCode());

        a = g.getNode("http://example.org/ontology#r4");
        b = g.getNode("http://example.org/ontology#r4");
        Assert.assertEquals(a, b);
        Assert.assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testEqualsBetweenGraphs() {
        Graph g1 = parseTurtle(openResource("statements.ttl"));
        Graph g2 = parseTurtle(openResource("statements.ttl"));
        Node a = g1.getNode("http://example.org/ontology#r3");
        Node b = g2.getNode("http://example.org/ontology#r3");
        Assert.assertEquals(a, b);
        Assert.assertEquals(a.hashCode(), b.hashCode());

        a = g1.getNode("http://example.org/ontology#r2");
        b = g2.getNode("http://example.org/ontology#r2");
        Assert.assertEquals(a, b);
        Assert.assertEquals(a.hashCode(), b.hashCode());

        a = g1.getNode("http://example.org/ontology#r1");
        b = g2.getNode("http://example.org/ontology#r1");
        Assert.assertEquals(a, b);
        Assert.assertEquals(a.hashCode(), b.hashCode());

        a = g1.getNode("http://example.org/ontology#r4");
        b = g2.getNode("http://example.org/ontology#r4");
        Assert.assertEquals(a, b);
        Assert.assertEquals(a.hashCode(), b.hashCode());
    }

}
