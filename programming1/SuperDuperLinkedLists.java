import java.util.*;

/**
 * @author Chris Mulligan
 * @email clm2186@columbia.edu
 *
 * A basic linked list class. Based in part on Weiss Chapter 3.
 */
public class SuperDuperLinkedLists<E> implements Iterable<E>{
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
		LinkNode<E> newNode = new LinkNode<E>(data);
		if (size == 0) {
			head = tail = newNode;
		} else {
			tail.setNext(newNode);
			tail = newNode;
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
	private void delete(LinkNode<E> toDelete) throws SDLException {
		if (toDelete != null) {
			if (toDelete == head) {
				head = head.getNext();
			}
			if (toDelete == tail) {
				tail = tail.getPrevious();
			}
			if (toDelete.getPrevious() != null)
				toDelete.getPrevious().setNext(toDelete.getNext());
			if (toDelete.getNext() != null)
				toDelete.getNext().setPrevious(toDelete.getPrevious());
			size--;
		} else {
			throw new SDLException();
		}
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
	
	public void print() {
		System.out.print("[head]");
		Iterator<E> ll = iterator();
		while (ll.hasNext()) {
			E item = ll.next();
			System.out.print(" -> " + item);
		}
		System.out.println(" <- [tail]");
	}

	public Iterator<E> iterator() {
		return new SDLIterator();
	}
	
	private class SDLIterator implements Iterator<E> {
		private LinkNode<E> current = head;
		
		public boolean hasNext() {
			return current != null;
		}
		
		public E next() {
			if (hasNext()) {
				E contents = current.getData();
				current = current.getNext();
				return contents;
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
