import java.util.*;
import java.lang.*;
import java.io.*;

/**
 * @author Chris Mulligan <clm2186@columbia.edu>
 *
 * City. Has 3 names, the city name, the state name, and the full name. Also
 * contains lists of incoming and outgoing flights. Keeps track of data for
 * DJikstra's algorithm.
 *
 */
public class City implements Comparable<City> {
	private String name;
	private String state;
	private String fullname;
	private List<Flight> outbound;
	private List<Flight> inbound;
	private double latitude;
	private double longitude;
	private int id;
	private int distance;
	private boolean visited;
	private City parent;
	private Flight parentFlight;
	
	public City(String name, String state, double latitude, double longitude) {
		this(-1, name, state, latitude, longitude);
	}
	
	public City(int id, String name, String state, double latitude, double longitude) {
		this.id = id;
		this.name = name;
		this.state = state;
		this.latitude = latitude;
		this.longitude = longitude;
		this.fullname = name + ", " + state;
		initialize();
	}
	
	public City(String fullname, String name, String state, double latitude, double longitude) {
		this(-1, fullname, name, state, latitude, longitude);
	}
	
	
	public City(int id, String fullname, String name, String state, double latitude, double longitude) {
		this.id = id;
		this.fullname = fullname;
		this.name = name;
		this.state = state;
		this.latitude = latitude;
		this.longitude = longitude;
		initialize();
	}
	
	public City(int id, String full, double latitude, double longitude) {
		this.id = id;
		setName(full);
		this.latitude = latitude;
		this.longitude = longitude;
		initialize();
	}
	
	private void initialize() {
		outbound = new LinkedList<Flight>();
		inbound = new LinkedList<Flight>();
		reset();
	}
	
	/**
	 * Reset the temporary info (distance, visited,  parent, parentFlight) for
	 * {@link MyGraphMap13#dijkstra(DistanceMetric, int)}
	 */
	public void reset() {
		distance = Integer.MAX_VALUE;
		visited = false;
		parent = null;
		parentFlight = null;
	}
	
	public void setName(String full) {
		if (full.contains(",")) {
			String[] parts = full.split(",");
			if (parts.length == 2) {
				this.name = parts[0].trim();
				this.state = parts[1].trim();
				this.fullname = full.trim();
			}
		} else {
			this.name = full.trim();
			this.state = full.trim();
			this.fullname = full.trim() + ", " + full.trim();
		}
	}
	
	public void addOutbound(Flight flight) {
		outbound.add(flight);
	}
	
	public void addInbound(Flight flight) {
		inbound.add(flight);
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public List<Flight> getOutbound() {
		return outbound;
	}

	public List<Flight> getInbound() {
		return inbound;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public City getParent() {
		return parent;
	}

	public void setParent(City parent) {
		this.parent = parent;
	}

	public Flight getParentFlight() {
		return parentFlight;
	}

	public void setParentFlight(Flight parentFlight) {
		this.parentFlight = parentFlight;
	}

	public String getName() {
		return name;
	}	

	
	/** Include in and out count.
	 * 
	 * {NAME. In: # Out: #}
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "{" + id + ": " + fullname + ". In: " + inbound.size() + " Out: " + outbound.size() + "}";
	}

	/** This compares based on DISTANCE! This is semi-nonsensical in other contexts
	 * but whatever.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(City other) {
		return this.distance - other.getDistance();
	}
}
