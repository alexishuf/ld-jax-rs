package br.ufsc.inf.lapesd.ld_jaxrs.core.traverser;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TraversalStrategy {
    Class<? extends Annotation> value();
}
