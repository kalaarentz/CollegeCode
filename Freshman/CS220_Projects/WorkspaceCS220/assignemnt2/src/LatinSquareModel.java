/**
Author: Kala Arentz

Date: Mar 10, 2015

Purpose: Will create a Latin square game with
n X n dimensions. The Latin square is filled
with n number of different symbols, where the 
symbols appear on different rows and columns.

 */

import java.util.Arrays;

public class LatinSquareModel {
	// New Latin Square Board
	private String[][] latinBoard;
	//= { {"A", "B", "C"}, {"C", "A", "B"}, {"B", "C", "A"} };
	private String[] validSymbols;
	private boolean modified;

	public LatinSquareModel( String[] validSym, int dimensions ) 
	{
		
		// Check for duplicates
		validSymbols = removeDuplicates( validSym );

		// Throw InvalidLatinSquare Exception
		//  	if: not enough symbols for dimensions of array
		//		if: Array of symbols contains duplicates

		if ( validSymbols.length < dimensions )
		{
			throw new InvalidLatinSquareException( "Not"
					+ " Enough Valid Symbols" );
		}

		// Create a new board
		latinBoard = new String[ dimensions ][ dimensions ];

		// set modified to true because using
		// first constructor so it does 
		// not matter if you modify the Latin Square
		modified = true;

	}

	/**
	 * This is the overloaded constructor. 
	 * 
	 * @param validSym: String that has the valid symbols to be used 
	 * @param board : String[][] initial state of the board
	 */
	public LatinSquareModel( String[] validSym, String[][] board )
	{
		// Make latin board 
		latinBoard = board;
		
		// Check for duplicates
		validSymbols = removeDuplicates( validSym );

		// Throw InvalidLatinSquare Exception
		//  	if: not enough symbols for dimensions of array
		//		if: Array of symbols contains duplicates

		if ( validSymbols.length < board.length )
		{
			throw new InvalidLatinSquareException( "Not"
					+ " Enough Valid Symbols" );
		}

		// Throw InvalidLatinSquareException
		// 		if: violates a property of the Latin Squares
		//		if: contains a symbol that is not included
		//			in the list of valid symbols
		// 		if: no solution to the given board exists

		// set modified to false because using
		// second constructor so it does 
		// matter if you modify the Latin Square
		modified = false;

		// CheckRowColumnForValue to see if is invalid 
		// more then one value in a row
		for( int row = 0; row < board.length; row++ )
		{
			for( int col = 0; col < board[ row ].length; col++ )
			{
				if( board[ row ][ col ] != null )
				{
					// Check for duplicates in Rows/Columns
					if( !checkRowColumnForValue( row, col, 
							board, board[ row ][ col ] ) )
					{
						throw new InvalidLatinSquareException( 
								"Duplicates Found!" );
					}
					
					if( !isAValidSymbol( board[ row ][ col ] ) )
					{
						throw new InvalidLatinSquareException( 
								"Invalid Symbol" );
					}
				}

			}
		}

	}

	public static void main( String[] args )
	{
		//String[] symbols = {"A", "B", "C"};
		//LatinSquareModel newGame = new LatinSquareModel( symbols , 3 );
		//newGame.solvedPuzzle();
		//System.out.println( "IsComplelete Method: " 
		//+ newGame.isComplete() );
		//System.out.println( "setSquare Method: "
		//+ newGame.setSquare( 0, 1, "A" ) );


	}

	/**
	 * This method will take the int for row and column
	 * to return the String value at that specific square.
	 * 
	 * @param row: the int for matrix row
	 * @param column: the int for matrix column
	 * @return String value at that row and column
	 */
	public String getSquare( int row, int column )
	{
		// Variable for returnValue
		String returnValue;

		// Check to make sure row and column are
		// in the index of the latinBoard
		try
		{
			returnValue = latinBoard[ row ][ column ];
		}
		catch( ArrayIndexOutOfBoundsException e )
		{
			throw new IllegalArgumentException( );
		}
		

		// Return the type string that is 
		// present at the latinBoard[ row ][ column ]
		if( latinBoard[ row ][ column ] == null )
		{
			returnValue = null;
		}
		else
		{
			returnValue = latinBoard[ row ][ column ];
		}

		return returnValue;
	}

