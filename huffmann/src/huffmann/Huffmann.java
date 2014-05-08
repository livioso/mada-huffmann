package huffmann;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
@SuppressWarnings("unused")



/** This class is used to create a 
 *  encoded a file using the Huffmann 
 *  encoding and store the resulting files
 *  and then decoded the file. */
public class Huffmann {
	
	/** This class is used to build a representation of the Huffmann tree 
	 *  in memory. Intersections as well as leafs are nodes in this 
	 *  representation of the tree: There is no distinction between 
	 *  nodes and leafs on class level. */
	public class HuffmannNode implements Comparable<HuffmannNode> {
		/** Represents the encoded character's integer value. 
		 * Should be set to -1 if it's a intersection. */
		Integer EncodedCharacter = -1;
		
		/** Huffmann encoding of the encoded character. */
		String HuffmannEncoding = ""; 
		
		/** Weight of the node: Is important to build the tree (e.g. for sorting) */
		Integer Weight;
		
		/** Holds ALL sub-nodes that are in the tree 'below'.
		 *  This is used to append a 0/1 to all those nodes when
		 *  the tree grows for a level. */
		List<HuffmannNode> children = new ArrayList<>();
		
		public int getEncodedCharacterAsInt() {
			return EncodedCharacter.intValue();
		}
		
		@Override
		public int compareTo(HuffmannNode o) {
			// we want to be able to sort by weight 
			return this.Weight.compareTo(o.Weight);
		}
		
		@Override
		public String toString () {
			return "(" + getEncodedCharacterAsInt() + 
				   ", " + Weight + ", " + HuffmannEncoding + ") ";
		}
	}
	
	/** Original input file path */
	private String inputFile = "";
	
	/** Map that holds the number of occurrences per character in the text where
	 *  the key is the integer representation and the value the amount of occurrences.*/
	private Map<Integer, Integer> characterFrequencMapUnsorted = new HashMap<>();
	
	/** List holding all nodes of the tree sorted by weight => Used to build the tree */ 
	private ArrayList<HuffmannNode> SortedList = new ArrayList<>();
	
	/** Resulting map that holds the Huffmann encoding for each character */
	private Map<Integer, String> characterHuffmannTree = new HashMap<>();
	
	/** Resulting bit string representation in Huffmann encoding. */
	private String bitStringResulting = "";
	
	
	/** Puts the amount of occurrences per character in the characterFrequencMapUnsorted map*/
	public void initializeUnsortedFrequencyMap (String pathOfFileToEncode) throws IOException {
		inputFile = pathOfFileToEncode;
		FileInputStream fileInput = new FileInputStream(pathOfFileToEncode);
		
		Integer character = 0;
		while((character = fileInput.read()) != -1) {
			// add new character or increment if already in the map
			int countCharacter = characterFrequencMapUnsorted.containsKey(character) ? 
								 characterFrequencMapUnsorted.get(character) : 0;
			characterFrequencMapUnsorted.put(character, countCharacter + 1);
		}
		
		System.out.println("Characters with frequency (as Integers):");
		System.out.println(characterFrequencMapUnsorted);
		
		fileInput.close();
	}
	
	/** This method creates a new sorted by value map based on characterFrequencMapUnsorted
	 * 	@precondition 	In order to make this work initializeUnsortedFrequencyMap 
	 * 					should have been called before so there are values to be sorted */
	public void initializeSortedFrequencyMap () {
		
		// just add each character to the collection as new node
		for(Map.Entry<Integer, Integer> each : characterFrequencMapUnsorted.entrySet()) {
			HuffmannNode node = new HuffmannNode();
			node.EncodedCharacter = each.getKey();
			node.Weight = each.getValue();
			SortedList.add(node);
		}
		
		// this is important: sort it so we have 
		// the node with the lowest weight at the 
		// beginning of the collection.
		Collections.sort(SortedList);
	}

	/** Creates, cleans up (removes nodes created 
	 *  during creation of the initial tree and 
	 *  writes the resulting mapping X:Encoding 
	 *  to the dec_tab.txt file. */
	public void initializeHuffmannTree() {
		createHuffmannTree();
		cleanUpHuffmannTree();
		writeHuffmannTreeToFile("dec_tab.txt");
	}

