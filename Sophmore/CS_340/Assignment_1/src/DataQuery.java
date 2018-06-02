/**
 * Interface to the Registrar class.
 * Identifies the required operations and any constraints on their output.
 * 
 * Unless otherwise specified:
 *  - Strings are sorted lexicographically (A to Z)
 *  - Numbers are sorted in ascending order (0 to 99)
 * 
 * Date Last Modified: Sept. 9, 2015
 * @author J. Hursey
 */
import java.io.*;
import java.util.*;


public interface DataQuery {

	/* **************************************************
	 * Serializing and deserializing the data to/from text files.
	 * ************************************************** */
	/**
	 * Load the data into the system from the three text files specified.
	 * 
	 * @param studentFilename File containing student information
	 * @param courseFilename File containing course information
	 * @param termFilename File containing student/course/term information
	 * @throws IOException Thrown if any file does not exist or is in an 
	 * incorrect format.
	 */
	public void loadData(String studentFilename, String courseFilename, 
			String termFilename) throws IOException;

	/**
	 * Save the data from the system into the three text files specified.
	 * 
	 * @param studentFilename File containing student information
	 * @param courseFilename File containing course information
	 * @param termFilename File containing student/course/term information
	 * @throws IOException Thrown if cannot write to the specified files.
	 */
	public void saveData(String studentFilename, String courseFilename, 
			String termFilename) throws IOException;


	/* **************************************************
	 * Searching the Data
	 * ************************************************** */
	/**
	 * Get a list of all students
	 * If no students are in the system, then the list is empty (not null)
	 * List of students is sorted by:
	 *   1) Last name
	 *   2) First name
	 *   3) Username
	 *   4) Student ID
	 * 
	 * @return Sorted list of Students.
	 */
	public List<Student> getAllStudents();

	/**
	 * Get the Student object associated with this student ID
	 * 
	 * @param sid Student ID
	 * @return Student object if found, null if not found.
	 */
	public Student findStudent(int sid);


	/**
	 * Get a list of all courses
	 * If no courses are in the system, then the list is empty (not null)
	 * List of courses is sorted by:
	 *   1) Department
	 *   2) Course Number
	 *   3) Number of Credits
	 *   4) Course Name
	 *   5) Course ID
	 * 
	 * @return Sorted list of Courses.
	 */
	public List<Course> getAllCourses();

	/**
	 * Get the Course object associated with this course ID
	 * 
	 * @param cid Course ID
	 * @return Course object if found, null if not found.
	 */
	public Course findCourse(int cid);


	/**
	 * Get a list of all terms
	 * If no terms are in the system, then the list is empty (not null)
	 * List of terms is sorted by:
	 *   1) Year (descending order: 2015 then 2014 then 2013)
	 *   2) Session (descending order: Fall, Summer, Spring, Winter)
	 *   3) Term ID
	 *   
	 * For example:
	 *    Fall 2015
	 *  Summer 2015
	 *  Spring 2015
	 *  Winter 2015
	 *    Fall 2014 
	 *  Summer 2014
	 *  Spring 2014
	 *  Winter 2014
	 *
	 * @return Sorted list of Terms.
	 */
	public List<Term> getAllTerms();

	/**
	 * Get the Term object associated with this term ID
	 * 
	 * @param tid Term ID
	 * @return Term object if found, null if not found.
	 */
	public Term findTerm(int tid);

	/**
	 * Get the Term object associated with this year and session.
	 * 
	 * @param year Year of the term
	 * @param session Session of the term
	 * @return Term object if found, null if not found.
	 * @throws IllegalArgumentException If the session is not one of 
	 * "Fall", "Summer", "Winter", "Spring"
	 */
	public Term findTerm(int year, String session) 
		throws IllegalArgumentException;


