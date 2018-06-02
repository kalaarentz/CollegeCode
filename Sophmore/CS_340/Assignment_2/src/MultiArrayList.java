import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;


public class MultiArrayList<T extends Comparable<T>> implements MultiList<T> {

	private int DEFAULT_CAPACITY = 5;
	private int theSize;
	private T[] unsortedArray;
	private T[] sortedArray;

	/**
	 *  Constructor for the MultiArrayList that will
	 *  use the clear() method to instantiate the array 
	 *  for the unsorted and sorted array
	 */
	public MultiArrayList()
	{
		clear();
	}

	@Override
	public void clear() {
		theSize = 0;
		ensureCapacity( DEFAULT_CAPACITY );
	}

	@Override
	public int size() {

		return theSize;
	}

	/**
	 * Will be the overridden method for the toString;
	 * will iterate through the unsorted Array to make a object of all the 
	 * information in the string
	 * 
	 * @param str String will all the items in it [item, item]
	 */
	public String toString()
	{
		String str = "[";

		for( int i = 0; i < theSize; i++ )
		{
			if( i != theSize - 1  )
			{
				str += unsortedArray[ i ] + ", ";
			}
			else
			{
				str += unsortedArray[ i ];
			}
		}

		return str + "]";

	}

	@Override
	public String toStringSorted() {

		String str = "[";

		for ( int i = 0; i < theSize; i++ )
		{
			if( i != theSize - 1  )
			{
				str += sortedArray[ i ] + ", ";
			}
			else
			{
				str += sortedArray[ i ];
			}
		}

		return str + "]";
	}

	@Override
	public boolean add(T item) {

		// make the array large if you must
		if ( theSize == unsortedArray.length )
		{
			ensureCapacity( theSize * 2 + 1 );
		}

		// add 
		if ( theSize == 0 )
		{
			unsortedArray[ theSize ] = item;
			sortedArray[ theSize ] = item;

		}
		else
		{
			// keep track of index of the element that is less then
			int idx = -1;
			int k = -1;
			unsortedArray[ theSize ] = item;

			// adding the element to the end of the unsortedArray
			//unsortedArray[ theSize ] = item;

			// searching for which idx is the item less then
			for ( int i = 0; i < theSize; i++ )
			{
				if ( item.compareTo( sortedArray[ i ] ) < 0 )
				{
					//System.out.println("DEBUGG: first loop ");
					// to make sure it only keeps the index for the
					// very first object that is less then that element
					if ( k == - 1 )
					{
						idx = i;
						k = 0;
					}

				}
			}

			// if idx = 0 then the item is not less then any object in the array
			// meaning we will add to the end of the array
			// if idx != 0 then you need to shift all the elements before that idx
			// to get to add to the correct idx

			if ( idx == -1 )
			{
				sortedArray[ theSize ] = item;
			}
			else
			{
				for ( int j = theSize; j > idx; j-- )
				{
					//System.out.println("DEBUGG: second loop ");

					sortedArray[ j ] = sortedArray[ j - 1 ];
				}
				sortedArray[ idx ] = item;
			}

		}

		theSize++;

		return true;
	}

	@Override
	public T removeFirst() {

		// make a temporary item to hold item that needs to be searched for
		T tmp = unsortedArray[ 0 ];
		int idx = placementOfItemInOtherArray( sortedArray, tmp );

		// moving elements forward 
		movingOverElements( unsortedArray, 0 );

		// will be used for moving elements over in array that has removed
		// in the middle
		movingOverElements( sortedArray, idx );

		theSize--;

		return tmp;
	}

	@Override
	public T removeFirstSorted() {

		// make a temporary item to hold item that needs to be searched for
		T tmp = sortedArray[ 0 ];
		int idx = placementOfItemInOtherArray( unsortedArray, tmp );

		// moving elements forward 
		movingOverElements( sortedArray, 0);

		// will be used for moving elements over in array that has removed
		// in the middle
		movingOverElements( unsortedArray, idx );

		theSize--;

		return tmp;
	}

	@Override
	public T removeLast() {

		// temporary variable of the item in the array
		T tmp = unsortedArray[ theSize - 1 ];

		// index of the item in the other array
		int idx = placementOfItemInOtherArray( sortedArray, tmp );

		System.out.println( "index: " + idx );
		// moving the elements over with the specific element removed
		movingOverElements( sortedArray, idx );

		// make the last element null 
		unsortedArray[ theSize - 1 ] = null;

		// decrease size
		theSize--;

		return tmp;
	}

