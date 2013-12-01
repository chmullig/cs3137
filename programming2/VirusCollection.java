import java.io.*;
import java.util.*;


public class VirusCollection {
	private HashTable<String, Integer> viruses;
	private HashTable<String, Integer> benign;
	private int n;
	
	public VirusCollection() {
		viruses = new HashTable<String, Integer>();
		benign = new HashTable<String, Integer>();
	}
	
	public int loadVirus(FileReader source) {
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
		
		for (int i = 0; i < buffer.length(); i++) {
			buffer.substring(i, i+n);
		}
		
		return count;
	}
	
	
	public int getN(){
		return n;
	}
	
	public void setN(int newN) {
		n = newN;
	}
	
}
