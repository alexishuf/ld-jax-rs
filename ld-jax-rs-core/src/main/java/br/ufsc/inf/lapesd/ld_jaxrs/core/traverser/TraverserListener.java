package br.ufsc.inf.lapesd.ld_jaxrs.core.traverser;

import br.ufsc.inf.lapesd.ld_jaxrs.core.model.Triple;

/**
 * Handles the result of a traversal by a {@link Traverser}
 */
public interface TraverserListener {
    void add(Triple triple);
}
