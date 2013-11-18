
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
	}

}
