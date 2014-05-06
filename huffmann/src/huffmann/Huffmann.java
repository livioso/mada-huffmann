package huffmann;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Huffmann {
	
	/** The used TreeMap (an implementation of a SortedMap) does 
	 *  the sorting for us; however we need to sort by value and
	 *  not by key (because we want to sort by the frequency (value)
	 *  of a character (key) => thus we have our own fancy comparator. */
	class CharacterValueComparator implements Comparator<Integer> {
	    Map<Integer, Integer> mapUnderComparison;
	    
	    public CharacterValueComparator(Map<Integer, Integer> mapUnderComparison) {
	    	// this is definitely a bit ugly as we have now 
	    	// a dependency to the map and vice versa. :(
	    	this.mapUnderComparison = mapUnderComparison;
	    }
	    
	    /** We need a ascending order so do that => least used characters 
	     *  in the beginning of the map and most used at the end as we build 
	     *  the map from the ground to the top ('bottom up') */ 
	    public int compare(Integer lhsCharacter, Integer rhsCharacter) {
	        return mapUnderComparison.get(lhsCharacter) >= 
	        	   mapUnderComparison.get(rhsCharacter) ? 1 : -1;
	    }
	}
	
	/** This map */ 
	private Map<Integer, Integer> characterFrequencMapUnsorted = new HashMap<>();
	private CharacterValueComparator comp = new CharacterValueComparator(characterFrequencMapUnsorted);
	private SortedMap<Integer, Integer> characterFrequencMapSorted = new TreeMap<>(comp);
	
	ArrayList<HuffmannNode> SortedList = new ArrayList<>();
	
	
	
	/** Map that holds the Huffmann encoding for each character */
	private Map<String, String> characterHuffmannTree = new HashMap<>();
	
	/** */
	public void initializeUnsortedFrequencyMap (String pathOfFileToEncode) throws IOException {
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
		
		while(SortedList.size() > 1) {
			// this works because the map is sorted so the two first 
			// entries are also the ones with the least occurrence :-)
			HuffmannNode leastOccuringCharacter = SortedList.get(0);
			HuffmannNode secondLeastOccuringCharacter = SortedList.get(1);
			
			leastOccuringCharacter.HuffmannEncoding = "0" + leastOccuringCharacter.HuffmannEncoding;
			secondLeastOccuringCharacter.HuffmannEncoding = "1" + secondLeastOccuringCharacter.HuffmannEncoding;
			
			for(HuffmannNode each : leastOccuringCharacter.children) {
				// don't add if it's the root node => obviously ;)
				if(SortedList.size() != 2) {
					each.HuffmannEncoding = "0" + each.HuffmannEncoding;
				}
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
		
		System.out.print(SortedList.get(0).children);
		
	}
	
	public class HuffmannNode implements Comparable<HuffmannNode> {
		Integer EncodedCharacter = -1;
		String HuffmannEncoding = ""; 
		
		Integer Weight;
		List<HuffmannNode> children = new ArrayList<>();
		
		@Override
		public int compareTo(HuffmannNode o) {
			return this.Weight.compareTo(o.Weight);
		}
		
		@Override
		public String toString () {
			return "(" + Character.toString((char) EncodedCharacter.intValue()) + 
					", " + Weight + ", " + HuffmannEncoding + ") ";
		}
	}
}
