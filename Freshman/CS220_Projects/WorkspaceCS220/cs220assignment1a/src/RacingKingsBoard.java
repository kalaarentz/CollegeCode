/*
 * Author: Kala Arentz
 *
 * Date: Feb 10, 2015
 *
 * Purpose: The role of this class is to encapsulate the state
 * of the game and enforce the rules.
 */
import java.io.FileOutputStream;
import java.io.*;
import java.io.IOException;
import java.util.Scanner;

public class RacingKingsBoard
{
	// We'll use a 2-D array of ChessSquares to represent our board.  Notice
	// we'll need to do some initialization here, probably in a constructor.
	private ChessSquare[][] board = new ChessSquare[8][8];

	private boolean whiteMove;
	private boolean stillPlaying;
	private boolean lastChance;

	private int winner = 0;
	private int counterBlack = 0;
	private int counterWhite = 0;

	public RacingKingsBoard( )
	{
		// Make the first 5 rows as empty chess square
		for( int idx = 0; idx < 6; idx++ )
		{
			for( int j = 0; j < 8; j++ )
			{	
				board[idx][j] = new ChessSquare(
						ChessSquare.ChessPiece.EMPTY, false );

			}
		}

		// Initialize the specific last two rows
		// Black on columns 1-3, White on columns 4-7

		// Black King and Queen
		board[6][0] = new ChessSquare( 
				ChessSquare.ChessPiece.KING, false );

		board[7][0] = new ChessSquare( 
				ChessSquare.ChessPiece.QUEEN, false );

		// White King and Queen
		board[6][7] = new ChessSquare( 
				ChessSquare.ChessPiece.KING, true);

		board[7][7] = new ChessSquare(
				ChessSquare.ChessPiece.QUEEN, true);

		// Initialize the rooks of both white and black
		for( int idx = 6; idx < 8; idx++ )
		{
			// Black rooks
			board[idx][1] = new ChessSquare(
					ChessSquare.ChessPiece.ROOK, false );

			// White rooks
			board[idx][6] = new ChessSquare( 
					ChessSquare.ChessPiece.ROOK, true );

		}

		// Initialize the bishops of both white and black
		for( int idx = 6; idx < 8; idx++ )
		{
			// Black Bishop
			board[idx][3] = new ChessSquare(
					ChessSquare.ChessPiece.BISHOP, false );

			// White Bishop
			board[idx][4] = new ChessSquare( 
					ChessSquare.ChessPiece.BISHOP, true );

		}

		// Initialize the knights of both white and black
		for( int idx = 6; idx < 8; idx++ )
		{
			// Black knights
			board[idx][2] = new ChessSquare(
					ChessSquare.ChessPiece.KNIGHT, false );

			// White knights
			board[idx][5] = new ChessSquare(
					ChessSquare.ChessPiece.KNIGHT, true );

		}

		// White has the first move
		whiteMove = true;
		stillPlaying = true;

	}

