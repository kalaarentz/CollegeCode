/*
 * Author: Andrew Berns
 *
 * Date: Apr 21, 2015
 *
 * Purpose: an interface for the "traffic jam" ADT
 */
package lab5;

public interface TrafficJam<E> extends Iterable<E>
{
	
    int getLaneCount( );
    
    void add( E element, int lane );
    
    E remove( );
    
    void shuffle( );
    
    // As this interface exstends Iterable<E>, there must also be a method
    // Iterator<E> iterator( );
}