import java.util.*;
import java.lang.*;
import java.beans.DesignMode;
import java.io.*;

/**
 * @author Chris Mulligan <clm2186@columbia.edu>
 *
 * Programming 3 assignment. This is the main graph of cities, and flights
 * between cities.
 * 
 * Internally it stores cities in a List, as well as a HashMap from Fullname to
 * City object.
 * 
 * Flights have two measures - distance and cost. It can perform all algorithms
 * like finding closest, and finding shortest path, using either metric. 
 * 
 * See README for more information.
 */
public class MyGraphMap13 {
	private List<City> cities;
	private HashMap<String, City> luCities;
	private int cityCount = 0;
	private int flightCount = 0;
	private City current;
	private Random rand;
	public enum DistanceMetric {
		COST, DISTANCE;
	}

	public MyGraphMap13() {
		cities = new ArrayList<City>();
		luCities = new HashMap<String, City>();
		current = null;
		rand = new Random();
		rand.setSeed(237465);
	}
	
	/**
	 * Given a reader, check the # of cities in the first row, then load
	 * all the cities in the file. NOTE, based on a piazza comment it swaps
	 * the lat/long from what was specified in the instructions. This way makes
	 * more sense to me.
	 * 
	 * It does NOT add random flights, use
	 * {@link #addRandomFlights(int, int, int, int)} for that
	 * 
	 * @param cityFileReader
	 * @return
	 */
	public int loadFile(BufferedReader cityFileReader) {
		int numCities = 0;
		int loaded = 0;
		try {
			numCities = Integer.parseInt(cityFileReader.readLine());
			for (int i = 0; i < numCities; i++) {
				String fullname = null;
				String name = null;
				String state = null;
				String rawname = cityFileReader.readLine();
				String longStr = cityFileReader.readLine();
				String latStr = cityFileReader.readLine();
				if (rawname.contains(",")) {
					fullname = rawname.trim();
					String[] split = rawname.split(",");
					name = split[0].trim();
					state = split[1].trim();
				} else {
					name = rawname.trim();
					state = name;
					fullname = name +", " + state;
				}
				
				Double latitude = null;
				Double longitude = null;
				try {
					latitude = Double.parseDouble(latStr);
					longitude = Double.parseDouble(longStr);
				} catch (NullPointerException e) {
					System.err.println(latStr);
					System.err.println(longStr);
				}
				
				City city = new City(fullname, name, state, latitude, longitude);
				addCity(city);
				loaded++;
			}
		} catch (NumberFormatException e) {
			System.err.println("Error, improperly formatted city file!");
		} catch (IOException e) {
			System.err.println("Error, improperly formatted city file!");
		}
		return loaded;
	}
	
	/**
	 * Add the city, and increment the appropriate counters.
	 * @param city
	 */
	public void addCity(City city) {
		if (!cities.contains(city)) {
			city.setId(cities.size());
			cities.add(city);
			luCities.put(city.getFullname(), city);
			cityCount++;
		}
	}
	

	/**
	 * "Add" a flight. This adds the flight to the origin's outbound list, and
	 * the destination's inbound list. It also increments the flight count.
	 * 
	 * @param flight
	 */
	public void addFlight(Flight flight) {		
		flight.getOrigin().addOutbound(flight);
		flight.getDestination().addInbound(flight);
		flightCount++;
	}
	
	/**
	 * Construction a Flight and then call {@link #addFlight(Flight)}
	 * 
	 * @param origin
	 * @param destination
	 * @param cost
	 */
	public void addFlight(City origin, City destination, int cost) {
		Flight myFlight = new Flight(origin, destination, cost);
		addFlight(myFlight);
	}
	
	/**
	 * Check if a city name is in the city list. Based on full name!
	 * 
	 * @param name
	 * @return
	 */
	public boolean containsCity(String name) {
		return luCities.containsKey(name);
	}
	
	/**
	 * Return the city based on full name.
	 * @param name
	 * @return
	 */
	public City findCity(String name) {
		City city = luCities.get(name);
		return city;
	}
	
