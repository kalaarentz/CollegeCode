import javax.swing.JComponent;

/**
 * The planet class will represent each of the five celestial bodies in our 
 * simulation. This class will not only allow us to draw the image of the planet, but also
 * track the orbit information about the planet. It will also provide the ability to 
 * center the planet at a given x and y position.
 * 
 * DATE LAST MODIFIED <December 11, 2014>
 * 
 * @author kalaarentz
 *
 */
public class Planet extends ImageLabel{

	// Planet Name
	private String planetName;

	// Planet description
	private String planetInfo;

	// How many days in a year for the planet (days are measured like Earth days)
	private double orbitalPeriod;

	// how many times the planet has gone around the sun (years)
	private int completeOrbits = 0;

	// distance from the star
	private double distanceFromSun;
	
	// Degrees in orbit
	private int degreeInOrbit = 0;

	
	/**
	 * Will setup a Planet object. 
	 * The object will have at its center a star located at ( xPos, yPos ), so 
	 * the planet should be position (initially) at ( xPos + distance, yPos).
	 * 
	 * @param filename: The filename of the image associated with this planet
	 * @param scale: How much to scale the image form the file
	 * @param name: Name of the planet
	 * @param xPos: X Position of the star it revolves around
	 * @param yPos: Y Position of the star it revolves around
	 * @param distance: Distance from the star (million km)
	 * @param period: Time to revolve around the star exactly one time (days)
	 */
	public Planet( String filename, double scale, String name, double xPos,
			double yPos, double distance, double period ) {
		super( filename, scale );

		planetName = name;
		orbitalPeriod = period;
		distanceFromSun = distance;
		
	}

	/**
	 * Return the name of the planet
	 * @return string: The name of the planet
	 */
	public String getName() {

		return planetName;
		
	}

	/**
	 * Set a description for the planet
	 * 
	 * @param desc: Text description of the planet
	 */
	public void setDescription( String desc ) {
		planetInfo = desc;
		
	}

	/**
	 * Return the description associated with the planet
	 * @return description that is associated with planet
	 */
	public String getDescription() {

		return planetInfo;
		
	}

	/**
	 * Override this method to return a string with the planet name and the number of 
	 * (whole) years that have passed on this planet since the start of the simulation.
	 * For example, "Earth (1 year)" or "Mars (10 years)"
	 */
	public String toString() {

		return getName() + " (" + completeOrbits + " years)";
		
	}

	/**
	 * Reset the position of the planet to that starting position, and clear any orbit 
	 * information. Does not clear the name or description information. 
	 */
	public void resetPostion() {
		
		degreeInOrbit = 0;
		completeOrbits = 0;
		
	}

	/**
	 * Advance time in our simulation by one step. You will call this once each time the
	 * RepeatButton fires.
	 * 
	 * This method will calculate the position of the planet based on its current
	 * degree in orbit, orbital velocity, and distance from the star it is orbiting. 
	 * 
	 * @param star: The star the planet is orbiting (SUN)
	 */
	public void advanceTime ( Planet star ) {
		
		// Orbital velocity
		double orbitalVelocity;
		orbitalVelocity = 360/orbitalPeriod;
		
		// PlanetPosition
		double planetPosition;
		planetPosition = degreeInOrbit * orbitalVelocity;
		
		setLocationCentered( (int)( star.getCenteredX() + ( distanceFromSun 
				* Math.cos( Math.toRadians( planetPosition ) ) ) ), 
				(int)( star.getCenteredY() - ( distanceFromSun * 
				Math.sin( Math.toRadians( planetPosition ) ) ) ) );

		degreeInOrbit++;
		
		if ( degreeInOrbit >= 360/( orbitalVelocity ) ){
			completeOrbits++;
			degreeInOrbit = 0;
		}
		
	}

	/**
	 * Return the number of complete orbits of the star (a.ka., number of years on
	 * this planet)
	 * 
	 * @return number of years on the planet ( how many times it has orbited star )
	 */
	public int getNumberOfCompletOrbits() {
		
		return completeOrbits;
		
	}

	/**
	 * This method will use the setLocation method to set the planet's location to be 
	 * centered at the point (x, y).
	 * 
	 * @param x: X Position to be passed to set location
	 * @param y: Y Position to be passed to set location
	 */
	public void setLocationCentered( int x, int y ) {
		setLocation( x - ( this.getWidth()/2 ), y - ( this.getHeight()/2 ) );
		
	}

	/**
	 * This method returns the x position at the center of the object
	 * @return x position at the center of the planet object
	 */
	public int getCenteredX() {

		return this.getX() + ( this.getWidth()/2 );
		
	}

	/**
	 * This method returns the y position at the center of the object.
	 * @return y position at the center of the planet object
	 */
	public int getCenteredY() {

		return this.getY() + ( this.getHeight()/2 );
		
	}



}
