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
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		City source = cities.get(0);
		map.setCurrentCity(source);
		List<City> closest = map.nByCost(99);
		
		map.print(System.err);
		
		for (City city: closest) {
			System.out.println(city);
		}
		

	}

}
