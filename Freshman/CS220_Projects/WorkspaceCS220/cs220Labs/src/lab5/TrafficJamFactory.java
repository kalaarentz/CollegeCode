/*
 * Author: Andrew Berns
 *
 * Date: Apr 21, 2015
 *
 * Purpose: the factory for building your traffic jam object
 */
package lab5;

public class TrafficJamFactory
{
    // A generic method.  Inside this method, 'E' will be whatever type
    // parameter the method is given.
    public static <E> TrafficJam<E> getTrafficJam( )
    {
        // Replace this with the return of an instantiation of your 
        // TrafficJam object
    	
        return new Hwy16<E> () ;
        
    }
}