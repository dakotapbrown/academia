import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.util.Random;

public class TestFileMaker {

	public static void main(String... args) throws IOException {
		for (int i = 0; i < 5; i++) {
			File out = new File(Paths.get("", "src/", String.format("test%d.txt", i)).toAbsolutePath().toString());
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(out));

			Random rand = new Random();
			int size = 0;
			while (size < 2) {
				size = (rand.nextInt() * 1861) % 100;
			}
			writer.write(String.format("%d\n", size));
			int numCuts = (rand.nextInt()  * 1861) % size;
			while (numCuts < 1 || numCuts > size - 1) {
				numCuts = (rand.nextInt()  * 1861) % size;
			}
			writer.write(String.format("%d\n", numCuts));
			for (int j = 0; j < numCuts; j ++) {
				int cut = (rand.nextInt() * 1861) % size;
				while (cut <= 0 || cut >= size) {
					cut = (rand.nextInt() * 1861) % size;
				}
				writer.write(String.format("%d\n", cut));
			}

			writer.flush();
			writer.close();
		}
	}

}
