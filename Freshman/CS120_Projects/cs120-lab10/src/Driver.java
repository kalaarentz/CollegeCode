/**
 * <This program will be using the basic graphics of rectangles, ovals, triangles, and lines
 * to sketch a kitten face. After drawing the cat, you will need to add a JLabel below the 
 * image, but still in the window with the name of your cat (the name can anything you like).
 * The requirments for this image are:
 * 		-Two eyes
 * 		-Two pupils
 * 		-Nose
 * 		-Mouth
 * 		-A few whiskers
 * 		-A name
 * 		- The use of at least 2 colors (other then the background color)>
 *
 * Last Modified: <November 13, 2014>
 * @author <Kala Arentz>
 */
import java.awt.Color;

import javax.swing.JLabel;

public class Driver {
    // Default Window height and width
	private final int WINDOW_HEIGHT = 250;
    private final int WINDOW_WIDTH  = 250;
    
    // Default Letter height and width (per letter)
    private final int TEXT_HEIGHT = 17;
    private final int LETTER_WIDTH = 6;

    // Repeat button example
    private RepeatButton meowButton;
    
    // Counter for the number of iterations
	private int timeIter = 0;
	
    /**
     * The main method, where the program starts
     * @param args
     */
	public static void main(String[] args) {
		Driver d = new Driver();
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
    	// create the window
    	Window window = new Window();
    	window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    	window.setBackground( Color.DARK_GRAY);
    	window.setTitle("Cat Face");
    	
    	// create the ears
    		//left
    	Triangle left = new Triangle( 60, 10, 80, 60, 1);
    	left.setBackground( Color.LIGHT_GRAY );
    	window.add(left);
    	
    		//right
    	Triangle right = new Triangle(110, 10, 80, 60, 1);
    	right.setBackground( Color.LIGHT_GRAY );
    	window.add(right);
    	
    		// cover the middle of triangles to make more pointy ears
    	Rectangle rect = new Rectangle(100, 10, 50, 60);
    	rect.setBackground( Color.DARK_GRAY );
    	window.add( rect );
    	
        // create the face
    	Oval face = new Oval(30, 40, 190, 190);
    	face.setBackground( Color.LIGHT_GRAY );
    	window.add(face);
    	
    	// create eyes
    	Oval leftEYE = new Oval(70, 70, 40, 25);
    	leftEYE.setBackground( Color.WHITE );
    	window.add(leftEYE);
    	
    	Oval rightEYE = new Oval(140, 70, 40, 25);
    	rightEYE.setBackground( Color.WHITE );
    	window.add(rightEYE);
    	
    		// create the pupils
    	Oval leftPUPIL = new Oval(85, 70, 10, 25);
    	leftPUPIL.setBackground( Color.BLACK );
    	window.add(leftPUPIL);
    	
    	Oval rightPUPIL = new Oval(155, 70, 10, 25);
    	rightPUPIL.setBackground( Color.BLACK );
    	window.add(rightPUPIL);
    		
    	// Create nose
    	Rectangle nose = new Rectangle(110, 110, 30, 40);
    	nose.setBackground( Color.BLACK );
    	window.add(nose);
    	
    	Triangle coverLeftNose = new Triangle(100, 130, 20, 20, 1);
    	coverLeftNose.setBackground( Color.LIGHT_GRAY );
    	window.add(coverLeftNose);
    	
    	Triangle coverRightNose = new Triangle(130, 130, 20, 20, 1);
    	coverRightNose.setBackground( Color.LIGHT_GRAY );
    	window.add(coverRightNose);
    	
    	Rectangle noseLong = new Rectangle(120, 145, 12, 30);
    	noseLong.setBackground( Color.BLACK );
    	window.add(noseLong);
    	
    	// create mouth
    	Rectangle mouth = new Rectangle( 80, 170, 90, 40);
    	mouth.setBackground( Color.BLACK );
    	window.add(mouth);
    	
    	Triangle mouthCornerLeft = new Triangle(70, 170, 20, 40, 1);
    	mouthCornerLeft.setBackground( Color.LIGHT_GRAY );
    	window.add(mouthCornerLeft);
    	
    	Triangle mouthCornerRight = new Triangle(160, 170, 20, 40, 1);
    	mouthCornerRight.setBackground( Color.LIGHT_GRAY );
    	window.add(mouthCornerRight);
    	
    	Triangle upsideDownLeft = new Triangle( 82, 170, 37, 25, 0);
    	upsideDownLeft.setBackground(Color.LIGHT_GRAY);
    	window.add(upsideDownLeft);
    	
    	Triangle upsideDownRight = new Triangle(131, 170, 37, 25, 0);
    	upsideDownRight.setBackground( Color.LIGHT_GRAY );
    	window.add(upsideDownRight);
    	
    	// create the whiskers
    		//Left side
    	Line leftTop = new Line(80, 120, 110, 150, 4);
    	leftTop.setBackground(Color.BLACK);
    	window.add(leftTop);
    	
    	Line leftMiddle = new Line( 70, 150, 110, 150, 4);
    	leftMiddle.setBackground(Color.BLACK);
    	window.add(leftMiddle);
    	
    	Line leftBottom = new Line (77, 165, 110, 150, 4);
    	leftBottom.setBackground(Color.BLACK);
    	window.add(leftBottom);
    	
    		//Right side
    	Line rightTop = new Line(140, 150, 180, 130, 4);
    	rightTop.setBackground(Color.BLACK);
    	window.add(rightTop);
    	
    	Line rightMiddle = new Line(140, 150, 190, 150, 4);
    	rightMiddle.setBackground(Color.BLACK);
    	window.add(rightMiddle);
    	
    	Line rightBottom = new Line(140, 150, 180, 170, 4);
    	rightBottom.setBackground(Color.BLACK);
    	window.add(rightBottom);
    	
    	// create the jlabel that shows the name of the cat
    	JLabel msg;
    	msg = new JLabel();
    	msg.setText("Excalibur");
    	msg.setBounds(10, 220, 100, 17);
    	
    	msg.setBackground(Color.DARK_GRAY);
    	msg.setForeground(Color.WHITE);
    	window.add(msg);
    	
    	
        /*
         * Setup the timer events
         */
        meowButton = new RepeatButton(this);
        meowButton.setDelay(100);
        meowButton.startTimer();
	}
	
	/**
	 * Event handler for RepeatButton.
	 * This is called on each time step when the timer is running.
	 * 
	 * @param b The button that was clicked.
	 */
	public void handleAction(RepeatButton b) {
		++timeIter;
		
		for(int i = 0; i < timeIter; ++i ) {
			System.out.print("Meow ");
		}
		System.out.println("");

		if( timeIter > 5 ) {
	        meowButton.stopTimer();
		}
	}
}
