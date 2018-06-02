import java.util.Iterator;
import java.util.ListIterator;

/**
 * An interface to a List-like ADT that maintains two views of the data:
 *   - insertion-ordered : Order in which items were inserted into the collection.
 *   - sort-ordered : Order in which items want to be naturally ordered based on the Comparable implementation.
 *   
 * @author J. Hursey
 * Date Last Modified: Sept. 27, 2015
 *
 * @param <T> Data type that must extend Comparable<T>
 */
public interface MultiList<T extends Comparable<T>> extends Iterable<T> {
    
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
     * Create a String containing all of the elements in the list.
     * Elements are displayed in insertion-order and comma separated.
     * For example, if the list contains Integers:
     *   {-1, -2, -3, -4, -5, 4, 3, 2, 1, 0, 3, 4, 5, 6, 7}
     * If the list is empty then it will return a string:
     *   {}
     *   
     * @return String representation of the list
     */
    public String toString();

    /**
     * Create a String containing all of the elements in the list.
     * Elements are displayed in sorted-order and comma separated.
     * For example, if the list contains Integers:
     *   {-5, -4, -3, -2, -1, 0, 1, 2, 3, 3, 4, 4, 5, 6, 7}
     * If the list is empty then it will return a string:
     *   {}
     *   
     * @return String representation of the list
     */
    public String toStringSorted();

    /**
     * Add an item to the end of the insertion-ordered view of the list.
     * The sort-ordered view of the list will put this element in the correct position.
     * 
     * @param item Item to add to the list
     * @return true
     */
    public boolean add(T item);

    /**
     * Remove the first element from the list relative to the insertion-ordered view of the list.
     * 
     * @return The value of the element removed
     */
    public T removeFirst();

    /**
     * Remove the first element from the list relative to the sort-ordered view of the list.
     * 
     * @return The value of the element removed
     */
    public T removeFirstSorted();

    /**
     * Remove the last element from the list relative to the insertion-ordered view of the list.
     * 
     * @return The value of the element removed
     */
    public T removeLast();

    /**
     * Remove the last element from the list relative to the sort-ordered view of the list.
     * 
     * @return The value of the element removed
     */
    public T removeLastSorted();

    /**
     * Test if the item is in the list. Returns the number of occurrences of the item in the list.
     * 
     * @param item The item to look for in the list.
     * @return The number of times this item is present in the list.
     */
    public int contains(T item);

    /**
     * Review the specification for an Iterator at:
     *   https://docs.oracle.com/javase/8/docs/api/java/util/Iterator.html
     * Your Iterator must support the following operations:
     *   hasNext, next, remove
     * 
     * This Iterator will start at the beginning of the insertion-ordered view of the list.
     *   
     * @return A valid Iterator object for this collection.
     */
    public Iterator<T> iterator();

    /**
     * Review the specification for an Iterator at:
     *   https://docs.oracle.com/javase/8/docs/api/java/util/Iterator.html
     * Your Iterator must support the following operations:
     *   hasNext, next, remove
     * 
     * This Iterator will start at the beginning of the sort-ordered view of the list.
     *   
     * @return A valid Iterator object for this collection.
     */
    public Iterator<T> sortedIterator();
    
    /**
     * Review the specification for an ListIterator at:
     *   https://docs.oracle.com/javase/8/docs/api/java/util/ListIterator.html
     * Your ListIterator must support the following operations:
     *   hasNext, next, remove, hasPrevious, previous, add, set
     *   
     * It does not need to support nextIndex and previousIndex. If these two operations are not
     * supported then the exception specified in the JavaDocs (link above) must be thrown. 
     * 
     * This ListIterator will start at the beginning of the insertion-ordered view of the list.
     *   
     * @return A valid ListIterator object for this collection.
     */
    public ListIterator<T> unsortedListIterator();

    /**
     * Review the specification for an ListIterator at:
     *   https://docs.oracle.com/javase/8/docs/api/java/util/ListIterator.html
     * Your ListIterator must support the following operations:
     *   hasNext, next, remove, hasPrevious, previous, add, set
     *   
     * It does not need to support nextIndex and previousIndex. If these two operations are not
     * supported then the exception specified in the JavaDocs (link above) must be thrown. 
     * 
     * This ListIterator will start at the specified index of the insertion-ordered view of the list.
     *   
     * @return A valid ListIterator object for this collection.
     */
    public ListIterator<T> unsortedListIterator(int index);

    /**
     * Review the specification for an ListIterator at:
     *   https://docs.oracle.com/javase/8/docs/api/java/util/ListIterator.html
     * Your ListIterator must support the following operations:
     *   hasNext, next, remove, hasPrevious, previous, add, set
     *   
     * It does not need to support nextIndex and previousIndex. If these two operations are not
     * supported then the exception specified in the JavaDocs (link above) must be thrown. 
     * 
     * This ListIterator will start at the beginning of the sort-ordered view of the list.
     *   
     * @return A valid ListIterator object for this collection.
     */
    public ListIterator<T> sortedListIterator();

    /**
     * Review the specification for an ListIterator at:
     *   https://docs.oracle.com/javase/8/docs/api/java/util/ListIterator.html
     * Your ListIterator must support the following operations:
     *   hasNext, next, remove, hasPrevious, previous, add, set
     *   
     * It does not need to support nextIndex and previousIndex. If these two operations are not
     * supported then the exception specified in the JavaDocs (link above) must be thrown. 
     * 
     * This ListIterator will start at the specified index of the sort-ordered view of the list.
     *   
     * @return A valid ListIterator object for this collection.
     */
    public ListIterator<T> sortedListIterator(int index);

}
