/**
 * This class supplies a class level method that can sort integer arrays via 
 * a counting algorithm.
 * 
 * @author Dakota Brown
 * @version CMSC 256-001, Spring 2018
 * @since March 17, 2018
 */
public class Sorter {	
	
	/**
	 * Sorts an array of natural integers (and zero) from smallest to largest.
	 * 
	 * @param array The integer array to be sorted
	 * @return The sorted integer array
	 * 
	 * @throws IllegalArgumentException
	 * 			If {@code array} is a null reference, or {@code array} contains
	 * 			a negative integer.
	 */
	public static int[] sortIntArray(int[] array) throws IllegalArgumentException {
		
		if (array == null) {
			throw new IllegalArgumentException("Cannot sort a null array");
		}
		
		if (array.length == 0 || array.length == 1) {
			return array;
		}
		
		int max = 0;
		for (int num : array) {
			if (num < 0) {
				throw new IllegalArgumentException("Integers must be positve");
			}
			
			if (num > max) {
				max = num;
			}		
		}
		
		int[] counter = new int[max + 1];
		
		for (int num : array) {
			counter[num]++;		
		}
		
		int j = 0;
		for (int i = 0; i < counter.length; i++) {
			while (counter[i] > 0) {
				array[j++] = i;
				counter[i]--;
			}
		}
		
		return array;
		
	}
	
}
