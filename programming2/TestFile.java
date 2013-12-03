/**
 * @author Chris Mulligan <clm2186>
 * 
 * Trivial object to hold 3 values, since it's non-trivial to make a 3-tuple
 *
 */
public class TestFile {
	public String body;
	public String name;
	public String type;
	
	public TestFile(String name, String type, String body) {
		this.name = name;
		this.type = type;
		this.body = body;
	}
}