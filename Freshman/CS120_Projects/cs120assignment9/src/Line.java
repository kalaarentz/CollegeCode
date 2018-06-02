/**
 * Author: David Riley & M. Allen
 * 
 * Creates a simple graphical line.
 */
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class Line extends JComponent {

    private int thickness;
    private int theX1, theY1, theX2, theY2;

    public Line(int x1, int y1, int x2, int y2) {
        super();
        thickness = 1;
        theX1 = x1;
        theY1 = y1;
        theX2 = x2;
        theY2 = y2;
        setBounds(Math.min(x1, x2) - 1, Math.min(y1, y2) - 1,
                  Math.max(x1, x2) + 2, Math.max(y1, y2) + 2);
        setBackground(Color.black);
    }

    public Line(int x1, int y1, int x2, int y2, int t) {
        super();
        thickness = t;
        theX1 = x1;
        theY1 = y1;
        theX2 = x2;
        theY2 = y2;
        setBounds(Math.min(x1, x2) - t - 1, Math.min(y1, y2) - t - 1,
                  Math.max(x1, x2) + t + 1, Math.max(y1, y2) + t + 1);
        setBackground(Color.black);
    }

    public void setThickness(int t) {
        thickness = t;
        setBounds(Math.min(theX1, theX2) - t - 1, Math.min(theY1, theY2) - t - 1,
                  Math.max(theX1, theX2) + t + 1, Math.max(theY1, theY2) + t + 1);
    }

    /**
     * postconditon this method draws a line segment from point (getX(),getY())
     * to point (x2, y2) in the background coordinate system and with color from
     * getBackground()
     */
    public void paint(Graphics g) {
        ((Graphics2D) g).setStroke(new BasicStroke(thickness,
                                                   BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));

        g.setColor(getBackground());

        g.drawLine(theX1 - getX(), theY1 - getY(),
                   theX2 - getX(), theY2 - getY());

        ((Graphics2D) g).setStroke(new BasicStroke(1.0f));
    }

}