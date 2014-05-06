package huffmann;

import java.io.IOException;

public class HuffmannCLI {
	public static void main(String[] args) {
		Huffmann huffmann = new Huffmann();
		try {
			huffmann.createFrequencyMap("/Users/livio/Desktop/input.txt");
			huffmann.sortFrequenceMap();
			//huffmann.createHuffmanEncoding();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