	/**
	 * This method returns true when the Latin 
	 * Square is completed, or a unique symbol
	 * appears in every row and column.
	 * 
	 * @return 
	 */
	public boolean isComplete( )
	{

		// Check to make sure there is no duplicates
		// in the same row or column
		// String to hold String value at a square to 

		for( int idx = 0; idx < latinBoard.length; idx++ )
		{
			for( int j = 0; j < latinBoard[ idx ].length; j++ )
			{
				// if this is true then there are 
				// duplicates found in the either the
				// rows or columns 
				if( checkForDuplicates( idx, j, latinBoard) )
				{
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * This method will take the row and column
	 * of a square in the matrix, and set it to 
	 * the given String value. If setting this 
	 * square results in a valid Latin square, 
	 * then the method should return true, 
	 * otherwise it should return false.
	 *   
	 * @param row: row of the Latin Square matrix
	 * @param column: column of the Latin Square matrix
	 * @param value: Symbol 
	 * @return returnedValue: valid Latin Square
	 * 				is true, and false is not
	 * 				valid Latin Square
	 */
	public boolean setSquare( int row, int column, String value )
	{
		int isValidSymbol = 0;
		String returnString = "";
		// Throw an IllegalArgumentException
		// if: given value is not a valid symbol
		for( int idx = 0; idx < validSymbols.length; idx++ )
		{
			if( value.toUpperCase( ).equals( 
					validSymbols[ idx ].toUpperCase( ) ) )
			{
				//System.out.println(" Valid Symbol " );
				isValidSymbol = 1;
			}	
		}

		// Throw an IllegalArgumentExeption when
		// isValidSymbol is not equal to 1
		// isValidSymbol is equal to 1 when it is a 
		// valid symbol
		if( isValidSymbol != 1 )
		{
			throw new IllegalArgumentException( );
		}

		// if: row/column are outside of range
		try
		{
			returnString = latinBoard[ row ][ column ];
		}
		catch( ArrayIndexOutOfBoundsException e )
		{
			throw new IllegalArgumentException( );
		}

		// Check Row/Column for value 
		if( !checkRowColumnForValue( row, column, latinBoard, value ) )
		{
			throw new InvalidLatinSquareException( 
					"Duplicates Found! ");
		}

		// if modified is true, you can ignore 
		// if the square is not null,
		// but if it is false, then you need 
		// to throw a IllegalArgumentException

		if( !isModified( ) )
		{
			if( getSquare( row, column ) != null )
			{
				throw new IllegalArgumentException( );
			}
		}

		// only set the new value if the spot is
		// empty
		if( getSquare( row, column ) == null )
		{
			latinBoard[ row ][ column ] = value;
		}


		// Check to see if setting the new value
		// will return a true for isComplete() 
		if( isComplete( ) )
		{
			return true;
		}

		return false;
	}



	/**
	 * This method should return an array of Strings
	 * representing a solved puzzle given the 
	 * current state of the board. It should not 
	 * change any value that is already set, and instead 
	 * should only fill in those values that are blank.
	 * 
	 *   If no solution exits, the method returns null.
	 *   
	 * @return: It will return null for no solution,
	 * 			or it will return an array of Strings
	 * 			representing finished solved puzzle
	 */
	public String[][] solvedPuzzle( )
	{
		// Base case if it is isComplete()
		// is true then it should return
		// an array 
		String[][] copiedBoard;

		copiedBoard = getLatinBoardCopy( );

		if( isComplete( ) )
		{
			//System.out.println( "ARRAY DEBUG: " 
			//+ Arrays.deepToString(copiedBoard) );
			return copiedBoard;

		}
		else
		{
			return recursiveSolve( copiedBoard );
		}

	}

	/**
	 * This method will check the row and columns
	 * for any type of duplicates in their arrays
	 * If checkForNull is true then you want to take
	 * into account that null squares are not a complete
	 * latinBoard, if false then that means you want 
	 * to just look for duplicates in rows and columns;
	 * this is more for use of solvedPuzzle.
	 * 
	 * @param row : row that you check for duplicates
	 * @param column : column you check for duplicates
	 * @param checkForNull : true if you want to return false
	 * 						false for nulls or ignore it 
	 * @return return true for duplicates found, and false
	 * 			for when no duplicates are found or when 
	 * 			holdVar is null
	 */
	private boolean checkForDuplicates( int row, int col, String[][] board )
	{
		// Checking the rows and columns for duplicates
		// if duplicates are found return true,
		// if no duplicates are found return false

		String holdVar = board[ row ][ col ];

		// If the holdVar equal null you can not check for duplicates
		if( holdVar != null )
		{
			// Checking for duplicates in rows and columns
			if( !checkRowColumnForValue( row, col, board, holdVar ) )
			{
				//System.out.println( "Duplicates Found" );
				return true;
			}

		}
		else{

			return true;
		}

		return false; 
	}

	/**
	 * This method makes a copy of 
	 * the latinBoard.
	 * @return boardCopy: copy of the latinBoard 
	 */
	private String[][] getLatinBoardCopy( )
	{
		String boardCopy[][] 
		    = new String[ latinBoard.length ][ latinBoard[ 0 ].length ];

		for( int i = 0; i < latinBoard.length; i++ )
		{
			for( int j = 0; j < latinBoard[ i ].length; j++ )
			{
				boardCopy[ i ][ j ] = latinBoard[ i ][ j ];
			}
		}

		return boardCopy;
	}

	/**
	 * This method looks at the valid symbols given
	 * and looks for duplicates. If duplicates are 
	 * found the method will remove the duplicates
	 * and return a new array that is shortened.
	 * 
	 * @param validSym :valid Symbols for latinBoard
	 * @return newArray : String array that returns new/shortened array
	 */
	private String[] removeDuplicates( String[] validSym )
	{
		// Check for how many duplicates there is
		// Change all the duplicates to null
		int count = 0;
		int j = 1;
		for( int idx = 0; idx < validSym.length; idx++ )
		{
			String posDuplicate = validSym[ idx ];
			while( idx + j < validSym.length )
			{
				if( posDuplicate != null 
						&& posDuplicate.equals( validSym[ idx + j ] ) )
				{
					validSym[ idx + j ] = null;
					count++;
				}
				j++;
			}
			j = 1;
		}

		// If count is not zero then there was 
		// duplicates that the new array will 
		// need to be shortened.
		String[] newArray = new String[ validSym.length - count ];
		int count1 = 0;

		for( int i = 0; i < validSym.length; i++ )
		{
			if( validSym[ i ] != null  )
			{
				newArray[ count1 ] = validSym[ i ];
				count1++;
			}
		}

		//System.out.println( "ARRAY DEBUG: " + Arrays.deepToString(newArray) );

		return  newArray;
	}

	/**
	 * This method will be the recursive method that 
	 * looks for the new valid symbol that will
	 * solve the Latin board. 
	 * @param board : what the board looks like
	 * @return return true if the copiedBoard is complete
	 * 		   return false if the copiedBoard is incomplete
	 */
	private String[][] recursiveSolve( String[][] board )
	{
		// Base case would be complete copied board
		// Find a blank space
		for( int row = 0; row < board.length; row++ )
		{
			for( int col = 0; col < board[ row ].length; col++ )
			{
				//Look for null spaces
				if( board[ row ][ col ] == null )
				{
					// Loop through the valid symbols
					for( int idx = 0; idx < validSymbols.length; idx++ )
					{
						// if true means you can write the symbols
						// it is a valid value
						if( checkRowColumnForValue( 
								row, col, board, validSymbols[ idx ] ) )
						{
							board[ row ][ col ] = validSymbols[ idx ];
							if( recursiveSolve( board ) != null )
							{
								return board;
							}
						}

					}
					// Erase the spot by making it null again
					board[ row ][ col ] = null;
					// Return null if you cannot find any solutions
					return null;
				}

			}
		}

		return board;
	}

	/**
	 * This method will check if there are any
	 * values that are similar in this row or column.
	 * 
	 * @param row: row number for board
	 * @param col: column number for board
	 * @param board: board you are checking
	 * @param value: value you are checking for
	 * @return true if value was not found, false if
	 * 			value was found in row or column
	 */
	private boolean checkRowColumnForValue( int row, int col, 
			String[][] board, String value )
	{

		for( int i = 0; i < board.length; i++ )
		{
			// Checking the rows
			if( i != col )
			{
				if( value.equals( board[ row ][ i ] ) )
				{
					return false;
				}
			}

			// Checking the columns
			if( i != row )
			{
				if( value.equals( board[ i ][ col ] ) )
				{
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * This method will takes into account the
	 * two different constructors.
	 * 
	 * If the first constructor is used, then 
	 * it is true because you can modify the 
	 * Latin Square.
	 * 
	 * If the second constructor is used, then
	 * it is false because you can NOT modify 
	 * the Latin Square.
	 * @return true if first constructor is used
	 * 			false if second constructor is used
	 */
	private boolean isModified( )
	{
		return modified;
	}
	
	/**
	 * Method checks for valid Symbols
	 * @param value : String to check
	 * @return true if the value is a valid symbol
	 * 			false if the value is not found 
	 * 			in the valid symbols
	 */
	private boolean isAValidSymbol( String value )
	{
		for( int idx = 0; idx < validSymbols.length; idx++ )
		{
			if( value.equals( validSymbols[ idx ] ) )
			{
				return true;
			}
		}
		
		return false;
	}

}