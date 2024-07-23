package sol;

import src.NodeNameExistsException;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * IGraph interface.
 */
public interface IGraph {

    /**
     * This method takes care of adding nodes.
     *
     * @param descr
     * @throws NodeNameExistsException
     */
    void addNode(String descr) throws NodeNameExistsException;

    /**
     * This method takes care of adding directed edges.
     * The two directed edges are passed as parameters orderly.
     *
     * @param descr1
     * @param descr2
     */
    void addDirectedEdge(String descr1, String descr2);

    /**
     * This method takes care of adding undirected edges.
     * The two directed edges are passed as parameters.
     *
     * @param descr1
     * @param descr2
     */
    void addUndirectedEdge (String descr1, String descr2);

    /**
     * This method takes care of counting the number of nodes that have
     * an edge to themselves.
     *
     * @return
     */
    int countSelfEdges();

    /**
     * This method takes care of determining if a given node can reach all the others.
     *
     * @param fromNodeLabel
     * @return
     */
    boolean reachesAllOthers(String fromNodeLabel);

    /**
     * This method takes care of returning all the nodes a given node points to.
     * (i.e., all the nodes a given node has edges to)
     *
     * @param checkNode
     * @return
     */
    LinkedList<String> getNeighbors(String checkNode);

    /**
     * This method takes care of returning all the nodes a graph contains.
     *
     * @return
     */
    ArrayList<String> getAllNodes();
}
