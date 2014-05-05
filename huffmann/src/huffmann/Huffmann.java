package huffmann;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Huffmann {
	/** A map where the key is a character contained in the
	 *  file that is encoded using the Huffmann encoding */
	private Map<Integer, Integer> mHuffmannMapping = new HashMap<>();
	
	/** This method is responsible for the encoding
	 *  @param pathOfFileToEncode This is the file path that should be encoded.
	 *  @param pathOfFileResult This is the file path of the resulting file. 
	 *  @throws IOException */
	public void encoding (String pathOfFileToEncode, String pathOfFileResult) throws IOException {
		FileInputStream fileInput = new FileInputStream(pathOfFileToEncode);
		
		Integer character = 0;
		while((character = fileInput.read()) != -1) {
			// if the character is not yet there add it with the initial 
			// occurrence of 0 - otherwise just increment it by one
			int countCharacter = mHuffmannMapping.containsKey(character) ? 
								 mHuffmannMapping.get(character) : 0;
			mHuffmannMapping.put(character, countCharacter + 1);
		}
		
		fileInput.close();
		
		System.out.println(mHuffmannMapping);
	}
}