	public RacingKingsBoard( String fileName )
	{
		// Will read the state of the file and each string
		// will represent the type Chess Piece
		// x represents empty
		// capital letters represent white Chess Pieces
		// kn represents knight

		//this();

		Scanner reader;

		try
		{
			reader = new Scanner( new File( fileName ) );
		}
		catch( IOException e)
		{
			return;
		}

		for( int i = 0; i < board.length; i++ )
		{
			for( int j = 0; j < board[i].length; j++ )
			{
				String next = reader.next();
				//System.out.println( next );
				if( next.equals( "x" ) )
				{
					board[i][j] = new ChessSquare( 
							ChessSquare.ChessPiece.EMPTY, false );
				}
				else if( next.equals( "q" ) )
				{
					board[i][j] = new ChessSquare( 
							ChessSquare.ChessPiece.QUEEN, false );
				}
				else if( next.equals( "k" ) )
				{
					board[i][j] = new ChessSquare( 
							ChessSquare.ChessPiece.KING, false );
				}
				else if( next.equals( "r" ) )
				{
					board[i][j] = new ChessSquare( 
							ChessSquare.ChessPiece.ROOK, false );
				}
				else if( next.equals( "kn" ) )
				{
					board[i][j] = new ChessSquare( 
							ChessSquare.ChessPiece.KNIGHT, false );
				}
				else if( next.equals( "b" ) )
				{
					board[i][j] = new ChessSquare(
							ChessSquare.ChessPiece.BISHOP, false );
				}
				else if( next.equals( "Q" ) )
				{
					board[i][j] = new ChessSquare( 
							ChessSquare.ChessPiece.QUEEN, true );
				}
				else if( next.equals( "K" ) )
				{
					board[i][j] = new ChessSquare( 
							ChessSquare.ChessPiece.KING, true );
				}
				else if( next.equals( "R" ) )
				{
					board[i][j] = new ChessSquare( 
							ChessSquare.ChessPiece.ROOK, true );
				}
				else if( next.equals( "KN" ) )
				{
					board[i][j] = new ChessSquare( 
							ChessSquare.ChessPiece.KNIGHT, true );
				}
				else if( next.equals( "B" ) )
				{
					board[i][j] = new ChessSquare( 
							ChessSquare.ChessPiece.BISHOP, true );
				}

			}
		}

		whiteMove = true;
		stillPlaying = true;

	}
	public static void main( String[] args ) 
	    throws InvalidMoveException, IOException
	{
		/*
		RacingKingsBoard game = new RacingKingsBoard(  );
		String[] possibleMoves = null;


		// Checking my private convertArrayToChessBoardFormat method
		//System.out.println( game.convertArrayToChessBoardFormat(2, 3) );

		// Checking getsMoves method works for knights and kings

		//possibleMoves = game.getMoves( "b2");
		//game.getSquare( "l9");


		for( int idx = 0; idx < possibleMoves.length; idx++)
		{
			System.out.println( possibleMoves[idx]);
		}
		 */

	}

