import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.NumericShaper;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;


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
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void k() {
		String fileBase = JOptionPane.showInputDialog(null,
				  "Save Gephi File as foo_edge.csv and foo_node.csv. What is foo?",
				  "Save Gephi",
				  JOptionPane.QUESTION_MESSAGE);
		try {
			app.k(fileBase);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @author Chris Mulligan <clm2186>
	 * Update the hyperparameters. We actually just set both whenever we set either.
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

	
	
	

	
	/**
	 * @author Chris Mulligan (clm2186)
	 * 
	 * (Attempt to) load a previously saved serialized file!
	 */
	private class LoadSerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String workingDir = System.getProperty("user.dir");
			JFileChooser chooser = new JFileChooser(workingDir);
			String help = "Select a .ser file containing a previously saved virus session";
			chooser.setDialogTitle(help);
			chooser.setFileFilter(new FileNameExtensionFilter("Serialized Session", "ser"));
			int chooserStatus = -1;
			chooserStatus = chooser.showOpenDialog(null);
			if (chooserStatus == JFileChooser.APPROVE_OPTION) {
				status.setText("Loading, please wait...");
				setAllButtons(false);
				repaint();
				try {
					FileInputStream fileIn = new FileInputStream(chooser.getSelectedFile());
					ObjectInputStream in = new ObjectInputStream(fileIn);
					//app = (VirusCollection) in.readObject();
					in.close();
					fileIn.close();
					status.setText("Loaded!");
					throw new ClassNotFoundException();
				} catch(IOException i) {
					JOptionPane.showMessageDialog(null, "Error: Unable to load serialized virus collection");
				} catch(ClassNotFoundException c) {
					JOptionPane.showMessageDialog(null, "Error: VirusCollection class not found");
				}
				setAllButtons(true);
			}
			repaint();
		}
	}
	
	/**
	 * @author Chris Mulligan (clm2186)
	 * 
	 * (Attempt to) load specified files and directories!
	 */
	private class LoadListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			boolean virus = false;
			
			String workingDir = System.getProperty("user.dir");
			JFileChooser chooser = new JFileChooser(workingDir);
			String help = "Select " + (virus ? "virus" : "benign") + " .hex files, or a directory";
			chooser.setDialogTitle(help);
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			chooser.setMultiSelectionEnabled(true);
			chooser.setFileFilter(new FileNameExtensionFilter("hex dump", "hex"));
			int chooserStatus = -1;
			chooserStatus = chooser.showOpenDialog(null);
			if (chooserStatus == JFileChooser.APPROVE_OPTION) {
				status.setText("Loading, please wait...");
				setAllButtons(false);
				repaint();
				
				
				for (File file: chooser.getSelectedFiles()) {
					status.setText("Loading " + file.getName());
					status.repaint();
//					if (file.isDirectory()) {
//						System.out.println("Loading directory " + file.getName());
//						//app.loadDirectory(file, virus);
//					} else {
//						try {
//							System.out.println("Loading file " + file.getName());
//							//app.loadFile(file, virus);
//						} catch (FileNotFoundException e1) {
//							System.out.println("error!");
//						}
//					}
				}
				System.out.println("Done loading.");
				status.setText("Loaded!");
				setAllButtons(true);
				repaint();
			}
			repaint();
		}
	}
	

	/**
	 * @author Chris Mulligan (clm2186)
	 * 
	 * (Attempt to) test specified files and directories!
	 */
	private class TestListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			String workingDir = System.getProperty("user.dir");
			JFileChooser chooser = new JFileChooser(workingDir);
			String help = "Select .hex files, or a directory to test";
			chooser.setDialogTitle(help);
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			chooser.setMultiSelectionEnabled(true);
			chooser.setFileFilter(new FileNameExtensionFilter("hex dump", "hex"));
			int chooserStatus = -1;
			chooserStatus = chooser.showOpenDialog(null);
			if (chooserStatus == JFileChooser.APPROVE_OPTION) {
				status.setText("Testing, please wait...");
				setAllButtons(false);
				repaint();
				
				for (File file: chooser.getSelectedFiles()) {
					status.setText("Testing " + file.getName());
					status.repaint();
					File[] files;
					if (file.isDirectory()) {
						files = file.listFiles();
					} else {
						files = new File[]{file};
					}
					try {
						for (File currFile: files) {
							FileReader fileReader = new FileReader(currFile);
							BufferedReader fileBuffer = new BufferedReader(fileReader);
							StringBuffer contents = new StringBuffer();
							contents.append(fileBuffer.readLine().replaceAll(" ", ""));
							
							//double prediction = app.computeNB(contents.toString());
							//JOptionPane.showMessageDialog(null, "File " + currFile.getName() + " prediction: " + prediction*100);
							
							fileBuffer.close();
						}
					} catch (IOException e1) {
						System.out.println("Testing IO exception");
						e1.printStackTrace();
					}
				}
				status.setText("Done testing!");
				setAllButtons(true);
				repaint();
			}
			repaint();
		}
	}
	
	/**
	 * @author Chris Mulligan (clm2186)
	 * 
	 * Save the current virus collection to a file.
	 */
	private class SaveListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String workingDir = System.getProperty("user.dir");
			JFileChooser chooser = new JFileChooser(workingDir);
			String help = "Save current session to a .ser file";
			chooser.setDialogTitle(help);
			chooser.setFileFilter(new FileNameExtensionFilter("Serialized Session", "ser"));
			int chooserStatus = -1;
			chooserStatus = chooser.showSaveDialog(null);
			if (chooserStatus == JFileChooser.APPROVE_OPTION) {
				status.setText("Saving, please wait...");
				setAllButtons(false);
				repaint();
				try {
					FileOutputStream fileOut = new FileOutputStream(chooser.getSelectedFile());
					ObjectOutputStream out = new ObjectOutputStream(fileOut);
					out.writeObject(app);
					out.close();
					fileOut.close();
				} catch(IOException i) {
					JOptionPane.showMessageDialog(null, "Error: Unable to save serialized virus collection");
				}
				status.setText("Saving!");
				setAllButtons(true);
			}
			repaint();
		}
	}
	
	
}
