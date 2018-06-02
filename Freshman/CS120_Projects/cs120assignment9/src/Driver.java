/**
 * CS 120:  Rock-Paper-Scissors-Lizard-Spock Game
 * 
 * PART ONE
 * The first part of this program deals with the layout of the GUI widgets. The game 
 * is played until one of the player reaches ten. The number of wins is represented by 
 * Rectangle objects on either side of the window. This program will have TWO Rectangle 
 * ARRAY'S to mark the number of wins. A light-gray Rectangle marks a potential future 
 * point. When Player 1 or 2 guesses correctly then on of the rectangle objects is colored.
 * Player 1 gets a red Rectangle if they win, while Player 2 gets a blue rectangle. There
 * will be lines for all five of the images that must be imported.The different lines 
 * represent who would win depending on the items they picked. Put the lines in an ARRAY 
 * as well. 
 * 
 * PART TWO
 * 		-When the "Start A New Game" is pressed then the game is reset to the view
 * 		 seen in Part 1
 * 		-Either player may choose first in each iteration of the game.
 * 		-When either "Player 1" or "Player 2" button is pressed then you need to check
 * 		 if the game is over and if that player has already guessed this round.
 * 			-If the game is over then the ShotChoiceWindow is not displayed, but instead
 * 			 the message should be displayed indicating that the user should start a 
 * 			 new game.
 * 			-If the game is not over and they have not already guessed then the 
 * 			 ShotChoiceWindow will be displayed prompting the user for their shot.
 * 			-If the game is not over and if they have already guessed then the 
 * 			 ShotChoiceWindow is not displayed, but instead the message should be displayed
 * 			 indicated that they need to wait for the other player to guess.
 * 		-After each guess has been made, then the game decides a winner by using the
 * 		 compareShotTo and compareShotToAsString methods of the ShotChoice class.
 * 		-The winner will be displayed such as "Paper disproves Spock"
 * 		-Then the window will have their Rectangle object markers updated to reflect 
 * 		 their current score.
 * 		-If it is a draw, then neither player receives any points, all of the lines
 * 		will be displayed and "Draw" will be displayed in the text field.
 * 
 * Last Modified: [November 15, 2014]
 * @author [Kala Arentz]
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

	// Objects found in the window
	private ActionButton newGame, player1, player2;
	private JLabel textBox;
	private Rectangle[] player1Rect, player2Rect;
	private Line scissor1, scissor2, paper1, paper2, rock1, rock2;
	private Line lizard1, lizard2, spock1, spock2;

	// Window for the shots chosen by the players
	private ShotChoiceWindow player1choice, player2choice;
	private ShotChoice shot1, shot2;

	// how many times the players have guesses/ number of times they have won
	private int count1 = 0, count2 = 0;
	private int win1 = 0, win2 = 0;


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

		window.setTitle( "The Rock-Paper-Scissors-Lizard-Spock Game" );
		window.setLocation( 20, 20 );
		window.setSize( 750, 600 );
		window.setBackground( Color.WHITE );


		/*
		 * PART ONE Visual Aspects of the game
		 * 
		 */

		// Buttons
		newGame = new ActionButton( this );
		newGame.setBounds( 300, 25, 150, 25 );
		newGame.setBackground( Color.lightGray );
		newGame.setText( "Start New Game" );
		window.add( newGame );

		player1 = new ActionButton( this );
		player1.setBounds( 50, 75, 125, 25 );
		player1.setBackground( Color.lightGray );
		player1.setText( "Player 1" );
		window.add( player1 );

		player2 = new ActionButton( this );
		player2.setBounds( 575, 75, 125, 25 );
		player2.setBackground( Color.lightGray);
		player2.setText( "Player 2" );
		window.add( player2 );

		/* 
		 * Rectangles for two players represent the answers
		 * must be ARRAYS
		 * 
		 */

		//PLAYER ONE (light gray for unanswered, will be red if they win)
		player1Rect = new Rectangle[10];
		int startPosX = 25;
		int startPosY = 475;
		int rectHeight = 20;
		int rectWidth = 50;

		for ( int idx = 0; idx < player1Rect.length; idx++ ){
			player1Rect[idx] = new Rectangle( startPosX, 
					startPosY, rectWidth, rectHeight );
			player1Rect[idx].setBackground( Color.LIGHT_GRAY );
			window.add( player1Rect[idx] );
			startPosY = startPosY - rectHeight - 10;

		}

		//PLAYER TWO (light gray for unanswered, will be blue if they win)
		player2Rect = new Rectangle[10];
		int startPosX2 = 675;
		int startPosY2 = 475;
		int rectHeight2 = 20;
		int rectWidth2 = 50;

		for ( int idx = 0; idx < player2Rect.length; idx++ ){
			player2Rect[idx] = new Rectangle( startPosX2, 
					startPosY2, rectWidth2, rectHeight2 );
			player2Rect[idx].setBackground( Color.LIGHT_GRAY );
			window.add( player2Rect[idx] );
			startPosY2 = startPosY2 - rectHeight2 - 10;

		}

		/*
		 * images for the different players
		 */

		//scissors
		ImageLabel scissor = new ImageLabel( "scissors.png", 0.4 );
		scissor.setLocation( 315, 75 );
		window.add( scissor );

		//paper
		ImageLabel paper = new ImageLabel( "paper.png", 0.4 );
		paper.setLocation( 500, 195 );
		window.add( paper );

		// Rock
		ImageLabel rock = new ImageLabel( "rock.png", 0.4 );
		rock.setLocation( 450, 395 );
		window.add( rock );

		// Lizard
		ImageLabel lizard = new ImageLabel( "lizard.png", 0.4 );
		lizard.setLocation( 170, 395 );
		window.add( lizard );

		// Spock
		ImageLabel spock = new ImageLabel( "spock.png", 0.4 );
		spock.setLocation( 120, 195 );
		window.add( spock );

		/*
		 * Different lines to represent who wins when this happens
		 * 
		 */

		//Scissor PINK goes to paper and lizard
		scissor1 = new Line( 375, 130, 560, 255, 3 );
		scissor1.setBackground( Color.magenta );
		scissor1.setVisible( true );
		window.add( scissor1 );

		scissor2 = new Line( 375, 130, 230, 455, 3 );
		scissor2.setBackground( Color.magenta );
		scissor2.setVisible( true );
		window.add( scissor2 );

		//Spock BLUE goes to scissor and rock
		spock1 = new Line( 180, 255, 375, 130, 3 );
		spock1.setBackground( Color.BLUE );
		spock1.setVisible( true );
		window.add( spock1 );

		spock2 = new Line( 180, 255, 510, 455, 3 );
		spock2.setBackground( Color.BLUE );
		spock2.setVisible( true );
		window.add( spock2 );

		//Lizard GREEN goes to paper and Spock
		lizard1 = new Line( 230, 455, 560, 255, 3 );
		lizard1.setBackground( Color.GREEN );
		lizard1.setVisible( true );
		window.add( lizard1 );

		lizard2 = new Line( 230, 455, 180, 255, 3 );
		lizard2.setBackground( Color.GREEN );
		lizard2.setVisible( true );
		window.add( lizard2 );

		//Rock RED goes to lizard and scissor
		rock1 = new Line ( 510, 455, 230, 455, 3 );
		rock1.setBackground( Color.RED );
		rock1.setVisible( true );
		window.add( rock1 );

		rock2 = new Line ( 510, 455, 375, 130, 3 );
		rock2.setBackground( Color.RED );
		rock2.setVisible( true );
		window.add( rock2 );

		//Paper YELLOW goes to Spock and rock
		paper1 = new Line( 560, 255, 180, 255, 3 );
		paper1.setBackground( Color.yellow );
		paper1.setVisible( true );
		window.add( paper1 );

		paper2 = new Line( 560, 255, 510, 455, 3 );
		paper2.setBackground( Color.yellow );
		paper2.setVisible( true );
		window.add( paper2 );

		/*
		 * Console window
		 */

		//Blank line that separates the game window and console window
		Line consoleLine = new Line ( 0, 525, 750, 525, 5 );
		consoleLine.setBackground( Color.BLACK );
		window.add( consoleLine );

		//Welcome Message
		textBox = new JLabel();
		textBox.setBounds( 125, 550, 500, 25 );
		textBox.setText( "Welcome to the Rock-Paper-Scissors-Lizard-Spock Game!" );
		textBox.setHorizontalAlignment( JLabel.CENTER );
		window.add( textBox );

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
		// Part II - Progress the game based upon which button has been pressed.

		// Check to see if the game is over
		if ( win1 == 10 || win2 == 10 ){
			
		}
		else if ( count1 > count2 && button == player1 ){ //Player one button has been pressed again
			textBox.setText("Player 1 has already selected a shot. "
					+ "Let Player 2 choose!");
		}
		else if ( count2 > count1 && button == player2 ){ //Player two button has been pressed again
			textBox.setText("Player 2 has already selected a shot. "
					+ "Let Player 1 choose!");
		}
		else if ( button == newGame  ) { // when new game button pressed

			//reset the lines back
			lizard1.setVisible( true );
			lizard2.setVisible( true );
			scissor1.setVisible( true );
			scissor2.setVisible( true );
			rock1.setVisible( true );
			rock2.setVisible( true );
			paper1.setVisible( true );
			paper2.setVisible( true );
			spock1.setVisible( true );
			spock2.setVisible(true );

			//reset the rectangles as grey

			for( int idx = 0; idx < player1Rect.length; idx++ ){
				player1Rect[idx].setBackground( Color.LIGHT_GRAY );
			}

			for( int idx = 0; idx < player2Rect.length; idx++ ){
				player2Rect[idx].setBackground( Color.LIGHT_GRAY );
			}

			//reset the number of wins and counts
			win1 = 0;
			win2 = 0;

			count1 = 0;
			count2 = 0;

			//reset the textbox
			textBox.setText( "Welcome the Rock-Paper-Scissors-Lizard-Spock Game!" );

		}
		else {
			if ( button == player1 ){// Press Player One button
				player1choice = new 
						ShotChoiceWindow( window, "Player 1 Choose Your Shot" );
				shot1 = player1choice.getShotChosen();
				count1++;

			}
			else if ( button == player2 ) {// Press Player Two button
				player2choice = new
						ShotChoiceWindow( window, "Player 2 Choose Your Shot" );
				shot2 = player2choice.getShotChosen();
				count2++;

			}
		}


		/*
		 * compare the shots
		 * 
		 * take into account all the different lines of color and what should show
		 * up for who beats who
		 * 
		 * add to the win1 or win2 variables and change color of the two different
		 * rectangle arrays
		 * 
		 */
		if ( ( win1 == 10 || win2 == 10 ) && button != newGame ){
			textBox.setText("Game is over! Press \'Start New Game\' "
					+ "to start another game.");
		}
		else if ( ( win1 == 10 || win2 == 10 ) && button == newGame ){
			
			//reset the lines back
			lizard1.setVisible( true );
			lizard2.setVisible( true );
			scissor1.setVisible( true );
			scissor2.setVisible( true );
			rock1.setVisible( true );
			rock2.setVisible( true );
			paper1.setVisible( true );
			paper2.setVisible( true );
			spock1.setVisible( true );
			spock2.setVisible(true );

			//reset the rectangles as grey

			for( int idx = 0; idx < player1Rect.length; idx++ ){
				player1Rect[idx].setBackground( Color.LIGHT_GRAY );
			}

			for( int idx = 0; idx < player2Rect.length; idx++ ){
				player2Rect[idx].setBackground( Color.LIGHT_GRAY );
			}

			//reset the number of wins and counts
			win1 = 0;
			win2 = 0;

			count1 = 0;
			count2 = 0;

			//reset the textbox
			textBox.setText( "Welcome the Rock-Paper-Scissors-Lizard-Spock Game!" );
			
		}
		else if ( count1 == count2 && count1 != 0 && count2 != 0 ){
			if ( shot1.compareShotTo( shot2 ) > 0 ){
				// clear the lines from the pictures
				if ( shot1.compareShotTo( shot2 ) == 1 ||
						shot1.compareShotTo( shot2 ) == -1 ){// Scissors cuts paper

					scissor1.setVisible( true );

					scissor2.setVisible( false );
					rock1.setVisible( false );
					rock2.setVisible( false );
					lizard1.setVisible( false );
					lizard2.setVisible( false );
					paper1.setVisible( false );
					paper2.setVisible( false );
					spock1.setVisible( false );
					spock2.setVisible( false );
				}
				else if ( shot1.compareShotTo( shot2 ) == 2 ||
						shot1.compareShotTo( shot2 ) == -2 ){ // Scissors decapitates Lizard

					scissor2.setVisible( true );

					scissor1.setVisible( false );
					rock1.setVisible( false );
					rock2.setVisible( false );
					lizard1.setVisible( false );
					lizard2.setVisible( false );
					paper1.setVisible( false );
					paper2.setVisible( false );
					spock1.setVisible( false );
					spock2.setVisible( false );
				}
				else if ( shot1.compareShotTo( shot2 ) == 3 ||
						shot1.compareShotTo( shot2 ) == -3 ){ // Paper disproves spock

					paper1.setVisible( true );

					scissor1.setVisible( false );
					scissor2.setVisible( false );
					rock1.setVisible( false );
					rock2.setVisible( false );
					lizard1.setVisible( false );
					lizard2.setVisible( false );
					paper2.setVisible( false );
					spock1.setVisible( false );
					spock2.setVisible( false );
				}
				else if ( shot1.compareShotTo( shot2 ) == 4 ||
						shot1.compareShotTo( shot2 ) == -4 ){ // Paper covers rock

					paper2.setVisible( true );

					scissor1.setVisible( false );
					scissor2.setVisible( false );
					rock1.setVisible( false );
					rock2.setVisible( false );
					lizard1.setVisible( false );
					lizard2.setVisible( false );
					paper1.setVisible( false );
					spock1.setVisible( false);
					spock2.setVisible(false );
				} 
				else if ( shot1.compareShotTo( shot2 ) == 5 ||
						shot1.compareShotTo( shot2 ) == -5 ){ // Rock crushes Scissors

					rock2.setVisible( true );

					rock1.setVisible( false );
					scissor1.setVisible( false );
					scissor2.setVisible( false );
					lizard1.setVisible( false );
					lizard2.setVisible( false );
					paper1.setVisible( false );
					paper2.setVisible( false );
					spock1.setVisible( false );
					spock2.setVisible( false );
				}
				else if ( shot1.compareShotTo( shot2 ) == 6 ||
						shot1.compareShotTo( shot2 ) == -6 ){ // Rock crushes lizard

					rock1.setVisible( true );

					rock2.setVisible( false );
					scissor1.setVisible( false );
					scissor2.setVisible( false );
					lizard1.setVisible( false );
					lizard2.setVisible( false );
					paper1.setVisible( false );
					paper2.setVisible( false );
					spock1.setVisible( false );
					spock2.setVisible( false );
				}
				else if ( shot1.compareShotTo( shot2 ) == 7 ||
						shot1.compareShotTo( shot2 ) == -7 ){ // Lizard eats paper

					lizard1.setVisible( true );

					lizard2.setVisible( false );
					scissor1.setVisible( false );
					scissor2.setVisible( false );
					rock1.setVisible( false );
					rock2.setVisible( false );
					paper1.setVisible( false );
					paper2.setVisible( false );
					spock1.setVisible( false );
					spock2.setVisible( false );
				}
				else if ( shot1.compareShotTo( shot2 ) == 8 ||
						shot1.compareShotTo( shot2 ) == -8 ){ // Lizard poisons spock

					lizard2.setVisible( true );

					lizard1.setVisible( false );
					scissor1.setVisible( false );
					scissor2.setVisible( false );
					rock1.setVisible( false );
					rock2.setVisible( false );
					paper1.setVisible( false );
					paper2.setVisible( false );
					spock1.setVisible( false );
					spock2.setVisible( false );
				}
				else if ( shot1.compareShotTo( shot2 ) == 9 ||
						shot1.compareShotTo( shot2 ) == -9 ){ // Spock vaporizes Rock

					spock2.setVisible( true );

					spock1.setVisible( false );
					scissor1.setVisible( false );
					scissor2.setVisible( false );
					lizard1.setVisible( false );
					lizard2.setVisible( false );
					paper1.setVisible( false );
					paper2.setVisible( false );
					rock1.setVisible( false );
					rock2.setVisible( false );
				}
				else if ( shot1.compareShotTo( shot2 ) == 10 ||
						shot1.compareShotTo( shot2 ) == -10 ){ // Spock smashes scissors

					spock1.setVisible( true );

					spock2.setVisible( false );
					scissor1.setVisible( false );
					scissor2.setVisible( false );
					lizard1.setVisible( false );
					lizard2.setVisible( false );
					paper1.setVisible( false );
					paper2.setVisible( false );
					rock1.setVisible( false );
					rock2.setVisible( false );
				}

				textBox.setText("Player 1 wins! " + shot1.compareShotToAsString(shot2));
				player1Rect[win1].setBackground( Color.RED );
				win1++;
				
				// Show who is the winner after the game has been played
				if ( win1 == 10 && win2 != 10 ) {
					textBox.setText("Player 1 wins! " + win1 + " to " + win2 );
				}
				else if ( win2 == 10 && win1 != 10 ){ 
					textBox.setText("Player 2 wins! " + win2 + " to " + win1 );
				}

			}
			else if ( shot1.compareShotTo( shot2 ) < 0 ){
				// clear the lines from the pictures
				if ( shot1.compareShotTo( shot2 ) == 1 ||
						shot1.compareShotTo( shot2 ) == -1 ){// Scissors cuts paper

					scissor1.setVisible( true );

					scissor2.setVisible( false );
					rock1.setVisible( false );
					rock2.setVisible( false );
					lizard1.setVisible( false );
					lizard2.setVisible( false );
					paper1.setVisible( false );
					paper2.setVisible( false );
					spock1.setVisible( false );
					spock2.setVisible( false );
				}
				else if ( shot1.compareShotTo( shot2 ) == 2 ||
						shot1.compareShotTo( shot2 ) == -2 ){ // Scissors decapitates Lizard

					scissor2.setVisible( true );

					scissor1.setVisible( false );
					rock1.setVisible( false );
					rock2.setVisible( false );
					lizard1.setVisible( false );
					lizard2.setVisible( false );
					paper1.setVisible( false );
					paper2.setVisible( false );
					spock1.setVisible( false );
					spock2.setVisible( false );
				}
				else if ( shot1.compareShotTo( shot2 ) == 3 ||
						shot1.compareShotTo( shot2 ) == -3 ){ // Paper disproves spock

					paper1.setVisible( true) ;

					scissor1.setVisible( false );
					scissor2.setVisible( false );
					rock1.setVisible( false );
					rock2.setVisible( false );
					lizard1.setVisible( false );
					lizard2.setVisible( false );
					paper2.setVisible( false );
					spock1.setVisible( false );
					spock2.setVisible( false );
				}
				else if ( shot1.compareShotTo( shot2 ) == 4 ||
						shot1.compareShotTo( shot2 ) == -4 ){ // Paper covers rock

					paper2.setVisible( true );

					scissor1.setVisible( false );
					scissor2.setVisible( false );
					rock1.setVisible( false );
					rock2.setVisible( false );
					lizard1.setVisible( false );
					lizard2.setVisible( false );
					paper1.setVisible( false );
					spock1.setVisible( false );
					spock2.setVisible( false );
				}
				else if ( shot1.compareShotTo( shot2 ) == 5 ||
						shot1.compareShotTo( shot2 ) == -5 ){ // Rock crushes Scissors

					rock2.setVisible( true );

					rock1.setVisible( false );
					scissor1.setVisible( false );
					scissor2.setVisible( false );
					lizard1.setVisible( false );
					lizard2.setVisible( false );
					paper1.setVisible( false );
					paper2.setVisible( false );
					spock1.setVisible( false );
					spock2.setVisible( false );
				}
				else if ( shot1.compareShotTo( shot2 ) == 6 ||
						shot1.compareShotTo( shot2 ) == -6 ){ // Rock crushes lizard

					rock1.setVisible( true );

					rock2.setVisible( false );
					scissor1.setVisible( false );
					scissor2.setVisible( false );
					lizard1.setVisible( false );
					lizard2.setVisible( false );
					paper1.setVisible( false );
					paper2.setVisible( false );
					spock1.setVisible( false );
					spock2.setVisible( false );
				}
				else if ( shot1.compareShotTo( shot2 ) == 7 ||
						shot1.compareShotTo( shot2 ) == -7 ){ // Lizard eats paper

					lizard1.setVisible( true );

					lizard2.setVisible( false );
					scissor1.setVisible( false );
					scissor2.setVisible( false );
					rock1.setVisible( false );
					rock2.setVisible( false );
					paper1.setVisible( false );
					paper2.setVisible( false );
					spock1.setVisible( false );
					spock2.setVisible( false );
				}
				else if ( shot1.compareShotTo( shot2 ) == 8 ||
						shot1.compareShotTo( shot2 ) == -8 ){ // Lizard poisons spock

					lizard2.setVisible( true );

					lizard1.setVisible( false );
					scissor1.setVisible( false );
					scissor2.setVisible( false );
					rock1.setVisible( false );
					rock2.setVisible( false );
					paper1.setVisible( false );
					paper2.setVisible( false );
					spock1.setVisible( false );
					spock2.setVisible( false );
				}
				else if ( shot1.compareShotTo( shot2 ) == 9 ||
						shot1.compareShotTo( shot2 ) == -9 ){ // Spock vaporizes Rock

					spock2.setVisible( true );

					spock1.setVisible( false );
					scissor1.setVisible( false );
					scissor2.setVisible( false );
					lizard1.setVisible( false );
					lizard2.setVisible( false );
					paper1.setVisible( false );
					paper2.setVisible( false );
					rock1.setVisible( false );
					rock2.setVisible( false );
				}
				else if ( shot1.compareShotTo( shot2 ) == 10 ||
						shot1.compareShotTo( shot2 ) == -10 ){ // Spock smashes scissors

					spock1.setVisible( true );

					spock2.setVisible( false );
					scissor1.setVisible( false );
					scissor2.setVisible( false );
					lizard1.setVisible( false );
					lizard2.setVisible( false );
					paper1.setVisible( false );
					paper2.setVisible( false );
					rock1.setVisible( false );
					rock2.setVisible( false );
				}
				textBox.setText( "Player 2 wins! " + shot1.compareShotToAsString(shot2) );
				player2Rect[win2].setBackground( Color.BLUE );
				win2++;
				
				// Show who is winning after the the game has been played
				if ( win1 == 10 && win2 != 10 ) {
					textBox.setText("Player 1 wins! " + win1 + " to " + win2 );
				}
				else if ( win2 == 10 && win1 != 10 ){ 
					textBox.setText("Player 2 wins! " + win2 + " to " + win1 );
				}
				
			}
			else if ( shot1.compareShotTo(shot2 ) == 0 ){ // Draw
				
				// Show all the lines if a draw
				spock1.setVisible( true );
				spock2.setVisible( true );
				scissor1.setVisible( true );
				scissor2.setVisible( true );
				lizard1.setVisible( true );
				lizard2.setVisible( true );
				paper1.setVisible( true );
				paper2.setVisible( true );
				rock1.setVisible( true );
				rock2.setVisible( true );
				
				textBox.setText( shot1.compareShotToAsString( shot2 ) );
				
			}
		}
		else {
			
		}


		// Always repaint after handling a button

		window.repaint();
	}
}
