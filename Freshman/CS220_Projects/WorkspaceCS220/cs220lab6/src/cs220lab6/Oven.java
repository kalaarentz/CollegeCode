/*
Author: Kala Arentz

Date: May 5, 2015

Purpose: This the oven that will be used by PizzaChef object.

*/
package cs220lab6;

public class Oven {

	private int internalTemp; 
	
	/**
	 * 
	 * @param item: the type of pizza
	 * @param bakeTemp: temperature the pizza must be baked at.
	 * @param bakeMinutes: how long the pizza must be baked at. 
	 * @return outcome: String that says "Baked ITEM at INTERNAL TEMPERATURE
	 * 				for BAKE MINUTES 
	 */
	public synchronized String bake( String item, int bakeTemp, int bakeMinutes )
	{
		internalTemp = bakeTemp;
		
		try
		{
			Thread.sleep( 100 * bakeMinutes);
		}
		catch( InterruptedException e )
		{
			System.out.println(" Pizza baking was interrupted!" );
		}
		
		String outcome = "Baked " + item + " at " + internalTemp + " for " 
		   + bakeMinutes;
		
		internalTemp = 0;
		
		return outcome;
	}
}
