package br.ufsc.inf.lapesd.ld_jaxrs.model.rdf;

import br.ufsc.inf.lapesd.ld_jaxrs.core.model.Graph;
import br.ufsc.inf.lapesd.ld_jaxrs.core.model.Node;
import br.ufsc.inf.lapesd.ld_jaxrs.core.model.Triple;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Iterator;

public abstract class TripleTest extends AbstractRDFModelTest {
    @Test
    public void testEqualsInsideGraph() {
        Graph g = parseTurtle(openResource("statements.ttl"));
        Node r1 = g.getNode("http://example.org/ontology#r1");
        Node r2 = g.getNode("http://example.org/ontology#r2");
        Iterator<Triple> it = g.query(r1, null, null);
        Assert.assertTrue(it.hasNext());
        Triple t1 = it.next();

        it = g.query(null, null, r2);
        Assert.assertTrue(it.hasNext());
        Triple t2 = it.next();
        Assert.assertEquals(t1, t2);
    }

    @Test
    public void testEqualsBetweenGraphs() {
        Graph g1 = parseTurtle(openResource("statements.ttl"));
        Graph g2 = parseTurtle(openResource("statements.ttl"));
        Node r1 = g1.getNode("http://example.org/ontology#r1");
        Node r2 = g2.getNode("http://example.org/ontology#r2");
        Iterator<Triple> it = g1.query(r1, null, null);
        Assert.assertTrue(it.hasNext());
        Triple t1 = it.next();

        it = g2.query(null, null, r2);
        Assert.assertTrue(it.hasNext());
        Triple t2 = it.next();
        Assert.assertEquals(t1, t2);
    }

    @Test
    public void testGetParts() {
        Graph g = parseTurtle(openResource("statements.ttl"));
        Node r1 = g.getNode("http://example.org/ontology#r1");
        Node p1 = g.getNode("http://example.org/ontology#p1");
        Node r2 = g.getNode("http://example.org/ontology#r2");

        Iterator<Triple> it = g.query(r1, null, null);
        Assert.assertTrue(it.hasNext());
        Triple triple = it.next();
        Assert.assertEquals(triple.getSubject(), r1);
        Assert.assertEquals(triple.getPredicate(), p1);
        Assert.assertEquals(triple.getObject(), r2);
    }
}
