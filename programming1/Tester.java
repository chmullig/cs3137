import java.util.*;

/**
 * @author Chris Mulligan
 * @email clm2186@columbia.edu
 *
 */
public class Tester {

	/**
	 * Tests the SuperDuperLinkedLists class
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args) {
		String[] words = {"Hello", "World.", "Goodbye.", "I miss my mommy"};

		SuperDuperLinkedLists<String> myLL = new SuperDuperLinkedLists<String>(); 
		for (String w: words) {
			myLL.insert(w);
		}
		
		System.out.println(myLL.getSize());
		
		Iterator<String> myIterator = myLL.iterator();
		do {
			String s = myIterator.next();
			System.out.println(s);
		} while (myIterator.hasNext());
		
	}

}
