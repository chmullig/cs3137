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
	
	public City findCity(String name) {
		City city = cities.get(name);
		if (city != null)
			return city;
		
		
		return null;
	}

}
