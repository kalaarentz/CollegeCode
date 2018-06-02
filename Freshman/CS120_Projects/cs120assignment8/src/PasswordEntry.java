/**
 * <This is the supporting class that will be used by the main driver class PasswordDriver.
 * This program will read in one Database obeject that will read the data into an array
 * of PasswordEntry objects. The Password objects represent a single username/password 
 * combination from the Database. The PasswordEntry will need to seperate the username 
 * from the password provided by the getNextTuple method.>
 *
 * Last Modified: <November 11, 2014 >
 * @author <Kala Arentz>
 */

public class PasswordEntry {
	/** private string for username*/
	private String userName;

	/** private string for password*/
	private String passWord;

	/**
	 * PasswordEntry constructor
	 * 
	 * The tuple provided should be from the Database service and
	 * will be of the form:
	 *    USERNAME    PASSWORD
	 * Where the USERNAME and PASSWORD are separated by some number of space characters (' ').
	 * 
	 * Your first task will be to separate the two values, so you can use them later.
	 * 
	 * @param tuple String containing the username and password separated by spaces
	 */
	public PasswordEntry(String tuple) {
		// Strings for username/passwords (make them private variables to be used)
		String username = "";
		String password = "";
		int count = 0;
		int end_username = 0;


		// get the username/password from the Database public method of getNextTuple
		for (int idx = 0; idx < tuple.length(); idx++){
			char hold = tuple.charAt(idx);

			// count the number of space characters ' '
			if (hold == ' '){
				count++;
			}
			// create the username string by using substring
			if (count == 1){
				end_username = idx;
				username = tuple.substring(0, idx);
			}

		}
		// create the password string by using substring
		password = tuple.substring(end_username + count, tuple.length());

		userName = username;
		passWord = password;
	}

	/**
	 * Accessor for the username
	 * 
	 * @return Return the username provided in the constructor
	 */
	public String getUsername() {
		// return the username
		return userName;
	}

	/**
	 * Accessor for the password
	 * 
	 * @return Return the password provided in the constructor
	 */
	public String getPassword() {
		// return the password
		return passWord;
	}

	/**
	 * Get the PasswordEntry as a string of the following format:
	 *   USERNAME : PASSWORD
	 * With a single space after the USERNAME, a colon, and a single space before
	 * the PASSWORD.
	 * 
	 * @return The properly formatted string containing the username and password
	 */
	public String getEntryAsString() {
		// correct formation of the username and password
		String correctEntry;
		correctEntry = userName + " : " + passWord;

		return correctEntry;
	}

	/**
	 * Validate the password against the provided parameters
	 * <pre>
	 * Return values:
	 *   0 = Satisfies the validation criteria
	 *   
	 *  -1 = If only not enough uppercase letters
	 *  -2 = If only not enough lowercase letters
	 *  -4 = If only not enough digits
	 *  -8 = If only not enough symbols
	 * 
	 *  -3 = If only not enough uppercase (-1) and lowercase (-2) letters 
	 *
	 *  -5 = If only not enough uppercase (-1) and digits (-4)
	 *  -6 = If only not enough lowercase (-2) and digits (-4)
	 *  -7 = If only not enough uppercase (-1), lowercase (-2), and digits (-4)
	 *
	 *  -9 = If only not enough uppercase (-1) and symbols (-8)
	 * -10 = If only not enough lowercase (-2) and symbols (-8)
	 * -11 = If only not enough uppercase (-1), lowercase (-2), and symbols (-8)
	 * -12 = If only not enough digits (-4) and symbols (-8) 
	 * -13 = If only not enough uppercase (-1), digits (-4), and symbols (-8) 
	 * -14 = If only not enough lowercase (-2), digits (-4), and symbols (-8) 
	 * -15 = If only not enough uppercase (-1), lowercase (-2), digits (-4), and symbols (-8) 
	 * 
	 * </pre>
	 * 
	 * @param num_uc Minimum number of uppercase letters
	 * @param num_lc Minimum number of lowercase letters
	 * @param num_digit Minimum number of digits (0-9)
	 * @param num_sym Minimum number of symbols
	 * @return 0 if the password satisfies the criteria, negative value (noted above) if it does not satisfy the criteria.
	 */
	public int validatePassword(int num_uc, int num_lc, int num_digit, int num_sym) {
		// create validate variable
		int validate_number;
		//Counters for how many of these symbols are in the password
		int numberOfuc_letters = 0, numberOflc_letters = 0;
		int numberOfDigits = 0, numberOfSymbols = 0;
		// the variables that will be added up to become the validate variable
		int validateUC = 0, validateLC = 0, validateDG = 0, validateSYM = 0;
		
		String password = this.getPassword();
		char hold;


		// search the password for any of parameters
		for ( int idx = 0; idx < password.length(); idx++){
			hold = password.charAt(idx);

			if ( 'A' <= hold && hold <= 'Z'){
				numberOfuc_letters++;
			}
			else if ( 'a' <= hold && hold <= 'z'){
				numberOflc_letters++;
			}
			else if ( '0' <= hold && hold <= '9'){
				numberOfDigits++;
			}
			else {
				numberOfSymbols++;
			}
		}

		if (numberOfuc_letters < num_uc){
			validateUC = -1;
		}
		else if (numberOflc_letters < num_lc){
			validateLC = -2;
		}
		else if (numberOfDigits < num_digit){
			validateDG = -4;
		}
		else if (numberOfSymbols < num_sym){
			validateSYM = -8;
		}
		// add up the validate variable to see what the answer 
		validate_number = validateUC + validateLC + validateDG + validateSYM;
		
		return validate_number;
	}

}
