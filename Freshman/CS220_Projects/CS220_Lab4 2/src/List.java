/*
 * Author: Andrew Berns
 *
 * Date: Apr 2, 2015
 *
 * Purpose: a (partial) interface for the list ADT.  We can define it using
 *          generics, ensuring it will work for any subclass of Object.
 */


// The <E> is the generic type -- when created, we can specify what type the
// Object this List stores
public interface List<E>
{
    // Adds the given element at position 'pos', moving everything after that
    // point "down" one
    void add( E item, int pos );
    
    // Returns the element at position 'pos'
    E get( int pos );
    
    // Returns the element at position 'pos' and removes it from the list
    E remove( int pos );
    
    // Returns the size of the list
    int size( );
}
