import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.logging.*;


/**
 * @author Chris Mulligan <clm2186@columbia.edu>
 *
 * This is the core of my lovely virus checker. It creates n-grams, where each
 * is a hex character from a hexdump of a virus or benign file.
 * 
 * It uses {@link HashTable} extensively for storing the ngrams, and for caching
 * probabilities to save time. It is not especially memory efficient.
 * 
 * My Na•ve Bayes approach is based on three resources:
 * Paul Graham's spam essay at http://www.paulgraham.com/spam.html, 
 * Wikipedia's articles on NB and Spam Filtering http://en.wikipedia.org/wiki/Bayesian_spam_filtering
 * Schutt & O'Neil, Doing Data Science, 2013. 
 * 
 * I calculate theta = P(Virus|Byte) = [(times in Virus) + alpha] / [(times in Virus) + (times in Benign) + beta].
 * alpha and beta determined experimentally to be 1 and 100.
 * I then store log(theta) and log(1-theta) in hash tables for later use. To 
 * compute P(virus|bytes) I let aeta = sum((1-theta_i) - theta_i), then the
 * overall probability is 1/(1+e^aeta).
 *
 */
public class VirusCollection implements Serializable {
	private static final long serialVersionUID = 2L;
	private static Logger LOGGER = Logger.getLogger("VirusCollection");
	private HashTable<String, Integer> viruses;
	private HashTable<String, Integer> benign;
	private HashTable<String, Double> thetas;
	private HashTable<String, Double> oneMinusThetas;
	private ArrayList<String> virusFileNames;
	private ArrayList<String> benignFileNames;
	private int n = 8;
	private double alpha = 1;
	private double beta = 200;
	private boolean cached = false;
	
	/**
	 * Standard Constructor
	 */
	public VirusCollection() {
		viruses = new HashTable<String, Integer>();
		benign = new HashTable<String, Integer>();
		
		virusFileNames = new ArrayList<String>();
		benignFileNames = new ArrayList<String>();
		
		LOGGER.setLevel(Level.ALL);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		LOGGER.addHandler(handler);
	}
	
	
	/**
	 * Load a virus, and store the file name for later use
	 * 
	 * @param source
	 * @return
	 * @throws FileNotFoundException
	 */
	public int loadVirus(File source) throws FileNotFoundException {
		cached = false;
		FileReader sourceRdr = new FileReader(source);
		virusFileNames.add(source.getName());
		return load(sourceRdr, viruses);
	}
	
	/**
	 * Anonymously load a virus from a FileReader.
	 * 
	 * @param source
	 * @return
	 */
	public int loadVirus(FileReader source) {
		cached = false;
		return load(source, viruses);
	}
	
	public int loadFile(File source, boolean virus) throws FileNotFoundException {
		cached= false;
		FileReader sourceRdr = new FileReader(source);
		if (virus) {
			virusFileNames.add(source.getName());
			return load(sourceRdr, viruses);
		} else {
			benignFileNames.add(source.getName());
			return load(sourceRdr, benign);
		}
	}
	
	/**
	 * Load a benign file, and store the file name for later use.
	 * 
	 * @param source
	 * @return
	 * @throws FileNotFoundException
	 */
	public int loadBenign(File source) throws FileNotFoundException {
		cached = false;
		benignFileNames.add(source.getName());
		FileReader sourceRdr = new FileReader(source);
		return load(sourceRdr, benign);
	}
	
	/**
	 * Anonymously load a benign file
	 * 
	 * @param source
	 * @return
	 */
	public int loadBenign(FileReader source) {
		cached = false;
		return load(source, benign);
	}
	
	/**
	 * Behind the scenes to load from a file reader into a hash table. Because
	 * there's no meaningful difference we only do this once, and have each
	 * call this with the appropriate arguments.
	 * 
	 * @param source
	 * @param destination
	 * @return
	 */
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
	
	/**
	 * Load an entire directory of viruses or benign files!
	 * 
	 * @param directory
	 * @param virus
	 * @return
	 */
	public int loadDirectory(File directory, boolean virus) {
		if (!directory.isDirectory() || !directory.canExecute() || !directory.canRead()) {
			return -1;
		}
		
		int fileCount = 0;
		File[] files = directory.listFiles();
		for (File file: files) {
			try {
				loadFile(file, virus);
				fileCount ++;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return fileCount;
	}
	
	/**
	 * Prep the cache by computing all the word probabilities and storing them
	 * in two hash tables (for theta and 1-theta).
	 * 
	 * This is called whenever we try to computed a probability and the cache
	 * is invalid. Cache is invalidated if 
	 */
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
	
	/**
	 * Takes a file object (instead of a precomputed string), cleans it up,
	 * then calls computeNB on the cleaned up string. Merely a convenience. 
	 * 
	 * @param file
	 * @return
	 * @throws IOException If there is a problem with the file
	 */
	public double predict(File file) throws IOException {
		FileReader fileReader = new FileReader(file);
		BufferedReader fileBuffer = new BufferedReader(fileReader);
		StringBuffer contents = new StringBuffer();
		contents.append(fileBuffer.readLine().replaceAll(" ", ""));
		fileBuffer.close();
		return computeNB(contents.toString());
	}
	
	/**
	 * Takes a string representing an already cleaned up hex file, and compute
	 * the naive bayes score. 
	 * 
	 * @param newFile
	 * @return
	 */
	public double computeNB(String newFile) {
		if (!cached) {
			loadCache();
		}
		double missing = Math.log(alpha / beta);
		double oneminusmissing = Math.log(1 - (alpha / beta));
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
			aeta = aeta + oneminus - theta;
		} 
		
		return 1/(1+Math.exp(aeta));
	}

	public boolean alreadyLoaded() {
		if (viruses.size() > 0 || benign.size() > 0)
			return true;
		return false;
	}
	
	/**
	 * Set the n for the n-grams. NOTE, this may not be changed after loading
	 * files, you MUST create a new instance of this class and setN immediately
	 * before loading any files.
	 * 
	 * @param n
	 */
	public void setN(int n) {
		if (alreadyLoaded())
			throw new IllegalStateException();
		this.n = n;
	}

	//getters/setters below
	public int getN() {
		return n;
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

	public ArrayList<String> getVirusFileNames() {
		return virusFileNames;
	}

	public ArrayList<String> getBenignFileNames() {
		return benignFileNames;
	}
	
}
