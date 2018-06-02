/*
 *  This program is a simple cash register for M & P's Cookie Factory. 
 *  The program takes as input the number of cookies desired and 
 *  then the program displays a receipt for the sale including
 *  Appropriate sales tax (5.50%). The program will then take as input
 *  the amount of cash provided, and display the change.
 *  
 *  Date Last Modified <September 30, 2014>
 *  
 *  Author <Kala Arentz>
 *  
 */

import java.text.DecimalFormat;
import java.util.Scanner;
		
public class CookieTruck {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int cookies, x, y;
		double subTotal, total, taxes, cashPaid;
		DecimalFormat df;
		df = new DecimalFormat("#,##0.00");		
		
		// Display Welcome Sentence
		System.out.println("Welcom to M & P's Cookies on the Move!");
		
		// User inputs the amount of cookies they want
		System.out.print("How many cookies would you like? ");
		cookies = input.nextInt();
		//System.out.println("DEBUGG: " + cookies);
		
		//Variables for selection statements
		x = cookies - ((cookies/48) *48);
		//System.out.println("DEBUGG: x" + x);
		y = x - ((x/12) * 12);
		//System.out.println("DEBUGG: y" + y);
		
		// Selection Statements
		if ((cookies >= 48) && (x/12 >=1) && (y >=1)) { // cookies are > 48, with dozens, and singles
			System.out.println(cookies/48 + " Cases at $10.00 per case"  + " : $ " + df.format((cookies/48) * 10.00));
			System.out.println(x/12 + " Dozens at $2.75 per dozen"  + ": $ " + df.format((x/12) * 2.75));
			System.out.println(y + " Singles at $0.25 each" + "   : $ " + df.format(y * .25));	
			subTotal = ((cookies/48) * 10.00) + ((x/12) * 2.75) + (y * 0.25);// Determine subtotal for the amount of cookies
			//System.out.println("DEBUG: " + subTotal);
			System.out.println('\t' + "          Subtotal : $ " + df.format(subTotal));
			taxes = Math.round(((subTotal * 0.055) * 100))/100.00;	// taxes equation
			System.out.println('\t' + "     Taxes (5.50%) : $ " + df.format((taxes)));// Determine the taxes for the amount of cookies
			System.out.println('\t' + "             Total : $ " + df.format(((subTotal + (taxes)))));// Determine the total amount for (taxes/subtotal)
			System.out.print('\n');
			System.out.print('\t' + "         Cash Paid : $ ");// User inputs the Cash paid
			cashPaid = input.nextDouble();
			//System.out.println("DEBUG: " + cashPaid + " cash paid");
			System.out.println('\t' + "            Change : $ " + df.format((cashPaid -((subTotal + (taxes)))))); // Determine the change
			System.out.print('\n');
			System.out.println("Thank You!");// Display Thank You message
		}
		
		else if ((cookies/48) >= 1 && (x == 0)) {
			System.out.println(cookies/48 + " Cases at $10.00 per case"  + " : $ " + df.format((cookies/48) * 10.00));
			subTotal = ((cookies/48) * 10.00) + ((x/12) * 2.75) + (y * 0.25);// Determine subtotal for the amount of cookies
			//System.out.println("DEBUG: " + subTotal);
			System.out.println('\t' + "          Subtotal : $ " + df.format(subTotal));
			taxes = Math.round(((subTotal * 0.055) * 100))/100.00;	// taxes equation
			System.out.println('\t' + "     Taxes (5.50%) : $ " + df.format((taxes)));// Determine the taxes for the amount of cookies
			System.out.println('\t' + "             Total : $ " + df.format(((subTotal + (taxes)))));// Determine the total amount for (taxes/subtotal)
			System.out.print('\n');
			System.out.print('\t' + "         Cash Paid : $ ");// User inputs the Cash paid
			cashPaid = input.nextDouble();
			//System.out.println("DEBUG: " + cashPaid + " cash paid");
			System.out.println('\t' + "            Change : $ " + df.format((cashPaid -((subTotal + (taxes)))))); // Determine the change
			System.out.print('\n');
			System.out.println("Thank You!");// Display Thank You message
		}
		
		else if ((cookies/48) >= 1 && (x/12 >= 1)) {// cookies contain cases, and only dozens
			System.out.println(cookies/48 + " Cases at $10.00 per case"  + " : $ " + df.format((cookies/48) * 10.00));
			System.out.println(x/12 + " Dozens at $2.75 per dozen"  + ": $ " + df.format((x/12) * 2.75));
			subTotal = ((cookies/48) * 10.00) + ((x/12) * 2.75) + (y * 0.25);// Determine subtotal for the amount of cookies
			//System.out.println("DEBUG: " + subTotal);
			System.out.println('\t' + "          Subtotal : $ " + df.format(subTotal));
			taxes = Math.round(((subTotal * 0.055) * 100))/100.00;	// taxes equation
			System.out.println('\t' + "     Taxes (5.50%) : $ " + df.format((taxes)));// Determine the taxes for the amount of cookies
			System.out.println('\t' + "             Total : $ " + df.format(((subTotal + (taxes)))));// Determine the total amount for (taxes/subtotal)
			System.out.print('\n');
			System.out.print('\t' + "         Cash Paid : $ ");// User inputs the Cash paid
			cashPaid = input.nextDouble();
			//System.out.println("DEBUG: " + cashPaid + " cash paid");
			System.out.println('\t' + "            Change : $ " + df.format((cashPaid -((subTotal + (taxes)))))); // Determine the change
			System.out.print('\n');
			System.out.println("Thank You!");// Display Thank You message
		}
		
