import java.util.*;

/**
 * @author Chris Mulligan
 * @email clm2186@columbia.edu
 *
 */
public class SuperDuperLinkedLists<E> implements Iterable<E>{
	private LinkNode<E> head;
	private LinkNode<E> tail;
	private int size;
	
	public <E> SuperDuperLinkedLists() {
		head = tail = null;
		size = 0;
	}
	
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
	
	public LinkNode<E> find(E data) {
		LinkNode<E> position = head;
		while(position != null) {
			if (position.getData().equals(data)) {
				return position;
			}
		}
		return null;
	}
	
	public void delete(E data) throws SDLException {
		LinkNode<E> toDelete = find(data);
		delete(toDelete);
	}
	
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

	public Iterator<E> iterator() {
		return new SDLIterator();
	}
	
	private class SDLIterator implements Iterator<E> {
		private LinkNode<E> current = head;
		
		public boolean hasNext() {
			return current.getNext() != null;
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
