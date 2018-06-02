/**
 * Author: David Riley & M. Allen
 * 
 * Draws a simple graphical rectangle.
 */
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class Rectangle extends JComponent {

    /**
     * post: getX() == x and getY() == y and getWidth() == w and getHeight() ==
     * h and getBackground() == Color.black
     */
    public Rectangle(int x, int y, int w, int h) {
        super();
        setBounds(x, y, w, h);
        setBackground(Color.black);
    }

    /**
     * @param comp JComponent to test for intersection
     * @return true iff this intersects
     */
    public boolean intersects(JComponent comp) {
        if ( this.getX() >= comp.getX()
                && this.getX() <= comp.getX() + comp.getWidth()
                && this.getY() >= comp.getY()
                && this.getY() <= comp.getY() + comp.getHeight() ) {
            return true;
        }

        if ( comp.getX() >= this.getX()
                && comp.getX() <= this.getX() + this.getWidth()
                && comp.getY() >= this.getY()
                && comp.getY() <= this.getY() + this.getHeight() ) {
            return true;
        }

        return false;
    }

    /**
     * post: Draws a filled Rectangle and the upper left corner is ( getX(),
     * getY() ) and the rectangle's dimensions are getWidth() and getHeight()
     * and the rectangle's color is getBackground()
     */
    public void paint(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
        paintChildren(g);
    }
}