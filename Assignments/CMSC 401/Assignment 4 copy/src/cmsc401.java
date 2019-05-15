// Dakota Brown

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class cmsc401 {

	private static boolean[] cuts;
	private static int[][] C;
	private static Queue<String> q;
	private static Map<String, String> map;

	public static void main(String... args) throws IOException {
		/* Debugging variable initialization */
		map = new HashMap<>();
		q = new ArrayDeque<>();
		/* Debugging variable initialization */

		Scanner sc;
		if (args.length > 0) {
			// for debugging; feeding files instead of input from console
			sc = new Scanner(Paths.get("src/" + args[0]).toAbsolutePath().toFile());
		} else {
			sc = new Scanner(System.in);
		}

		// get values
		int size = sc.nextInt();
		int numCuts = sc.nextInt();
		C = new int[size + 1][size + 1];
		cuts = new boolean[size + 1];

		// initialize boolean array
		for (int i = 0; i < numCuts; i++) {
			cuts[sc.nextInt()] = true;
		}

		// initialize cut cost matrix
		for (int i = 0; i < C.length; i++) {
			for (int j = 0; j < C[0].length; j++) {
				C[i][j] = Integer.MAX_VALUE;
			}
		}

		// do the thing
		System.out.println(cut(0, size, -1));


		/* Debugging output */
//		q.forEach(str -> System.out.print(map.get(str)));
//		System.out.println();
//
//		System.out.printf("%5s", "");
//		for (int i = 0; i < size + 1; i++) {
//			System.out.printf("%5d", i);
//		}
//		System.out.println();
//		int i = 0;
//		for (int[] ints : C) {
//			System.out.printf("%5d", i++);
//			for (int anInt : ints) {
//				if (anInt == Integer.MAX_VALUE) {
//					System.out.printf("%5s", "[]");
//				} else {
//					System.out.printf("%5d", anInt);
//				}
//			}
//			System.out.println();
//		}
		/* Debugging output */
	}


	/*
		beg+1 will at min be 1, and end will at max will be length of rod. therefore,
		this for loop executes at most n-1 times. each successive  recursive call thereafter will be
		less than n-1 executions; for loop has O(n) time complexity. assuming each recursive call does
		not decrement and each calls the for loop with n executions, for loop has overall O(n * 2n) = O(n^2).
		all other statements are O(1); so cut() has overall time complexity O(n^2). although this is faster than
		O(n^3), as n grows larger, the cost of recursive function calls would outweigh this benefit. a bottom-up
		dynamic programming approach would be more time-saving than this top-down approach.
	*/
	private static int cut(int beg, int end, int prev) {
		/* Debugging variables  */
//		StringBuilder builder = new StringBuilder();
//		builder.append(String.format("\n(%d, %d)\n", beg, end));
//		q.offer(String.format("%d, %d", beg, end));
		/* Debugging variables  */

		int min = Integer.MAX_VALUE;
		int len = end - beg;

		for (int i = beg + 1; i < end; i++) {
			if (cuts[i]) {
				if (prev != -1) {
					prev = i;
				}

				// If C[beg][i] hasn't been calculated, do so
				if (C[beg][i] == Integer.MAX_VALUE) {
					C[beg][i] = cut(beg, i, prev);
				}
				// Likewise for C[i][end]
				if (C[i][end] == Integer.MAX_VALUE) {
					C[i][end] = cut(i, end, prev);
				}

				if (len + C[beg][i] + C[i][end] <= min) {
					min = len + C[beg][i] + C[i][end];
				}

				/* Debug */
//				builder.append((String.format("\t%d + (%d, %d) + (%d, %d) = %d\n", len, beg, i, i, end, len + C[beg][i] + C[i][end])));
			}
		}


		if (min == Integer.MAX_VALUE) {
			min = 0;

			/* Debug */
//			builder.append("\tNo cuts\n");
		}

		/* Debug */
//		map.put(String.format("%d, %d", beg, end), builder.toString());

		return min;
	}
}

