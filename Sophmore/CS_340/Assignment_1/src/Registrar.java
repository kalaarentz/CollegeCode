/**
 * Program Description: This class implements the DataQuery interface and uses
 * information from files about specific terms and the students enrolled and 
 * the course offered during those terms
 *
 * Date Last Modified: Sep 28, 2015
 *
 * @author: kalaarentz
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class Registrar implements DataQuery
{
	// Original Lists/Files
	private List<Student> myStudents = new LinkedList<>();
	private List<Course> myCourses = new LinkedList<>();

	// this list will have all the information read from the enrollment file
	private List<Term> allMyTermsInfo = new LinkedList<>();

	// this list will just be the individual amount of terms
	// will not have any duplicates terms with the same termID number
	private List<Term> myTerms = new LinkedList<>();

	// Lists that have added more information, used for saveData


	/* (non-Javadoc)
	 * @see DataQuery#loadData(java.lang.String, java.lang.String, 
	 * java.lang.String)
	 */
	@Override
	public void loadData(String studentFilename, String courseFilename,
			String termFilename) throws IOException {
		// TRY CATCH BLOCK TO OPEN ALL FILES AND TO CATCH IOException THAT
		// MAY BE THROWN

		//LinkedList for students, course, enrollement/term

		try {

			// BUFFERED READERS FOR THE THREE FILES 
			// ***********************************************************
			BufferedReader student = 
					new BufferedReader( new FileReader( studentFilename ) );
			BufferedReader course = 
					new BufferedReader( new FileReader( courseFilename ) );
			BufferedReader term =
					new BufferedReader( new FileReader( termFilename ) );

			String sline = student.readLine();
			String cline = course.readLine();
			String tline = term.readLine();

			// parsing through Student file
			while ( sline != null )
			{
				// parse the student line
				Scanner parser = new Scanner( sline );
				parser.useDelimiter( "#" );

				// add the information found in the student text files to 
				// the stud or new Student 
				Student stud = new Student( );
				stud.setStudentID( parser.nextInt( ) );
				stud.setFirstName( parser.next( ) );
				stud.setLastName( parser.next( ) );
				stud.setUsername( parser.next( ) );

				// add student to myStudents LinkedList
				myStudents.add( stud );

				sline = student.readLine( );
				parser.close();

			}

			// -------------------------------------------
			// PARSING THROUGH COURSE FILE
			while ( cline != null )
			{
				// parse the course line
				Scanner parser = new Scanner( cline );
				parser.useDelimiter( "#" );

				// add the information found in the course text files to 
				// the crse or new Course 
				Course crse = new Course( );

				crse.setCourseID(parser.nextInt() );
				crse.setDepart( parser.next( ) );
				crse.setCourseNumber( parser.nextInt( ) );
				crse.setCredits( parser.nextInt( ) );
				crse.setName( parser.next( ) );

				// this is to check if there is more additional information
				if ( parser.hasNext() )
				{
					crse.setAttributes( parser.next() );
				}

				// add student to myStudents LinkedList
				myCourses.add( crse );

				cline = course.readLine( );
				parser.close();

			}

			// -------------------------------------------
			// PARSING THROUGH ENROLLMENT FILE
			Term semester;

			while ( tline != null )
			{
				Scanner parser = new Scanner( tline );
				parser.useDelimiter("#");

				int sid = parser.nextInt();
				int cid = parser.nextInt();
				int tid = parser.nextInt();
				int year = parser.nextInt();
				String session = parser.next();
				String grade = parser.next();

				semester = new Term( sid, cid, tid,
						year, session, grade );

				allMyTermsInfo.add( semester );

				tline = term.readLine();
				parser.close();

			}

			// will be separating all terms to put in the myTerms list 
			// from the allMyTermsInfo
			for ( Term t : allMyTermsInfo )
			{
				if ( myTerms.isEmpty() )
				{
					t.addEnrollment( findCourse( t.getCourseID() ) );
					t.addStudent( findStudent( t.getStudentID() ),
							t.getCourseID() );
					myTerms.add( t );
				}

				if ( !isTermIdInMyTermsList( t.getTermID() ) )
				{
					t.addEnrollment( findCourse( t.getCourseID() ) );
					t.addStudent( findStudent( t.getStudentID() ),
							t.getCourseID());
					myTerms.add( t );
				}
				else 
				{
					if ( !t.isCourseEnrolled( t.getCourseID() ) )
					{
						getSpecificTermInMyTermsList( 
								t.getTermID() ).addEnrollment( 
										findCourse( t.getCourseID() ) );
					}

					if ( !t.isStudentEnrolledInCourse( t.getCourseID(),
							t.getStudentID() ) )
					{
						getSpecificTermInMyTermsList( t.getTermID() )
							.addStudent( findStudent( t.getStudentID()),
								t.getCourseID() );
					}

				}

			}

			// Close all the BufferedReader Files
			student.close();
			course.close();
			term.close();


		} 
		catch ( IOException e )
		{
			throw e;
		}

	}

	/* (non-Javadoc)
	 * @see DataQuery#saveData(java.lang.String, java.lang.String, 
	 * java.lang.String)
	 */
	@Override
	public void saveData(String studentFilename, String courseFilename,
			String termFilename) throws IOException {

		// BUFFERED WRITERS FOR THE THREE FILES 
		// ***********************************************************
		BufferedWriter student = 
				new BufferedWriter( new FileWriter( studentFilename ) );
		BufferedWriter course = 
				new BufferedWriter( new FileWriter( courseFilename ) );
		BufferedWriter term =
				new BufferedWriter( new FileWriter( termFilename ) );

		// Iterate through each of the lists so you can write the 
		// new info into another text file 

		// READ INTO STUDENT FILE
		for ( Student s : myStudents )
		{
			student.write( s.getStudentID() + "#" + s.getFirstName() + "#" 
					+ s.getLastName() + "#" + s.getUsername() );
			student.newLine();
		}

		// READ INTO COURSE FILE
		for ( Course c : myCourses )
		{
			// Take into account the additional information variable
			// may be null 
			if ( c.getAdditionalInfo() != null )
			{
				course.write( c.getCourseID() + "#" + c.getDepartAbb() + "#" 
						+ c.getCourseNum() + "#" + c.getNumOfCredits() + "#" 
						+ c.getNameOfCourse() + "#" + c.getAdditionalInfo() );
				course.newLine();
			}
			else
			{
				course.write( c.getCourseID() + "#" + c.getDepartAbb() + "#" 
						+ c.getCourseNum() + "#" + c.getNumOfCredits() + "#" 
						+ c.getNameOfCourse() );
				course.newLine();
			}

		}

		// READ INTO THE ENROLLMENT FILE 
		for ( Term t : allMyTermsInfo )
		{
			term.write( t.getStudentID() + "#" + t.getCourseID() + "#" 
					+ t.getTermID() + "#" + t.getTermYear() + "#" 
					+ t.getTermSession() + "#" + t.getGrade() );
		}

		student.close();
		course.close();
		term.close();

	}

	/* (non-Javadoc)
	 * @see DataQuery#getAllStudents()
	 */
	@Override
	public List<Student> getAllStudents() {
		// Will sort the myStudents by the Comparable part of students
		Collections.sort( myStudents );

		return myStudents;
	}

	/* (non-Javadoc)
	 * @see DataQuery#findStudent(int)
	 */
	@Override
	public Student findStudent(int sid) {
		// Search for specific student by sid number

		for ( Student s: myStudents )
		{
			if ( s.getStudentID() == sid )
			{
				return s;
			}
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see DataQuery#getAllCourses()
	 */
	@Override
	public List<Course> getAllCourses() {

		Collections.sort( myCourses );
		return myCourses;
	}

	/* (non-Javadoc)
	 * @see DataQuery#findCourse(int)
	 */
	@Override
	public Course findCourse(int cid) {
		// search for specific course by using course id

		for ( Course c : myCourses )
		{
			if ( c.getCourseID() == cid )
			{
				return c;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see DataQuery#getAllTerms()
	 */
	@Override
	public List<Term> getAllTerms() {
		// Sort the my enrollment to follow the list format
		// will by using collection sort

		Collections.sort( myTerms, new CompareDifferentSessions() );
		return myTerms;
	}


	/* (non-Javadoc)
	 * @see DataQuery#findTerm(int)
	 */
	@Override
	public Term findTerm(int tid) {

		for ( Term t : myTerms )
		{
			if ( t.getTermID() == tid )
			{
				return t;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see DataQuery#findTerm(int, java.lang.String)
	 */
	@Override
	public Term findTerm(int year, String session)
		throws IllegalArgumentException {

		if ( session.equals( "Fall") || session.equals( "Spring") 
				|| session.equals( "Summer") || session.equals( "Winter") )
		{
			for ( Term t : myTerms )
			{
				if ( t.getTermYear() == year 
						&& t.getTermSession().equals( session ) )
				{

					return t;
				}
			}
		}
		else
		{
			throw new IllegalArgumentException();
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see DataQuery#getAllStudentsEnrolledInTerm(Term)
	 */
	@Override
	public List<Object[]> getAllStudentsEnrolledInTerm(Term term)
		throws IllegalArgumentException {

		// check to make sure that term is found in list
		if ( findTerm( term.getTermID() ) == null )
		{
			throw new IllegalArgumentException();
		}

		// create list and obj for adding information
		Object[] obj = new Object[ 2 ];
		List<Object[]> studentsEnrolled = new LinkedList<>();
		int count = 0;

		for ( Student s : term.getAllStudentsEnrolled() )
		{
			obj[ 0 ] = s;
			for ( Enrollment e : term.getEnrolledList() )
			{
				if ( e.isStudentEnrolled( s.getStudentID() ) )
				{
					count += findCourse( e.getCID() ).getNumOfCredits();
				}
			}
			obj[1] = count;
			studentsEnrolled.add( obj );
		}


		return studentsEnrolled;
	}

	/* (non-Javadoc)
	 * @see DataQuery#getAllCoursesOfferedInTerm(Term)
	 */
	@Override
	public List<Object[]> getAllCoursesOfferedInTerm(Term term)
		throws IllegalArgumentException 
	{
		
		if ( isTermIdInMyTermsList( term.getTermID() ) )
		{
			throw new IllegalArgumentException();
		}

		List< Object[] > courseOffered = new LinkedList<>();
		Object[] obj = new Object[ 2 ];

		for ( Enrollment e : term.getEnrolledList() )
		{
			obj[ 0 ] = findCourse( e.getCID() );
			obj[ 1 ] = e.getNumOfStudentsEnrolled();
			courseOffered.add( obj );
		}

		return courseOffered;
	}

	/* (non-Javadoc)
	 * @see DataQuery#getClassListForStudentEnrolledInTerm(Term, Student)
	 */
	@Override
	public List<Course> getClassListForStudentEnrolledInTerm(Term term,
			Student student) throws IllegalArgumentException 
			{
		// will check to make sure term is in the lists
		// will check to make sure student is in the lists for students
		if ( !isTermIdInMyTermsList( term.getTermID() ) 
				|| findCourse( student.getStudentID() ) == null )
		{
			throw new IllegalArgumentException();
		}

		List<Course> classList = new LinkedList<>();

		// will search enrollment file for specific student id;
		// if ID is in that course, the add that course to the classList
		for ( Enrollment e : term.getEnrolledList() )
		{
			if ( e.isStudentEnrolled( student.getStudentID() ) )
			{
				classList.add( findCourse( e.getCID() ) );
			}
		}

		Collections.sort( classList );

		return classList;
			}

	/* (non-Javadoc)
	 * @see DataQuery#getClassListForCourseOfferedInTerm(Term, Course)
	 */
	@Override
	public List<Student> getClassListForCourseOfferedInTerm(Term term,
			Course course) throws IllegalArgumentException 
	{
		
		// make sure the course exists in the term object
		if ( findCourse( course.getCourseID() ) == null )
		{
			throw new IllegalArgumentException();
		}

		List<Student> studentsEnrolled = new LinkedList<>();

		// if the course is enrolled in the term then return
		// the students in that course
		if ( term.isCourseEnrolled( course.getCourseID() ) ) 
		{
			studentsEnrolled = term.getCourse( 
					course.getCourseID( ) ).returnStudentsEnrolled();
		}

		Collections.sort( studentsEnrolled );

		return studentsEnrolled;
	}

	/* (non-Javadoc)
	 * @see DataQuery#getCourseHistoryForStudent(Student)
	 */
	@Override
	public List<Object[]> getCourseHistoryForStudent(Student student)
		throws IllegalArgumentException {
		// make my list and object array
		List<Object[]> courseHistory = new LinkedList<>();
		Object[] obj = new Object[ 3 ];

		if ( findStudent( student.getStudentID() ) == null )
		{
			throw new IllegalArgumentException();
		}

		// search through terms for a specific student
		// if she is enrolled, make a reference to that student, course
		// and search through allMyTermsInfo for grade 

		for ( Term t : myTerms )
		{
			if ( t.isStudentEnrolled( student.getStudentID() ) ) 
			{
				obj[ 0 ] = t;
				obj[ 1 ] = findCourse( t.getCourse( 
						student.getStudentID() ).getCID() );

				obj[ 2 ] = findGradeForStudentInCourse( t, findCourse( 
						t.getCourse( student.getStudentID() )
						.getCID() ), student );
				courseHistory.add( obj );
			}
		}

		return courseHistory;
	}

	/* (non-Javadoc)
	 * @see DataQuery#getEnrollmentHistoryForCourse(Course)
	 */
	@Override
	public List<Object[]> getEnrollmentHistoryForCourse(Course course)
		throws IllegalArgumentException {
		// check to make sure course exists
		// then look for that course, return the course, will return null
		// if not, then return number of students in that course

		if ( findCourse( course.getCourseID() ) == null )
		{
			throw new IllegalArgumentException();
		}

		List<Object[]> enrollmentHistory = new LinkedList<>();
		Object[] obj = new Object[ 2 ];

		for ( Term t: myTerms )
		{
			if ( t.isCourseEnrolled( course.getCourseID() ) )
			{
				obj[ 0 ] = t;
				obj[ 1 ] = t.getCourse( course.getCourseID() );
				enrollmentHistory.add( obj );
			}
		}

		return enrollmentHistory;
	}

	/* (non-Javadoc)
	 * @see DataQuery#getCreditsPerTermForStudent(Student)
	 */
	@Override
	public List<Object[]> getCreditsPerTermForStudent(Student student)
		throws IllegalArgumentException {
		//Check to make sure student exits in the system
		if ( findStudent( student.getStudentID() ) == null )
		{
			throw new IllegalArgumentException();
		}

		// Make my list of objects, and object array
		List<Object[]> creditsForStudents = new LinkedList<>();
		Object[] obj = new Object[ 2 ];
		int count = 0;

		for ( Term t : myTerms )
		{
			if ( t.isStudentEnrolled( student.getStudentID() ) )
			{
				obj[ 0 ] = t;

				for ( Enrollment e : t.getEnrolledList() )
				{
					if ( e.isStudentEnrolled( student.getStudentID() ) )
					{
						count += findCourse( e.getCID() ).getNumOfCredits();
					}

				}
				obj[ 2 ] = count;
				creditsForStudents.add( obj );
			}
		}

		return creditsForStudents;
	}

	/* (non-Javadoc)
	 * @see DataQuery#addStudent(int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Student addStudent(int sid, String lastName, String firstName,
			String username) throws Exception {

		// Check to make sure the student does not exist
		if ( findStudent( sid ) == null )
		{
			throw new Exception();
		}

		Student added = new Student();

		added.setFirstName( firstName );
		added.setLastName( lastName );
		added.setStudentID( sid );
		added.setUsername( username );

		myStudents.add( added );
		return added;
	}

	/* (non-Javadoc)
	 * @see DataQuery#addCourse(int, java.lang.String, int, int, java.lang.String, java.lang.String)
	 */
	@Override
	public Course addCourse(int cid, String dept, int courseNumber,
			int credits, String name, String attrs) throws Exception {

		// check to make sure a course doesnt exist such as this
		if ( findCourse( cid ) == null )
		{
			throw new Exception();
		}

		Course added = new Course();

		added.setCourseID( cid );
		added.setDepart( dept );
		added.setCourseNumber( courseNumber );
		added.setCredits(credits);
		added.setName( name );

		// check to make sure attributes is not empty
		if ( attrs != null )
		{
			added.setAttributes( attrs );
		}

		myCourses.add( added );

		return added;
	}

	/* (non-Javadoc)
	 * @see DataQuery#addTerm(int, int, java.lang.String)
	 */
	@Override
	public Term addTerm(int tid, int year, String session) throws Exception,
	IllegalArgumentException {

		// check to make sure session string is valid
		if ( !session.equals( "Fall") || !session.equals( "Summer") 
				|| !session.equals( "Spring") || !session.equals( "Winter") )
		{
			throw new IllegalArgumentException();
		}

		// check to make sure term does not exist
		if ( findTerm( tid ) == null || findTerm( year, session ) == null )
		{
			throw new Exception();
		}

		Term added = new Term();

		added.setTermID( tid );
		added.setTermYear( year );
		added.setTermSession( session );

		myTerms.add( added );

		return added;
	}

	/* (non-Javadoc)
	 * @see DataQuery#addCourseToTerm(Term, Course)
	 */
	@Override
	public void addCourseToTerm(Term term, Course course) throws Exception,
	IllegalArgumentException {
		// check to make sure the term and course exist
		if ( findTerm( term.getTermID() ) == null  
				|| findCourse( course.getCourseID() ) == null )
		{
			throw new IllegalArgumentException();
		}

		// add course to term
		term.addEnrollment( course );


	}

	/* (non-Javadoc)
	 * @see DataQuery#addStudentToCourseInTerm(Term, Course, Student)
	 */
	@Override
	public void addStudentToCourseInTerm(Term term, Course course,
			Student student) throws Exception, IllegalArgumentException {

		// check to make sure the course is enrolled in the term
		if ( !term.isCourseEnrolled( course.getCourseID() ) )
		{
			throw new Exception();
		}
		// check to make sure the course,term, and student exist
		if ( findTerm( term.getTermID() ) == null
				|| findCourse( course.getCourseID() ) == null 
				|| findStudent( student.getStudentID() ) == null )
		{
			throw new IllegalArgumentException();
		}

		// add student to course in term
		term.addStudent( student , course.getCourseID() );

	}

	/* (non-Javadoc)
	 * @see DataQuery#addGradeForStudent(Term, Course, Student, java.lang.String)
	 */
	@Override
	public void addGradeForStudent(Term term, Course course, Student student,
			String grade) throws Exception, IllegalArgumentException {
		// check to make sure there is not a grade for this student
		// in this course in this term
		if ( findGradeForStudentInCourse( term, course, student ) != null )
		{
			throw new Exception();
		}

		// make sure the term, course, and student does exist
		if ( findTerm( term.getTermID() ) == null 
				|| findCourse( course.getCourseID() ) == null 
				|| findStudent( student.getStudentID() ) == null )
		{
			throw new IllegalArgumentException();
		}

		// search for the specific term in allMyTermsInfor to add grade
		for ( Term t : allMyTermsInfo )
		{
			if ( t.getTermID() == term.getTermID() 
					&& t.getCourseID() == course.getCourseID() 
					&& t.getStudentID() == student.getStudentID() )
			{
				t.setStudentsGrade( grade );
			}
		}
	}

	/* (non-Javadoc)
	 * @see DataQuery#updateGradeForStudent(Term, Course, Student, java.lang.String)
	 */
	@Override
	public void updateGradeForStudent(Term term, Course course,
			Student student, String grade) throws Exception,
			IllegalArgumentException {
		// check to make sure there is not a grade for this student
		// in this course in this term
		if ( findGradeForStudentInCourse( term, course, student ) != null )
		{
			throw new Exception();
		}

		// make sure the term, course, and student does exist
		if ( findTerm( term.getTermID() ) == null 
				|| findCourse( course.getCourseID() ) == null 
				|| findStudent( student.getStudentID() ) == null )
		{
			throw new IllegalArgumentException();
		}

		// search for the specific term in allMyTermsInfor to add grade
		for ( Term t : allMyTermsInfo )
		{
			if ( t.getTermID() == term.getTermID() 
					&& t.getCourseID() == course.getCourseID() 
					&& t.getStudentID() == student.getStudentID() )
			{
				t.setStudentsGrade( grade );
			}
		}

	}

	/* (non-Javadoc)
	 * @see DataQuery#removeStudentFromCourseInTerm(Term, Course, Student)
	 */
	@Override
	public void removeStudentFromCourseInTerm(Term term, Course course,
			Student student) throws IllegalArgumentException {
		// make sure the term, course, and student does exist
		if ( findTerm( term.getTermID() ) == null 
				|| findCourse( course.getCourseID() ) == null 
				|| findStudent( student.getStudentID() ) == null )
		{
			throw new IllegalArgumentException();
		}

		if ( term.isStudentEnrolledInCourse( course.getCourseID(), 
				student.getStudentID() ) )
		{
			term.getCourse( course.getCourseID( ) ).removeStudent( student );
		}

	}

	/* (non-Javadoc)
	 * @see DataQuery#removeStudentFromAllCoursesInTerm(Term, Student)
	 */
	@Override
	public List<Course> removeStudentFromAllCoursesInTerm(Term term,
			Student student) throws IllegalArgumentException {
		// check to make sure the student is in system
		if ( findTerm( term.getTermID() ) == null 
				|| findStudent( student.getStudentID() ) == null )
		{
			throw new IllegalArgumentException();
		}

		// search through the term for the student enrolled in each 
		// individual course and make a list of references for that 
		// courses that had student removed

		List<Course> studentRemovedFromCourses = new LinkedList<>();
		for ( Enrollment e : term.getEnrolledList() )
		{
			if ( e.isStudentEnrolled( student.getStudentID() ) )
			{
				e.removeStudent( student );
				studentRemovedFromCourses.add( findCourse( e.getCID() ) );
			}
		}

		Collections.sort( studentRemovedFromCourses );
		return studentRemovedFromCourses;
	}

	/* (non-Javadoc)
	 * @see DataQuery#removeStudent(Student)
	 */
	@Override
	public List<Term> removeStudent(Student student)
		throws IllegalArgumentException {
		if ( findStudent( student.getStudentID() ) == null )
		{
			throw new IllegalArgumentException();
		}

		// search through all the terms for the student and remove
		// from all the terms
		LinkedList<Term> studentRemovedFromTerms = new LinkedList<>();

		for ( Term t : myTerms )
		{
			if ( t.isStudentEnrolled( student.getStudentID() ) )
			{
				t.removeStudentFromEnrolledList( student );
				studentRemovedFromTerms.add( t );
			}
		}
		
		// sort terms
		Collections.sort( studentRemovedFromTerms );
		
		return studentRemovedFromTerms;
	}

	/* (non-Javadoc)
	 * @see DataQuery#removeCourseFromTerm(Term, Course)
	 */
	@Override
	public List<Student> removeCourseFromTerm(Term term, Course course)
		throws Exception, IllegalArgumentException {
		// throw an exception if course can not be found in term
		if ( !term.isCourseEnrolled( course.getCourseID() ) )
		{
			throw new Exception();
		}
		// throw an IllegalArgument if the term/course object did 
		//not originate form the system
		if ( findTerm( term.getTermID())  == null 
				|| findCourse( course.getCourseID() ) == null )
		{
			throw new IllegalArgumentException();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see DataQuery#removeCourse(Course)
	 */
	@Override
	public List<Term> removeCourse(Course course)
			throws IllegalArgumentException {
		// will throw IllegalArgumentException if the course does
		// not exist in our system
		if ( findCourse( course.getCourseID() ) == null )
		{
			throw new IllegalArgumentException();
		}
		
		// List of terms that had the course removed
		List<Term> termsWithoutCourse = new LinkedList<>();
		
		// remove course from all terms
		for ( Term t : myTerms )
		{
			// check to make sure course is enrolled
			if ( t.isCourseEnrolled( course.getCourseID() ) )
			{
				t.removeCourseFromEnrolledList( course );
				termsWithoutCourse.add( t );
			}
		}
		// sort like getAllTerms() method
		Collections.sort( termsWithoutCourse );
		
		return termsWithoutCourse;
	}

	/* (non-Javadoc)
	 * @see DataQuery#removeTerm(Term)
	 */
	@Override
	public void removeTerm(Term term) throws IllegalArgumentException {
		// remove student from the term
		// check to make sure term is part of the systerm
		// if not throw IllegalArgumentException
		
		if ( findTerm( term.getTermID() ) == null )
		{
			throw new IllegalArgumentException();
		}
		
		myTerms.remove( myTerms.indexOf( term ) );

	}

	/**
	 * Will find the grade for the specific student in a specific course 
	 * from a specific term from the file allMyTermsInfo that is directly 
	 * read from the file 
	 * 
	 * If the term object in allMyTermsInfo has the correct termID, courseID
	 * and studentID, then get grade
	 * 
	 * @param term Term object that will be used for term identification number
	 * @param course Course object that will use course identification number
	 * @param student Student object that will be used for student 
	 * identification number
	 * @return
	 */
	private String findGradeForStudentInCourse( Term term, Course course, 
			Student student )
	{
		// find the term used for that grade, and the course and student
		// and search through allMyTermsInfo for the grade assigned
		for ( Term t : allMyTermsInfo )
		{
			if ( t.getTermID() == term.getTermID() && t.getCourseID() 
					== course.getCourseID() && t.getStudentID() 
					== student.getStudentID() )
			{
				return t.getGrade();
			}
		}
		return null;
	}
	/**
	 * Returns the reference to a specific term in the term list
	 * of myTerms
	 * @param tid term identification number
	 * @return
	 */
	private Term getSpecificTermInMyTermsList( int tid )
	{
		for ( Term t : myTerms )
		{
			if ( tid == t.getTermID() )
			{
				return t;
			}
		}
		return null;
	}

	/**
	 * Will return true if the term id is already in the 
	 * myTerms
	 * 
	 * Will return false if the term id is not in the myTerms
	 * @param tid termID
	 * @return boolean
	 */
	private boolean isTermIdInMyTermsList( int tid )
	{
		for ( Term t : myTerms )
		{
			if ( tid == t.getTermID() )
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * Program Description: Will be used to compare the different sessions
	 * of the term objects. It will follow the order of Fall, Summer, Spring,
	 * and then Winter.
	 *
	 * Date Last Modified: Sep 25, 2015
	 *
	 * @author: kalaarentz
	 */
	private class CompareDifferentSessions implements Comparator<Term>
	{

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, 
		 * java.lang.Object)
		 */
		@Override
		public int compare(Term o1, Term o2) {
			// Will compare the different terms to be in this order, Fall, 
			// Summer, Spring, Fall


			if ( o1.compareTo( o2 ) == 0 )
			{
				// if first term object session is Fall
				if ( o1.getTermSession().equals( "Fall" ) )
				{
					if ( o2.getTermSession().equals( "Fall" ) )
					{
						return 0;
					}
					else if ( o2.getTermSession().equals( "Summer" ) )
					{
						return -1;
					}
					else if ( o2.getTermSession().equals( "Winter" ) )
					{
						return -1;
					}
					else if ( o2.getTermSession().equals( "Spring" ) )
					{
						return -1;
					}
				}

				// if first term object session is Summer
				if ( o1.getTermSession().equals( "Summer" ) )
				{
					if ( o2.getTermSession().equals( "Fall" ) )
					{
						return 1;
					}
					else if ( o2.getTermSession().equals( "Summer" ) )
					{
						return 0;
					}
					else if ( o2.getTermSession().equals( "Winter" ) )
					{
						return -1;
					}
					else if ( o2.getTermSession().equals( "Spring" ) )
					{
						return -1;
					}
				}

				// if first term object session is Winter
				if ( o1.getTermSession().equals( "Winter" ) )
				{
					if ( o2.getTermSession().equals( "Fall" ) )
					{
						return 1;
					}
					else if ( o2.getTermSession().equals( "Summer" ) )
					{
						return 1;
					}
					else if ( o2.getTermSession().equals( "Winter" ) )
					{
						return 0;
					}
					else if ( o2.getTermSession().equals( "Spring" ) )
					{
						return -1;
					}
				}

				// if first term object session is spring
				if ( o1.getTermSession().equals( "Spring" ) )
				{
					if ( o2.getTermSession().equals( "Fall" ) )
					{
						return 1;
					}
					else if ( o2.getTermSession().equals( "Summer" ) )
					{
						return 1;
					}
					else if ( o2.getTermSession().equals( "Winter" ) )
					{
						return 1;
					}
					else if ( o2.getTermSession().equals( "Spring" ) )
					{
						return 0;
					}
				}
			}
			else 
			{
				return o1.compareTo( o2 );
			}
			return 0;

		}

	}
}


