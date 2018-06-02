import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;


/**
 * Program Description:
 *
 * Date Last Modified: Nov 22, 2015
 *
 * @author: kalaarentz
 */
public class CompressUncompressAssignment implements Assignment
{

	private HashMap<String, Integer> table;
	private HashMap<String, String> encodings;
	private HashMap<String, String> decodedString;
	private PriorityQueue<StringValuePair> wordsQ;
	private int[] ascii = new int[ 52 ]; 

	public CompressUncompressAssignment()
	{

		int idx = 97;

		// will make an array of ascii characters for the lowercase
		// alphabet and upercase alphabet

		for( int i = 0; i < 27 && idx < 123; i++ )
		{
			ascii[ i ] = idx;
			idx++;
		}

		idx = 65;
		for( int i = 26; i < 52 && idx < 91; i++ )
		{
			ascii[ i ] = idx;
			idx++;
		}
	}
	/* (non-Javadoc)
	 * @see Assignment#compressFile(java.lang.String, java.lang.String)
	 */
	@Override
	public void compressFile(String inputFilename, String outputFilename) {


		//STEP TWO
		getOccurances( inputFilename );

		makePriorityQueue();

		//STEP THREE 
		getEncodings( inputFilename );

		//STEP FOUR
		try
		{
			// will open file and read all the words
			// will put them through the makeHashTable method 
			// that will read all the substrings of the word
			File file = new File( inputFilename );
			Scanner parser = new Scanner( file );

			PrintWriter writer = new PrintWriter( new File( outputFilename ) );

			writer.write( encodings.toString() );
			writer.write( "\n" );
			writer.write( "\n" );

			while( parser.hasNextLine() )
			{
				Scanner parseWord = new Scanner( parser.nextLine() );
				while( parseWord.hasNext() )
				{
					String word = parseWord.next();
					Set<String> keySet = encodings.keySet();
					for( String s: keySet )
					{
						if( word.contains( s ) )
						{
							word = word.replace( s, encodings.get( s ) );
							if( parseWord.hasNext() )
							{
								word += " ";
							}
							else
							{
								word += "\n";
							}
						}
					}
					writer.write( word );
				}
				parseWord.close();
			}

			parser.close();
			writer.close();

		}
		catch( FileNotFoundException e )
		{
			e.printStackTrace();
		}
		catch( IOException exp )
		{
			exp.printStackTrace();
		}

	}

	/**
	 * This method will make the PriorityQueue that will be used to 
	 * keep track of the impact factor of the word and will 
	 * later used in the getEncoding method.
	 */
	private void makePriorityQueue()
	{
		wordsQ = new PriorityQueue<>();

		// will loop though the HashMap and add each individual word
		// and its frequency to make priority queue that will put the highest
		// impact factor on the top
		for( Map.Entry<String, Integer> entry : table.entrySet() )
		{
			String word = entry.getKey();
			int freq = entry.getValue();

			StringValuePair tmp = new StringValuePair( word, freq );

			wordsQ.add( tmp );
		}
	}

	/**
	 * This is more used for testing the priorityqueue as being 
	 * correct
	 * @return PriorityQuue<StringValuePair> wordsQ
	 */
	public PriorityQueue<StringValuePair> getQueue()
	{
		return wordsQ;
	}

	/**
	 * This more used to check that my ASCII array holds all
	 * the correct number
	 * @return int[] ascii
	 */
	public int[] getASCII()
	{
		return ascii;
	}

	/* (non-Javadoc)
	 * @see Assignment#decompressFile(java.lang.String, java.lang.String)
	 */
	@Override
	public void decompressFile(String inputFilename, String outputFilename) {
		// TODO Auto-generated method stub

		try
		{
			// will open file and read all the words
			// will put them through the makeHashTable method 
			// that will read all the substrings of the word
			File file = new File( inputFilename );
			Scanner parser = new Scanner( file );

			PrintWriter writer = new PrintWriter( new File( outputFilename ) );

			makeDecodedHashMap( parser.nextLine() );
			parser.nextLine();
			
			Scanner parseWord = new Scanner( parser.nextLine() );
			
			while( parseWord.hasNext() )
			{
				String word = parseWord.next();
				Set<String> keySet = decodedString.keySet();
				for( String s: keySet )
				{
					if( word.contains( s ) )
					{
						word = word.replace( s, decodedString.get( s ) );
						if( parseWord.hasNext() )
						{
							word += " ";
						}
						else
						{
							word += "\n";
						}
					}
				}
				writer.write( word );
			}
			parseWord.close();
			parser.close();
			writer.close();

		}
		catch( FileNotFoundException e )
		{
			e.printStackTrace();
		}
		catch( IOException exp )
		{
			exp.printStackTrace();
		}

	}
	
