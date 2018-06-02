import java.util.LinkedList;
import java.util.List;
/**
 * Program Description: This class will hold information regarding the 
 * students enrolled in a specific course. The class will have a students
 * list that holds the Student object that holds all the information 
 * to that student. Will be directly involved with the term class.
 *
 * Date Last Modified: Sep 25, 2015
 *
 * @author: kalaarentz
 */
public class Enrollment
{

	private List<Student> students = new LinkedList<>();
	private Course courseOfTerm;
	
	
	/**
	 * Constructor, enrollment will be set when course 
	 * Identification number is passed through
	 * @param course Course object that will be passed through
	 */
	public Enrollment( Course course ) 
	{ 
		courseOfTerm = course;
	}

	/**
	 *  will return the courseID
	 *  
	 * @return courseID
	 */
	
	public int getCID( )
	{
		return courseOfTerm.getCourseID();
	}
	
	
	/**
	 * This will check to make sure you do not have any duplicates of 
	 * students in the enrolled students for this class
	 * 
	 *  true will mean that there is a student in this course with 
	 *  that student id
	 *  
	 *  false will mean that there is not a student in this course with 
	 *  that student id
	 * 
	 * @param sid student identification number will be an int
	 * @return boolean either true or false explained above
	 */
	public boolean isStudentEnrolled( int sid )
	{
		for ( Student s : students )
		{
			if ( sid == s.getStudentID() )
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Will add student class to the students list that 
	 * holds all the students enrolled in this course 
	 * 
	 * @param student Student object passed through; will be reference
	 */
	public void addStudent( Student student )
	{
		students.add( student );
	}
	
	/**
	 * Remove a student form a specific course
	 * 
	 * @param student Student object that is a reference
	 */
	public void removeStudent( Student student )
	{
		for ( Student s : students )
		{
			if ( s.getStudentID() == student.getStudentID() )
			{
				students.remove( ( students.indexOf( s ) ) );
			}
		}
	}
	
	/**
	 * Will return the length of the list students
	 * but in reality the amount of students enrolled in 
	 * this course 
	 * 
	 * @return students size of the LinkedList students
	 */
	public int getNumOfStudentsEnrolled( )
	{
		return students.size();
	}
	
	/**
	 * Return the entire student list for this enrollment object
	 * 
	 * @return students List of all students enrolled
	 */
	public List<Student> returnStudentsEnrolled( )
	{
		return students;
	}
	
	
	
}
