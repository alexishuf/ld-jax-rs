package br.ufsc.inf.lapesd.ld_jaxrs.core.traverser;

public class TraverserInstantiationException extends RuntimeException {
    public TraverserInstantiationException() {
    }

    public TraverserInstantiationException(String s) {
        super(s);
    }

    public TraverserInstantiationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public TraverserInstantiationException(Throwable throwable) {
        super(throwable);
    }
}
