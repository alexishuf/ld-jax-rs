package br.ufsc.inf.lapesd.ld_jaxrs.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Set of helpers to build SPARQL queries using Node instances
 */
public class SPARQLGraphHelper {
    public static String S = "s";
    public static String P = "p";
    public static String O = "o";

    private static Map<Class<? extends Node>, Function<Node, String>> sparqlStringfiers;

    @Nonnull
    public static String selectBGP(@Nullable Node subject, @Nullable Node predicate,
                            @Nullable Node object) {
        return String.format("SELECT ?%s ?%s ?%s WHERE { %s %s %s.}", S, P, O,
                toSPARQL(subject, S), toSPARQL(predicate, P), toSPARQL(object, O));
    }

    @Nonnull
    public static String selectSubject(PropertySpec... properties) {
        StringBuilder queryBuilder = new StringBuilder("SELECT ?" + S + " WHERE {\n");
        for (int i = 0; i < properties.length; i++) {
            queryBuilder.append(String.format("  ?%s %s %s.\n", S,
                    toSPARQL(properties[i].getPredicate(), P + i),
                    toSPARQL(properties[i].getObject(), O + i)));
        }
        return queryBuilder.append("}\n").toString();
    }

    public static String toSPARQL(@Nullable Node node, @Nonnull String varName) {
        if (node == null) return "?" + varName;
        Function<Node, String> stringfier = findStringfier(node);
        String string = stringfier.apply(node);
        return string != null ? string : "?" + varName; //stringfier may want a variable
    }

    @Nonnull
    private synchronized static Function<Node, String> findStringfier(Node node) {
        Class<? extends Node> actual = node.getClass();
        Class<? extends Node> best = Node.class;
        for (Class<? extends Node> candidate : sparqlStringfiers.keySet()) {
            if (candidate.isAssignableFrom(actual) && best.isAssignableFrom(candidate))
                best = candidate;
        }
        assert sparqlStringfiers.containsKey(best);
        return sparqlStringfiers.get(best);
    }

    public static synchronized <T extends Node>
    void registerSPARQLStringfier(@Nonnull Class<T> clazz,
                                  @Nonnull Function<Node, String> stringfier) {
        sparqlStringfiers.put(clazz, stringfier);
    }

    public static synchronized <T extends Node>
    void unregisterSPARQLStringfier(@Nonnull Class<T> clazz) {
        sparqlStringfiers.remove(clazz);
    }

    public static String escapeForSPARQL(String lexicalForm) {
        return lexicalForm
                .replace("\\", "\\\\")
                .replace("\t", "\\t")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\"", "\\\"")
                .replace("'", "\\\'");

    }

    public static String stringify(Node n) {
        String string;
        if (n.isBlankNode()) return null;
        if (n.isResource()) return "<" + n.getURI() + ">";

        assert n.isLiteral();
        string = String.format("\"%s\"", escapeForSPARQL(n.getLexicalForm()));
        if (n.getLanguage() != null)
            string += "@" + n.getLanguage();
        else if (n.getDatatypeURI() != null)
            string += "^^<" + n.getDatatypeURI() + ">";
        return string;
    }

    static {
        sparqlStringfiers = new HashMap<>();
        sparqlStringfiers.put(Node.class, SPARQLGraphHelper::stringify);
        sparqlStringfiers.put(SPARQLBlankNode.class, n -> ((SPARQLBlankNode)n).getSPARQLString());
    }
}
