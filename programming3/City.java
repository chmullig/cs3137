import java.util.*;
import java.lang.*;
import java.io.*;

/**
 * @author Chris Mulligan <clm2186@columbia.edu>
 *
 */
public class City {
	private String name;
	private String state;
	private String fullname;
	private List<Flight> outbound;
	private List<Flight> inbound;
	double latitude;
	double longitude;
	
	public City(String name, String state, double latitude, double longitude) {
		super();
		this.name = name;
		this.state = state;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public City(String full) {
		setName(full);
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

}
