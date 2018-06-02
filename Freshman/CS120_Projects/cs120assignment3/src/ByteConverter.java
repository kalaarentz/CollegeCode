/*
 * This program will convert a given number of bytes into a more human
 * readable format. The program will convert it into one of the following: 
 * Bytes, Kilobytes, Megabytes, Gigabytes,Terabytes.  
 * 
 * Date Last Modified <September 26, 2014>
 * 
 * Author <Kala Arentz>
 * 
 */

import java.util.Scanner;
import java.text.DecimalFormat;

public class ByteConverter {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		long number;
		DecimalFormat df;
		df = new DecimalFormat("#0.00");
			
		// User inputs the number of bytes
		System.out.print("Enter Number of Bytes: ");
		number = input.nextLong();
		//System.out.println("DEBUG: " + number);
		
		
		// Selection Statements
		if (number >= Math.pow(2, 1) && (number < Math.pow(2,10))) { //--> Statement for Byte
			System.out.print(number + " Bytes is " + df.format(number/1.0) + " B");
		}
		else if (number >= Math.pow(2,10) && (number < Math.pow(2, 20))) { //--> Statement for Kilobyte
			System.out.print(number + " Bytes is " + df.format(number/(Math.pow(2,10))) + " KB");
		}
		else if (number >= Math.pow(2, 20) && (number < Math.pow(2, 30))) { //--> Statement for Megabyte
			System.out.print(number + " Bytes is " + df.format(number/(Math.pow(2,20))) + " MB");
		}
		else if (number >= Math.pow(2, 30) && (number < Math.pow(2, 40))) { //--> Statement for Gigabyte
			System.out.print(number + " Bytes is " + df.format(number/(Math.pow(2,30))) + " GB");
		}
		else if (number >= Math.pow(2, 40)) { //--> Statement for Terabyte
			System.out.print(number + " Bytes is " + df.format(number/(Math.pow(2,40))) + " TB");
		}
		

	}

}
