import java.util.*;

/**
 * @author Chris Mulligan <clm2186>
 *
 * A doubly linked linked list class. Based in part on Weiss Chapter 3.
 * 
 * Contains a head and tail of {@link LinkNode} nodes. Each node then points to
 * the next and previous nodes in the list. If a node is tail the tail it's next
 * is null (and likewise for head's previous).
 * 
 * Internally each link node stores a count, so when adding or removing from the
 * list it will just increment/decrement the count when possible and avoid creating
 * duplicate entries.
 * 
 * Sorting in count order is based on
 * http://www.chiark.greenend.org.uk/~sgtatham/algorithms/listsort.html
 */
public class SuperDuperLinkedLists<E> implements Iterable<LinkNode<E>>{
	private LinkNode<E> head;
	private LinkNode<E> tail;
	private int size;
	
	public static final String PERCENT = "percent";
	public static final String COUNT = "count";
	public static final  String NONE = "none";
	
	
	public <E> SuperDuperLinkedLists() {
		head = tail = null;
		size = 0;
	}
	
	
	/**
	 * Add an element, either incrementing the count of the element if it already
	 * equals an element in the list, or appending it to the end of the list if
	 * not
	 * 
	 * @param data the value to add
	 */
	public void insert(E data) {
		if (size == 0) {
			head = tail = new LinkNode<E>(data);
		} else {
			LinkNode<E> oldNode = find(data);
			if (oldNode != null) {
				oldNode.incrementCount();
			} else {
				LinkNode<E> newNode = new LinkNode<E>(data);
				tail.setNext(newNode);
				newNode.setPrevious(tail);
				tail = newNode;
			}
		}
		size++;
	}
	
	
	/**
	 * Look for the first node with the given value
	 * 
	 * @param data the data to search for
	 * @return the first link node containing the data, or null if can't be found 
	 */
	public LinkNode<E> find(E data) {
		LinkNode<E> position = head;
		while(position != null) {
			if (position.getData().equals(data)) {
				return position;
			}
			position = position.getNext();
		}
		return null;
	}
	
	
	/**
	 * Decrement the count on the the first node whose data .equals() the given
	 * data. If the new count is zero, remove it from the list entirely.
	 * 
	 * Uses find to find the node, and delete(LinkedNode) to delete it.
	 * 
	 * @param data the data element to delete the first occurrence of
	 * @throws SDLException If the element cannot be found
	 */
	public void delete(E data) throws SDLException {
		LinkNode<E> toDelete = find(data);
		delete(toDelete);
	}
	
	
	/**
	 * Decrement the count for a specified LinkNode in the list. If the new count
	 * is zero delete it from the list by bypassing it.
	 * 
	 * @param toDelete the node to be deleted from the list
	 * @throws SDLException if the node cannot be found
	 */
	public void delete(LinkNode<E> toDelete) throws SDLException {
		if (toDelete == null) {
			throw new SDLException();
		}
		
		toDelete.decrementCount();
		if (toDelete.isDead()) {
			//This was the last one, actually remove it from the list
			if (toDelete.getPrevious() != null)
				toDelete.getPrevious().setNext(toDelete.getNext());
			if (toDelete.getNext() != null)
				toDelete.getNext().setPrevious(toDelete.getPrevious());
			
			if (toDelete == head)
				head = head.getNext();
			if (toDelete == tail)
				tail = tail.getPrevious();
		}
		size--;
	}

	
	public LinkNode<E> getHead() {
		return head;
	}

	public LinkNode<E> getTail() {
		return tail;
	}

