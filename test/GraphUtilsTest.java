package test;

import org.junit.Assert;
import org.junit.Test;

import sol.EdgeArrayGraph;
import sol.GraphUtils;
import sol.IGraph;
import sol.NodeEdgeGraph;
import src.NoRouteException;
import src.NodeNameExistsException;

import java.util.HashMap;
import java.util.LinkedList;

public class GraphUtilsTest {
    // Assumes that graph will be empty, modifies it in-place
    private void addSimpleGraphNodes(IGraph graph) throws NodeNameExistsException {
        graph.addNode("node 1");
        graph.addNode("node 2");
        graph.addNode("node 3");
        graph.addNode("node 4");
    }

    // Assumes that graph will have nodes from `addSimpleGraphNodes`,
    //     modifies it in-place
    private void addSimpleGraphEdges(IGraph graph) {
        graph.addDirectedEdge("node 1", "node 2");
        graph.addDirectedEdge("node 2", "node 3");
    }

    // Assumes that graph will be empty, modifies it in-place
    private void makeSimpleGraph(IGraph graph) throws NodeNameExistsException {
        addSimpleGraphNodes(graph);
        addSimpleGraphEdges(graph);
    }

    @Test
    public void testGetRouteSimple(){
        try {
            //NodeEdgeGraph
            IGraph simpleGraphNode = new NodeEdgeGraph("a graph");
            makeSimpleGraph(simpleGraphNode);
            String fromNode = "node 1";
            String toNode = "node 3";
            LinkedList<String> route = GraphUtils.getRoute(simpleGraphNode, fromNode, toNode);
            LinkedList<String> expectedRoute = new LinkedList<>();
            expectedRoute.add("node 1");
            expectedRoute.add("node 2");
            expectedRoute.add("node 3");
            Assert.assertEquals(expectedRoute, route);

            //EdgeArrayGraph
            IGraph simpleGraphArray = new EdgeArrayGraph("a graph");
            makeSimpleGraph(simpleGraphArray);
            String fromNodeArray = "node 1";
            String toNodeArray = "node 3";
            LinkedList<String> routeArray = GraphUtils.getRoute(simpleGraphArray, fromNodeArray, toNodeArray);
            LinkedList<String> expectedRouteArray = new LinkedList<>();
            expectedRouteArray.add("node 1");
            expectedRouteArray.add("node 2");
            expectedRouteArray.add("node 3");
            Assert.assertEquals(expectedRouteArray, routeArray);
        }
        catch (NodeNameExistsException e) {
            // fail() automatically stops and fails the current test with an error message
            Assert.fail("Could not create graph to test");
        } catch (NoRouteException e) {
            Assert.fail("getRoute did not find a route");
        }
    }


    private void addEdges(IGraph graph) {
        graph.addDirectedEdge("A", "G");
        graph.addDirectedEdge("G", "B");
        graph.addDirectedEdge("C", "A");
        graph.addDirectedEdge("C", "E");
        graph.addDirectedEdge("E", "B");
        graph.addDirectedEdge("E", "D");
        graph.addDirectedEdge("D", "F");
        graph.addDirectedEdge("G", "H");
        graph.addDirectedEdge("F", "H");
        graph.addDirectedEdge("B", "F");

    }

    private void addNodes(IGraph graph) throws NodeNameExistsException {
        graph.addNode("A");
        graph.addNode("V");
        graph.addNode("C");
        graph.addNode("D");
        graph.addNode("E");
        graph.addNode("F");
        graph.addNode("G");
        graph.addNode("H");
    }

    public boolean checkRouteValidity(IGraph graph, LinkedList<String> route){
        for(int i = 0; i<route.size()-1; i++){
            if (!GraphUtils.hasRoute(graph, route.get(i), route.get(i+1)))
                return false;
        }
        return true;
    }

