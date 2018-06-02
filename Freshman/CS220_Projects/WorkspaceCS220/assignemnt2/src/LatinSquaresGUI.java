/*
 * Author: Andrew Berns
 *
 * Date: Mar 22, 2015
 *
 * Purpose: a GUI to test out your LatinSquareModel
 */
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.GridLayout;
import java.awt.BorderLayout;

import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LatinSquaresGUI extends JPanel implements ActionListener
{
    // Our model that we'll use to control the GUI.  This is what you have
    // implemented.
    private LatinSquareModel model;
    
    // The array of JTextFields we'll use to represent the fields on the board.
    private JTextField[][] textFields;
    
    // A LatinSquaresGUI, when created, takes the valid symbols and the
    // dimensions of the board (just like a LatinSquareModel)
    public LatinSquaresGUI( String[] symbols, int n )
    {
        // Create an empty LatinSquareModel with the appropriate symbols
        model = new LatinSquareModel( symbols, n );
        textFields = new JTextField[n][n];
        
        // We'll use a BorderLayout so we can put the buttons at the bottom,
        // with the rest taking up the majority of the screen.
        setLayout( new BorderLayout( ) );
        
        // Set up the button panel, including making this GUI be the action
        // listener for the buttons
        JPanel buttonPanel = new JPanel( );
        JButton hint = new JButton( "Hint" );
        JButton solve = new JButton( "Solve" );
        hint.addActionListener( this );
        solve.addActionListener( this );
        buttonPanel.add( hint );
        buttonPanel.add( solve );
        
        // Create the JPanel we'll use to display the text fields, switch to
        // an appropriately-sized GridLayout, and then fill it with JTextFields
        JPanel textPanel = new JPanel( );
        textPanel.setLayout( new GridLayout( n, n ) );
        for( int i = 0; i < n * n; i++ )
        {
            // Create the fields
            textFields[i/n][i%n] = new JTextField( );
            textFields[i/n][i%n].setHorizontalAlignment( JTextField.CENTER );
            
            // add a FocusListener for this text field
            textFields[i/n][i%n].addFocusListener( 
                    new LatinSquareTextListener( i/n, i%n ) );
            
            textFields[i/n][i%n].setText( null );
            // Add the field to the panel
            textPanel.add( textFields[i/n][i%n] );
        }
        
        // Add the panels to the layout
        add( textPanel );
        add( buttonPanel, BorderLayout.SOUTH );
    }
    
    @Override
    public void actionPerformed( ActionEvent ae )
    {
        // If the puzzle has already been solved, don't do anything here!
        if( model.isComplete( ) )
        {
            return;
        }
        
        // Get the solution.  This should be a *copy* of the original puzzle,
        // not the puzzle itself.  I'll make sure to check each location of the
        // puzzle against 'getSquare' even on 'hint' just to make sure
        String[][] solution = model.solvedPuzzle( );
        int size = textFields.length;
        
        // If the 'Hint' button was pressed, pick a random square to write a
        // value to
        if( ae.getActionCommand( ).equals( "Hint" ) )
        {
            // Add just one value -- start scanning at a random point in the
            // square
            int randomStart = (new java.util.Random( )).nextInt( size * size );
            
            // Try to find a null element -- if not, we'll overwrite what was
            // there, but this shouldn't be a different value...
            for( int i = 0; 
                 model.getSquare( randomStart/size, randomStart%size ) != null
                         && i < size * size; i++ )
            {
                // Makes sure that 'randomStart' doesn't exceed the bounds
                // of the board
                randomStart = (randomStart + 1) % (size * size);
            }
            
            // Update the model and the view
            model.setSquare( randomStart / size, randomStart % size,
                    solution[randomStart/size][randomStart%size] );
            textFields[randomStart/size][randomStart%size].setText( 
                    solution[randomStart/size][randomStart%size] );
            
            // Update the display.  Hopefully this catches when you change the
            // original board and not a copy.
            for( int i = 0; i < size * size; i++ )
            {
                textFields[i/size][i%size].setText( 
                        model.getSquare( i/size, i%size ) );
            }
        }
        else
        {
            // Add all values -- note this doesn't verify the existing values
            // haven't been changed!
            for( int i = 0; i < size * size; i++ )
            {
                model.setSquare( i/size, i%size, solution[i/size][i%size] );
                textFields[i/size][i%size].setText( solution[i/size][i%size] );
            }
        }
    }
    
    // This inner class is the 'FocusListener' for each text field.  It will
    // set the appropriate square in the model whenever the user has clicked
    // out of the square
    class LatinSquareTextListener implements FocusListener
    {
        // The row and column of our puzzle this listener is listening to
        private int row, col;
        
        // The constructor sets the row and column
        public LatinSquareTextListener( int r, int c )
        {
            row = r;
            col = c;
        }
        
        @Override
        public void focusGained( FocusEvent fe ) { }
        
        // When focus is lost, take what was in that square and attempt to
        // write it to the puzzle.  If any exception is thrown, it'll be
        // displayed and the program will continue.
        @Override
        public void focusLost( FocusEvent fe )
        {
            try
            {
                // Set the square and determine if it has won the game
                String field = ((JTextField) fe.getSource( )).getText( );
                if( field.length( ) > 0 && model.setSquare( row, col, field ) )
                {
                    JOptionPane.showMessageDialog( null, "You win!" );
                }
            }
            catch( Exception e )
            {
                // Catch any exception throw and display it.  It's up to you to
                // verify the right exception is thrown!
                JOptionPane.showMessageDialog( null, e, "Error", 
                        JOptionPane.ERROR_MESSAGE );
            }
            
            // Updates the text field to be whatever your model says it is.
            ((JTextField)fe.getSource()).setText( model.getSquare( row, col ));
        }
    }
    
    public static void main( String[] args )
    {
        // Get the size and symbols (you should replace this with something
        // else if you don't want to keep typing it in)
        int size = Integer.parseInt( 
                JOptionPane.showInputDialog( "Input square side length:" ) );
        String[] symbols = JOptionPane.showInputDialog( 
                "Input symbols, separated by a ',' (no space):" ).split( "," );
                
        // Create the GUI inside a frame using the values input from the user
        JFrame mainFrame = new JFrame( "Latin Squares" );
        mainFrame.setSize( 600, 600 );
        mainFrame.add( new LatinSquaresGUI( symbols, size ) );
        mainFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        mainFrame.setVisible( true );
    }
}