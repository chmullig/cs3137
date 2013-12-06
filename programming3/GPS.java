import java.util.*;
import java.lang.*;
import java.io.*;

/**
 * @author Chris Mulligan <clm2186@columbia.edu>
 *
 */
public class GPS {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String cityFileName = null;
		BufferedReader cityFileReader = null;
		try {
			cityFileName = args[0];
			cityFileReader = new BufferedReader(new FileReader(cityFileName));
			while (cityFileReader.ready())
				System.out.println(cityFileReader.readLine());
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
		

	}

}
