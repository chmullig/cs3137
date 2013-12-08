import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.NumericShaper;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * @author Chris Mulligan (clm2186@columbia.edu)
 * 
 * GUI interface to the GPS application. Uses a {@link MapApplication} to
 * do the real world. GUI version of {@link mainf13}.
 *
 */
public class MapPanel extends JPanel {
	private MapApplication app;
	
	JLabel headerStatus;
	JLabel fileLabel;
	JLabel currentCity;
	JButton[] buttons;
	JTextArea outputArea;
	JLabel status;
	
	String[] commands = {"a. Load up a city file into the system",
			"b. Search for state",
			"c. Search for city",
			"d. Set current city",
			"e. Show current city",
			"f. Find n closest cities using gps distances.",
			"g. Find n closest cities using directed edge costs",
			"h. Find shortest path between current and some target city",
			"i. quit",
			"j. write GraphViz .dot file",
			"k. write Gephi .csv files"};
	
	String[] tooltips = {"a. Load up a city file into the system",
			"b. Search for state and list all cities there with their in/out counts.",
			"c. Search for city and display some information about it.",
			"d. Set current city as the starting point.",
			"e. Show current city",
			"f. Find n closest cities to current city using gps distances.",
			"g. Find n closest cities to current city using directed edge costs",
			"h. Find shortest path between current and some target city",
			"i. quit",
			"j. write GraphViz .dot file",
			"k. write Gephi .csv files"};
	
