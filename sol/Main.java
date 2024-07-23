package sol;

import src.NoRouteException;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        EdgeArrayGraph G = new EdgeArrayGraph("sample");

        G.addDirectedEdge("A", "B");
        G.addDirectedEdge("A", "C");

        G.addDirectedEdge("C", "E");
        G.addDirectedEdge("E", "B");

        G.addDirectedEdge("C", "F");
        G.addDirectedEdge("B", "J");
        G.addDirectedEdge("J", "D");
        G.addDirectedEdge("D", "C");

        System.out.println(GraphUtils.hasRoute(G, "A", "F") + "false");
        System.out.println(GraphUtils.hasRoute(G, "A", "C") + "true");
        System.out.println(GraphUtils.hasRoute(G, "A", "B") + "false");
        System.out.println(GraphUtils.hasRoute(G, "A", "G") + "true");

        System.out.println("--------------------");


        EdgeArrayGraph A = new EdgeArrayGraph("sample");
        A.addDirectedEdge("A", "C");
        A.addDirectedEdge("C", "E");

        A.addDirectedEdge("A", "D");
        A.addDirectedEdge("D", "G");

        A.addDirectedEdge("B", "A");
        A.addDirectedEdge("B", "E");
        A.addDirectedEdge("B", "F");
        try {
            System.out.println(GraphUtils.getRoute(G, "A", "D"));
        }
        catch (NoRouteException e){
            System.out.println("nope");
        }
//        System.out.println(GraphUtils.hasRoute(A, "A", "C") + "true");
//        System.out.println(GraphUtils.hasRoute(A, "A", "B") + "false");
//        System.out.println(GraphUtils.hasRoute(A, "A", "G") + "true");

    }
}
