/**
 * Lab 09: Playing with Arrays and Floyd's Triangle
 * This program uses arrays to explore Floyd's Triangle. Each row starts with a 
 * number from the lazy caterer's sequence which is defined for this problem by the 
 * following equation. Given a row index R (where R is between 0 and the maximum
 * number  of rows minus one) the first value of the row is ((row*row)+row+2)/2
 *  
 * Last Modified: <November 5, 2014>
 * @author <Kala Arentz>
 */
import java.util.Scanner;


public class FloydsTriangle {
    /** No class scoped variables (data members) are allowed */

    /**
     * The main method, where the program starts
     * @param args
     */
    public static void main(String[] args) {
        FloydsTriangle t = new FloydsTriangle();
        Scanner input = new Scanner(System.in);
        t.drawTriangle(input);
        // No other Java statements should be in here.
    }
    
    /**
     * Constructor for the PasswordDriver.
     */
    public FloydsTriangle() {
        // Nothing to initialize in this class (no data members).
        // No other Java statements should be in here.
    }
    
    /**
     * Run the application (this is where the program really starts)
     * 
     * @param input Scanner to use for all interaction with the user.
     */
    public void drawTriangle(Scanner input) {
        int forwardValues[], backwardValues[], resultValues[];
        int max;

        System.out.print("Maximum number of rows: ");
        max = input.nextInt();

        System.out.println("-----------------------------------");
        
        /*
         * Display Floyd's Triangle 
         */
        for(int row = 0; row < max; ++row ) {
            forwardValues = getRowNumber(max, row);
            displayRow( forwardValues );
        }

        System.out.println("-----------------------------------");
        
        /*
         * Display Floyd's Triangle upside down and to the right
         */
        for(int row = max-1; row >= 0; --row ) {
            forwardValues = getRowNumber(max, row);
            reverseRow( forwardValues );
            displayRow( forwardValues );
        }
        
        System.out.println("-----------------------------------");

        /*
         * Join the two values
         */
        for(int row = 0; row < max; ++row ) {
            forwardValues = getRowNumber(max, row);

            backwardValues = getRowNumber(max, max - row -1);
            reverseRow( backwardValues );
            
            resultValues = mergeArrays( forwardValues, backwardValues );
            displayRow( resultValues );
        }

        System.out.println("-----------------------------------");

        /*
         * Join the two values and sort the result
         */
        for(int row = 0; row < max; ++row ) {
            forwardValues = getRowNumber(max, row);

            backwardValues = getRowNumber(max, max - row -1);
            reverseRow( backwardValues );
            
            resultValues = mergeArrays( forwardValues, backwardValues );
            
            sortArray(resultValues);
            displayRow( resultValues );
        }

        System.out.println("-----------------------------------");
    }

    /**
     * Get a row of Floyd's Triangle
     * 
     * Example, if max = 5 then
     * Row # | Row array values
     * ------+-----------------------
     *  0    | [ 1,  0,  0,  0,  0]
     *  1    | [ 2,  3,  0,  0,  0]
     *  2    | [ 4,  5,  6,  0,  0]
     *  3    | [ 7,  8,  9, 10,  0]
     *  4    | [11, 12, 13, 14, 15]
     * ------+-----------------------
     *  
     * @param max The maximum number of rows
     * @param row The row number to extract (must be > 0 and < max)
     * @return An array of the length 'max' with the appropriate values filled in.
     */
    private int[] getRowNumber(int max, int row) {
        // create an array
    	int rowNumber[] = new int[max];
    	
    	// lazy caterer's sequence
    	int value = ((row * row) + row + 2)/2;
    	
    	// loop
    	for (int idx = 0; idx < max; idx++){
    		if (idx < row + 1){
    			rowNumber[idx] = value;
    			value++;
    		}
    		else {
    			rowNumber[idx] = 0;
    		}
    	}
        return rowNumber;
    }
    
    /**
     * Reverse the values array provided
     * For example, if the array contains:
     * [ 7,  8,  9, 10,  0]
     * then after calling this method the array will contain:
     * [ 0, 10,  9,  8,  7]
     * 
     * @param values The array to reverse
     */
    private void reverseRow( int values[] ) {
        // compare the two farthest 
    	int max = (values.length) - 1;
    	
    	// loop to keep the entire string
    	for(int idx = 0; idx < values.length; idx++) {
    		// loop to decrease the max number
    		for ( max = (values.length) - 1; max >= idx; max--){
    			if (values[max] == 0){
    				int tmp = values[idx];
        			values[idx] = values[max];
        			values[max] = tmp;
    			} 
    			else {
    				if (values[max] > values[idx]){
            			int tmp = values[idx];
            			values[idx] = values[max];
            			values[max] = tmp;
            		}
            		else {
            		}
    			}
    			idx++;
    		}
    	}
    }

    /**
     * Display the row with values separated with a tab character ('\t')
     * If the value is less than or equal to 0 then a dash ('-')
     * character is printed instead.
     * 
     * For example, if the array contains:
     * [ 7,  8,  9, 10,  0]
     * Then this method will print out:
     *  7    8   9   10  -
     *  
     * @param values The array to display
     */
    private void displayRow( int values[] ) {
        // loop
    	for (int idx = 0; idx < values.length; idx++){
    		if (values[idx] == 0) {
    			System.out.print('-');
    			System.out.print('\t');
    		}
    		else {
    			System.out.print(values[idx]);
    			System.out.print('\t');
    		}
    	}
    	System.out.println();
    }
    
    /**
     * Merge the two arrays to produce a new array, of the same size,
     * taking the sum of the values in each of the arrays.
     * 
     * For example, if the arrays contain:
     * left  = [ 7,  8,  9, 10,  0]
     * right = [ 2,  3,  0,  0,  0]
     * then the resulting array will be:
     * result = [ 9, 11, 9, 10, 0]
     * 
     * Note: You may assume that the left and right arrays of of the same length.
     * 
     * @param left The left input array
     * @param right The right input array
     * @return The sum of the left and right array elements as a new array
     */
    private int[] mergeArrays(int[] left, int[] right) {
    	// create new Array
    	int newArray[] = new int[left.length];
    	
        // make a loop to go through each of idx
    	for (int idx = 0; idx < left.length; idx++){
    		newArray[idx] = left[idx] + right[idx];
    	}
        return newArray;
    }
    
    /**
     * Sort the array in ascending order.
     * See lab specification for the algorithm to implement.
     * 
     * For example, if the array contains:
     * result = [ 7, 5, 9, 13, 2]
     * Then the resulting array would be:
     * result = [ 2, 5, 7, 9, 13]
     * 
     * @param arrayToSort The array to sort
     */
    private void sortArray(int[] arrayToSort) {
        /** // Where 'n' is the length of the array.
         * 
         *  for p = 0 to n-1{// the "to" notation means inclusive of both 0 and n-1
         *  	// Walk through the array backward to move the value into place
         *  	for j = n-1 to p+1 [
         *  		// If the two values next to one another are out of order
         *  		if a[j] < a[j-1]
         *  			// Swap the values
         *  			swap a[j] with a[j-1]
         */
    	
    	int n = arrayToSort.length;
    	
    	for (int idx = 0; idx <= n-1; idx++) {
    		for (int max = n - 1; max >= idx + 1; max--){
    			if (arrayToSort[max] < arrayToSort[max-1]){
    				int tmp = arrayToSort[max];
    				arrayToSort[max] = arrayToSort[max-1];
    				arrayToSort[max-1] = tmp;
    			}
    		}
    	}
  
    }
}