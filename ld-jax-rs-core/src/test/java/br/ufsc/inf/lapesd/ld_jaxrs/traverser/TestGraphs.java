package br.ufsc.inf.lapesd.ld_jaxrs.traverser;

import br.ufsc.inf.lapesd.ld_jaxrs.TraverserPredicate;
import br.ufsc.inf.lapesd.ld_jaxrs.core.priv.uris.RDF;
import br.ufsc.inf.lapesd.ld_jaxrs.inmemory.MemGraph;
import br.ufsc.inf.lapesd.ld_jaxrs.inmemory.MemNode;
import br.ufsc.inf.lapesd.ld_jaxrs.model.Node;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TestGraphs {

    private static final MemNode aPocket = b();
    private static final MemNode aReif = b();
    private static final MemNode bPocket = b();
    private static final MemNode cPocket = b();

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
    public static MemGraph twoFriendsCBDNamed_A_1() {
        return new MemGraph()
                .add("A", "friend", "B")
                .add("A", "name", l("a"))
                .add("A", "hasPocket", aPocket)
                .add(aPocket, "member", "item1")
                .add("B", "name", l("b"));
    }

    public static MemGraph threeFriends() {
        return new MemGraph()
                .add("A", "friend", "B")
                .add("A", "name", l("a"))
                .add("B", "name", l("b"))
                .add("B", "friend", "C")
                .add("B", "hasPocket", bPocket)
                .add(bPocket, "member", "item1")
                .add("C", "name", l("c"))
                .add("C", "hasPocket", cPocket)
                .add(cPocket, "member", "item2");
    }
    public static MemGraph threeFriendsNamed_A() {
        return new MemGraph()
                .add("A", "friend", "B")
                .add("A", "name", l("a"))
                .add("B", "name", l("b"))
                .add("B", "friend", "C")
                .add("B", "hasPocket", bPocket)
                .add(bPocket, "member", "item1")
                .add("C", "name", l("c"))
                .add("C", "hasPocket", cPocket)
                .add(cPocket, "member", "item2");
    }
    public static MemGraph threeFriendsNamed_A_1() {
        return new MemGraph()
                .add("A", "friend", "B")
                .add("A", "name", l("a"))
                .add("B", "name", l("b"))
                .add("B", "friend", "C")
                .add("B", "hasPocket", bPocket);
    }
    public static MemGraph threeFriendsNamed_A_Named1() {
        return new MemGraph()
                .add("A", "friend", "B")
                .add("A", "name", l("a"))
                .add("B", "name", l("b"))
                .add("B", "friend", "C")
                .add("B", "hasPocket", bPocket)
                .add(bPocket, "member", "item1")
                .add("C", "name", l("c"))
                .add("C", "hasPocket", cPocket);
    }
    public static MemGraph threeFriendsNamed_A_Named0() {
        return new MemGraph()
                .add("A", "friend", "B")
                .add("A", "name", l("a"))
                .add("B", "name", l("b"))
                .add("B", "friend", "C")
                .add("B", "hasPocket", bPocket);
    }
    public static MemGraph bNodePathLim() {
        return new MemGraph()
                .add("A", "friend", "B")
                .add("A", "hasPocket", aPocket)
                .add(aPocket, "member", "A1")
                .add("A1", "friend", "A2")
                .add("A2", "friend", "A3")
                .add("B", "friend", "C")
                .add("C", "friend", "D");
    }
    public static MemGraph bNodePathLim_A_0() {
        return new MemGraph()
                .add("A", "friend", "B")
                .add("A", "hasPocket", aPocket)
                .add(aPocket, "member", "A1")
                .add("B", "friend", "C")
                .add("C", "friend", "D");
    }
    public static MemGraph bNodePathLim_A_1() {
        return new MemGraph()
                .add("A", "friend", "B")
                .add("A", "hasPocket", aPocket)
                .add(aPocket, "member", "A1")
                .add("A1", "friend", "A2")
                .add("B", "friend", "C")
                .add("C", "friend", "D");
    }
    public static MemGraph chain() {
        return new MemGraph()
                .add("A", "friend", "B")
                .add("A", "friend", "E")
                .add("B", "friend", "C")
                .add("C", "friend", "D")
                .add("D", "friend", "E")
                .add("E", "friend", "F");
    }
    public static MemGraph chainCut_B() {
        return new MemGraph()
                .add("A", "friend", "B")
                .add("A", "friend", "E")
                .add("E", "friend", "F");
    }
    public static MemGraph chainCut_B_noB() {
        return new MemGraph()
                .add("A", "friend", "E")
                .add("E", "friend", "F");
    }
    public static MemGraph symmetric() {
        return new MemGraph()
                .add("A", "friend", "C")
                .add("C", "friend", "B")
                .add("C", "friend", "D")
                .add("D", "friend", "E")
                .add("E", "friend", "F")
                .add("G", "friend", "F")
                .add("B", "friend", "G");
    }
    public static MemGraph symmetricResult() {
        return new MemGraph()
                .add("A", "friend", "C")
                .add("C", "friend", "D")
                .add("D", "friend", "E")
                .add("E", "friend", "F");
    }

    public static class BlacklistTraverser_B implements TraverserPredicate {
        @Nullable
        @Override
        public Object apply(@Nullable Object state, @Nonnull Node node) {
            Preconditions.checkArgument(node instanceof MemNode);
            if (node.isResource() && !node.isBlankNode())
                if (((MemNode)node).getURI().equals("B"))
                    return null;
            return Boolean.TRUE;
        }
    }
}
