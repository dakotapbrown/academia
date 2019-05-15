import java.util.Scanner;

public class MedianFinder {

	public static void main(String... args) {

		Scanner scanner = new Scanner(System.in);
		int loops = scanner.nextInt();
		if (scanner.hasNext()) {
			scanner.nextLine();
		}

		long[] array = new long[loops];
		for (int i = 0; i < loops; i++) {
			array[i] = scanner.nextLong();
		}

		randomizedQuickSort(array, 0, array.length - 1);

		if (array.length % 2 == 0) {
			System.out.printf("%s%d\n", "floor(n/2) - 1: ", array[array.length / 2 - 1]);
			System.out.printf("%s%d\n", "floor(n/2): ", array[array.length / 2]);
		} else {
			System.out.printf("%s%d\n", "median: ", array[array.length / 2]);
		}

	}

	private static void randomizedQuickSort(long[] A, int p, int r) {
		if (p < r) {
			int randIndex = random(p, r);
			swap(A, r, randIndex);
			int q = partition(A, p, r);
			randomizedQuickSort(A, p, q - 1);
			randomizedQuickSort(A, q + 1, r);
		}
	}

	private static int random(int p, int r) {
		int rand = (int) (Math.random() * (r - p ) + p);
		while (rand < p && rand > r) {
			rand = (int) (Math.random() * (r - p) + p);
		}

		return rand;
	}

	private static void swap(long[] array, int a, int b) {
		long temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}

	private static int partition(long[] A, int p, int r) {
		long x = A[r];
		int i = p - 1;
		for (int j = p; j <= r - 1; j++) {
			if (A[j] <= x) {
				i++;
				swap(A, i, j);
			}
		}

		swap(A, i + 1, r);
		return i + 1;
	}
}
