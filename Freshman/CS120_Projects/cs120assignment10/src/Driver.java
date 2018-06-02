/**
 * <This program simulates the orbits for the inner planets (Mercury, Venus, Earth,
 * and Mars) around the Sun. This program will have a Planet class that extends to
 * Image Label.
 * The Sun object is positioned exactly in the middle of the screen. The planets
 * start from a fixed position to the right of the Sun, as seen below. 
 * The planets will trace the lines of their orbit for exactly two orbits or the program
 * will slow down. The orbit trace will be made by drawing a line from one position
 * to the next position. The planets will continue to orbit the Sun. 
 * There will be four buttons, a Pause/Resume, Reset, Speed Up, and Slow Down. Each have 
 * there own functions. Pause/Resume buttons text will change depending if they are paused
 * or the user wants to start the orbiting again.
 * There is a text field button that will display 100 milliseconds. Depending on the 
 * buttons Speed Up or Slow Down the number will change by ten. If the user puts in 
 * a number it will be taken by hitting enter. In addition, it will resume the game if
 * the game was paused.
 * Using the mouse click on any of the planets will provide a pop up window with 
 * information about the planet. >
 * 
 * Last Modified: <December 11, 2014>
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

	// Planet class objects
	Planet sun , mercury, venus, earth, mars;

	// ActionButtons
	ActionButton pause, reset, speedUp, speedDown;

	// Input Text Field
	ActionTextField speedOfPlanets;

	// Default text for input text field
	String defaultText = "100";

	// input Text field Delay number (speed of the planet)
	private int delayNum = 100;

	// PopUpWindows 
	PopUpWindow sunPw, mercuryPw, venusPw, earthPw, marsPw; 

	// Repeat Button (orbital progression)
	RepeatButton orbitalProgress = new RepeatButton( this );

	// Lines to show Orbits
	Line mercuryLines, venusLines, earthLines, marsLines;


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
		 * Create new planet classes
		 * sun, mercury, venus, earth, mars
		 * 
		 */

		// Sun information
		sun = new Planet( "sun.png", sunScale, "Sun", 0, 0, 0, 0 );
		sun.setLocationCentered( WINDOW_WIDTH/2, WINDOW_HEIGHT/2 );
		window.add( sun );

		sun.setDescription( "The Sun is the star at the center of "
				+ "the Solar System. It has a diameter of about "
				+ "865,374 mile, around 109 times that of Earth, "
				+ "and its mass is approximately 330,000 times the "
				+ "mass of Earth. The Sun accounts for about 99.86% of "
				+ "the total mass of the Solar System. The Sun formed "
				+ "about 4.567 billion years ago. It completes an "
				+ "orbit of the galaxy every 250 million years. \n" +
				"-Wikipedia \n" +
				"The sun is divided into several layers. The core extends"
				+ " from the sun’s center to about a quarter of the way "
				+ "to its surface. It only takes up 2% of the sun’s volume, "
				+ "but holds nearly half of the sun’s mass. The next "
				+ "layer is the radiative zone that makes up 32% of "
				+ "the sun’s volume and 48% of its mass.  The convection "
				+ "zone makes up 66% of the sun’s volume but only a little"
				+ " more than 2 percent of its mass. The photosphere "
				+ "layer emits the light we see. \n" +
				"- Space.com" );

		// Mercury Information
		mercury = new Planet ( "mercury.png", 0.3829 * earthScale, "Mercury", 
				sun.getX(), sun.getY(), 69.816900, 87.9691 );
		mercury.setLocationCentered( sun.getCenteredX() + (int)69.816900,
				sun.getCenteredY() );
		window.add( mercury );

		mercury.setDescription( "Mercury is the closest planet to the sun. "
				+ "In addition, it circles the sun faster than all the other "
				+ "planets, which is why Romans named it after the swift-footed "
				+ "messenger god Mercury. Since the planet is so close to the sun, "
				+ "Mercury’s surface temperature can reach a scorching 840 degrees "
				+ "Fahrenheit.  Mercury is the smallest planet—it is only slightly"
				+ " larger then Earth’s moon.  Mercury’s orbital period is 88 Earth "
				+ "days. The diameter of Mercury is 3,032 miles.  Mercury has a "
				+ "thin atmosphere and has a liquid iron core which makes up about "
				+ "three-fourths of its radius.\n- Space.com" );

		// Venus Information
		venus = new Planet ( "venus.png", 0.9499 * earthScale, "Venus",
				sun.getX(), sun.getY(), 108.939000, 224.698 );
		venus.setLocationCentered( sun.getCenteredX() + (int)108.939000, 
				sun.getCenteredY() );
		window.add( venus );

		venus.setDescription( "Venus, the second planet from the sun, "
				+ "is named for the Roman goddess of love and beauty. "
				+ "The planet—the only planet named after a female, may"
				+ " have been named for this because it shone the brightest "
				+ "of the five planets known to ancient astronomers.  Venus "
				+ "and Earth are called twins because they are similar in size,"
				+ " mass, density, composition and gravity. Venus is the hottest "
				+ "planet in the Solar System. Temperatures on Venus reach 870 degrees "
				+ "Fahrenheit.  The surface of Venus is extremely dry, and the planet "
				+ "is in a prolonged molten state. There is no liquid water on its "
				+ "surface today because it is boiled away. The orbital period is about"
				+ " 225 Earth days long. \n- Space.com" );

		// Earth Information
		earth = new Planet ( "earth.png", earthScale, "Earth",
				sun.getX(), sun.getY(), 152.098232, 365.256 );
		earth.setLocationCentered( sun.getCenteredX() + (int)152.098232,
				sun.getCenteredY() );
		window.add( earth );

		earth.setDescription( "Earth is the third planet from the sun. "
				+ "It is the only planet known to have an atmosphere containing"
				+ " free oxygen, oceans of liquid water, and life. Earth is the"
				+ " fifth largest of the planets in the solar system. Earth has a "
				+ "diameter of roughly 8,000 miles. Earth’s surface is roughly 71% "
				+ "water, and most of it in the oceans. \n"
				+ "Earth spins on an imaginary line called an axis that runs from the"
				+ " North Pole to the South Pole, while also orbiting the sun. It takes"
				+ " Earth 23.934 hours to complete a rotation on its axis, and roughly"
				+ " 365.24 days to complete an orbit around the sun.  The Earth’s "
				+ "average distance from the sun is 92,955,820 miles. The Earth was"
				+ " formed at roughly the same time as the sun and other planets some "
				+ "4.6 billion years ago.\n- Space.com" ); 

		// Mar INformation
		mars = new Planet ( "mars.png", 0.533 * earthScale, "Mars",
				sun.getX(), sun.getY(), 249.209300, 686.971 );
		mars.setLocationCentered( sun.getCenteredX() + (int)249.209300,
				sun.getCenteredY() );
		window.add( mars );

		mars.setDescription( "Mars is the fourth planet from the sun. Befitting "
				+ "the red planet’s bloody color, the Romans name it after their god "
				+ "of war.  The bright rust colors Mars is known for is due to "
				+ "iron-rich minerals on its surface. The red planet is home to both "
				+ "the highest mountain and the deepest, longest valley in the solar"
				+ " system. \nMars is much colder than Earth; the average "
				+ "temperature is about -80 degrees Fahrenheit. In addition, Mars "
				+ "has the same type of axis like Earth giving Mars seasons. The "
				+ "average distance from the sun is 141,633,260 miles. Mars has two "
				+ "moons, Phobos and Deimos.\n- Space.com" );

		/*
		 * Buttons for the screen
		 * 
		 * pause/resume: the text will be changing for this button depending on if 
		 * the repeat button is on or not
		 * 
		 * reset: changes the position of the planets
		 * 
		 * speedUp/speedDown: changes the delay of the repeatButton
		 * 
		 */

		pause = new ActionButton( this );
		pause.setText( "Pause" );
		pause.setBounds( 10, 10, BUTTON_WIDTH, BUTTON_HEIGHT );
		window.add( pause );

		reset = new ActionButton( this );
		reset.setText( "Reset" );
		reset.setBounds( 10, 20 + BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT );
		window.add( reset );

		speedUp = new ActionButton( this );
		speedUp.setText( "Speed Up" );
		speedUp.setBounds( 10, 60 + BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT );
		speedUp.setForeground( Color.RED );
		window.add( speedUp );

		speedDown = new ActionButton( this );
		speedDown.setText( "Speed Down" );
		speedDown.setBounds( 10, 110 + BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT );
		speedDown.setForeground( Color.BLUE );
		window.add( speedDown );

		/*
		 *  Action Text Field 
		 *  
		 *  default text is 100
		 *  
		 */

		speedOfPlanets = new ActionTextField( this );
		speedOfPlanets.setBounds( 10, 85 + BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
		speedOfPlanets.setBackground( Color.WHITE );
		speedOfPlanets.setText( defaultText );
		window.add( speedOfPlanets );

		// Start the timer
		orbitalProgress.startTimer();

	}

	/**
	 * The method triggered when someone presses 'Enter' while in the text field
	 * 
	 * @param tf A pointer to the text field that caused the event
	 */
	public void handleTextFieldAction( ActionTextField tf ) {
		// create a parser for the text
		Scanner parser;

		// Skip everything if the text field is empty
		if ( speedOfPlanets.getText().length() > 0 ){
			// create a scanner to parse the string
			parser = new Scanner( speedOfPlanets.getText() );

			delayNum = parser.nextInt();
			// check to see if the resume button is on or not
			if ( pause.getText().equals( "Resume" ) ){
				// check what the delayNum is--> cannot not be negative number
				if( delayNum < 0 ) {

					delayNum = 100;
					orbitalProgress.startTimer();
					orbitalProgress.setDelay( delayNum );
					speedOfPlanets.setText( defaultText );
					pause.setText( "Pause" );

				}
				else {

					orbitalProgress.startTimer();
					orbitalProgress.setDelay( delayNum );
					pause.setText( "Pause" );
				}
			}
			else {
				// check what the delayNum is--> cannot not be negative number
				if( delayNum < 0 ) {

					delayNum = 100;
					orbitalProgress.setDelay( delayNum );
					speedOfPlanets.setText( defaultText );

				}
				else {

					orbitalProgress.setDelay( delayNum );

				}
			}


		}
		else { // if there are no numbers in the text field, reset it to 100

			delayNum = 100;
			orbitalProgress.setDelay( delayNum );
			speedOfPlanets.setText( defaultText );

		}
	}

	/**
	 * The method triggered when someone presses the button on the screen.
	 * 
	 * @param button A pointer to the button that caused this event
	 */
	public void handleButtonAction( ActionButton button ) {

		// Button hit is the pause/resume button
		if ( button == pause ){
			// Stop the timer
			// Take into account that the buttons change what they say
			if ( button.getText().equals( "Pause" ) ){

				orbitalProgress.stopTimer();
				pause.setText( "Resume" );

			}
			else if ( button.getText().equals( "Resume" ) ){

				orbitalProgress.startTimer();
				pause.setText( "Pause" );
			}

		}
		else if ( button == reset ){ // Reset Button hit 
			JComponent elements[] = window.getAllComponents();
			
			// Call the resetPosition method. 

			// If the pause button text says pause, the repeat button is still on
			if ( pause.getText().equals( "Pause" ) ){

				// put them in original position
				mercury.setLocationCentered( sun.getCenteredX() + (int)69.816900,
						sun.getCenteredY() );
				venus.setLocationCentered( sun.getCenteredX() + (int)108.939000, 
						sun.getCenteredY() );
				earth.setLocationCentered( sun.getCenteredX() + (int)152.098232,
						sun.getCenteredY() );
				mars.setLocationCentered( sun.getCenteredX() + (int)249.209300,
						sun.getCenteredY() );

				// resetPosition 
				mercury.resetPostion();
				venus.resetPostion();
				earth.resetPostion();
				mars.resetPostion();


				for ( int idx = 0; idx < elements.length; idx++ ){
					if ( elements[idx] instanceof Line ){
						window.remove( elements[idx] );
					}
				}
				
			}
			else if ( pause.getText().equals( "Resume" ) ){ // If button says resume

				// put them in original position
				mercury.setLocationCentered( sun.getCenteredX() + (int)69.816900,
						sun.getCenteredY() );
				venus.setLocationCentered( sun.getCenteredX() + (int)108.939000, 
						sun.getCenteredY() );
				earth.setLocationCentered( sun.getCenteredX() + (int)152.098232,
						sun.getCenteredY() );
				mars.setLocationCentered( sun.getCenteredX() + (int)249.209300,
						sun.getCenteredY() );

				// resetPosition 
				mercury.resetPostion();
				venus.resetPostion();
				earth.resetPostion();
				mars.resetPostion();


				for ( int idx = 0; idx < elements.length; idx++ ){
					if ( elements[idx] instanceof Line ){
						window.remove( elements[idx] );
					}
				}
			}
			
		}
		else if ( button == speedUp ){ // SpeedUp Button hit

			if ( delayNum >= 10 ){ // Numbers greater then 10, but can have 10 subtracted

				delayNum = delayNum - 10;
				orbitalProgress.setDelay( delayNum );
				speedOfPlanets.setText( "" + delayNum );

			}
			else if ( delayNum < 0 ){ // Numbers less then 0, changed to 

				delayNum = 100;
				orbitalProgress.setDelay( delayNum );
				speedOfPlanets.setText( "" + delayNum );

			}
			else if ( delayNum >= 0 && delayNum <= 9 ){

				orbitalProgress.setDelay( delayNum );
				speedOfPlanets.setText( "" + delayNum );

			}

		}
		else if ( button == speedDown ){ // SpeedDown button hit

			delayNum = delayNum + 10;
			orbitalProgress.setDelay ( delayNum );
			speedOfPlanets.setText( "" + delayNum );

		}
	}

	/**
	 * Event handler for RepeatButton.
	 * This is called on each time step when the timer is running.
	 * 
	 * @param b The button that was clicked.
	 */
	public void handleAction(RepeatButton b){
		// use Planet method of advance time
		int mercuryX, mercuryY, venusX, venusY, earthX, earthY, marsX, marsY;

		mercuryX = mercury.getCenteredX();
		mercuryY = mercury.getCenteredY();

		venusX = venus.getCenteredX();
		venusY = venus.getCenteredY();

		earthX = earth.getCenteredX();
		earthY = earth.getCenteredY();

		marsX = mars.getCenteredX();
		marsY = mars.getCenteredY();

		// Draw Lines
		// Take into account the years of the completeOrbits
		
		// Number of years for mercury, greater then 2, wont draw lines
		if ( mercury.getNumberOfCompletOrbits() < 2 ) {
			
			window.remove( mercury );
			mercury.advanceTime( sun );
			mercuryLines = new Line( mercuryX, mercuryY,
					mercury.getCenteredX(), mercury.getCenteredY() );
			mercuryLines.setBackground( Color.CYAN );
			window.add( mercuryLines );
			window.add( mercury );
			
		}
		else {
			
			mercury.advanceTime( sun );
			
		}

		// Number of years for venus lines, greater then 2, wont draw lines
		if ( venus.getNumberOfCompletOrbits() < 2 ){
			
			window.remove( venus );
			venus.advanceTime( sun );
			venusLines = new Line( venusX, venusY,
					venus.getCenteredX(), venus.getCenteredY() );
			venusLines.setBackground( Color.CYAN );
			window.add( venusLines );
			window.add( venus );
			
		}
		else {
			
			venus.advanceTime( sun );
			
		}

		// number of years for earth, greater then 2, wont draw lines
		if ( earth.getNumberOfCompletOrbits() < 2 ) {
			
			window.remove( earth );
			earth.advanceTime( sun );
			earthLines = new Line( earthX, earthY,
					earth.getCenteredX(), earth.getCenteredY() );
			earthLines.setBackground( Color.CYAN );
			window.add( earthLines );
			window.add( earth );
			
		}
		else {
			
			earth.advanceTime( sun );
			
		}
	
		// number of years for mars, greater then 2, wont draw lines
		if ( mars.getNumberOfCompletOrbits() < 2 ){
			
			window.remove( mars );
			mars.advanceTime( sun );
			marsLines = new Line( marsX, marsY,
					mars.getCenteredX(), mars.getCenteredY() );
			marsLines.setBackground( Color.CYAN );
			window.add( marsLines );
			window.add( mars );
			
		}
		else {
			
			mars.advanceTime( sun );
			
		}
		
	}

	/**
	 * Handle a mouse click
	 * 
	 * @param x X Position of the point of click
	 * @param y Y Position of the point of click
	 * @param isLeftButton If the button was left or right
	 */
	public void handleMouseClick( int x, int y, boolean isLeftButton ) {
		// Check to see if they intersect the different planets
		if ( sun.pointIntersects( x, y) ){
			
			sunPw = new PopUpWindow( window, sun.toString(), 
					sun.getDescription() );
			
		}
		else if ( mercury.pointIntersects( x, y ) ){
			
			mercuryPw = new PopUpWindow( window, mercury.toString() ,
					mercury.getDescription() );
			
		}
		else if ( venus.pointIntersects( x, y ) ){
			
			venusPw = new PopUpWindow( window, venus.toString(), 
					venus.getDescription() );
			
		}
		else if ( earth.pointIntersects( x, y ) ){
			
			earthPw = new PopUpWindow( window, earth.toString(), 
					earth.getDescription() );
			
		}
		else if ( mars.pointIntersects( x, y ) ){
			
			marsPw = new PopUpWindow( window, mars.toString(), 
					mars.getDescription() );
			
		}
	}
}
