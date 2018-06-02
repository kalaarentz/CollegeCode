import java.io.IOException;
import java.util.Random;

/**
 * Simple driver to test the additional interfaces for the Binary Search Tree
 * 
 * @author J. Hursey
 */
public class BSTDriver {

    public static void main(String[] args) {
        BSTDriver d = new BSTDriver();
    }
    
    public BSTDriver() {
        BinarySearchTree<Integer> t = new BinarySearchTree<Integer>();
        Random rand = new Random();
        final int nums = 7;

        t.insert( 50 );
        t.insert( 25 );
        t.insert( 75 );
        t.insert( 90 );
        t.insert( 70 );
        t.insert( 30 );

        
        for ( int i = 0; i < nums; i++ ) {
            t.insert( rand.nextInt( 100 ) );
        }
        
        
        System.out.println("------------------------");
        System.out.println("PREFIX: " + t.toPrefixString() );
        System.out.println("------------------------");
        System.out.println("Writing dot file:");
        t.writeDotFile("tmp.dot");
        System.out.println("------------------------");
    }

}
