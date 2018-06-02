/*
Author: Kala Arentz

Date: Mar 24, 2015

Purpose:

 */

public class practice {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println( findBestChange( 0, 28 ) );
		//computeRecurrence( 29 );
		//System.out.println( computeRecurrence( 29 ));
		int i = 29;
		while( i > 0)
		{
			System.out.print( i/3 );
			i = i/3;
			if( i > 0 )
			{
				System.out.print(" + ");
			}
		}
		
	}
	private static String findBestChange( int owed, int paid )
	{
		String[] firstCoin = new String[3];
		
		if( owed == paid)
		{
			return "";
		}
		
		if( paid - owed >= 23 )
		{
			System.out.println( "Second Coin R");
			System.out.println("owed: " + owed + " paid: " + (paid - 23) );
			firstCoin[2] = "R" + findBestChange( owed, paid - 23 );
		}
		
		if( paid - owed >= 7 )
		{
			System.out.println( "First Coin 7");
			System.out.println("owed: " + owed + " paid: " + (paid - 7) );
			firstCoin[1] = "7"+ findBestChange( owed, paid - 7 );
		}
		
		if( paid - owed >= 1 )
		{
			System.out.println( "Zero Coin 1");
			System.out.println("owed: " + owed + " paid: " + (paid - 1) );
			firstCoin[0] = "1" + findBestChange( owed, paid - 1 );
		}
		else
		{
			throw new IllegalArgumentException( "No Change Possible!" );
		}
		
		int min = -1;
		
		for( int i = 0; i < firstCoin.length; i ++ )
		{
			if( min == -1 && firstCoin[i] != null )
			{
				min = i;
			}
			if( min != -1 && firstCoin[ i ] != null 
					&& firstCoin[i].length() < firstCoin[min].length() ) 
			{
				min = i;
			}
		}
		
		return firstCoin[min];
	}
	
	private static int computeRecurrence( int n )
	{
		if( n > 0 )
		{
			System.out.print( n + " + ");
			return n + computeRecurrence( n/3 );
			
		}
		System.out.println( "0" );
		return 0;
	}

}
