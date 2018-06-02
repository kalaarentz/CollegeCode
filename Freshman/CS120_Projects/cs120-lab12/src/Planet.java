/**
 * This class will be a sub-class of ImageLabel. It will be extended to ImageLabel 
 * class so it will inherit all of the base functionality we need. DO NOT COPY ImageLabel
 * to create the planet class, must use inheritance. 
 * 
 * STEP ONE:
 * writing constructor of this method
 * 
 * STEP TWO: 
 * To set the location of the planet you will implement the following methods of the 
 * planet class
 *  	setLocationCentered
 *  	getCenteredX
 *  	getCenteredY
 * 
 * DATE LAST MODIFIED < December 5, 2014 >
 * @author kalaarentz
 *
 */
public class Planet extends ImageLabel {

	// Centered X and Y positions
	private int xCenter, yCenter;
	
	/**
	 * Setup of a planet object.
	 * The object will have at its center a star located at (xPos, yPos), so the planet
	 * should be positioned (initially) at (xPos + distance, yPos).
	 * 
	 * @param filename: The filename of the image associated with this planet
	 * @param scale: How much to scale the image from the file
	 * @param name: Name of the planet
	 * @param xPos: X Position of the star it revolves around
	 * @param yPos: Y Position of the star it revolves around
	 * @param distance: Distance from the star (million km)
	 * @param period: Time to revolve around the star exactly one time (days)
	 */
	public Planet( String filename, double scale, String name, double xPos, 
			double yPos, double distance, double period) {
		super(  filename,  scale );
	}
	
	/**
	 * This method will use the setLocation method to set the planet's location to be
	 * centered at the point (x, y).
	 * The x position to be passed to set location can be calculated as follows:
	 * 		x - (widthOfTheObject/2)
	 * The y position to be passed to set location can be calculated as follows:
	 * 		y - (heightOfTheObject/2)
	 * 
	 * @param x: X Position of that will be centered in the window
	 * @param y: Y Position of that will be centered in the window
	 */
	public void setLocationCentered( int x, int y ){
		
		setLocation( x - ( this.getWidth()/2 ),
				y - ( this.getHeight()/2 ) );
		
	}

	/**
	 * Returns the x position at the center of the object
	 * the x position can be calculated as follows:
	 * 		x + (widthOfTheObject/2)
	 * 
	 * @return: x position at the center of the object
	 */
	public int getCenteredX() {
		xCenter = this.getX() + (this.getWidth()/2);
		
		return xCenter;
	}
	
	/**
	 * This method returns the y position at the center of the object.
	 * The y position can be calculated as follows:
	 * 		y + (heightOfTheObjec/2)
	 * @return
	 */
	public int getCenteredY(){
		yCenter = this.getY() + ( this.getHeight()/2 );
		
		return yCenter;
	}
	                      
}

