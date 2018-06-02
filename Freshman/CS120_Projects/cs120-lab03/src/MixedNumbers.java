/*
 * This program will convert improper fractions into a 
 * mixed number.
 * 
 * Date Last Modified <September 17, 2014>
 * 
 * author <Kala Arentz>
 * 
 */
import java.util.Scanner;

public class MixedNumbers {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int numerator;
		int denominator;
		
        // Enter the numerator
		System.out.print("Enter numerator: ");
		numerator = input.nextInt();
		
		// Enter the denominator
		System.out.print("Enter denominator: ");
		denominator = input.nextInt();
		
		// Show the Improper Fraction
		String improperFraction =  (numerator +"/" + denominator);
		System.out.print( improperFraction + " = " );
		
		// Convert the Improper Fraction to Mixed Number
		System.out.print( numerator/denominator);
		System.out.print( " " + (numerator%denominator)+ "/" + denominator );
		
		
	}

}
