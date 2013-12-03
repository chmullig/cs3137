import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;
import java.util.logging.*;
import java.lang.*;

/**
 * @author Chris Mulligan <clm2186>
 * 
 * HashTable class for COMSW3137 Programming 2
 * 
 * This implements a basic generic K, V hash table using quadratic probing.
 * 
 * Not all functioned specified by the Map<K,V> interface are meaningful complete,
 * only those I found necessary or interesting. This means things like
 * containsValue, and various Set, Map, iterator type things are undone. They
 * will throw UnsupportedOperationExceptions.
 * 
 * I use a single find(key) function to either locate the key in the underlying
 * arrays, or to locate where it could go. This is using quadratic probing. I 
 * therefore use lazy deletion, by setting the Key to a special sentinel object.
 * 
 * The backing arrays capacities are always prime, using a list from 
 * http://planetmath.org/goodhashtableprimes
 *
 */
public class HashTable<K, V> implements Map<K, V>, Serializable {
	private static final long serialVersionUID = 1L;
	private int capacity;
	private int size;
	private final double loadFactor = 0.66;
	@SuppressWarnings("unchecked")
	private final K DELETED = (K) (new Boolean(false));
	
	private K[] keys;
	private V[] values;
	
	private static Logger LOGGER = Logger.getLogger("HashTable");

	
	// Primes from: http://planetmath.org/goodhashtableprimes
	final private int[] primes = {11, 53, 97, 193, 389, 769, 1543, 3079, 6151,
			12289, 24593, 49157, 98317, 196613, 393241, 786433, 1572869,
			3145739, 6291469, 12582917, 25165843, 50331653, 100663319, 201326611,
			402653189, 805306457, 1610612741};
	
	/**
	 * Default constructor, sizes the array initially at 53;
	 */
	public HashTable() {
		this(53);
	}
	
	/**
	 * Constructor that lets the user provide a hint to the size. It will make
	 * the capacity actually equal to the smallest prime we have greater than or
	 * equal to the target capacity.
	 * 
	 * @param targetCapacity
	 */
	@SuppressWarnings("unchecked")
	public HashTable(int targetCapacity) {
		int i;
        for (i = 0; i < primes.length; i++) {
                if (primes[i] >= targetCapacity)
                        break;
        }
        capacity = primes[i];
		size = 0;
		keys = (K[]) new Object[capacity];
		values = (V[]) new Object[capacity];
		
		LOGGER.setLevel(Level.ALL);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		LOGGER.addHandler(handler);
	}
	
	/**
	 * Find the location of key in this hash table's arrays.
	 * 
	 * Actually calls find(key, keys) to do the real work.
	 * 
	 * @param key we want to find this
	 * @return the array position where this is, or would go if it can't be found
	 */
	private int find(Object key) {
		return find(key, keys);
	}
	
	/**
	 * Lets you find the position on an arbitrary array of keys. This is used
	 * during grow operations to let us simultaneously find in the old array
	 * and the new array.
	 * 
	 * Uses quadratic probing, and keeps track of lazily deleted objects, letting
	 * us overwrite deleted objects and relcaim their space.
	 * 
	 * @param key to find
	 * @param keys array of keys to look in
	 * @return the array position of where this key is, or would go
	 */
	private int find(Object key, K[] keys) {
		int initialPos = Math.abs(key.hashCode() % keys.length);
		int finalPos, deletedPos = -1;
		boolean found = false;
	
		for (int i = 0; ; i++) {
			finalPos = (initialPos+(i*i)) % keys.length;
			if (keys[finalPos] == null)  {
				//it's not in the table
				break;
			} else if (keys[finalPos] == DELETED && deletedPos == -1) {
				deletedPos = finalPos;
			} else if (keys[finalPos].equals(key)){
				//we found it
				return finalPos;
			}
		}
		
		if (deletedPos >= 0)
			return deletedPos;
		else
			return finalPos;
	}
	
	/**
	 * Increase the capacity to the next largest prime, copy the values over.
	 */
	@SuppressWarnings("unchecked")
	private void grow() {
        int i;
        for (i = 0; i < primes.length; i++) {
                if (primes[i] > capacity)
                        break;
        }
        int newCapacity = primes[i];
        //LOGGER.log(Level.FINE, "Growing from: " + capacity + " to " + newCapacity);
        
        K[] newKeys = (K[]) new Object[newCapacity];
        V[] newValues = (V[]) new Object[newCapacity];
        
        //Copy each value from the old thingie to the new thingie
        for (int oldPos = 0; oldPos < capacity; oldPos++) {
                if (keys[oldPos] != null && keys[oldPos] != DELETED) {
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


	/** 
	 * Take a key and value, and store it!
	 * 
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public V put(K key, V value) {
		int pos = find(key);
		if (keys[pos] == null || keys[pos] == DELETED) {
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
		if (keys[pos] != null && keys[pos].equals(key)) {
			return values[pos];
		} else {
			return null;
		}
	}

	@Override
	public boolean containsKey(Object key) {
		int pos = find(key);
		if (keys[pos] != null && keys[pos].equals(key)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}



	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public Set<K> keySet() {
		Set<K> setOfKeys = new HashSet<K>(size);
		for (K key: keys) {
			if (key != null && key != DELETED) {
				setOfKeys.add(key);
			}
		}
		return setOfKeys;
	}



	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException();
	}

	@Override
	public V remove(Object key) {
		int pos = find(key);
		V ret = null;
		if (pos > -1) {
			keys[pos] = (K) DELETED;
			ret = values[pos];
			values[pos] = null;
			size--;
		}
		return ret;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Collection<V> values() {
		throw new UnsupportedOperationException();
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public void clear() {
		size = 0;
		keys = (K[]) new Object[capacity];
		values = (V[]) new Object[capacity];
	}


}
