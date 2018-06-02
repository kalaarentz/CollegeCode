import java.util.LinkedList;
import java.util.List;


/**
 * Program Description: One part of this class will be used to hold
 * all the information read from the enrollment file, while the other will 
 * be used with the enrollment class to keep track of the individual terms
 * in the file read. 
 *
 * Date Last Modified: Sep 28, 2015
 *
 * @author: kalaarentz
 */
public class Term implements Comparable<Term>
{

	private int studentID;
	private int courseID;
	private int termID;
	private int termYear;
	private String termSession;
	private String termGrade;

	private List<Enrollment> enrolled;
	private List<Student> studentsInTerm = new LinkedList<>();

	/**
	 * Will be a complete copy of the enrollment file. Will have
	 * all the information to be used later for when you write 
	 * a new file.
	 * 
	 * @param sid int student identification number
	 * @param cid int course identification number
	 * @param tid int term identification number
	 * @param year int term year
	 * @param session int term session (Fall, Summer, Spring, Winter)
	 * @param grade of this term session and course
	 */
	public Term( int sid, int cid, int tid,
			int year, String session, String grade )
	{

		studentID = sid;
		courseID = cid;
		termID = tid;
		termYear = year;
		termSession = session;
		termGrade = grade;

		enrolled = new LinkedList<>();
	}
	/**
	 *  Second constructor for a term that will hold the information
	 *  regarding the enrollment list of courses and students
	 *  
	 */
	public Term() {
		enrolled = new LinkedList<>();
	}
	/**
	 * Set the private variable for the student id
	 * that is enrolled in this term
	 * @param sid student identification number
	 */
	public void setStudentID( int sid )
	{
		studentID = sid;
	}

	/**
	 * Set the private variable for the course id 
	 * of the course offered this term 
	 * 
	 * @param cid course identification number
	 */
	public void setCourseID( int cid )
	{
		courseID = cid;
	}
	/**
	 * Set the term identification number for the term 
	 * 
	 * @param tid int term identification number
	 */
	public void setTermID( int tid )
	{
		termID = tid;
	}

	/**
	 * Set the private variable of the term year 
	 * @param year int term year 
	 */
	public void setTermYear( int year )
	{
		termYear = year;
	}

	/**
	 * Set the private variable of term session 
	 * @param session string term session either Fall, Summer, Spring, or 
	 * Winter
	 */
	public void setTermSession( String session )
	{
		termSession = session;
	}

	/**
	 * Set the private variable of the grade for that student
	 * in this term for a specific course
	 * 
	 * @param grade String grade of this student in this enrollment file
	 */
	public void setStudentsGrade( String grade )
	{
		termGrade = grade;
	}

	/**
	 * Return the term identification number
	 * 
	 * @return termID int term identification number 
	 */
	public int getTermID() {

		return termID;
	}

	/**
	 * Return the term year 
	 * @return termYear term year associated with this term 
	 */
	public int getTermYear()
	{
		return termYear;
	}

	/**
	 *  Return the term session of the term
	 * @return termSession session associated with this term
	 */
	public String getTermSession()
	{
		return termSession;
	}

	/**
	 * Return the course identification of the term
	 * 
	 * @return courseID int course identification number 
	 */
	public int getCourseID()
	{
		return courseID;
	}

	/**
	 * Return the student identification number
	 * 
	 * @return studentID student identification number 
	 */
	public int getStudentID()
	{
		return studentID;
	}

	/**
	 *  Return the grade from the term
	 *  
	 * @return termGrade grade for this term object
	 */
	public String getGrade()
	{
		return termGrade;
	}

	/**
	 * Will return the reference to the enrollment 
	 * object in the list
	 * 
	 * @param cid course identification number to look 
	 * for
	 * @return reference to enrollment object in enrollment list
	 */
	public Enrollment getCourse( int cid )
	{
		for ( Enrollment e : enrolled )
		{
			if ( e.getCID() == cid )
			{
				return e;
			}
		}
		return null;
	}