	/**
	 * Will read the first line of the decompressed file
	 * to make a hash map of the key and the values that go with that
	 * @param line
	 */
	private void makeDecodedHashMap( String line )
	{
		decodedString = new HashMap<>();
		Scanner parser = new Scanner( line );
		
		while( parser.hasNext() )
		{
			String svp = parser.next();
			if( svp.charAt(0) == '{' )
			{
				svp = svp.substring( 1 );
			}
			else if ( svp.charAt( svp.length() - 1 ) == '{' )
			{
				svp = svp.substring( 0, svp.length() - 1 );
			}
			
			Scanner parse = new Scanner( svp );
			parse.useDelimiter("=");
			String key = parse.next();
			String val = parse.next();
			val = val.substring( 0, val.length() - 1 );
			
			decodedString.put( val, key );
		
			parse.close();
		}
		
		parser.close();
		
	}
	
	/**
	 * This is used more for testing that the decodedString is
	 * correct
	 * @return HashMap<String,String> decodedString
	 */
	public HashMap<String,String> getDecodedHashMap()
	{
		return decodedString;
	}

	/* (non-Javadoc)
	 * @see Assignment#getOccurances(java.lang.String)
	 */
	@Override
	public HashMap<String, Integer> getOccurances(String inputFilename) {

		try
		{
			// will open file and read all the words
			// will put them through the makeHashTable method 
			// that will read all the substrings of the word
			File file = new File( inputFilename );
			Scanner parser = new Scanner( file );
			table = new HashMap<>();

			while( parser.hasNext() )
			{
				makeHashTable( parser.next() );
			}

			parser.close();

		}
		catch( Exception e )
		{
			e.printStackTrace();
		}

		return table;
	}

	/**
	 * Will make the hash table for each individual word and will
	 * find all the substrings that are part of this word. It will also
	 * increase the frequency or the hash maps value up by one when
	 * the substring is found in another word. 
	 *  
	 * @param word String word that is read from the file
	 */
	private void makeHashTable( String word )
	{
		// will iterate from a start index of one and then will
		// iterate the start index + the endIdx that will start at 
		// three, and then be increased by one 
		int endIdx = 3;
		for( int idx = 0; idx < word.length() - 2; idx++ )
		{
			while( endIdx + idx < word.length() + 1 )
			{
				String tmp = word.substring( idx, idx + endIdx );

				/* DEBUG STATEMENT
				System.out.println( "Start Idx: " + idx 
						+ ", EndIdx: " + (idx + endIdx ) 
						+ ", Word: " + tmp );
				 */

				if( !table.containsKey( tmp ) )
				{
					table.put( tmp, 1 );
				}
				else if ( table.containsKey( tmp ) )
				{
					table.replace( tmp, table.get( tmp ) + 1 );
				}

				endIdx++;
			}
			// reset to 3 
			endIdx = 3;
		}

	}


	/* (non-Javadoc)
	 * @see Assignment#getEncodings(java.lang.String)
	 */
	@Override
	public HashMap<String, String> getEncodings(String inputFilename) {

		//*** no matter what, one of the letters of the strings will
		// be a "+" which is ASCII number of 43
		// ---> lowercase alphabet is ASCII numbers 97-122
		// ---> uppercase alphabet is ASCII numbers 65-90
		// if priority queue has a size greater then 52
		encodings = new HashMap<>();

		Set<String> strKeys = encodings.keySet();
		int idx = 0;
		boolean isEncoded = true;

		for( StringValuePair svp : wordsQ )
		{
			for( String str : strKeys )
			{	
				// check to see if it contains it encoded "ther" word from 
				// queue is "there"
				if( svp.getWord().contains( str ) || str.contains( svp.getWord() ) )
				{
					isEncoded = false;
				}
			}

			if( isEncoded )
			{
				encodings.put( svp.getWord() , Character.toString( '+' ) 
						+ Character.toString( (char)ascii[ idx ] ) );
				idx++;
			}

			strKeys = encodings.keySet();
			isEncoded = true;
		}

		return encodings;
	}

}
