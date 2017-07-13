package br.ufsc.inf.lapesd.ld_jaxrs.jena;

import br.ufsc.inf.lapesd.ld_jaxrs.SPARQLDescribe;
import br.ufsc.inf.lapesd.ld_jaxrs.core.traverser.TraversalStrategy;
import br.ufsc.inf.lapesd.ld_jaxrs.core.traverser.Traverser;
import br.ufsc.inf.lapesd.ld_jaxrs.core.traverser.TraverserListener;
import br.ufsc.inf.lapesd.ld_jaxrs.core.traverser.TraverserRegistry;
import br.ufsc.inf.lapesd.ld_jaxrs.jena.impl.model.JenaModelGraph;
import br.ufsc.inf.lapesd.ld_jaxrs.jena.impl.model.JenaSPARQLServiceGraph;
import br.ufsc.inf.lapesd.ld_jaxrs.jena.impl.model.JenaTriple;
import br.ufsc.inf.lapesd.ld_jaxrs.model.*;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.StmtIterator;

import javax.annotation.Nonnull;

/**
 * A {@link Traverser} implementation that simply issues a SPARQL DESCRIBE. This requires the
 * Graph instance to be a {@link PublicSPARQLGraph} or a {@link JenaModelGraph}.
 *
 * If the SPARQL endpoint requires authentication, use {@link JenaSPARQLServiceGraph}
 */
@TraversalStrategy(SPARQLDescribe.class)
public class JenaDescribeTraverser implements Traverser {
    public JenaDescribeTraverser() {}

    /**
     * Same as JenaDescribeTraverser(). Required by {@link TraverserRegistry}
     * @param annotation ignored
     */
    public JenaDescribeTraverser(SPARQLDescribe annotation) {}

    @Override
    public boolean supportsGraph(@Nonnull Graph graph) {
        return (graph instanceof JenaModelGraph) || (graph instanceof PublicSPARQLGraph);
    }

    @Override
    public void traverse(@Nonnull Graph graph, @Nonnull Node node, @Nonnull TraverserListener listener) {
        if (!supportsGraph(graph)) {
            throw new IllegalArgumentException("graph must implement PublicSPARQLGraph or " +
                    "be a JenaModelGraph");
        }
        if (!node.isResource())
            throw new IllegalArgumentException("Cannot DESCRIBE a literal");
        String string = SPARQLGraphHelper.toSPARQL(node, "error");
        if (string.equals("?error")) {
            throw new IllegalArgumentException("stringfier wants a variable for node"
                    + node.toString() + ". Cannot DESCRIBE variables.");
        }

        try (QueryExecution ex = createQueryExecution(graph, "DESCRIBE " + string)) {
            Model model = ex.execDescribe();
            for (StmtIterator it = model.listStatements(); it.hasNext(); )
                listener.add(new JenaTriple(it.next()));
            model.close();
        }
    }

    private QueryExecution createQueryExecution(Graph graph, String query) {
        if (graph instanceof JenaModelGraph)
            return QueryExecutionFactory.create(query, ((JenaModelGraph)graph).getModel());
        if (graph instanceof JenaSPARQLServiceGraph)
            return ((JenaSPARQLServiceGraph) graph).createExecution(query);
        return QueryExecutionFactory.sparqlService(((SPARQLGraph) graph).getServiceURI(), query);
    }
}
