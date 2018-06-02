/*
 * This program will take three inputs of three integers from the user 
 * and sort these three integers in descending order (largest to
 * smallest)
 * 
 * Date Last Modified <September 25, 2014>
 * 
 * Author <Kala Arentz>
 * 
 */
import java.util.Scanner;

public class Sort3Numbers {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int number1, number2, number3;
		
		// User inputs two numbers
		System.out.print("Enter a number: ");
		number1 = input.nextInt();
		//System.out.println("DEBUG: " + number1);
		System.out.print("Enter a number: ");
		number2 = input.nextInt();
		//System.out.println("DEBUG: " + number2);
		System.out.print("Enter a number: ");
		number3 = input.nextInt();
		//System.out.println("DEBUG: " + number3);
		
		
		// Selection statements 
		//--> Larger number first
		if ((number1 >= number2) && (number2 >= number3)) {
			System.out.println("Sorted Numbers: " + number1 + ", " + number2 + ", " + number3);
		}
		else if ((number2 >= number1) && (number1 >= number3)) {
			System.out.println("Sorted Numbers: " + number2 + ", " + number1 + ", " + number3);
		}
		else if ((number3 >= number2) && (number2 >= number1)) {
			System.out.println("Sorted Numbers: " + number3 + ", " + number2 + ", " + number1);
		}
		else if ((number1 >= number2) && (number1 >= number3)) {
			System.out.println("Sorted Numbers: " + number1 + ", " + number3 + ", " + number2);
		}
		else if ((number2 >= number1) && (number3 >= number1)) {
			System.out.println("Sorted Numbers: " + number2 + ", " + number3 + ", " + number1);
		}
		else if ((number3 >= number2) && (number1 >= number2)) {
			System.out.println("Sorted Numbers: " + number3 + ", " + number1 + ", " + number2);
		}
	}

}
