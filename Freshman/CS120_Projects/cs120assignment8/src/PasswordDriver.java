/**
 * <This program is to help Amy the Admin find users with bad passwords so that 
 * she may notify them of the security risks. This program will search one of her three
 * databases for "bad passwords". "Bad passwords" are defined by the user by the these 
 * following parameters:
 * 		-Uppercase Letters
 * 		-Lowercase Letters
 * 		-Digits
 * 		-non-whitespace symbols(any character not classified by one of the categories above)
 * 
 * This program will only be able to use the Database class once-- so as you read in the 
 * values you should save them in an array for later searching activities. If you try
 * creating more than on database object the program is designed to crash with an error
 * message.
 * 
 * The three files you will be searching through are:
 * 		- SMALL: 44 entries
 * 		- MEDIUM: 2,243 entries
 * 		- LARGE: 220,022 entries>
 *
 * Last Modified: <November 11, 14>
 * @author <Kala Arentz>
 */
import java.text.DecimalFormat;
import java.util.Scanner;


public class PasswordDriver {
	/** No class scoped variables (data members) are allowed */

	/**
	 * The main method, where the program starts
	 * @param args
	 */
	public static void main(String[] args) {
		PasswordDriver d = new PasswordDriver();
		Scanner input = new Scanner(System.in);
		d.runApplication( input );
		// No other Java statements should be in here.
	}

	/**
	 * Constructor for the PasswordDriver.
	 */
	public PasswordDriver() {
		// Nothing to initialize in this class (no data members).
		// No other Java statements should be in here.
	}

	/**
	 * Run the application (this is where the program really starts)
	 * 
	 * @param input Scanner to use for all interaction with the user.
	 */
	public void runApplication( Scanner input ) {
		// Remove the following DEBUG output statement
		//System.out.println("DEBUG: Just here to make sure you can run the program. - Remove me");

		//Variables for use
		int answer = 0, answerUC = 0, answerLC = 0;
		int answerDG = 0, answerSYM = 0, answerLength = 0;
		DecimalFormat df = new DecimalFormat("###,#00");

		// Ask for which database to use (small=0, medium=1, large=2)
		// Make loop to check that the input is correct
		do{
			System.out.print("Which database would you like to search (small=0, "
					+ "medium=1, large=2): ");
			answer = input.nextInt();
			if (answer < 0 || answer > 2){
				System.out.println("Error: Please select 0, 1, or 2");
			}
		} while (answer < 0 || answer > 2);

		//Use the Database class to read all of the date into an array of PasswordEntry Objects
		Database data = new Database(answer);

		//PasswordEntry class needs to separate the username/password combination
		// new array using PasswordEntry
		PasswordEntry[] objects;
		objects = new PasswordEntry[data.getNumTuples()];

		for (int idx = 0; idx < data.getNumTuples(); idx++){

			String strings = data.getNextTuple();

			objects[idx] = new PasswordEntry(strings);
			//System.out.println("Strings DEBUG: " + strings);
		}
		
		/*DEBUG Statement
		 for (int idx = 0; idx < data.getNumTuples(); idx++){
			System.out.println("DEBUG PasswordObjects: " + objects[idx].getEntryAsString());
			
		}
		*/

		// Print out the number of entries for the database selected
		System.out.println();
		System.out.println("There are " + df.format(data.getNumTuples()) + " entries in the "
				+ "username/password database.");
		System.out.println();


		/*
		 * Enter the search parameters for the following 
		 * 	uppercase
		 * 	lowercase
		 * 	digits
		 * 	symbols
		 * 	minimum password length
		 * 	
		 */
		System.out.println("Enter the search parameters below (all values inclusive).");

		//Make loop to check that the input is correct
		//UPPERCASE letters
		do {
			System.out.print('\t');
			System.out.print("At least how many UPPERCASE letters? ");
			answerUC = input.nextInt();
			if (answerUC < 0){
				System.out.println("Error: Please enter a positive number!");
			}
		} while (answerUC < 0);

		//lowercase letters
		do {
			System.out.print('\t');
			System.out.print("At least how many lowercase letters? ");
			answerLC = input.nextInt();
			if (answerLC < 0){
				System.out.println("Error: Please enter a positive number!");
			}
		} while (answerLC < 0);

		//Digits
		do {
			System.out.print('\t');
			System.out.print("At least how many digits? ");
			answerDG = input.nextInt();
			if (answerDG < 0){
				System.out.println("Error: Please enter a positive number!");
			}
		} while (answerDG < 0);

		//At least how many symbols
		do {
			System.out.print('\t');
			System.out.print("At least how many symbols? ");
			answerSYM = input.nextInt();
			if (answerSYM < 0){
				System.out.println("Error: Please enter a positive number!");
			}
		} while (answerSYM < 0);

		//minimum password length
		do {
			System.out.print('\t');
			System.out.print("Minimum password length? ");
			answerLength = input.nextInt();
			if (answerLength < 0){
				System.out.println("Error: Please enter a positive number!");
			}
		} while (answerLength < 0);

		/* 
		 * Search the database with following information
		 * given by the loops
		 * 
		 * uppercase letters
		 * lowercase letter
		 * digits 
		 * symbols
		 * minimum length of the password
		 * 
		 */
		PasswordEntry bad_passwords[];
		
		bad_passwords = findBadPasswords(objects, answerUC, answerLC, 
				answerDG, answerSYM, answerLength);
		
		/*
		 * Display the bad passwords
		 */
		System.out.println();
		
		displayBadPasswords(bad_passwords);
	}