	/**
	 * Return a city by its unique ID number.
	 * 
	 * @param id
	 * @return
	 */
	public City findCity(int id) {
		return cities.get(id);
	}
	
	/**
	 * Get a list of all the cities. Note that you can fuck things up if you
	 * mess with this! 
	 * 
	 * @return
	 */
	public List<City> getCities() {
		return cities;
	}
	
	/**
	 * Return a list of all the states in the database.
	 * 
	 * @return
	 */
	public List<String> listStates() {
		List<String> states = new LinkedList<String>();
		for (City city: cities) {
			if (!states.contains(city.getState()))
				states.add(city.getState());
		}
		return states;
	}
	
	/**
	 * Return a list of all the cities with a given state string.
	 * 
	 * @param state
	 * @return
	 */
	public List<City> getCitiesInState(String state) {
		List<City> matching = new LinkedList<City>();
		for (City city: cities) {
			if (city.getState().equals(state))
				matching.add(city);
		}
		return matching;
	}
	
	/**
	 * Add random flights. Parameters specified as described below. It doesn't
	 * allow self flights, nor multiple flights with the same origin and
	 * destination.
	 * 
	 * @param lowerN min outbound flights per city
	 * @param upperN max outbound flights per city
	 * @param lowerCost min cost per flight
	 * @param upperCost max cost per flight
	 */
	public void addRandomFlights(int lowerN, int upperN, int lowerCost, int upperCost) {
		for (City city: cities) {
			int nOutbound = rand.nextInt(upperN-lowerN)+lowerN;
			for (int i = 0; i < nOutbound; i++) {
				int cost = rand.nextInt(upperCost-lowerCost)+lowerCost;
				if (cities.get(i) == city) {
					nOutbound++;
					continue;
				}
				int destI = rand.nextInt(cityCount);
				Flight flt = new Flight(city, cities.get(destI), cost);
				addFlight(flt);
			}
		}
	}
	
	
	/**
	 * Pick a city and name it our capital! We love this city!
	 * 
	 * @param city
	 */
	public void setCurrentCity(City city) {
		current = city;
	}
	
	public City getCurrentCity() {
		return current;
	}
	
	
	public int getCityCount() {
		return cityCount;
	}
	
	
	/**
	 * Get the N cheapest cities to current city, as measured by edge weigth (aka
	 * cost. Note that it calls {@link #dijkstra(DistanceMetric, int)}
	 * with {@link DistanceMetric}.COST to do everything.
	 * 
	 * @param n
	 * @return
	 */
	public List<City> nByCost(int n) {
		return dijkstra(DistanceMetric.COST, n);
	}
	
	/**
	 * Get the N closest cities to current city, as measured by great circle
	 * distance. Note that it calls {@link #dijkstra(DistanceMetric, int)}
	 * with {@link DistanceMetric}.DISTANCE to do everything.
	 * @param n
	 * @return
	 */
	public List<City> nByDistance(int n) {
		return dijkstra(DistanceMetric.DISTANCE, n);
	}
	
	/**
	 * Beginning from the current city, calculates cheapest path using Djikstra's
	 * algorithm. It will use either distance or cost as the weight, depending
	 * on the {@link DistanceMetric} passed in. It returns a list of the n
	 * closest cities. However it also has a nice side effect of setting all
	 * cities' distance, and parents to correct based on the DistanceMetric
	 * passed in. Thus it helps solve the {@link #cheapestPath(City)} problem. 
	 * 
	 * @param metric
	 * @param n
	 * @return
	 */
	private List<City> dijkstra(DistanceMetric metric, int n) {
		if (current == null) {
			throw new IllegalStateException("must set current city first");
		}
		
		for (City city: cities) {
			city.reset();
		}
		current.setParent(null);
		current.setDistance(0);
		
		BinaryHeap<City> q = new BinaryHeap<City>(); 
		q.insert(current);
		while (!q.isEmpty()) {
			City u = q.deleteMin();
			u.setVisited(true);
			for (Flight flight: u.getOutbound()) {
				City dest = flight.getDestination();

				int alt = Integer.MAX_VALUE;
				if (metric == DistanceMetric.COST) {
					alt = u.getDistance() + flight.getCost();
				} else if (metric == DistanceMetric.DISTANCE) {
					alt = (int) (u.getDistance() + flight.getDistance());
				}
				if (alt < dest.getDistance()) {
					dest.setDistance(alt);
					dest.setParent(u);
					dest.setParentFlight(flight);
					if (!dest.isVisited()) {
						q.insert(dest);
					}
				}
			}
		}
		
		List<City> sortedCities = new ArrayList<City>(cities);
		Collections.sort(sortedCities);
		
		return sortedCities.subList(1, n+1);
	}
	
