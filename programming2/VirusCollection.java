import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.logging.*;




public class VirusCollection {
	private static Logger LOGGER = Logger.getLogger("VirusCollection");
	private HashTable<String, Integer> viruses;
	private HashTable<String, Integer> benign;
	private int numViruses = 0;
	private int numBenign = 0;
	private int n = 6;
	private boolean cached = false;
	
	public VirusCollection() {
		viruses = new HashTable<String, Integer>();
		benign = new HashTable<String, Integer>();
		
		LOGGER.setLevel(Level.ALL);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		LOGGER.addHandler(handler);
	}
	
	public int loadVirus(FileReader source) {
		numViruses++;
		cached = false;
		return load(source, viruses);
	}
	
	public int loadBenign(FileReader source) {
		numBenign++;
		cached = false;
		return load(source, benign);
	}
	
	private int load(FileReader source, HashTable<String, Integer> destination) {
		int count = 0;
		BufferedReader bufReader = new BufferedReader(source);
		StringBuffer buffer = new StringBuffer(65536);
		try {
			while (bufReader.ready()) {
					buffer.append(bufReader.readLine().replaceAll(" ", ""));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i = 0; i < buffer.length()-n; i++) {
			String ngram = buffer.substring(i, i+n);
			if (destination.containsKey(ngram)) {
				int ngramCount = destination.get(ngram) + 1;
				destination.put(ngram, ngramCount);
			} else {
				destination.put(ngram, 1);
			}
			count++;
		}
		
		return count;
	}
	
	public int loadDirectory(File directory, boolean virus) {
		if (!directory.isDirectory() || !directory.canExecute() || !directory.canRead()) {
			return -1;
		}
		
		int fileCount = 0;
		File[] files = directory.listFiles();
		for (File file: files) {
			try {
				FileReader fileReader = new FileReader(file);
				if (virus) {
					LOGGER.log(Level.FINE, "Loading virus " + file.getName());
					loadVirus(fileReader);
				} else {
					LOGGER.log(Level.FINE, "Loading benign " + file.getName());
					loadBenign(fileReader);
				}
				fileCount ++;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return fileCount;
	}
	
	/**
	 * Takes an already cleaned up hex file
	 * 
	 * @param newFile
	 * @return
	 */
	public double computeNB(String newFile) {
		
		for (int i = 0; i < newFile.length(); i++) {
			String ngram = newFile.substring(i, i+n);
			int virusCount = viruses.get(ngram);
			int benignCount = benign.get(ngram);
		}
		
		return 0.0;
		
	}
	
	
	public int getN(){
		return n;
	}
	
	public void setN(int newN) {
		n = newN;
	}
	
}
