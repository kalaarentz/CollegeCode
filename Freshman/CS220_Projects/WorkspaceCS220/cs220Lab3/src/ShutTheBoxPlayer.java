/**
 * The interface defining a player of our 'shut the box' game.  It contains
 * the methods required for a player to play a match independently, without any
 * knowledge of other players' moves.
 * 
 * @author Andrew Berns
 */

public interface ShutTheBoxPlayer
{
    /**
     * Returns the name to be displayed for this player on the "leaderboard".
     * Null should be returned if no name should be displayed.
     * <p>
     * Note the name is only used for the leaderboard, and you may make it any
     * <i>appropriate</i> identifier you wish.
     * 
     * @return the name to display for the player (null for no display)
     */
    String getDisplayName( );
    
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
    boolean[] getPlay( int[] diceValues, boolean[] tiles );
    
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
    int getRollCount( boolean[] tiles );
}