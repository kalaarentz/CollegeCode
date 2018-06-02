/**
 * CS 120: Assignment 07
 * Word Scrambler Game
 * 
 * Last Modified: Oct. 1, 2014
 * @author Josh Hursey
 *
 */
import java.util.Scanner;

public class WordGame {
    /** No class scoped variables (data members) are allowed */
    
    /**
     * The main method, where the program starts
     * @param args
     */
    public static void main(String[] args) {
        WordGame d = new WordGame();
        d.play();
        // No other Java statements should be in here.
    }

    /**
     * Constructor for the WordPlay driver.
     */
    public WordGame() {
        // Nothing to initialize in this class (no data members).
        // No other Java statements should be in here.
    }
    
    public void play() {
        char diff;
        String value;
        char yesOrNo;
        Scanner input = new Scanner(System.in);
        
        // A welcoming message
        System.out.println("Welcome to the Word Scrambler Game!");
        System.out.println();

        do {
            // Ask the user for the difficulty level
            diff = getDifficultyLevel(input);
            
            // Play the game with the user
            playAGame(input, diff);
        
            // Ask if they would like to play again
            System.out.println();
            do {
                System.out.print("Would you like to play again? [Y/N] ");
                value = input.nextLine().toUpperCase();
                yesOrNo = value.charAt(0);
                if( yesOrNo != 'Y' && yesOrNo != 'N' ) {
                    System.out.println("Error: Enter either 'Y' or 'N'!");
                }
            } while( yesOrNo != 'Y' && yesOrNo != 'N' );
            System.out.println();

        } while(yesOrNo == 'Y');

        // A goodbye message
        System.out.println("Thanks for playing the Word Scrambler Game!");
    }


    /**
     * Play a single game with the user at the given difficulty level
     * 
     * @param difficulty Difficulty level ('e', 'n', or 'h')
     */
    private void playAGame(Scanner input, char difficulty) {
        WordScrambler wp;

        // Get a word by instantiating the WordScramble game class
        // In the WordScramble() constructor it picks a word at random
        // based upon the difficulty level specified
        wp = new WordScrambler(difficulty);

        System.out.println();
        System.out.println("You will keep guessing words until you have either");
        System.out.println("guessed the word correctly, or have guessed");
        System.out.println("incorrectly "+wp.getMaxNumberOfTurns()+" times!");
        System.out.println();

        // The following debug statement might be helpful when creating the game
        //System.out.println("DEBUG: Original Word: " + wp.getUnscrambledWord());

        // Keep processing guesses until the game is over
        do {
            processAGuess(input, wp);
        } while( !wp.isGameOver() );

        // Game over - Determine if they won or not.
        System.out.println();
        if( wp.didWin() ) {
            System.out.print("Congratulations! You won in " + wp.getNumberOfTurns());
            if( wp.getNumberOfTurns() == 1 ) {
                System.out.println(" turn!");
            }
            else {
                System.out.println(" turns!");
            }
        }
        else {
            System.out.println("Sorry, but you did not win...");
            System.out.println("The word was " + wp.getUnscrambledWord());
        }
    }
    
    /**
     * Process a guess from the user.
     * 
     * @param wp WordScramble game to play against
     */
    private void processAGuess(Scanner input, WordScrambler wp) {
        String wordGuessed;
        boolean wasValidGuess;
        int rem = wp.getMaxNumberOfTurns() - wp.getNumberOfTurns();

        // Display the Guess setup message
        System.out.println();
        if( rem == 1 ) {
            System.out.println("You have " + rem + " guess remaining!");
        }
        else {
            System.out.println("You have " + rem + " guesses remaining!");
        }
        System.out.println("Scrambled Word     : " + wp.getScrambledWord());
        System.out.println("Unscrambled Letters: " + wp.getDisplayWord());

        // Get a guess from the user
        // Keep asking for guesses until they give a word of the proper length
        do {
            System.out.print("Enter a valid guess: ");
            wordGuessed = input.nextLine();
            
            wasValidGuess = wp.guessWord(wordGuessed);
            if( !wasValidGuess ) {
                System.out.println("Error: Invalid guess, try again");
            }
        } while( !wasValidGuess );
    }
    
    /**
     * Ask the user for the difficulty level at which to play the game.
     * 
     * @return Difficulty level
     */
    private char getDifficultyLevel(Scanner input) {
        char level;
        String value = "";
        
        do {
            System.out.print("At what difficulty level would you like to play? [(E)asy, (N)ormal, (H)ard] ");
            value = input.nextLine();
            level = value.toUpperCase().charAt(0);

            if( level != 'E' && level != 'N' && level != 'H') {
                System.out.println("Error: Please enter one of the valid letters [E, e, N, n, H, h]!");
            }
        } while( level != 'E' && level != 'N' && level != 'H');
        
        return value.charAt(0);
    }

}
