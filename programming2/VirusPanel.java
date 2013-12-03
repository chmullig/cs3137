import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.NumericShaper;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class VirusPanel extends JPanel {
	private VirusCollection myVC;
	JLabel headerStatus;
	
	JScrollPane outputPane;
	JTextArea nArea;
	JButton changeN;
	JTextArea alphaArea;
	JButton changeAlpha;
	JTextArea betaArea;
	JButton changeBeta;
	JButton loadSer;
	JButton loadVirus;
	JButton loadBenign;
	JButton saveSer;
	JButton reset;
	JButton test;
	JButton save;
	JLabel status;
	
	/**
	 * Ask for a file and initialize the list  
	 */
	public VirusPanel() {
		myVC = new VirusCollection();
		
		setLayout(new BorderLayout());
		
		JPanel north = new JPanel();
		north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
		JLabel masthead = new JLabel("Chris's Virus Scanner");
		masthead.setFont(new Font("Dialog", Font.PLAIN, 24));
		masthead.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		north.add(masthead);
		headerStatus = new JLabel(" ");
		headerStatus.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		north.add(headerStatus);
		north.setBorder(new EmptyBorder(5, 5, 15, 5));
		add(north, BorderLayout.NORTH);
		
		
		//Add the settings to the Left
		JPanel settingsPanel = new JPanel();
		settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
		add(settingsPanel, BorderLayout.EAST);
		JLabel settingsLabel = new JLabel("Settings");
		settingsPanel.add(settingsLabel);
		
		JPanel nPanel = new JPanel();
		nPanel.add(new JLabel("n = "));
		nArea = new JTextArea("" + myVC.getN(), 1, 4);
		nPanel.add(nArea);
		changeN = new JButton("change");
		changeN.addActionListener(new SetNListener());
		changeN.setMnemonic('n');
		changeN.setToolTipText("Adjust N - MUST BE DONE BEFORE LOADING FILES");
		nPanel.add(changeN);
		settingsPanel.add(nPanel);
		
		JPanel betaPanel = new JPanel();
		betaPanel.add(new JLabel("ß = "));
		betaArea = new JTextArea("" + myVC.getN(), 1, 4);
		betaPanel.add(betaArea);
		changeBeta = new JButton("change");
		changeBeta.addActionListener(new SetHyperParamsListener());
		changeBeta.setMnemonic('e');
		changeBeta.setToolTipText("Change smoothing hyperparameter alpha");
		betaPanel.add(changeBeta);
		settingsPanel.add(betaPanel);
		
		JPanel alphaPanel = new JPanel();
		alphaPanel.add(new JLabel("α = "));
		alphaArea = new JTextArea("" + myVC.getN(), 1, 4);
		alphaPanel.add(alphaArea);
		changeAlpha = new JButton("change");
		changeAlpha.addActionListener(new SetHyperParamsListener());
		changeAlpha.setMnemonic('a');
		changeAlpha.setToolTipText("Change smoothing hyperparameter alpha");
		alphaPanel.add(changeAlpha);
		settingsPanel.add(alphaPanel);
		
		settingsPanel.add(Box.createRigidArea(new Dimension(0,5)));
		
		loadSer = new JButton("Load Serialized Settings");
		loadSer.addActionListener(new LoadSerListener());
		loadSer.setMnemonic('l');
		loadSer.setToolTipText("Load a previously saved serialized settings file. It may have been created with the command line version of this program. Will erase all current session data!");
		settingsPanel.add(loadSer);
		
		loadVirus = new JButton("Load Viruses");
		loadVirus.addActionListener(new LoadListener());
		loadVirus.setMnemonic('v');
		loadVirus.setToolTipText("Load additional viruses. Can select either files or directories!");
		settingsPanel.add(loadVirus);
		
		loadBenign = new JButton("Load Benign");
		loadBenign.addActionListener(new LoadListener());
		loadBenign.setMnemonic('v');
		loadBenign.setToolTipText("Load additional benign files. Can select either files or directories!");
		settingsPanel.add(loadBenign);
		
		settingsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(settingsPanel, BorderLayout.WEST);
		
		
		
		//Add the testing to the right
		JPanel testPanel = new JPanel();
		testPanel.setLayout(new BoxLayout(testPanel, BoxLayout.Y_AXIS));
		testPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		testPanel.add(new JLabel("Test"));
		
		test = new JButton("Test Files");
		test.addActionListener(new TestListener());
		test.setMnemonic('t');
		test.setToolTipText("Test n files, or a directory of files, and determine their virality");
		testPanel.add(test);
		
		save = new JButton("Save Settings");
		save.addActionListener(new SaveListener());
		save.setMnemonic('s');
		save.setToolTipText("Save current configuration.");
		testPanel.add(save);
		
		
		testPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(testPanel, BorderLayout.EAST);
		
		settingsPanel.add(Box.createRigidArea(new Dimension(0,5)));
		
		
		//bottom
		JPanel bottom = new JPanel();
		bottom.setBorder(new EmptyBorder(5, 5, 20, 5));
		status = new JLabel(" ");
		bottom.add(status);
        add(bottom, BorderLayout.SOUTH);

		
	}
	
	protected void setAllButtons(boolean enabled) {
		nArea.setEditable(enabled);
		changeN.setEnabled(enabled);
		alphaArea.setEditable(enabled);
		changeAlpha.setEnabled(enabled);
		betaArea.setEditable(enabled);
		changeBeta.setEnabled(enabled);
		loadSer.setEnabled(enabled);
		loadVirus.setEnabled(enabled);
		loadBenign.setEnabled(enabled);
		test.setEnabled(enabled);
		save.setEnabled(enabled);
		repaint();
	}
	
	/** Repaints the text if it has changed. Also disables or enables the sorting
	 * buttons as required, so you can't perform redundant searches.
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics page) {
		super.paintComponent(page);
		
		headerStatus.setText("Virus Files Loaded: " + myVC.getVirusFileNames().size()
				+ ". Benign Files Loaded: " + myVC.getBenignFileNames().size() + ".");
		nArea.setText("" + myVC.getN());
		alphaArea.setText("" + myVC.getAlpha());
		betaArea.setText("" + myVC.getBeta());
		if (myVC.alreadyLoaded()) {
			nArea.setEditable(false);
			changeN.setEnabled(false);
		}
	}
	

	
	/**
	 * @author Chris Mulligan (clm2186)
	 * 
	 * update the n if nothing is loaded!
	 */
	private class SetNListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int newN;
			try {
				newN = Integer.parseInt(nArea.getText());
			} catch (NumberFormatException exp) {
				JOptionPane.showMessageDialog(null, "Error: invalid n!");
				return;
			}
			if (newN != myVC.getN()) {
				if (myVC.alreadyLoaded()) {
					JOptionPane.showMessageDialog(null, "Error: Cannot change N after loading files!");
					repaint();
					return;
				}
				myVC.setN(newN);
				repaint();	
			}
		}
	}
	
	
	/**
	 * @author Chris Mulligan <clm2186>
	 * Update the hyperparameters. We actually just set both whenever we set either.
	 */
	private class SetHyperParamsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			double newBeta;
			double newAlpha;
			try {
				newBeta = Double.parseDouble(betaArea.getText());
				newAlpha = Double.parseDouble(alphaArea.getText());
			} catch (NumberFormatException exp) {
				JOptionPane.showMessageDialog(null, "Error: invalid alpha or beta!");
				return;
			}
			if (Math.abs(newBeta - myVC.getBeta()) > .1) {
				myVC.setBeta(newBeta);
			}
			if (Math.abs(newAlpha - myVC.getAlpha()) > .1) {
				myVC.setAlpha(newAlpha);
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
					myVC = (VirusCollection) in.readObject();
					in.close();
					fileIn.close();
					status.setText("Loaded!");
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
			if (e.getSource() == loadVirus) {
				virus = true;
			}
			
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
					if (file.isDirectory()) {
						System.out.println("Loading directory " + file.getName());
						myVC.loadDirectory(file, virus);
					} else {
						try {
							System.out.println("Loading file " + file.getName());
							myVC.loadFile(file, virus);
						} catch (FileNotFoundException e1) {
							System.out.println("error!");
						}
					}
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
							
							double prediction = myVC.computeNB(contents.toString());
							JOptionPane.showMessageDialog(null, "File " + currFile.getName() + " prediction: " + prediction*100);
							
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
					out.writeObject(myVC);
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