	public int getSize() {
		return size;
	}
	
	
	/**
	 * Print out the Linked List in order, with metadata and helpful tips. 
	 * 
	 * If the parameter is "count" it prints the number of times each entry shows up
	 * If the parameter is "percent" it prints the percentage of entries this
	 * is. Example output for count:
	 * 
	 * size: [head] -> element1:count -> element2:count <- [tail]
	 * 
	 * @param format "count" or "percent" or "none", from the obvious constants
	 */
	public void print(String format) {
		System.out.print(getSize() + ": [head]");
		Iterator<LinkNode<E>> ll = iterator();
		while (ll.hasNext()) {
			LinkNode<E> item = ll.next();
			System.out.print(" -> " + item.getData());
			if (format.equals(COUNT))
				System.out.print(":" + item.getCount());
			else if (format.equals(PERCENT))
				System.out.print(" " + Math.round(((double)item.getCount()/size)*1000.0)/10f + "%");
		}
		System.out.println(" <- [tail]");
	}
	
	
	/**
	 * Reverse the list in place, such that the head now points to the old tail.
	 * Also reverses all links (as a doubly linked list this is "easy" 
	 */
	public void reverse() {
		LinkNode<E> current = head;
		while (current != null) {
			LinkNode<E> temp = current.getNext();
			current.setNext(current.getPrevious());
			current.setPrevious(temp);
			current = temp;
		}
		LinkNode<E> temp = head;
		head = tail;
		tail = temp;
	}
	
	
	/**
	 * Sort the linked list with an in place mergesort, based on the value of
	 * count. It's in ascending (smallest to largest) order.
	 * 
	 * Implemented based on:
	 * http://www.chiark.greenend.org.uk/~sgtatham/algorithms/listsort.html
	 */
	public void sort() {
		LinkNode<E> p, q, e;
		int insize, nmerges, psize, qsize;
		insize = 1;
		
		while (true) {
			p = head;
			head = tail = null;
			nmerges = 0;
			while (p != null) {
				nmerges++;
				q = p;
				psize = 0;
				for (int i = 0; i < insize; i++) {
					psize++;
					q = q.getNext();
					if (q == null) 
						break;
				}
				qsize = insize;
				
				// now merge the two lists
				while (psize > 0 || (qsize > 0 && q != null)) {
					e = null;
					// decide whether to take e from q or p
					if (psize == 0) {
						// p empty, take from q
						e = q;
						q = q.getNext();
						qsize--;
					} else if (qsize == 0 || q == null) {
						// q empty, take from p
						e = p;
						p = p.getNext();
						psize--;
					} else if (p.getCount() <= q.getCount()) {
						//p is less or eq q, take from p
						e = p;
						p = p.getNext();
						psize--;						
					} else if (p.getCount() > q.getCount()) {
						// q is less, take from q
						e = q;
						q = q.getNext();
						qsize--;
					}
					
					if (tail != null) {
						// add to end of LL
						tail.setNext(e);
					} else {
						head = e;
					}
					e.setPrevious(tail);
					tail = e;
				} // end select loop
				p = q;
			}
			tail.setNext(null);
			
			if (nmerges <= 1) {
				break;
			}
			
			insize *= 2;
		}
	}
	
	
	/**
	 * Print the n most frequently occurring elements in the list.
	 * 
	 * Implemented through a horrible hack:
	 *  1. clone the current linked list
	 *  2. sort the clone by count 
	 *  3. print the last n elements from the tail backwards 
	 * 
	 * @param n the number of elements to print
	 */
	public void printN(int n) {
		SuperDuperLinkedLists<E> sortedLL = this.clone();
		sortedLL.sort();
		LinkNode<E> current = sortedLL.getTail();
		for (int i = 0; i < n; i++) {
			if (current == null)
				break;
			System.out.println("\t" + current.getData() + " " + Math.round(((double)current.getCount()/size)*1000.0)/10f + "%");
			current = current.getPrevious();
		}
		System.out.println();
	}
	
	
	/* 
	 * Make a copy where all the nodes are identically structured new nodes. The
	 * data will be the same though. 
	 * 
	 * @see java.lang.Object#clone()
	 */
	public SuperDuperLinkedLists<E> clone() {
		SuperDuperLinkedLists<E> copy = new SuperDuperLinkedLists<E>();
		Iterator<LinkNode<E>> ll = iterator();
		while (ll.hasNext()) {
			LinkNode<E> source = ll.next();
			copy.insert(source.getData());
			if (source.getCount() != 1) {
				copy.getTail().setCount(source.getCount());
				copy.size += source.getCount() - 1; 
			}
		}
		return copy;
	}
	

	public Iterator<LinkNode<E>> iterator() {
		return new SDLIterator();
	}
	
	
	/**
	 * @author Chris Mulligan <clm2186>
	 *
	 * Iterator for standard forward only iteration through the LinkedList
	 * On calling next it returns the actual node, rather than the node's data,
	 * to enable caller to get the count. 
	 * 
	 * Does not implement remove.
	 */
	private class SDLIterator implements Iterator<LinkNode<E>> {
		private LinkNode<E> current = head;
		
		public boolean hasNext() {
			return current != null;
		}
		
		public LinkNode<E> next() {
			if (hasNext()) {
				LinkNode<E> ret = current;
				current = current.getNext();
				return ret;
			} else {
				throw new NoSuchElementException();
			}
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	

}
