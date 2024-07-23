package sol;

import src.NodeNameExistsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * EdgeArrayGraph class that implements the IGraph interface.
 */
public class EdgeArrayGraph implements IGraph {
    String name;
    ArrayList<ArrayList<Boolean>> arrayGraph; //[ROW[COLS], ROW[COLS], ROW[COLS], ..., ROW[COLS]]
    HashMap<String, Integer> strToInt;
    HashMap<Integer, String> intToStr;


    /**
     * Constructor of the EdgeArrayGraph class.
     * It sets the name of the Graph using the @param and initializes instance variables.
     *
     * @param name
     */
    public EdgeArrayGraph(String name) {
        this.name = name;
        this.arrayGraph = new ArrayList<>();
        this.strToInt = new HashMap<String, Integer>();
        this.intToStr = new HashMap<Integer, String>();
    }

    /**
     * Method to add a new node with the given description. An exception will
     * be thrown if the description already names a node in the graph
     *
     * @param descr the text description or label to associate with the node
     * @throws NodeNameExistsException if that description is already
     *                                 associated with a node in the graph
     */
    public void addNode(String descr) throws NodeNameExistsException {
        if (!this.strToInt.containsKey(descr)) {
            ArrayList row = new ArrayList<>();
            this.arrayGraph.add(row);
            this.intToStr.put(this.intToStr.size(), descr);
            this.strToInt.put(descr, this.strToInt.size());
            for(int i=0 ; i<this.strToInt.size() ; i++)
                row.add(i, false);
            for(int i=0 ; i<this.strToInt.size() - 1; i++)
                this.arrayGraph.get(i).add(false);
        } else {
            throw new NodeNameExistsException();
        }
    }


    /**
     * An internal method to add a node without checking whether it exists.
     * This is useful for internally avoiding the exception handling when
     * we already know the node doesn't exist.
     *
     * @param descr the text description or label to associate with the node
     * @return the (new) node associated with the given description
     */
    private void addNodeUnchecked(String descr) {
        if (!this.strToInt.containsKey(descr)) {
            ArrayList row = new ArrayList<>();
            this.arrayGraph.add(row);
            this.intToStr.put(this.intToStr.size(), descr);
            this.strToInt.put(descr, this.strToInt.size() );
            for(int i=0 ; i<this.strToInt.size() ; i++)
                row.add(i, false);
            for(int i=0 ; i<this.strToInt.size() - 1; i++)
                this.arrayGraph.get(i).add(false);
        }
    }


    /**
     * Method to add a directed edge between the nodes associated with the given
     * descriptions. If descr1 and descr2 are not already
     * valid node labels in the graph, those nodes are also created.
     * If the edge already exists, no changes are made
     * (and no exceptions or warnings are raised)
     *
     * @param descr1 the source node for the edge
     * @param descr2 the target node for the edge
     */
    public void addDirectedEdge(String descr1, String descr2) {
        this.addNodeUnchecked(descr1);
        this.addNodeUnchecked(descr2);
        int row = this.strToInt.get(descr1);
        int col = this.strToInt.get(descr2);
        this.arrayGraph.get(row).set(col, true);
    }

    /**
     * Method to add an undirected edge between the nodes associated with the given
     * descriptions. This is equivalent to adding two directed edges, one from
     * descr1 to descr2, and another from descr2 to descr1.
     * If descr1 and descr2 are not already valid node labels in the graph,
     * those nodes are also created.
     *
     * @param descr1 the source node for the edge
     * @param descr2 the target node for the edge
     */
    public void addUndirectedEdge(String descr1, String descr2) {
        this.addNodeUnchecked(descr1);
        this.addNodeUnchecked(descr2);
        int row1 = this.strToInt.get(descr1);
        int col1 = this.strToInt.get(descr2);
        this.arrayGraph.get(row1).set(col1, true);
        int row2 = this.strToInt.get(descr2);
        int col2 = this.strToInt.get(descr1);
        this.arrayGraph.get(row2).set(col2, true);
    }

    /**
     * Method to count how many nodes have edges to themselves
     *
     * @return the number of nodes that have edges to themselves
     */
    public int countSelfEdges() {
        int counter = 0;
        for (int i = 0; i < this.arrayGraph.size(); i++) {
            if (this.arrayGraph.get(i).get(i)) {
                counter++;
            }
        }
        return counter;
    }
    /*
    Linear Time O(N)
    The runtime depends on the number of "cities" (N) in the arrayGraph data structure.
    There is a for loop that will run N times. For each iteration constant time operations are performed,
    getting an element from a specific position in an ArrayList.
     */


    /**
     * Method to check whether a given node has edges to every other node (with or without an edge to itself).
     * Assumes that fromNodeLabel is a valid node label in the graph.
     *
     * @param fromNodeLabel the node to check
     * @return true if fromNodeLabel has an edge to every other node, otherwise false
     */
    public boolean reachesAllOthers(String fromNodeLabel) {
        int row = this.strToInt.get(fromNodeLabel);
        for (int i = 0; i < this.arrayGraph.size(); i++) {
            if (!this.arrayGraph.get(row).get(i) && row != i) {
                return false;
            }
        }
        return true;
    }
    /*
    Linear Time O(N)
    The runtime depends on the number of "cities" (N) in the arrayGraph data structure.
    First we get an element from a HashMap which is constant time.
    Then, there is a for loop that will run N times. For each iteration constant time operations are performed,
    getting an element from a specific position in an ArrayList.
     */

    @Override
    public LinkedList<String> getNeighbors(String checkNode) {
        LinkedList stringList = new LinkedList<>();
        int row = this.strToInt.get(checkNode);
        for (int i = 0; i<this.arrayGraph.size(); i++){
            if(this.arrayGraph.get(row).get(i))
                stringList.add(this.intToStr.get(i));
        }
        return stringList;
    }

    @Override
    public ArrayList<String> getAllNodes(){
        return new ArrayList<>(this.strToInt.keySet());
    }

}
