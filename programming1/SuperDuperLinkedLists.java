import java.util.*;

/**
 * @author Chris Mulligan
 * @email clm2186@columbia.edu
 *
 * A basic linked list class. Based in part on Weiss Chapter 3.
 * 
 * sorting based on http://www.chiark.greenend.org.uk/~sgtatham/algorithms/listsort.html
 */
public class SuperDuperLinkedLists<E> implements Iterable<LinkNode<E>>{
	private LinkNode<E> head;
	private LinkNode<E> tail;
	private int size;
	
	public <E> SuperDuperLinkedLists() {
		head = tail = null;
		size = 0;
	}
	
	/**
	 * Add a new node to the end of the list
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
	 * Deletes the first node whose data .equals() the given data
	 * 
	 * @param data the data element to delete the first occurrence of
	 * @throws SDLException If the element cannot be found
	 */
	public void delete(E data) throws SDLException {
		LinkNode<E> toDelete = find(data);
		delete(toDelete);
	}
	
	/**
	 * Delete a specified LinkNode from the list
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

	public void setHead(LinkNode<E> head) {
		this.head = head;
	}

	public LinkNode<E> getTail() {
		return tail;
	}

	public void setTail(LinkNode<E> tail) {
		this.tail = tail;
	}

	public int getSize() {
		return size;
	}
	
	public void print(String format) {
		System.out.print(getSize() + ": [head]");
		Iterator<LinkNode<E>> ll = iterator();
		while (ll.hasNext()) {
			LinkNode<E> item = ll.next();
			System.out.print(" -> " + item.getData());
			if (format.equals("count"))
				System.out.print(":" + item.getCount());
			else if (format.equals("percent"))
				System.out.print(" " + Math.round(((double)item.getCount()/size)*1000.0)/10f + "%");
		}
		System.out.println(" <- [tail]");
	}
	
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
			LinkNode<E> next = current.getNext();
			try {
				delete(current);
			} catch (SDLException e) {
				e.printStackTrace();
			}
			current = next;
			
		}
	}
	

}
