package modules;

import java.util.Scanner;
import java.util.Set;

public class GetJourney {

    public static void start(MetrolinkDijkstra planner, Set<String> validStations) {

        Scanner scanner = new Scanner(System.in);

        // CLI Welcome Screen
        System.out.println("                 _-====-__-======-__-========-_____-============-__");
        System.out.println("               _(   WELCOME TO THE MANCHESTER METROLINK PLANNER   _)");
        System.out.println("            OO(                                                   )_");
        System.out.println("           0  (_                                                   _)");
        System.out.println("         o0     (_                                                _)");
        System.out.println("        o         '=-___-===-_____-========-___________-===-dwb-='");
        System.out.println("      .o                                _________");
        System.out.println("     . ______          ______________  |         |      _____");
        System.out.println("   _()_||__|| ________ |            |  |_________|   __||___||__");
        System.out.println("  (BNSF 1995| |      | |            | __Y______00_| |_         _|");
        System.out.println(" /-OO----OO\"\"=\"OO--OO\"=\"OO--------OO\"=\"OO-------OO\"=\"OO-------OO\"=P");
        System.out.println("#####################################################################");
        System.out.println();

        // Get user input
        String startStation = getValidStation(scanner, "Enter your starting station: ", validStations);
        String endStation = getValidStation(scanner, "Enter your destination station: ", validStations);

        // Output for testing
        System.out.println("\nPlanning route...");
        System.out.println("From: " + startStation);
        System.out.println("To: " + endStation + "...\n");

        planner.findShortestRoute(startStation, endStation); // task 3

        scanner.close();
    }

    private static String getValidStation(Scanner scanner, String prompt, Set<String> validStations) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            // Check if input exists in the list of valid stations
            // Used for loop to make it case-insensitive for better ux
            for (String station : validStations) {
                if (station.equalsIgnoreCase(input)) {
                    return station; // Return station capitalised
                }
            }

            // If station is invalid
            System.out.println("  [Error] '" + input + "' is not a recognized Metrolink station. Please try again.\n");
        }
    }
}
