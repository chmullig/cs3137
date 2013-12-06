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

	public MyGraphMap13() {
		cities = new HashMap<String, City>();
	}
	
	public MyGraphMap13(int size) {
		cities = new HashMap<String, City>(size);
	}
	
	
	public int loadFile(BufferedReader cityFileReader) {
		int numCities = 0;
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
				
				City city = new City(name, state, latitude, longitude);
				addCity(city);
			}
		} catch (NumberFormatException e) {
			System.err.println("Error, improperly formatted city file!");
		} catch (IOException e) {
			System.err.println("Error, improperly formatted city file!");
		}

		return numCities;
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
		City newCity = new City(name, state, latitude, longitude);
		addCity(newCity);
	}
	
	public boolean containsCity(String name) {
		return cities.containsKey(name);
	}
	
	public City findCity(String name) {
		City city = cities.get(name);
		return city;
	}

}
