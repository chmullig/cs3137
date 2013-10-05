import java.lang.System.*;

/**
 * @author Chris Mulligan
 * @email clm2186@columbia.edu
 * 
 * An element to be stored in a linked list.
 * 
 * @see SuperDuperLinkedLists 
 */
public class LinkNode<E> {
	private LinkNode<E> next;
	private LinkNode<E> previous;
	private E data;
	private int count;


	public LinkNode(E data) {
		next = previous = null;
		this.data = data;
		count = 1;
	}

	
	public LinkNode<E> getNext() {
		return next;
	}

	public void setNext(LinkNode<E> next) {
		this.next = next;
	}

	public LinkNode<E> getPrevious() {
		return previous;
	}

	public void setPrevious(LinkNode<E> previous) {
		this.previous = previous;
	}

	public E getData() {
		return data;
	}

	public void setData(E data) {
		this.data = data;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public void incrementCount() {
		count++;
	}
	
	public void decrementCount() {
		count--;
	}
	
	public boolean isDead() {
		return count < 1;
	}
	
	public String toString() {
		return "[<-" + 
			((previous != null) ? Integer.toHexString(System.identityHashCode(previous)) : "null") +
				"|" + Integer.toHexString(System.identityHashCode(this)) + "|" +
			((next != null) ? Integer.toHexString(System.identityHashCode(next)) : "null") +
			"->]";
	}
	

}
