/*
 * Author: Kala Arentz
 *
 * Date: Feb 10, 2015
 *
 * Purpose: the starting point for your ChessSquare
 */
public class ChessSquare
{
    // 7 possible choices for what piece is at a square (including no piece)
    public enum ChessPiece { EMPTY, PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING };
    
    // Instance variable representing the piece at this particular square
    private ChessPiece piece;
    private boolean colorOfPiece;
    
    public ChessSquare( ChessPiece pieceType, boolean color )
    {
    	this.setPieceType( pieceType );
    	this.setPlayer( color );
    	
    }
    
    // Returns the type of the piece at this square
    public ChessPiece getPieceType( )
    {
        return piece;
    }
    
    public void setPieceType( ChessPiece type)
    {
    	piece = type;
    }
    
    public boolean getPlayer()
    {
    	return colorOfPiece;
    }
    
    public void setPlayer( boolean color )
    {
    	colorOfPiece = color;
    }
    
}