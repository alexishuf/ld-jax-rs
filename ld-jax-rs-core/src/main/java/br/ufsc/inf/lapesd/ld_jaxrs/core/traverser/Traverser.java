package br.ufsc.inf.lapesd.ld_jaxrs.core.traverser;

import br.ufsc.inf.lapesd.ld_jaxrs.model.Graph;
import br.ufsc.inf.lapesd.ld_jaxrs.model.Node;
import br.ufsc.inf.lapesd.ld_jaxrs.model.SPARQLGraph;

import javax.annotation.Nonnull;

/**
 * Traverses a node according to some implementation-defined strategy..
 */
public interface Traverser {
    /**
     * Determines if traverse() supports using the given graph.
     *
     * Some Traverser implementations may rely on implementation-specific functionality
     * of the graph. This method is used to check if the graph has the required functionality.
     * An example are traversers that execute a DESCRIBE query, and that may require
     * {@link SPARQLGraph} implementations.
     *
     * @param graph graph to check for compatibility
     * @return true iff the graph implementation is supported.
     */
    boolean supportsGraph(@Nonnull Graph graph);

    /**
     * Traverses the graph starting from node, feeding triples to listener.
     *
     * The given node must belong to the graph.
     *
     * @param graph graph to be traversed
     * @param node start of the traversal
     * @param listener output of the traversal
     */
    void traverse(@Nonnull Graph graph, @Nonnull Node node,
                  @Nonnull TraverserListener listener);
}
