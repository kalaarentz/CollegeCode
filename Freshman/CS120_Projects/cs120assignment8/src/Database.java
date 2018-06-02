/**
 * CS 120
 * Database for username/passwords. Uses a data file in the 'src' directory.
 * 
 * Last Modified: Nov. 1, 2014
 * @author Josh Hursey
 *
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;
import java.util.Scanner;

public class Database {
    /** Issue a warning if this class is used more than once */
    private static int multiuse_warning = 0;
    /** Filename that we will be reading from */
    private String filename;
    /** File pointer to the open file */
    private File fp;
    private Scanner fs;

    /** Number of tuples in the database */
    private int numTuples;
    /** Current Tuple number */
    private int curTuple = 0;

    /**
     * Database Constructor (may only be called once per-program run)
     * Default database selection (if db_selection is not 0, 1, or 2) is 0.
     * 
     * @param db_selection Which database to search (0 = small, 1 = medium, 2 = large)
     */
    public Database(int db_selection) {
        // Determine which file to serve
        if( 2 == db_selection ) {
            filename = "src/username-password-list-large.txt";
        }
        else if( 1 == db_selection ) {
            filename = "src/username-password-list.txt";
        }
        else {
            filename = "src/username-password-list-small.txt";
        }

        // Connect to the file specified above
        fp = new File(filename);

        // Calculate the number of entries in the database
        numTuples = calcNumEntries();
        curTuple = 0;

        // Open the file so it is ready to go
        fs = openFile();
        if( null == fs ) {
            System.exit(-1);
        }

        // Enforce the assignment restriction of a read-once database
        multiuse_warning += 1;
        if( multiuse_warning > 1 ) {
            System.out.println("*********************************");
            System.out.println("Error: You may only read out of the database one time!");
            System.out.println("That means you need to convert all of the database entries");
            System.out.println("into an array and use that array if you need to access those");
            System.out.println("entries multiple times. The Database constructor can only be called once!");
            System.out.println("*********************************");

            throw new InvalidParameterException("Database class may not be used multiple times!");
        }
    }

    /**
     * Access the number of tuples/entries in the database
     * 
     * @return Number of tuples in the database
     */
    public int getNumTuples() {
        return numTuples;
    }

    /**
     * Access the next tuple in the database.
     * It will be in the form (separated by ' ' space characters):
     * USERNAME    PASSWORD
     * Where the USERNAME and the PASSWORD will not contain any space characters.
     * 
     * @return next tuple or null if no more tuples in the database.
     */
    public String getNextTuple() {
        String tuple = null;
        if( curTuple == numTuples ) {
            return null;
        }
        else {
            curTuple += 1;
            if( fs.hasNext() ) {
                tuple = fs.nextLine();
            }
            else {
                closeFile(fs);
                fs = null;
            }
            return tuple;
        }
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
     * Calculate the number of entries in the file
     *
     * @return Number of entries in the file
     */
    private int calcNumEntries() {
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
}
