import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.logging.*;




public class VirusCollection implements Serializable {
	private static final long serialVersionUID = 2L;
	private static Logger LOGGER = Logger.getLogger("VirusCollection");
	private HashTable<String, Integer> viruses;
	private HashTable<String, Integer> benign;
	private HashTable<String, Double> thetas;
	private HashTable<String, Double> oneMinusThetas;
	private int numViruses = 0;
	private int numBenign = 0;
	private int n = 8;
	private double alpha = 5;
	private double beta = 20;
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
	
	private void loadCache() {
		LOGGER.log(Level.FINE, "Loading cache");
		Set<String> allKeys = viruses.keySet();
		allKeys.addAll(benign.keySet());
		thetas = new HashTable<String, Double>((int) (allKeys.size()*1.51));
		oneMinusThetas = new HashTable<String, Double>((int) (allKeys.size()*1.51));
		for (String key: allKeys) {
			Double vCount;
			Integer rawVcount = viruses.get(key);
			if (rawVcount == null)
				vCount = 0.0;
			else
				vCount = rawVcount.doubleValue();
			
			Double bCount;
			Integer rawBcount = viruses.get(key);
			if (rawBcount == null)
				bCount = 0.0;
			else
				bCount = rawBcount.doubleValue();

			double theta = Math.log(vCount + alpha)/(vCount + bCount + beta);
			double oneMinus = Math.log(1 - ((vCount + alpha)/(vCount + bCount + beta)));
			thetas.put(key, theta);
			oneMinusThetas.put(key, oneMinus);
		}
		cached = true;
		LOGGER.log(Level.FINE, "Cache loaded");
	}
	
	public double predict(File file) {
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader fileBuffer = new BufferedReader(fileReader);
			StringBuffer contents = new StringBuffer();
			contents.append(fileBuffer.readLine().replaceAll(" ", ""));
			fileBuffer.close();
			return computeNB(contents.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * Takes an already cleaned up hex file. 
	 * 
	 * @param newFile
	 * @return
	 */
	public double computeNB(String newFile) {
		if (!cached) {
			loadCache();
		}
		double missing = Math.log(4.0 / 10.0);
		double oneminusmissing = Math.log(1 - (4.0 / 10.0));
		double aeta = 0.0;
		for (int i = 0; i < newFile.length() - n; i++) {
			String ngram = newFile.substring(i, i+n);
			Double theta = thetas.get(ngram);
			Double oneminus;
			if (theta == null) {
				theta = missing;
				oneminus =  oneminusmissing;
			} else {
				oneminus = oneMinusThetas.get(ngram);
			}
			aeta = aeta - oneminus + theta;
		} 
		
		return 1/(1+Math.exp(aeta));
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		cached = false;
		this.alpha = alpha;
	}

	public double getBeta() {		
		return beta;
	}

	public void setBeta(double beta) {
		cached = false;
		this.beta = beta;
	}

	public Double[] testDirectory() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
