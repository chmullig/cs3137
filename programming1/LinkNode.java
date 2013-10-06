import java.lang.System.*;

/**
 * @author Chris Mulligan  <clm2186>
 * 
 * A double linked to be stored in a linked list.
 * 
 * Most functions are self explanatory - getting/setting the next, previous,
 * and data fields.
 * 
 *  This maintains an internal count of the number of times it's in use. It
 *  should generally be used through increment/decrement count, but can be set
 *  directly as needed.
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
	
	/**
	 * True if this node has a count of zero and should be killed
	 * 
	 * @return true if the internal count is 0
	 */
	public boolean isDead() {
		return count < 1;
	}
	
	/** 
	 * Prints out a debugging version of the node, with the memory addresses of
	 * this node, and the nodes it's pointing toward for previous and next.
	 * 
	 * [<-previous|this|next->]
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "[<-" + 
			((previous != null) ? Integer.toHexString(System.identityHashCode(previous)) : "null") +
				"|" + Integer.toHexString(System.identityHashCode(this)) + "|" +
			((next != null) ? Integer.toHexString(System.identityHashCode(next)) : "null") +
			"->]";
	}
	

}
