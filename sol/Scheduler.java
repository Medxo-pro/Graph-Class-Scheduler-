package sol;

import java.util.*;

import src.NoScheduleException;

/**
 * This is Scheduler class. It has two methods: One to check if a proposed
 * schedule is valid, and the other is to suggest a valid schedule given a lab-constraint
 * graph if possible.
 */
public class Scheduler {

    /**
     * Scheduler's constructor
     */
    public Scheduler() {
    }

    /**
     * Method which checks if a given allocation of labs adheres to
     * the scheduling constraints of the graph. Assumes that
     * all lab names in proposedAlloc are valid labels in theGraph.
     *
     * @param theGraph      the graph to try to schedule
     * @param proposedAlloc the proposed allocation of labs between Kathi and Elijah
     * @return boolean indicating whether the proposed allocation is valid
     */
    public static boolean checkValidity(IGraph theGraph, ArrayList<HashSet<String>> proposedAlloc) {
        if (proposedAlloc.size() != 2)
            return false;
        for (String lab : proposedAlloc.get(0)) {
            if (proposedAlloc.get(1).contains(lab))
                return false;
            for (String neighbor : theGraph.getNeighbors(lab)) {
                if (!proposedAlloc.get(1).contains(neighbor))
                    return false;
            }
        }
        for (String lab : proposedAlloc.get(1)) {
            if (proposedAlloc.get(0).contains(lab))
                return false;
            for (String neighbor : theGraph.getNeighbors(lab)) {
                if (!proposedAlloc.get(0).contains(neighbor))
                    return false;
            }
        }
        return true;
    }

    /**
     * Method to compute a valid split of the graph nodes
     * without violating scheduling constraints,
     * if such a split exists
     * Throws a NoScheduleException if no such split exists
     *
     * @param theGraph the graph to try to schedule
     * @return an ArrayList of HashSets of node labels that constitute a
     * valid split of the graph
     * @throws NoScheduleException if no such split exists
     */
    public static ArrayList<HashSet<String>> findSchedule(IGraph theGraph)
            throws NoScheduleException {
        ArrayList<String> toVisit = theGraph.getAllNodes();
        HashSet<String> kathi = new HashSet<>();
        HashSet<String> elijah = new HashSet<>();
        ArrayList<HashSet<String>> schedule = new ArrayList<>();
        schedule.add(kathi);
        schedule.add(elijah);
        HashMap<String,Integer> allocator = new HashMap<>();
        while (!toVisit.isEmpty()) {
            Queue<String> queuing = new LinkedList<>();
            String node = toVisit.get(0);
            queuing.add(node);
            while (!queuing.isEmpty()) {
                node = queuing.peek();
                queuing.remove(node);
                int instructor;
                if(!toVisit.contains(node)) {
                    instructor = allocator.get(node);
                }
                else{
                    instructor = 0;
                    allocator.put(node, 0);
                    schedule.get(0).add(node);
                }
                toVisit.remove(node);
                for (String neighbor : theGraph.getNeighbors(node)) {
                    if (schedule.get(0).contains(neighbor) && (instructor+1)%2 == 1 || schedule.get(1).contains(neighbor) && (instructor+1)%2 == 0)
                        throw new NoScheduleException();
                    if(toVisit.contains(neighbor)) {
                        schedule.get((instructor + 1) % 2).add(neighbor);
                        allocator.put(neighbor, (instructor + 1) % 2);
                        queuing.add(neighbor);
                        toVisit.remove(neighbor);
                    }
                }
            }
        }
        return schedule;
    }



    }
