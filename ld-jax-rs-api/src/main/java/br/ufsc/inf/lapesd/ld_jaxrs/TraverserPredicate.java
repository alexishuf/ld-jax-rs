package br.ufsc.inf.lapesd.ld_jaxrs;

import br.ufsc.inf.lapesd.ld_jaxrs.model.Node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A Predicate to determine if a node should be traversed by a Traverse
 *
 * Such objects are allowed to maintain state, and their lifetime is the
 * lifetime of the traversal.
 *
 * Implementations are expected to provide a default constructor that does not throw any
 * checked exception.
 */
@FunctionalInterface
public interface TraverserPredicate {
    /**
     * Determines if a node should be traversed considering <code>state</code> as the current state.
     *
     * @param state Current state, produced previously by the predicate object as a return of
     *              this method or <code>null</code> if node is a traversal root.
     * @param node Node whose traversal from the current state is to be evaluated.
     * @return next state or null if node must not be traversed.
     */
    @Nullable
    Object apply(@Nullable Object state, @Nonnull Node node);

    /**
     * A stateless predicate that is always true
     */
    final class True implements TraverserPredicate {
        @Override
        @Nullable
        public Object apply(@Nullable Object state, @Nonnull Node node) { return Boolean.TRUE; }
    }
}
