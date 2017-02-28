package br.ufsc.inf.lapesd.ld_jaxrs.jena.impl.model;

import br.ufsc.inf.lapesd.ld_jaxrs.model.Node;
import br.ufsc.inf.lapesd.ld_jaxrs.model.Triple;
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

    @Override
    public Node getSubject() {
        return new JenaNode(statement.getSubject());
    }
    @Override
    public Node getPredicate() {
        return new JenaNode(statement.getPredicate());
    }
    @Override
    public Node getObject() {
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
}
