import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Key/Value sorted list interface
 * List stored in lexographic order [A-Z]
 * 
 * @author J. Hursey
 *
 * @param <V> Value to associate with the key
 */
public interface KVList<V> extends Iterable<V> {

    /**
     * Clear the list of all elements
     */
    public void clear();
    
    /**
     * Access the size of the list.
     * 
     * @return Size of the list
     */
    public int size();
    
    /**
     * Insert the key/value pair into the list.
     * List is maintained in sorted order based upon the key.
     * Keys are unique in the list
     * 
     * @param key Key to associated with the value
     * @param value Value to store
     * @throws IllegalArgumentException If the key is already in the list
     */
    public void insert( String key, V value );
    
    /**
     * Set a new value for an existing key.
     * 
     * @param key Key to lookup (must be in the list)
     * @param value New value to associate with this key
     * @return The old value
     * @throws NoSuchElementException if key not in list
     */
    public V set( String key, V value );
    
    /**
     * Get the value associated with this key.
     * Key must be in the list.
     * 
     * @param key Key to lookup (must be in the list) 
     * @return The value stored at this location.
     * @throws NoSuchElementException if key not in list
     */
    public V get( String key );

    /**
     * Determine if the key is in the list.
     * 
     * @param key Key to search for
     * @return true if the key is in the list, false otherwise.
     */
    public boolean contains( String key );
    
    /**
     * Write the list in the graphviz Dot format as a 'graph'
     * (See assignment specification for details on the file format).
     * 
     * @param filename The filename to save as
     */
    public void writeDotFile( String filename );
    
    /**
     * Review the specification for an Iterator at:
     *   https://docs.oracle.com/javase/8/docs/api/java/util/Iterator.html
     * Your Iterator must support the following operations:
     *   hasNext, next
     * Your Iterator does not need to support the following operations:
     *   remove
     * 
     * This Iterator will start at the beginning of the the list.
     *   
     * @return A valid Iterator object for this collection.
     */
    public Iterator<V> iterator();

}