	/**
	 * Find the cheapest path using costs from current city to the destination.
	 * 
	 * Calls {@link #dijkstra(DistanceMetric, int)} to compute distances/costs,
	 * then starts from the destination and moves backward. Returns null 
	 * if there is no path.
	 * 
	 * @param destination
	 * @return
	 */
	public List<Flight> cheapestPath(City destination) {
		List<Flight> shortestPath = new LinkedList<Flight>();
		City u = destination;
		dijkstra(DistanceMetric.COST, 1);
		if (u.getDistance() == Integer.MAX_VALUE) {
			return null;
		}
		while (u != current) {
			shortestPath.add(u.getParentFlight());
			u = u.getParent();
		}
		
		Collections.reverse(shortestPath);
		return shortestPath;
	}
	
	/**
	 * Same as cheapest path, but uses distance instead of cost.
	 * 
	 * @param destination
	 * @return
	 */
	public List<Flight> shortestPath(City destination) {
		List<Flight> shortestPath = new LinkedList<Flight>();
		City u = destination;
		dijkstra(DistanceMetric.DISTANCE, 1);
		while (u != current) {
			shortestPath.add(u.getParentFlight());
			u = u.getParent();
		}
		
		Collections.reverse(shortestPath);
		return shortestPath;
	}

	

	/**
	 * Print it out in a reasonably appealing manner.
	 * 
	 * @param out
	 */
	public void print(PrintStream out) {
		for (City city: getCities()) {
			out.print(city.toString() + "  ->  {");
			for (Flight flight: city.getOutbound()) {
				out.print(flight.getDestination().getFullname() +" $" + flight.getCost() +"/" + Math.round(flight.getDistance()) + "km;  ");
			}
			out.println("}");
		}
	}
	
	public void makeGephiEdges(PrintStream out) {
		out.println("source,target,distance,weight");
		for (City city: getCities()) {
			for (Flight flight: city.getOutbound()) {
				out.print("\"" + city.getFullname() +"\",");
				out.print("\"" + flight.getDestination().getFullname() +"\",");
				out.print(-flight.getDistance()/1000 +",");
				out.println((double)flight.getCost()/1000.0 +",");
			}
		}
	}
	
	public void makeGephiNodes(PrintStream out) {
		out.println("id,label,latitude,longitude");
		for (City city: cities) {
			out.print("\""+ city.getFullname() +"\",");
			out.print("\""+ city.getFullname() +"\",");
			out.print(city.getLatitude() +",");
			out.println(city.getLongitude() +",");
		}
	}


	public void makeGraphviz(PrintStream out) {
		out.println("digraph G {\n    graph [outputorder=edgesfirst]\n" +
					"edge [penwidth=1,color=\"#000000FF\"]\n" +
					"node [style=filled fillcolor=\"#FFFFFFAA\"]");
		for (City city: getCities()) {
			out.println("\"" + city.getFullname() + "\"[pos=\"" + city.getLongitude() +","+city.getLatitude()+ "!\"];");
			for (Flight flight: city.getOutbound()) {
				out.println("\"" + city.getFullname() + "\" -> \"" + flight.getDestination().getFullname() + "\";");
			}
			
		}
		out.println("}");
	}
}
