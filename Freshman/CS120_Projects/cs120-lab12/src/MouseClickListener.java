/**
 * Mouse click listener wrapper class
 * 
 * Last Modified on April 23, 2013
 * @author  Josh Hursey
 * 
 *  *** Do NOT Edit this file ***
 */
import java.awt.event.*;

public class MouseClickListener implements MouseListener {
    /* Driver to tell about any action events. */
    private Driver driver;

    /* Copy of the Window, in case we need it later */
    private Window window;

    /**
     * Register a mouse listener
     * @param d
     * @param w
     */
    public MouseClickListener( Driver d, Window w ) {
        super();
        driver = d;
        window = w;
        w.getJFrame().addMouseListener( this );
    }

    /**
     * Mouse clicked event translates the X/Y position to something
     * that will fit in the Window class provided.
     */
    public void mouseClicked( MouseEvent event ) {
        int x = event.getX();
        int y = event.getY();
        int button = event.getButton();

        x = x - window.getJFrame().getInsets().left;
        y = y - window.getJFrame().getInsets().top;

        if( button == MouseEvent.BUTTON1 ) {
            driver.handleMouseClick(x, y, true);
        }
        else {
            driver.handleMouseClick(x, y, false);
        }
    }

    /*
     * None of the other methods are used
     */
    
    @Override
    public void mouseEntered(MouseEvent e) {
        // not used
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // not used
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // not used
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // not used
    }
}
