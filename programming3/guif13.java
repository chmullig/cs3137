import javax.swing.*;

/**
 * @author Chris Mulligan <clm2186>
 *
 */
public class guif13 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("Map 13");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new MapPanel());
		frame.pack();
		frame.setVisible(true);
	}
}
