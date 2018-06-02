/*
 * This program will identify a given year as either a leap year or
 * not a leap year.
 * 
 * Date Last Modified <Sept. 24, 2014>
 * 
 * Author <Kala Arentz> 
 * 
 */
import java.util.Scanner;

public class LeapYear {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int year;

		//Ask user for to enter year
		System.out.print("Enter Year: ");
		year = input.nextInt();
		//System.out.println("DEBUG: " + year);

		//Decide if it is a leap year or not
		if ((year%4 == 0) && (year%100 !=0)) { //--> leap year if is divisible by 4, not 100
			System.out.print("The Year " + year + " is a leap year!");
		}
		else if (((year%4 == 0) && (year%100 == 0)) && (year%400 == 0)) {//--> leap year if is divisible by 4, 100, and 400
			System.out.print("The Year " + year + " is a leap year!");
		}
		else if ((year%4 != 0) || (year%400 != 0)) {//--> not a leap year if not divisible by 4, 100, but not 400
			System.out.println("The Year " + year + " is not a leap year!");
		}


	}

}
