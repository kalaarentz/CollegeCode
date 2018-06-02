/*
 * This program will be like you are in a programming language that only supports addition (+) and subtraction (-) of
 * integer numbers. This program will add support for multiplication, division, and modulus as three different methods.
 * The program will only support positive integers values inclusive of 0. The program will NEVER use "*", "/", "%" symbols,
 * except for debugging purposes. This program will only use the following types: int, boolean, String, Scanner.
 * 
 * <Date Last Modified: October 10, 2014>
 * 
 *  <Author: Kala Arentz>
 *  
 */
import java.util.Scanner;

public class SimpleLangCalc {

	public static void main(String[] args) {
		int a, b;
		int result;
		Scanner input = new Scanner(System.in);

		a = getPositiveInt(input, "First number : ");
		b = getPositiveInt(input, "Second number : ");
		System.out.println();

		result = a + b;
		System.out.println(a + " + " + b + " = " + result);

		result = a - b;
		System.out.println(a + " - " + b + " = " + result);

		result = multiply(a, b);
		System.out.println(a + " * " + b + " = " + result);
		// Debug Output
		//result = a * b;
		//System.out.println(a + " * " + b + " = " + result + "(DEBUG)");

		result = divide(a, b);
		System.out.println(a + " / " + b + " = " + result);
		// Debug Output
		//result = a / b;
		//System.out.println(a + " / " + b + " = " + result + "(DEBUG)");

		result = modulus(a, b);
		System.out.println(a + " % " + b + " = " + result);
		// Debug Output
		//result = a % b;
		//System.out.println(a + " % " + b + " = " + result + "(DEBUG)");
	}

	/*
	 * Get a positive integer value (inclusive of 0) from the user. Display the provided prompt string before scanning 
	 * for the integer value. If the value provided is negative, then print an error, and prompt again.
	 * 
	 * @param input Scanner object to use for input
	 * @param prompt The prompt to display
	 * @return Positive integer value provided by the user
	 * 
	 */

	private static int getPositiveInt(Scanner input, String prompt) {
		//Ask the user to enter a first number and second number, both need to be greater then 0
		int x; // input for a and b

		do {
			System.out.print(prompt);
			x = input.nextInt();
			if (x < 0){
				System.out.println("Error: Please enter a positive number");
				System.out.print(prompt);
				x = input.nextInt();
			}
		}while (x < 0);
		return x;
	}

	/*
	 * Multiplication: lhs * rhs
	 * 
	 * @param lhs First number
	 * @param rhs Second number
	 * @return lhs * rhs
	 * 
	 */

	private static int multiply(int lhs, int rhs){
		// multiply a and b by only adding the numbers
		int count, total;
		count = 1;
		total = 0;
		while (count <= rhs) {
			total = total +lhs;
			count++;
		}
		return total;		
	}

	/* 
	 * Division; num / dem
	 * 
	 * @param num Numerator
	 * @param dem Denominator 
	 * @return num / dem, or 0 if dem is 0
	 * 
	 */
	private static int divide(int num, int dem) {
		// division of a and b by only subtracting numbers
		int count, total;
		count = 0;
		total = num;

		while (total > dem ) { 
			if (total < 0) {
				return count;
			}
			else if (total == 0) {
				return count;
			}
			total = total - dem;
			//System.out.println("DEBUG: total " + total);
			count++;
			//System.out.println("DEBUG: count " + count );
			if (num < dem) {
				count = 0;
				return count;
			}
			if (dem == 0) {
				count = 0;
				return count;
			}
		}
		return count; // how many times the loop runs throug

	}

	/*
	 * Modulus calculation: num % dem
	 * 
	 * @param num Numerator
	 * @param dem Denominator
	 * @return num / dem, or 0 if dem is 0
	 * 
	 */

	private static int modulus(int num, int dem) {
		// using adding and subtraction to find if the num/dem have any remainders
		int count, total;
		count = 0;
		total = num;

		while (total > dem ) { 
			if (total < 0) {
				return total;
			}
			else if (total == 0) {
				total = 0;
				return total;
			}
			total = total - dem;
			//System.out.println("DEBUG: total " + total);
			count++;
			//System.out.println("DEBUG: count " + count );
			if (num < dem) {
				return total;
			}
			if (dem == 0) {
				total = 0;
				return total;
			}
		}
		return total;

	}
}
