package br.ufsc.inf.lapesd.ld_jaxrs.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;

/**
 * Interface for accessing a querying a graph of {@link Node}s
 */
public interface Graph {
    @Nonnull
    Iterator<Triple> query(@Nullable Node subject, @Nullable Node predicate, @Nullable Node object);

    /**
     * Get a subject with given properties
     * @param properties list of properties that the subjects must have
     * @return Iterator to subjects matching the query
     */
    @Nonnull
    Iterator<Node> querySubjects(PropertySpec... properties);

    /**
     * Gets a node identified by its URI. This never fails, and will return a node that
     * is not linked to any other if there is no node with given URI
     *
     * @param uri Full URI of the node
     * @return the Node.
     */
    @Nonnull
    Node getNode(@Nonnull String uri);
}
