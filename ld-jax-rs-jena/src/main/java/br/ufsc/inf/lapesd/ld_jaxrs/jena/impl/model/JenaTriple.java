package br.ufsc.inf.lapesd.ld_jaxrs.jena.impl.model;

import br.ufsc.inf.lapesd.ld_jaxrs.model.Triple;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;

import javax.annotation.Nonnull;

/**
 * Wrapper of {@link Statement} as a {@link Triple}
 */
public final class JenaTriple implements Triple {
    private final Statement statement;

    public JenaTriple(@Nonnull Statement statement) {
        this.statement = statement;
    }

    @Nonnull
    public Statement getStatement() { return statement; }

    public org.apache.jena.graph.Triple getTriple() {
        return toTriple(this);
    }

    @Override
    public JenaNode getSubject() {
        return new JenaNode(statement.getSubject());
    }
    @Override
    public JenaNode getPredicate() {
        return new JenaNode(statement.getPredicate());
    }
    @Override
    public JenaNode getObject() {
        return new JenaNode(statement.getObject());
    }

    @Override
    public String toString() {
        return statement.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JenaTriple that = (JenaTriple) o;

        return statement.equals(that.statement);
    }

    @Override
    public int hashCode() {
        return statement.hashCode();
    }

    public static org.apache.jena.graph.Triple toTriple(Triple triple) {
        RDFNode s = JenaNode.toRDFNode(triple.getSubject());
        RDFNode p = JenaNode.toRDFNode(triple.getPredicate());
        RDFNode o = JenaNode.toRDFNode(triple.getObject());
        return new org.apache.jena.graph.Triple(s.asNode(), p.asNode(), o.asNode());
    }
}
