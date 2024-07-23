package test;

import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashSet;

import sol.IGraph;
import sol.NodeEdgeGraph;
import sol.Scheduler;
import src.NoScheduleException;
import src.NodeNameExistsException;

public class SchedulerTest {
    // Assumes that graph will be empty, modifies it in-place
    private void addSimpleGraphNodes(IGraph graph) throws NodeNameExistsException {
        graph.addNode("lab 1");
        graph.addNode("lab 2");
        graph.addNode("lab 3");
        graph.addNode("lab 4");
    }

    // Assumes that graph will have nodes from `addSimpleGraphNodes`,
    //     modifies it in-place
    private void addSimpleGraphEdges(IGraph graph) {
        graph.addUndirectedEdge("lab 1", "lab 2");
        graph.addUndirectedEdge("lab 2", "lab 3");
    }

    // Assumes that graph will be empty, modifies it in-place
    private void makeSimpleGraph(IGraph graph) throws NodeNameExistsException {
        addSimpleGraphNodes(graph);
        addSimpleGraphEdges(graph);
    }

    @Test
    public void testCheckValidityTrue() {
        try{
            IGraph simpleGraph = new NodeEdgeGraph("a graph");
            makeSimpleGraph(simpleGraph);

            ArrayList<HashSet<String>> proposedSchedule = new ArrayList<>();
            HashSet<String> set1 = new HashSet<>();
            set1.add("lab 1");
            set1.add("lab 3");
            set1.add("lab 4");
            proposedSchedule.add(set1);
            HashSet<String> set2 = new HashSet<>();
            set2.add("lab 2");
            proposedSchedule.add(set2);
            Assert.assertTrue(Scheduler.checkValidity(simpleGraph, proposedSchedule));
        }
        catch (NodeNameExistsException e) {
            // fail() automatically stops and fails the current test with an error message
            Assert.fail("Could not create graph to test");
        }
    }

    @Test
    public void testCheckValidityFalse() {
        try{
            IGraph simpleGraph = new NodeEdgeGraph("a graph");
            makeSimpleGraph(simpleGraph);

            ArrayList<HashSet<String>> proposedSchedule = new ArrayList<>();
            HashSet<String> set1 = new HashSet<>();
            set1.add("lab 3");
            set1.add("lab 4");
            proposedSchedule.add(set1);
            HashSet<String> set2 = new HashSet<>();
            set2.add("lab 1");
            set2.add("lab 2");
            proposedSchedule.add(set2);

            Assert.assertFalse(Scheduler.checkValidity(simpleGraph, proposedSchedule));
        }
        catch (NodeNameExistsException e) {
            // fail() automatically stops and fails the current test with an error message
            Assert.fail("Could not create graph to test");
        }
    }

    @Test
    public void testCheckValidityFalseEdgeCases() {
        try{
            IGraph simpleGraph = new NodeEdgeGraph("a graph");
            simpleGraph.addNode("node 1");
            simpleGraph.addNode("node 2");
            simpleGraph.addNode("node 3");
            simpleGraph.addNode("node 4");
            simpleGraph.addNode("node 5");
            simpleGraph.addNode("node 6");
            simpleGraph.addUndirectedEdge("node 1", "node 2");
            simpleGraph.addUndirectedEdge("node 3", "node 4");
            simpleGraph.addUndirectedEdge("node 5", "node 6");

            ArrayList<HashSet<String>> proposedSchedule = new ArrayList<>();
            HashSet<String> set1 = new HashSet<>();
            set1.add("node 1");
            set1.add("node 2");
            proposedSchedule.add(set1);
            Assert.assertFalse(Scheduler.checkValidity(simpleGraph, proposedSchedule)); // There is only one Hashset (<2)
            HashSet<String> set2 = new HashSet<>();
            set2.add("node 3");
            set2.add("node 4");
            proposedSchedule.add(set2);
            Assert.assertFalse(Scheduler.checkValidity(simpleGraph, proposedSchedule)); // Nodes 5 and 6 are left out
            HashSet<String> set3 = new HashSet<>();
            set2.add("node 5");
            set2.add("node 6");
            proposedSchedule.add(set3);
            Assert.assertFalse(Scheduler.checkValidity(simpleGraph, proposedSchedule)); // There are three Hashsets (>2)

            IGraph simpleGraph2 = new NodeEdgeGraph("a graph");
            simpleGraph2.addNode("node 1");
            ArrayList<HashSet<String>> proposedSchedule2 = new ArrayList<>();
            HashSet<String> set4 = new HashSet<>();
            set4.add("node 1");
            set4.add("node 1");
            proposedSchedule2.add(set4);
            // There are two identical labs in one Hashset.
            Assert.assertFalse(Scheduler.checkValidity(simpleGraph2, proposedSchedule2));

            IGraph simpleGraph3 = new NodeEdgeGraph("a graph");
            ArrayList<HashSet<String>> proposedSchedule3 = new ArrayList<>();
            HashSet<String> set5 = new HashSet<>();
            set5.add("node 1");
            set5.add("node 15");
            proposedSchedule2.add(set5);
            // A node that was never added.
            Assert.assertFalse(Scheduler.checkValidity(simpleGraph3, proposedSchedule3));
        }
        catch (NodeNameExistsException e) {
            // fail() automatically stops and fails the current test with an error message
            Assert.fail("Could not create graph to test");
        }
    }

