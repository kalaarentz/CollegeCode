import javax.swing.*;
import java.awt.event.*;

/**
 * CS 120: Support Class
 * An extension to JTextField that binds the button action to a Driver object.
 * Driver must implement public void handleTextFieldAction().
 * 
 * Last Modified: Nov. 10, 2012
 * 
 * @author Josh Hursey
 * @author M. Allen
 * 
 */
@SuppressWarnings("serial")
public class ActionTextField extends JTextField implements ActionListener {

    /* Driver to tell about any action events. */
    private Driver driver;

    /**
     * pre: d must be properly initialized; must possess handleTextFieldAction()
     * method.
     * 
     * post: text == driver variable instantiated.
     * 
     * @param d a Driver for communication.
     * 
     */
    public ActionTextField(Driver d) {
        super();
        driver = d;
        addActionListener(this);
    }

    /**
     * post: this sent to driver.handleTextFieldAction()
     * 
     * @param e ActionEvent from associated objects
     * 
     */
    public void actionPerformed(ActionEvent e) {
        driver.handleTextFieldAction(this);
    }
}
