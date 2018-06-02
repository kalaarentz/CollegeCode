import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 * Binary Search Tree Implementation
 * 
 * @author J. Hursey, M. Allen, Mark Allen Weiss
 *
 * @param <T> Type to store in the tree
 */
public class BinarySearchTree<T extends Comparable<? super T>> {
	// The tree root.
	private BinaryNode<T> root;

	/**
	 * Construct the tree with null root.
	 */
	public BinarySearchTree() {
		root = null;
	}

	/**
	 * Make the tree logically empty.
	 */
	public void makeEmpty() {
		root = null;
	}

	/**
	 * Test if the tree is logically empty.
	 *
	 * @return true if and only if empty.
	 */
	public boolean isEmpty() {
		return root == null;
	}


	/**
	 * Insert into the tree; duplicates are ignored.
	 *
	 * @param x the item to insert.
	 */
	public void insert(T x) {
		root = insert(x, root);
	}

	/**
	 * Return a pre-order traversal of the string.
	 * Child nodes are surrounded by parenthesis.
	 * If the tree is empty then the empty string ("") is returned.
	 * Below is a properly formatted tree rooted at 50
	 *  "50 (25 (8, 30 (49 (48 (39 (47 (43) ) ) , 75 (70, 90) )"
	 *   
	 * @return Properly formatted string
	 */
	public String toPrefixString( ) {

		// Use string builder since you don't know how large the tree will be
		String str = "";
		StringBuilder sb = new StringBuilder( str );

		// if the tree is empty return ("")
		if( isEmpty() )
		{
			sb.append( "(\"\")" );
		}

		printTreePreOrder( root, sb );

		return sb.toString();
	}

