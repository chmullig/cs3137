import java.util.*;
import java.lang.*;
import java.io.*;

/**
 * @author Chris Mulligan <clm2186@columbia.edu>
 * Represent a flight between two cities. Because every city has a lat/long,
 * every flight has a distance, as defined by their great circle distance on
 * earth. Flights may optionally have a cost.  
 *
 */
public class Flight {
	private City origin;
	private City destination;
	private int cost;
	private double distance;
	final private double EARTH_RADIUS = 6371;
	
	
	public Flight(City origin, City destination, int cost) {
		this.origin = origin;
		this.destination = destination;
		this.cost = cost;
		calculateDistance();
	}

	public Flight(City origin, City dest) {
		this(origin, dest, Integer.MAX_VALUE);
	}
	
	
	/**
	 * Calculate using great circle distance in kilometers.
	 * Based in part on http://stackoverflow.com/a/837957/349931
	 */
	private void calculateDistance() {
		double dLat = Math.toRadians(destination.getLatitude() - origin.getLatitude());
		double dLng = Math.toRadians(destination.getLongitude() - origin.getLongitude());
		double angle =	Math.sin(dLat/2) * Math.sin(dLat/2) +
	               		Math.cos(Math.toRadians(destination.getLatitude())) * Math.cos(Math.toRadians(origin.getLatitude())) *
	               		Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(angle), Math.sqrt(1-angle));
	    distance = EARTH_RADIUS * c;
	}


	public City getOrigin() {
		return origin;
	}
	public void setOrigin(City origin) {
		this.origin = origin;
	}
	public City getDestination() {
		return destination;
	}
	public void setDestination(City destination) {
		this.destination = destination;
	}
	/**
	 * Integer.MAX_VALUE indicates this is unspecified.
	 * 
	 * @return
	 */
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public double getDistance() {
		return distance;
	}
	
	/** 
	 * Print it out like
	 * 
	 * [ORIGIN, NAME -> DEST, NAME / $COST / DISTkm]
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "[" + origin.getFullname() + " -> " + destination.getFullname() + " / $" + cost +" / " + Math.round(distance) + "km]";
	}

}
