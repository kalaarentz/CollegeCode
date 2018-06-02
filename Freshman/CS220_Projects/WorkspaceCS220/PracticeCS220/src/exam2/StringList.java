/*
 * Author: Andrew Berns
 *
 * Date: Apr 2, 2015
 *
 * Purpose: (a subset of) our interface for the list storing Strings
 */
package exam2;

public interface StringList
{
    // Adds the given String at position 'pos' (the String will be inserted
    // "before" the element currently at position 'pos', shifting everything
    // after one "spot")
    void add( String item, int pos );
    
    // Return the element at the given position
    String get( int pos );
    
    // Return the element at the given position and remove it from the list
    String remove( int pos );
    
    // Get the size of the list
    int size( );
    
    void swapHalfs();

	LinkedStringList sublist(int i, int j);
}