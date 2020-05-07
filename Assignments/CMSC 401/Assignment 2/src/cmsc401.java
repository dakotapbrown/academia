// Dakota Brown

import java.util.Scanner;

public class cmsc401 {

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

		long ans = partition(array, 0, array.length - 1);
		System.out.println(ans);
	}

	private static long partition(long[] array, int begin, int end) {
		long x = array[end];
		int i = begin - 1;
		for (int j = begin; j <= end - 1; j++) {
			if (array[j] <= x) {
				i++;
				swap(array, i, j);
			}
		}

		int pivotPosition = i + 1;
		swap(array, pivotPosition, end);

		int middlePosition = array.length / 2;
		if ((array.length % 2 == 0 && pivotPosition == middlePosition - 1) || (pivotPosition == middlePosition)) {
			// this is likely the median.
			return array[pivotPosition];
		} else if (pivotPosition > middlePosition) {
			// choose left part of partition
			return partition(array, begin, pivotPosition - 1);
		} else {
			// choose right part of partition
			return partition(array, pivotPosition + 1, end);
		}

	}

	private static void swap(long[] array, int a, int b) {
		long temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}
}
