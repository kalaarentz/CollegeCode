/**
 * This program will use an object of the BankAccount class to manipulate a 
 * savings bank account. The ATMDriver class contains the runATM method which will 
 * take all of they input (using the Scanner object provided as an argument) 
 * and emit all of the output. All of the state for the savings account must be stored
 * in the savings account object that you will create from the BankAccount class. 
 * This means that the ATMDriver class will need to ask the savings account object of 
 * the BankAccount class for the account balance, and to process any updates to that 
 * account via method calls on that object. The ATMDriver class will ask the user for a
 * starting balance and then enter a loop allowing the user to perform on of the 
 * following actions until the user chooses to exit the ATM:
 * 	-Deposit money into the bank account
 * 	-Withdraw money from the bank account
 * 	-Warn the user when they have overdrawn the account after the withdraw
 * 	-Apply interest to the bank account balance (provided in percentage form)
 * 	-Exit the ATM
 *
 * Last Modified: <October 23, 2014>
 * @author <Kala Arentz>
 */

import java.text.DecimalFormat;
import java.util.Scanner;


public class ATMDriver {
	/** No class scoped variables (data members) are allowed */

	/**
	 * The main method, where the program starts
	 * @param args
	 */
	public static void main( String[] args ) {
		Scanner input = new Scanner(System.in);
		ATMDriver atm = new ATMDriver();
		atm.runATM( input );
		// No other Java statements should be in here.
	}

	/**
	 * Constructor for the ATM driver.
	 */
	public ATMDriver() {
		// Nothing to initialize in this class (no data members).
		// No other Java statements should be in here.
	}

	/**
	 * Run the ATM
	 * Note: This is where the program really starts
	 * 
	 * @param input Scanner to use for user input
	 */
	public void runATM( Scanner input ) {
		// Note: You should only use the Scanner provided,
		//       and not create another one for this lab.

		/*
		 * Welcome the user to the ATM
		 */
		BankAccount mySavings;
		double value;
		String answer;
		DecimalFormat df = new DecimalFormat("###,##0.00");


		System.out.println("Welcome to the Penny Saver Community Bank ATM!");

		//Get the Starting Balance
		System.out.print("Starting Balance for Savings: ");
		value = input.nextDouble();
		mySavings = new BankAccount( value );

		//DEBUG Statements
		//System.out.println("DEBUG value: " + value);
		//System.out.println("DEBUG: getBalance " + mySavings.getBalance());

		do {
			System.out.println("------------------------");

			//Display Balance
			System.out.println("The current balance is: $" 
					+ df.format(mySavings.getBalance()));
			
			//Ask what they want to do
			System.out.println("Would you like to (D)eposit money, (W)ithdraw money, "
					+ "Apply (I)nterest, or (E)xit?");
			System.out.print("Please enter one of the following characters (D,W,I,E) ");
			answer = input.next();

			//DEBUG Statements
			//System.out.println("DEBUG: string " + answer);

			//Deposit Money
			if (answer.charAt(0) == 'd' || answer.charAt(0) == 'D'){
				System.out.print("How much would you like to deposit? ");
				value = input.nextDouble();

				mySavings.depositMoney( value );
			}
			//Withdraw Money
			else if (answer.charAt( 0 ) == 'w' || answer.charAt( 0 ) == 'W'){  
				System.out.print("How much would you like to withdrawal? ");
				value = input.nextDouble();

				// When money is over drawn
				boolean over;
				over = mySavings.withdrawMoney(value);
				if (over == false){
					System.out.println("Warning: Your account is overdrawn by $" 
							+ df.format(-1 * mySavings.getBalance()) + "!");
				}
				else {
				}
			}
			//Apply Interest
			else if (answer.charAt( 0 ) == 'i' || answer.charAt( 0 ) == 'I'){
				System.out.print("What is the interest rate? ");
				value = input.nextDouble();

				mySavings.applyInterest( value );
			}

		}while (answer.charAt( 0 ) == 'd' || answer.charAt( 0 ) == 'D'
				|| answer.charAt( 0 ) == 'w' || answer.charAt( 0 ) == 'W'
				|| answer.charAt( 0 ) == 'i' || answer.charAt( 0 ) == 'I'); 

		//Print out final balance when they exit ATM
		System.out.println("------------------------");
		System.out.println("Final Balance: $" + df.format(mySavings.getBalance()));
		System.out.println("Have a great day!");

	}

}
