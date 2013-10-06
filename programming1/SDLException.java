/**
 * @author Chris Mulligan <clm2186>
 *
 * Exception, mainly used when you attempt to delete something not in the list
 */
public class SDLException extends Exception {
	  public SDLException() {
		  super();
	  }
	  
	  public SDLException(String message) {
		  super(message);
	  }
}
