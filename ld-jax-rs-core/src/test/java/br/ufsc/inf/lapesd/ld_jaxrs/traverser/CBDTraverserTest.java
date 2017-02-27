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
                        new CBDTraverser().setMaxPath(1).setReifications(true),
                        TestGraphs.twoFriendsCBDReification_A_1()},
        };
    }

    @Test(dataProvider = "testData")
    public void test(MemGraph graph, MemNode node, CBDTraverser traverser, MemGraph expected) {
        MemGraph actual = new MemGraph();
        traverser.traverse(graph, node, actual);
        Assert.assertEquals(actual, expected);
    }
}
