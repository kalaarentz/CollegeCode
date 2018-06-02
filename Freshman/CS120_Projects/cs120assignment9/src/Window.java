import javax.swing.JFrame;
import javax.swing.JComponent;
import java.awt.Color;

/**
 * CS 120: Support Class
 * A simple JFrame-using class to create a window to display "things"
 * things = JComponents
 * 
 * Added accessor for the internal JFrame so we can setup a mouse listener
 * 
 * Last Modified: April 21, 2013
 * @author Josh Hursey
 * @author M. Allen
 *
 */
public class Window {
    /** JFrame window being wrapped */
    private JFrame window;

    /**
     * Constructor for the window
     * Sets up a window with some default properties
     * 
     * post: JFrame created with ( getTitle() == "Window" ) && ( getX() == 50 )
     * && ( getY() == 50 ) && ( getWidth() == 200 ) && ( getHeight() == 100 ) &&
     * ( getLayout() == null ) && ( getBackground() == Color.white ) &&
     * isVisible() && !isResizable()
     */
    public Window() {
        window = new JFrame( "Window" );
        window.setLocation( 50, 50 );
        window.setSize( 200, 100 );
        window.setLayout( null );
        window.getContentPane().setBackground( Color.white );
        window.setResizable( false );
        window.setVisible( true );

        // Exit upon closing
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Access the JFrame
     * @return reference to the internal JFrame
     */
    public JFrame getJFrame() {
        return window;
    }

    /**
     * Set the location of the window on the screen
     *   post: window.getX() == x and window.getY() == y
     * 
     * @param x X position
     * @param y Y position
     */
    public void setLocation( int x, int y ) {
        window.setLocation( x, y );
    }

    /**
     * Return the X position of the window
     * 
     * @return X position of the window
     */
    public int getX() {
        return window.getX();
    }

    /**
     * Return the Y position of the window
     * 
     * @return Y position of the window
     */
    public int getY() {
        return window.getY();
    }

    /**
     */
    /**
     * Set the size of the window
     * Width : Minimum = 200, Maximum = 1200
     * Height: Minimum = 100, Maximum = 1200
     *   post: ( window.getContentPane().getWidth() == w )
     *         (minimum width == 200 and maximum width == 600)
     *         ( window.getContentPane().getHeight() == h )
     *         (minimum height == 100 and maximum height == 600)
     * 
     * @param w Width of the window
     * @param h Height of the window
     */
    public void setSize( int w, int h ) {
        w = Math.max( w, 200 );
        w = Math.min( w, 1200 );
        h = Math.max( h, 100 );
        h = Math.min( h, 1200 );
        window.setSize( w + window.getInsets().left + window.getInsets().right,
                        h + window.getInsets().top + window.getInsets().bottom );
    }

    /**
     * Get the height of the window
     * 
     * @return Height of the display area
     */
    public int getHeight() {
        return window.getHeight() - window.getInsets().bottom - window.getInsets().top;
    }

    /**
     * Get the width of the window
     * 
     * @return Width of the display area
     */
    public int getWidth() {
        return window.getWidth() - window.getInsets().left - window.getInsets().right;
    }

    /**
     * Set the title of the window
     *   post: window.getTitle() == title
     * 
     * @param title Title to use for the display area
     */
    public void setTitle( String title ) {
        window.setTitle( title );
    }

    /**
     * Set the color of the background
     *   post: window.getBackground() == col
     * 
     * @param color Color to use for background
     */
    public void setBackground( Color color ) {
        window.getContentPane().setBackground( color );
        window.repaint();
    }

    /**
     * Add a graphical component to the window
     *   post: adds input any graphical JComponent, like a Triangle or Oval, to
     *   JFrame
     * 
     * @param component Graphical component to add to the display window
     */
    public void add( JComponent component ) {
        window.add( component, 0 );
        component.repaint();
    }

    /**
     * Remove a graphical component from the window
     *    post:  removes input JComponent, if present
     * 
     * @param component Graphical component to remove from the display window
     */
    public void remove( JComponent component ) {
        window.remove( component );
        window.repaint();
    }

    /**
     * Request to repaint the window
     *   post: request for window.repaint() to update graphics
     */
    public void repaint() {
        window.repaint();
    }
}
