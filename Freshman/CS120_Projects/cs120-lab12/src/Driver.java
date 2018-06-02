/**
 * <This program will help setup the program for Assignment 10. This program will
 * involve inheritance, MouseClickListener and PopUpWindow.
 * 
 * Step One:
 * Making sure that MouseClickListener object works.
 * Make a DEBUG STATEMENT that prints out (x,y)
 * 
 * Step Two: 
 * Playing with the PopUpWindow
 * create a pop up window that prints out (x,y)
 * 
 * Step Three: 
 * Creating a Planet Class
 * The planet class needs to extend to ImageLabel Class
 * Must bring inheritance for the base functionality 
 * To set location of the Planet in the constructor will need to implement 
 * 		setLocationCentered
 * 		getCenteredX
 * 		getCenteredY
 * 
 * Step Four:
 * Now this will deal with the HandleMouseClick method. You only want a pop up window
 * to show when the mouse click intersects the sun>
 * 
 * Last Modified: <December 2,2014>
 * @author <Kala Arentz>
 *
 * http://eyes.nasa.gov/index.html
 * http://space.jpl.nasa.gov/
 * http://en.wikipedia.org/wiki/Orrery
 */
import java.awt.Color;
import java.util.Scanner;

import javax.swing.JComponent;

public class Driver {
    // Height and Width of the window
    private final int WINDOW_HEIGHT = 600;
    private final int WINDOW_WIDTH  = 800;

    // Some rough numbers for the height and width of a letter
    private final int LETTER_HEIGHT = 17;
    private final int LETTER_WIDTH = 6;

    // Height and Width of a Button
    private final int BUTTON_WIDTH  = 100;
    private final int BUTTON_HEIGHT = LETTER_HEIGHT+2;

    // The window we are drawing in
    private Window window;

    // Some scaling for the planets
    private final double sunScale = 0.2;
    private final double earthScale = 0.07;
    
    // Planets
    private Planet sun;
    

    /**
     * @param args
     */
    public static void main( String[] args ) {
        Driver solarSystem = new Driver();
    }

    /**
     * Our main driver that creates our GUI environment
     */
    public Driver() {

        /*
         * Setup the GUI Window
         */
        window = new Window();

        window.setTitle("Solar System");
        window.setLocation( 20, 20);
        window.setSize( WINDOW_WIDTH, WINDOW_HEIGHT );
        window.setBackground( Color.BLACK );

        /*
         * Listener for mouse events
         */
        MouseClickListener ml = new MouseClickListener(this, window);


        /*
         *  Add the sun planet to the screen
         */
        sun = new Planet( "sun.png", sunScale, "Sun", 0 , 0 ,
        		0, 0);
        sun.setLocationCentered( WINDOW_WIDTH/2, WINDOW_HEIGHT/2 );
        
        window.add( sun );
        
    }

    /**
     * The method triggered when someone presses 'Enter' while in the text field
     * 
     * @param tf A pointer to the text field that caused the event
     */
    public void handleTextFieldAction( ActionTextField tf ) {
        // TODO Write this method
    }

    /**
     * The method triggered when someone presses the button on the screen.
     * 
     * @param button A pointer to the button that caused this event
     */
    public void handleButtonAction( ActionButton button ) {
        // TODO Write this method
    }

    /**
     * Event handler for RepeatButton.
     * This is called on each time step when the timer is running.
     * 
     * @param b The button that was clicked.
     */
    public void handleAction(RepeatButton b) {
        // TODO Write this method
    }

    /**
     * Handle a mouse click
     * 
     * @param x X Position of the point of click
     * @param y Y Position of the point of click
     * @param isLeftButton If the button was left or right
     */
    public void handleMouseClick( int x, int y, boolean isLeftButton ) {
        // STEP ONE; DEBUG STATEMENT
    	//System.out.println( "Clicked at (" + x + ", " + y + ")" );
    	
    	/* STEP TWO
    	PopUpWindow pw = new PopUpWindow( window, "Mouse Click" , 
    			"Clicked at (" + x + ", " + y + ")" );
    	*/
    	
    	// STEP FOUR
    	if ( sun.pointIntersects( x, y)) {
    		PopUpWindow pw = new PopUpWindow( window, "Sun" , 
        			"Clicked at (" + x + ", " + y + ")" );
    	}
    }
}
