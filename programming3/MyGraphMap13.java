import java.util.*;
import java.lang.*;
import java.io.*;

/**
 * @author Chris Mulligan <clm2186@columbia.edu>
 *
 */
public class MyGraphMap13 {
	private HashMap<String, City> cities;
	private int cityCount = 0;
	private int flightCount = 0;
	private City current;
	private int lastcityid = 0;

	public MyGraphMap13() {
		cities = new HashMap<String, City>();
		current = null;
	}
	
	public MyGraphMap13(int size) {
		cities = new HashMap<String, City>(size);
		current = null;
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
				String latStr = cityFileReader.readLine();
				String longStr = cityFileReader.readLine();
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
				
				lastcityid++;
				City city = new City(lastcityid, fullname, name, state, latitude, longitude);
				addCity(city);
				loaded++;
			}
		} catch (NumberFormatException e) {
			System.err.println("Error, improperly formatted city file!");
		} catch (IOException e) {
			System.err.println("Error, improperly formatted city file!");
		}
		cityCount += loaded;
		return loaded;
	}
	
	
	
	
	public void addFlight(Flight flight) {
		City origin = flight.getOrigin();
		if (!cities.containsKey(origin.getFullname())) {
			cities.put(origin.getFullname(), origin);
		}
		City dest = flight.getDestination();
		if (!cities.containsKey(dest.getFullname())) {
			cities.put(dest.getFullname(), dest);
		}
		origin.addOutbound(flight);
		dest.addInbound(flight);
	}
	
	public void addFlight(City origin, City destination, int cost) {
		Flight myFlight = new Flight(origin, destination, cost);
		addFlight(myFlight);
	}
	
	public void addCity(City city) {
		if (!cities.containsKey(city.getFullname())) {
			cities.put(city.getFullname(), city);
		}
	}
	
	public void addCity(String name, String state, double latitude, double longitude) {
		lastcityid++;
		City newCity = new City(lastcityid, name, state, latitude, longitude);
		addCity(newCity);
		cityCount++;
	}
	
	public boolean containsCity(String name) {
		return cities.containsKey(name);
	}
	
	public City findCity(String name) {
		City city = cities.get(name);
		return city;
	}
	
	public ArrayList<City> getCities() {
		Collection<City> cc= cities.values();
		ArrayList<City> cl = new ArrayList<City>(cc);
		return cl;
	}
	
	public void addRandomFlights(int lowerN, int upperN, int lowerCost, int upperCost) {
		List<City> cities = getCities();
		Random rand = new Random(1674473376168722L);
		for (City city: getCities()) {
			Collections.shuffle(cities);
			int nOutbound = rand.nextInt(upperN-lowerN)+lowerN;
			for (int i = 0; i < nOutbound; i++) {
				int cost = rand.nextInt(upperCost-lowerCost)+lowerCost;
				if (cities.get(i) == city) {
					nOutbound++;
					continue;
				}
				Flight flt = new Flight(city, cities.get(i), cost);
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
	
	
	public List<City> nByCost(int n) {
		if (current == null) {
			throw new IllegalStateException("must set current city first");
		}
		
		for (City city: getCities()) {
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
				int alt = u.getDistance() + flight.getCost();
				if (alt < dest.getDistance()) {
					dest.setDistance(alt);
					dest.setParent(current);
					if (!dest.isVisited()) {
						q.insert(dest);
					}
				}
			}
		}
		
		ArrayList<City> cities = getCities();
		Collections.sort(cities);
		
		return cities.subList(0, n);
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
