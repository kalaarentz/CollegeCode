/**
 * <This program is provided to support the StringDriver class. Each method will
 * fully support the StringDriver for specific tasks. This program will not us the 
 * Scanner or System.out. In addition, there will be no modifications to the method 
 * signatures in the template. >
 *
 * Last Modified: <October 31, 2014>
 * @author <Kala Arentz>
 */
public class StringAnalysis {
	// TODO Add -only- the necessary class scoped variables here (class attributes)
	private String word;

	/**
	 * Constructor of the analysis object
	 * 
	 * @param phrase The String to analyze
	 */
	public StringAnalysis( String phrase ) {
		// Set up the phrase that the user inputed
		word = phrase;
	}

	/**
	 * Access the original String provided to the constructor
	 * 
	 * @return the original string
	 */
	public String getOriginalString() {
		// return the word the user inputed
		return word;
	}

	/**
	 * Count the number of vowels (a, e, i, o, u, y) in the word.
	 * 
	 * @return Number of vowels in the word.
	 */
	public int getNumberOfVowels() {
		// figure out the number of vowels in the word
		int count;
		count = 0;
		char hold;
		String word;

		word = this.getOriginalString();

		for (int idx = 0; idx < word.length(); idx++){
			hold = word.charAt(idx);
			if (hold == 'A' || hold == 'E' || hold == 'I' ||
					hold == 'O' || hold == 'U' || hold == 'Y'
					|| hold == 'a' || hold == 'e' || hold == 'i'
					|| hold == 'o' || hold == 'u' || hold == 'y') {
				count++;
			}
		}

		return count;
	}

	/**
	 * Create a new string with all of the vowels removed.
	 * 
	 * @return The new string with the vowels removed.
	 */
	public String removeAllVowels() {
		// remove the vowels
		String newWord;
		newWord = "";
		char hold;
		word = this.getOriginalString();

		for (int idx = 0; idx < word.length(); idx++) {
			hold = word.charAt(idx);
			if (hold != 'A' && hold != 'E' && hold != 'I' &&
					hold != 'O' && hold != 'U' && hold != 'Y'
					&& hold != 'a' && hold != 'e' && hold != 'i'
					&& hold != '0' && hold != 'u' && hold != 'y') {
				newWord = newWord + hold;
			}
		}
		return newWord;

	}

	/**
	 * Replace all of the vowels with the character specified as a parameter
	 * 
	 * @param c Character used to replace the vowels
	 * @return A new String with the vowels replaced with the given characters
	 */
	public String replaceAllVowelsWith(char c) {
		// replace variables with the character provided for by the user
		String newWord;
		newWord = "";
		char hold;
		word = this.getOriginalString();
		newWord = word;

		for (int idx = 0; idx < newWord.length(); idx++) {
			hold = newWord.charAt(idx);
			if (hold == 'A' || hold == 'E' || hold == 'I' ||
					hold == 'O' || hold == 'U' || hold == 'Y'
					|| hold == 'a' || hold == 'e' || hold == 'i'
					|| hold == 'o' || hold == 'u' || hold == 'y') {
				newWord = newWord.substring( 0, idx) + c 
						+ newWord.substring(idx + 1, newWord.length());
			}
		}

		return newWord;
	}

	/**
	 * Create a new string that is reversed
	 * 
	 * @return The reversed string
	 */
	public String reverseString() {
		// The characters of the string will be reverse from the back to front
		String original, reverse = "";
		word = this.getOriginalString();
		original = word;
		int length = original.length();

		for (int idx = length -1; idx >= 0; idx--) {
			reverse = reverse + original.charAt(idx);
		}

		return reverse;
	}

	/**
	 * Check to see if the word is a palindrome (ignore case)
	 * 
	 * @return true if the word is a palindrome, false if it is not
	 */
	public boolean isPalindrome() {
		// compare the original word to the reversed word
		String original, reversed;
		int count = 0;
		word = this.getOriginalString();
		original = word.toUpperCase();
		reversed = this.reverseString();
		reversed = reversed.toUpperCase();

		boolean isPalindrome = false;

		// Check to makesure the characters are the same
		for (int idx = 0; idx < original.length(); idx++){
			if (original.charAt(idx) == reversed.charAt(idx)) {
				count++;
			}
			else{
			}
			// If characters equal the same number as count then it is palindrome

			if (count == original.length()){
				isPalindrome = true;
			}
			else {
				isPalindrome = false;
			}
		}

		return isPalindrome;
	}

	/**
	 * Split the string in half, and flip the two halves of the string.
	 * Two examples:
	 *   Given the String "abcd" this method will return "cdab"
	 *   Given the String "abcde" this method will return "decab"
	 * Notice in the second example the 'c' character does not move position
	 * 
	 * @return The flipped version of the String
	 */
	public String flipString() {
		// two different cases, a even length or odd length
		String word, flippedWord = "";
		word = this.getOriginalString();
		int length, middleIdx = 0;
		length = word.length();

		// even length
		if (length % 2 == 0){
			// find middle of number 
			middleIdx = length / 2;
			flippedWord = word.substring(middleIdx, length) 
					+ word.substring(0, middleIdx);
		}
		else if (length % 2 != 0) { // odd length
			// find middle of number
			middleIdx = length / 2;
			flippedWord = word.substring(middleIdx + 1, length) 
					+ word.substring(middleIdx, middleIdx + 1)
					+ word.substring(0, middleIdx);
		}

		return flippedWord;
	}
}