	/** Creates the huffmann tree itself.
	 *  => This is where all the magic happens. */
	private void createHuffmannTree() {
		
		while(SortedList.size() > 1) {
			// this works because the collection is sorted so the two first 
			// entries are also the ones with the least occurrence :-)
			// => we want them because they are the next ones to be processed. 
			HuffmannNode leastOccuringCharacter = SortedList.get(0);
			HuffmannNode secondLeastOccuringCharacter = SortedList.get(1);
			
			// prepend a 0 to this node (the one on the left)
			leastOccuringCharacter.HuffmannEncoding = "0" + 
					leastOccuringCharacter.HuffmannEncoding;
			// ... and a 1 to the other one (the one to the right)
			secondLeastOccuringCharacter.HuffmannEncoding = "1" + 
					secondLeastOccuringCharacter.HuffmannEncoding;
			
			// now all nodes below this particular node need to be changed
			// => prepend a 0 to the left side of the tree
			for(HuffmannNode each : leastOccuringCharacter.children) {
				each.HuffmannEncoding = "0" + each.HuffmannEncoding;
			}
			
			// // => prepend a 1 to the right side of the tree
			for(HuffmannNode each : secondLeastOccuringCharacter.children) {
				each.HuffmannEncoding = "1" + each.HuffmannEncoding;
			}
			
			// now create a new node that represents 
			// the parent of those two nodes (with the 
			// least frequency. 
			HuffmannNode parent = new HuffmannNode();
			parent.children.add(leastOccuringCharacter);
			parent.children.add(secondLeastOccuringCharacter);
			
			// also add all previous children so we will still 
			// know what nodes are children (and children' children, 
			// and so on) => keep track of that.
			parent.children.addAll(leastOccuringCharacter.children);
			parent.children.addAll(secondLeastOccuringCharacter.children);
			
			// this is crucial: the weight of this new parent node must be the sum!
			parent.Weight = leastOccuringCharacter.Weight + secondLeastOccuringCharacter.Weight;
			
			SortedList.add(parent);
			
			// get rid of the nodes 
			// with the least frequency 
			// => we added the new one. :)
			SortedList.remove(0);
			SortedList.remove(0);
		}
	}
	
	/** This method removes nodes that we have in the sorted 
	 *  list that don't represent a real character but an intersection.
	 *  Those intersections are easy to recognize due to their encoded 
	 *  character being non-sense. */
	private void cleanUpHuffmannTree() {
		
		// there should be only the root note left
		// => precondition createHuffmannTree should be called before
		assert(SortedList.size() == 1);
		
		HuffmannNode root = SortedList.get(0);
		System.out.println("\nHuffmann tree:");
		
		for(HuffmannNode each : root.children) {
			if(each.EncodedCharacter != -1) {
				// add only real characters but no intersections
				characterHuffmannTree.put(each.getEncodedCharacterAsInt(), each.HuffmannEncoding);
				System.out.println(each);
			}
		}
		
		
	}
	
