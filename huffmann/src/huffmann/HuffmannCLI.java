package huffmann;

import java.io.IOException;

public class HuffmannCLI {
	public static void main(String[] args) {
		Huffmann huffmann = new Huffmann();
		try {
			huffmann.encoding("/Users/livio/Desktop/input.txt", "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
