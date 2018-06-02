import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * Program Description: This will be a class used by TallPrefixTree.
 * This program keeps a unique list of keys or strings
 * that have some type of generic value.
 *
 * Date Last Modified: Nov 2, 2015
 *
 * @author: kalaarentz
 */
public class StringKVList<V> implements KVList<V> {

	private Node<V> head;
	private Node<V> tail;
	private int theSize;

	public StringKVList() {

		clear();
	}

	@Override
	public void clear() {

		head = new Node<V>( null, null, null, tail );
		tail = new Node<V>( null, null, head, null );
		head.next = tail;

		theSize = 0;
	}

	@Override
	public int size() {

		return theSize;
	}

	@Override
	public void insert(String key, V value) {

		// if the the size is empty
		if ( size() == 0 )
		{
			Node<V> node = new Node<>( key, value, head, tail );
			head.next = node;
			tail.prev = node;

			theSize++;
		}
		else
		{
			//unique key
			if ( contains( key ) )
			{
				throw new IllegalArgumentException( "Key Exists Already!" );
			}
			else
			{
				Node<V> current = head.next;
				// integer variable so you don't add to the end of the
				// list right away
				int idx = 0;

				// sorting list and checking to make sure key is unique
				while ( current != tail )
				{

					// to insert into the sorted list by key
					if ( key.compareTo( current.key ) <= -1 )
					{
						idx = -1;

						Node<V> node = new Node<>( key, value, 
								current.prev, current );
						current.prev.next = node;
						current.prev = node;

						theSize++;
						break;
					}

					current = current.next;
				}

				// only add the information if the idx was not 
				// changed by the if statement above
				if ( idx != -1 )
				{
					// if the key is unique and not less then anything add  
					// to the end of the list
					Node<V> node = new Node<>( key, value, tail.prev, tail );
					tail.prev.next = node;
					tail.prev = node;

					theSize++;
				}
			}
		}
	}

	@Override
	public V set(String key, V value) {
		
		Node<V> current = head.next;
		V tmp = null;

		// if the key does not exist then the else
		// statement will throw the NoSuchElementException

		if ( contains( key ) )
		{
			while ( current != tail )
			{
				if ( key.equals( current.key ) )
				{
					tmp = current.value;
					current.value = value;
				}
				current = current.next;
			}
		}
		else
		{
			throw new NoSuchElementException( "Key does not exist" );
		}
		
		return tmp;

	}

	@Override
	public V get(String key) {
		
		Node<V> current = head.next;
		V tmp = null;

		// if the key does not exist then the else
		// statement will throw the NoSuchElementException

		if ( contains( key ) )
		{
			while ( current != tail )
			{
				if ( key.equals( current.key) )
				{
					tmp = current.value;
				}
				current = current.next;
			}

		}
		else
		{
			throw new NoSuchElementException( "Key does not exist" );
		}

		return tmp;
	}

	@Override
	public boolean contains(String key) {

		Node<V> current = head.next;

		while ( current != tail )
		{
			//unique key
			if ( key.equals( current.key ) )
			{
				return true;
			}

			current = current.next;
		}

		return false;
	}

	@Override
	public void writeDotFile(String filename) {

		try 
		{
			PrintWriter file = new PrintWriter( filename );
			Node<V> current = head.next;
			int idx = 0;

			file.write( "graph StringKVListGraph { \n "
					+ "// Display the nodes left-to-right \n rankdir=LR; "
					+ "\n\n //List of Nodes \n" );

			// make the list of nodes
			while ( current != tail )
			{
				file.write( "n_" + idx +" [label=\"" + current.key + "\"];" );
				file.println();

				current = current.next;
				idx++;

			}

			current = head.next;

			file.println();
			file.write( "//Connections between nodes \n" );

			idx = 0;
			// write the connection between the nodes
			while ( current != tail )
			{
				if( idx == 0 )
				{
					file.write( "n_" + idx );
				}
				else
				{
					file.write( " -- " + "n_" + idx  );
				}

				current = current.next;
				idx ++;
			}

			file.write( ";" );

			file.println();
			file.write("}");
			file.close();

		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public Iterator<V> iterator() {

		return new StringKVListIterator();
	}

	/**
	 * 
	 * Program Description: iterator for the generic value that is 
	 * held in the this node
	 *
	 * Date Last Modified: Oct 31, 2015
	 *
	 * @author: kalaarentz
	 */
	private class StringKVListIterator implements Iterator<V>
	{
		private Node<V> current = head.next;

		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {

			return current != tail;
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		@Override
		public V next() {

			if( !hasNext() )
			{
				throw new NoSuchElementException( "Out of Elements" );
			}

			V tmp = current.value;
			current = current.next;

			return tmp;

		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		public void remove()
		{
			throw new UnsupportedOperationException( "We will not "
					+ "use this method" );
		}

	}

	/**
	 * This is the doubly-linked list node.
	 * Two sets of links are provided.
	 * One for the insertion-order and the other for sorted-order.
	 */
	private static class Node<V> {

		private V value;
		private String key;
		private Node<V> prev;
		private Node<V> next;

		/**
		 * Constructs basic list node.
		 * 
		 * @param d The data element held by the list node.
		 * @param p A Node that occurs immediately before 
		 * this in list insertion order.
		 * @param n A node that occurs immediately after 
		 * this in list insertion order.
		 * 
		 */
		private Node(String ky, V vle, Node<V> p, Node<V> n) {
			value = vle;
			key = ky;
			prev = p;
			next = n;

		}
	}
}
