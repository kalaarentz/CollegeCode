import java.util.HashMap;


/**
 * Program Description:
 *
 * Date Last Modified: Nov 22, 2015
 *
 * @author: kalaarentz
 */
public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		CompressUncompressAssignment example = new CompressUncompressAssignment();
		example.compressFile( "practice.txt", "outputPrac.txt" );
		example.decompressFile( "outputPrac.txt", "practiceDecompress.txt" );
		
//		HashMap<String,String> table = example.getDecodedHashMap();
//
//		for( String words: table.keySet() )
//		{
//			System.out.println( "The key ( " + words 
//					+ " ) has the frequency of " 
//					+ table.get( words ) );
//		}
		
//		HashMap<String,Integer> table = example.getOccurances( "practice.txt" );
//
//		for( String words: table.keySet() )
//		{
//			System.out.println( "The key ( " + words 
//					+ " ) has the frequency of " 
//					+ table.get( words ) );
//		}
//		
//		HashMap<String,String> encodings = example.getEncodings( "practice.txt" );
//		
//		System.out.println("-----------------------");
//		
//		for( String words: encodings.keySet() )
//		{
//			System.out.println( "The key ( " + words 
//					+ " ) has a encoding as [ " + encodings.get( words ) + " ]" );
//		}
//		
//		/* DEBUGG FOR MY ASCII ARRAY
//		for( int i = 0; i < example.getASCII().length; i++ )
//		{
//			System.out.println( example.getASCII()[ i ] );
//		}
//		*/
//		
//
//		example.compressFile( "practice.txt", "outputPrac.txt" );
//
//		//System.out.println( example.getQueue().peek().getWord()  );
//		System.out.println( "size of queue: " + example.getQueue().size() );
//		System.out.println( "________________________________________" );
//
//		
//		while( example.getQueue().size() > 0 )
//		{
//			StringValuePair svp = example.getQueue().poll();
//
//			System.out.println( "Word [" + svp.getWord() + "] " 
//					+ "and the Impact Factor ( " + svp.getImpactFactor() + " )" );
//		}
//
//		example.getEncodings( "practice.txt" );
	}

}