    @Test
    public void testGetRoute(){
        try {
            //NodeEdgeGraph
            IGraph simpleGraphNode = new NodeEdgeGraph("a graph");
            addNodes(simpleGraphNode);
            addEdges(simpleGraphNode);
            //NodeEdgeGraph - Test for shortest route out of multiple routes.
            LinkedList<String> routeNode1 = GraphUtils.getRoute(simpleGraphNode, "G", "H");
            Assert.assertEquals(2, routeNode1.size());
            Assert.assertEquals("G", routeNode1.get(0));
            Assert.assertEquals("H", routeNode1.get(1));
            Assert.assertTrue(checkRouteValidity(simpleGraphNode, routeNode1));
            //NodeEdgeGraph - Test for multiple identical routes.
            LinkedList<String> routeNode2 = GraphUtils.getRoute(simpleGraphNode, "E", "H");
            Assert.assertEquals(4, routeNode2.size());
            Assert.assertEquals("E", routeNode2.get(0));
            Assert.assertEquals("H", routeNode2.get(3));
            Assert.assertTrue(checkRouteValidity(simpleGraphNode, routeNode2));
            //NodeEdgeGraph - Test for one-to-one. A to A.
            LinkedList<String> routeNode3 = GraphUtils.getRoute(simpleGraphNode, "A", "A");
            Assert.assertEquals(1, routeNode3.size());
            Assert.assertEquals("A", routeNode3.get(0));
            Assert.assertTrue(checkRouteValidity(simpleGraphNode, routeNode3));

            //NodeEdgeGraph
            IGraph simpleGraphArray = new EdgeArrayGraph("a graph");
            addNodes(simpleGraphArray);
            addEdges(simpleGraphArray);
            //NodeEdgeGraph - Test for shortest route out of multiple routes.
            LinkedList<String> routeArray1 = GraphUtils.getRoute(simpleGraphArray, "G", "H");
            Assert.assertEquals(2, routeArray1.size());
            Assert.assertEquals("G", routeArray1.get(0));
            Assert.assertEquals("H", routeArray1.get(1));
            Assert.assertTrue(checkRouteValidity(simpleGraphArray, routeArray1));
            //NodeEdgeGraph - Test for multiple identical routes.
            LinkedList<String> routeArray2 = GraphUtils.getRoute(simpleGraphArray, "E", "H");
            Assert.assertEquals(4, routeArray2.size());
            Assert.assertEquals("E", routeArray2.get(0));
            Assert.assertEquals("H", routeArray2.get(3));
            Assert.assertTrue(checkRouteValidity(simpleGraphArray, routeArray2));
            //NodeEdgeGraph - Test for one-to-one. A to A.
            LinkedList<String> routeArray3 = GraphUtils.getRoute(simpleGraphArray, "A", "A");
            Assert.assertEquals(1, routeArray3.size());
            Assert.assertEquals("A", routeArray3.get(0));
            Assert.assertTrue(checkRouteValidity(simpleGraphArray, routeArray3));

        }
        catch (NodeNameExistsException e) {
            // fail() automatically stops and fails the current test with an error message
            Assert.fail("Could not create graph to test");
        } catch (NoRouteException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testGetRouteSimpleNoRoute(){
        try {
            //NodeEdgeGraph
            NodeEdgeGraph simpleGraph = new NodeEdgeGraph("a graph");
            makeSimpleGraph(simpleGraph);
            String fromNode = "node 1";
            String toNode = "node 4";
            Assert.assertThrows(
                    NoRouteException.class,
                    () -> GraphUtils.getRoute(simpleGraph, fromNode, toNode));

            //EdgeArrayGraph
            NodeEdgeGraph simpleGraphArray = new NodeEdgeGraph("a graph");
            makeSimpleGraph(simpleGraphArray);
            String fromNodeArray = "node 1";
            String toNodeArray = "node 4";
            Assert.assertThrows(
                    NoRouteException.class,
                    () -> GraphUtils.getRoute(simpleGraphArray, fromNodeArray, toNodeArray));
        }
        catch (NodeNameExistsException e) {
            Assert.fail("Could not create graph to test");
        }
    }

    @Test
    public void testGetRouteSimpleNoRouteDirected(){
        try {
            //NodeEdgeGraph
            NodeEdgeGraph simpleGraph = new NodeEdgeGraph("a graph");
            simpleGraph.addNode("node 1");
            simpleGraph.addNode("node 2");
            simpleGraph.addDirectedEdge("node 1", "node 2");
            String fromNode = "node 2";
            String toNode = "node 1";
            Assert.assertThrows(
                    NoRouteException.class,
                    () -> GraphUtils.getRoute(simpleGraph, fromNode, toNode));
        }
        catch (NodeNameExistsException e) {
            Assert.fail("Could not create graph to test");
        }
    }

    @Test
    public void testCountSelfEdgesSimple() {
        try {
            //NodeEdgeGraph
            IGraph basicGraph = new NodeEdgeGraph("a graph");
            basicGraph.addNode("node 1");
            basicGraph.addDirectedEdge("node 1", "node 1");
            Assert.assertEquals(1, basicGraph.countSelfEdges());

            //EdgeArrayGraph
            IGraph basicGraphArray = new NodeEdgeGraph("a graph");
            basicGraphArray.addNode("node 1");
            basicGraphArray.addDirectedEdge("node 1", "node 1");
            Assert.assertEquals(1, basicGraphArray.countSelfEdges());
        } catch (NodeNameExistsException e) {
            Assert.fail("Could not create graph to test");
        }
    }

    @Test
    public void testCountSelfEdges() {
        try {
            //NodeEdgeGraph
            IGraph grahNodes = new NodeEdgeGraph("a graph");
            grahNodes.addNode("A");
            grahNodes.addNode("B");
            grahNodes.addNode("C");
            grahNodes.addNode("D");
            grahNodes.addNode("E");
            Assert.assertEquals(0, grahNodes.countSelfEdges());
            grahNodes.addDirectedEdge("A", "B");
            Assert.assertEquals(0, grahNodes.countSelfEdges());
            grahNodes.addDirectedEdge("B", "C");
            grahNodes.addDirectedEdge("C", "C");
            Assert.assertEquals(1, grahNodes.countSelfEdges());
            grahNodes.addDirectedEdge("E", "A");
            grahNodes.addDirectedEdge("B", "C");
            grahNodes.addDirectedEdge("E", "E");
            Assert.assertEquals(2, grahNodes.countSelfEdges());
            grahNodes.addDirectedEdge("C", "C");
            Assert.assertEquals(2, grahNodes.countSelfEdges());

            //EdgeArrayGraph
            IGraph graphArray = new EdgeArrayGraph("a second graph");
            graphArray.addNode("A");
            graphArray.addNode("B");
            graphArray.addNode("C");
            graphArray.addNode("D");
            graphArray.addNode("E");
            Assert.assertEquals(0, graphArray.countSelfEdges());
            graphArray.addDirectedEdge("A", "B");
            Assert.assertEquals(0, graphArray.countSelfEdges());
            graphArray.addDirectedEdge("B", "C");
            graphArray.addDirectedEdge("C", "C");
            Assert.assertEquals(1, graphArray.countSelfEdges());
            graphArray.addDirectedEdge("E", "A");
            graphArray.addDirectedEdge("B", "C");
            graphArray.addDirectedEdge("E", "E");
            Assert.assertEquals(2, graphArray.countSelfEdges());
            graphArray.addDirectedEdge("C", "C");
            Assert.assertEquals(2, graphArray.countSelfEdges());
        } catch (NodeNameExistsException e) {
            Assert.fail("Could not create graph to test");
        }
    }

    @Test
    public void testReachesAllOthersSimple(){
        //NodeEdgeGraph
        IGraph basicGraphNode = new NodeEdgeGraph("b graph");
        basicGraphNode.addUndirectedEdge("node 1", "node 2");
        Assert.assertTrue(basicGraphNode.reachesAllOthers("node 1"));
        Assert.assertTrue(basicGraphNode.reachesAllOthers("node 2"));
        //EdgeArrayGraph
        IGraph basicGraphArray = new NodeEdgeGraph("b graph");
        basicGraphArray.addUndirectedEdge("node 1", "node 2");
        Assert.assertTrue(basicGraphArray.reachesAllOthers("node 1"));
        Assert.assertTrue(basicGraphArray.reachesAllOthers("node 2"));
    }

    @Test
    public void testReachesAllOthers(){
        //NodeEdgeGraph
        IGraph graphNode = new NodeEdgeGraph("a graph");
        graphNode.addDirectedEdge("A", "B");
        Assert.assertTrue(graphNode.reachesAllOthers("A"));
        Assert.assertFalse(graphNode.reachesAllOthers("B"));
        graphNode.addDirectedEdge("A", "A");
        Assert.assertTrue(graphNode.reachesAllOthers("A"));
        graphNode.addDirectedEdge("A", "C");
        graphNode.addDirectedEdge("C", "D");
        graphNode.addDirectedEdge("E", "E");
        Assert.assertFalse(graphNode.reachesAllOthers("E"));
        Assert.assertFalse(graphNode.reachesAllOthers("D"));
        graphNode.addDirectedEdge("A", "D");
        graphNode.addDirectedEdge("A", "E");
        Assert.assertTrue(graphNode.reachesAllOthers("A"));
        //EdgeArrayGraph
        IGraph graphArray = new EdgeArrayGraph("a graph");
        graphArray.addDirectedEdge("A", "B");
        Assert.assertTrue(graphArray.reachesAllOthers("A"));
        Assert.assertFalse(graphArray.reachesAllOthers("B"));
        graphArray.addDirectedEdge("A", "A");
        Assert.assertTrue(graphArray.reachesAllOthers("A"));
        graphArray.addDirectedEdge("A", "C");
        graphArray.addDirectedEdge("C", "D");
        graphArray.addDirectedEdge("E", "E");
        Assert.assertFalse(graphArray.reachesAllOthers("E"));
        Assert.assertFalse(graphArray.reachesAllOthers("D"));
        graphArray.addDirectedEdge("A", "D");
        graphArray.addDirectedEdge("A", "E");
        Assert.assertTrue(graphArray.reachesAllOthers("A"));
    }


    @Test
    public void testGetRoute2(){
        try {
            //NodeEdgeGraph
            IGraph simpleGraphNode = new NodeEdgeGraph("a graph");
            simpleGraphNode.addUndirectedEdge("1", "4");
            simpleGraphNode.addUndirectedEdge("4", "7");
            simpleGraphNode.addUndirectedEdge("4", "2");
            simpleGraphNode.addUndirectedEdge("7", "2");
            simpleGraphNode.addUndirectedEdge("2", "5");
            simpleGraphNode.addUndirectedEdge("2", "3");
            simpleGraphNode.addUndirectedEdge("5", "9");
            simpleGraphNode.addUndirectedEdge("9", "10");

            LinkedList<String> routeNode1 = GraphUtils.getRoute(simpleGraphNode, "1", "10");
            System.out.println(routeNode1);

        } catch (NoRouteException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testPathRetriever(){
        HashMap<String, String> visitLog = new HashMap<>();
        visitLog.put("A", "B");
        visitLog.put("B", "C");
        visitLog.put("S", "R");
        visitLog.put("C", "D");
        visitLog.put("D", "E");
        visitLog.put("F", "D");
        LinkedList<String> path = new LinkedList<>();
        path.add("E");
        path.add("D");
        path.add("C");
        path.add("B");
        path.add("A");
        LinkedList<String> pathTest = GraphUtils.pathRetriever(visitLog, "E", "A", new LinkedList<>());
        Assert.assertEquals(path, pathTest);
    }





}
