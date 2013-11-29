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
	
	private class KeyWrapper<K> {
		public boolean deleted;
		public K key;
		public KeyWrapper (K key) {
			this.key = key;
			this.deleted = false;
		}
		public String toString() {
			return key.toString();
		}
	}
	
	private int capacity;
	private int size;
	private final double loadFactor = 0.66;
	
	private KeyWrapper<K>[] keys;
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
		keys = (KeyWrapper<K>[]) new KeyWrapper[capacity];
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
		return find(new KeyWrapper<K>((K) key), keys);
	}
	
	/**
	 * Do the actual find of position for a key, based on key, and 
	 * 
	 * 
	 * @param key 
	 * @param capacity
	 * @param tempKeys
	 * @return
	 */
	private int find(KeyWrapper<K> key, KeyWrapper<K>[] tempKeys) {
		System.out.print("[");
		for (int i = 0; i < tempKeys.length; i++) {
			System.out.print(tempKeys[i] + ", ");
		}
		System.out.println("]");
		
		
		int initialPos = Math.abs(key.key.hashCode() % tempKeys.length);
		int finalPos = -1;
		int deletedPos = -1;
		boolean found = false;
		
		LOGGER.log(Level.FINE, "Finding " + key.key.toString() + ". Hash code:" + key.key.hashCode() + ". Initial Pos: " + initialPos);

		for (int i = 0; ; i++) {
			finalPos = (initialPos+(i*i)) % tempKeys.length;
			LOGGER.log(Level.FINE, "Probing with i = " + i + " and new position " + finalPos);
			if (tempKeys[finalPos] == null)  {
				//not in the table
				System.out.println("not equals");
				break;
			} else if (tempKeys[finalPos].deleted && deletedPos == -1) {
				System.out.println("deleted");
				deletedPos = finalPos;
			} else if (tempKeys[finalPos].key.equals((K) key)){
				System.out.println("equals");
				//it's in the table
				found = true;
				break;
			}
		}
		LOGGER.log(Level.FINE, "Final Pos: " + finalPos);
		//We want to return deletedPos only when we don't find it and there is
		//a deleted spot we can reuse.
		if (found) {
			return finalPos;
		} else if (deletedPos >= 0) {
			return deletedPos;
		} else {
			return finalPos;
		}
	}
	
	private void grow() {
		int i;
		for (i = 0; i < primes.length; i++) {
			if (primes[i] > capacity)
				break;
		}
		int newCapacity = primes[i];
		LOGGER.log(Level.FINE, "Growing from: " + capacity + " to " + newCapacity);
		
		KeyWrapper<K>[] newKeys = (KeyWrapper<K>[]) new KeyWrapper[capacity];
		V[] newValues = (V[]) new Object[newCapacity];
		
		//Copy each value from the old thingie to the new thingie
		for (int oldPos = 0; oldPos < capacity; oldPos++) {
			if (keys[oldPos] != null && !keys[oldPos].deleted) {
				int newPos = find(keys[oldPos], newKeys);
				newKeys[newPos] = keys[oldPos];
				newValues[newPos] = values[oldPos];
			}
		}
		
		//Swap them in!
		this.keys = newKeys;
		this.values = newValues;
		this.capacity = newCapacity;
		
	}


	@Override
	public V put(K key, V value) {
		int pos = find(key);
		if (keys[pos] == null || keys[pos].deleted) {
			keys[pos] = new KeyWrapper<K>(key);
			values[pos] = value;
			size++;
			if (((float)size / capacity) > loadFactor) {
				grow();
			}
		} else if (keys[pos].key.equals(key)) {
			values[pos] = value;
		}
		return value;
	}
	
	@Override
	public V get(Object targetKey) {
		K target = (K)targetKey;
		int pos = find(target);
		LOGGER.log(Level.FINE, "Getting " + target + ". Pos is " + pos + "found... " + keys[pos]);
		if (pos > -1 && keys[pos].key.equals((K)target)) {
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
		int pos = find(key);
		if (pos > -1) {
			keys[pos].deleted = true;
			size--;
			return values[pos];
		}
		return null;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public void clear() {
		size = 0;
		keys = (KeyWrapper<K>[]) new Object[capacity];
		values = (V[]) new Object[capacity];
	}


}
