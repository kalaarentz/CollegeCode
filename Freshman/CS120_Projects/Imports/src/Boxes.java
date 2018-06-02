/*
  *This program will use the galapagos file to use the 
  *drawing turtle to draw a square. Inside this square there
  *will be an inscribed diamond.
  * 
  * Last Modified <Sept. 12, 2014>
  * author <Kala Arentz>
  */
import java.awt.Color;

import galapagos.Turtle;

public class Boxes {

	public static void main(String[] args) {
		// Create new Turtle pen
		Turtle pen;
		
		// Create drawing turtle
		pen = new Turtle();
		// Change the color 
		pen.penColor(null);
		// Change the pen size
		pen.penSize(4);
		// Change speed of Turtle
		pen.speed(200);
		// Draw the square
		pen.move(100);
		pen.turn(90);
		pen.move(100);
		pen.turn(90);
		pen.move(100);
		pen.turn(90);
		pen.move(100);
		// Pick up the pen
		pen.penUp();
		// Move the turtle inside the square
		pen.turn(90);
		pen.move(50);
		pen.turn(90);
		pen.move(15);
		// Put the pen back down
		pen.penDown();
		// Draw the diamond inscribed in the square
		pen.turn(45);
		pen.move(50);
		pen.turn(-90);
		pen.move(50);
		pen.turn(-90);
		pen.move(50);
		pen.turn(-90);
		pen.move(50);
		// Pick up the pen
		pen.penUp();
		// Move turtle off the picture
		pen.turn(45);
		pen.move(75);
		
	}

}
