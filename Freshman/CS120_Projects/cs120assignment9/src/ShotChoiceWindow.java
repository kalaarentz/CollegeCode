/**
 * Window to allow the user to select a value in a Rock-Paper-Scissors-Lizard-Spock game.
 * 
 * Last Modified Nov. 9, 2014
 * @author J. Hursey
 */
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;

@SuppressWarnings("serial")
public class ShotChoiceWindow extends JDialog implements ActionListener, KeyListener {
    // Default Button height
    private final int BUTTON_HEIGHT = 25;

    // The Shot selected by the user
    private ShotChoice shot;

    // The buttons to select the shot
    private JButton btnRock, btnPaper, btnScissors, btnLizard, btnSpock;

    /**
     * Window to choose a shot.
     * This is a modal window meaning that the rest of the program pauses while
     * this window is present.
     * 
     * You can call it like the following.
     * <pre><code>
     *  ShotChoiceWindow choiceWin = new ShotChoiceWindow(window, "Example Title");
     *  ShotChoice myShot = choiceWin.getShotChosen();
     * </code></pre>
     * 
     * @param parentWindow
     * @param title
     */
    public ShotChoiceWindow(Window parentWindow, String title) {
        // Attach to the parent window, and make sure the parent window
        // waits for us to return
        super(parentWindow.getJFrame(), true);

        // Setup the initial size of the dialog window
        int gap = 5;
        int width, height;
        width = 300;
        height = 20 + 4 * gap + 6 * BUTTON_HEIGHT;
        int xPos = (parentWindow.getWidth() - width) / 2;
        int yPos = (parentWindow.getHeight() - height) / 2;

        setTitle(title);
        setBounds(xPos, yPos, width, height);
        this.getContentPane().setBackground( Color.white );
        setLayout( null );
        setResizable(false);

        /*
         * Add Buttons
         */
        xPos = 10;
        yPos = 10;
        btnRock = new JButton("Rock");
        btnRock.setSize(width - 2*xPos, BUTTON_HEIGHT);
        btnRock.setLocation(xPos, yPos);
        btnRock.addActionListener(this);
        btnRock.addKeyListener(this);
        this.add(btnRock);

        yPos = yPos + BUTTON_HEIGHT + gap;
        btnPaper = new JButton("Paper");
        btnPaper.setSize(width - 2*xPos, BUTTON_HEIGHT);
        btnPaper.setLocation(xPos, yPos);
        btnPaper.addActionListener(this);
        btnPaper.addKeyListener(this);
        this.add(btnPaper);

        yPos = yPos + BUTTON_HEIGHT + gap;
        btnScissors = new JButton("Scissors");
        btnScissors.setSize(width - 2*xPos, BUTTON_HEIGHT);
        btnScissors.setLocation(xPos, yPos);
        btnScissors.addActionListener(this);
        btnScissors.addKeyListener(this);
        this.add(btnScissors);

        yPos = yPos + BUTTON_HEIGHT + gap;
        btnLizard = new JButton("Lizard");
        btnLizard.setSize(width - 2*xPos, BUTTON_HEIGHT);
        btnLizard.setLocation(xPos, yPos);
        btnLizard.addActionListener(this);
        btnLizard.addKeyListener(this);
        this.add(btnLizard);

        yPos = yPos + BUTTON_HEIGHT + gap;
        btnSpock = new JButton("Spock");
        btnSpock.setSize(width - 2*xPos, BUTTON_HEIGHT);
        btnSpock.setLocation(xPos, yPos);
        btnSpock.addActionListener(this);
        btnSpock.addKeyListener(this);
        this.add(btnSpock);

        /*
         * Make it visible
         */
        setVisible( true );
        
        this.requestFocus();
        addKeyListener(this);
    }

    /**
     * Access the shot selected by the user
     * 
     * @return The ShotChoice selected by the user
     */
    public ShotChoice getShotChosen() {
        return shot;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // Create the shot associated with the button.
        if( e.getSource() == btnRock ) {
            shot = new ShotChoice( ShotChoice.ROCK );
        }
        else if( e.getSource() == btnPaper ) {
            shot = new ShotChoice( ShotChoice.PAPER );
        }
        else if( e.getSource() == btnScissors ) {
            shot = new ShotChoice( ShotChoice.SCISSORS );
        }
        else if( e.getSource() == btnLizard ) {
            shot = new ShotChoice( ShotChoice.LIZARD );
        }
        else if( e.getSource() == btnSpock ) {
            shot = new ShotChoice( ShotChoice.SPOCK );
        }
        else {
            shot = new ShotChoice( ShotChoice.INVALID );
        }

        setVisible( false );
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // to determine what key was pressed, we can get is code
        int i = e.getKeyCode();

        if( i == KeyEvent.VK_1 ) {
            btnRock.doClick();
        }
        else if( i == KeyEvent.VK_2 ) {
            btnPaper.doClick();
        }
        else if( i == KeyEvent.VK_3 ) {
            btnScissors.doClick();
        }
        else if( i == KeyEvent.VK_4 ) {
            btnLizard.doClick();
        }
        else if( i == KeyEvent.VK_5 ) {
            btnSpock.doClick();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }
}
