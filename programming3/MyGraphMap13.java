import java.util.*;
import java.lang.*;
import java.beans.DesignMode;
import java.io.*;

/**
 * @author Chris Mulligan <clm2186@columbia.edu>
 *
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
	
	public MyGraphMap13(int size) {
		cities = new ArrayList<City>();
		luCities = new HashMap<String, City>(size);
		current = null;
		rand = new Random();
		rand.setSeed(237465);
	}
	
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
	
	public void addCity(City city) {
		if (!cities.contains(city)) {
			city.setId(cities.size());
			cities.add(city);
			luCities.put(city.getFullname(), city);
			cityCount++;
		}
	}
	

	public void addFlight(Flight flight) {
		City origin = flight.getOrigin();
		if (!luCities.containsKey(origin.getFullname())) {
			luCities.put(origin.getFullname(), origin);
		}
		City dest = flight.getDestination();
		if (!luCities.containsKey(dest.getFullname())) {
			luCities.put(dest.getFullname(), dest);
		}
		origin.addOutbound(flight);
		dest.addInbound(flight);
		flightCount++;
	}
	
	public void addFlight(City origin, City destination, int cost) {
		Flight myFlight = new Flight(origin, destination, cost);
		addFlight(myFlight);
	}
	
	public boolean containsCity(String name) {
		return luCities.containsKey(name);
	}
	
	public City findCity(String name) {
		City city = luCities.get(name);
		return city;
	}
	
	public City findCity(int id) {
		return cities.get(id);
	}
	
	public List<City> getCities() {
		return cities;
	}
	
	public List<String> listStates() {
		List<String> states = new LinkedList<String>();
		for (City city: cities) {
			if (!states.contains(city.getState()))
				states.add(city.getState());
		}
		return states;
	}
	
	public List<City> getCitiesInState(String state) {
		List<City> matching = new LinkedList<City>();
		for (City city: cities) {
			if (city.getState().equals(state))
				matching.add(city);
		}
		return matching;
	}
	
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
	
	
	public void setCurrentCity(City city) {
		current = city;
	}
	
	public City getCurrentCity() {
		return current;
	}
	
	
	public int getCityCount() {
		return cityCount;
	}
	
	
	public List<City> nByCost(int n) {
		return dijkstra(DistanceMetric.COST, n);
	}
	
	public List<City> nByDistance(int n) {
		return dijkstra(DistanceMetric.DISTANCE, n);
	}
	
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
		
		if (n != cityCount)
			return sortedCities.subList(0, n);
		else
			return sortedCities;
	}
	
	public List<Flight> cheapestPath(City destination) {
		List<Flight> shortestPath = new LinkedList<Flight>();
		City u = destination;
		dijkstra(DistanceMetric.COST, 1);
		while (u != current) {
			shortestPath.add(u.getParentFlight());
			u = u.getParent();
		}
		
		Collections.reverse(shortestPath);
		return shortestPath;
	}
	
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

	

	public void print(PrintStream out) {
		for (City city: getCities()) {
			out.print(city.toString() + " -> {");
			for (Flight flight: city.getOutbound()) {
				out.print(flight.getDestination().getFullname() +" $" + flight.getCost() +"; ");
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
		out.println("digraph G {");
		for (City city: getCities()) {
			for (Flight flight: city.getOutbound()) {
				out.println("\"" + city.getFullname() + "\" -> \"" + flight.getDestination().getFullname() + "\";");
			}
			
		}
		out.println("}");
	}
}
