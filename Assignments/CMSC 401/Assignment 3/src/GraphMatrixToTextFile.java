import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class GraphMatrixToTextFile {

	private static int[][] matrix;
	private static boolean[][] printed;

	public static void main(String... args) throws IOException {

		Scanner scanner = new Scanner(new File(args[0]));

		int rows = scanner.nextInt();
		int cols = scanner.nextInt();
		if (scanner.hasNextLine()) {
			scanner.nextLine();
		}
		matrix = new int[rows][cols];
		printed = new boolean[rows][cols];

		int row = 0;
		int col = 0;
		while (scanner.hasNextLine()) {
			if (row == 7) {
				while (scanner.hasNextLine()) {
					scanner.nextLine();
				}
				continue;
			}
			String[] nums = scanner.nextLine().split("\\s");
			for (String num : nums) {
				matrix[row][col] = Integer.parseInt(num);
				col++;
			}
			row++;
			col = 0;
		}

		Random random = new Random();
		for (int i = 1; i <= Integer.parseInt(args[1]); i++) {
			int rand = Math.abs(random.nextInt((8192))) % 8191;
			File output = new File(String.format("src\\test%d.txt", i));
			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(output));

			writer.write(String.format("%d\n", rows));
			writer.write(String.format("%d\n", 24));

			for (row = 3; row <= rows; row++) {
				writer.write(String.format("%d ", row));
				writer.write(String.format("%d\n", (row * rand) % 200));
			}


			for (row = 0; row < rows; row++) {
				for (col = 0; col < cols; col++) {
					if (matrix[row][col] != 0 && row != col && !printed[row][col]) {
						writer.write(String.format("%d %d %d\n", row + 1, col + 1, (matrix[row][col] * rand) % 200));
						printed[row][col] = true;
						printed[col][row] = true;
						rand = Math.abs(random.nextInt((8192))) % 8191;
					}
				}
			}

			printed = new boolean[rows][cols];
			writer.flush();
			writer.close();
		}
	}

}
