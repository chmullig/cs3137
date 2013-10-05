/**
 * @author Chris Mulligan
 * @email clm2186@columbia.edu
 *
 */
public class LinkNode<E> {
	private LinkNode next;
	private LinkNode previous;
	private E data;
	
	public LinkNode(E data) {
		next = previous = null;
		this.data = data;
	}

	public LinkNode getNext() {
		return next;
	}

	public void setNext(LinkNode next) {
		this.next = next;
	}

	public LinkNode getPrevious() {
		return previous;
	}

	public void setPrevious(LinkNode previous) {
		this.previous = previous;
	}

	public E getData() {
		return data;
	}

	public void setData(E data) {
		this.data = data;
	}
	

}
