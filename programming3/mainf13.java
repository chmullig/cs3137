import java.util.*;
import java.lang.*;
import java.io.*;

/**
 * @author Chris Mulligan <clm2186@columbia.edu>
 *
 */
public class mainf13 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String cityFileName = null;
		BufferedReader cityFileReader = null;
		try {
			cityFileName = args[0];
			cityFileReader = new BufferedReader(new FileReader(cityFileName));
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Please specify a city file!");
			System.exit(1);
		} catch (FileNotFoundException e) {
			System.err.println("File not found.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("IO Error.");
			System.exit(1);
		}
		
		MyGraphMap13 map = new MyGraphMap13();
		int numCities = map.loadFile(cityFileReader);
		
		List<City> cities = map.getCities();
		
		map.addRandomFlights(2, 8, 100, 2000);
		
		System.out.println("Loaded " + numCities);
		try {
			PrintStream dot = new PrintStream(args[0]+".dot");
			map.makeGraphviz(dot);
			dot.close();
			
			map.makeGephiEdges(new PrintStream(args[0]+"_edges.csv"));
			map.makeGephiNodes(new PrintStream(args[0]+"_nodes.csv"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		City source = cities.get(0);
		City target = cities.get(11);
		for (Flight out: source.getOutbound()) {
			System.out.println("\t" + out);
		}
		map.setCurrentCity(source);
		List<City> closest = map.nByCost(15);
		
//		System.out.println("\n\nn by cost");
//		for (City city: closest) {
//			System.out.println(city);
//		}
		
		System.out.println("\n\nSHORTEST COST PATH FROM " + source +" TO " + target);
		List<Flight> path = map.cheapestPath(target);
		for (Flight flt: path) {
			System.out.println(flt);
		}
		
//		System.out.println("\n\nn by distance");
//		closest = map.nByDistance(15);
//		for (City city: closest) {
//			System.out.println(city);
//		}
		System.out.println("\n\nSHORTEST DISTANCE PATH FROM " + source +" TO " + target);
		path = map.shortestPath(target);
		for (Flight flt: path) {
			System.out.println(flt);
		}
		

	}

}
