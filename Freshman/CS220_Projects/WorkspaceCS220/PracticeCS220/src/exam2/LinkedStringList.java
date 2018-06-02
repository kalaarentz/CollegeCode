/*
 * Author: Andrew Berns
 *
 * Date: Apr 2, 2015
 *
 * Purpose: an implementation of our StringList using doubly-linked lists.
 */
package exam2;

import java.util.Random;

public class LinkedStringList implements StringList
{
    // We'lll use two sentinels: a head node and a tail node.  These will never
    // be deleted.  They simplify the processing we'll do later (eliminate some
    // checks for 'edge cases' where the head or tail would be null without a
    // sentinel)
    private StringNode head = new StringNode( "HEAD" );
    private StringNode tail = new StringNode( "TAIL" );
    
    // Initially our list has nothing in it.
    private int  size = 0;
    
    // When created, a StringList has nothing in it, just a head and tail
    // sentinel node connected together
    public LinkedStringList( )
    {
        head.setNext( tail );
        tail.setPrev( head );
    }
    
    // Inserts the given String at the given position
    public void add( String element, int pos )
    {
        // Find the node at the given position
        StringNode cursor = getNodeAt( pos );
        
        // Create the new node to insert, placing the given element into the
        // node.  This node's next pointer should be the cursor, and its prev
        // reference should be to the cursor's previous
        StringNode newNode = new StringNode( element );
        newNode.setPrev( cursor.getPrev( ) );
        newNode.setNext( cursor );
        
        // Now set the next reference of the node's previous neighbor to be
        // the node.
        newNode.getPrev( ).setNext( newNode );
        
        // Set the prev reference of the node's next neighbor to the new node
        newNode.getNext( ).setPrev( newNode );
        
        // Add one to our size
        size++;
    }
    
    // Returns the String at the given position
    public String get( int pos )
    {
        // Get the node at the given position, and then return the element in
        // that node
        return getNodeAt( pos ).getElement( );
    }
    
    // Remove the element at the given position and return it
    public String remove( int pos )
    {
        // A bit of error checking (we'll need more in a full version!) to make
        // sure we don't try and delete from an empty list
        if( size == 0 )
        {
            return null;
        }
        
        // Get the node at the given position
        StringNode cursor = getNodeAt( pos );
        
        // Update the references for the nodes before and after the cursor so
        // the list "bypasses" this cursor
        cursor.getPrev( ).setNext( cursor.getNext( ) );
        cursor.getNext( ).setPrev( cursor.getPrev( ) );
        
        // Decrement the size
        size--;
        
        // Return the element at the cursor node
        return cursor.getElement( );
    }
    
    // Returns the size of the list
    public int size( )
    {
        return size;
    }
    
    /**
	 *  This method takes no parameters and returns nothing. When called, it 
	 *  makes the first half of the list move to the “end”, and the second 
	 *  half of the list become the “beginning”. The first half consists of 
	 *  ⌊𝑛/2⌋ elements (e.g. if the list length is odd, the ‘extra’ element
	 *   is from the original second half).
	 *   
	 *   Examples 
	 *   (A, B, C, D, E, F)
	 *   		(D, E, F, A, B, C)
	 *   (A, B, C, D, E)
	 *   		(C, D, E, A, B)
	 */
	public void swapHalfs( )
	{
		StringNode temp = getNodeAt( size/2 );
		//System.out.println( "Node at half point: " + temp.getElement() );
		tail.setNext( head );
		temp.getPrev().setNext(null);
		tail.getPrev().setNext( head.getNext() );
		head.setNext(temp);
		
	}
	
	public LinkedStringList sublist( int start, int end )
	{
		LinkedStringList newList = new LinkedStringList();
		StringNode cursor = getNodeAt( start );
		
		newList.add( cursor.getElement(), 0 );
		
		for( int i = 1; i < end - start; i++ )
		{
			
			newList.add( cursor.getNext().getElement(), i );
			
		}
		
		return newList;
	}

	// A helper method which returns a reference to the node at the given pos
    private StringNode getNodeAt( int pos )
    {
        // Start at the first node in the list, which is the node immediately
        // after the tail
        StringNode cursor = head.getNext( );
        
        // As long as position is greater than 0, subtract 1 from pos...
        while( pos-- > 0 )
        {
            // ... and move the cursor to the next node
            cursor = cursor.getNext( );
        }
        
        // Return the node
        return cursor;
    }
    
    // This is a private inner class.  Think of it like a helper 'class', much
    // like we've seen private helper methods.  Users of the public class don't
    // know nor care that this exists -- it is only there to help our linked
    // list implement the List interface.
    private class StringNode
    {
        // The "node" in a linked list consists of the element/value it holds,
        // as well as a reference to the next and previous node in the list
        // (a singly-linked list would have only the 'next' - this is a doubly
        // linked list).
        private String element;
        private StringNode next, prev;
        
        // When created, the StringNode is given the element it should hold
        public StringNode( String e )
        {
            element = e;
        }
        
        // Returns the next node (from 'next' reference)
        public StringNode getNext( )
        {
            return next;
        }
        
        // Returns the previous node (referred to with the 'prev' reference)
        public StringNode getPrev( )
        {
            return prev;
        }
        
        // Sets the node to point to 'n' as the next node
        public void setNext( StringNode n )
        {
            next = n;
        }
        
        // Sets the node's previous reference to 'n'
        public void setPrev( StringNode n )
        {
            prev = n;
        }
        
        // Returns the element the node is holding
        public String getElement( )
        {
            return element;
        }
    }
    
    // A main method to demo our linked list
    public static void main( String[] args )
    {
        // This code was for motivating our discussion of generics - everything
        // instance is an Object in Java, which is great for polymorphism, but
        // we lose the ability to call specific methods until we do a cast.
        // Remember, "the type of the reference determines what methods *can*
        // be called, while the type of the instance determines what code is
        // actually *run*.
    	
        Object gen = new Random( );
        if( gen instanceof Random )
        {
            ((Random)gen).nextInt( );
        }
        
        // We'll make an instance of our StringList and then add some values to
        // it.
        StringList l = new LinkedStringList( );
        l.add( "A", 0 );
        l.add( "B", 1 );
        l.add( "C", 2 );
        l.add("D", 3);
        l.add("E", 4);
        //l.add("F", 5 );
        
        // We'll then print out all our elements in order
        for( int i = 0; i < l.size( ); i++ )
        {
            System.out.println( l.get( i ) );
        }
      
        StringList example = l.sublist( 1,3 );
        
        System.out.println("sublist");
        for( int i = 0; i < example.size( ); i++ )
        {
            System.out.println( example.get( i ) );
        }
        
        
        
        
        // this where i sprint off new swapHalfs list
        
        // Then we'll start removing elements from the back, and repeat until
        // the list is empty.
        /*
        while( l.size( ) > 0)
        {
            System.out.println( l.remove( l.size( ) - 1 ) );
        }
        */
        
        // This is an example of instantiating an object with a generic.  Here,
        // we are saying we want a List object whose element is of type
        // Random.  As we'll see after exam 2, this allows us to forget about
        // casts and instanceof.
        //List<Random> listORandoms;
    }
}
    
    // Just something to show how our LIst of Random objects can take Random
    // objects as input, and will return an object of type Random
    /*
    private static void demoGenerics( List<Random> list )
    {
        list.add( new Random( ), 0 );
        list.get( 0 ).nextInt( 3 );
    }
    */
