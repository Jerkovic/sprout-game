package com.binarybrains.sprout.skilltree;

import com.binarybrains.sprout.structs.graph.Graph;
import com.binarybrains.sprout.structs.graph.GraphTraversal;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class SkillGraphTest {

    Graph graph = new Graph();

    @Before
    public void setUp() throws Exception {
        graph.addVertex("Bob"); // Root 0
        graph.addVertex("Alice"); // Level 1
        graph.addVertex("Rob"); // Level 1
        graph.addVertex("Mark");
        graph.addVertex("Maria");

        graph.addEdge("Bob", "Alice");
        graph.addEdge("Bob", "Rob");
        graph.addEdge("Alice", "Mark");
        graph.addEdge("Rob", "Mark");
        graph.addEdge("Rob", "Maria");
    }

    @Test
    public void givenAGraph_whenTraversingDepthFirst_thenExpectedResult() {
        assertEquals("[Bob, Rob, Maria, Mark, Alice]",
                GraphTraversal.depthFirstTraversal(this.graph, "Bob").toString());
    }
}
