// Dakota Brown

import java.util.Scanner;

public class cmsc401 {

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

		int majorityElement = getMajorityElementOf(array);

		if (isMajorityElement(majorityElement, array)) {
			System.out.println(majorityElement);
		} else {
			System.out.println(-1);
		}

	}

	private static boolean isMajorityElement(int majorityElement, int[] array) {

		int count = 0;

		for (int number : array) {
			if (number == majorityElement) {
				count++;
			}
		}

		if (count > (array.length / 2)) {
			return true;
		} else {
			return false;
		}

	}

	private static int getMajorityElementOf(int[] array) {

		int counter = 0;
		int result = array[0];

		for (int number : array) {
			if (number == result) {
				counter++;
			} else {
				counter--;
			}

			if (counter == 0) {
				result = number;
				counter = 1;
			}
		}

		return result;
	}

}
