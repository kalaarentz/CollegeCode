/*
 * Author: Andrew Berns
 *
 * Date: Feb 14, 2015
 *
 * Purpose: a JPanel which displays the "chess board".  It is linked to a
 *          RacingKingsBoard object.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ChessBoardPanel extends JPanel
{
    // The RacingKingsBoard object storing the game state
    private RacingKingsBoard boardModel;
    
    // Stores if this click was the first or second (determines when to
    // actually move a piece)
    private boolean firstClick = true;
    private String firstClickLocation;
    
    // The array of JLabels we'll use to display the state of the game
    private JLabel board[][] = new JLabel[8][8];
    
    // The status label
    private JLabel status;
    
    // Our constructor, which will set up the panel according to the
    // RacingKingsBoard
    public ChessBoardPanel( )
    {
        // Here, there is an implicit call to 'super( );', which will build our
        // JPanel, and then we'll add stuff to it.
        
        // Set the layout to BorderLayout, with the center containing a panel
        // with the 64 squares in a GridLayout, and the NORTH containing a
        // status message about the game.
        setLayout( new BorderLayout( ) );
        status = new JLabel( );
        status.setHorizontalAlignment( JLabel.CENTER );
        add( status, BorderLayout.NORTH );
        
        JPanel centerPanel = new JPanel( );
        centerPanel.setLayout( new GridLayout( 8, 8 ) );
        
        // Initialize the RacingKingsBoard
        boardModel = new RacingKingsBoard( "chessBoard" );
        
        // Make an 8x8 grid of JLabels (squares)
        for( int i = 7; i >= 0; i-- )
        {
            for( int j = 0; j < 8; j++ )
            {
                // Make the JLabel, center the text, set the font to a bigger
                // size, and set the color
                board[i][j] = new JLabel( );
                board[i][j].setHorizontalAlignment( JLabel.CENTER );
                board[i][j].setFont( 
                        new Font( Font.SANS_SERIF, Font.PLAIN, 36 ) );
                board[i][j].setOpaque( true );
                
                // Create a new instance of a private BoardMouseAdapter, and
                // set it as the listener for this JLabel
                board[i][j].addMouseListener( new BoardMouseAdapter( i, j ) );
                
                // Add the JLabel to the center panel (the chess board)
                centerPanel.add( board[i][j] );
            }
        }
        
        // Update the board state -- we make this a helper method since we'll
        // be doing it quite a bit
        updateDisplay( );
        
        // Add the center panel
        add( centerPanel );
    }
    
    // Updates the pieces at each square (based on the model)
    private void updateDisplay( )
    {
        // Update the status label
        status.setText( "isWhiteMove: " + boardModel.isWhiteMove( ) + ";  " 
                      + "isPlaying: " + boardModel.isPlaying( ) + ";  "
                      + "getWinner: " + boardModel.getWinner( ) );
        
        // Loop for updating the squares
        for( int i = 0; i < 8; i++ )
        {
            for( int j = 0; j < 8; j++ )
            {
                // Get the location, ask the board model what the state of that
                // location is, and display it
                String location = "" + (char)(j + 'A') + (char)(i + '1');
                ChessSquare square = boardModel.getSquare( location );
                
                if( (i + j) % 2 == 0 )
                {
                    board[i][j].setBackground( Color.LIGHT_GRAY );
                }
                else
                {
                    board[i][j].setBackground( Color.WHITE );
                }
                
                board[i][j].setText( getPieceString( square ) );
            }
        }
    }
    
    // Converts the given ChessSquare into the appropriate Unicode character.
    // Note that this assumes the ordering of the enumerated type is:
    //  EMPTY, PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING
    // If you have a different order, that is OK, but you will have to modify
    // this method (let me know if you need help with this).
    private String getPieceString( ChessSquare sq )
    {
        // Determine which piece type it is by looking at the "ordinal" -- the
        // position the enumerated type appears at.  EMPTY is 0, KING is 6.
        int pieceType = sq.getPieceType( ).ordinal( );
        char pieceGraphic = ' ';
        
        // If the square isn't empty, pick the right piece display for it
        if( pieceType > 0 )
        {
            pieceGraphic = (char)(9818 - pieceType);
            if( !sq.getPlayer( ) )
            {
                pieceGraphic += 6;
            }
        }
        
        return "" + pieceGraphic;
    }
    
    // A private inner class for listening for mouse events.  Each instance
    // has access to the instance variables of a ChessBoardPanel.
    private class BoardMouseAdapter extends MouseAdapter
    {
        String location;
        
        // When created, this adapter is given the position of the square it
        // is listening to
        public BoardMouseAdapter( int row, int col )
        {
            location = "" + (char)(col + 'A') + (char)(row + '1');
        }
        
        // Overrides the mouseClicked method to move the chess piece (if
        // appropriate)
        public void mouseClicked( MouseEvent me )
        {
            if( firstClick && boardModel.isPlaying( ) )
            {
                // Remember this is the first place someone clicked
                firstClickLocation = location;
                
                // Get the list of valid moves and highlight the squares
                // returned
                String[] validMoves = boardModel.getMoves( location );
                
                for( int i = 0; i < validMoves.length && validMoves[i] != null; 
                        i++ )
                {
                    int row = (validMoves[i].charAt(1) - '1');
                    int col = validMoves[i].toUpperCase( ).charAt(0) - 'A';
                    
                    board[row][col].setBackground( Color.PINK );
                }
                
                // Only keep thinking this is the "first click" if no valid
                // moves are returned
                firstClick = validMoves.length == 0 || validMoves[0] == null;
            }
            else if( boardModel.isPlaying( ) )
            {
                // Make a move and update the display
                try
                {
                    boardModel.movePiece( firstClickLocation, location );
                }
                catch( InvalidMoveException m )
                {
                    JOptionPane.showMessageDialog( ChessBoardPanel.this, m );
                }
                
                // Update the display, including re-coloring the squares 
                updateDisplay( );
                
                // Switch 'firstClick' back to 'true'
                firstClick = true;
            }
            else
            {
                // The game is no longer going, display what the "getWinner"
                // message was.
                JOptionPane.showMessageDialog( ChessBoardPanel.this, 
                        "Game is over: getWinner returns " 
                        + boardModel.getWinner( ) );
            }
        }
    }
    
    public static void main( String[] args )
    {
        // Create a JFrame and put the ChessBoardPanel into it
        JFrame jf = new JFrame( "Racing Kings" );
        jf.add( new ChessBoardPanel( ) );
        
        // Initialization: size, default close operation, visibility
        jf.setSize( 500, 500 );
        jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        jf.setVisible( true );
    }
}