	/**
	 *  This method takes a board location in standard notation
	 *  and returns a String objects, where each string is a board 
	 *  location that the chess piece may move to.
	 *  May throw an BoardLocationOutOfBoundsException
	 *  
	 * @param loc: location of the chessPeice
	 * @return an array of possible destinations for the piece at given loc
	 */
	public String[] getMoves( String loc )
	{
		// Figure out the index from the given location -- I'll assume it's
		// 0, 0 for now.
		int row, column;

		column = loc.toUpperCase().charAt( 0 ) - 'A';
		row = (loc.toUpperCase().charAt( 1 ) - '8') *-1;

		// Check to make sure the row and columns are not index out of bounds
		if( row < 0 || column > 7 )
		{
			throw new BoardLocationOutOfBoundsException( "Index out of Bounds");
		}

		ChessSquare p = getSquare( loc );

		// Possible moves array
		String[] result = new String[0];

		// Determine what type it is, and specify the available moves from this
		// type.
		if( p.getPieceType() == ChessSquare.ChessPiece.KING )
		{
			result = new String[8];

			// I can move this one square in any direction.
			// Will have 8 possible placements for this chess piece
			int countIdx = 0;
			//up one
			if( ( row -1 >= 0 && row -1 < 8 ) 
					&& (column >= 0 && column < 8 )
					&& board[row-1][column].getPieceType() 
					== ChessSquare.ChessPiece.EMPTY )
			{
				result[countIdx] = 
						convertArrayToChessBoardFormat( row-1, column );
				countIdx++;
			}

			//down one
			if( ( row +1 >= 0 && row +1 < 8 ) 
					&& (column >= 0 && column < 8 )
					&& board[row+1][column].getPieceType() 
					== ChessSquare.ChessPiece.EMPTY )
			{
				result[countIdx] = 
						convertArrayToChessBoardFormat( row+1, column );
				countIdx++;
			}

			// left one
			if( ( ( row >= 0 && row < 8 ) 
					&& (column -1 >= 0 && column -1 < 8 )
					&& board[row][column-1].getPieceType() 
					== ChessSquare.ChessPiece.EMPTY ) )
			{
				result[countIdx] = 
						convertArrayToChessBoardFormat( row, column-1 );
				countIdx++;
			}

			// right one 
			if( ( row >= 0 && row < 8 ) 
					&& (column+1 >= 0 && column+1 < 8 )
					&& board[row][column+1].getPieceType() 
					== ChessSquare.ChessPiece.EMPTY )
			{
				result[countIdx] = 
						convertArrayToChessBoardFormat( row, column+1 );
				countIdx++;
			}

			// one top right
			if( ( row -1 >= 0 && row -1 < 8 ) 
					&& (column+1 >= 0 && column+1 < 8 )
					&& board[row-1][column+1].getPieceType() 
					== ChessSquare.ChessPiece.EMPTY )
			{
				result[countIdx] = convertArrayToChessBoardFormat( 
						row-1, column+1 );
				countIdx++;
			}

			// one top left
			if(  ( row -1 >= 0 && row -1 < 8 ) 
					&& (column-1 >= 0 && column-1 < 8 )
					&& board[row-1][column-1].getPieceType() 
					== ChessSquare.ChessPiece.EMPTY )
			{
				result[countIdx] = 
						convertArrayToChessBoardFormat( 
								row-1, column-1 );
				countIdx++;
			}

			// bottom left
			if( ( row+1 >= 0 && row+1 < 8 ) 
					&& (column+1 >= 0 && column+1 < 8 )
					&& board[row+1][column+1].getPieceType() 
					== ChessSquare.ChessPiece.EMPTY )
			{
				result[countIdx] = 
						convertArrayToChessBoardFormat( 
								row+1, column+1 );
				countIdx++;
			}

			// bottom right
			if( ( row+1 >= 0 && row+1 < 8 ) 
					&& (column-1 >= 0 && column-1 < 8 )
					&& board[row+1][column-1].getPieceType() 
					== ChessSquare.ChessPiece.EMPTY )
			{
				result[countIdx] = convertArrayToChessBoardFormat( 
						row+1, column-1 );
				countIdx++;
			}


		}
		else if ( p.getPieceType() == ChessSquare.ChessPiece.QUEEN )
		{
			result = new String[64];

			// Queen can move to any vacant squares in a
			// Horizontal, vertical, or diagonal direction
			// USE HELPER METHOD
			// If there's an open spot, add a String
			// to my result

			// Horizontal down
			this.getMovesForQueenRookBishop(row, column, 1, 0, result);
			// Horizontal up
			this.getMovesForQueenRookBishop(row, column, -1, 0, result);
			// Vertical left
			this.getMovesForQueenRookBishop(row, column, 0, -1, result);
			// Vertical Right
			this.getMovesForQueenRookBishop(row, column, 0, 1, result);
			// Down Bottom Right 
			this.getMovesForQueenRookBishop(row, column, 1, 1, result);
			// Down Bottom Left
			this.getMovesForQueenRookBishop(row, column, 1, -1, result);
			// Down Top Left
			this.getMovesForQueenRookBishop(row, column, -1, -1, result);
			// Down Top Right
			this.getMovesForQueenRookBishop(row, column, -1, 1, result);
		}
		else if ( p.getPieceType() == ChessSquare.ChessPiece.ROOK )
		{
			result = new String[64];

			// Rook can move to any vacant squares in a 
			// Horizontal or vertical direction
			// USE HELPER METHOD
			// If there's an open spot, add a String
			// to my result

			// Horizontal down
			this.getMovesForQueenRookBishop(row, column, 1, 0, result);
			// Horizontal up
			this.getMovesForQueenRookBishop(row, column, -1, 0, result);
			// Vertical left
			this.getMovesForQueenRookBishop(row, column, 0, -1, result);
			// Vertical Right
			this.getMovesForQueenRookBishop(row, column, 0, 1, result);

		}
		else if ( p.getPieceType() == ChessSquare.ChessPiece.BISHOP )
		{
			result = new String[64];

			// Bishop can move to any vacant squares 
			// in any diagonal direction.
			// USE HELPER METHOD
			// If there's an open spot, add a String
			// to my result

			// Down Bottom Right 
			this.getMovesForQueenRookBishop(row, column, 1, 1, result);
			// Down Bottom Left
			this.getMovesForQueenRookBishop(row, column, 1, -1, result);
			// Down Top Left
			this.getMovesForQueenRookBishop(row, column, -1, -1, result);
			// Down Top Right
			this.getMovesForQueenRookBishop(row, column, -1, 1, result);
		}
		else if ( p.getPieceType() == ChessSquare.ChessPiece.KNIGHT )
		{
			result = new String[8];
			// Knight moves two squares horizontally and 
			// then one square vertically, or moving
			// one square horizontally and then two 
			// squares vertically.

			if( ( row -1 >=0 && row-1 < 8 
					&& column-2 >= 0 && column < 8 ) 
					&&board[row-1][column-2].getPieceType() 
					== ChessSquare.ChessPiece.EMPTY )
			{
				result[0] = 
						convertArrayToChessBoardFormat( row-1, column-2 );
			}
			else
			{
				result[0] = null;
			}

			if( ( row -1 >= 0 && row -1 < 8 
					&& column+2 >= 0 && column+2 < 8 ) 
					&& board[row-1][column+2].getPieceType() 
					== ChessSquare.ChessPiece.EMPTY )
			{
				result[1] = 
						convertArrayToChessBoardFormat( row-1, column+2 );
			}
			else
			{
				result[1] = null;
			}

			if( ( row -2 >= 0 && row -2 < 8 
					&& column-1 >= 0 && column-1 < 8 ) 
					&& board[row-2][column-1].getPieceType() 
					== ChessSquare.ChessPiece.EMPTY )
			{
				result[2] = 
						convertArrayToChessBoardFormat( row-2, column-1 );
			}
			else
			{
				result[2] = null;
			}

			if( ( row -2 >= 0 && row -2 < 8 
					&& column+1 >= 0 && column+1 < 8 ) 
					&& board[row-2][column+1].getPieceType() 
					== ChessSquare.ChessPiece.EMPTY )
			{
				result[3] = 
						convertArrayToChessBoardFormat( row-2, column+1 );
			}
			else
			{
				result[3] = null;
			}

			if( ( row +2 >= 0 && row +2 < 8 
					&& column-1 >= 0 && column-1 < 8 ) 
					&& board[row+2][column-1].getPieceType() 
					== ChessSquare.ChessPiece.EMPTY )
			{
				result[4] = 
						convertArrayToChessBoardFormat( row+2, column-1 );
			}
			else
			{
				result[4] = null;
			}

			if( ( row +2 >= 0 && row +2 < 8 
					&& column+1 >= 0 && column+1 < 8 ) 
					&& board[row+2][column+1].getPieceType() 
					== ChessSquare.ChessPiece.EMPTY )
			{
				result[5] = 
						convertArrayToChessBoardFormat( row+2, column+1 );
			}
			else
			{
				result[5] = null;
			}

			if( ( row +2 >= 0 && row +2 < 8 
					&& column-2 >= 0 && column-2 < 8 ) 
					&& board[row+2][column-2].getPieceType() 
					== ChessSquare.ChessPiece.EMPTY )
			{
				result[6] = 
						convertArrayToChessBoardFormat( row+1, column-2 );
			}
			else
			{
				result[6] = null;
			}

			if( ( row +1 >= 0 && row +1 < 8 
					&& column+2 >= 0 && column+2 < 8 ) 
					&& board[row+1][column+2].getPieceType() 
					== ChessSquare.ChessPiece.EMPTY )
			{
				result[7] = 
						convertArrayToChessBoardFormat( row+1, column+2 );
			}
			else
			{
				result[7] = null;
			}

		}

		// Return the list of possible locations.

		return result;
	}

