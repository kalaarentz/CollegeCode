import javax.swing.*;
import java.awt.event.*;

/**
 * CS 120: Support Class
 * An extension to JButton that binds the button action to a Driver object.
 * Driver must implement public void handleButtonAction().
 * 
 * Last Modified: Nov. 10, 2012
 * 
 * @author Josh Hursey
 * @author M. Allen
 * 
 */
@SuppressWarnings("serial")
public class ActionButton extends JButton implements ActionListener {

    /** Driver to tell about any action events. */
    private Driver driver;

    /**
     * pre: d must be properly initialized; must possess handleAction() method.
     * 
     * post: text == driver variable instantiated.
     * 
     * @param d a Driver for communication.
     * 
     */
    public ActionButton(Driver d) {
        super();
        driver = d;
        addActionListener(this);
    }

    /**
     * post: this sent to driver.handleButtonAction()
     * 
     * @param e ActionEvent from associated objects
     * 
     */
    public void actionPerformed(ActionEvent e) {
        driver.handleButtonAction(this);
    }
}
