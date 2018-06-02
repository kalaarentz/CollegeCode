/**
 * CS 120:  Rock-Paper-Scissors-Lizard-Spock Game
 * 
 * Last Modified: [DATE]
 * @author [NAME]
 *
 */
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Scanner;
import javax.swing.*;
import java.awt.Font;


public class Driver {
    // Default Letter height and width (per letter)
    private final int TEXT_HEIGHT = 17;
    private final int LETTER_WIDTH = 6;

    // Default Button height and width
    private final int BUTTON_WIDTH  = 120;
    private final int BUTTON_HEIGHT = TEXT_HEIGHT+2;

    // The window that we are drawing in
    private Window window;
    
    /**
     * The main method, where the program starts
     * @param args
     */
    @SuppressWarnings("unused")
    public static void main( String[] args ) {
        Driver game = new Driver();
        // No other Java statements should be in here.
    }

    /**
     * Constructor for the Driver.
     * Actually draw the window and setup the various visual elements
     */
    public Driver() {
        /*
         * Setup the GUI Window
         */
        window = new Window();

        window.setTitle("The Rock-Paper-Scissors-Lizard-Spock Game");
        window.setLocation( 20, 20);
        window.setSize( 750, 600 );
        window.setBackground( Color.WHITE );
        
        
        // TODO Part I - Draw the visual elements
        
        
        // Last thing we do is repaint the window
        window.repaint();
    }

    /**
     * Event handler for RepeatButton.
     * This is called on each time step when the timer is running.
     * 
     * @param b The button that was clicked.
     */
    public void handleAction(RepeatButton b){
        // Nothing to do here
    }

    /**
     * Event handler for button presses.
     * This is called each time the user clicks a button.
     * 
     * @param button The button that caused the event.
     */
    public void handleButtonAction( ActionButton button ) {
        // TODO Part II - Progress the game based upon which button has been pressed.

        // Always repaint after handling a button
        window.repaint();
    }
}
