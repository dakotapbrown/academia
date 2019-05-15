/* Program for finding out majority element in an array */

import java.util.Scanner;

class MajorityElement {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		int loops = scanner.nextInt();
		if (scanner.hasNext()) {
			scanner.nextLine();
		}

		int[] array = new int[loops];
		for (int i = 0; i < loops; i++) {
			array[i] = scanner.nextInt();
		}

		printMajority(array, array.length);

	}

	/* Function to print Majority Element */

	static void printMajority(int a[], int size)	{

		/* Find the candidate for Majority*/
		int cand = findCandidate(a, size);

		/* Print the candidate if it is Majority*/
		if (isMajority(a, size, cand))
			System.out.println(cand);
		else
			System.out.println("-1");

	}

	/* Function to find the candidate for Majority */

	static int findCandidate(int a[], int size) {

		int maj_index = 0, count = 1;
		int i;
		for (i = 1; i < size; i++) {
			if (a[maj_index] == a[i])
				count++;
			else
				count--;
			if (count == 0) {
				maj_index = i;
				count = 1;
			}
		}

		return a[maj_index];

	}

	/* Function to check if the candidate occurs more
	   than n/2 times */

	static boolean isMajority(int a[], int size, int cand) {

		int i, count = 0;
		for (i = 0; i < size; i++) {
			if (a[i] == cand)
				count++;
		}
		if (count > size / 2) {
			return true;
		} else {
			return false;
		}

	}
}