	/**
	 * Get all students enrolled in a particular term along with their 
	 * credit load for that term.
	 * If no students are enrolled for this term, then the list is empty 
	 * (not null).
	 * List can be in any order.
	 * 
	 * Each element of the List will be an Object array of exactly two 
	 * elements:
	 *  Position 0: Reference to the Student object (Student)
	 *  Position 1: Number of credits for that student (Integer)
	 *  
	 * @param term Term to search
	 * @return List of Object arrays (see above).
	 * @throws IllegalArgumentException If the term object did not 
	 * originate from the system.
	 */
	public List<Object[]> getAllStudentsEnrolledInTerm(Term term) 
		throws IllegalArgumentException;

	/**
	 * Get all courses offered in a particular term along with the number of 
	 * students enrolled in each.
	 * If no courses are offered for this term, then the list is empty 
	 * (not null).
	 * List can be in any order.
	 * 
	 * Each element of the List will be an Object array of exactly two elements:
	 *  Position 0: Reference to the Course object (Course)
	 *  Position 1: Number of students enrolled in that course (Integer)
	 * 
	 * @param term Term to search
	 * @return List of Object arrays (see above).
	 * @throws IllegalArgumentException If the term object did not originate 
	 * from the system.
	 */
	public List<Object[]> getAllCoursesOfferedInTerm(Term term) 
		throws IllegalArgumentException;


	/**
	 * Get the class list for a student in a particular term.
	 * If the student is not taking any courses in this term, then the 
	 * list is empty (not null).
	 * List must be sorted the same as the getAllCourses() method.
	 * 
	 * @param term Term object to search
	 * @param student Student object to find
	 * @return List of sorted Course objects
	 * @throws IllegalArgumentException If the term or student object did 
	 * not originate from the system.
	 */
	public List<Course> getClassListForStudentEnrolledInTerm(Term term, 
			Student student) throws IllegalArgumentException;

	/**
	 * Get the class list for a course in a particular term.
	 * If the course is not offered in this term, then the list is 
	 * empty (not null).
	 * List must be sorted the same as the getAllStudents() method.
	 * 
	 * @param term Term object to search
	 * @param course Course object to find
	 * @return List of sorted Student objects
	 * @throws IllegalArgumentException If the term or course object did not 
	 * originate from the system.
	 */
	public List<Student> getClassListForCourseOfferedInTerm(Term term, 
			Course course) throws IllegalArgumentException;


	/**
	 * Get the course history for a particular student.
	 * If the student has not taken any courses, then the list 
	 * is empty (not null).
	 * List can be in any order.
	 * 
	 * Each element of the List will be an Object array of exactly 
	 * three elements:
	 *  Position 0: Reference to the Term object (Term)
	 *  Position 1: Reference to the Course object (Course)
	 *  Position 2: Grade assigned (String)
	 *  
	 * @param student Student to reference
	 * @return List of Object arrays (see above).
	 * @throws IllegalArgumentException If the student object did not 
	 * originate from the system.
	 */
	public List<Object[]> getCourseHistoryForStudent(Student student) 
		throws IllegalArgumentException;

	/**
	 * Get the enrollment history for a particular course.
	 * If the course has never been offered, then the list is empty (not null).
	 * List can be in any order.
	 * 
	 * Each element of the List will be an Object array of exactly 
	 * two elements:
	 *  Position 0: Reference to the Term object (Term)
	 *  Position 1: Number of students enrolled in the course for 
	 *  that term (Integer)
	 * 
	 * @param course Course to reference
	 * @return List of Object arrays (see above).
	 * @throws IllegalArgumentException If the course object did not 
	 * originate from the system.
	 */
	public List<Object[]> getEnrollmentHistoryForCourse(Course course) 
		throws IllegalArgumentException;

