
/**
 * Program Description: This program will hold all the information related to
 * courses that is read from the course file. 
 * 
 * Date Last Modified: Sep 28, 2015
 *
 * @author: kalaarentz
 */
public class Course implements Comparable<Course> 
{

	/*
	 *  ***********************************
	 *  PRIVATE VARIABLES
	 *  ***********************************
	 */

	private int courseID;
	private String departAbb;
	private int courseNum;
	private int numOfCredits;
	private String nameOfCourse;
	private String additionalInfo;


	/*
	 *  **********************************************************
	 *  SETTING ALL THE INFORMATION TO PRIVATE VARIABLES METHODS
	 *  **********************************************************
	 */
	
	/**
	 * Will set the course identification number
	 * @param cid int course identification number from the file
	 */
	public void setCourseID( int cid )
	{
		courseID = cid;
	}
	
	/**
	 * Will set the department abbreviation from the file
	 * @param dept String
	 */
	public void setDepart( String dept )
	{
		departAbb = dept;
	}
	
	/**
	 * Will set the course number from the file for this object
	 * @param num int 
	 */
	public void setCourseNumber( int num )
	{
		courseNum = num;
	}
	
	/**
	 * Will set the course credits from the file for this object
	 * @param credits int 
	 */
	public void setCredits( int credits )
	{
		numOfCredits = credits;
	}

	/**
	 * Will set the name of the course read from the file for this object
	 * @param name String 
	 */
	public void setName( String name )
	{
		nameOfCourse = name;
	}
	
	/**
	 * Will set the any extra information that may be found from the file
	 * Not all courses have extra information
	 * @param extra Sting 
	 */
	public void setAttributes( String extra )
	{
		additionalInfo = extra;
	}
	/*
	 *  *************************************************
	 *  RETRIEVING ALL THE PRIVATE VARIABLES METHODS
	 *  *************************************************
	 */

	/**
	 * Retrieve course ID
	 * @return int courseID: identification number for course
	 */
	public int getCourseID( )
	{
		return courseID;
	}

	/**
	 * Retrieve department abbreviation
	 * 
	 * @return String dpartAbb
	 */
	public String getDepartAbb( )
	{
		return departAbb;
	}

	/**
	 * Retrieve course number in file
	 * 
	 * @return int courseNum
	 */
	public int getCourseNum( )
	{
		return courseNum;
	}

	/**
	 * Retrieve number of credits the course is listed as
	 * 
	 * @return int numOfCredits
	 */
	public int getNumOfCredits( )
	{
		return numOfCredits;
	}

	/**
	 * Retrieve name of the course
	 * 
	 * @return String nameOfCourse
	 */
	public String getNameOfCourse( ) 
	{
		return nameOfCourse;
	}

	/**
	 * Retrieve any additional information about course
	 * 
	 * @return String additionalInfo
	 */
	public String getAdditionalInfo( )
	{
		return additionalInfo;
	}

	/*
	 *  *****************************
	 *  TO STRING OVERIDED MEHTOD
	 *  *****************************
	 */
	
	/**
	 * This is the overiden method for the toString() method
	 * 
	 * Will look like Course: courseID, departAbb, courseNum, numofCredits,
	 * nameOfCourse
	 * 
	 *  @Override
	 */
	public String toString( ) 
	{
		// need to take into account that sometimes additioanlInfo is null

		if ( additionalInfo == null )
		{
			return "Course: " + courseID + ", " + departAbb + ", " 
					+ courseNum + ", " + numOfCredits + ", " + nameOfCourse; 
		}
		else
		{
			return "Course: " + courseID + ", " + departAbb + ", " 
					+ courseNum + ", " + numOfCredits + ", " + nameOfCourse
					+ ", " + additionalInfo;
		}


	}
	/*
	 * ***************************************
	 * COMPARABLE METHOD
	 * ***************************************
	 */

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Course other) {
		// sort the list by depart abb, then course number, number of credits,
		// course name and course id

		if ( this.getDepartAbb().equals( other.getDepartAbb() ) )
		{
			if ( this.getCourseNum() == other.getCourseNum() )
			{
				if ( this.getNumOfCredits() == other.getNumOfCredits() )
				{
					if ( this.getNameOfCourse().equals( 
							other.getNameOfCourse() ) )
					{
						return this.getCourseID() - other.getCourseID();
					}
					return this.getNameOfCourse().compareTo( 
							other.getNameOfCourse() );
				}
				return this.getNumOfCredits() - other.getNumOfCredits();
			}
			return this.getCourseNum() - other.getCourseNum();
		}
		else
		{
			return this.getDepartAbb().compareTo( other.getDepartAbb() );
		}

	}

	



}
