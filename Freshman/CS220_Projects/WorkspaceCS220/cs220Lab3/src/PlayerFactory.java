/*
 * Author: Andrew Berns
 *
 * Date: Mar 31, 2015
 *
 * Purpose: a "factory" for generating instances of your player
 */

public class PlayerFactory implements ShutTheBoxPlayer
{
    public static ShutTheBoxPlayer getPlayer( )
    {
        // Just replace this line with an instantiation and return of your
        // ShutTheBoxPlayer.
    	ShutTheBoxPlayer player = new PlayerFactory( );
        return player;
    }
	private String name;

	/**
     * Returns the name to be displayed for this player on the "leaderboard".
     * Null should be returned if no name should be displayed.
     * <p>
     * Note the name is only used for the leaderboard, and you may make it any
     * <i>appropriate</i> identifier you wish.
     * 
     * @return the name to display for the player (null for no display)
     */
    public String getDisplayName( )
    {
    	name = "Phoenix Rising!";
    	return name;
    }
    
    /**
     * This method represents the play of a player for a particular dice roll.
     * It is given the values of at-most-two die, and a boolean array
     * representing the tiles that are still raised (a value of 'true' at index
     * i represents the tile with value (i+1) still being raised).  The method
     * shall not modify the input arrays.
     * <p>
     * The method returns a boolean array representing the new tile state,
     * again with the convention of 'true' at index i representing tile with
     * value (i+1) being raised.  If no change was made, the player's turn is
     * "over".
     * <p>
     * The method does not return an array with elements set to 'true'
     * which were 'false' in the input.  The method does not return closed 
     * tiles which do not add up to the sum of the given dice.
     * 
     * @param diceValues The values of the dice that were rolled. May be of 
     *                   length 1 or 2.
     * @param tiles The state of the tiles ('true' at index i means tile (i+1)
     *              is raised).  This array may be of arbitrary length,
     *              representing games with various numbers of tiles.
     * @return The new state of the tiles ('true' at index i means tile (i+1)
     *         is raised)
     */
    public boolean[] getPlay( int[] diceValues, boolean[] tiles )
    {
    	// result of the two diceValues
    	int result = 0;
    	for( int i = 0; i < diceValues.length; i++ )
    	{
    		result = diceValues[ i ] + result;
    	}
    	// create new tiles that represents new state of board
    	boolean[] changedTiles = new boolean[ tiles.length ];
    
    	// copy over the changedTiles 
    	for( int j = 0; j < changedTiles.length; j++ )
    	{
    		changedTiles[ j ] = tiles[ j ];
    	}
    	// find the tiles that are still raised
    	for( int idx = 0; idx < tiles.length; idx++ )
    	{
    		if( tiles[ idx ] == true )
    		{
    			if( idx + 1 == result )
    			{
    				changedTiles[ idx ] = false;
    			}
    		}
    	}
    	// determine what tiles can be closed from diceValues
    	// change the state of the game by putting down tiles still raised
    	
    	return changedTiles;
    }
    
    /**
     * This method is used to determine how many die the player wishes to roll.
     * It is given the current state of the board as an array of booleans, with
     * a value of 'true' at index i representing the tile with value (i+1) is
     * raised.  The method shall not change the input parameter.
     * <p>
     * The method should return either 1 or 2, denoting how many die the player
     * wishes to roll for their next turn, which will have the given state.
     * 
     * @return The number of die that the player wishes to roll (1 or 2)
     * @param tiles The state of the tiles ('true' at index i means tile (i+1)
     *              is raised). This parameter may be of arbitrary length for
     *              various game setups.
     */
    public int getRollCount( boolean[] tiles )
    {
    	// the number to return for dice, either 2 or 1
    	int dice = 0;
    	// result is for the number that the tiles add up to
    	// if number is greater then 6, then you will want two dice
    	// if number is less then 6 then you will want only one dice
    	int result = 0;
    	// Iterate through boolean tiles
    	for( int idx = 0; idx < tiles.length; idx++ )
    	{
    		if( tiles[ idx ] == true )
    		{
    			result = idx + 1;
    			// if idx 6, 7, or 8 is true means two dice
    			if( idx >= 6 )
    			{
    				return 2;
    			}	
    		}
    	}
    	
    	// check the results
    	if( result >= 7 )
    	{
    		dice = 2;
    	}
    	else{
    		
    		dice = 1;
    		
    	}
    
    	return dice;
    }
}
