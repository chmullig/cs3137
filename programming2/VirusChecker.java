import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.logging.*;

import org.apache.commons.cli.*;


/**
 * @author Chris Mulligan <clm2186>
 * 
 * Command Line Virus Checker program!
 * 
 * Uses the Apache Commons CLI library to parse command line arguments.
 * 
 * It takes either a -ser <serialized.ser> saved serialization file, or both a
 * virus directory and a benign directory to build a training data set.
 * 
 * It tests it on the -t test directory.
 * 
 * This does very little work, it's almost all handled by
 * {@link VirusCollection}. This just loads files in the directories and calls
 * the virus collection.
 *
 */
public class VirusChecker {

	public static void main(String[] args) {
	    // create the parser
	    CommandLineParser parser = new GnuParser();
	    Options options = new Options();
	    HelpFormatter formatter = new HelpFormatter();
	    Option n = OptionBuilder.withArgName("n")
	    						.hasArg()
	    						.withDescription("n for n-grams")
	    						.withType(Integer.class)
	    						.create("n");
	    options.addOption(n);
	    options.addOption(OptionBuilder.withArgName("alpha")
				.hasArg()
				.withDescription("alpha smoothing value")
				.withType(Double.class)
				.create("alpha"));
	    options.addOption(OptionBuilder.withArgName("beta")
				.hasArg()
				.withDescription("beta smoothing value")
				.withType(Double.class)
				.create("beta"));
	    
	    Option virusDir = OptionBuilder.withArgName("v")
				.hasArgs()
				.withLongOpt("virusDir")
				.withDescription("directory of viruses")
				.create("v");
	    options.addOption(virusDir);
	    Option benignDir = OptionBuilder.withArgName("b")
				.hasArgs()
				.withLongOpt("benignDir")
				.withDescription("directory of benign files")
				.create("b");
	    options.addOption(benignDir);

	    Option ser = OptionBuilder.withArgName("s")
				.hasArgs()
				.withLongOpt("ser")
				.withDescription("serialized, saved file to load")
				.create("s");
	    options.addOption(ser);
	    
	    Option save = OptionBuilder.withArgName("save")
				.hasArg()
				.withDescription("save serialized data to file")
				.create("save");
	    options.addOption(save);
	    
	    Option testDir = OptionBuilder.withArgName("t")
				.hasArg()
				.withLongOpt("testDir")
				.withDescription("directory of test files")
				.create("t");
	    options.addOption(testDir);
	    
	    options.addOption(OptionBuilder
				.withLongOpt("probe")
				.withDescription("Try to probe values of alpha and beta")
				.create("t"));

	    
	    CommandLine line = null;
	    
	    try {
	        // parse the command line arguments
	    	line = parser.parse( options, args );    
	    } catch (Exception exp) {
	        // oops, something went wrong
	        System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
	    }
	    
	    
	    VirusCollection myVC = null;
	    
	    if (line.hasOption("s")) {
			try {
				System.out.println("Loading serialized virus collection...");
				FileInputStream fileIn = new FileInputStream(line.getOptionValue("s"));
				ObjectInputStream in = new ObjectInputStream(fileIn);
				myVC = (VirusCollection) in.readObject();
				in.close();
				fileIn.close();
			} catch(IOException i) {
				System.out.println("Unable to load serialized virus collection");
				System.exit(1);
			}catch(ClassNotFoundException c) {
				System.out.println("VirusCollection class not found");
				System.exit(1);
			}
	    } else if (line.hasOption("v") && line.hasOption("b")) {
    		System.out.println("Loading from directories " + line.getOptionValue("v") + ", " + line.getOptionValue("b") + "...");
			myVC = new VirusCollection();
			for (String virusName: line.getOptionValues("v")) {
				File virusFile = new File(virusName);
				myVC.loadDirectory(virusFile, true);
			}
			for (String benignName: line.getOptionValues("b")) {
				File benignFile = new File(benignName);
				myVC.loadDirectory(benignFile, false);
	    }
		} else {
			formatter.printHelp(args[0], options, true);
			System.exit(1);
		}
	    
	    if (line.hasOption("n")) {
	    	myVC.setBeta(Integer.valueOf(line.getOptionValue("n")));
	    }
	    if (line.hasOption("alpha")) {
	    	myVC.setAlpha(Double.valueOf(line.getOptionValue("alpha")));
	    }
	    if (line.hasOption("beta")) {
	    	myVC.setBeta(Double.valueOf(line.getOptionValue("beta")));
	    }
	    
	    
	    if (line.hasOption("probe")) {
	    	// We want to probe values of alpha and beta!
	    	// The probe values have been checked over time to get something sensible
	    	
	    	//Do some precomputing to make iterating fast.
	    	ArrayList<TestFile> testFiles = new ArrayList<TestFile>();
	    	
	    	for (String testFolderName: line.getOptionValues("v")) {
				for (File file: new File(testFolderName).listFiles()) {
					try {
						FileReader fileReader = new FileReader(file);
						BufferedReader fileBuffer = new BufferedReader(fileReader);
						StringBuffer contents = new StringBuffer();
						contents.append(fileBuffer.readLine().replaceAll(" ", ""));
						
						TestFile tf = new TestFile(file.toString(), "virus", contents.toString());
						testFiles.add(tf);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
	    	}
	    	for (String testFolderName: line.getOptionValues("b")) {
				for (File file: new File(testFolderName).listFiles()) {
					try {
						FileReader fileReader = new FileReader(file);
						BufferedReader fileBuffer = new BufferedReader(fileReader);
						StringBuffer contents = new StringBuffer();
						contents.append(fileBuffer.readLine().replaceAll(" ", ""));
						
						testFiles.add(new TestFile(file.toString(), "benign", contents.toString()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
	    	}
	    	
	    	
	    	System.out.println("n,beta,alpha,type,name,prediction");
	    	for (double beta = 100; beta < 200.1; beta+=10) {
	    		myVC.setBeta(beta);
	    		for (double alpha = 1; alpha < 2; alpha++) {
	    			myVC.setAlpha(alpha);
	    	    	for (TestFile testFile: testFiles) {
	    	    		double prediction = myVC.computeNB(testFile.body);
	    	    		System.out.println(myVC.getN()+","+beta+","+alpha+","+testFile.type+","+testFile.name+","+prediction);
	    	    	}	    					
	    		}
	    	}
	    }
	    
	    
	    if (line.hasOption("t")) {   	
	    	for (String testFolderName: line.getOptionValues("t")) {
				for (File file: new File(testFolderName).listFiles()) {
					try {
						FileReader fileReader = new FileReader(file);
						BufferedReader fileBuffer = new BufferedReader(fileReader);
						StringBuffer contents = new StringBuffer();
						contents.append(fileBuffer.readLine().replaceAll(" ", ""));
						
						double prediction = myVC.computeNB(contents.toString());
						System.out.println("File " + file.getName() + " prediction: " + prediction*100);
						
						fileBuffer.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
	    	}
	    }
	    
	    
	    if (line.hasOption("save")) {
	    	try {
	    		System.out.println("Saving serialized data");
				FileOutputStream fileOut = new FileOutputStream(line.getOptionValue("save"));
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(myVC);
				out.close();
				fileOut.close();
				System.out.println("Serialized data is saved");
			} catch(IOException i) {
				i.printStackTrace();
			}
	    }
	}

	
}


