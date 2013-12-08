import java.util.*;
import java.lang.*;
import java.io.*;

/**
 * @author Chris Mulligan <clm2186@columbia.edu>
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
		this(origin, dest, 0);
	}
	
	
	/**
	 * Calculate using great circle distance.
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
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public String toString() {
		return "[" + origin.getFullname() + " -> " + destination.getFullname() + " / $" + cost +" / " + Math.round(distance) + "km]";
	}

}
