
/**
 * Program Description: This will store a substring and
 * its count value. This class will be Comparable to other
 * things in its type.
 * 
 * The impact factor is the product of the substring and the 
 * length of the substring.
 *
 * Date Last Modified: Nov 24, 2015
 *
 * @author: kalaarentz
 */
public class StringValuePair implements Comparable<StringValuePair>
{
	private String word;
	private int frequency;
	private int impactFactor;
	
	public StringValuePair( String str, int freq ) 
	{
		word = str;
		frequency = freq;
		impactFactor = word.length() * frequency;
		
	}
	
	public String getWord()
	{
		return word;
	}
	
	public int getFrequency()
	{
		return frequency;
	}
	
	public int getImpactFactor()
	{
		return impactFactor;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(StringValuePair obj) {
		// TODO Auto-generated method stub
		
		if( this.getImpactFactor() > obj.getImpactFactor() )
		{
			return -1;
		}
		else if ( this.getImpactFactor() < obj.getImpactFactor() )
		{
			return 1;
		}
		
		return 0;
	}

}
