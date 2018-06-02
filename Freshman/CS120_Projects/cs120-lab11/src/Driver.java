/**
 * <This program will be launching watermelons towards a randomly generated target in
 * a field.
 * 
 * This program will use ActionTextField Class to ask the user for a velocity and 
 * angle that will be later used to help launch the watermelon at the target.
 * 
 * >
 * 
 * Last Modified: <November 20,2014>
 * @author <Kala Arentz>
 */
import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Scanner;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Driver {

	// Height and Width of the window
	private final int WINDOW_HEIGHT = 600;
	private final int WINDOW_WIDTH = 1200;

	// Some rough numbers for the height and width of a letter
	private final int LETTER_HEIGHT = 17;
	private final int LETTER_WIDTH = 6;

	// Height and Width of a Button
	private final int BUTTON_WIDTH = 100;
	private final int BUTTON_HEIGHT = LETTER_HEIGHT + 2;

	// Gravity constant 9.8 m/(s^2)
	private final double gravity = 9.8;

	// Ground level on the screen
	private final int GROUND_LEVEL = WINDOW_HEIGHT - 200;

	// X/Y reference point for the object's initial position
	private int LAUNCH_X;
	private int LAUNCH_Y;

	// The window we are drawing in
	private Window window;

	// The watermelon we are launching
	private ImageLabel watermelon;

	// A status message for the user
	private JLabel promptStatus;

	// Text fields for velocity and angle
	private ActionTextField velocityTF, angleTF;

	// Buttons for launching and starting a new game
	private ActionButton launchButton, newGameButton;

	// A timer to animate the launch
	private RepeatButton launchTimer;

	// The current time marker (to calculate position)
	private int curTime;

	// The current velocity and angle (provided by user)
	private double velocity, angle;

	// The target we are aiming for
	private Rectangle target;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Driver circle = new Driver();
	}

	/**
	 * Our main driver that creates our GUI environment
	 */
	public Driver() {
		int xPos, yPos, width, height;

		/*
		 * Setup the GUI Window
		 */
		window = new Window();

		window.setTitle("Watermelon Launcher Game");
		window.setLocation(20, 20);
		window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		window.setBackground(Color.WHITE);

		/*
		 * Ground line
		 */
		xPos = 0;
		yPos = 0;
		width = WINDOW_WIDTH + 1;
		height = GROUND_LEVEL;

		Rectangle sky = new Rectangle(xPos, yPos, width, height);
		sky.setBackground(new Color(204, 229, 255));
		window.add(sky);

		xPos = 0;
		yPos = GROUND_LEVEL;
		width = WINDOW_WIDTH + 1;
		height = WINDOW_HEIGHT + 1 - GROUND_LEVEL;

		Rectangle ground = new Rectangle(xPos, yPos, width, height);
		ground.setBackground(new Color(130, 178, 101));
		window.add(ground);

		xPos = 0;
		yPos = GROUND_LEVEL;
		width = WINDOW_WIDTH;

		Line ln = new Line(xPos, yPos, width, yPos, 3);
		ln.setBackground(Color.BLUE);
		window.add(ln);

		/*
		 * Watermelon at the launch position
		 * Define the launch position so we can build relative to it
		 */
		watermelon = new ImageLabel("Watermelon.png", 0.25);

		LAUNCH_X = 50;
		LAUNCH_Y = GROUND_LEVEL - watermelon.getHeight();

		xPos = LAUNCH_X;
		yPos = LAUNCH_Y;
		// width and height set automatically in the ImageLabel constructor

		watermelon.setLocation(LAUNCH_X, LAUNCH_Y);
		window.add(watermelon);

		//DEBUG Statement
		//System.out.println("DEBUG X: " + watermelon.getX() + " DEBUG Y: " + watermelon.getY());

		/*
		 * Status Messages towards the top of the screen
		 */
		promptStatus = new JLabel("Welcome to the Watermelon Launcher Game!");

		width = window.getWidth();
		height = LETTER_HEIGHT + 10;
		xPos = 10;
		yPos = 10;

		promptStatus.setLocation(xPos, yPos);
		promptStatus.setSize(width, height);
		promptStatus.setHorizontalAlignment(JLabel.CENTER);
		window.add(promptStatus);

		/*
		 * Launch Button
		 */
		launchButton = new ActionButton(this);
		launchButton.setText("Launch!");

		width = BUTTON_WIDTH + BUTTON_WIDTH / 2;
		height = BUTTON_HEIGHT;
		xPos = window.getWidth() / 2 - width - 5;
		yPos = window.getHeight() - height - 10;

		launchButton.setLocation(xPos, yPos);
		launchButton.setSize(width, height);
		window.add(launchButton);

		/*
		 * New Game button
		 */
		newGameButton = new ActionButton(this);
		newGameButton.setText("New Game!");

		width = BUTTON_WIDTH + BUTTON_WIDTH / 2;
		height = BUTTON_HEIGHT;
		xPos = window.getWidth() / 2 + 5;
		yPos = window.getHeight() - height - 10;

		newGameButton.setLocation(xPos, yPos);
		newGameButton.setSize(width, height);
		window.add(newGameButton);

		/*
		 * Velocity Text Field and JLabel
		 */
		velocityTF = new ActionTextField(this);

		width = launchButton.getWidth();
		height = LETTER_HEIGHT + 2;
		xPos = launchButton.getX();
		yPos = launchButton.getY() - 20 - height;

		velocityTF.setLocation(xPos, yPos);
		velocityTF.setSize(width, height);
		window.add(velocityTF);

		JLabel titleVel, titleAngle;
		titleVel = new JLabel("Velocity");

		width = velocityTF.getWidth();
		height = LETTER_HEIGHT;
		xPos = velocityTF.getX();
		yPos = velocityTF.getY() - height - 10;

		titleVel.setLocation(xPos, yPos);
		titleVel.setSize(width, height);
		titleVel.setHorizontalAlignment(JLabel.CENTER);
		window.add(titleVel);

		/*
		 * Angle Text Field
		 */
		angleTF = new ActionTextField(this);

		width = velocityTF.getWidth();
		height = velocityTF.getHeight();
		xPos = newGameButton.getX();
		yPos = velocityTF.getY();

		angleTF.setLocation(xPos, yPos);
		angleTF.setSize(width, height);
		window.add(angleTF);

		titleAngle = new JLabel("Angle");

		width = titleVel.getWidth();
		height = titleVel.getHeight();
		xPos = angleTF.getX();
		yPos = titleVel.getY();

		titleAngle.setLocation(xPos, yPos);
		titleAngle.setSize(width, height);
		titleAngle.setHorizontalAlignment(JLabel.CENTER);
		window.add(titleAngle);

		/*
		 * Setup the launch animation mechanism
		 */
		launchTimer = new RepeatButton(this);
		launchTimer.setDelay(100);
		curTime = 0;

		/*
		 * Generate the initial target
		 */
		generateTarget();
	}

	/**
	 * The method triggered when someone presses 'Enter' while in a text field
	 * 
	 * @param tf
	 *            A pointer to the text field that caused the event
	 */
	public void handleTextFieldAction(ActionTextField tf) {

		attemptLaunch();
	}

	/**
	 * The method triggered when someone presses the button on the screen.
	 * 
	 * @param button A pointer to the button that caused this event
	 */
	public void handleButtonAction(ActionButton button) {
		/*
		 * Launch the watermelon
		 */
		if ( button == launchButton ) {
			attemptLaunch();
		}
		/*
		 * Start a new game
		 */
		else {
			// Reset to the launch position
			watermelon.setLocation(LAUNCH_X, LAUNCH_Y);
			// Generate a new target
			generateTarget();
			// Reset the prompt
			promptStatus.setText("Welcome to the Watermelon Launcher Game!");
			// Reset the text fields
			velocityTF.setText("");
			angleTF.setText("");
		}
	}

	/**
	 * Generate a target in the field of play
	 */
	private void generateTarget() {
		int xPos, yPos, width, height;
		Random r = new Random();

		/*
		 * Remove the old target, if one exists
		 */
		if ( null != target ) {
			window.remove(target);
		}

		width = watermelon.getWidth() + 50;
		height = 10;

		// Must be beyond the launch position in the field of play
		// And with some random offset therein
		xPos = LAUNCH_X + watermelon.getWidth() + 100;
		xPos += r.nextInt(WINDOW_WIDTH - xPos - width);
		// Must be above ground level so we can smash into it!
		yPos = GROUND_LEVEL - 5;

		target = new Rectangle(xPos, yPos, width, height);
		target.setBackground(Color.RED);
		window.add(target);
	}

	/**
	 * Attempt to launch the watermelon
	 * 
	 * @return true if the object was launched, false otherwise
	 */
	private boolean attemptLaunch() {
		// TODO: Write this method

		/*
		 * Check to make sure the velocity has been set
		 * The string should be greater than 0 characters in length
		 */

		if(velocityTF.getText().length() <= 0){
			return false;
		}
		/*
		 * Check to make sure the angle has been set
		 * The string should be greater than 0 characters in length
		 */

		if (angleTF.getText().length() <= 0){
			return false;
		}
		/*
		 * Parse the velocity
		 * Then check to make sure the value is greater than 0.
		 * If it is less than 0 then return false.
		 */
		Scanner parser;

		parser = new Scanner(velocityTF.getText());
		velocity = parser.nextInt();

		if (velocity <= 0){
			return false;
		}

		/*
		 * Parse the angle
		 * Then check to make sure the value is greater than 0.
		 * If it is less than 0 then return false.
		 */
		parser = new Scanner(angleTF.getText());
		angle = parser.nextInt();

		if (angle <= 0){
			return false;
		}

		// Reset the location of the watermelon to the launch position
		watermelon.setLocation(LAUNCH_X, LAUNCH_Y);

		// Launch the watermelon
		launchTimer.startTimer();

		return true;
	}

	/**
	 * Event handler for RepeatButton.
	 * This is called on each time step when the timer is running.
	 * 
	 * @param b The button that was clicked.
	 */
	public void handleAction(RepeatButton b) {
		// TODO: Write this method

		// Advance time
		curTime++;

		// Get the X and Y positions (true positions relative to [0,0])
		//System.out.println("GetDistance: " + getDistance(velocity, angle));

		// Offset the calculation by the x and y position

		// If we crossed the y-axis then we hit the ground
		if ((getPositionY(velocity, angle, curTime) > 0)) {
			watermelon.setLocation(LAUNCH_X + (int)getPositionX(velocity, angle, curTime), 
					LAUNCH_Y- (int)getPositionY(velocity, angle, curTime));

			promptStatus.setText("Flying...");

		}
		else {
			watermelon.setLocation(LAUNCH_X + (int)getPositionX(velocity, angle, curTime), 
					LAUNCH_Y- (int)getPositionY(velocity, angle, curTime));

			launchTimer.stopTimer();
			if (watermelon.intersects(target)){
				promptStatus.setText("Yay! You hit the target.");
				curTime = 0;
			}
			else {
				promptStatus.setText("So close! Try Again.");
				curTime = 0;

			}
		}


	}

	/**
	 * Get the distance traveled by the object launched, as directed by the
	 * user.
	 * 
	 * @param velocity Velocity of the object at launch
	 * @param angle Angle of the launch
	 * @return the distance traveled by the pumpkin
	 */
	private double getDistance(double velocity, double angle) {
		double distance;
		distance = velocity * velocity * Math.sin(2 * Math.toRadians(angle)) / gravity;
		return distance;
	}

	/**
	 * Get position in the x axis of the watermelon at a given time
	 * 
	 * @param velocity Velocity of the watermelon at launch
	 * @param angle Angle of the launch
	 * @param time A point in time in seconds
	 * @return Position in the x axis
	 */
	private double getPositionX(double velocity, double angle, double time) {
		double x;
		x = velocity * Math.cos(Math.toRadians(angle)) * time;
		return x;
	}

	/**
	 * Get position in the y axis of the watermelon at a given time
	 * 
	 * @param velocity Velocity of the watermelon at launch
	 * @param angle Angle of the launch
	 * @param time A point in time in seconds
	 * @return Position in the y axis
	 */
	private double getPositionY(double velocity, double angle, double time) {
		double y;
		y = velocity * Math.sin(Math.toRadians(angle)) * time - (gravity * time * time) / 2;
		return y;
	}
}
