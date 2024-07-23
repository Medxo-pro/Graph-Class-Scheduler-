package sol;

import src.NoRouteException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;


/**
 * GraphUtils class.
 */
public class GraphUtils {

    /**
     * Constructor of the GraphUtils class.
     */
    public GraphUtils() {}

    /**
     * Method to use breadth-first-search to check whether there is a path
     *     from one node to another in a graph. Assumes that both fromNodeLabel
     *     and toNodeLabel are valid node labels in theGraph.
     *
     * @param theGraph the graph to traverse
     * @param fromNodeLabel name of the node from which to start searching
     * @param toNodeLabel   name of the node we want to reach
     * @return boolean indicating whether such a route exists
     */
    public static boolean hasRoute(IGraph theGraph, String fromNodeLabel, String toNodeLabel) {
        // set up and initialize data structures
        HashSet<String> visited = new HashSet<>();
        LinkedList<String> toCheck = new LinkedList<>();

        toCheck.add(fromNodeLabel);

        // process nodes to search for toNode
        while (!toCheck.isEmpty()) {
            String checkNode = toCheck.pop();
            if (checkNode.equals(toNodeLabel)) {
                return true;
            } else if (!visited.contains(checkNode)) {
                visited.add(checkNode);
                toCheck.addAll(theGraph.getNeighbors(checkNode));
            }
        }
        return false;
    }

    /**
     * Method to produce a sequence of nodes that constitutes a shortest path
     *     from fromNodeLabel to toNodeLabel. Assumes that both fromNodeLabel
     *     and toNodeLabel are valid node labels in theGraph.
     * Throws a NoRouteException if no such path exists
     *
     * @param theGraph the graph to traverse
     * @param fromNodeLabel the node from which to start searching
     * @param toNodeLabel   the node we want to reach
     * @return List of nodes in order of the path
     * @throws NoRouteException if no such path exists
     */
    public static LinkedList<String> getRoute(IGraph theGraph, String fromNodeLabel, String toNodeLabel)
            throws NoRouteException {
        HashMap<String, String> visitLog = new HashMap();
        HashSet<String> visited = new HashSet<>();
        LinkedList<String> toCheck = new LinkedList<>();
        toCheck.addLast(fromNodeLabel);

        // process nodes to search for toNode
        while (!toCheck.isEmpty()) {
            String checkNode = toCheck.poll();
            if (checkNode.equals(toNodeLabel)) {
                return pathRetriever(visitLog, fromNodeLabel, toNodeLabel, new LinkedList<String>());
            } else if (!visited.contains(checkNode)) {
                for(String neighbour : theGraph.getNeighbors(checkNode)) {
                    if(!visitLog.containsKey(neighbour)) {
                        visitLog.put(neighbour, checkNode);
                        toCheck.addLast(neighbour);
                    }
                }
                visited.add(checkNode);

            }
        }
        throw new NoRouteException();
    }

    /**
     * Method meant to return the path between two nodes after all the data structures were effectively populated.
     *
     * @param visitLog HashMap that maps were each nodes comes from.
     * @param fromNodeLabel Initial node.
     * @param toNodeLabel Final node.
     * @param path The linked list containing the path.
     * @return
     */
    public static LinkedList<String> pathRetriever(HashMap<String, String> visitLog,
                                                    String fromNodeLabel, String toNodeLabel, LinkedList path) {
        if(fromNodeLabel.equals(toNodeLabel)) {
            path.addFirst(fromNodeLabel);
            return path;
        }
        else{
            path.addFirst(toNodeLabel);
            return pathRetriever(visitLog, fromNodeLabel, visitLog.get(toNodeLabel), path);
        }
    }
}