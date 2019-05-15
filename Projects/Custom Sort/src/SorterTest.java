
/**
 * This class rigorously tests the {@code Sorter} class.
 * 
 * @author Dakota Brown
 * @version CMSC 256-001, Spring 2018
 * @since March 17, 2018
 */

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SorterTest {


	@Test
	/**
	 * Tests a simple integer array of length 20.
	 */
	void test1() {
		int[] unsortedArray =
			{42, 47, 18, 73, 48, 6, 18, 81, 76, 82, 41, 11, 23, 65, 38};
		int[] expectedResult = 
			{6, 11, 18, 18, 23, 38, 41, 42, 47, 48, 65, 73, 76, 81, 82};
		int[] sortedArray = Sorter.sortIntArray(unsortedArray);
				
		assertArrayEquals(expectedResult, sortedArray);
	}
	
	@Test
	/**
	 * Tests a simple integer array (all values the same) of length 20.
	 */
	void test2() {
		int[] unsortedArray =
			{42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42};
		int[] expectedResult = 
			{42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42};
		int[] sortedArray = Sorter.sortIntArray(unsortedArray);
		
		assertArrayEquals(expectedResult, sortedArray);
	}

	@Test
	/**
	 * Tests a simple integer array of length 0.
	 */
	void test3() {
		int[] unsortedArray = {};
		int[] expectedResult = {};
		int[] sortedArray = Sorter.sortIntArray(unsortedArray);
		
		assertArrayEquals(expectedResult, sortedArray);
	}
	
	@Test
	/**
	 * Tests a simple integer array of length 1.
	 */
	void test4() {
		int[] unsortedArray = {99};
		int[] expectedResult = {99};
		int[] sortedArray = Sorter.sortIntArray(unsortedArray);
		
		assertArrayEquals(expectedResult, sortedArray);
	}
	
	@Test
	/**
	 * Tests passing sortIntArray an array with negative values.
	 */
	void test5() {
		int[] unsortedArray = 
			{42, 47, 18, 73, 48, 6, 18, 81, 76, 82, 41, 11, 23, 65, -38};
		
		assertThrows(IllegalArgumentException.class, 
					 ()->{Sorter.sortIntArray(unsortedArray);});		
		
	}
	
	@Test
	/**
	 * Tests passing sortIntArray a null array.
	 */
	void test6() {
		int[] unsortedArray = null;
		
		assertThrows(IllegalArgumentException.class, 
					 ()->{Sorter.sortIntArray(unsortedArray);});		
		
	}
	
	@Test
	/**
	 * Tests passing sortIntArray an array with a large value. The underlying
	 * implementation of the sort relies on having a counter array with an 
	 * index of each number to count. If the number is too large, however, the
	 * program will attempt to create an array whose size too large to be
	 * accommodated by the current memory.
	 */
	void test7() {
		int[] unsortedArray = {0, Integer.MAX_VALUE - 1};
		
		assertThrows(OutOfMemoryError.class, 
					 ()->{Sorter.sortIntArray(unsortedArray);});		
		
	}
	
	@Test
	/**
	 * Tests passing sortIntArray an array with the max integer value. The 
	 * underlying implementation of the sort relies on having a counter
	 * array with an index of each number to count. In doing so, the counter
	 * array must have a length equal to the largest value encountered, plus
	 * one. Adding one, however, to the max integer value causes an overflow, 
	 * and the number wraps around to a negative value (specifically, in this 
	 * case, Integer.MIN_VALUE).
	 */
	void test8() {
		int[] unsortedArray = {0, Integer.MAX_VALUE};
		
		assertThrows(NegativeArraySizeException.class, 
					 ()->{Sorter.sortIntArray(unsortedArray);});		
		
	}
	
	@Test
	/**
	 * Tests an array with multiple zeros.
	 */
	void test9() {
		int[] unsortedArray =
			{42, 47, 18, 73, 48, 0, 18, 81, 76, 82, 0, 0, 23, 65, 38};
		int[] expectedResult = 
			{0, 0, 0, 18, 18, 23, 38, 42, 47, 48, 65, 73, 76, 81, 82};
		
		assertArrayEquals(expectedResult, Sorter.sortIntArray(unsortedArray));		
		
	}

}
