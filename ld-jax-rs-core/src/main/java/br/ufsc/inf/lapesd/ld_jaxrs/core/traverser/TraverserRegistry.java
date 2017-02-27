package br.ufsc.inf.lapesd.ld_jaxrs.core.traverser;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A registry for {@link Traverser} implementations. Allows selecting a Traverser from annotations.
 *
 * To be registered, implementations MUST have the {@link TraversalStrategy} annotation
 */
public class TraverserRegistry {
    private static TraverserRegistry instance = new TraverserRegistry()
            .register(CBDTraverser.class);

    private Map<Class<? extends Annotation>, Function<Annotation, Traverser>> map
            = new HashMap<>();

    /**
     * Gets the default registry, with all core {@link Traverser} implementations registered.
     * @return the default registry
     */
    public static TraverserRegistry get() {
        return instance;
    }

    /**
     * Register a {@link Traverser} implementation.
     *
     * @param implementation The implementation class, must have a {@link TraversalStrategy}
     *                       annotation and a constructor that receives an annotation of
     *                       the type specified in {@link TraversalStrategy}.
     * @return The registry itself
     */
    public TraverserRegistry register(Class<? extends Traverser> implementation) {
        TraversalStrategy strategy = implementation.getAnnotation(TraversalStrategy.class);
        if (strategy == null) throw new IllegalArgumentException("@TraversalStrategy is required");
        Class<? extends Annotation> annotationClass = strategy.value();
        try {
            Constructor<? extends Traverser> constructor = implementation.getConstructor(annotationClass);
            map.put(annotationClass, a -> {
                try {
                    return constructor.newInstance(a);
                } catch (InstantiationException | IllegalAccessException
                        | InvocationTargetException e) {
                    throw new TraverserInstantiationException("Problem instantiating Traverser", e);
                }
            });
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("No appropriate constructor");
        }
        return this;
    }

    /**
     * Selects {@link Traverser} implementation from the given annotations and return a instance
     *
     * @param annotations Array of annotations to be considered, the last strategy annotation
     *                    will be used to select the {@link Traverser}
     * @return A {@link Traverser} instance or null if no registered implementation handles
     * the annotations.
     */
    @Nullable
    public Traverser select(Annotation[] annotations) {
        for (int i = annotations.length - 1; i >= 0; i--) {
            Function<Annotation, Traverser> f = map.get(annotations[i].annotationType());
            if (f != null)
                return f.apply(annotations[i]);
        }
        return null;
    }

    /**
     * Same as <code>select()</code>, but throws an appropriate {@link WebApplicationException}
     * with status 500 instead of returning null
     *
     * @param annotations annotations, as in <code>select()</code>
     * @return The selected traverser instance.
     */
    @Nonnull
    public Traverser selectOrWebApplicationException(Annotation[] annotations) {
        try {
            Traverser traverser = select(annotations);
            if (traverser == null) {
                throw new WebApplicationException("Could not locate a traverser",
                        Response.Status.INTERNAL_SERVER_ERROR);
            }
            return traverser;
        } catch (TraverserInstantiationException e) {
            throw new WebApplicationException("Could not instantiate traverser", e);
        }
    }
}
