/**
 * Author: M. Allen
 * 
 * An extension to JButton that binds the button action to a Driver object.
 * Driver must implement public void handleAction().
 * Button can be used to create animation in Driver by repeatedly calling
 * handleAction() on a toggle-switch basis.
 * 
 * Modified on April 14, 2013 by Josh Hursey
 * - Added the startTimer and stopTimer methods that wrap around the
 * doClick() method in the JButton to start and stop the timer events.
 */
import javax.swing.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class RepeatButton extends JButton implements ActionListener {

    /* Driver to tell about any action events. */
    private Driver driver;
    /* Timer for animation */
    private Timer timer;

    /**
     * pre: d must be properly initialized; must possess
     * handleAction() method.
     * 
     * post: text == driver variable instantiated.
     *
     * @param d a Driver for communication.
     * 
     */
    public RepeatButton(Driver d) {
        super();
        setText("Move");
        driver = d;
        addActionListener(this);
        timer = new Timer(100, this);
    }

    /**
     * post: sets Timer interval to delay
     * 
     * @param delay interval for Timer in milliseconds
     */
    public void setDelay(int delay) {
        timer.setDelay(delay);
    }

    /**
     * Start the timer events
     */
    public void startTimer() {
        // If the timer is already running, do nothing
        if ( !timer.isRunning() ) {
            this.doClick();
        }
    }

    /**
     * Stop the timer events
     */
    public void stopTimer() {
        // If the timer is not already running, do nothing
        if ( timer.isRunning() ) {
            this.doClick();
        }
    }

    /**
     * post: this sent to driver.handleAction()
     * 
     * @param e ActionEvent from associated objects
     */
    public void actionPerformed(ActionEvent e) {
        if ( e.getSource() == this ) {
            if ( timer.isRunning() ) {
                setText("Move");
                timer.stop();
            }
            else {
                setText("Stop");
                timer.start();
            }
        }

        if ( e.getSource() == timer ) {
            driver.handleAction(this);
        }
    }
}