	/**
	 * This method will take a string as a parameter and return
	 * a copy of what is on that board at that position.
	 * 
	 * @param loc: location on the board as a string (A2)
	 * @return return the COPY of the state of the board at that position
	 */
	public ChessSquare getSquare( String loc )
	{

		int row, column;

		column = loc.toUpperCase().charAt( 0 ) - 'A';
		row = (loc.toUpperCase().charAt( 1 ) - '8') * -1;
		//System.out.println( "DEBUG Column: " + column );
		//System.out.println( "DEBUG Row: " + row );

		// BoardLocationOutOfBoundsException
		if( row < 0 || column > 7 )
		{
			throw new BoardLocationOutOfBoundsException( "Index out of Bounds");
		}

		ChessSquare sq = new ChessSquare( board[row][column].getPieceType(), 
				board[row][column].getPlayer() );

		return sq;
	}

	/**
	 * This method will attempt to move the piece at the starting 
	 * location to the ending location. If the move is illegal, then an 
	 * InvalidMoveException should be thrown(checked exception)
	 * 
	 * @param startLoc: String that represent starting location on board
	 * @param endLoc: String that represents ending location on board
	 */
	public void movePiece( String startLoc, String endLoc )
	    throws InvalidMoveException
	{
		// Check to make sure they do not have index out of bounds 
		try
		{
			ChessSquare firstPiece = getSquare( startLoc );
			ChessSquare secondPiece = getSquare( endLoc );
		}
		catch( BoardLocationOutOfBoundsException e)
		{
			throw new BoardLocationOutOfBoundsException( "Index Out of Bounds");
		}

		ChessSquare firstPiece = getSquare( startLoc );
		ChessSquare secondPiece = getSquare( endLoc );

		int endRow, endColumn, startRow, startColumn;

		startColumn = startLoc.toUpperCase().charAt( 0 ) - 'A';
		startRow = ( startLoc.toUpperCase().charAt( 1 ) - '8' ) * -1;

		endColumn = endLoc.toUpperCase().charAt( 0 ) - 'A';
		endRow = ( endLoc.toUpperCase().charAt( 1 ) - '8' ) * -1;

		// Check to make sure second piece is not a king
		// Check to make sure first piece and second piece 
		// 		same color--> empty is considered black

		if( secondPiece.getPieceType() == ChessSquare.ChessPiece.KING )
		{
			throw new InvalidMoveException( "Try Again!" );
		}
		else if( firstPiece.getPlayer() == secondPiece.getPlayer() 
				&& secondPiece.getPieceType() != ChessSquare.ChessPiece.EMPTY )
		{
			throw new InvalidMoveException( "Try Again!" );
		}
		else if( whiteMove != firstPiece.getPlayer() )
		{
			throw new InvalidMoveException( "Try Again!" );
		}
		else
		{
			board[endRow][endColumn].setPieceType( firstPiece.getPieceType() ); 
			board[endRow][endColumn].setPlayer( firstPiece.getPlayer() );
			board[startRow][startColumn]
			    = new ChessSquare( ChessSquare.ChessPiece.EMPTY, false);
		}
		whiteMove = !whiteMove;

		// Check to make sure the if last chance
		if ( lastChance == true && counterBlack == 1 )
		{
			stillPlaying = true;
		}

		// Check to make sure who is winning
		if( firstPiece.getPieceType() == ChessSquare.ChessPiece.KING 
				&& firstPiece.getPlayer() == false 
				&& getWinner() == 0 && endRow == 0 )
		{
			counterBlack = counterBlack + 1;
		}
		else if( firstPiece.getPieceType() == ChessSquare.ChessPiece.KING 
				&& firstPiece.getPlayer() == true 
				&& getWinner() == 0 && endRow == 0 )
		{
			counterWhite = counterWhite + 1;
		}
		else if( firstPiece.getPieceType() == ChessSquare.ChessPiece.KING 
				&& firstPiece.getPlayer() == false 
				&& getWinner() == 0 && endRow == 7 && counterBlack == 1)
		{
			counterBlack = counterBlack + 1;
		}
		else if( firstPiece.getPieceType() == ChessSquare.ChessPiece.KING 
				&& firstPiece.getPlayer() == true 
				&& getWinner() == 0 && endRow == 7 && counterWhite == 1 )
		{
			counterWhite = counterWhite + 1;
			lastChance = true;
		}

		// Determine who is winning
		if( counterBlack == 2 && counterWhite == 2 )
		{
			winner = 0;
			stillPlaying = false;
		}
		else if( counterBlack == 2 )
		{
			winner = -1;
			stillPlaying = false;
		}
		else if( counterWhite == 2 && stillPlaying != true)
		{
			winner = 1;
			stillPlaying = false;
		}
		else if( counterWhite == 2 )
		{
			winner = 1;
			stillPlaying = false;
		}
	}

