import javax.swing.*;

/**
 * @author Chris Mulligan <clm2186>
 *
 */
public class VirusGUI {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("Virus Scanner");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new VirusPanel());
		frame.pack();
		frame.setVisible(true);
	}
}
