import java.util.*;
import java.io.*;

/**
 * @author Chris Mulligan <clm2186>
 *
 * Tests the SuperDuperLinkedLists class. See the comments on
 * {@link #main(String[])}
 * 
 * @see SuperDuperLinkedLists
 */
public class HWTest {

	/**
	 * Features a number of internal tests to to check that the linked list
	 * properly handles adding, finding, removing, sorting, printing, etc a
	 * {@link SuperDuperLinkedLists} linked list. 
	 * 
	 * Includes generating 1000 ints as strings, storing them in a linked list
	 * and manipulating them. (reverse, printN, etc).
	 * 
	 * Comments have been replaced by print statements to aid interpreting output
	 * 
	 * @param args takes a single argument - a file name to read in
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) {
		String[] words = {"Hello", "World.", "Goodbye.", "I miss C", "Or at least python"};

		SuperDuperLinkedLists<String> myLL = new SuperDuperLinkedLists<String>(); 
		for (String w: words) {
			myLL.insert(w);
		}
		myLL.print(SuperDuperLinkedLists.COUNT);
		
		System.out.println("\nAdd some duplicates, print out both ways.");		
		myLL.insert("Hello");
		myLL.insert("Goodbye.");
		myLL.insert("Goodbye.");
		myLL.print(SuperDuperLinkedLists.COUNT);
		myLL.print(SuperDuperLinkedLists.PERCENT);
		
		System.out.println("\nReversing");
		myLL.reverse();
		myLL.print(SuperDuperLinkedLists.COUNT);
		
		System.out.println("\nReversing again");
		myLL.reverse();
		myLL.print(SuperDuperLinkedLists.COUNT);

		System.out.println("\ndelete the tail");
		try {
			myLL.delete("Or at least python");
		} catch (SDLException e) {
			e.printStackTrace();
		}
		
		System.out.println("Try to delete something not here (and throw an exception)");
		try {
			myLL.delete("i am not real");
		} catch (SDLException e) {
			System.out.println("\tTried to delete an element not in the list, OK!");
		}
		myLL.print(SuperDuperLinkedLists.COUNT);
		
		System.out.println("\ndelete the head");
		try {
			myLL.delete("Hello");
			myLL.delete("Hello");
		} catch (SDLException e) {
			e.printStackTrace();
		}
		myLL.print(SuperDuperLinkedLists.COUNT);
		
		System.out.println("\nNow add another element and we'll delete the middle");
		myLL.insert("Hello Again");
		try {
			myLL.delete("Goodbye.");
			myLL.delete("Goodbye.");
			myLL.delete("Goodbye.");
		} catch (SDLException e) {
			e.printStackTrace();
		}
		myLL.print(SuperDuperLinkedLists.COUNT);
		
		
		System.out.println("\nPrint all the elements");
		Iterator<LinkNode<String>> myIterator = myLL.iterator();
		while (myIterator.hasNext()) {
			System.out.println(myIterator.next());
		}

		System.out.println("\nLooking for \"World.\", and \"i am not real\"");
		LinkNode<String> gb = myLL.find("World.");
		System.out.println(gb);
		LinkNode<String> missing = myLL.find("i am not real");
		System.out.println(missing);
		
		
		System.out.println("\nAdd 1000 integers [0-30] to a new list");
		SuperDuperLinkedLists<String> numbersLL = new SuperDuperLinkedLists<String>();
		Random r = new Random();
		long preInsert = System.nanoTime();
		System.out.println("Current time is: " + preInsert);
		for (int i = 0; i < 1000; i++) {
			String num = Integer.toString(r.nextInt(31));
			numbersLL.insert(num);
		}
		long postInsert = System.nanoTime();
		System.out.println("Inserting 1000 random integers took " + (postInsert-preInsert)/1000000.0 + "ms");
		numbersLL.print(SuperDuperLinkedLists.PERCENT);
		
		System.out.println("\nPrint the 3 most frequent items");
		numbersLL.printN(3);
		System.out.println("\nMake sure the original is intact");
		numbersLL.print(SuperDuperLinkedLists.PERCENT);
		
		long preReverse = System.nanoTime();
		numbersLL.reverse();
		long postReverse = System.nanoTime();
		System.out.println("Reversing 1000 random integers took " + (postReverse-preReverse)/1000000.0 + "ms");
		
		
		System.out.println("\nSort the list by count");
		numbersLL.sort();
		numbersLL.print(SuperDuperLinkedLists.PERCENT);	
		
		System.out.println("\n\nAttempting to load the file");
		// Step 8: load a file from args
		SuperDuperLinkedLists<String> fileLL = new SuperDuperLinkedLists<String>();
		if (args.length == 1) {
			String filename = args[0];
			File file = new File(filename);
			Scanner fileScanner = null;
			try {
				fileScanner = new Scanner(file);
			} catch (FileNotFoundException e) {
				System.out.println("File " + filename + " not found!");
				System.exit(1);
			}
			while (fileScanner.hasNext()) {
				fileLL.insert(fileScanner.nextLine());
			}
			
			fileLL.sort();
			fileLL.reverse();
			fileLL.print(SuperDuperLinkedLists.PERCENT);
		} else {
			System.out.println("No filename given. Please call with a file as the sole command line argument.");
			System.exit(1);
		}
	}

}
