/*
 * Write a program that converts military time into standard time, then converts 
 * the standard time into an English phrase for that time. Military time represent 
 * hours on a 24-hour clock (values from 0 to 23 inclusive). Standard time represents
 * time on a 12-hour clock (values form 1 to 12, inclusive). Your program will take as 
 * input the hours and minutes in military time. It will then convert those values to 
 * standard time, and display the standard time to the console. After displaying the 
 * standard time to the console, your program will then convert the standard time to an 
 * English phrase representing that time. 
 * 
 * <Date Last Modified October 9, 2014>
 * 
 * <Author: Kala Arentz>
 * 
 */
import java.text.DecimalFormat;
import java.util.Scanner;

public class EnglishStandardTime {

	public static void main(String[] args) {
		// Main work of the program
		int hour, minutes;
		String timeOfDay, englishHour, englishMinute;
		Scanner input = new Scanner(System.in);
		DecimalFormat df;
		df = new DecimalFormat("00");

		hour = getHour(input);
		minutes = getMinute(input);

		// Show the time of day (am versus pm)
		timeOfDay = "";
		if ( hour <= 11) {
			timeOfDay= "am";
		}
		else {
			timeOfDay= "pm";
		}

		//this will return the standard time for hours
		if (hour == 1 || hour == 13) {
			hour = 1;
		}
		else if (hour == 2 || hour == 14) {
			hour = 2;
		}
		else if (hour == 3 || hour == 15) {
			hour = 3;
		}
		else if (hour == 4 || hour == 16) {
			hour = 4;
		}
		else if (hour == 5 || hour == 17) {
			hour = 5;
		}
		else if (hour == 6 || hour == 18) {
			hour = 6;
		}
		else if (hour == 7 || hour == 19) {
			hour = 7;
		}
		else if (hour == 8 || hour == 20) {
			hour = 8;
		}
		else if (hour == 9 || hour == 21) {
			hour = 9;
		}
		else if (hour == 10 || hour == 22) {
			hour = 10;
		}
		else if (hour == 11 || hour == 23) {
			hour = 11;
		}
		else if (hour == 12 || hour == 0) {
			hour = 12;
		}

		//System.out.println("DEBUG: zero " + convertToStringBetweenZeroAndNineteen(0));

		englishHour = convertHourToEnglish(hour); // English word for hour
		englishMinute = convertMinutesToEnglish(minutes); // English word for minutes

		/* Special cases of Noon, Midnight, and minutes are zero 
		 * for any other hour beside Noon/Midnight*/
		if (hour == 12 && minutes == 0 && timeOfDay == "am") {
			System.out.print(hour + ":" + df.format(minutes) + " " + timeOfDay + " is Midnight");
		}
		else if (hour == 12 && minutes == 0 && timeOfDay == "pm") {
			System.out.print(hour + ":" + df.format(minutes) + " " + timeOfDay + " is Noon");
		}
		else if (minutes == 0) { 
			System.out.print(hour + ":" + df.format(minutes) + " " + timeOfDay + " is " + 
					englishHour + " " + timeOfDay);
		}
		else if (minutes > 0 && minutes <= 9) {
			System.out.print(hour + ":" + df.format(minutes) + " " + timeOfDay + 
					" is " + englishHour + " oh-" +englishMinute + " " + timeOfDay);
		}
		else {
			System.out.print(hour + ":" + df.format(minutes) + " " + timeOfDay + 
					" is " + englishHour + " " +englishMinute + " " + timeOfDay);
		}

	}

	/*
	 * Get the hour (in military time) from the user.
	 * If the hour is out-of-bound, then an error is displayed and the user is 
	 * prompted again until they enter a valid value.
	 * 
	 * @param input Scanner object to use for input
	 * @return Hour between o and 23, inclusive
	 * 
	 */

	private static int getHour(Scanner input)  {
		//positive numbers between 0 - 23 inclusive
		int x; // variable for the input for hours
		do {
			System.out.print("Enter Hour" + '\t' + ": ");
			x = input.nextInt();
			if (x < 0 ) {
				System.out.println("Error: Please enter a value greater then 0 ");
				//System.out.print("2Enter Hour" + '\t' + ": ");
				//x =  input.nextInt();
			}
			else if (x > 23) {
				System.out.println("Error: Please enter a value less than 23 ");
				//System.out.print("3Enter Hour" + '\t' + ": ");
				//x =  input.nextInt();
			}
		} while (x < 0 || x > 23);
		return x;
	}

	/*
	 * Get the minute(s) from the user.
	 * If the minute is out-of-bounds, then an error is displayed and 
	 * the user is prompted again until they enter a valid value.
	 * 
	 * @param input Scanner object to use for input
	 * @return Minutes between 0 and 59, inclusive
	 * 
	 */

