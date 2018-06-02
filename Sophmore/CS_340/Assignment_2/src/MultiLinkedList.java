import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;


public class MultiLinkedList<T extends Comparable<T>> implements MultiList<T> {

	private Node<T> head;
	private Node<T> tail;
	private int theSize;

	/**
	 * Constructor for the LinkedLIst, will create a new linked
	 * list in the clear() method
	 */
	public MultiLinkedList()
	{
		clear();
	}
	@Override
	public void clear() {
		// NEEDS TO BE COMPLEXITY OF O( 1 )
		head = new Node<T>( null, null, tail, null, tail );
		tail = new Node<T>( null, head, null, head, null );

		theSize = 0;
	}

	@Override
	public int size() {
		// NEEDS TO BE COMPLEXITY OF O( 1 )
		return theSize;
	}

	/**
	 * This toString method will be typing out the data from the unsorted
	 * list
	 * 
	 *  @Override
	 *  @return str : will be a string representing data from linked list
	 */
	public String toString()
	{
		// NEEDS TO BE COMPLEXITY OF O( N )
		String str = "[";
		int idx = 0;

		Node<T> current = head.next;

		while ( idx < size() )
		{
			str += current.data;

			if ( idx != size() -1 )
			{
				str += ", ";
			}
			current = current.next;
			idx++;
		}

		return str + "]";

	}

	@Override
	public String toStringSorted() {
		// NEEDS TO BE COMPLEXITY OF O( N )

		String str = "[";

		int idx = 0;

		Node<T> current = head.nextSorted;

		while ( idx < size() )
		{
			str += current.data;

			if ( idx != size() -1 )
			{
				str += ", ";
			}
			current = current.nextSorted;
			idx++;
		}

		return str + "]";
	}

	@Override
	public boolean add(T item) {
		// NEEDS TO BE COMPLEXITY OF O( N )

		if ( size() == 0 )
		{
			Node<T> node = new Node<>( item, head, tail, head, tail );
			tail.prev = node;
			tail.prevSorted = node;
			head.next = node;
			head.nextSorted = node;

			theSize++;
		}
		else
		{
			// used to iterate over the sorted list to add the node
			// to the correct placement
			Node<T> tmp = head.nextSorted;

			// used for adding at the end of the list
			Node<T> current = tail.prev;

			while ( tmp != tail )
			{
				// compared item with tmps data to see if it 
				// greater or less then
				if ( item.compareTo( tmp.data ) <= -1 )
				{
					Node<T> node = new Node<>( item, current, tail, 
							tmp.prevSorted , tmp );
					// fixing the other connections for the unsorted list
					tail.prev = node;
					current.next = node;

					// fixing the other connections for the sorted list
					tmp.prevSorted.nextSorted = node;
					tmp.prevSorted = node;

					// add more to the size
					theSize++;
					return true;

				}

				// move the node to next one
				tmp = tmp.nextSorted;


			}
			// went through entire list have not found any item below so will
			// add the information to the end of the list
			Node<T> node = new Node<>( item, current, tail, 
					tail.prevSorted , tail );
			// fixing the other connections for the unsorted list
			tail.prev = node;
			current.next = node;

			// fixing the other connections for the sorted list
			tail.prevSorted.nextSorted = node;
			tail.prevSorted = node;

			// add more to the size
			theSize++;
		}

		return true;

	}

	@Override
	public T removeFirst() {

		// NEEDS TO BE COMPLEXITY OF O( 1 )

		Node<T> current = head.next;
		T item = current.data;

		// remove from the first from insertion list
		head.next = current.next;
		current.next.prev = head;
		current.next = null;
		current.prev = null;

		// remove from sorted
		current.prevSorted.nextSorted = current.nextSorted;
		current.nextSorted.prevSorted = current.prevSorted;
		current.prevSorted = null;
		current.nextSorted = null;

		// allow garbage collector to get this unused node
		current = null;

		theSize--;

		return item;
	}

	@Override
	public T removeFirstSorted() {

		// NEEDS TO BE A COMPLEXITY OF O( 1 )
		Node<T> current = head.nextSorted;

		T item = current.data;

		// remove first from sorted list
		head.nextSorted = current.nextSorted;
		current.nextSorted.prevSorted = head;
		current.nextSorted = null;
		current.prevSorted = null;

		// remove from insertion list
		current.prev.next = current.next;
		current.next.prev = current.prev;
		current.next = null;
		current.prev = null;

		// allow garbage collector to get this unused node
		current = null;

		theSize--;

		return item;
	}

