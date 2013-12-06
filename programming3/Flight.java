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
	
	
	public Flight(City origin, City destination, int cost) {
		super();
		this.origin = origin;
		this.destination = destination;
		this.cost = cost;
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

}
