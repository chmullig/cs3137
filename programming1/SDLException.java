/**
 * @author Chris Mulligan
 * @email clm2186@columbia.edu
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
