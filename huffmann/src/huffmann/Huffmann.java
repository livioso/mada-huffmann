package huffmann;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Huffmann {
	/** Map that holds the frequency for each character  */
	private Map<Integer, Integer> characterFrequencyMap = new HashMap<>();
	
	/** */
	public void createFrequencyMap (String pathOfFileToEncode) throws IOException {
		FileInputStream fileInput = new FileInputStream(pathOfFileToEncode);
		
		Integer character = 0;
		while((character = fileInput.read()) != -1) {
			// if the character is not yet there add it with the initial 
			// occurrence of 0 - otherwise just increment it by one
			int countCharacter = characterFrequencyMap.containsKey(character) ? 
								 characterFrequencyMap.get(character) : 0;
			characterFrequencyMap.put(character, countCharacter + 1);
		}
		
		fileInput.close();
	}
	
	public void sortFrequenceMap () {
		
		List<Map.Entry<Integer, Integer>> characterFrequencyMapAsList = 
			new ArrayList<Map.Entry<Integer, Integer>>(characterFrequencyMap.entrySet());

		// sort the map according the values
		Collections.sort(characterFrequencyMapAsList,
		new Comparator<Object>() {
	    	@SuppressWarnings("unchecked")
			public int compare(Object o1, Object o2) {
	    		Map.Entry<Integer, Integer> e1 = (Map.Entry<Integer, Integer>) o1;
	    		Map.Entry<Integer, Integer> e2 = (Map.Entry<Integer, Integer>) o2;
	            return ((Comparable<Integer>) e1.getValue()).compareTo(e2.getValue());
	         }
		});
		
		// now empty the unsorted map
		characterFrequencyMap.clear();
		
		// ... and fill it with the sorted list
		for (Map.Entry<Integer, Integer> each : characterFrequencyMapAsList) {
			characterFrequencyMap.put(each.getKey(), each.getValue());
		}
	}

	public void createHuffmanEncoding() {
		// get possible root
		characterFrequencyMap.get(0);
	}
}
