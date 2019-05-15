import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class IntegerParse {

	public static void main(String[] args) throws IOException {

		File input = new File("src\\generate.txt");
		Scanner scanner = new Scanner(input);
		scanner.useDelimiter("");

		File output = new File("src\\test5.txt");
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(output));

		StringBuilder newInteger = new StringBuilder();
		int length = 0, numberCount = 0;
		double random = Math.random() * 10;
		while (scanner.hasNext()) {
			char ch[] = scanner.next().toCharArray();
			char potentialNumber = ch[0];
			if (Character.isDigit(potentialNumber)) {
				newInteger.append(potentialNumber);
				length++;
			}

			if (length > random) {
				newInteger.append("\n");
				writer.write(newInteger.toString());
				newInteger = new StringBuilder();
				length = 0;
				numberCount++;
				random = Math.random() * 10;
			}
		}

		writer.write(String.valueOf(numberCount));
		writer.flush();
		writer.close();
	}

}
