import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class ShortPrefixTree implements PrefixTree {

	private TreeNode root;

	public ShortPrefixTree() {

		root = new TreeNode( "" );
		root.string = "";
	}

	@Override
	public void insert(String s) {
		// TODO Auto-generated method stub
		insert( root, s );
	}

	private void insert( TreeNode node, String prefix )
	{

		if ( prefix.length() == 0 ) 
		{
			if ( !node.children.contains("") ) 
			{
				node.children.insert( "", new TreeNode( "" ) );
			}
		}

		int index = -1;
		TreeNode tmpNode = null;
		for ( TreeNode child : node.children ) 
		{
			int tmpIdx = indexOfMatchedString( child.string, prefix );
			if ( tmpIdx > index ) 
			{
				index = tmpIdx;
				tmpNode = child;
			}
		}

		if ( node.children.contains( prefix ) ) 
		{
			return;
		}
		if ( index == -1 ) 
		{
			node.children.insert( prefix, new TreeNode( prefix ) );
			TreeNode tmp = node.children.get( prefix );
			insert (tmp, "" );

		}
		else if ( node.children.contains( prefix.substring(0, index + 1 ) ) ) 
		{
			tmpNode.children.insert( prefix.substring(index + 1), 
					new TreeNode( prefix.substring( index + 1 ) ) );
		} 
		else
		{
			TreeNode endOfOldPrefix = new TreeNode( 
					tmpNode.string.substring( index + 1) );
			tmpNode.string = prefix.substring( 0, index + 1 );

			for ( TreeNode child : tmpNode.children)  
			{
				endOfOldPrefix.children.insert(child.string, child);
			}

			tmpNode.children.clear();
			tmpNode.children.insert( 
					tmpNode.string.substring( index + 1 ), endOfOldPrefix );
			insert( tmpNode, prefix.substring( index + 1 ) );
		}

	}

	private int indexOfMatchedString( String prefix, String str )
	{
		// idx will be -1 if there is no similar words at all
		// or the the first string of the str1 and str2 do not have
		// a similar string at all
		int idx = -1;

		for ( int i = 0; i < prefix.length() &&  i < str.length(); i++ )
		{
			if ( prefix.charAt( i ) == str.charAt( i ) )
			{
				idx = i;
			}
			else
			{
				return idx;
			}
		}

		return idx;
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
			if ( ! n.string.equals( "" ) )
			{
				words.addAll( getMatches( n, prefix + n.string ) );
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
		str.append( "\""+ n.string + "\"" );

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

				if ( n.string.equals( "" ) )
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
			file.write("digraph shortPrefixTree {" );
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
	public void writeDotFile(String filename, String prefix) {

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
	 * Program Description: Will be the private class that will hold the 
	 * tree node and the string that is correlated with that specific 
	 * tree node or branch/child
	 *
	 * Date Last Modified: Nov 3, 2015
	 *
	 * @author: kalaarentz
	 */
	private static class TreeNode
	{
		private String string;
		private StringKVList<TreeNode> children;

		private TreeNode( String str )
		{
			string = str;
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
	private void listOfTreeNodes( TreeNode node, String prefix, 
			int idx, PrintWriter file )
	{
		prefix = prefix + "_" + idx;
		String label = "[label=\"" + node.string + "\"];";
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
	private void connectionBetweenNodes( TreeNode node, 
			String prefix, int idx, PrintWriter file )
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