		else if ((cookies < 48) && (x/12 >=1) && (y >=1)) {// cookies less then 48, make dozens and singles
			System.out.println(x/12 + " Dozens at $2.75 per dozen"  + ": $ " + df.format((x/12) * 2.75));
			System.out.println(y + " Singles at $0.25 each" + "   : $ " + df.format(y * .25));	
			subTotal = ((cookies/48) * 10.00) + ((x/12) * 2.75) + (y * 0.25);// Determine subtotal for the amount of cookies
			//System.out.println("DEBUG: " + subTotal);
			System.out.println('\t' + "          Subtotal : $ " + df.format(subTotal));
			taxes = Math.round(((subTotal * 0.055) * 100))/100.00;	// taxes equation
			System.out.println('\t' + "     Taxes (5.50%) : $ " + df.format((taxes)));// Determine the taxes for the amount of cookies
			System.out.println('\t' + "             Total : $ " + df.format(((subTotal + (taxes)))));// Determine the total amount for (taxes/subtotal)
			System.out.print('\n');
			System.out.print('\t' + "         Cash Paid : $ ");// User inputs the Cash paid
			cashPaid = input.nextDouble();
			//System.out.println("DEBUG: " + cashPaid + " cash paid");
			System.out.println('\t' + "            Change : $ " + df.format((cashPaid -((subTotal + (taxes)))))); // Determine the change
			System.out.print('\n');
			System.out.println("Thank You!");// Display Thank You message
		}
		else if ((cookies > 48) && (x/12 == 0) && (y>=1)) {// cookies that make a case, but not dozens
			System.out.println(cookies/48 + " Cases at $10.00 per case"  + " : $ " + df.format((cookies/48) * 10.00));
			System.out.println(y + " Singles at $0.25 each" + "   : $ " + df.format(y * .25));
			subTotal = ((cookies/48) * 10.00) + ((x/12) * 2.75) + (y * 0.25);// Determine subtotal for the amount of cookies
			//System.out.println("DEBUG: " + subTotal);
			System.out.println('\t' + "          Subtotal : $ " + df.format(subTotal));
			taxes = Math.round(((subTotal * 0.055) * 100))/100.00;	// taxes equation
			System.out.println('\t' + "     Taxes (5.50%) : $ " + df.format((taxes)));// Determine the taxes for the amount of cookies
			System.out.println('\t' + "             Total : $ " + df.format(((subTotal + (taxes)))));// Determine the total amount for (taxes/subtotal)
			System.out.print('\n');
			System.out.print('\t' + "         Cash Paid : $ ");// User inputs the Cash paid
			cashPaid = input.nextDouble();
			//System.out.println("DEBUG: " + cashPaid + " cash paid");
			System.out.println('\t' + "            Change : $ " + df.format((cashPaid -((subTotal + (taxes)))))); // Determine the change
			System.out.print('\n');
			System.out.println("Thank You!"); // Display Thank You message
		}

		else if ((cookies == 36) || (cookies == 24) || (cookies== 12)) { // cookies equal 36 or 24 or 12
			System.out.println(x/12 + " Dozens at $2.75 per dozen"  + ": $ " + df.format((x/12) * 2.75));		
			subTotal = ((cookies/48) * 10.00) + ((x/12) * 2.75) + (y * 0.25);// Determine subtotal for the amount of cookies
			//System.out.println("DEBUG: " + subTotal);
			System.out.println('\t' + "          Subtotal : $ " + df.format(subTotal));
			taxes = Math.round(((subTotal * 0.055) * 100))/100.00;	// taxes equation
			System.out.println('\t' + "     Taxes (5.50%) : $ " + df.format((taxes)));// Determine the taxes for the amount of cookies
			System.out.println('\t' + "             Total : $ " + df.format(((subTotal + (taxes)))));// Determine the total amount for (taxes/subtotal)
			System.out.print('\n');
			System.out.print('\t' + "         Cash Paid : $ ");// User inputs the Cash paid
			cashPaid = input.nextDouble();
			//System.out.println("DEBUG: " + cashPaid + " cash paid");
			System.out.println('\t' + "            Change : $ " + df.format((cashPaid -((subTotal + (taxes)))))); // Determine the change
			System.out.print('\n');
			System.out.println("Thank You!");// Display Thank You message
		}

		else if ((cookies < 12)) { //cookies < 12
			System.out.println(y + " Singles at $0.25 each" + '\t' + "   : $ " + df.format(y * .25));
			subTotal = ((cookies/48) * 10.00) + ((x/12) * 2.75) + (y * 0.25); // Determine subtotal for the amount of cookies
			//System.out.println("DEBUG: " + subTotal);
			System.out.println('\t' + "          Subtotal : $ " + df.format(subTotal));
			taxes = Math.round(((subTotal * 0.055) * 100))/100.00;	// taxes equation
			System.out.println('\t' + "     Taxes (5.50%) : $ " + df.format((taxes)));// Determine the taxes for the amount of cookies
			System.out.println('\t' + "             Total : $ " + df.format(((subTotal + (taxes)))));// Determine the total amount for (taxes/subtotal)
			System.out.print('\n');
			System.out.print('\t' + "         Cash Paid : $ ");// User inputs the Cash paid
			cashPaid = input.nextDouble();
			//System.out.println("DEBUG: " + cashPaid + " cash paid");
			System.out.println('\t' + "        Change    : $ " + df.format((cashPaid -((subTotal + ((taxes))))))); // Determine the change
			System.out.print('\n');
			System.out.println("Thank You!");// Display Thank You message
		}
	}
}

		