	/**
	 * Write the graph in the graphviz Dot format as a 'digraph'
	 * (See assignment specification for details on file format.
	 * 
	 * @param filename The filename to save as
	 * 
	 */
	public void writeDotFile( String filename ) {

		try {

			PrintWriter file = new PrintWriter( filename );
			file.write("digraph binarySearchTree {" );
			file.println();
			file.write( "//List of Nodes" );
			file.println();
			listOfNodesDotFile( root, "n", 0, file );

			file.write( "//List of connections between pairs of nodes" );
			file.println();
			connectionBetweenNodesDotFile( root, "n", 0, file );
			file.write("}");

			file.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

	}

	/*
	 * Internal method to insert into a subtree.
	 *
	 * @param x Item to insert.
	 * @param t Node that roots the subtree.
	 * @return New root of the subtree.
	 */
	private BinaryNode<T> insert(T x, BinaryNode<T> t) {
		if ( t == null ) {
			return new BinaryNode<T>(x, null, null);
		}

		int compareResult = x.compareTo(t.element);

		if ( compareResult < 0 ) {
			t.left = insert(x, t.left);
		}
		else if ( compareResult > 0 ) {
			t.right = insert(x, t.right);
		}

		// If here, then x is a duplicate item;
		// simply return original tree
		return t;
	}


	/**
	 * Remove from the tree. Nothing is done if x is not found.
	 *
	 * @param x the item to remove.
	 */
	public void remove(T x) {
		root = remove(x, root);
	}

	/*
	 * Internal method to remove from a subtree.
	 *
	 * @param x Item to remove.
	 * @param t Node that roots a subtree.
	 * @return New root of the subtree.
	 */
	private BinaryNode<T> remove(T x, BinaryNode<T> t) {
		// if item not found, return null
		if ( t == null ) {
			return t;
		}

		int compareResult = x.compareTo(t.element);

		if ( compareResult < 0 ) {
			// element x is less than subtree root, so
			// recursively remove x on the left side
			t.left = remove(x, t.left);
		}
		else if ( compareResult > 0 ) {
			// element x is greater than subtree root, so
			// recursively remove x on the right side
			t.right = remove(x, t.right);
		}
		else if ( t.left != null && t.right != null ) {
			// root element equals x, root has 2 children
			t.element = findMin(t.right).element;
			t.right = remove(t.element, t.right);
		}
		else if ( t.left != null ) {
			// root element equals x, has only left child
			t = t.left;
		}
		else {
			// root element equals x, has only right child
			t = t.right;
		}

		// return subtree root (t) when done
		return t;
	}


	/**
	 * Find the least item in the tree.
	 *
	 * @throws UnderflowException if isEmpty() == true.
	 * @return Least item in tree.
	 */
	public T findMin() {
		if ( isEmpty() ) {
			throw new UnderflowException();
		}

		return findMin(root).element;
	}

	/*
	 * Internal method to find the least item in a subtree.
	 *
	 * @param t Node that roots the subtree.
	 * @return Node containing the least item.
	 */
	private BinaryNode<T> findMin(BinaryNode<T> t) {
		if ( t == null ) {
			return null;
		}
		else if ( t.left == null ) {
			return t;
		}

		return findMin(t.left);
	}


	/**
	 * Find the maximum item in the tree.
	 *
	 * @throws UnderflowException if isEmpty() == true.
	 * @return Maximum item in tree.
	 */
	public T findMax() {
		if ( isEmpty() ) {
			throw new UnderflowException();
		}

		return findMax(root).element;
	}

	/*
	 * Internal method to find the maximum item in a subtree.
	 *
	 * @param t Node that roots the subtree.
	 * @return Node containing the maximum item.
	 */
	private BinaryNode<T> findMax(BinaryNode<T> t) {
		if ( t != null ) {
			while( t.right != null ) {
				t = t.right;
			}
		}

		return t;
	}


	/**
	 * Find an item in the tree.
	 *
	 * @param x Item to search for.
	 * @return true if and only if item found.
	 */
	public boolean contains(T x) {
		return contains(x, root);
	}

	/*
	 * Internal method to find an item in a subtree.
	 *
	 * @param x Item to search for.
	 * @param t Node that roots the subtree.
	 * @return true if and only if x is contained in subtree rooted at t.
	 */
	private boolean contains(T x, BinaryNode<T> t) {
		if ( t == null ) {
			return false;
		}

		int compareResult = x.compareTo(t.element);

		if ( compareResult < 0 ) {
			return contains(x, t.left);
		}
		else if ( compareResult > 0 ) {
			return contains(x, t.right);
		}
		else {
			return true;
		}
	}

	/*
	 * Private static class for tree nodes.
	 * Each contains a data element, and
	 * links to right- and left-subtrees.
	 */
	private static class BinaryNode<T> {
		private T element;
		private BinaryNode<T> left;
		private BinaryNode<T> right;

		private BinaryNode(T theElement, BinaryNode<T> lt, BinaryNode<T> rt) {
			element = theElement;
			left = lt;
			right = rt;
		}
	}

	/**
	 * Exception class for access in empty containers
	 * such as stacks, queues, and priority queues.
	 *
	 * @author Mark Allen Weiss
	 */
	@SuppressWarnings("serial")
	private static class UnderflowException extends RuntimeException {
	}

	/**
	 * Recursive method of printing out the tree that
	 * is just the binary
	 * 
	 * @param t BinaryNode<T> can be either left or right or root
	 * @param builder StringBuilder so you can append all the elements needed
	 */
	private void printTreePreOrder( BinaryNode<T> t, StringBuilder builder )
	{
		if ( t != null )
		{
			// print parent of string
			builder.append( t.element );

			// only print this if the root is a parent 
			if ( t.left != null || t.right != null )
			{
				builder.append( " (" );
			}

			// print left sub tree 
			printTreePreOrder( t.left , builder );

			if ( t.left != null && t.right != null )
			{
				builder.append(", ");
			}

			// print right sub tree
			printTreePreOrder( t. right, builder );

			if ( t.left != null || t.right != null )
			{
				builder.append( ") " );
			}

		}
	}

	/**
	 * Recursive method for printing to the dot file for the different list of 
	 * nodes and the index of each of those nodes
	 * 
	 * It will be written like this 
	 * n_0 [label="50"]; ( this is the example of the root)
	 * 
	 * @param t the BinaryNode that you will reading starts with root
	 * @param prefix String will be a list of numbers to know how far into tree
	 * you are
	 * @param id int 0 will represent the left branch and 1 will represent
	 *  the right branch
	 * @param file PrintWriter to pass through so you can print to the dot file
	 * 
	 */
	private void listOfNodesDotFile( BinaryNode<T> t, String prefix, 
			int id, PrintWriter file )
	{
		if ( t != null )
		{
			// print to file for the information for the specific node
			prefix = prefix + "_" + id; 
			String str = prefix + "[label=\"" + t.element + "\"];";
			
			file.write( str );
			file.println();

			// print to file for the left of the tree
			listOfNodesDotFile( t.left, prefix, 0, file );

			// if one of the trees does not have a both children
			// this is if the left child is null and right is not
			if ( t.left == null && t.right != null )
			{
				str = prefix + "_" + 0 + "[label=\"\",style=invis];";
				file.write( str );
				file.println();
			}

			// print to file for the right of the tree
			listOfNodesDotFile( t.right, prefix, 1, file );

			// if one of the trees does not have a both children
			// this is if the right child is null and the left is not 
			if ( t.left != null && t.right == null )
			{
				str = prefix + "_" + 1 + "[label=\"\",style=invis];";
				file.write( str );
				file.println();
			}
		}
		else
		{
			String str = "";
			str = prefix + "_" + id + "[label=\"\",style=invis];";
			file.write( str );
			file.println();
		}
	}

	/**
	 * Method that will make a String for the left and right child
	 * of the parent.
	 *  
	 * @param prefix String that will be added to the prefix 
	 * @param id 1 for the right subtree and 0 for the left subtree
	 * @return str String that will be returned that will be the complete
	 * list of all the nodes for different levels
	 */
	private String childrenStringNotation( String prefix, int id )
	{
		prefix = prefix + "_" + id;
		String str = prefix;

		return str;
	}

	/**
	 * Recursive method for writing to the dot file for the connection
	 * between the tree nodes
	 * 
	 * @param t BinaryNode<T> will start with the root
	 * @param prefix String that will have an "n" and latter 
	 * added with "_"
	 * @param id 1 for the right subtree and 0 for the left subtree
	 * @param file Print
	 */
	private void connectionBetweenNodesDotFile( BinaryNode<T> t , 
			String prefix, int id, PrintWriter file )
	{
		if ( t != null )
		{
			prefix = prefix + "_" + id;

			// Have this so it stops before the children are null, need to have
			// grey invisible color
			
			if ( t.left != null && t.right == null )
			{
				// this is for when one parent does not have both children
				// has a left child but not a right one
				file.write( prefix + " -> " 
						+ childrenStringNotation( prefix, 0 ) +";" );
				file.println();
				file.write( prefix + " -> " 
						+ childrenStringNotation( prefix, 1 ) 
						+ " [style=dotted, color=lightgray] ;" );
				file.println();
			}
			else if ( t.left == null && t.right != null )
			{
				// this is for when one parent does not have both children
				// has a right child but not a left one
				file.write( prefix + " -> " 
						+ childrenStringNotation( prefix, 0 ) 
						+ " [style=dotted, color=lightgray] ;" );
				file.println();
				file.write( prefix + " -> " 
						+ childrenStringNotation( prefix, 1 ) + ";" );
				file.println();
			}
			else if ( t.left != null || t.right != null )
			{
				
				file.write( prefix + " -> " 
						+ childrenStringNotation( prefix, 0 ) +";" );
				file.println();
				file.write( prefix + " -> " 
						+ childrenStringNotation( prefix, 1 ) + ";" );
				file.println();
			}
			else 
			{
				// this is for if both of the children are null 
				file.write( prefix + " -> " 
						+ childrenStringNotation( prefix, 0 ) 
						+ " [style=dotted, color=lightgray] ;" );
				file.println();
				file.write( prefix + " -> " 
						+ childrenStringNotation( prefix, 1 ) 
						+ " [style=dotted, color=lightgray] ;" );
				file.println();
			}
			
			connectionBetweenNodesDotFile( t.left, prefix, 0, file );

			connectionBetweenNodesDotFile( t.right, prefix, 1, file );

		}
		
	}

}
