/*
 * This program will play a guessing game with the user as many times as they desire.
 * The computer will guess the users secret whole number in the least number of attempts (the 
 * computer will only get 6 guess, and then it looses). The user will asked to give the upper 
 * lower bounds, and the upper and lower bounds will be included in numbers they can guess. All 
 * the numbers that are inputed will need to be greater then 0.
 * 
 * Date Last Modified< October 2, 2014>
 * 
 * Author <Kala Arentz>
 * 
 */
import java.util.Random;
import java.util.Scanner;

public class GuessingGame {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		Random rand = new Random();
		int lowerBound, upperBound, count, answer1, answer2 ;

		//________________________________________________________________________
		// Write out the welcome statement
		System.out.println("Welcome to the Guessing Game!");
		System.out.println("I have 6 turns to guess your secret number.");
		System.out.println();

		do {
			//________________________________________________________________________
			// user inputs lower bound
			do { 
				System.out.print("What is the lower bound? ");
				lowerBound = input.nextInt();
				if (lowerBound <= 0) { // take into account no number below or equal to 0
					System.out.println("Error: Please enter a number greater than " + lowerBound);
				}

			} while (lowerBound <= 0);

			//________________________________________________________________________
			// user inputs upper bound
			do { 
				System.out.print("What is the upper bound? ");
				upperBound = input.nextInt();
				if (upperBound <= lowerBound) { // take into account no number below or equal to 0
					System.out.println("Error: Upper Bound must be greater than the Lower bound of " + lowerBound);
				}

			} while (upperBound <= lowerBound);

			System.out.println();

			//________________________________________________________________________
			// Process Guesses
			int randomNumber;
			count = 1;

			//System.out.println("DEBUG: " + randomNumber + " random number");

			while (count <= 6)  {
				//________________________________________________________________________
				// A.) Generate random guess
				randomNumber = lowerBound + rand.nextInt((upperBound - lowerBound +1));
				System.out.println("I guess that the number is " + randomNumber + "!");
				count++ ;

				//________________________________________________________________________
				// B.) Ask if Correct (do... while)
				do {
					System.out.print("Was that correct? [0=yes, -1=too low, 1=too high] ");
					answer1 = input.nextInt();
					if (answer1 !=0 && answer1 !=-1 && answer1 !=1){
						System.out.print("Error: Please enter on of the following [0=yes, -1=too low, 1=too high] ");
						answer1 = input.nextInt();
					}

				} while (answer1 !=0 && answer1 !=-1 && answer1 !=1);

				//________________________________________________________________________
				// Losing after 6 tries statement
				if (count == 7) {
					System.out.println("Drat! I lost...");
					System.out.println();
				}
				else {
				}

				//________________________________________________________________________
				// C.) Adjust the bounds
				if (answer1 == 0) {
					System.out.println("Whew! That took me " + count + " attempts. I can do better next time.");
					count = 7;
				}
				else if (answer1 == -1) {
					lowerBound = randomNumber;
				}
				else if (answer1 == 1 ) {
					upperBound = randomNumber;
				}


			}


			//________________________________________________________________________
			// Play again 

			do {
				System.out.println();
				System.out.print("Shall we play again? [1=yes/ 0=no] ");
				answer2 = input.nextInt();
				if (answer2 != 0 && answer2 !=1) {
					System.out.print("Error: Please enter either 1 or 0 ");
					answer2 = input.nextInt();
				}
			} while (answer2 != 0 && answer2 !=1);
			if (answer2 == 0) {
				System.out.println();
				System.out.println("_____________________________________");
				System.out.println("Thank you for playing a game with me!");
			}
			else {
				System.out.println();
			}


		} while (answer2 == 1);

	}
}
