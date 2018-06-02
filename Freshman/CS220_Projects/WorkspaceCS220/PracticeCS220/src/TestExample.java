/*
Author: Kala Arentz

Date: Feb 26, 2015

Purpose:

 */

public class TestExample {

	public static void main(String[] args) {
		int[][] quizGrades = {{15,20,100},{19, 13,18},{34,1}};
		
	}
	public int[] dropWorst(int[][] quizGrades )
	{
		int lowScore = 0;
		int sumArray[] = new int[ quizGrades.length ];
		int result = 0;
		int row = 0;
		int column = 0;
		// drop the worst score
		for( int i = 0; i < quizGrades.length; i++)
		{
			lowScore = quizGrades[i][0];
			for(int j = 0; j < quizGrades[i].length; j++ )
			{
				
				if( lowScore > quizGrades[i][j] )
				{
					lowScore = quizGrades[i][j];
					row = i;
					column = j;
				}
				System.out.println( lowScore );
			}
			quizGrades[row][column] = 0;
			
			
		}

		// add the sums up
		for( int i = 0; i < quizGrades.length; i++)
		{
			for(int j = 0; j < quizGrades[i].length; j++ )
			{
				result += quizGrades[i][j];
			}
			System.out.println( "Result DEBUG: " + result);
			sumArray[i] = result;
			result = 0;
		}
		
		return sumArray;
	}

}
