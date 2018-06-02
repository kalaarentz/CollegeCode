/**
 * CS 120
 * Random Word Generator. Uses a dictionary in the 'src' directory.
 * 
 * Last Modified: Oct. 1, 2014
 * @author Josh Hursey
 *
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class RandomWord {
    /** Filename that we will be reading from */
    private String filename;
    /** File pointer to the open file */
    private File fp;
    /** Number of words in the dictionary */
    private int numWords;

    /** Mocking selection */
    private int mockWord = -1;
    
    /**
     * Constructor
     */
    public RandomWord() {
        filename = "src/final-wordlist.txt";
        fp = new File(filename);
        numWords = calcNumWords();
    }
    
    /**
     * Access a random word from the dictionary
     * 
     * @return A random word
     */
    public String getRandomWord() {
        int nth = getRandomNumber(1, numWords);
        return getNthWord(nth).toUpperCase();
    }
    
    /**
     * Get a random number between min and max
     * 
     * @param min Minimum value
     * @param max Maximum value
     * @return A random number in the range
     */
    int getRandomNumber(int min, int max) {
        if( mockWord > 0 ) {
            return mockWord;
        }
        return min + (int)(Math.random() * ( (max-min) + 1));
    }
    
    /**
     * Open the file
     * 
     * @return A scanner to the open file
     */
    private Scanner openFile() {
        Scanner fs;
        
        if( !fp.exists() ) {
        	System.out.println("Error: File " + filename + " does not exist in this project!");
            return null;
        }
        
        try {
            fs = new Scanner(fp);
        } catch (FileNotFoundException e) {
        	System.out.println("Error: File " + filename + " cannot be opened!");
            e.printStackTrace();
            return null;
        }

        return fs;
    }
    
    /**
     * Close the file
     * 
     * @param fs Scanner pointing to the open file
     */
    private void closeFile(Scanner fs) {
        fs.close();
    }
    
    /**
     * Calculate the number of words in the file
     *
     * @return Number of words in the file
     */
    private int calcNumWords() {
        Scanner fs;
        int counter = 0;
        
        fs = openFile();
        if( null == fs ) {
            return 0;
        }
        
        while( fs.hasNext() ) {
            fs.nextLine();
            ++counter;
        }
     
        closeFile(fs);
        
        return counter;
    }
    
    /**
     * Get the n'th word out of the file
     * 
     * @param nth The index of the word to access
     * @return The word or an empty string if the word does not exist
     */
    private String getNthWord(int nth) {
        Scanner fs;
        String word = "";
        int counter = 0;
        
        fs = openFile();
        if( null == fs ) {
            return null;
        }
        
        while( fs.hasNext() ) {
            word = fs.nextLine();
            ++counter;
            if( counter == nth ) {
                break;
            }
        }
     
        closeFile(fs);
        
        return word;
    }
}