	/**
	 * This method returns true when it is White players turn to move.
	 * White player gets first turn in chess
	 * 
	 * @return true when it is Whites turn to move
	 */
	public boolean isWhiteMove()
	{
		return whiteMove;
	}

	/**
	 * True: Neither team has lost, still playing game
	 * False: a team has won, even it it is draw
	 * 
	 * @return boolean represents if game is still playing
	 */
	public boolean isPlaying()
	{
		return stillPlaying;
	}

	/** 
	 * Returns 0: the game is still going on or if a draw
	 * Returns -1: Black team has won
	 * Returns 1: White team has won
	 * 
	 * @return value that represents who has won the game
	 */
	public int getWinner()
	{
		return winner;
	}

	/**
	 * This method will save the state of the Chess Board.
	 * 
	 * @param filename: string name for the file t 
	 */
	public void saveState( String filename ) throws IOException
	{
		// File will be rewritten to show what has been saved.
		FileOutputStream fileOut = new FileOutputStream( "chessBoard" );

		for( int i = 0; i < board.length; i++ )
		{
			for( int j = 0; j < board[i].length; j++ )
			{
				String loc = convertArrayToChessBoardFormat( i, j );
				ChessSquare piece = getSquare( loc );
				if( j == 8 )
				{
					if( piece.getPieceType() == ChessSquare.ChessPiece.EMPTY )
					{
						fileOut.write( 'x' );
						fileOut.write( ' ' );
					}
					else if( piece.getPieceType() 
							== ChessSquare.ChessPiece.QUEEN 
							&& piece.getPlayer() == false )
					{
						fileOut.write( 'q' );
						fileOut.write( ' ' );
					}
					else if( piece.getPieceType()  
							== ChessSquare.ChessPiece.KING  
							&& piece.getPlayer() == false )
					{
						fileOut.write( 'k' );
						fileOut.write( ' ' );
					}

					else if( piece.getPieceType() 
							== ChessSquare.ChessPiece.ROOK
							&& piece.getPlayer() == false )
					{
						fileOut.write( 'r' );
						fileOut.write( ' ' );
					}
					else if( piece.getPieceType() 
							== ChessSquare.ChessPiece.KNIGHT 
							&& piece.getPlayer() == false )
					{
						fileOut.write( 'k' );
						fileOut.write( 'n' );
						fileOut.write( ' ' );
					}
					else if( piece.getPieceType() 
							== ChessSquare.ChessPiece.BISHOP 
							&& piece.getPlayer() == false )
					{
						fileOut.write( 'b' );
						fileOut.write( ' ' );
					}
					else if( piece.getPieceType() 
							== ChessSquare.ChessPiece.QUEEN 
							&& piece.getPlayer() == true )
					{
						fileOut.write( 'Q' );
						fileOut.write( ' ' );
					}
					else if( piece.getPieceType() 
							== ChessSquare.ChessPiece.KING 
							&& piece.getPlayer() == true )
					{
						fileOut.write( 'K' );
						fileOut.write( ' ' );
					}
					else if( piece.getPieceType() 
							== ChessSquare.ChessPiece.ROOK 
							&& piece.getPlayer() == true )
					{
						fileOut.write( 'R' );
						fileOut.write( ' ' );
					}
					else if( piece.getPieceType() 
							== ChessSquare.ChessPiece.KNIGHT 
							&& piece.getPlayer() == true )
					{
						fileOut.write( 'K' );
						fileOut.write( 'N' );
						fileOut.write( ' ' );
					}
					else if( piece.getPieceType() 
							== ChessSquare.ChessPiece.BISHOP 
							&& piece.getPlayer() == true )
					{
						fileOut.write( 'B' );
						fileOut.write( ' ' );
					}

					fileOut.write( '\n' );
				}
				else 
				{
					if( piece.getPieceType() 
							== ChessSquare.ChessPiece.EMPTY )
					{
						fileOut.write( 'x' );
						fileOut.write( ' ' );
					}
					else if( piece.getPieceType() 
							== ChessSquare.ChessPiece.QUEEN  
							&& piece.getPlayer() == false )
					{
						fileOut.write( 'q' );
						fileOut.write( ' ' );
					}
					else if( piece.getPieceType() 
							== ChessSquare.ChessPiece.KING  
							&& piece.getPlayer() == false )
					{
						fileOut.write( 'k' );
						fileOut.write( ' ' );
					}

					else if( piece.getPieceType() 
							== ChessSquare.ChessPiece.ROOK  
							&& piece.getPlayer() == false )
					{
						fileOut.write( 'r' );
						fileOut.write( ' ' );
					}
					else if( piece.getPieceType() 
							== ChessSquare.ChessPiece.KNIGHT  
							&& piece.getPlayer() == false )
					{
						fileOut.write( 'k' );
						fileOut.write( 'n' );
						fileOut.write( ' ' );
					}
					else if( piece.getPieceType() 
							== ChessSquare.ChessPiece.BISHOP  
							&& piece.getPlayer() == false )
					{
						fileOut.write( 'b' );
						fileOut.write( ' ' );
					}
					else if( piece.getPieceType() 
							== ChessSquare.ChessPiece.QUEEN  
							&& piece.getPlayer() == true )
					{
						fileOut.write( 'Q' );
						fileOut.write( ' ' );
					}
					else if( piece.getPieceType() 
							== ChessSquare.ChessPiece.KING 
							&& piece.getPlayer() == true )
					{
						fileOut.write( 'K' );
						fileOut.write( ' ' );
					}
					else if( piece.getPieceType() 
							== ChessSquare.ChessPiece.ROOK 
							&& piece.getPlayer() == true )
					{
						fileOut.write( 'R' );
						fileOut.write( ' ' );
					}
					else if( piece.getPieceType() 
							== ChessSquare.ChessPiece.KNIGHT 
							&& piece.getPlayer() == true )
					{
						fileOut.write( 'K' );
						fileOut.write( 'N' );
						fileOut.write( ' ' );
					}
					else if( piece.getPieceType() 
							== ChessSquare.ChessPiece.BISHOP 
							&& piece.getPlayer() == true )
					{
						fileOut.write( 'B' );
						fileOut.write( ' ' );
					}
				}

			}
		}

		fileOut.close();
	}
	/**
	 * This method converts the array, two numbers, into the format
	 * of the chess board. 
	 * 
	 * @param row: index of the array row
	 * @param column: index of the array column
	 * @return newLoc: the string that represents the chess board format
	 */
	private String convertArrayToChessBoardFormat( int row, int column )
	{

		char rowChar = '\0';
		char columnChar = '\0';
		String newLoc;

		// Converting the rows to the correct Chess Board format
		if( row == 0 )
		{
			rowChar = '8';
		}
		else if( row == 1 )
		{
			rowChar = '7';
		}
		else if( row == 2 )
		{
			rowChar = '6';
		}
		else if( row == 3 )
		{
			rowChar = '5';
		}
		else if( row == 4 )
		{
			rowChar = '4';
		}
		else if( row == 5 )
		{
			rowChar = '3';
		}
		else if( row == 6 )
		{
			rowChar = '2';
		}
		else if( row == 7 )
		{
			rowChar = '1';
		}

		// Converting the rows to the correct Chess Board format
		if( column == 0 )
		{
			columnChar = 'A';
		}
		else if( column == 1 )
		{
			columnChar = 'B';
		}
		else if( column == 2 )
		{
			columnChar = 'C';
		}
		else if( column == 3 )
		{
			columnChar = 'D';
		}
		else if( column == 4 )
		{
			columnChar = 'E';
		}
		else if( column == 5 )
		{
			columnChar = 'F';
		}
		else if( column == 6 )
		{
			columnChar = 'G';
		}
		else if( column == 7 )
		{
			columnChar = 'H';
		}

		if( rowChar =='\0' || columnChar == '\0' )
		{
			return newLoc = "";
		}

		newLoc = "" + columnChar + rowChar;

		return newLoc;
	}

	private void getMovesForQueenRookBishop( int row, int column, int xDir,
			int yDir, String[] result )
	{
		int i = 0;
		for( i = 0; i < result.length && result[i] != null; i++ );
		
		while( (row + xDir < 8 && row + xDir >= 0) && (column + yDir < 8 
				&& column + yDir >= 0) 
				&& (board[row + xDir][column + yDir].getPieceType() 
						== ChessSquare.ChessPiece.EMPTY ) )
		{
			result[i] = convertArrayToChessBoardFormat( 
					row + xDir, column + yDir );
			row+= xDir;
			column+= yDir;
		}

	}

}