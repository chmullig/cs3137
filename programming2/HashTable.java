import java.lang.reflect.Array;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 */

/**
 * @author chmullig
 *
 */
public class HashTable<K, V> implements Map<K, V> {
	private int capacity;
	private int size;
	private final double loadFactor = 0.66;
	
	private K[] keys;
	private V[] values;
	
	private static Logger LOGGER = Logger.getLogger("HashTable");

	
	// Primes from: http://planetmath.org/goodhashtableprimes
	final private int[] primes = {11, 53, 97, 193, 389, 769, 1543, 3079, 6151,
			12289, 24593, 49157, 98317, 196613, 393241, 786433, 1572869,
			3145739, 6291469,12582917, 25165843, 50331653, 100663319, 201326611,
			402653189, 805306457, 1610612741};
	
	public HashTable() {
		capacity = primes[0];
		size = 0;
		keys = (K[]) new Object[capacity];
		values = (V[]) new Object[capacity];
		
		LOGGER.setLevel(Level.ALL);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		LOGGER.addHandler(handler);
	}
	
	/**
	 * Uses quadratic probing!
	 * 
	 * @param key
	 * @return
	 */
	private int find(Object key) {
		
		int initialPos = Math.abs(key.hashCode() % capacity);
		int finalPos = -1;
		
		LOGGER.log(Level.FINE, "Finding " + key.toString() + ". Hash code:" + key.hashCode() + ". Initial Pos: " + initialPos);

		for (int i = 0; ; i++) {
			finalPos = (initialPos+(i*i)) % capacity;
			if (keys[finalPos] == null)  {
				//we found it
				break;
			} else if (keys[finalPos].equals(key)){
				//it's not in the table
				break;
			}
		}
		LOGGER.log(Level.FINE, "Final Pos: " + finalPos);
		return finalPos;
	}
	
	private void grow() {
		
	}


	@Override
	public V put(K key, V value) {
		int pos = find(key);
		if (keys[pos] == null) {
			keys[pos] = key;
			values[pos] = value;
			size++;
			if (((float)size / capacity) > loadFactor) {
				grow();
			}
		} else if (keys[pos].equals(key)) {
			values[pos] = value;
		}
		return value;
	}
	
	@Override
	public V get(Object key) {
		int pos = find(key);
		if (keys[pos].equals(key)) {
			return values[pos];
		} else {
			return null;
		}
	}

	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<K> keySet() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public V remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}


}
