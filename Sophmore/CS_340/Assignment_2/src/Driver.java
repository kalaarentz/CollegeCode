import java.util.Iterator;
import java.util.ListIterator;


/**
 * Program Description:
 *
 * Date Last Modified: Oct 7, 2015
 *
 * @author: kalaarentz
 */
public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		MultiArrayList<Integer> example = new MultiArrayList<>();
		
		example.add( 10 );
		example.add( 33 );
		example.add( 1 );
		example.add( 7 );
		example.add( 9 );
		example.add( 16 );
		example.add( -54 );
		example.add( 88 );
		example.add( 14 );
		
		System.out.println();
		System.out.println( "Orginal Information" );

		
		System.out.println("----------------------------" );
		System.out.println("unsorted list: " + example.toString() );
		System.out.println("----------------------------" );
		System.out.println("sorted list: " + example.toStringSorted() );
		System.out.println("----------------------------" );
		
		//System.out.println("Contains: " + example.contains( -54) );
		
		example.removeLastSorted();
		System.out.println( "Removing First" );
		System.out.println("----------------------------" );
		System.out.println("unsorted list: " + example.toString() );
		System.out.println("----------------------------" );
		System.out.println("sorted list: " + example.toStringSorted() );
		System.out.println("----------------------------" );
		

		/*
		System.out.println("Removing items with iterator" );
		System.out.println("----------------------------" );

		/*
		MultiLinkedList<Integer> example = new MultiLinkedList<>();

		example.add( 9 );
		example.add( 32);
		example.add( 17 );
		example.add( 6 );

		System.out.println( "Orginal Information" );

		System.out.println("----------------------------" );
		System.out.println("unsorted list: " + example.toString() );
		System.out.println("----------------------------" );
		System.out.println("sorted list: " + example.toStringSorted() );
		System.out.println("----------------------------" );

		System.out.println("Removing items with iterator" );
		System.out.println("----------------------------" );


		Iterator<Integer> itr = example.iterator();

		itr.remove();
		System.out.println("unsorted list: " + example.toString() );
		System.out.println("----------------------------" );
		System.out.println("sorted list: " + example.toStringSorted() );
		System.out.println("----------------------------" );


		/*
		System.out.println( "ListIterator" );
		System.out.println("----------------------------" );
		ListIterator<Integer> itr = example.unsortedListIterator();

		while( itr.hasNext() )
		{
			System.out.println( itr.next() );
		}

		/*
		System.out.println( "Removing Items" );


		System.out.println("----------------------------" );
		example.removeFirst();
		System.out.println("unsorted list Remove first: " + example.toString() );
		System.out.println("sorted list Remove First:" + example.toStringSorted() );


		System.out.println("----------------------------" );
		example.removeFirstSorted();
		System.out.println("unsorted list Remove first Sorted: " + example.toString() );
		System.out.println("sorted list Remove First Sorted:" + example.toStringSorted() );

		/*
		System.out.println("----------------------------" );
		example.removeLast();
		System.out.println("unsorted list Remove last: " + example.toString() );
		System.out.println("sorted list Remove last:" + example.toStringSorted() );

		System.out.println("----------------------------" );
		example.removeLastSorted();
		System.out.println("unsorted list Remove last Sorted: " + example.toString() );
		System.out.println("sorted list Remove last Sorted:" + example.toStringSorted() );
		System.out.println("----------------------------" );
		 */



	}

}
