package br.ufsc.inf.lapesd.ld_jaxrs.model.rdf;

import br.ufsc.inf.lapesd.ld_jaxrs.model.Graph;
import br.ufsc.inf.lapesd.ld_jaxrs.model.Node;
import br.ufsc.inf.lapesd.ld_jaxrs.model.Triple;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Iterator;

public abstract class GraphTest  extends AbstractRDFModelTest {
    @Test
    public void testSimpleQuery() {
        Graph g = parseTurtle(openResource("statements.ttl"));
        Node r1 = g.getNode("http://example.org/ontology#r1");
        Iterator<Triple> it = g.query(r1, null, null);
        Assert.assertTrue(it.hasNext());
        Triple triple = it.next();
        Assert.assertEquals(triple.getObject(), g.getNode("http://example.org/ontology#r2"));
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void testBackwardQuery() {
        Graph g = parseTurtle(openResource("statements.ttl"));
        Node r2 = g.getNode("http://example.org/ontology#r2");
        Iterator<Triple> it = g.query(null, null, r2);
        Assert.assertTrue(it.hasNext());
        Triple triple = it.next();
        Assert.assertEquals(triple.getSubject(), g.getNode("http://example.org/ontology#r1"));
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void testQuerySubjectAndProperty() {
        Graph g = parseTurtle(openResource("statements.ttl"));
        Node r1 = g.getNode("http://example.org/ontology#r1");
        Node p1 = g.getNode("http://example.org/ontology#p1");
        Iterator<Triple> it = g.query(r1, p1, null);
        Assert.assertTrue(it.hasNext());
        Triple triple = it.next();
        Assert.assertEquals(triple.getObject(), g.getNode("http://example.org/ontology#r2"));
        Assert.assertFalse(it.hasNext());
    }


    @Test
    public void testQueryPropertyAndObject() {
        Graph g = parseTurtle(openResource("statements.ttl"));
        Node p1 = g.getNode("http://example.org/ontology#p1");
        Node r2 = g.getNode("http://example.org/ontology#r2");
        Iterator<Triple> it = g.query(null, p1, r2);
        Assert.assertTrue(it.hasNext());
        Triple triple = it.next();
        Assert.assertEquals(triple.getSubject(), g.getNode("http://example.org/ontology#r1"));
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void testQuerySubjectAndObject() {
        Graph g = parseTurtle(openResource("statements.ttl"));
        Node r1 = g.getNode("http://example.org/ontology#r1");
        Node r2 = g.getNode("http://example.org/ontology#r2");
        Iterator<Triple> it = g.query(r1, null, r2);
        Assert.assertTrue(it.hasNext());
        Triple triple = it.next();
        Assert.assertEquals(triple.getPredicate(), g.getNode("http://example.org/ontology#p1"));
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void testQueryFromLiteral() {
        Graph g = parseTurtle(openResource("statements.ttl"));
        Node r3 = g.getNode("http://example.org/ontology#r3");
        Iterator<Triple> it = g.query(r3, null, null);
        Assert.assertTrue(it.hasNext());
        Triple t = it.next();
        Node literal = t.getObject();
        Assert.assertTrue(literal.isLiteral());

        it = g.query(null, null, literal);
        Assert.assertTrue(it.hasNext());
        t = it.next();
        Assert.assertEquals(t.getSubject(), r3);
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void testQueryFromBlankNode() {
        Graph g = parseTurtle(openResource("statements.ttl"));
        Node r4 = g.getNode("http://example.org/ontology#r4");
        Iterator<Triple> it = g.query(r4, null, null);
        Assert.assertTrue(it.hasNext());
        Triple t = it.next();
        Node bnode = t.getObject();
        Assert.assertTrue(bnode.isBlankNode());

        it = g.query(null, null, bnode);
        Assert.assertTrue(it.hasNext());
        t = it.next();
        Assert.assertEquals(t.getSubject(), r4);
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void testQueryOpaqueTriples() {
        Graph g = parseTurtle(openResource("statements.ttl"));
        Node r3 = g.getNode("http://example.org/ontology#r3");
        Iterator<Triple> it = g.query(r3, null, null);
        Assert.assertTrue(it.hasNext());
        Triple t = it.next();
        Assert.assertTrue(t.getObject().isLiteral());
        Assert.assertFalse(it.hasNext());

        Node r4 = g.getNode("http://example.org/ontology#r4");
        it = g.query(r4, null, null);
        Assert.assertTrue(it.hasNext());
        t = it.next();
        Assert.assertTrue(t.getObject().isBlankNode());
        Assert.assertFalse(it.hasNext());
    }
}