	/**
	 * Will return true if that course is already enrolled 
	 * in this term
	 * will return false if that course is not enrolled in
	 * this term
	 * 
	 * @param cid int course identification number
	 * @return boolean either true if course is found or 
	 * false if course is not found
	 */
	public boolean isCourseEnrolled( int cid )
	{
		for ( Enrollment e : enrolled )
		{
			if ( cid == e.getCID() )
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Will take the course id to iterate through enrollment list
	 * and will then look an that enrollment to see if student is 
	 * enrolled into the that class already
	 * 
	 * Will return true if student is enrolled
	 * 
	 * Will return false if student is not enrolled 
	 * @param cid  int course identification number 
	 * @param sid  int student identification number
	 * @return boolean true if student is found in the list of enrolled courses
	 */
	public boolean isStudentEnrolledInCourse( int cid, int sid )
	{
		for ( Enrollment e : enrolled )
		{
			if ( e.getCID() == cid && e.isStudentEnrolled( sid ) )
			{
				return true;
			}

		}
		return false;
	}

	/**
	 * Will see if a student is enrolled in this term
	 * 
	 * Will return true if the student is found in this term
	 * 
	 * Will return false if student is not enrolled 
	 * 
	 * @param sid int student identification number
	 * @return boolean
	 */
	public boolean isStudentEnrolled( int sid )
	{
		for ( Enrollment e : enrolled )
		{
			if ( e.isStudentEnrolled( sid ) )
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Will return the total number of students enrolled 
	 * in the entire term.
	 * 
	 * @return allStudents.size() the length of the list of allStudents 
	 */
	public int returnNumberOfStudentsEnrolledInTerm( )
	{

		return studentsInTerm.size();
	}

	/**
	 * Will add the enrollment object to the enrolled list of the
	 * enrollment class
	 * 
	 * @param Course course reference to course object
	 */
	public void addEnrollment( Course course )
	{
		Enrollment obj = new Enrollment( course );
		enrolled.add( obj );
	}

	/**
	 * Will add Student to a specific course 
	 * 
	 * @param student that will keep the information about the student
	 * @param cid course identification number
	 */
	public void addStudent( Student student , int cid )
	{
		for ( Enrollment e : enrolled )
		{
			if ( e.getCID() == cid )
			{
				e.addStudent( student );
			}
		}
		
		if ( !isStudentEnrolledInTerm( student ) )
		{
			studentsInTerm.add( student );
		}
		
	}

	
	/**
	 * Will return all a list of all the students enrolled in this term
	 * @return List< Student > allStudents
	 */
	public List<Student> getAllStudentsEnrolled( )
	{		
		return studentsInTerm;
	}

	/**
	 * Will return the list of all the courses identification 
	 * numbers that are held in this term
	 * 
	 * @return List<Enrollment> enrolled courses
	 */
	public List<Enrollment> getEnrolledList( )
	{
		return enrolled;
	}

	/**
	 * Overridden method of toString()
	 * @Override
	 * @return String Term: termID, termSession, termYear, number of students
	 * enrolled, and number of courses offered 
	 * 
	 */
	public String toString() {

		return "Term: " + termID +  ", " + termSession + ", " + termYear
				+ ", " + returnNumberOfStudentsEnrolledInTerm( ) 
				+ ", " + enrolled.size();

	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	
	/**
	 * Will compare the term and sort them by the years, with 
	 * the years closer to our year 2015, on the top
	 * 
	 * so descending order 
	 * 
	 * @return 0 for the same number, -1 for number less then other
	 * and 1 for number greater then term other
	 */
	public int compareTo(Term other) {
		

		if ( this.getTermYear() != other.getTermYear() )
		{
			return other.getTermYear() - this.getTermYear();
		}

		return 0;

	}
	
	/**
	 * This will remove the course form the enrolled list that has
	 * references to all the course objects that are enrolled for 
	 * this specific term.
	 * 
	 * @param course Course object to remove
	 */
	public void removeCourseFromEnrolledList( Course course )
	{
		enrolled.remove( enrolled.indexOf( course ) );
	} 
	
	/**
	 * Will be removing the student form all the enrolled courses
	 * that have this student 
	 * 
	 * When removeStudentFromEnrolledList I am assuming that i have 
	 * already checked that the student exists in this term
	 * 
	 * @param student Student object reference so I can use 
	 * the studentID to compare to the students in the enrolled list
	 */
	public void removeStudentFromEnrolledList( Student student )
	{
		for ( Enrollment e : enrolled )
		{
			if ( e.isStudentEnrolled( student.getStudentID() ) )
			{
				e.removeStudent( student );
			}
		}
	}
	/**
	 * Check to make sure you do not have a duplicate student 
	 * in the studentsInTerm
	 * 
	 * Will return true if there is a student with the same 
	 * identification number--> which means it is the same student
	 * 
	 * Will return false if the student identification number is not
	 * found in studentsInTerm
	 * 
	 * @param student Student object that is a reference 
	 * @return boolean true if student is already enrolled in studentsInTerm
	 * false if they are not found 
	 */
	private boolean isStudentEnrolledInTerm( Student student )
	{
		for ( Student s : studentsInTerm )
		{
			if ( student.getStudentID() == s.getStudentID() )
			{
				return true;
			}
		}
		return false;
	}


}
