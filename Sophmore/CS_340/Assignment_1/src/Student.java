/**
 * Program Description: This class will hold all the information regarding 
 * to a specific student and will have all individual identification numbers
 * Will be used with the enrollment class
 *
 * Date Last Modified: Sep 28, 2015
 *
 * @author: kalaarentz
 */
public class Student implements Comparable<Student>{

	/*
	 *  ***********************************
	 *  PRIVATE VARIABLES
	 *  ***********************************
	 */

	private int studentID;
	private String firstName;
	private String lastName;
	private String username;

	/*
	 *  *************************************************
	 *  SETTING ALL THE INFORMATION TO PRIVATE VARIABLES
	 *  *************************************************
	 */

	/**
	 * Set the student identification number from the file
	 * 
	 * @param id int identification number
	 */
	public void setStudentID( int id ) 
	{
		studentID = id;
	}

	/**
	 * Set the first name that was read from the file
	 * 
	 * @param name String first name 
	 */
	public void setFirstName( String name )
	{
		firstName = name;
	}

	/**
	 * Set the last name that was read from the file 
	 * 
	 * @param name String last name 
	 */
	public void setLastName( String name )
	{
		lastName = name;
	}

	/**
	 * Set the username that was read from the file
	 * 
	 * @param name String username 
	 */
	public void setUsername( String name )
	{
		username = name;
	}

	/*
	 *  *************************************************
	 *  RETRIEVING ALL THE PRIVATE VARIABLES METHODS
	 *  *************************************************
	 */

	/**
	 * Retrieve student identification number 
	 * @return int studentID
	 */
	public int getStudentID( )
	{
		return studentID;
	}

	/**
	 * Retrieve first name
	 * 
	 * @return String firstName
	 */
	public String getFirstName( )
	{
		return firstName;
	}

	/**
	 * Retrieve last name 
	 * 
	 * @return String lastName
	 */
	public String getLastName( )
	{
		return lastName;
	}

	/**
	 * Retrieve username 
	 * 
	 * @return String username 
	 */
	public String getUsername( )
	{
		return username;
	}

	/*
	 *  *****************************
	 *  TO STRING OVERIDED MEHTOD
	 *  *****************************
	 */
	/**
	 * This will be the toString method overridden
	
	 * @return String Student: studentID, lastName, firstName, username
	 * @Override
	 */
	public String toString( ) {

		return "Student: " + studentID + ", "  + lastName + ", " + firstName 
				+ ", " + username;

	}

	/*
	 * ********************************
	 * OVERIDEN MEHTOD FOR COMPARABLE
	 * ********************************
	 * 	
	 */
	
	/**
	 * This is the overridden method of Comparable
	 * This will compare two objects if will make sure last names 
	 * are not equal then if will be order that way, but if the same then
	 * they will be ordered by last name, then user name and finally
	 * the student identification number  
	 * 
	 * @return return either 0 for same, -1 for less than, or 1 for greater
	 * then when comparing objects
	 * @Override
	 * 
	 */
	public int compareTo( Student other )
	{
		if ( this.getLastName().equals( other.getLastName() ) )
		{
			// check to see if first name is the same
			if ( this.getFirstName().equals( other.getFirstName() ) )
			{
				if ( this.getUsername().equals( other.getUsername() ) )
				{
					return this.getStudentID() - ( other.getStudentID() );
				}
				return this.getUsername().compareTo( other.getUsername() );
			}
			else
			{
				return this.getFirstName().compareTo( other.getFirstName() );
			}
		}
		else
		{
			return this.getLastName().compareTo( other.getLastName() );
		}


	}
}
