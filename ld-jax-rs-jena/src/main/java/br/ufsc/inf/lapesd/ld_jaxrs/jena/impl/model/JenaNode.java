package br.ufsc.inf.lapesd.ld_jaxrs.jena.impl.model;

import br.ufsc.inf.lapesd.ld_jaxrs.model.Node;
import br.ufsc.inf.lapesd.ld_jaxrs.model.SPARQLGraphHelper;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.WeakHashMap;

/**
 * Wraps a {@link RDFNode} as a Node
 */
public final class JenaNode implements Node {
    private final RDFNode node;

    public JenaNode(@Nonnull RDFNode node) {
        this.node = node;
    }

    public RDFNode asRDFNode() { return node; }
    public Resource asResource() { return node.asResource(); }
    public Property asProperty() { return node.as(Property.class); }

    @Override
    public boolean isBlankNode() { return node.isAnon(); }
    @Override
    public boolean isResource() { return node.isResource(); }
    @Override
    public boolean isLiteral() { return node.isLiteral(); }

    @Override
    public String getURI() {
        if (!isResource()) throw new IllegalArgumentException("Not a resource");
        return isBlankNode() ? null : asResource().getURI();
    }

    @Override
    public String getLexicalForm() {
        if (!isLiteral()) throw new IllegalArgumentException("Not a literal");
        return asRDFNode().asLiteral().getLexicalForm();
    }

    @Override
    public String getLanguage() {
        if (!isLiteral()) throw new IllegalArgumentException("Not a literal");
        String lang = asRDFNode().asLiteral().getLanguage();
        return lang != null && lang.trim().isEmpty() ? null : lang;
    }

    @Override
    public String getDatatypeURI() {
        if (!isLiteral()) throw new IllegalArgumentException("Not a literal");
        return asRDFNode().asLiteral().getDatatypeURI();
    }

    @Override
    public String toString() {
        return node.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JenaNode jenaNode = (JenaNode) o;

        return node.equals(jenaNode.node);
    }

    @Override
    public int hashCode() {
        return node.hashCode();
    }

    private static String sparqlStringfier(Node node) {
        RDFNode n = ((JenaNode) node).asRDFNode();

        if (n.isAnon()) return "<_:" + n.asResource().getId() + ">";
        if (n.isURIResource()) return "<" + n.asResource().getURI() + ">";

        assert n.isLiteral();
        Literal lit = n.asLiteral();
        String string = "\"" + SPARQLGraphHelper.escapeForSPARQL(lit.getLexicalForm()) + "\"";

        String lang = lit.getLanguage();
        String dtURI = lit.getDatatypeURI();
        if (lang != null && !lang.isEmpty())
            string += "@" + lang;
        else if (dtURI != null)
            string += "^^<" + dtURI + ">";

        return string;
    }

    public static @Nonnull Resource toResource(@Nonnull Node node) {
        return JenaResourceConverter.get().toResource(node);
    }

    public static @Nonnull
    Literal toLiteral(@Nonnull Node node) {
        final Charset utf8 = Charset.forName("UTF-8");
        if (!node.isLiteral()) throw new IllegalArgumentException();
        if (node instanceof JenaNode) return ((JenaNode) node).asRDFNode().asLiteral();

        String lexicalForm = node.getLexicalForm();
        String lang = node.getLanguage();
        if (lang != null)
            return ResourceFactory.createLangLiteral(lexicalForm, lang);
        String datatypeURI = node.getDatatypeURI();

        String turtle = String.format("<a> <b> \"%s\"^^<%s>", lexicalForm, datatypeURI);
        Model model = ModelFactory.createDefaultModel();
        ByteArrayInputStream in = new ByteArrayInputStream(turtle.getBytes(utf8));
        RDFDataMgr.read(model, in, Lang.TURTLE);
        return model.listStatements().next().getObject().asLiteral();
    }

    public static @Nonnull Property toProperty(@Nonnull Node node) {
        return toResource(node).as(Property.class);
    }

    public static @Nonnull RDFNode toRDFNode(@Nonnull Node node) {
        if (node.isResource()) return toResource(node);
        assert node.isLiteral();
        return toLiteral(node);
    }

    static {
        SPARQLGraphHelper.registerSPARQLStringfier(JenaNode.class, JenaNode::sparqlStringfier);
    }

    static class JenaResourceConverter {
        private WeakHashMap<Node, Resource> bNodesMap = new WeakHashMap<>();
        private static final JenaResourceConverter instance = new JenaResourceConverter();

        public static JenaResourceConverter get() {
            return instance;
        }

        public synchronized  @Nonnull
        Resource toResource(@Nonnull Node node) {
            if (!node.isResource()) throw new IllegalArgumentException();
            if (node instanceof JenaNode) return ((JenaNode)node).asResource();

            Resource resource;
            if (node.isBlankNode()) {
                resource = bNodesMap.getOrDefault(node, null);
                if (resource == null)
                    bNodesMap.put(node, resource = ResourceFactory.createResource());
            } else {
                resource = ResourceFactory.createResource(node.getURI());
            }
            return resource;
        }
    }
}
