package br.ufsc.inf.lapesd.ld_jaxrs;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a Resource class or one of its methods so that Resources are written to JAX-RS
 * messages according to their concise bounded descriptions [1].
 *
 * [1]: https://www.w3.org/Submission/CBD/
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CBD {
    /**
     * Maximum path length from the root node (the resource described) to the to any other
     * traversed nodes. Traversed nodes are those for which properties are considered for
     * inclusion in the CDB. Setting this to zero will cause only the root node properties
     * to be included.
     */
    int maxPath() default Integer.MAX_VALUE;

    /**
     * Include in the CBD all paths that end in the start node, in addition to those
     * that originate from it.
     */
    boolean symmetric() default false;

    /**
     * IF true (the default) CBD of reifications of statements included in the root resource CBD
     * are included.
     */
    boolean reifications() default true;

    /**
     * The CBD spec halts traversal when a named node is met. Setting this to true will cause
     * traversers to continue including triples reachable from the named nodes.
     */
    boolean traverseNamed() default false;

    /**
     * If <code>traverseNamed() == true</code>, when a named node is by the first time in the
     * current path, a new path counter is started to check no path starting from this named node
     * larger than specicified is traversed
     */
    int maxPathFromFirstNamed() default Integer.MAX_VALUE;

    /**
     * Similar to <code>maxPathFromFirstNamed()</code>, but applies to the first blank node in
     * a path.
     */
    int maxPathFromFirstBlank() default Integer.MAX_VALUE;

    /**
     * A predicate to apply during Traversal in addition to all other rules specified.
     * The class is instantiated when a traversal starts and is used through the traversal.
     */
    Class<? extends TraverserPredicate> traverserPredicate() default TraverserPredicate.True.class;
}
