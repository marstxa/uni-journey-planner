package modules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

public class ReadMap {
    // Reads the Metrolink CSV format and populates the Dijkstra planner

    public static void loadMapData(String filePath, MetrolinkDijkstra planner, Set<String> extractedStations) {
        String line;
        String currentLineColor = "idk";

        // Using try to ensure file is closed
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            // read and discard first header row NOTE: From, To, Time (mins)
            br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] columns = line.split(",", -1);

                if (columns.length >= 3 && columns[1].trim().isEmpty() && columns[2].trim().isEmpty()) {
                    currentLineColor = columns[0].trim(); // update state to a new line color
                } else if (columns.length >= 3) {
                    try {
                        String formStation = columns[0].trim();
                        String toStation = columns[1].trim();
                        double travelTime = Double.parseDouble(columns[2].trim());

                        // add connection to graph
                        planner.addConnection(formStation, toStation, currentLineColor, travelTime);

                        // add to set
                        extractedStations.add(formStation);
                        extractedStations.add(toStation);
                    } catch (NumberFormatException e) {
                        // if it fails to parse the time (please don't)
                        System.err.println("Warning: Could not parse time for row: " + line);
                    }
                }
            }

            // Successful data read
            System.err.println("Map data loaded succesfully");
        } catch (IOException e) {
            System.err.println("Could not read the file " + filePath);
            System.err.println("Make sure the file exists in the correct directory it should be in /utils per default");
        }
    }
}
