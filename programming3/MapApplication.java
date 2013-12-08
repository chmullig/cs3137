import java.util.*;
import java.lang.*;
import java.beans.DesignMode;
import java.io.*;

/**
 * @author Chris Mulligan <clm2186@columbia.edu>
 *
 */
public class MapApplication {
	public MyGraphMap13 map;
	public List<String> loadedFiles;
	
	
	public MapApplication() {
		map = new MyGraphMap13();
		loadedFiles = new LinkedList<String>();
	}
	
	public boolean hasCities() {
		return !loadedFiles.isEmpty();
	}
	
	/**
	 * a. Load up a city file into the system
	 * 
	 * @param filename
	 * @return
	 * @throws FileNotFoundException
	 */
	public int a(String filename, boolean reset) throws FileNotFoundException {
		if (reset) {
			map = new MyGraphMap13();
			loadedFiles = new LinkedList<String>();
		}
		if (loadedFiles.contains(filename))
			return 0;
		BufferedReader file = new BufferedReader(new FileReader(filename));
		loadedFiles.add(filename);
		int loaded = map.loadFile(file);
		map.addRandomFlights(2, 8, 100, 2000);
		return loaded;
	}
	
	/**
	 * b. Search for state and list all cities there with their in/out counts.
	 * 
	 * @param state
	 * @return a long string where each city is toString()'d on a separate line
	 */
	public String b(String state) {
		StringBuffer results = new StringBuffer();
		for (City city: map.getCitiesInState(state)) {
			results.append(city.toString());
			results.append("\n");
		}
		return results.toString();
	}
	
	/**
	 * c. Search for city and display some information about it.
	 * 
	 * @param id city id number
	 * @return the city's toString()
	 */
	public String c(int id) {
		City match = map.findCity(id);
		return match.toString();
	}
	

	/**
	 * d. Set current city as the starting point.
	 * 
	 * @param id City ID number
	 */
	public void d(int id) {
		City match = map.findCity(id);
		map.setCurrentCity(match);
	}
	
	/**
	 * e. Show current city
	 * 
	 * @return the current city's toString()
	 */
	public String e() {
		City current = map.getCurrentCity();
		if (current != null)
			return current.toString();
		else
			return "none";
	}
	
	private void randomCurrent() {
		int randomCityID = (int)Math.random()*map.getCityCount();
		City randomCurrent = map.getCities().get(randomCityID);
		map.setCurrentCity(randomCurrent);
	}
	
	/**
	 * f. Find n closest cities using gps distances 
	 * 
	 * @param n number of cities
	 * @return
	 */
	public String f(int n) {
		City current = map.getCurrentCity();
		if (current == null)
			randomCurrent();
		
		List<City> closestN = map.nByDistance(n);
		
		StringBuffer results = new StringBuffer();
		for (City city: closestN) {
			results.append(city.toString());
			results.append("\n");
		}
		return results.toString();
	}
	
	/**
	 * g. find n closest by cost
	 * 
	 * @param n
	 * @return
	 */
	public String g(int n) {
		City current = map.getCurrentCity();
		if (current == null)
			randomCurrent();
		
		List<City> closestN = map.nByCost(n);
		
		StringBuffer results = new StringBuffer();
		for (City city: closestN) {
			results.append(city.toString());
			results.append("\n");
		}
		return results.toString();
	}
	
	
	/**
	 * h. find shortest path
	 * 
	 * between current city and 
	 * 
	 * @param targetID
	 * @return
	 */
	public String h(int targetID) {
		City current = map.getCurrentCity();
		if (current == null)
			randomCurrent();
		City target = map.findCity(targetID);
		List<Flight> path = map.cheapestPath(target);
		
		StringBuffer results = new StringBuffer();
		for (Flight flight: path) {
			results.append(flight.toString());
			results.append("\n");
		}
		return results.toString();
	}
	
	public void j(String filename) throws FileNotFoundException {
		PrintStream dot = new PrintStream(filename +".dot");
		map.makeGraphviz(dot);
	}
	
	public void k(String filename) throws FileNotFoundException {
		PrintStream nodes = new PrintStream(filename +"_nodes.csv");
		map.makeGephiNodes(nodes);
		PrintStream edges = new PrintStream(filename +"_edges.csv");
		map.makeGephiEdges(edges);
	}

}
