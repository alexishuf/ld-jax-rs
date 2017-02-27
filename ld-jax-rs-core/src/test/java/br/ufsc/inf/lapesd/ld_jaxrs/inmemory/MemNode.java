package br.ufsc.inf.lapesd.ld_jaxrs.inmemory;

import br.ufsc.inf.lapesd.ld_jaxrs.core.model.Node;

public class MemNode implements Node {
    public enum Type {
        Blank,
        Named,
        Literal
    }
    private Type type;
    private String uri = null;
    private String literalValue = null;

    public MemNode(Type type) {
        this.type= type;
    }

    public static MemNode createBlankNode() { return new MemNode(Type.Blank); }
    public static MemNode createNamed(String uri) {
        MemNode node = new MemNode(Type.Named);
        node.uri = uri;
        return node;
    }
    public static MemNode createLiteral(String value) {
        MemNode node = new MemNode(Type.Literal);
        node.literalValue = value;
        return node;
    }

    public String getURI() { return uri; }
    public String getLiteralValue() { return literalValue; }

    public boolean isBlankNode() { return type == Type.Blank; }
    public boolean isResource() { return !isLiteral(); }
    public boolean isLiteral() { return type == Type.Literal; }

    @Override
    public String toString() {
        if (isBlankNode()) return String.format("_:%h", this);
        else if (isLiteral()) return String.format("\"%s\"", literalValue);
        else return String.format("<%s>", uri);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemNode memNode = (MemNode) o;

        if (type == Type.Blank)
            return this == o;
        if (type != memNode.type) return false;
        if (uri != null ? !uri.equals(memNode.uri) : memNode.uri != null) return false;
        return literalValue != null ? literalValue.equals(memNode.literalValue) : memNode.literalValue == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (uri != null ? uri.hashCode() : 0);
        result = 31 * result + (literalValue != null ? literalValue.hashCode() : 0);
        return result;
    }
}