	/** Write the resulting huffmann encoding to a file 
	 *  using the format character1:encoding1-char2:enc2 ... */
	private void writeHuffmannTreeToFile(String path) {
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(path, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {

		}
		
		// just dumb that map like K:V- and so on.
		String completeEncoding = "";
		for(Entry<Integer, String> each : characterHuffmannTree.entrySet()) {
			completeEncoding = completeEncoding + each.getKey() + ":" + each.getValue() + "-";
		}
		
		// remove trailing '-'
		completeEncoding = completeEncoding.substring(0, completeEncoding.length() - 1);
		
		// write the encoding to the file
		writer.write(completeEncoding);
		writer.close();
	}

	/** Encodes the file specified as input file and put it out to the location specified. */ 
	@SuppressWarnings("resource")
	public void encodeToFile(String outputFilePath) throws FileNotFoundException {
		
		try {
			FileInputStream fileInput = new FileInputStream(inputFile);
			Integer readChar = 0;
			while((readChar = fileInput.read()) != -1) {
				// replace each char in the input file with the corresponding 
				// huffmann encoding (map lookup)
				String encoding = characterHuffmannTree.get(readChar.intValue());
				bitStringResulting += encoding;
				}
			} catch (IOException e) {
			e.printStackTrace();
		}
		
		// in order to get a full byte 
		// we need to add trailing zeros
		bitStringResulting += "1";
		while(bitStringResulting.length() % 8 != 0) {
			bitStringResulting += "0";
		}
		
		// now we can dumb the resulting string to a byte array
		byte[] bitStringAsByteArray = new byte[bitStringResulting.length() / 8];
		
		for(int i = 0; i < bitStringResulting.length() / 8; i++) {
			// get 8 bits at the time => 1 byte and convert it
			String temp = bitStringResulting.substring(i * 8, (i+1) * 8);
			bitStringAsByteArray[i] = (byte) Integer.parseInt(temp, 2);
		}
	     FileOutputStream fos = new FileOutputStream("output.dat");
	     try {
			fos.write(bitStringAsByteArray);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    
	}
	
	/** Decodes a file given in the encoded and the used huffmann encoding */ 
	public void decodeFile(String encodedFile, String keyFile) throws IOException {
		
		// reconstruct the map from the given huffmann encoding 
		Map<Integer, String> encodingMap = createEncodingMap(keyFile);
		
		// reconstruct the bit string form the given encoded file
	    String encodedText = createBitString(encodedFile);
	    
	    // we added trailing zeros after the last 1 (the 1 we also added ourselves)
	    encodedText = encodedText.substring(0, encodedText.lastIndexOf('1'));
	    	    
	    String decodedText = "";
	    String tempSearchQuery = "";
	    int treeCounter = 1;

	    while(encodedText.length() > 1) {
	    	for (Entry<Integer, String> each: encodingMap.entrySet()) {
	    		tempSearchQuery = encodedText.substring(0, treeCounter);
	    		
	    		// try to find the particular encoded value in the
	    		// map => repeat as long as we don't find it. 
	    		// if we find it: at the represented character and
	    		// remove the just decoded char form the bit string.
		    	if (each.getValue().equals(tempSearchQuery)) {
		    		encodedText = encodedText.substring(tempSearchQuery.length(), encodedText.length());
		    		decodedText += Character.toChars(each.getKey())[0];
		    		treeCounter = 0;
				}
		    }
			treeCounter++;
	    }
	    
	    PrintWriter writer = new PrintWriter("decompress.txt", "UTF-8");
	    writer.println(decodedText);
	    writer.close();
	    
	}
	
	/** Helper method that constructs the map represented in a text file. e.g. the huffmann encoding */
	private Map<Integer, String> createEncodingMap(String keyFile) throws IOException {
		
	    BufferedReader fisKey = new BufferedReader(new FileReader(keyFile));
	    String decodeKey = "";
	    
	    try {
	    	StringBuilder sb = new StringBuilder();
	        String line = fisKey.readLine();
	        
	        while (line != null) {
	            sb.append(line);
	            sb.append('\n');
	            line = fisKey.readLine();
	        }
	        decodeKey = sb.toString();
	        decodeKey = decodeKey.substring(0, decodeKey.lastIndexOf('\n'));
	        
	        fisKey.close();
	        
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    // the whole file content is now in the decodeKey
	    // now we can just split by - to get all the tuples.
	    Map<Integer, String> encodingMap = new TreeMap<>();
	    String[] splittedDecodeKey = decodeKey.split("-");
	    
	    // we know that the formit is like char:encoding
	    // so we can simple split by : and put the resulting
	    // result to the map.
	    for (int i = 0; i < splittedDecodeKey.length; i++) {
	    	String[] items = splittedDecodeKey[i].split(":");
	    	encodingMap.put(Integer.parseInt(items[0]), items[1]); 
		}
		
	    System.out.println("\nEncodingMap as bitstrings:");
	    System.out.println(encodingMap);
	    
		return encodingMap;
	}
	
	/** Simply reads the full encoded file entry content and return it as a string. */
	private String createBitString(String encodedFile) throws IOException {
		 File file = new File("output.dat");
		 byte[] bFile = new byte[(int)file.length()];
		 FileInputStream fis = new FileInputStream(file);
		 String encodedBitString = "";
		 
		 try {
			 fis.read(bFile);
			 fis.close();
			 StringBuffer buffer = new StringBuffer();
			 for(int i=0; i<bFile.length;i++){
				 String substring = String.format("%8s", Integer.toBinaryString(bFile[i] & 0xFF)).replace(' ', '0');
				 buffer.append(substring);
			 }
			 String newString = buffer.toString();
			 encodedBitString = newString;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	    return encodedBitString;
	}
	
	/** Small analysis function to find out how much space has been saved etc. */
	public void analyzeFileSizes(String inputFile, String decompressFile) {
		File fileInput = new File(inputFile);
		File fileDecompress = new File(decompressFile);
		double savedPercentage = (100.0 / fileInput.length()) * fileDecompress.length();
		savedPercentage = (double)Math.round(savedPercentage * 100) / 100;
		
		System.out.println("\nFilesize analysis");
		System.out.println("Length of original text: " + fileInput.length());
		System.out.println("Length of compressed text: " + fileDecompress.length());
		System.out.println("Percental difference: " + savedPercentage + "%");
	}
}

