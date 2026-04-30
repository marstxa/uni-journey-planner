package modules;

import java.util.*;

public class MetrolinkDijkstra {

    // core data structure
    static class Connection {

        String destination;
        String line;
        double time;

        public Connection(String destination, String line, double time) {
            this.destination = destination;
            this.line = line;
            this.time = time;
        }
    }

    static class RouteState implements Comparable<RouteState> {

        String station;
        String line; // line used to arrive to this station
        double totalCost; // Renamed since time changes based on the method selected - time taken to get here so far
        double actualTime; // The actual time taken for the final output
        RouteState previous; // To reconstruct path backward if needed
        int changes; // Number of line changes made

        public RouteState(String station, String line, double totalCost, double actualTime, int changes, RouteState previous) {
            this.station = station;
            this.line = line;
            this.totalCost = totalCost;
            this.actualTime = actualTime;
            this.changes = changes;
            this.previous = previous;
        }

        @Override // for safety
        public int compareTo(RouteState other) {
            return Double.compare(this.totalCost, other.totalCost);
        }
    }

    // Graph that maps a station name to a list of connections
    private final Map<String, List<Connection>> network = new HashMap<>();

    public void addConnection(String from, String to, String line, double time) {
        network.putIfAbsent(from, new ArrayList<>());
        network.putIfAbsent(to, new ArrayList<>());

        // Add two way connection
        network.get(from).add(new Connection(to, line, time));
        network.get(to).add(new Connection(from, line, time));
    }

    // Pathfinding algorithm
    // Since i have to allow the users to decide wether they want the fastest route or the one with least changes
    // Added a new parameter for fewest changes
    public void findShortestRoute(String startstation, String endStation, boolean optimisedRoute) {
        PriorityQueue<RouteState> queue = new PriorityQueue<>();

        // Tracks minimum time to reach a station + line
        // allows the algorithm to visit a station twice if its on a different line or we have to backtrack
        Map<String, Double> minCost = new HashMap<>();

        // Start with - time and 0 line
        queue.add(new RouteState(startstation, null, 0.0, 0.0, 0, null));

        RouteState finalState = null;

        while (!queue.isEmpty()) {
            RouteState current = queue.poll();

            // Break condition = we found the fastest route
            if (current.station.equals(endStation)) {
                finalState = current;
                break;
            }

            // Check neigbouring stations
            for (Connection conn : network.getOrDefault(current.station, new ArrayList<>())) {
                double travelTime = conn.time;
                int newChanges = current.changes;

                // add penalty for change, (TEST)
                if (current.line != null && !current.line.equals(conn.line)) {
                    travelTime += 2.0; //TODO: Change 
                    newChanges++;
                }

                double newActualTime = current.actualTime + travelTime;
                double newTotalCost;

                // Calculate cost to determine if its optimised or not
                if (optimisedRoute) {
                    newTotalCost = newActualTime;
                } else {
                    // break ties happening if two routes have 0 changes
                    newTotalCost = newChanges;
                }

                String stateKey = conn.destination + "_" + conn.line;

                // if this is the fastest way we found for this station-line
                if (newTotalCost < minCost.getOrDefault(stateKey, Double.MAX_VALUE)) {
                    minCost.put(stateKey, newTotalCost);
                    queue.add(new RouteState(conn.destination, conn.line, newTotalCost, newActualTime, newChanges, current));
                }
            }
        }

        // Print output
        if (finalState == null) {
            System.out.println("No route could be found");
        } else {
            printFormattedRoute(finalState, optimisedRoute);
        }
    }

    private void printFormattedRoute(RouteState endState, boolean optimisedRoute) {
        if (optimisedRoute) {
            System.err.println("\n*** Minimal Time Route ***");
        } else {
            System.out.println("\n*** Route with the Fewest Changes ***");
        }

        // Reconstruct the path backwards
        List<RouteState> path = new ArrayList<>();
        RouteState curr = endState;

        while (curr != null) {
            path.add(curr);
            curr = curr.previous;
        }
        Collections.reverse(path); // Start -> End

        String currentLine = path.get(1).line; // The line we take from the start station

        // Print start station
        System.out.println(path.get(0).station + " on " + currentLine + " line");

        for (int i = 1; i < path.size(); i++) {
            RouteState step = path.get(i);

            // Check if we changed lines
            if (!step.line.equals(currentLine)) {
                System.out.println("** Change Line to " + step.line + " line***");
                System.out.println(path.get(i - 1).station + " on " + step.line + " line");
                currentLine = step.line;
            }
            System.out.println(step.station + " on " + step.line + " line");
        }
        System.out.println("Overall Journey Time (mins) =" + endState.actualTime);
        System.out.println("Total changes: " + endState.changes);
    }
}
