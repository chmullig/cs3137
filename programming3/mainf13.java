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

		
		boolean batch = false;
		Scanner scn;
		if (args.length == 1) {
			scn = new Scanner(new File(args[0]));
			System.out.println("entering batch mode");
			batch = true;
		} else {
			scn = new Scanner(System.in);
		}
		
		printHelp();
		while (true) {
			try {
				System.out.print("Command: ");

				String command = getInput(scn, batch);
				if (command.equals("a")) {
					boolean reset = false;
					if (app.hasCities()) {
						System.out.print("Would you like to reset the database? ");
						String resetCommand = getInput(scn, batch).toLowerCase();
						if (resetCommand.startsWith("y") || resetCommand.equals("true")) {
							reset = true;
						}
					}
					System.out.print("What file would you like to load? ");
					String filename = getInput(scn, batch);
					try {
						System.out.println("Loaded " + app.a(filename, reset));
					} catch (FileNotFoundException e) {
						System.out.println("Error loading file " + filename);
					}
				} else if (command.equals("b")) {
					System.out.print("State to search for: ");
					String state = getInput(scn, batch);
					System.out.println(app.b(state));
				} else if (command.equals("c")) {
					System.out.print("City Name to search for: ");
					String cityStr = getInput(scn, batch);
					System.out.println(app.c(cityStr));
				} else if (command.equals("d")) {
					System.out.print("Set current cityID to: ");
					String cityStr = getInput(scn, batch);
					Integer cityID = Integer.parseInt(cityStr);
					app.d(cityID);
				} else if (command.equals("e")) {
					System.out.println(app.e());
				} else if (command.equals("f")) {
					System.out.print("closest by distance: n ?= ");
					String nStr = getInput(scn, batch);
					int n = Integer.parseInt(nStr);
					System.out.println(app.f(n));
				} else if (command.equals("g")) {
					System.out.print("closest by cost: n ?= ");
					String nStr = getInput(scn, batch);
					int n = Integer.parseInt(nStr);
					System.out.println(app.g(n));
				} else if (command.equals("h")) {
					System.out.print("Cheapest Route Target CityID: ");
					String cityStr = getInput(scn, batch);
					Integer targetID = Integer.parseInt(cityStr);
					System.out.println(app.h(targetID));
				} else if (command.equals("i")) {
					System.exit(0);
					
				//Bonus below
				} else if (command.equals("j")) {
					System.out.print("Dot output filename (do not enter extension): ");
					String filename = getInput(scn, batch);
					app.j(filename);
				} else if (command.equals("k")) {
					System.out.print("Gephi output base filename (do not enter extension): ");
					String filename = getInput(scn, batch);
					app.k(filename);
				} else if (command.equals("z")) {
					app.map.print(System.out);
					
				} else {
					System.out.println("Unknown command!");
					printHelp();
				}
			} catch (java.util.NoSuchElementException e) {
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void printHelp() {
		System.out.println(
				"COMMANDS:\n"+
				"=========\n" +
				"  a. Load up a city file into the system\n" +
				"  b. Search for state and list all cities there with their in/out counts.\n" +
				"  c. Search for city and display some information about it.\n" +
				"  d. Set current city as the starting point.\n" +
				"  e. Show current city\n" + 
				"  f. Find n closest cities to current city using gps distances.\n" +
				"  g. Find n closest cities to current city using directed edge costs\n" +
				"  h. Find shortest path between current and some target city\n" +
				"  i. quit"
				+ "\n-----Bonus Commands------\n" +
				"  j. write GraphViz .dot file\n" +
				"  k. write Gephi .csv files"
				);
	}
	
	public static String getInput(Scanner scn, boolean batch) {
		String input = scn.nextLine();
		input = input.trim();
		if (batch)
			System.out.println(input);
		return input;
	}

}
