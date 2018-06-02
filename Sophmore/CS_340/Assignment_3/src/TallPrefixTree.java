import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * 
 * Program Description: The class for TallPrefixTree that will
 * implement PrefixTree interface. The private class TreeNode
 * will use the StringKVList class.
 *
 * Date Last Modified: Nov 2, 2015
 *
 * @author: kalaarentz
 */
public class TallPrefixTree implements PrefixTree {

	private TreeNode root;

	public TallPrefixTree() {

		root = new TreeNode( "" );
		root.letter = "";
	}

	@Override
	public void insert(String s) {

		insert( root, s );
	}

	/**
	 * Will be used to insert the word into the TallPrefixTree
	 * 
	 * @param node TreeNode that will be traversed 
	 * @param str String that will be added to the TallPrefixTree 
	 */
	private void insert( TreeNode node, String str )
	{
		if ( str.length() == 0 )
		{
			if ( ! node.children.contains(""))
			{
				node.children.insert( "", new TreeNode( "" ) );
			}

		}
		else if ( str.length() == 1 )
		{
			if ( !node.children.contains( str ) )
			{
				TreeNode tmp = new TreeNode( str );
				node.children.insert( str, tmp);
				insert(tmp, "" );
			}
			else
			{
				insert( node.children.get( str ) , "" );
			}

		}
		else
		{
			if ( !node.children.contains( str.substring(0, 1) ) )
			{
				TreeNode tmp = new TreeNode( str.substring( 0, 1 ) );
				node.children.insert( str.substring( 0, 1 ), tmp);
				insert( tmp, str.substring( 1 ) );
			}
			else
			{
				// more then one children and it does not contain it or 
				// for the instance of the children being empty

				insert( node.children.get( str.substring( 0,1 ) ), str.substring( 1 ) );

			}
		}

	}

	@Override
	public LinkedList<String> getMatches(String prefix) {

		LinkedList<String> words = new LinkedList<>();
		TreeNode current = root;
		try
		{
			for ( int i = 0; i < prefix.length(); i++ )
			{
				current = current.children.get( prefix.substring( i, i+1 ) );
			}
		}
		catch( NoSuchElementException e )
		{
			return words;
		}
		
		words.addAll( getMatches( current, prefix ) );
		
		return words;
	}

	/**
	 * Will be used to get the list of words for a specific prefix 
	 * @param node TreeNode to traverse through
	 * @param prefix String if word is in the list
	 * @return words LinkedList<String> that is a list of all the words 
	 */
	private LinkedList<String> getMatches( TreeNode node, String prefix  )
	{
		LinkedList<String> words = new LinkedList<>();

		if ( node.children.contains( "" ) )
		{
			words.add( prefix );
		}

		for ( TreeNode n : node.children )
		{
			if ( ! n.letter.equals( "" ) )
			{
				words.addAll( getMatches( n, prefix + n.letter ) );
			}
		}

		return words;
	}

	@Override
	public String toPrefixString() {

		StringBuilder str = new StringBuilder( );

		printTallPrefixTree( root, str );

		return str.toString();
	}

	/**
	 * This is used for the toPrefixTree to write the string by
	 * traversing through the tree
	 * 
	 * @param n Node of the TreeNode
	 * @param str StringBuilder to used to append something
	 */
	private void printTallPrefixTree( TreeNode n, StringBuilder str )
	{	
		str.append( "\""+ n.letter + "\"" );

		// for adding the parenthesis after each element
		if ( n.children.size() != 0 )
		{
			str.append(" (" );
		}

		// Traverse through each branch of this tallPrefixTree
		if ( n.children.size() != 0 )
		{
			for ( TreeNode tn : n.children )
			{
				printTallPrefixTree( tn, str );
				
				if ( n.letter.equals( "" ) )
				{
					str.append( " , " );
				}
			}
		}
		
		str.append( " ) " );
	}

	@Override
	public void writeDotFile(String filename) {

		try {

			PrintWriter file = new PrintWriter( filename );
			file.write("digraph tallPrefixTree {" );
			file.println();
			file.write( "//List of Nodes" );
			file.println();

			listOfTreeNodes( root, "n", 0, file );

			file.write( "// A list of connections between the children" );
			file.println();

			connectionBetweenNodes( root, "n", 0, file );
			file.write("}");

			file.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}


	@Override
	public void writeDotFile( String filename, String prefix ) {

		try {

			PrintWriter file = new PrintWriter( filename );
			file.write("digraph tallPrefixTreeFromSpecificString {" );
			file.println();
			file.write( "//List of Nodes" );
			file.println();

			// get to the certain node of this prefix
			TreeNode current = root;
			try
			{
				for ( int i = 0; i < prefix.length(); i++ )
				{
					current = current.children.get( prefix.substring( i, i+1 ) );
				}
			}
			catch( NoSuchElementException e )
			{
				return;
			}

			listOfTreeNodes( current, "n", 0, file );

			file.write( "// A list of connections between the children" );
			file.println();

			connectionBetweenNodes( current, "n", 0, file );
			file.write("}");

			file.close();
		} catch ( FileNotFoundException e ) {

			e.printStackTrace();
		}
	}


	/**
	 * 
	 * Program Description: Private class of the tree node
	 * that holds a unique character for that node, and a
	 * StringKVList class that is considered the different 
	 * branches of the tree node.
	 *
	 * Date Last Modified: Nov 1, 2015
	 *
	 * @author: kalaarentz
	 */
	private static class TreeNode
	{
		private String letter;
		private StringKVList<TreeNode> children;

		private TreeNode( String character )
		{
			letter = character;

			children = new StringKVList<>();
		}
	}

	/**
	 * Used for the write dot file to write each 
	 * node and the label that goes with that node
	 * @param node TreeNode 
	 * @param prefix String will be "n_0..."
	 * @param idx int that will represent the branches of the 
	 * of that tree node in the StringKVList branches
	 * @param file PrintWriter to write to the dot file.
	 */
	private void listOfTreeNodes( TreeNode node, String prefix, int idx, PrintWriter file )
	{
		prefix = prefix + "_" + idx;
		String label = "[label=\"" + node.letter + "\"];";
		String str = prefix + label;

		file.write( str );
		file.println();

		if ( node.children.size() != 0 )
		{
			int i = 0;
			for ( TreeNode n : node.children )
			{
				listOfTreeNodes( n, prefix, i, file);
				i++;
			}
		}

	}

	/**
	 * Writing to the dot file and the connections between each node
	 * 
	 * @param node TreeNode that will represent that node
	 * @param prefix String that will be the node representation "n_0..."
	 * @param idx int will representing the branches of the children
	 * @param file PrintWriter to write to the dot file
	 */
	private void connectionBetweenNodes( TreeNode node, String prefix, int idx, PrintWriter file )
	{
		prefix= prefix + "_" + idx;
		String arrow = "->";

		if ( node != null )
		{
			int i = 0;
			for ( TreeNode n : node.children )
			{
				file.write( prefix + arrow + prefix+ "_" + i + ";");
				file.println();

				connectionBetweenNodes( n, prefix, i, file);
				i++;
			}
		}
	}


}
