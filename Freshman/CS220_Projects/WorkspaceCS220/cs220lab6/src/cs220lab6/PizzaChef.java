/*
Author: Kala Arentz

Date: May 5, 2015

Purpose: this is the Pizza chef that will have a constructor that gives you
the name and the temperature in which the pizza will be cooked. It will be 
use Runnable to have a one oven that has three different PizzaChef objects.

*/
package cs220lab6;

public class PizzaChef implements Runnable {

	private Oven oven; 
	private String name;
	private int temperature;
	private int minutes;
	
	public static void main(String[] args) {
		
		Oven oven1 = new Oven( );
		
		PizzaChef pizza1 = new PizzaChef( "Pepperoni", 400, 12 );
		PizzaChef pizza2 = new PizzaChef( "Stuffed Crust", 375, 20 );
		PizzaChef pizza3 = new PizzaChef( "Dessert", 425, 6 );
		
		pizza1.setOven( oven1 );
		pizza2.setOven( oven1 );
		pizza3.setOven( oven1 );
		
		Thread t1 = new Thread( pizza1 );
		Thread t2 = new Thread( pizza2 );
		Thread t3 = new Thread( pizza3 );
		
		t1.start();
		t2.start();
		t3.start();
		
	}

	/**
	 * Constructor of the Pizza Chef
	 * 
	 * @param type: Type of Pizza
	 * @param temp: temperature in which Pizza must be baked at
	 * @param bakeMinutes: how long the Pizza must be baked at
	 */
	public PizzaChef( String type, int temp, int bakeMinutes )
	{
		name = type;
		temperature = temp;
		minutes = bakeMinutes;
	}
	
	/**
	 * Creates the oven object that will be used by the PizzaChef object
	 * @param ov: Oven object
	 */
	public void setOven( Oven ov )
	{
		oven = ov;
	}
	
	/**
	 * will run the object Oven's bake method
	 */
	public void run( )
	{
		System.out.println( oven.bake( name, temperature, minutes) );
	}
}
