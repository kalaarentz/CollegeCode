import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;

/**
 * CS 120: 
 * A simple popup window that display a set of word-wrapped text
 * 
 *  *** Do NOT Edit this file ***
 * 
 * Last Modified: Nov. 11, 2014
 * @author Josh Hursey
 *
 */
@SuppressWarnings( "serial" )
public class PopUpWindow extends JDialog implements ActionListener {
    private int WINDOW_HEIGHT = 400;
    private int WINDOW_WIDTH = 500;

    private String displayText;
    private Window contextWindow;

    /**
     * Create a new popup window
     * 
     * @param w The Window that contains the popup
     * @param title Title of the popup window
     * @param text Text to display inside the window
     */
    public PopUpWindow(Window w, String title, String text) {
        super(w.getJFrame());
        displayText = text;
        contextWindow = w;

        setTitle(title);

        paintWindow();
    }


    /**
     * Paint the Window
     */
    private void paintWindow() {
        // Setup the Panel
        JPanel myPanel = new JPanel();
        myPanel.setLayout(null);
        this.setResizable( false );
        getContentPane().add(myPanel);
        setLocationRelativeTo(contextWindow.getJFrame());
        setVisible(true);
        setLocation(contextWindow.getWidth()/2 - WINDOW_WIDTH/2,
                    contextWindow.getHeight()/2 - WINDOW_HEIGHT/2);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        /*
         * A Scrolling text area 
         */
        JTextArea ta = new JTextArea();
        ta.setSize(WINDOW_WIDTH - 40,
                   WINDOW_HEIGHT - 80);
        ta.setSize(WINDOW_WIDTH - 40,
                   80);
        ta.setLocation(20, 20);
        ta.setEditable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setText(displayText);
        ta.setBackground(Color.WHITE);
        ta.setText(displayText);
        ta.setCaretPosition(0);

        JScrollPane scroll = new JScrollPane(ta);
        scroll.setLocation(20, 20);
        scroll.setSize(WINDOW_WIDTH - 40,
                       WINDOW_HEIGHT - 80);
        myPanel.add(scroll);

        /*
         * OK button
         */
        JButton okButton = new JButton("Close Window");
        okButton.setSize(WINDOW_WIDTH - 40,
                         20);
        okButton.setLocation(20,
                             scroll.getHeight() + 20);
        okButton.addActionListener(this);
        myPanel.add(okButton);
    }

    /*
     * When an action is performed then hide the window
     * (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed( ActionEvent e ) {
        setVisible(false);
    }
}
