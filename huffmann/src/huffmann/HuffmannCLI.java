package huffmann;

import java.io.IOException;

public class HuffmannCLI {
	public static void main(String[] args) {
		Huffmann huffmann = new Huffmann();
		try {
			huffmann.initializeUnsortedFrequencyMap("input.txt");
			huffmann.initializeSortedFrequencyMap();
			huffmann.initializeHuffmannTree();
			huffmann.encodeToFile("output.dat");
			huffmann.decodeFile("output.dat", "dec_tab.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