	/**
	 * Ask for a file and initialize the list  
	 */
	public MapPanel() {
		app = new MapApplication();
		
		setLayout(new BorderLayout());
		
		JPanel north = new JPanel();
		north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
		JLabel masthead = new JLabel("Chris's GPS");
		masthead.setFont(new Font("Dialog", Font.PLAIN, 24));
		masthead.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		north.add(masthead);
		headerStatus = new JLabel("Loaded Files: ");
		headerStatus.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		north.add(headerStatus);
		north.setBorder(new EmptyBorder(5, 5, 15, 5));
		add(north, BorderLayout.NORTH);
		
		
		JPanel commandPanel = new JPanel();
		commandPanel.setLayout(new BoxLayout(commandPanel, BoxLayout.Y_AXIS));
		commandPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		commandPanel.add(new JLabel("Commands:"));
		
		buttons = new JButton['k'-'a'];		
		for (int i = 0; i < buttons.length; i++) {
			char letter = (char) (i+'a');
			buttons[i] = new JButton(commands[i]);
			buttons[i].addActionListener(new ButtonListener());
			buttons[i].setMnemonic(letter);
			buttons[i].setToolTipText(tooltips[i]);
			commandPanel.add(buttons[i]);
		}
		
		commandPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(commandPanel, BorderLayout.WEST);
		
		
		//Center output window
		JPanel midPanel = new JPanel();
		midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.Y_AXIS));
		
		midPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(midPanel, BorderLayout.CENTER);
		currentCity = new JLabel("Current City: none");
		currentCity.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		currentCity.setHorizontalAlignment(SwingConstants.LEFT);
		midPanel.add(currentCity);

		JLabel outputLabel = new JLabel("Output:");
		outputLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		outputLabel.setHorizontalAlignment(SwingConstants.LEFT);
		midPanel.add(outputLabel);

		outputArea = new JTextArea(30, 60);
		outputArea.setEditable(false);
		JScrollPane sp = new JScrollPane(outputArea);
		sp.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		midPanel.add(sp);
		
		
		
		//bottom
		JPanel bottom = new JPanel();
		bottom.setBorder(new EmptyBorder(5, 5, 20, 5));
		status = new JLabel(" ");
		bottom.add(status);
        add(bottom, BorderLayout.SOUTH);
	}
	
	
	
	protected void setAllButtons(boolean enabled) {
		for (JButton b: buttons) 
			b.setEnabled(enabled);
		repaint();
	}
	
	/** Repaints the text if it has changed. Also disables or enables the sorting
	 * buttons as required, so you can't perform redundant searches.
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics page) {
		super.paintComponent(page);
		
		headerStatus.setText("Files Loaded: " + app.loadedFiles.toString());
		currentCity.setText("Current City: " + app.e());
	}
	
	public void a() {
		String workingDir = System.getProperty("user.dir");
		JFileChooser chooser = new JFileChooser(workingDir);
		String help = "Select a file to load";
		chooser.setDialogTitle(help);
		chooser.setFileFilter(new FileNameExtensionFilter("GPS files", "txt"));
		int chooserStatus = -1;
		chooserStatus = chooser.showOpenDialog(null);
		if (chooserStatus == JFileChooser.APPROVE_OPTION) {
			status.setText("Loading, please wait...");
			setAllButtons(false);
			repaint();
			
			File file = chooser.getSelectedFile();
			status.setText("Loading " + file.getName());
			status.repaint();
			try {
				app.a(file.getName(), false);
				status.setText("Loaded!");
			} catch (FileNotFoundException e) {
				status.setText("Error loading!");
				e.printStackTrace();
			}
			
			setAllButtons(true);
		}
	}
	public void b() {
		String state = JOptionPane.showInputDialog(null,
				  "What state do you want to search for?",
				  "State",
				  JOptionPane.QUESTION_MESSAGE);
		outputArea.setText(app.b(state));
	}
	public void c() {
		String cityText = JOptionPane.showInputDialog(null,
				  "Which city ID do you want?",
				  "CityID",
				  JOptionPane.QUESTION_MESSAGE);
		int cityID = Integer.parseInt(cityText);
		outputArea.setText(app.c(cityID));
	}
	public void d() {
		String cityText = JOptionPane.showInputDialog(null,
				  "Which city ID do you want?",
				  "CityID",
				  JOptionPane.QUESTION_MESSAGE);
		int cityID = Integer.parseInt(cityText);
		app.d(cityID);
	}

	public void e() {
		outputArea.setText(app.e());
	}

	public void f() {
		String nText = JOptionPane.showInputDialog(null,
				  "n = ?",
				  "Find n closest by GPS distance",
				  JOptionPane.QUESTION_MESSAGE);
		int n = Integer.parseInt(nText);
		outputArea.setText(app.f(n));
	}

	public void g() {
		String nText = JOptionPane.showInputDialog(null,
				  "n = ?",
				  "Find n closest by edge costs",
				  JOptionPane.QUESTION_MESSAGE);
		int n = Integer.parseInt(nText);
		outputArea.setText(app.g(n));
	}

	public void h() {
		String targetText = JOptionPane.showInputDialog(null,
				  "Which target city ID do you want?",
				  "Target CityID",
				  JOptionPane.QUESTION_MESSAGE);
		int targetID = Integer.parseInt(targetText);
		outputArea.setText(app.h(targetID));
	}

	public void i() {
		System.exit(0);
	}

	public void j() {
		String fileBase = JOptionPane.showInputDialog(null,
				  "Save Graphviz File as foo.dot. What is foo?",
				  "Save Graphviz",
				  JOptionPane.QUESTION_MESSAGE);
		try {
			app.j(fileBase);
			status.setText("Saved!");
		} catch (FileNotFoundException e) {
			status.setText("Error Saving!");
		}
	}

	public void k() {
		String fileBase = JOptionPane.showInputDialog(null,
				  "Save Gephi File as foo_edge.csv and foo_node.csv. What is foo?",
				  "Save Gephi",
				  JOptionPane.QUESTION_MESSAGE);
		try {
			app.k(fileBase);
			status.setText("Saved!");
		} catch (FileNotFoundException e) {
			status.setText("Error Saving!");
		}
	}
	
	/**
	 * @author Chris Mulligan <clm2186>
	 * Check which button was pushed and call the appropriate single letter
	 * function. Does a tiny bit of housekeeping before/after.
	 * 
	 */
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			status.setText(" ");
			if (event.getSource() == buttons[0]) {
				a();
			} else if (event.getSource() == buttons[1]) {
				b();
			} else if (event.getSource() == buttons[2]) {
				c();
			} else if (event.getSource() == buttons[3]) {
				d();
			} else if (event.getSource() == buttons[4]) {
				e();
			} else if (event.getSource() == buttons[5]) {
				f();
			} else if (event.getSource() == buttons[6]) {
				g();
			} else if (event.getSource() == buttons[7]) {
				h();
			} else if (event.getSource() == buttons[8]) {
				i();
			} else if (event.getSource() == buttons[9]) {
				j();
			} else if (event.getSource() == buttons[10]) {
				k();
			}
			repaint();
		}
	}

	
	
	
	
}
