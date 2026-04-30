
import java.util.HashSet;
import java.util.Set;
import modules.GetJourney;
import modules.MetrolinkDijkstra;
import modules.ReadMap;

public class Main {

    public static void main(String[] args) {
        // Initialise planner
        MetrolinkDijkstra planner = new MetrolinkDijkstra();

        // hold valid stations in set
        Set<String> validStations = new HashSet<>();

        //path to csv 
        String csvPath = "src/utils/Metrolink_times_linecolour(in).csv";

        // load data and get a set of valid stations
        ReadMap.loadMapData(csvPath, planner, validStations);

        if (validStations.isEmpty()) {
            System.err.println("[Error] No stations were loaded. Please check the CSV file");
            return;
        }

        // Call interface
        GetJourney.start(planner, validStations);
    }
}
