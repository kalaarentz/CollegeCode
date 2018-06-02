/**
Author: Kala Arentz

Date: Mar 3, 2015

Purpose: This program will allow us
to practice some recursion methods.
Will have two different public 
methods. 

 */

import java.util.Arrays;

public class Lab2
{

	private String[][] alphabet = { { "0" },
		{ "1" },{ "2", "a", "b","c" },
		{ "3", "d", "e", "f" }, 
		{ "4", "g", "h", "i" },
		{ "5", "j", "k", "l" },
		{ "6", "m", "n", "o" },
		{ "7", "p", "q", "r", "s" },
		{ "8", "t", "u", "v" },
		{ "9", "w", "x", "y", "z" } };

	/**
	 *  1.) For the simple recursive method for 
	 *  calculating n! It would be called for 
	 *  n + 1.
	 *  
	 *  2.) It is an exponential growth, has an
	 *  example of about 2^n so for Fib(10) you
	 *  would get about 1000 functions.
	 *  It is almost doubling the amount of work.
	 *  1 number higher is almost twice the amount
	 *  or work or method calls. 
	 *  
	 * @param args all of the arguments
	 */
	public static void main( String[] args ) {

		Lab2 lab2 = new Lab2( );

		// Practice of mcCarthy91 method
		System.out.println( lab2.mcCarthy91( 99 ) );
		System.out.println( lab2.mcCarthy91( 100 ) );
		System.out.println( lab2.mcCarthy91( 87 ) );
		System.out.println( lab2.mcCarthy91( 110 ) );


		// Practice of findPhoneWords method
		System.out.println( Arrays.toString( lab2.findPhoneWords( "38" ) ) );
		System.out.println( Arrays.toString( lab2.findPhoneWords( "0" ) ) );
		System.out.println( Arrays.toString( lab2.findPhoneWords( "1" ) ) );
		System.out.println( Arrays.toString( lab2.findPhoneWords( "3856" ) ) );

	}

	/**
	 * This method implements the McCarthy 91 function,
	 * which is defined as follows:
	 * 
	 * 		M(x) = {  x - 10; x > 100
	 * 			   {  M(M(x+11)), x<= 100
	 * 
	 * @param x : number given to go through function
	 * @return number returned by function
	 */
	public int mcCarthy91( int x )
	{
		if ( x > 100 )
		{
			return  x - 10 ;
		}
		else if ( x <= 100 )
		{
			return mcCarthy91( mcCarthy91( x + 11 ) );

		}
		return 0;
	}

	/**
	 * This takes as input a String representing
	 * a phone number, and returns an array of Strings 
	 * containing all possible "phone numbers."
	 * 
	 * You can assume the input String contains
	 * only numerals and is non-empty.
	 * 
	 * @param word : the string given that has numbers
	 * @return result : the string with all the letters
	 */
	public String[] findPhoneWords( String word )
	{
		// All the different arrays that are used
		String[] partialAnswer = null;
		String[] result = null;
		// word length is one
		if ( word.length() < 2 )
		{
			int number = word.charAt( 0 ) - '0';
			result = new String[ alphabet[ number ].length ];

			for ( int i = 0; i < alphabet[ number ].length; i++ )
			{
				result[ i ] = alphabet[ number ][ i ].toUpperCase();
			}
		}
		else
		{
			int number = word.charAt( 0 ) - '0';
			int idx = 0;
			partialAnswer = findPhoneWords( 
					word.substring( 1, word.length() ) );
			result = new String[ partialAnswer.length 
			                     * alphabet[ number ].length ];

			for ( int i = 0; i < alphabet[ number ].length; i++ )
			{
				for ( int j = 0; j < partialAnswer.length; j++ )
				{
					result[ idx ] = alphabet[number][ i ].toUpperCase() 
							+ partialAnswer[ j ];
					idx++;
				}
			}
		}
		return result;
	}
}