    @Test
    public void testFindScheduleValid(){
        try {
            IGraph simpleGraph = new NodeEdgeGraph("a graph");
            makeSimpleGraph(simpleGraph);
            ArrayList<HashSet<String>> schedule = Scheduler.findSchedule(simpleGraph);
            Assert.assertTrue(Scheduler.checkValidity(simpleGraph, schedule));
        }
        catch (NodeNameExistsException e) {
            // fail() automatically stops and fails the current test with an error message
            Assert.fail("Could not create graph to test");
        } catch (NoScheduleException e) {
            Assert.fail("findSchedule did not find a schedule");
        }
    }


    @Test
    public void testFindScheduleValidHard(){
        try {
            IGraph hardGraph = new NodeEdgeGraph("a graph");
            hardGraph.addUndirectedEdge("lab 1", "lab 2");
            hardGraph.addUndirectedEdge("lab 1", "lab 3");
            hardGraph.addUndirectedEdge("lab 2", "lab 4");
            hardGraph.addUndirectedEdge("lab 2", "lab 5");
            hardGraph.addUndirectedEdge("lab 3", "lab 6");
            hardGraph.addUndirectedEdge("lab 3", "lab 7");
            hardGraph.addUndirectedEdge("lab 8", "lab 9");
            hardGraph.addUndirectedEdge("lab 8", "lab 10");
            hardGraph.addUndirectedEdge("lab 10", "lab 11");
            hardGraph.addUndirectedEdge("lab 9", "lab 11");
            ArrayList<HashSet<String>> schedule = Scheduler.findSchedule(hardGraph);
            Assert.assertTrue(Scheduler.checkValidity(hardGraph, schedule));
        } catch (NoScheduleException e) {
            Assert.fail("findSchedule did not find a schedule");
        }
    }

    @Test
    public void testCheckValidityHard(){
        try {
            IGraph hardGraph = new NodeEdgeGraph("a graph");
            hardGraph.addNode("lab 12");
            hardGraph.addNode("lab 13");
            hardGraph.addUndirectedEdge("lab 1", "lab 2");
            hardGraph.addUndirectedEdge("lab 1", "lab 3");
            hardGraph.addUndirectedEdge("lab 2", "lab 4");
            hardGraph.addUndirectedEdge("lab 2", "lab 5");
            hardGraph.addUndirectedEdge("lab 3", "lab 6");
            hardGraph.addUndirectedEdge("lab 3", "lab 7");
            hardGraph.addUndirectedEdge("lab 8", "lab 9");
            hardGraph.addUndirectedEdge("lab 8", "lab 10");
            hardGraph.addUndirectedEdge("lab 10", "lab 11");
            hardGraph.addUndirectedEdge("lab 9", "lab 11");
            HashSet<String> kathi = new HashSet<>();
            HashSet<String> elijah = new HashSet<>();
            kathi.add("lab 1");
            kathi.add("lab 4");
            kathi.add("lab 5");
            kathi.add("lab 6");
            kathi.add("lab 7");
            kathi.add("lab 8");
            kathi.add("lab 11");
            elijah.add("lab 2");
            elijah.add("lab 3");
            elijah.add("lab 10");
            elijah.add("lab 9");
            elijah.add("lab 12");
            elijah.add("lab 13");
            ArrayList<HashSet<String>> proposedSched = new ArrayList<>();
            proposedSched.add(kathi);
            proposedSched.add(elijah);
            Assert.assertTrue(Scheduler.checkValidity(hardGraph, proposedSched));
        }catch (NodeNameExistsException e){
            Assert.fail("Could not create graph to test");
        }
        }

