package br.ufsc.inf.lapesd.ld_jaxrs.jena.impl.model;

import br.ufsc.inf.lapesd.ld_jaxrs.core.model.Node;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import javax.annotation.Nonnull;

/**
 * Wraps a {@link RDFNode} as a Node
 */
public final class JenaNode implements Node {
    private final RDFNode node;

    public JenaNode(@Nonnull RDFNode node) {
        this.node = node;
    }

    public RDFNode asRDFNode() { return node; }
    public Resource asResource() { return node.asResource(); }
    public Property asProperty() { return node.as(Property.class); }

    @Override
    public boolean isBlankNode() { return node.isAnon(); }
    @Override
    public boolean isResource() { return node.isResource(); }
    @Override
    public boolean isLiteral() { return node.isLiteral(); }

    @Override
    public String toString() {
        return node.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JenaNode jenaNode = (JenaNode) o;

        return node.equals(jenaNode.node);
    }

    @Override
    public int hashCode() {
        return node.hashCode();
    }
}
