/*
Author: Kala Arentz

Date: May 11, 2015

Purpose:

*/
package finalExam;

import java.util.ArrayList;

public class PracticeExam {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//methodOne();
		//methodTwo();
		
		try
		{
			methodOne();
			methodTwo();
		}
		catch( Exception e )
		{
			System.out.println("Oh no!");
		}
	}
	
	private static void methodOne( ) throws Exception
	{
		int[] [] grid = { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };
		
		for( int i = 0; i < grid.length; i++ )
		{
			System.out.println( grid[i][grid[i].length - 1] );
			for( int j = i + 1; j < grid[i].length; j++ )
			{
				System.out.println( grid[i][j] );
			}
		}
		
		throw new Exception();
	}
	
	private static void methodTwo( )
	{
		ArrayList< ArrayList <Integer > > nums = new ArrayList<>();
		
		for( int i = 0; i < 5; i++ )
		{
			nums.add( new ArrayList< Integer >() );
			
			for( int j = 0; j<3; j++ )
			{
				nums.get(i).add( j+ (i * 3) );
				
			}
		}
		
		int sum = 0;
		
		for( ArrayList< Integer > oneList : nums )
		{
			sum += oneList.size();
			
			for( int num : oneList )
			{
				sum += num;
				
			}
		}
		
		System.out.println( sum );
	}

}
