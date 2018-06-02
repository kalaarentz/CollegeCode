/**
 * Author: Kala Arentz
 * 
 * Date: February 3, 2015
 * 
 * Purpose: This program serves as a re-introduction to Java programming. 
 * Also as your program working with multi-dimensional arrays.
 * This program has two different methods deleteCells and a main method
 * to test your code.
 *
 */
import java.util.Arrays;

public class Lab1 {

	/**
	 *  Main method to test my deleteCells Method
	 * 
	 *  I tested multiple different lengths of arrays and also false versus 
	 *  true and the specific number that was being deleted for rows 
	 *  and columns. I tried both of the examples in the lab1, and 
	 *  both of their outputs were the correct array given in the 
	 *  example. I also tried variations of each of those examples and 
	 *  made my table larger with 3 elements and so on. 
	 *  
	 */
	public static void main( String args[] ){

		int[][] table = { { 1, 2 }, { 3, 4 } };

		// Test the strings, printing them out
		System.out.println( Arrays.deepToString( table ) );
		System.out.println( Arrays.deepToString( 
				deleteCells( table, false, 1 ) ) );

	}

	/**
	 * This method will delete either a row or column from 2-D array.
	 * 
	 * *Assuming the arrays are all the same length.
	 * 
	 * @param int table: 2-D array
	 * @param boolean isRowOrColumn: which row or column will be deleted
	 * @param int number: which row or column that is given earlier will be
	 * 		deleted (will start at an index of 0)
	 */
	public static int[][] deleteCells( int[][] table, boolean isRowOrColumn,
			int number ){
		
		// temporary integer to store the number held in array
		int temp = 0; 

		// boolean is true remove "row", false remove "column"
		if( isRowOrColumn ){

			//initialize new array
			int[][] deletdArray = new int[ table.length-1][table[0].length];

			for( int idx = 0; idx < table.length; idx++ ){

				for( int j = 0; j < table[idx].length; j++ ){

					// copy the existing array into new array
					// take into account the idx number being above or below
					if( idx < number ){
						temp = table[idx][j];
						deletdArray[idx][j] = temp;

					}
					// idx will be subtracted by one
					else if( idx > number ){ 
						temp = table[idx][j];
						deletdArray[idx-1][j] = temp;

					}
				}
			}

			return deletdArray;

		}
		// false--> remove the column
		else{ 

			//initialize new array
			int[][] deletdArray = new int[table.length][table[0].length-1];

			for( int idx = 0; idx < table.length; idx++ ){

				for( int j = 0; j < table[idx].length; j++ ){

					// copy the existing array into new array
					// take into account the j number being above or below
					if( j < number ){
						temp = table[idx][j];
						deletdArray[idx][j] = temp;

					}
					// j will be subtracted by one
					else if( j > number ){ 
						temp = table[idx][j];
						deletdArray[idx][j-1] = temp;

					}	
				}
			}

			return deletdArray;

		}
	}
}
