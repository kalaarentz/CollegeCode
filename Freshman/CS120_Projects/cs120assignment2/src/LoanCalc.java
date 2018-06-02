/*
 * This program will compute information for student loan calculator.
 * It will also need to compute monthly payments, total amount paid, 
 * total interest paid for given loan amount, annual interest rate and 
 * loan period in years.
 * 
 *  Date Last Modified <September 18, 2014>
 *  
 *  Author <Kala Arentz>
 *  
 */
import java.util.Scanner;
import java.text.DecimalFormat;

public class LoanCalc {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		double loanAmount, interestRate, years, formulaInterestRate,  
			number, monthlyPayments, totalPaid, interestPaid ;
		DecimalFormat df, df1, df2;
		df = new DecimalFormat ("#,##0.00");
		df1 = new DecimalFormat ("0.00");
		df2 = new DecimalFormat ("0.0");
		
		
		// User enters loan amount
		System.out.print("Loan Amount" + '\t' + '\t' + ": " );
		loanAmount = input.nextDouble();
		//System.out.println("DEBUG: " + loanAmount);
		
		// User enters annual interest rate
		System.out.print("Annual interest rate (%): ");
		interestRate = input.nextDouble();
		//System.out.println("DEBUG: " + interestRate);
		
		// User enters loan period or years
		System.out.print("Loan period (years)" + '\t' + ": ");
		years = input.nextDouble();
		//System.out.println("DEBUG: " + years);
		
		// Program computes loan amount in correct decimal format
		System.out.print('\n');
		System.out.print("Loan Amount : $" + df.format(loanAmount));
		
		// Program compute interest rate in correct decimal format
		System.out.print('\n');
		System.out.print("Annual Rate : " 
				+ df1.format(interestRate) +"%");
		
		// Program computes years in correct decimal format
		System.out.print('\n');
		System.out.print("Loan Period : " 
				+ df2.format(years) +" years");
		
		// Program computes monthly payment in money decimal format
		number = years * 12;
		formulaInterestRate = (interestRate/12)/100;
		monthlyPayments = (loanAmount * 
				formulaInterestRate)/(1 - Math.pow(1/(1 + 
						formulaInterestRate),number));
						
		System.out.print('\n');
		System.out.print('\n');
		System.out.print("Monthly payment is $" + 
				df.format(monthlyPayments)); 
		
		// Program computes total paid in money decimal format
		System.out.print('\n');
		totalPaid = number * monthlyPayments;
		System.out.print("Total paid is $" + df.format(totalPaid));
		
		// Program computes total interest paid in money decimal format
		System.out.print('\n');
		interestPaid = totalPaid - loanAmount;
		System.out.println("Total interest paid is $" + df.format(interestPaid));
		

	}

}