	@Override
	public T removeLast() {

		// NEEDS TO BE COMPLEXITY OF O( 1 )

		Node<T> current = tail.prev;
		T item = current.data;

		// remove last from insertion list
		tail.prev = current.prev;
		current.prev.next = tail;
		current.next = null;
		current.prev = null;

		// remove item from sorted list
		current.prevSorted.nextSorted = current.nextSorted;
		current.nextSorted.prevSorted = current.prevSorted;
		current.nextSorted = null;
		current.prevSorted = null;

		// allow garbage collector to get this unused node
		current = null;

		theSize--;

		return item;
	}

	@Override
	public T removeLastSorted() {

		// NEEDS TO BE COMPLEXITY OF O( 1 )

		Node<T> current = tail.prevSorted;
		T item = current.data;

		//remove from the insertion list
		current.prev.next = current.next;
		current.next.prev = current.prev;
		current.next = null;
		current.prev = null;


		// removing the last from sorted list
		tail.prevSorted = current.prevSorted;
		current.prevSorted.nextSorted = tail;
		current.nextSorted = null;
		current.prevSorted = null;

		// allow garbage collector to get this unused node
		current = null;

		theSize--;

		return item;
	}

	@Override
	public int contains(T item) {

		// NEEDS TO BE COMPLEXITY OF O( N )

		Node<T> current = head.next;
		int counter = 0;

		while( current != tail )
		{
			if( current.data.equals( item ) )
			{
				counter++;
			}
			current = current.next;
		}

		return counter;
	}

	@Override
	public Iterator<T> iterator() {
		// NEEDS TO BE COMPLEXITY OF O( 1 )

		return new MultiLinkedListIterartor() ;
	}

	/**
	 * Program Description: Will be the private class for the Iterator<T>
	 * and will be used for the insertion ordered list;
	 *
	 * Date Last Modified: Oct 7, 2015
	 *
	 * @author: kalaarentz
	 */
	private class MultiLinkedListIterartor implements Iterator<T>
	{
		private Node<T> current = head.next;

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
		public T next() {

			if( !hasNext() )
			{
				throw new NoSuchElementException( "Out of Elements" );
			}

			T item = current.data;
			current = current.next;

			return item;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() 
		{
			// removing item from list to add the a new element to list
			// to keep the insertion and sorted order still correct
			current.prevSorted.nextSorted = current.nextSorted;
			current.nextSorted.prevSorted = current.prevSorted;
			
			current.prev.next = current.next;
			current.next.prev = current.prev;
			
		}

	}

	@Override
	public Iterator<T> sortedIterator() {

		return new MultiLinkedListSortedIterator();
	}

	/**
	 * Program Description: Will be the private class for the sortedIterator<T>
	 * and will be used for the sorted list;
	 *
	 * Date Last Modified: Oct 7, 2015
	 *
	 * @author: kalaarentz
	 */
	private class MultiLinkedListSortedIterator implements Iterator<T>
	{
		private Node<T> current = head.nextSorted;

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
		public T next() {

			if( !hasNext() )
			{
				throw new NoSuchElementException( "Out of Elements" );
			}

			T item = current.data;
			current = current.nextSorted;

			return item;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() 
		{
			// removing item from list to add the a new element to list
						// to keep the insertion and sorted order still correct
						current.prevSorted.nextSorted = current.nextSorted;
						current.nextSorted.prevSorted = current.prevSorted;
						
						current.prev.next = current.next;
						current.next.prev = current.prev;
						
		}

	}

	@Override
	public ListIterator<T> unsortedListIterator() {

		return new MultiLinkedListUnsortedIterator();
	}

	@Override
	public ListIterator<T> unsortedListIterator(int index) {

		return new MultiLinkedListUnsortedIterator( index );
	}

	/**
	 * 
	 * Program Description: ListIterator for the unsorted part of 
	 * linked list, will have two different constructors for the 
	 * indexed that may be passed through 
	 *
	 * Date Last Modified: Oct 19, 2015
	 *
	 * @author: kalaarentz
	 */
	private class MultiLinkedListUnsortedIterator implements ListIterator<T>
	{
		// keep track of the idx for what node you are on
		private int idx;
		private Node<T> current;

		/**
		 *  constructor for unsorted list
		 */
		public MultiLinkedListUnsortedIterator()
		{
			current = head.next;
		}

