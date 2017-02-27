package br.ufsc.inf.lapesd.ld_jaxrs.jena;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides the set of @Provider classes implemented in ld-jax-rs-jena
 */
public class JenaProviders {
    private static Set<Class> providers = null;

    public static Set<Class> getProviders() {
        if (providers == null) {
            providers = Collections.unmodifiableSet(Stream.of(
                    ModelMessageBodyReader.class,
                    ModelMessageBodyWriter.class,
                    ResourceMessageBodyWriter.class
            ).collect(Collectors.toSet()));
        }
        return providers;
    }
}