	/**
	 * Search the 'allEntries' array for the bad passwords.
	 * This method must make meaningful use of the validatePassword() method from the
	 * PasswordEntry objects.
	 * 
	 * Hint: You may need to perform the search twice to properly build the array
	 * to return. The first time to count the number of bad passwords - so 
	 * you can initialize the output array. The second time to fill in that
	 * output array.
	 * 
	 * Note: There will be no System.out or Scanner use in this method.
	 * 
	 * @param allEntries All of the PasswordEntry objects from the database
	 * @param num_uc Minimum number of uppercase letters
	 * @param num_lc Minimum number of lowercase letters
	 * @param num_digit Minimum number of digits (0-9)
	 * @param num_sym Minimum number of symbols
	 * @param min_len Minimum length of the password
	 * @return An array of only the PasswordEntry objects that are bad. 
	 */
	private PasswordEntry[] findBadPasswords(PasswordEntry allEntries[],
			int num_uc, int num_lc,
			int num_digit, int num_sym,
			int min_len) {
		// Create an integer that counts number of bad passwords
		int count = 0;
		int check_password;
		// Search the amount of bad username/passwords
		for (int idx = 0; idx < allEntries.length; idx++){
			check_password = allEntries[idx].validatePassword(num_uc, num_lc, num_digit, num_sym);
			
			//Check if password passes the validate password, if not 0 = bad password
			if (check_password != 0 || allEntries[idx].getPassword().length() < min_len ){
				count++;
			}
		}
		
		// Create an PasswordEntry bad password array
		PasswordEntry[] bad_password = new PasswordEntry[count];
		
		// Search the database for bad username/passwords
		int idx2 = 0;
		for (int idx = 0; idx < allEntries.length; idx++){
			check_password = allEntries[idx].validatePassword(num_uc, num_lc, num_digit, num_sym);
			
			if (check_password != 0 || allEntries[idx].getPassword().length() < min_len ){
				bad_password[idx2] = allEntries[idx];
				idx2++;
			}
			
		}
		return bad_password;
	}


	/**
	 * Display all of the bad passwords that were found. The array provided contains only
	 * those PasswordEntry objects found in the prior search.
	 * 
	 * This method will first display the number of entries found:
	 *   Found NUMBER_FOUND bad password(s)!
	 *   
	 * If the number of bad passwords is 0, then only the message above is displayed.
	 * 
	 * If the number of bad passwords is less than or equal to 20, then all
	 * of the entries are displayed prefixed with the lines:
	 *   All entries
	 *   ------------------------------------
	 * 
	 * If the number of bad passwords is greater than 20, then only the first 10 and 
	 * last 10 entries are displayed.
	 * The first 10 entries are displayed prefixed with the lines:
	 *  First 10 entries
	 *  ------------------------------------
	 * The last 10 entries are displayed prefixed with the lines:
	 *  Last 10 entries
	 *  ------------------------------------
	 * 
	 * @param foundEntries The PasswordEntry objects found during a prior search
	 */
	private void displayBadPasswords(PasswordEntry foundEntries[]) {
		/*DEBUG STATEMENT
		for (int idx = 0; idx < foundEntries.length; idx++){
			System.out.println("BadPasswords DEBUG: " + foundEntries[idx].getEntryAsString());
		}
		*/
		
		// Display the number found of bad passwords
		System.out.println("Found " + foundEntries.length + " bad passwords(s)!");
		System.out.println();
		
		// Selection logic for the following number of bad passwords
			// Number found to be less then 20, display all 20
		if (foundEntries.length <= 20 && foundEntries.length > 0){
			System.out.println("All entries");
			System.out.println("------------------------------------");
			for( int idx = 0; idx < foundEntries.length; idx++){
				System.out.println(foundEntries[idx].getEntryAsString());
			}
		}
			// Number found greater then 20, display the first 10, last 10
		if (foundEntries.length > 20){
			System.out.println("First 10 Entries");
			System.out.println("------------------------------------");
			
			for (int idx = 0; idx < 10; idx++){
				System.out.println(foundEntries[idx].getEntryAsString());
			}
			
			System.out.println();
			System.out.println("Last 10 Entries");
			System.out.println("------------------------------------");
			
			for (int idx = foundEntries.length - 10; idx < foundEntries.length; idx++){
				System.out.println(foundEntries[idx].getEntryAsString());
			}
		}
	}

}
