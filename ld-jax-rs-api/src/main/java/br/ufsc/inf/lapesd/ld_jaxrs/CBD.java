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
    int maxPath() default Integer.MAX_VALUE;
    boolean reifications() default true;
}
