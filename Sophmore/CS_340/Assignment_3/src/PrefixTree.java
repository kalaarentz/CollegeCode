import java.util.LinkedList;

/**
 * Prefix Tree Interface
 * 
 * @author J. Hursey
 */
public interface PrefixTree {
    
    /**
     * Insert given String into prefix tree.
     * 
     * @param String s to be inserted into prefix tree.
     */
    public void insert( String s );

    /**
     * Returns a list of all words from tree starting with input prefix.
     * The list must be sorted in ascending lexographical order.
     * 
     * @param prefix String for which to search.
     * @return LinkedList<String> of all words beginning with input prefix.
     */
    public LinkedList<String> getMatches( String prefix );

    /**
     * Return a pre-order traversal of the string.
     * Child nodes are surrounded by parenthesis.
     * If the tree is empty then the empty string "" is returned.
     * Below is a properly formatted tree rooted at "" for dictionary_small.txt
     *  "" ("IC" ("E" ("", "CREAM" ("") ) , "ICLE" ("") ) , "LOVE" ("", "LY" ("") ) ) 
     *   
     * @return Properly formatted string
     */
    public String toPrefixString( );
    
    /**
     * Write the tree in the graphviz Dot format as a 'digraph'
     * (See assignment specification for details on file format.
     * 
     * @param filename The filename to save as
     */
    public void writeDotFile( String filename );

    /**
     * Write the subtree in the graphviz Dot format as a 'digraph'
     * (See assignment specification for details on file format.
     * 
     * @param filename The filename to save as
     * @param The prefix specifying the subtree to write
     */
    public void writeDotFile( String filename, String prefix );

}
