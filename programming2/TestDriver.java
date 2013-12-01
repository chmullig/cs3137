
public class TestDriver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HashTable<String, String> myHT = new HashTable<String, String>();
		myHT.put("Hello", "World");
		myHT.put("Tuna", "Salad");
		myHT.put("Lettuce", "Salad");
		myHT.put("Tomato", "with Mozzarella");
		System.out.println(myHT.get("Hello"));
		System.out.println(myHT.get("Tomato"));
		
		for (int i = 0; i <  50; i++) {
			myHT.put(i + " squared", Integer.toString(i*i));
		}
		
		for (int i = 0; i <  50; i++) {
			System.out.println(i + "^2 = " + myHT.get(i + " squared"));
			myHT.remove(i + " squared");
		}
		
		for (int i = 0; i < 50; i++) {
			myHT.put(i + "x2", Integer.toString(i*2));
			System.out.println(i + "x2 = " + myHT.get(i + "x2"));
		}
		


	}

}