	@Override
	public T removeLastSorted() {

		// temporary variable of the item in the array
		T tmp = sortedArray[ theSize - 1 ];

		// index of the item in the other array
		int idx = placementOfItemInOtherArray( unsortedArray, tmp );

		// moving the elements over with the specific element removed
		movingOverElements( unsortedArray, idx );

		// make the last element null 
		sortedArray[ theSize - 1 ] = null;

		// decrease size
		theSize--;
		return null;
	}

	@Override
	public int contains(T item) {

		int count = 0;

		for( int i = 0; i < theSize; i++ )
		{
			if( unsortedArray[ i ].equals( item ) )
			{
				count++;
			}
		}
		return count;
	}

	@Override
	public Iterator<T> iterator() {
		// return iterator for the unsortedArray
		return new MultiArrayListIterator();
	}

	/**
	 * 
	 * Program Description: This is the private iterator to be used for
	 * the multiarraylist. It will be using the unsortedArray. 
	 *
	 * Date Last Modified: Oct 13, 2015
	 *
	 * @author: kalaarentz
	 */
	private class MultiArrayListIterator implements Iterator<T>
	{

		private int current;

		public MultiArrayListIterator()
		{
			current = 0;
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {

			return current < size();
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		@Override
		public T next() {

			if ( !hasNext() )
			{
				throw new NoSuchElementException( "Out of Elements" );
			}

			T tmp = sortedArray[ current ];
			current++;

			return tmp;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove()
		{
			T item = sortedArray[ current ];

			// removing the specific item from both arrays
			movingOverElements( unsortedArray, 
					placementOfItemInOtherArray( unsortedArray, item ) );
			movingOverElements( sortedArray, 
					placementOfItemInOtherArray( sortedArray, item ) );

		}

	}
	@Override
	public Iterator<T> sortedIterator() {

		return new MultiArrayListSortedIterator();
	}

	/**
	 * 
	 * Program Description: Array iterator for the sorted iterator version
	 * of the array
	 *
	 * Date Last Modified: Oct 16, 2015
	 *
	 * @author: kalaarentz
	 */
	private class MultiArrayListSortedIterator implements Iterator<T>
	{

		private int current;

		public MultiArrayListSortedIterator()
		{
			current = 0;
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {

			return current < size();
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		@Override
		public T next() {

			if ( !hasNext() )
			{
				throw new NoSuchElementException( "Out Of Elments" );
			}

			T tmp = sortedArray[ current ];
			current++;

			return tmp;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove()
		{
			T item = sortedArray[ current ];

			// removing the specific item from both arrays
			movingOverElements( unsortedArray, 
					placementOfItemInOtherArray( unsortedArray, item ) );
			movingOverElements( sortedArray, 
					placementOfItemInOtherArray( sortedArray, item ) );
		}

	}

	@Override
	public ListIterator<T> unsortedListIterator() {

		return new MultiArrayUnsortedListIterator( );
	}

	@Override
	public ListIterator<T> unsortedListIterator(int index) {

		return new MultiArrayUnsortedListIterator( index );
	}

	/**
	 * 
	 * Program Description: Will iterate through the unsorted array
	 * will have two different constructors for one when you start 
	 * from a specific index
	 *
	 * Date Last Modified: Oct 19, 2015
	 *
	 * @author: kalaarentz
	 */
	private class MultiArrayUnsortedListIterator implements ListIterator<T>
	{
		private int current;

		/**
		 * Constructor for the unsorted array list that 
		 * starts at the first index of the array.
		 */
		public MultiArrayUnsortedListIterator()
		{
			current = 0;
		}

		/**
		 * Constructor for the unsorted array list taht starts
		 * at a specific index
		 * 
		 * @param index int index of where the iterator starts at 
		 */
		public MultiArrayUnsortedListIterator( int index )
		{
			current = 0;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#hasNext()
		 */
		@Override
		public boolean hasNext() {

			return current < theSize;
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

			T item = unsortedArray[ current ];
			current++;

			return item;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#hasPrevious()
		 */
		@Override
		public boolean hasPrevious() {

			return current >= 0;
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

			T item = unsortedArray[ current ];
			current--;

			return item;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#nextIndex()
		 */
		@Override
		public int nextIndex() {

			return current + 1;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#previousIndex()
		 */
		@Override
		public int previousIndex() {

			return current - 1;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#remove()
		 */
		@Override
		public void remove() {

			T item = sortedArray[ current ];

			// removing the specific item from both arrays
			movingOverElements( unsortedArray, 
					placementOfItemInOtherArray( unsortedArray, item ) );
			movingOverElements( sortedArray, 
					placementOfItemInOtherArray( sortedArray, item ) );

		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#set(java.lang.Object)
		 */
		@Override
		public void set(T e) {

			T item = unsortedArray[ current ];

			movingOverElements( unsortedArray, 
					placementOfItemInOtherArray( unsortedArray, item ) );
			movingOverElements( sortedArray, 
					placementOfItemInOtherArray( sortedArray, item ) );

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

		return new MultiArraySortedListIterator();
	}

	@Override
	public ListIterator<T> sortedListIterator(int index) {

		return new MultiArraySortedListIterator( index );
	}

	/**
	 * 
	 * Program Description: Will iterate through the sorted array
	 * will have two different constructors for one when you start 
	 * from a specific index
	 *
	 * Date Last Modified: Oct 19, 2015
	 *
	 * @author: kalaarentz
	 */
	private class MultiArraySortedListIterator implements ListIterator<T>
	{
		private int current;

		/**
		 * Constructor of the sortedArray that does not require
		 * a specific index
		 */
		public MultiArraySortedListIterator()
		{
			current = 0;
		}

		/**
		 * Constructor of the sortedArray that does require a
		 * specific index to iterate from
		 * 
		 * @param index int index to iterate from in the array
		 */
		public MultiArraySortedListIterator( int index )
		{
			current = index;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#hasNext()
		 */
		@Override
		public boolean hasNext() {

			return current < theSize;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#next()
		 */
		@Override
		public T next() {

			if ( !hasNext() )
			{
				throw new NoSuchElementException( "Out of Elements" );
			}

			T item = sortedArray[ current ];
			current++;

			return item;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#hasPrevious()
		 */
		@Override
		public boolean hasPrevious() {

			return current >= 0;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#previous()
		 */
		@Override
		public T previous() {

			if (!hasPrevious() )
			{
				throw new NoSuchElementException( "Out of Elements" );
			}

			T item = sortedArray[ current ];
			current--;

			return item;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#nextIndex()
		 */
		@Override
		public int nextIndex() {

			return current + 1;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#previousIndex()
		 */
		@Override
		public int previousIndex() {

			return current - 1;
		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#remove()
		 */
		@Override
		public void remove() {

			T item = sortedArray[ current ];

			// removing the specific item from both arrays
			movingOverElements( unsortedArray, 
					placementOfItemInOtherArray( unsortedArray, item ) );
			movingOverElements( sortedArray, 
					placementOfItemInOtherArray( sortedArray, item ) );

		}

		/* (non-Javadoc)
		 * @see java.util.ListIterator#set(java.lang.Object)
		 */
		@Override
		public void set(T e) {
			T item = sortedArray[ current ];

			// removing the specific item from both arrays
			movingOverElements( unsortedArray, 
					placementOfItemInOtherArray( unsortedArray, item ) );
			movingOverElements( sortedArray, 
					placementOfItemInOtherArray( sortedArray, item ) );

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
	 * Will copy the old arrays, both the unsortedArray and the sortedArray
	 * and will make a larger array
	 * 
	 * @param newCapacity integer that will be new size of the arrays
	 */
	@SuppressWarnings("unchecked")
	private void ensureCapacity( int newCapacity )
	{
		if ( newCapacity < theSize )
		{
			return;
		}

		// copy old elements to new array
		T[] oldUnsorted = unsortedArray;
		T[] oldSorted = sortedArray;

		unsortedArray = (T[]) new Comparable[ newCapacity ];
		sortedArray = (T[]) new Comparable[ newCapacity ];

		for ( int i = 0; i < theSize; i++ )
		{
			unsortedArray[ i ] = oldUnsorted[ i ];
			sortedArray[ i ] = oldSorted[ i ];
		}
	}

	/**
	 * Will be used to find the item that is removed in the 
	 * other array
	 * 
	 * @param array either the unsortedArray or sortedArray
	 * @param item that you need to look for
	 * @return idx of that element in that array
	 */
	private int placementOfItemInOtherArray( T[] array, T item )
	{
		int idx = 0;

		for( int i = 0; i < theSize; i++ )
		{
			if( array[ i ].equals( item ) )
			{
				idx = i;
			}
		}

		return  idx;
	}


	/**
	 * Will be moving elements over from the right of the index
	 * over to one index to the left
	 * 
	 * @param array either unsortedArray or sortedArray
	 * @param idx int of the index of element in the array
	 */
	private void movingOverElements( T[] array, int idx )
	{
		int k = idx;
		while( idx < theSize )
		{
			array[ idx ] = array[ k + 1];
			idx++;
			k++;
		}

	}

}
