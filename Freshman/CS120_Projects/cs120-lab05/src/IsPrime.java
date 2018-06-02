/*
 * This program will use the number the user inputs to decide if it is prime number or not.
 * The program will also have to make sure the user puts in a number greater then 1 (cannot be 
 * negative or zero). The way to check if an number is an prime number is if it divisible by 
 * any integer M (2 to number/2). 
 * 
 * Date Last Modified < October 1, 2014>
 * 
 * Author <Kala Arentz>
 * 
 */
import java.util.Scanner;

public class IsPrime {

	public static void main(String[] args) {
		int number, m , total, count;
		Scanner input = new Scanner(System.in);


		// Loop Statements
		m = 2;
		count = 0;

		do {
			System.out.print("Enter a number to check: ");
			number = input.nextInt();
			if (number <=1) {
				System.out.println("Please enter a number greater than 1.");
			}

		} while (number <= 1);

		// Initialization
		while (m <= (number/2)) {	// Condition
			// Main Work
			total = number/m;

			//System.out.println("DEBUG: " + total + " total");

			if ((number%m) == 0) {
				System.out.println(number + " is composite. Divisible by " + m);
				count ++;
			}
			m ++ ; // Progress

		}
		if (count == 0) {
			System.out.println(number + " is Prime!");
		}
		else {
			System.out.println(number + " is not prime. It is divisible by " + count + " other numbers.");
		}

	}
}
