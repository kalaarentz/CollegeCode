/**
 * Author: Martin Allen
 * 
 * A simple graphical Triangle class.
 * Each Triangle can be added to JFrame-style windows.
 * Triangles can be set to point up or down at instantiation.
 */
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class Triangle extends JComponent {

    private boolean up = false;

    /**
     * post: getX() == x and getY() == y
     * and getWidth() == w and getHeight() == h
     * and getBackground() == Color.black
     * and boolean up == (dir == 1)
     */
    public Triangle(int x, int y, int w, int h, int dir) {
        super();
        setBounds(x, y, w, h);
        setBackground(Color.black);
        up = (dir == 1);
    }

    /**
     * post: this method draws a filled Triangle
     * and the upper left corner of the bounding rectangle is (getX(), getY())
     * and the triangles's dimensions are getWidth() and getHeight()
     * and the triangle's color is getBackground()
     * and the triangle points: upward if (boolean up == 1) else downward
     */
    public void paint(Graphics g) {
        g.setColor(getBackground());
        int[] xArr = null;
        int[] yArr = null;

        if ( up ) {
            xArr = new int[]{0, getWidth() / 2, getWidth()};
            yArr = new int[]{getHeight(), 0, getHeight()};
        }
        else {
            xArr = new int[]{0, getWidth() / 2, getWidth()};
            yArr = new int[]{0, getHeight(), 0};
        }

        g.fillPolygon(xArr, yArr, 3);
        paintChildren(g);
    }
}