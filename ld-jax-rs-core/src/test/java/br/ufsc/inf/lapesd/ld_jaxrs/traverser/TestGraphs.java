package br.ufsc.inf.lapesd.ld_jaxrs.traverser;

import br.ufsc.inf.lapesd.ld_jaxrs.core.priv.uris.RDF;
import br.ufsc.inf.lapesd.ld_jaxrs.inmemory.MemGraph;
import br.ufsc.inf.lapesd.ld_jaxrs.inmemory.MemNode;

public class TestGraphs {

    private static final MemNode aPocket = b();
    private static final MemNode aReif = b();

    private static MemNode b() {
        return MemNode.createBlankNode();
    }
    private static MemNode l(String value) {
        return MemNode.createLiteral(value);
    }

    public static MemGraph twoFriends() {
        return new MemGraph()
                .add("A", "friend", "B")
                .add("A", "name", l("a"))
                .add("A", "hasPocket", aPocket)
                .add(aPocket, "member", "item1")
                .add("B", "name", l("b"))
                .add(aReif, RDF.subject, aPocket)
                .add(aReif, RDF.predicate, "member")
                .add(aReif, RDF.object, "item1")
                .add(aReif, "a", "Stolen");
    }
    public static MemGraph twoFriendsCBD_A_0() {
        return new MemGraph()
                .add("A", "friend", "B")
                .add("A", "name", l("a"))
                .add("A", "hasPocket", aPocket);
    }
    public static MemGraph twoFriendsCBD_A_1() {
        return new MemGraph()
                .add("A", "friend", "B")
                .add("A", "name", l("a"))
                .add("A", "hasPocket", aPocket)
                .add(aPocket, "member", "item1");
    }
    public static MemGraph twoFriendsCBDReification_A_1() {
        return new MemGraph()
                .add("A", "friend", "B")
                .add("A", "name", l("a"))
                .add("A", "hasPocket", aPocket)
                .add(aPocket, "member", "item1")
                .add(aReif, RDF.subject, aPocket)
                .add(aReif, RDF.predicate, "member")
                .add(aReif, RDF.object, "item1")
                .add(aReif, "a", "Stolen");
    }
}
