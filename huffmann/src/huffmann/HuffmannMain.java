package huffmann;

import java.io.IOException;

public class HuffmannMain {
	public static void main(String[] args) {
		Huffmann huffmann = new Huffmann();
		try {
			huffmann.initializeUnsortedFrequencyMap("input.txt");
			huffmann.initializeSortedFrequencyMap();
			huffmann.initializeHuffmannTree();
			huffmann.encodeToFile("output.dat");
			huffmann.decodeFile("output.dat", "dec_tab.txt");
			huffmann.analyzeFileSizes("input.txt", "output.dat");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
