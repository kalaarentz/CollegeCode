import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * Program Description: Used to test the information
 * 
 *
 * Date Last Modified: Oct 21, 2015
 *
 * @author: kalaarentz
 */
public class Driver {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) {

		Driver d = new Driver();

		//d.binarySearchTests();
		//d.stringKVListTests();
		//d.tallPrefixTreeTester();
		d.shortPrefixTreeTester();
	}

	/**
	 * this is the method that was used to test 
	 * the binary search tree methods
	 */
	public void binarySearchTests()
	{
		BinarySearchTree<Integer> tree = new BinarySearchTree<>();

		System.out.println( tree.toPrefixString() );
		System.out.println( "---------------------------" );

		tree.insert( 50 );
		tree.insert( 25 );
		tree.insert( 75 );
		tree.insert( 30 );
		tree.insert( 12 );
		tree.insert( 27 );
		tree.insert( 9 );
		tree.insert( 20 );
		tree.insert( 70 );
		tree.insert( 90 );
		tree.insert( 99 );

		System.out.println( tree.toPrefixString() );
		tree.writeDotFile( "tmp.dot" );
	}

	/**
	 * this is the method that was used to test 
	 * the string KV List methods
	 */
	public void stringKVListTests()
	{
		StringKVList<Integer> list = new StringKVList<>();

		list.insert( "A", 56 );
		list.insert( "CD", 32 );
		list.insert( "zEB", -1 );
		list.insert( "B", 69 );

		Iterator<Integer> itr = list.iterator();

		while( itr.hasNext() )
		{
			System.out.print( itr.next() + ", " );
		}
		System.out.println();

		System.out.println( "If it contains: " + list.contains( "b") );
		System.out.println( "Setting value: " + list.set( "A", -90 ) );
		//System.out.println( "Setting value: " + list.set( "c", -13 ) );
		System.out.println( "Getting a value: " + list.get( "A" ) );
		//System.out.println( "Getting a value: " + list.get( "c" ) );
		
		System.out.println( "--------------------" );
		
		Iterator<Integer> itr2 = list.iterator();
		
		while( itr2.hasNext() )
		{
			System.out.print( itr2.next() + ", ");
		}
		
		list.writeDotFile( "tmp.dot" );
	}
	
	/**
	 * this is the method that was used to test 
	 * the tall Prefix Tree tester for those methods
	 */
	public void tallPrefixTreeTester()
	{
		TallPrefixTree tree = new TallPrefixTree();
		
//		tree.insert( "arc" );
//		tree.insert( "icecream" );
//		tree.insert( "bag" );
//		tree.insert( "baggy" );
		
		tree.insert( "blah" );
		tree.insert( "blog" );
		
		System.out.println( tree.toPrefixString() );
		
		tree.writeDotFile( "tmp.dot" );
		tree.writeDotFile( "tmp2.dot", "bag" );
		
		LinkedList<String> listOfWords = tree.getMatches( "bla" );
		
		for( String s : listOfWords )
		{
			System.out.println( s.toString() );
		}
	}
	
	public void shortPrefixTreeTester()
	{
		ShortPrefixTree tree = new ShortPrefixTree();
		
		// testing the indexOfMatchedLetter--> had made it public but changed
		// it back to private method
		//System.out.println( tree.indexOfMatchedLetter( "ics", "icecream" ) ); 
		
//		tree.insert( "ice" );
//		tree.insert( "icke" );
//		tree.insert( "icicle");
//		tree.insert( "icabod" );
//		tree.insert( "bag" );
//		tree.insert( "baggy" );
//		tree.insert( "blah" );
		
		//System.out.println( tree.indexOfMatchedString( "ice", "icecream") );
		tree.insert( "ice" );
		tree.insert( "icecream" );
		tree.insert( "icecreamcone" );
		tree.insert( "ichabod" );
		tree.insert( "icicle" );
		tree.insert( "ideal" );
		tree.insert( "limb" );
		tree.insert( "loner" );
		tree.insert( "long" );
		tree.insert( "love" );
		tree.insert( "lovely" );
		tree.insert( "loveliness" );
		
		tree.writeDotFile( "tmp2.dot" );
		
	}
}
