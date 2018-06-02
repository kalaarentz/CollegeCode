/*
Author: Kala Arentz

Date: Apr 9, 2015

Purpose:

*/
package exam2;

public class RecursionMethods {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RecursionMethods example = new RecursionMethods( );
		int[] numbers = { 1, 2, 3, 5 };
		System.out.println( example.linearSearch( numbers, 7 ) );

	}
	
	/**
	 * which takes as input an array of ints and a target to look for, and 
	 * returns the position of ‘target’ in the array (if found) or -1 
	 * (if not found). You are free to write your own private helper methods
	 * for linearSearch to use.
	 * @param nums
	 * @param target
	 * @return
	 */
	public int linearSearch( int[] nums, int target )
	{
		// Base Case: when all of the array's have been 
		// searched, either it has been found or it has not, which means -1
		if( recursiveLinear( nums, target, nums.length ) == -1 )
		{
			return -1;
		}
		else
		{
			return recursiveLinear( nums, target, nums.length );
		}
	}
	
	/**
	 * helper method of linear Search you need the size to change that is why
	 * you have another method to help search for the number
	 * @param nums
	 * @param target
	 * @param size
	 * @return
	 */
	private int recursiveLinear( int[] nums, int target, int size )
	{
		if( size == 0 )
		{
			return -1;
		}
		else if( nums[ size - 1] == target )
		{
			return size - 1;
		}
		else
		{
			return recursiveLinear( nums, target, size - 1 );
		}

	}

}