	/**
	 * For a given student, get the total number of credits taken per term.
	 * If the student has not taken any courses, then the list is empty 
	 * (not null).
	 * List can be in any order.
	 * 
	 * Each element of the List will be an Object array of exactly 
	 * two elements:
	 *  Position 0: Reference to the Term object (Term)
	 *  Position 1: Number of credits taken (Integer)
	 *  
	 * @param student Student to reference
	 * @return List of Object arrays (see above).
	 * @throws IllegalArgumentException If the student object did not 
	 * originate from the system.
	 */
	public List<Object[]> getCreditsPerTermForStudent(Student student) 
			throws IllegalArgumentException;


	/* **************************************************
	 * Modifying the Data
	 * ************************************************** */
	/**
	 * Add a student to the system.
	 * If an exception is thrown then the student is not added 
	 * to the system.
	 * 
	 * @param sid Student ID (must be unique in the system)
	 * @param lastName Last name
	 * @param firstName First name
	 * @param username Username (must be unique in the system)
	 * @return Reference to the Student object
	 * @throws Exception If the student already exists, or a student 
	 * with the same sid or username exists.
	 */
	public Student addStudent(int sid, String lastName, String firstName, 
			String username) throws Exception;

	/**
	 * Add a course to the system.
	 * If an exception is thrown then the course is not added to the system.
	 * 
	 * @param cid Course ID (must be unique in the system)
	 * @param dept Course department (e.g., CS) 
	 * @param courseNumber Course number (e.g., 340)
	 * @param credits Number of credits (e.g., 3)
	 * @param name Name of the course (Software Design III: Abstract 
	 * Data Types) 
	 * @param attrs Any additional attributes - empty string if none.
	 * @return Reference to the Course object
	 * @throws Exception If the course already exists, or a course with 
	 * the same cid exists.
	 */
	public Course addCourse(int cid, String dept, int courseNumber, 
			int credits, String name, String attrs) throws Exception;

	/**
	 * Add a term to the system.
	 * If an exception is thrown then the term is not added to the system.
	 * 
	 * @param tid Term ID (must be unique in the system)
	 * @param year Term year (e.g., 2015)
	 * @param session Term session. One of "Fall", "Summer", "Winter", "Spring"
	 * @return Reference to the Term object
	 * @throws Exception If the term already exists, or a term with the 
	 * same tid exists.
	 * @throws IllegalArgumentException if the session string is invalid.
	 */
	public Term addTerm(int tid, int year, String session) 
		throws Exception, IllegalArgumentException;

	/**
	 * Add a course to a term.
	 * 
	 * @param term Term to add to
	 * @param course Course to add
	 * @throws Exception If the term already contains the course.
	 * @throws IllegalArgumentException If the course or term objects 
	 * did not originate from the system.
	 */
	public void addCourseToTerm(Term term, Course course) 
		throws Exception, IllegalArgumentException;

	/**
	 * Add a student to a course in a term.
	 * 
	 * @param term Term to add to
	 * @param course Course to add to
	 * @param student Student to add
	 * @throws Exception If the course/term combination does not exist,
	 *  or if the student is already associated with the course/term.
	 * @throws IllegalArgumentException If the term/course/student object 
	 * did not originate from the system.
	 */
	public void addStudentToCourseInTerm(Term term, Course course, 
			Student student) throws Exception, IllegalArgumentException;

	/**
	 * Add a grade for a student in a course in a term.
	 * Requires that there not be a grade associated with the 
	 * student/course/term combination.
	 * 
	 * @param term Term reference
	 * @param course Course reference
	 * @param student Student reference
	 * @param grade Grade to add
	 * @throws Exception If the student/course/term combination does not exist,
	 * or if the student already has a grade for this course/term.
	 * @throws IllegalArgumentException If the term/course/student object 
	 * did not originate from the system.
	 */
	public void addGradeForStudent(Term term, Course course, Student student, 
			String grade)  throws Exception, IllegalArgumentException;


