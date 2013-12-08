import java.util.*;
import java.lang.*;
import java.beans.DesignMode;
import java.io.*;


/**
 * @author Chris Mulligan <clm2186@columbia.edu>
 *
 * Wraps {@link MyGraphMap13} to support the Programming 3 requirements. Maps
 * a specific letter command to the appropriate MyGraph13 functions. 
 *
 */
public class MapApplication {
	public MyGraphMap13 map;
	public List<String> loadedFiles;
	
	
	public MapApplication() {
		map = new MyGraphMap13();
		loadedFiles = new LinkedList<String>();
	}
	
	/**
	 * @return true if any file has been loaded yet
	 */
	public boolean hasCities() {
		return !loadedFiles.isEmpty();
	}
	
	/**
	 * Pick a random city and make IT our current city.  
	 */
	private void randomCurrent() {
		int randomCityID = (int)Math.random()*map.getCityCount();
		City randomCurrent = map.getCities().get(randomCityID);
		map.setCurrentCity(randomCurrent);
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
	public String c(String qry) {
		List<City> matches = map.findCities(qry);
		StringBuffer results = new StringBuffer();
		for (City match: matches) {
			results.append(match.toString());
			results.append("\n");
			for (Flight flight: match.getInbound()) {
				results.append(String.format("   in<- %-36s  $%6s  %6skm\n", flight.getOrigin().getFullname(), flight.getCost(), Math.round(flight.getDistance())));
			}
			results.append("\n");
			for (Flight flight: match.getOutbound()) {
				results.append(String.format("   out-> %-35s  $%6s  %6skm\n", flight.getOrigin().getFullname(), flight.getCost(), Math.round(flight.getDistance())));
			}
		}
		return results.toString();
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
		if (current != null) {
			StringBuffer results = new StringBuffer();
			results.append(current.toString());
			results.append("\n");
			for (Flight flight: current.getInbound()) {
					results.append(String.format("   in<- %-36s  $%6s  %6skm\n", flight.getOrigin().getFullname(), flight.getCost(), Math.round(flight.getDistance())));
			}
			results.append("\n");
			for (Flight flight: current.getOutbound()) {
				results.append(String.format("   out-> %-35s  $%6s  %6skm\n", flight.getOrigin().getFullname(), flight.getCost(), Math.round(flight.getDistance())));
			}
			return results.toString();
		} else {
			return "none";
		}
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
			results.append(String.format("%-55s %6skm", city.toString(), city.getDistance()));
			
			City u = city;
			results.append("\t via ");
			List<String> path = new LinkedList<String>();
			while (u != null) {
				path.add(u.getFullname());
				u = u.getParent();
			}
			for (int i = path.size()-1; i >= 0; i--) {
				results.append(path.get(i));
				if (i > 0) {
					results.append(" -> ");
				}
			}
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
			results.append(String.format("%-55s $%6s", city.toString(), city.getDistance()));;
			
			City u = city;
			results.append("\t via ");
			List<String> path = new LinkedList<String>();
			while (u != null) {
				path.add(u.getFullname());
				u = u.getParent();
			}
			for (int i = path.size()-1; i >= 0; i--) {
				results.append(path.get(i));
				if (i > 0) {
					results.append(" -> ");
				}
			}
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
		
		int totalCost = 0;
		int totalDistance = 0;
		
		
		StringBuffer results = new StringBuffer();
		for (Flight flight: path) {
			totalCost += flight.getCost();
			totalDistance += flight.getDistance();
			results.append(String.format("   %-75s  total: $%6s    %6skm\n", flight, totalCost, totalDistance));
		}
		return results.toString();
	}
	
	/**
	 * j. added by Chris - saves a graphviz .dot file
	 * 
	 * @param filename
	 * @throws FileNotFoundException
	 */
	public void j(String filename) throws FileNotFoundException {
		PrintStream dot = new PrintStream(filename +".dot");
		map.makeGraphviz(dot);
	}
	
	/**
	 * k. added by Chris - saves gephi .csv files
	 * 
	 * @param filename
	 * @throws FileNotFoundException
	 */
	public void k(String filename) throws FileNotFoundException {
		PrintStream nodes = new PrintStream(filename +"_nodes.csv");
		map.makeGephiNodes(nodes);
		PrintStream edges = new PrintStream(filename +"_edges.csv");
		map.makeGephiEdges(edges);
	}

}
