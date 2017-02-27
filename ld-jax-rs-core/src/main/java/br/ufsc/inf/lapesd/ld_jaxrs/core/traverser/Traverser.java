package br.ufsc.inf.lapesd.ld_jaxrs.core.traverser;

import br.ufsc.inf.lapesd.ld_jaxrs.core.model.Graph;
import br.ufsc.inf.lapesd.ld_jaxrs.core.model.Node;

import javax.annotation.Nonnull;

/**
 * Traverses a node according to some implementation-defined strategy..
 */
public interface Traverser {
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