	/**
	 * Update a grade for a student in a course in a term.
	 * Requires that there be a grade associated with the 
	 * student/course/term combination.
	 * 
	 * @param term Term reference
	 * @param course Course reference
	 * @param student Student reference
	 * @param grade Grade to add
	 * @throws Exception If the student/course/term combination does not exist, 
	 * or if the student does not have a grade for this course/term.
	 * @throws IllegalArgumentException If the term/course/student object did 
	 * not originate from the system.
	 */
	public void updateGradeForStudent(Term term, Course course, 
			Student student, String grade) 
		throws Exception, IllegalArgumentException;


	/**
	 * Remove a student from a course in a given term.
	 * 
	 * @param term Term reference
	 * @param course Course reference
	 * @param student Student reference
	 * @throws IllegalArgumentException If the term/course/student 
	 * object did not originate from the system.
	 */
	public void removeStudentFromCourseInTerm(Term term, 
			Course course, Student student) throws IllegalArgumentException;

	/**
	 * Remove a student from all courses in a term.
	 * If the student is not enrolled in this term, then the list 
	 * is empty (not null).
	 * List must be sorted the same as the getAllCourses() method.
	 *
	 * @param term Term reference
	 * @param student Course reference
	 * @return List of sorted Course objects
	 * @throws IllegalArgumentException If the term/student object did
	 *  not originate from the system.
	 */
	public List<Course> removeStudentFromAllCoursesInTerm(Term term, 
			Student student) throws IllegalArgumentException;

	/**
	 * Remove a student from the system.
	 * 
	 * Note that the Student object is no longer valid in the system 
	 * upon successful completion of this method.
	 * So it is not able to be used in a subsequent call to, 
	 * for example, getCourseHistoryForStudent().
	 * 
	 * If the student is not enrolled in any term, then the list 
	 * is empty (not null).
	 * List must be sorted the same as the getAllTerms() method.
	 * 
	 * @param student Student reference
	 * @return List of sorted Term objects
	 * @throws IllegalArgumentException If the student object 
	 * did not originate from the system.
	 */
	public List<Term> removeStudent(Student student)
		throws IllegalArgumentException;

	/**
	 * Remove a course from a term. Removes all references to
	 *  this course in the system.
	 * 
	 * Note that the Course object is no longer valid in the 
	 * system upon successful completion of this method.
	 * So it is not able to be used in a subsequent call to, 
	 * for example, getEnrollmentHistoryForCourse().
	 * 
	 * If no students where enrolled in this course, then the
	 *  list is empty (not null).
	 * List must be sorted the same as the getAllStudents() method.
	 * 
	 * @param term Term reference
	 * @param course Course reference
	 * @return List of sorted Student objects
	 * @throws Exception If the course is not offered in this term
	 * @throws IllegalArgumentException If the term/course object
	 *  did not originate from the system.
	 */
	public List<Student> removeCourseFromTerm(Term term, Course course) 
		throws Exception, IllegalArgumentException;

	/**
	 * Remove a course from the system. Removes all references to 
	 * this course in the system.
	 * 
	 * Note that the Course object is no longer valid in the system 
	 * upon successful completion of this method.
	 * So it is not able to be used in a subsequent call to, for example, 
	 * getEnrollmentHistoryForCourse().
	 * 
	 * If the course was not offered in any term, then the list is 
	 * empty (not null).
	 * List must be sorted the same as the getAllTerms() method.
	 * 
	 * @param course Course reference
	 * @return List of sorted Term objects
	 * @throws IllegalArgumentException If the course object did not 
	 * originate from the system.
	 */
	public List<Term> removeCourse(Course course)
		throws IllegalArgumentException;

	/**
	 * Remove a term from the system. Removes all references to 
	 * this term in the system.
	 * 
	 * Note that the Term object is no longer valid in the system 
	 * upon successful completion of this method.
	 * So it is not able to be used in a subsequent call to, for 
	 * example, getAllStudentsEnrolledInTerm().
	 * 
	 * @param term Term reference
	 * @throws IllegalArgumentException If the term object did 
	 * not originate from the system.
	 */
	public void removeTerm(Term term) throws IllegalArgumentException;
}
