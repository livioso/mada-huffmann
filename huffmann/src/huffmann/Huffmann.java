package huffmann;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Huffmann {
	
	public class HuffmannNode implements Comparable<HuffmannNode> {
		Integer EncodedCharacter = -1;
		String HuffmannEncoding = ""; 
		
		Integer Weight;
		List<HuffmannNode> children = new ArrayList<>();
		
		public Character getEncodedCharacterAsChar() {
			return (char) EncodedCharacter.intValue();
		}
		
		@Override
		public int compareTo(HuffmannNode o) {
			return this.Weight.compareTo(o.Weight);
		}
		
		@Override
		public String toString () {
			return "(" + getEncodedCharacterAsChar() + 
				   ", " + Weight + ", " + HuffmannEncoding + ") ";
		}
	}
	
	private String inputFile = "";
	
	private Map<Integer, Integer> characterFrequencMapUnsorted = new HashMap<>();
	private ArrayList<HuffmannNode> SortedList = new ArrayList<>();
	/** Map that holds the Huffmann encoding for each character */
	private Map<Character, String> characterHuffmannTree = new HashMap<>();
	
	/** */
	public void initializeUnsortedFrequencyMap (String pathOfFileToEncode) throws IOException {
		inputFile = pathOfFileToEncode;
		
		FileInputStream fileInput = new FileInputStream(pathOfFileToEncode);
		
		Integer character = 0;
		while((character = fileInput.read()) != -1) {
			int countCharacter = characterFrequencMapUnsorted.containsKey(character) ? 
								 characterFrequencMapUnsorted.get(character) : 0;
			characterFrequencMapUnsorted.put(character, countCharacter + 1);
		}
		
		fileInput.close();
	}
	
	/** This method creates a new sorted by value map based on characterFrequencMapUnsorted
	 * 	@precondition 	In order to make this work initializeUnsortedFrequencyMap 
	 * 					should have been called before so there are values to be sorted */
	public void initializeSortedFrequencyMap () {
		
		for(Map.Entry<Integer, Integer> each : characterFrequencMapUnsorted.entrySet()) {
			HuffmannNode node = new HuffmannNode();
			node.EncodedCharacter = each.getKey();
			node.Weight = each.getValue();
			SortedList.add(node);
		}
		
		Collections.sort(SortedList);
	}

	public void initializeHuffmannTree() {
		createHuffmannTree();
		cleanUpHuffmannTree();
		writeHuffmannTreeToFile("dec_tab.txt");
	}

	private void writeHuffmannTreeToFile(String path) {
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(path, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {

		}
		
		String completeEncoding = "";
		for(Map.Entry<Character, String> each : characterHuffmannTree.entrySet()) {
			completeEncoding = completeEncoding + each.getKey() + ":" + each.getValue() + "-";
		}
		
		// remove trailing '-'
		completeEncoding = completeEncoding.substring(0, completeEncoding.length() - 1);
		
		// write the encoding to the file
		writer.write(completeEncoding);
		writer.close();
	}

	private void createHuffmannTree() {
		while(SortedList.size() > 1) {
			// this works because the map is sorted so the two first 
			// entries are also the ones with the least occurrence :-)
			HuffmannNode leastOccuringCharacter = SortedList.get(0);
			HuffmannNode secondLeastOccuringCharacter = SortedList.get(1);
			
			leastOccuringCharacter.HuffmannEncoding = "0" + leastOccuringCharacter.HuffmannEncoding;
			secondLeastOccuringCharacter.HuffmannEncoding = "1" + secondLeastOccuringCharacter.HuffmannEncoding;
			
			for(HuffmannNode each : leastOccuringCharacter.children) {
				each.HuffmannEncoding = "0" + each.HuffmannEncoding;
			}
			
			for(HuffmannNode each : secondLeastOccuringCharacter.children) {
				each.HuffmannEncoding = "1" + each.HuffmannEncoding;
			}
			
			HuffmannNode parent = new HuffmannNode();
			parent.children.add(leastOccuringCharacter);
			parent.children.add(secondLeastOccuringCharacter);
			
			parent.children.addAll(leastOccuringCharacter.children);
			parent.children.addAll(secondLeastOccuringCharacter.children);
			
			parent.Weight = leastOccuringCharacter.Weight + secondLeastOccuringCharacter.Weight;
			
			SortedList.add(parent);
			
			SortedList.remove(0);
			SortedList.remove(0);
		}
	}
	
	private void cleanUpHuffmannTree() {
		// there should be only the root note left
		assert(SortedList.size() == 1);
		
		HuffmannNode root = SortedList.get(0);
		
		for(HuffmannNode each : root.children) {
			if(each.EncodedCharacter != -1) {
				characterHuffmannTree.put(each.getEncodedCharacterAsChar(), each.HuffmannEncoding);
			}
		}
	}

	@SuppressWarnings("resource")
	public void encodeToFile(String outputFilePath) throws FileNotFoundException {
		
		String bitStringResulting = "";
		
		try {
			FileInputStream fileInput = new FileInputStream(inputFile);
			Integer readChar = 0;
			while((readChar = fileInput.read()) != -1) {
				String encoding = characterHuffmannTree.get((char)readChar.intValue());
				bitStringResulting += encoding;
				}
			} catch (IOException e) {
			e.printStackTrace();
		}
		
		bitStringResulting += "1";
		while(bitStringResulting.length() % 8 != 0) {
			bitStringResulting += "0";
		}
		
		byte[] bitStringAsByteArray = new byte[bitStringResulting.length() / 8];
		
		for(int i = 0; i < bitStringResulting.length() / 8; i++) {
			String temp = bitStringResulting.substring(i * 8, (i+1) * 8);
			bitStringAsByteArray[i] = (byte) Integer.parseInt(temp, 2);
		}
		
	     FileOutputStream fos = new FileOutputStream("output.dat");
	     try {
			fos.write(bitStringAsByteArray);
			fos.close();
		} catch (IOException e) {
		}
	}
}