	private static int getMinute(Scanner input) {
		int x; // variable for the input
		do {
			System.out.print("Enter Minute" + '\t' + ": ");
			x = input.nextInt();
			if (x < 0 ) {
				System.out.println("Error: Please enter a value greater then 0 ");
				//System.out.print("Enter Minute" + '\t' + ": ");
				//x =  input.nextInt();
			}
			else if (x > 59) {
				System.out.println("Error: Please enter a value less than 60 ");
				//System.out.print("Enter Minute" + '\t' + ": ");
				//x =  input.nextInt();
			}
		} while (x < 0 || x > 59);
		return x;
	}

	/*
	 * Convert the hour to an English word.
	 * 
	 * @param hour Hour value to convert
	 * @return A String representing the English word for the value provided.
	 * 
	 */

	private static String convertHourToEnglish(int hour) {
		//return the english words from the method convertToStringBetweenZeroNineteen
		String x;
		x = "";
		x = convertToStringBetweenZeroAndNineteen(hour);
		return x;
	}

	/*
	 * Convert the minute(s) to an English phrase.
	 * 
	 * @param minutes Minute(s) value to convert
	 * @return a string representing the English phrase for the value provided.
	 * 
	 */

	private static String convertMinutesToEnglish(int minutes) {
		/* return the English words from the method convertToStringBetweenZeroNineteen, 
		 * and convertToStringBetweenZeroAndFiftyNine */
		String x; // the two strings for the tenths and ones
		x= "";

		x= convertToStringBetweenZeroAndFiftyNine(minutes);
		return x ;

	}

	/*
	 * Convert the number provided to an English string representing that number.
	 * We assume that the number is between 0 and 19, inclusive.
	 * 
	 * @param number The number to convert to a String
	 * @return String representation of that number.
	 * 
	 */

	private static String convertToStringBetweenZeroAndNineteen(int number) {
		//The numbers are english words between 0-19
		String x;
		x = "";
		if (number ==  0) {
			x = "zero";
			return x;
		}
		else if (number == 1) {
			x = "one";
			return x;
		}
		else if (number == 2) {
			x = "two";
			return x;
		}
		else if (number == 3) {
			x = "three";
			return x;
		}
		else if (number == 4) {
			x = "four";
			return x;
		}
		else if (number == 5) {
			x = "five";
			return x;
		}
		else if (number == 6) {
			x = "six";
			return x;
		}
		else if (number == 7) {
			x = "seven";
			return x;
		}
		else if (number == 8) {
			x = "eight";
			return x;
		}
		else if (number == 9) {
			x = "nine";
			return x;
		}
		else if (number == 10) {
			x = "ten";
			return x;
		}
		else if (number == 11) {
			x = "eleven";
			return x;
		}
		else if (number == 12) {
			x = "twelve";
			return x;
		}
		else if (number == 13) {
			x = "thirteen";
			return x;
		}
		else if (number == 14) {
			x = "fourteen";
			return x;
		}
		else if (number == 15) {
			x = "fifteen";
			return x;
		}
		else if (number == 16) {
			x = "sixteen";
			return x;
		}
		else if (number == 17) {
			x = "seventeen";
			return x;
		}
		else if (number == 18) {
			x = "eighteen";
			return x;
		}
		else if (number == 19) {
			x = "nineteen";
			return x;
		}
		return x;

	}

	/*
	 * Convert the number provided to an English String representing the number. 
	 * We assume that the number is between 0 and 59, inclusive.
	 * Us the converToStringBetweenZeroAndNineteen() method to help you write this method.
	 * 
	 * @param number The number to convert to a String
	 * @return String representation of that number
	 * 
	 */

	private static String convertToStringBetweenZeroAndFiftyNine(int number) {
		//converting numbers to English words
		String x;
		x = "";

		if (number >= 0 && number <= 19) {
			x = convertToStringBetweenZeroAndNineteen(number);
			return x;
		}
		if (number == 20) {
			x = "twenty";
			return x;
		}
		if (number == 30) {
			x = "thirty";
			return x;
		}
		if (number == 40) {
			x = "forty";
			return x;
		}
		if (number == 50) {
			x = "fifty";
			return x;
		}
		if (number > 20 && number <= 29) {
			if ((number - 20) >= 1 && (number - 20) <= 9) {
				x = convertToStringBetweenZeroAndNineteen(number-20);
				return "twenty-" + x;
			}
		}
		if (number > 30 && number <= 39) {
			if ((number - 30) >= 1 && (number - 30) <= 9) {
				x = convertToStringBetweenZeroAndNineteen(number-30);
				return "thirty-" + x;
			}
		}
		if (number > 40 && number <= 49) {
			if ((number - 40) >= 1 && (number - 40) <= 9) {
				x = convertToStringBetweenZeroAndNineteen(number-40);
				return "forty-" + x;
			}
		}
		if (number > 50 && number <= 59) {
			if ((number - 50) >= 1 && (number - 50) <= 9) {
				x = convertToStringBetweenZeroAndNineteen(number-50);
				return "fifty-" + x;
			}
		}
		return "" + x;
	}
}