    @Test
    public void testFindScheduleInvalid(){
        try {
            IGraph simpleGraph = new NodeEdgeGraph("a graph");
            makeSimpleGraph(simpleGraph);
            simpleGraph.addUndirectedEdge("lab 3", "lab 1"); // This edge should make it impossible to schedule
            Assert.assertThrows(
                    NoScheduleException.class,
                    () -> Scheduler.findSchedule(simpleGraph));
        }
        catch (NodeNameExistsException e) {
            Assert.fail("Could not create graph to test");
        }
    }

    @Test
    public void testFindScheduleInvalidHard(){
        try {
            IGraph hardGraph = new NodeEdgeGraph("a graph");
            hardGraph.addNode("lab 1");
            hardGraph.addNode("lab 2");
            hardGraph.addNode("lab 3");
            hardGraph.addNode("lab 4");
            hardGraph.addNode("lab 5");
            hardGraph.addUndirectedEdge("lab 1", "lab 3");
            hardGraph.addUndirectedEdge("lab 1", "lab 4");
            hardGraph.addUndirectedEdge("lab 2", "lab 5");
            hardGraph.addUndirectedEdge("lab 2", "lab 4");
            hardGraph.addUndirectedEdge("lab 3", "lab 5");
            Assert.assertThrows(
                    NoScheduleException.class,
                    () -> Scheduler.findSchedule(hardGraph));
        }
        catch (NodeNameExistsException e) {
            Assert.fail("Could not create graph to test");
        }
    }

    @Test
    public void testFindScheduleValidTwoNodes(){
        try {
            IGraph simpleGraph = new NodeEdgeGraph("a graph");
            simpleGraph.addNode("node 1");
            simpleGraph.addNode("node 2");
            simpleGraph.addUndirectedEdge("node 1", "node 2");
            ArrayList<HashSet<String>> schedule = Scheduler.findSchedule(simpleGraph);
            Assert.assertTrue(Scheduler.checkValidity(simpleGraph, schedule));
            Assert.assertEquals(2, schedule.size());
            Assert.assertEquals(1, schedule.get(0).size());
            Assert.assertEquals(1, schedule.get(1).size());
        }
        catch (NodeNameExistsException e) {
            // fail() automatically stops and fails the current test with an error message
            Assert.fail("Could not create graph to test");
        } catch (NoScheduleException e) {
            Assert.fail("findSchedule did not find a schedule");
        }
    }

    @Test
    public void testFindScheduleEmptyGraph(){
        try {
            IGraph simpleGraph = new NodeEdgeGraph("a graph");
            ArrayList<HashSet<String>> schedule = Scheduler.findSchedule(simpleGraph);
            Assert.assertTrue(Scheduler.checkValidity(simpleGraph, schedule));
            Assert.assertEquals(2, schedule.size());
            Assert.assertEquals(0, schedule.get(0).size());
            Assert.assertEquals(0, schedule.get(1).size());
        }
        catch (NoScheduleException e) {
            Assert.fail("findSchedule did not find a schedule");
        }
    }

