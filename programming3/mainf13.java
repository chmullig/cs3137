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
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		MapApplication app = new MapApplication();

		try {
			String cityFileName = args[0];
			app.a(cityFileName, false);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Please specify a city file!");
			System.exit(1);
		} catch (FileNotFoundException e) {
			System.err.println("Please specify a city file!");
			System.exit(1);
		}
		
		InputStream source = null;
		if (args.length == 3) {
			try {
				source = new FileInputStream(args[1]);
			} catch (FileNotFoundException e) {
				System.err.println("In batch mode, pelase specify a valid command file!");
				System.exit(1);
			}
		} else {
			source = System.in;
		}
		Scanner scn = new Scanner(source);
		printHelp();
		while (true) {
			try {
				System.out.print("Command? ");
				String command = scn.nextLine().trim().toLowerCase();
				if (command.equals("a")) {
					System.out.print("Would you like to reset the database? ");
					String resetCommand = scn.nextLine().trim().toLowerCase();
					boolean reset = false;
					if (resetCommand.startsWith("y") || resetCommand.equals("true")) {
						reset = true;
					}
					System.out.print("What file would you like to load? ");
					String filename = scn.nextLine().trim();
					try {
						System.out.println("Loaded " + app.a(filename, reset));
					} catch (FileNotFoundException e) {
						System.out.println("Error loading file " + filename);
					}
				} else if (command.equals("b")) {
					System.out.print("State to search for: ");
					String state = scn.nextLine().trim();
					System.out.println(app.b(state));
				} else if (command.equals("c")) {
					System.out.print("CityID to search for: ");
					String cityStr = scn.nextLine().trim();
					Integer cityID = Integer.parseInt(cityStr);
					System.out.println(app.c(cityID));
				} else if (command.equals("d")) {
					System.out.print("Set current cityID to: ");
					String cityStr = scn.nextLine().trim();
					Integer cityID = Integer.parseInt(cityStr);
					app.d(cityID);
				} else if (command.equals("e")) {
					System.out.println(app.e());
				} else if (command.equals("f")) {
					System.out.print("closest by distance: n ?= ");
					String nStr = scn.nextLine().trim();
					int n = Integer.parseInt(nStr);
					System.out.println(app.f(n));
				} else if (command.equals("g")) {
					System.out.print("cheapest route: n ?= ");
					String nStr = scn.nextLine().trim();
					int n = Integer.parseInt(nStr);
					System.out.println(app.g(n));
				} else if (command.equals("h")) {
					System.out.print("Target CityID: ");
					String cityStr = scn.nextLine().trim();
					Integer targetID = Integer.parseInt(cityStr);
					System.out.println(app.h(targetID));
				} else if (command.equals("i")) {
					System.exit(0);
				} else {
					System.out.println("Unknown command!");
					printHelp();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void printHelp() {
		System.out.println(
				"COMMANDS:\n"+
				"=========\n" +
				"a. Load up a city file into the system\n" +
				"b. Search for state and list all cities there with their in/out counts.\n" +
				"c. Search for city and display some information about it.\n" +
				"d. Set current city as the starting point.\n" +
				"e. Show current city\n" + 
				"f. Find n closest cities to current city using gps distances.\n" +
				"g. Find n closest cities to current city using directed edge costs\n" +
				"h. Find shortest path between current and some target city\n" +
				"i. quit");
	}

}
