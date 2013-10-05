import java.util.*;

/**
 * @author Chris Mulligan
 * @email clm2186@columbia.edu
 *
 */
public class HWTest {

	/**
	 * Tests the SuperDuperLinkedLists class
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args) {
		String[] words = {"Hello", "World.", "Goodbye.", "I miss C", "Or at least python"};

		SuperDuperLinkedLists<String> myLL = new SuperDuperLinkedLists<String>(); 
		for (String w: words) {
			myLL.insert(w);
		}
		myLL.print("count");
		
		System.out.println("\nAdd some duplicates, print out both ways.");		
		myLL.insert("Hello");
		myLL.insert("Goodbye.");
		myLL.insert("Goodbye.");
		myLL.print("count");
		myLL.print("percent");
		
		System.out.println("\nReversing");
		myLL.reverse();
		myLL.print("count");
		
		System.out.println("\nReversing again");
		myLL.reverse();
		myLL.print("count");

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
		myLL.print("count");
		
		System.out.println("\ndelete the head");
		try {
			myLL.delete("Hello");
			myLL.delete("Hello");
		} catch (SDLException e) {
			e.printStackTrace();
		}
		myLL.print("count");
		
		System.out.println("\nNow add another element and we'll delete the middle");
		myLL.insert("Hello Again");
		try {
			myLL.delete("Goodbye.");
			myLL.delete("Goodbye.");
			myLL.delete("Goodbye.");
		} catch (SDLException e) {
			e.printStackTrace();
		}
		myLL.print("count");
		
		
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
		long preInsert = System.currentTimeMillis();
		System.out.println("Current time is: " + preInsert);
		for (int i = 0; i < 1000; i++) {
			String num = Integer.toString(r.nextInt(30));
			numbersLL.insert(num);
		}
		long postInsert = System.currentTimeMillis();
		System.out.println("Inserting 1000 random integers took " + (postInsert-preInsert)/1000.0 + "s");
		numbersLL.print("percent");
		
		long preReverse = System.currentTimeMillis();
		numbersLL.reverse();
		long postReverse = System.currentTimeMillis();
		System.out.println("Reversing 1000 random integers took " + (postReverse-preReverse)/1000.0 + "s");

			

	}

}
