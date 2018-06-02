/*
Author: Kala Arentz

Date: Apr 8, 2015

Purpose: Write a recursive method for doing multiplication using addition only.
  For an extra twist, see if you can write your method to work with negative 
  numbers as well!

*/
package exam2;

public class MultiplicationRecursive {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MultiplicationRecursive example = new MultiplicationRecursive( );
		System.out.println( example.multiplicationSolve( 5, 10 ) );

	}
	
/**
 * 
 * @param num first number is 5 x 10, so the number 5 will be adding 10
 * @param amount second number is how many times 
 * @return
 */
	public int multiplicationSolve( int num, int amount )
	{
		int result = 0;
		if( amount != 0 )
		{
			result = 5 + multiplicationSolve( num, amount - 1 );
			return result;
		}
		
		return result;
	}

}
