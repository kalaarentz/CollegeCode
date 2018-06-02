/**
 *  Takes two strings as parameters representing input and output 
 *  filenames and copies only the numbers from the input file to 
 *  output file.  
 * @author kalaarentz
 *
 */
import java.io.*;

public class numbersOnly { 

	public numbersOnly( String inputFile, String outputFile ) throws IOException
	{
		FileInputStream inFile = new FileInputStream( inputFile );
		FileOutputStream outFile = new FileOutputStream( outputFile );
		
		int aByte = inFile.read();
		
		while( aByte >= 0 )
		{
			if( aByte == '0' || aByte == '1' || aByte == '2' || aByte == '3'
					|| aByte == '4' || aByte == '5' || aByte == '6' || aByte == '7'
					|| aByte == '8' || aByte == '9' )
			{
				outFile.write( aByte );
				
			}
			else
			{
				
			}
		}
	}
}
