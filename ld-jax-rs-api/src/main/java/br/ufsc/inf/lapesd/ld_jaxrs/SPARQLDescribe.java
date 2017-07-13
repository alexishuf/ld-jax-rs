package br.ufsc.inf.lapesd.ld_jaxrs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Instructs JAX-RS message writers to serialize a resource according to the result of a
 * SPARQL DESCRIBE [1].
 *
 * [1]: https://www.w3.org/TR/sparql11-query/#describe
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SPARQLDescribe {
}