    @Test
    public void testFindScheduleValidScattered(){
        try {
            NodeEdgeGraph test = new NodeEdgeGraph("random");
            for (int i = 1; i <14; i++){
                test.addNode("lab "+i);
            }
            ArrayList<HashSet<String>> sch = Scheduler.findSchedule(test);
            Assert.assertTrue(Scheduler.checkValidity(test, sch));
        }
        catch (NoScheduleException e) {
            Assert.fail("findSchedule did not find a schedule");
        } catch (NodeNameExistsException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFindScheduleValidLinearGraph(){
        try {
            IGraph simpleGraph = new NodeEdgeGraph("a graph");
            simpleGraph.addNode("node 1");
            simpleGraph.addNode("node 2");
            simpleGraph.addNode("node 3");
            simpleGraph.addNode("node 4");
            simpleGraph.addUndirectedEdge("node 1", "node 2");
            simpleGraph.addUndirectedEdge("node 2", "node 3");
            simpleGraph.addUndirectedEdge("node 3", "node 4");
            ArrayList<HashSet<String>> schedule = Scheduler.findSchedule(simpleGraph);
            Assert.assertTrue(Scheduler.checkValidity(simpleGraph, schedule));
            Assert.assertEquals(2, schedule.size());
            Assert.assertEquals(2, schedule.get(0).size());
            Assert.assertEquals(2, schedule.get(1).size());
        }
        catch (NoScheduleException e) {
            Assert.fail("findSchedule did not find a schedule");
        } catch (NodeNameExistsException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFindScheduleValidCircularGraph(){
        try {
            IGraph simpleGraph = new NodeEdgeGraph("a graph");
            simpleGraph.addNode("node 1");
            simpleGraph.addNode("node 2");
            simpleGraph.addNode("node 3");
            simpleGraph.addNode("node 4");
            simpleGraph.addNode("node 5");
            simpleGraph.addNode("node 6");
            simpleGraph.addUndirectedEdge("node 1", "node 2");
            simpleGraph.addUndirectedEdge("node 2", "node 3");
            simpleGraph.addUndirectedEdge("node 3", "node 4");
            simpleGraph.addUndirectedEdge("node 4", "node 5");
            simpleGraph.addUndirectedEdge("node 5", "node 6");
            simpleGraph.addUndirectedEdge("node 6", "node 1");

            ArrayList<HashSet<String>> schedule = Scheduler.findSchedule(simpleGraph);
            Assert.assertTrue(Scheduler.checkValidity(simpleGraph, schedule));
            Assert.assertEquals(2, schedule.size());
            Assert.assertEquals(3, schedule.get(0).size());
            Assert.assertEquals(3, schedule.get(1).size());
        }
        catch (NoScheduleException e) {
            Assert.fail("findSchedule did not find a schedule");
        } catch (NodeNameExistsException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFindScheduleValidOnlyOneNode(){
        try {
            IGraph simpleGraph = new NodeEdgeGraph("a graph");
            simpleGraph.addNode("node 1");
            ArrayList<HashSet<String>> schedule = Scheduler.findSchedule(simpleGraph);
            Assert.assertTrue(Scheduler.checkValidity(simpleGraph, schedule));
            Assert.assertEquals(2, schedule.size());
            Assert.assertEquals(1, schedule.get(0).size());
            Assert.assertEquals(0, schedule.get(1).size());
        }
        catch (NoScheduleException e) {
            Assert.fail("findSchedule did not find a schedule");
        } catch (NodeNameExistsException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCheckValidityGraphFalseLinear(){
        try {
            IGraph simpleGraph = new NodeEdgeGraph("a graph");
            simpleGraph.addNode("node 1");
            simpleGraph.addNode("node 2");
            simpleGraph.addNode("node 3");
            simpleGraph.addNode("node 4");
            simpleGraph.addUndirectedEdge("node 1", "node 2");
            simpleGraph.addUndirectedEdge("node 2", "node 3");
            simpleGraph.addUndirectedEdge("node 3", "node 4");

            ArrayList<HashSet<String>> proposedSchedule = new ArrayList<>();
            HashSet<String> set1 = new HashSet<>();
            set1.add("node 2");
            set1.add("node 3");
            proposedSchedule.add(set1);

            HashSet<String> set2 = new HashSet<>();
            set2.add("node 4");
            set2.add("node 1");
            proposedSchedule.add(set2);
            Assert.assertFalse(Scheduler.checkValidity(simpleGraph, proposedSchedule)); // There is only one Hashset (<2)
        }
        catch (NodeNameExistsException e) {
            throw new RuntimeException(e);
        }
    }

}