		/**
		 * constructor for sorted list
		 * @param index
		 */
		public MultiLinkedListUnsortedIterator( int index )
		{
			current = head.next;
			idx = index;
			for( int i = 0; i < idx; i++ )
			{
				current = current.next;
			}

		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#hasNext()
		 */
		@Override
		public boolean hasNext() {

			return current != tail;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#next()
		 */
		@Override
		public T next() {

			if( !hasNext() )
			{
				throw new NoSuchElementException( "Out of Elements" );
			}
			T item = current.data;
			current = current.next;

			idx++;

			return item;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#hasPrevious()
		 */
		@Override
		public boolean hasPrevious() {

			return current != head;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#previous()
		 */
		@Override
		public T previous() {

			if( !hasPrevious() )
			{
				throw new NoSuchElementException( "Out of Elements" );
			}

			T item = current.data;
			current = current.prev;

			idx--;

			return item;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#nextIndex()
		 */
		@Override
		public int nextIndex() {

			return idx + 1;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#previousIndex()
		 */
		@Override
		public int previousIndex() {

			return idx - 1;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#remove()
		 */
		@Override
		public void remove() {
			// removing item from list to add the a new element to list
						// to keep the insertion and sorted order still correct
						current.prevSorted.nextSorted = current.nextSorted;
						current.nextSorted.prevSorted = current.prevSorted;
						
						current.prev.next = current.next;
						current.next.prev = current.prev;
						
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#set(java.lang.Object)
		 */
		@Override
		public void set(T e) {

			// removing item from list to add the a new element to list
			// to keep the insertion and sorted order still correct
			current.prevSorted.nextSorted = current.nextSorted;
			current.nextSorted.prevSorted = current.prevSorted;

			current.prev.next = current.next;
			current.next.prev = current.prev;

			add( e );

		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#add(java.lang.Object)
		 */
		@Override
		public void add(T e) {
			add( e );
		}

	}

	@Override
	public ListIterator<T> sortedListIterator() {

		return new MultiLinkedListSortedIterator1();
	}

	@Override
	public ListIterator<T> sortedListIterator(int index) {

		// NEEDS TO BE COMPLEXITY O( N ) BUT REALLY O( N/2 )

		return new MultiLinkedListSortedIterator1( index );
	}

	private class MultiLinkedListSortedIterator1 implements ListIterator<T>
	{
		// keep track of the idx for what node you are on
		private int idx;
		private Node<T> current;

		/**
		 *  constructor for unsorted list
		 */
		public MultiLinkedListSortedIterator1()
		{
			current = head.nextSorted;
		}

		/**
		 * constructor for sorted list
		 * @param index
		 */
		public MultiLinkedListSortedIterator1( int index )
		{
			current = head.nextSorted;
			idx = index;
			for( int i = 0; i < idx; i++ )
			{
				current = current.nextSorted;
			}

		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#hasNext()
		 */
		@Override
		public boolean hasNext() {

			return current != tail;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#next()
		 */
		@Override
		public T next() {

			if( !hasNext() )
			{
				throw new NoSuchElementException( "Out of Elements" );
			}
			T item = current.data;
			current = current.nextSorted;
			
			idx++;

			return item;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#hasPrevious()
		 */
		@Override
		public boolean hasPrevious() {

			return current != head;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#previous()
		 */
		@Override
		public T previous() {

			if( !hasPrevious() )
			{
				throw new NoSuchElementException( "Out of Elements" );
			}

			T item = current.data;
			current = current.prevSorted;

			idx--;

			return item;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#nextIndex()
		 */
		@Override
		public int nextIndex() {

			return idx + 1;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#previousIndex()
		 */
		@Override
		public int previousIndex() {

			return idx - 1;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#remove()
		 */
		@Override
		public void remove() {
			// removing item from list to add the a new element to list
			// to keep the insertion and sorted order still correct
			current.prevSorted.nextSorted = current.nextSorted;
			current.nextSorted.prevSorted = current.prevSorted;

			current.prev.next = current.next;
			current.next.prev = current.prev;

		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#set(java.lang.Object)
		 */
		@Override
		public void set(T e) {

			// removing item from list to add the a new element to list
			// to keep the insertion and sorted order still correct
			current.prevSorted.nextSorted = current.nextSorted;
			current.nextSorted.prevSorted = current.prevSorted;

			current.prev.next = current.next;
			current.next.prev = current.prev;

			add( e );

		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#add(java.lang.Object)
		 */
		@Override
		public void add(T e) {
			add( e );
		}

	}

	/**
	 * This is the doubly-linked list node.
	 * Two sets of links are provided.
	 * One for the insertion-order and the other for sorted-order.
	 */
	private static class Node<T> {
		private T data;
		private Node<T> prev;
		private Node<T> next;
		private Node<T> prevSorted;
		private Node<T> nextSorted;

		/**
		 * Constructs basic list node.
		 * 
		 * @param d The data element held by the list node.
		 * @param p A Node that occurs immediately before this in list insertion order.
		 * @param n A node that occurs immediately after this in list insertion order.
		 * @param ps A Node that occurs immediately before this in sorted order.
		 * @param ns A Node that occurs immediately after this in sorted order.
		 */
		private Node(T d, Node<T> p, Node<T> n, Node<T> ps, Node<T> ns) {
			data = d;
			prev = p;
			next = n;
			prevSorted = ps;
			nextSorted = ns;
		}
	}
}
