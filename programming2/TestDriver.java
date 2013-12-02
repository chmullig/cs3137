import java.util.*;
import java.util.logging.Level;
import java.io.*;
import java.lang.*;

public class TestDriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		HashTable<String, String> myHT = new HashTable<String, String>();
//		myHT.put("Hello", "World");
//		myHT.put("Tuna", "Salad");
//		myHT.put("Lettuce", "Salad");
//		myHT.put("Tomato", "with Mozzarella");
//		System.out.println(myHT.get("Hello"));
//		System.out.println(myHT.get("Tomato"));
//		
//		for (int i = 0; i <  50; i++) {
//			myHT.put(i + " squared", Integer.toString(i*i));
//		}
//		
//		for (int i = 0; i <  50; i++) {
//			System.out.println(i + "^2 = " + myHT.get(i + " squared"));
//			myHT.remove(i + " squared");
//		}
//		
//		for (int i = 0; i < 50; i++) {
//			myHT.put(i + "x2", Integer.toString(i*2));
//			System.out.println(i + "x2 = " + myHT.get(i + "x2"));
//		}		
		
		File virusDir = new File("/Users/chmullig/Documents/Columbia/Current Classes/COMS3137 DSA/programming2/hwv");
		File benignDir = new File("/Users/chmullig/Documents/Columbia/Current Classes/COMS3137 DSA/programming2/hwb");
		
		VirusCollection myVC = null;
//		boolean loaded = false;
//		try {
//			FileInputStream fileIn = new FileInputStream("myVirusCollection.ser");
//			ObjectInputStream in = new ObjectInputStream(fileIn);
//			System.out.println("Loading serialized virus collection...");
//			myVC = (VirusCollection) in.readObject();
//			in.close();
//			fileIn.close();
//			loaded = true;
//		} catch(IOException i) {
//			System.out.println("Unable to load serialized virus collection");
//		}catch(ClassNotFoundException c) {
//			System.out.println("VirusCollection class not found");
//			c.printStackTrace();
//			return;
//		}
		
		if (myVC == null) {
			myVC = new VirusCollection();
			myVC.loadDirectory(virusDir, true);
			myVC.loadDirectory(benignDir, false);
		}
		
		File testDir = new File("/Users/chmullig/Documents/Columbia/Current Classes/COMS3137 DSA/programming2/test");  
		for (File file: testDir.listFiles()) {
			try {
				FileReader fileReader = new FileReader(file);
				BufferedReader fileBuffer = new BufferedReader(fileReader);
				StringBuffer contents = new StringBuffer();
				contents.append(fileBuffer.readLine().replaceAll(" ", ""));
				
				double prediction = myVC.computeNB(contents.toString());
				System.out.println("File " + file.getName() + " prediction: " + prediction);
				
				fileBuffer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
//		if (!loaded) {
//			try {
//				FileOutputStream fileOut = new FileOutputStream("myVirusCollection.ser");
//				ObjectOutputStream out = new ObjectOutputStream(fileOut);
//				out.writeObject(myVC);
//				out.close();
//				fileOut.close();
//				System.out.printf("Serialized data is saved");
//			} catch(IOException i) {
//				i.printStackTrace();
//			}
//		}

	}

}
