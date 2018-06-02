/* 
 * THis program will ask the user for some information to compute the 
 * compound interest formula for investments.
 * 
 * Date Last Modified <September 18, 2014>
 * 
 * Author <Kala Arentz>
 * 
 */
import java.util.Scanner;
import java.text.DecimalFormat;

 
public class Savings {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in); 
		double principal, interestRate, years, answer ;
		DecimalFormat df1;
		DecimalFormat df2;
		DecimalFormat df3;
		df1 = new DecimalFormat ("0.000#");
		df2 = new DecimalFormat ("#,##0.00");
		df3 = new DecimalFormat ("0.0");
		
		
		// Enter the amount invested
		System.out.print("Enter the principal amount invested: ");
		principal = input.nextDouble();
				
		// Enter the interest rate
		System.out.print("Enter the interest rate %: ");
		interestRate = input.nextDouble();
		//System.out.println("DEBUG:" + df1.format(interestRate));
		
		// Enter the number of years
		System.out.print("Enter the number of years: ");
		years = input.nextDouble();
		
		
		// Write the formula
		answer = principal *(Math.pow((1 + interestRate/100), years));
		
		
		// Display the final amount after the number of the years
		System.out.print("The final amount after " + df3.format(years) + " "
				+ "years will be $" + df2.format(answer) + "!");
	
	
		
		
	}

}
