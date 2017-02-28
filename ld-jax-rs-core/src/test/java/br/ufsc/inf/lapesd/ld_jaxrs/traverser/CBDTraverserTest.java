package br.ufsc.inf.lapesd.ld_jaxrs.traverser;

import br.ufsc.inf.lapesd.ld_jaxrs.core.traverser.CBDTraverser;
import br.ufsc.inf.lapesd.ld_jaxrs.inmemory.MemGraph;
import br.ufsc.inf.lapesd.ld_jaxrs.inmemory.MemNode;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
public class CBDTraverserTest {

    @DataProvider
    public static Object[][] testData() {
        return new Object[][] {
                {TestGraphs.twoFriends(), MemNode.createNamed("A"),
                        new CBDTraverser().setMaxPath(0).setReifications(false),
                        TestGraphs.twoFriendsCBD_A_0()},
                {TestGraphs.twoFriends(), MemNode.createNamed("A"),
                        new CBDTraverser().setMaxPath(1).setReifications(false),
                        TestGraphs.twoFriendsCBD_A_1()},
                {TestGraphs.twoFriends(), MemNode.createNamed("A"),
                        new CBDTraverser().setReifications(false),
                        TestGraphs.twoFriendsCBD_A_1()},
                {TestGraphs.twoFriends(), MemNode.createNamed("A"),
                        new CBDTraverser().setMaxPath(1).setReifications(true),
                        TestGraphs.twoFriendsCBDReification_A_1()},
                {TestGraphs.twoFriends(), MemNode.createNamed("A"),
                        new CBDTraverser().setMaxPath(1).setReifications(false).setTraverseNamed(true),
                        TestGraphs.twoFriendsCBDNamed_A_1()},
                {TestGraphs.twoFriends(), MemNode.createNamed("A"),
                        new CBDTraverser().setReifications(false).setTraverseNamed(true),
                        TestGraphs.twoFriendsCBDNamed_A_1()},
                {TestGraphs.threeFriends(), MemNode.createNamed("A"),
                        new CBDTraverser().setReifications(false).setTraverseNamed(true),
                        TestGraphs.threeFriendsNamed_A()},
                {TestGraphs.threeFriends(), MemNode.createNamed("A"),
                        new CBDTraverser().setReifications(false).setTraverseNamed(true)
                                .setMaxPath(1),
                        TestGraphs.threeFriendsNamed_A_1()},
                {TestGraphs.threeFriends(), MemNode.createNamed("A"),
                        new CBDTraverser().setReifications(false).setTraverseNamed(true)
                                .setMaxPathFromFirstNamed(1),
                        TestGraphs.threeFriendsNamed_A_Named1()},
                {TestGraphs.bNodePathLim(), MemNode.createNamed("A"),
                        new CBDTraverser().setReifications(false).setTraverseNamed(true)
                                .setMaxPathFromFirstBlank(0),
                        TestGraphs.bNodePathLim_A_0()},
                {TestGraphs.bNodePathLim(), MemNode.createNamed("A"),
                        new CBDTraverser().setReifications(false).setTraverseNamed(true)
                                .setMaxPathFromFirstBlank(1),
                        TestGraphs.bNodePathLim_A_1()},
                {TestGraphs.chain(), MemNode.createNamed("A"),
                        new CBDTraverser().setReifications(false).setTraverseNamed(true)
                                .setTraverserPredicate(new TestGraphs.BlacklistTraverser_B()),
                        TestGraphs.chainCut_B()},
        };
    }

    @Test(dataProvider = "testData")
    public void test(MemGraph graph, MemNode node, CBDTraverser traverser, MemGraph expected) {
        MemGraph actual = new MemGraph();
        traverser.traverse(graph, node, actual);
        Assert.assertEquals(actual, expected);
    }
}
