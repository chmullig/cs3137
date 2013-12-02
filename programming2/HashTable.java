import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;
import java.util.logging.*;
import java.lang.*;

/**
 * 
 */

/**
 * @author chmullig
 *
 */
public class HashTable<K, V> implements Map<K, V>, Serializable {
	private static final long serialVersionUID = 1L;
	private int capacity;
	private int size;
	private final double loadFactor = 0.66;
	private final K DELETED = (K) (new Boolean(false));
	
	private K[] keys;
	private V[] values;
	
	private static Logger LOGGER = Logger.getLogger("HashTable");

	
	// Primes from: http://planetmath.org/goodhashtableprimes
	final private int[] primes = {11, 53, 97, 193, 389, 769, 1543, 3079, 6151,
			12289, 24593, 49157, 98317, 196613, 393241, 786433, 1572869,
			3145739, 6291469, 12582917, 25165843, 50331653, 100663319, 201326611,
			402653189, 805306457, 1610612741};
	
	public HashTable() {
		this(11);
	}
	
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
	 * Uses quadratic probing!
	 * 
	 * @param key
	 * @return
	 */
	private int find(Object key) {
		return find(key, keys);
	}
	
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
	
	private void grow() {
        int i;
        for (i = 0; i < primes.length; i++) {
                if (primes[i] > capacity)
                        break;
        }
        int newCapacity = primes[i];
        LOGGER.log(Level.FINE, "Growing from: " + capacity + " to " + newCapacity);
        
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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
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
		keys = (K[]) new Object[capacity];
		values = (V[]) new Object[capacity];
	}


}
