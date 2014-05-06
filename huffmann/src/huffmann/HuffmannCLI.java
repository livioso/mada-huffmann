package huffmann;

import java.io.IOException;

public class HuffmannCLI {
	public static void main(String[] args) {
		Huffmann huffmann = new Huffmann();
		try {
			huffmann.initializeUnsortedFrequencyMap("/Users/livio/Desktop/input.txt");
			huffmann.initializeSortedFrequencyMap();
			huffmann.initializeHuffmannTree();
			huffmann.encodeToFile("output.dat");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
