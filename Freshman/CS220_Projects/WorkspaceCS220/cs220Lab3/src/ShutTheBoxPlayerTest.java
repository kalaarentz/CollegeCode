/*
 * Author: Andrew Berns
 *
 * Date: Mar 31, 2015
 *
 * Purpose: simple check to make sure the interface was followed.
 */
//package lab3;

import java.util.Random;
import java.util.Arrays;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

public class ShutTheBoxPlayerTest
{
    private ShutTheBoxPlayer player;
    private Random generator = new Random( );
    
    // Create a new player by using the PlayerFactory, and ensure it is a new
    // instance, not a copy of an existing one.
    @Before
    public void setUp( )
    {
        player = PlayerFactory.getPlayer( );
        String errorMsg = "Calls to PlayerFactory.getPlayer( ) should return "
                + "new instances.";
        assertNotSame( errorMsg, player, PlayerFactory.getPlayer( ) );
    }
    
    // Make sure the methods exist with the same signature
    @Test
    public void methodsExist( )
    {
        player.getPlay( new int[]{2, 4}, new boolean[]{true, false} );
        player.getRollCount( new boolean[]{false, false, false} );
        player.getDisplayName( );
    }
    
    // Verify that the methods are not changing the inputs, only copies
    // of them
    @Test
    public void methodsDoNotChangeInputs( )
    {
        int[] diceRolls = {2, 3};
        int[] diceRollsCopy = Arrays.copyOf( diceRolls, diceRolls.length );
        
        boolean[] tiles = {true, true, true, true, true, true, true, true};
        boolean[] tilesCopy = Arrays.copyOf( tiles, tiles.length );
        
        player.getPlay( diceRolls, tiles );
        assertTrue( "Do not change inputs in your methods",
                Arrays.equals( tilesCopy, tiles ) );
        assertArrayEquals( "Do not change inputs in your methods",
                diceRollsCopy, diceRolls );
    }
    
    // Verify that getRollCount returns a value of 1 or 2
    @Test
    public void legalRollCounts( )
    {
        // Run 'getRollCount' 1000 times, making sure each time the value is
        // either 1 or 2
        for( int i = 0; i < 1000; i++ )
        {
            boolean[] tiles = getRandomBooleanArray( );
            
            int rolls = player.getRollCount( tiles );
            assertTrue( "getRollCount must return 1 or 2", 
                    rolls == 1 || rolls == 2 );
        }
    }
    
    // Verify that all moves are 'valid' -- either no tile is put down, or the
    // tiles sum to the dice roll amount
    @Test
    public void onlyValidDiceRollCounts( )
    {
        // Check every possible move with 2 dice
        for( int i = 0; i < 36; i++ )
        {
            // Make the dice rolls and tiles
            int[] diceRoll = { (i / 6) + 1, (i % 6) + 1 };
            boolean[] tiles = getRandomBooleanArray( );

            // Verify the game is valid for the given inputs
            verifyGame( diceRoll, tiles );
        }
        
        // Check every case for a single die
        for( int i = 1; i <= 6; i++ )
        {
            // Make the roll and tile arrays
            int[] diceRoll = { i };
            boolean[] tiles = getRandomBooleanArray( );
            
            // Verify the game worked
            verifyGame( diceRoll, tiles );
        }
    }
    
    // Helper method for verifying a game returns a valid sum of removed tiles
    // for the given inputs
    private void verifyGame( int[] diceRoll, boolean[] tiles )
    {
        // "Reset" the player
        setUp( );
        
        // Copy the inputs just in case the player incorrectly changes them
        int[] rollCopy = Arrays.copyOf( diceRoll, diceRoll.length );
        boolean[] tilesCopy = Arrays.copyOf( tiles, tiles.length );

        // Get their choice and verify it's the same length
        boolean[] newTiles = player.getPlay( diceRoll, tiles );
        String tileOutputError = "Your method should return an array with "
                + "the same length as the input.";

        assertEquals( tileOutputError, tiles.length, newTiles.length );

        // Verify that (i) nothing was changed, and (ii) if they made a
        // move, they completely "exhausted" the dice
        String dupError = "Your method should not change the inputs.";
        assertArrayEquals( dupError, diceRoll, rollCopy );
        assertTrue( dupError, Arrays.equals( tiles, tilesCopy ) );

        int sum = 0;

        // Sum up the flipped tiles
        for( int j = 0; j < newTiles.length; j++ )
        {
            if( newTiles[j] != tiles[j] )
            {
                // Make sure they went from 'up' to 'down'
                assertFalse( newTiles[j] );

                // add the tile to the sum
                sum += (j + 1);
            }
        }

        // Make sure the sum of the flips was either 0, or the sum of
        // the dice
        String rollsDone = "Your output should flip either no tiles, or "
                + "flip exactly enough tiles to equal the sum of the die";

        int diceRollTotal = 0;

        for( int i = 0; i < rollCopy.length; i++ )
        {
            diceRollTotal += rollCopy[i];
        }

        assertTrue( rollsDone, ( sum == 0 ) 
                || ( sum == ( diceRollTotal ) ) );
    }
    
    // Makes a random boolean array
    private boolean[] getRandomBooleanArray( )
    {
        // Generate a random-length boolean
        boolean[] tiles = new boolean[generator.nextInt( 10 ) + 10];
        
        // set the values randomly
        for( int j = 0; j < tiles.length; j++ )
        {
            tiles[j] = generator.nextBoolean( );
        }
        
        // return the array
        return tiles;
    }
}
