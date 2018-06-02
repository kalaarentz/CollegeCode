/**
 * String Driver program.
 */
import java.util.Scanner;

public class StringDriver {
    
    public static void main(String[] args) {
        StringDriver d = new StringDriver();
        d.processStrings();
    }
    
    public StringDriver() {
        // Nothing to do
    }
    
    private void processStrings() {
        Scanner input = new Scanner( System.in );
        String word;
        
        // Test 5 random words not from the user
        RandomWord rw = new RandomWord();
        for(int i = 0; i < 5; ++i ) {
            System.out.println("-----------------");
            processWord( rw.getRandomWord() );
        }

        // Get a word from the user to test
        System.out.println("-----------------");
        System.out.print("Enter a word: ");
        word = input.nextLine();
        processWord( word );
    }
    
    private void processWord( String word ) {
        StringAnalysis analyze;
        
        analyze = new StringAnalysis( word );

        System.out.println("Word: " + analyze.getOriginalString() );
        System.out.println("\tNumber of Vowels     : " + analyze.getNumberOfVowels() );
        System.out.println("\tString without Vowels: " + analyze.removeAllVowels() );
        System.out.println("\tReplace Vowels with !: " + analyze.replaceAllVowelsWith('!') );
        System.out.println("\tReplace Vowels with ^: " + analyze.replaceAllVowelsWith('^') );
        System.out.println("\tString in reverse    : " + analyze.reverseString() );
        System.out.println("\tString as flipped    : " + analyze.flipString());
        System.out.println("\tIs palindrome? " + analyze.isPalindrome() );
        if( !analyze.getOriginalString().equals(word) ) {
            System.out.println("********* Error ******* Word changed inside the object!");
            System.out.println("\tExpected the word to be \""+word+"\" but was \""+analyze.getOriginalString()+"\"");
        }
    }
}
