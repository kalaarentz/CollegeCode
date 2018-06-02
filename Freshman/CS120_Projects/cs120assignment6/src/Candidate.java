/**
 * Assignment 06: Candidate Template
 * 
 * This class represents a candidate applying for a position.
 * This class accumulates votes in support of an election activity.
 * You must not change this template class.
 * 
 * Last Modified: Oct. 1, 2014
 * @author Josh Hursey
 */

public class Candidate {
	/** Name of the candidate */
	private String name;
	/** Statement provided by candidate */
	private String blurb;
	/** Position candidate is applying for */
	private String position;
	/** Political party */
	private String party;
	/** Number of votes accumulated for this candidate */
	private int numberOfVotes;

	/**
	 * Instantiate a new candidate. 
	 * @param n Name of the candidate
	 * @param p Position candidate is applying for
	 */
	public Candidate(String n, String p) {
		name = n;
		position = p;
		blurb = "";
		party = "";
		numberOfVotes = 0;
	}
	
	/**
	 * Set the statement provided by the candidate
	 * @param b Statement provided by candidate
	 */
	public void setBlurb(String b) {
		blurb = b;
	}

	/**
	 * Return the statement provided by the candidate
	 * @return Statement provided by candidate
	 */
	public String getBlurb() {
		return blurb;
	}

	/**
	 * Return the name of the candidate
	 * @return Name of the candidate
	 */
	public String getName() {
		return name;
	}
	
    
    
    
    
    
	/**
	 * Return the position the candidate is applying for
	 * @return Position the candidate is applying for
	 */
	public String getPosition() {
		return position;
	}
    
	/**
	 * Set the political party of the candidate
	 * @param p Party of the candidate
	 */
	public void setPoliticalParty(String p) {
	    party = p;
	}

	/**
	 * Return the political party of the candidate
	 * @return political party
	 */
	public String getPoliticalParty() {
		return party;
	}
	
	/**
	 * Record a single vote for this candidate
	 */
	public void recordVote() {
		numberOfVotes++;
	}
	
	/**
	 * Return the accumulated total number of votes for this candidate
	 * @return Number of votes accumulated
	 */
	public int getNumberOfVotes() {
		return numberOfVotes;
	}
}
