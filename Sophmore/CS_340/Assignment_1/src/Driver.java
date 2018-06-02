/**
 * Program Description: Will use this to test my registrar class and all
 * the classes the work with registrar
 *
 * Date Last Modified: Sep 25, 2015
 *
 * @author: kalaarentz
 */
import java.util.List;
import java.io.IOException;


public class Driver {

	/**
	 * This be the part of the class that will call certain methods
	 * to help test out registrar class
	 * @param args
	 */
	public static void main(String[] args) throws IOException  {

		Driver d = new Driver( );

		//d.printSpecificLists( 1 );
		//d.printSpecificLists( 2 );
		d.printSpecificLists( 3 );
		//d.printSpecificLists( 4 );

		//d.practiceWrite( );

		//d.findSpecificItem();


	}

	/**
	 *  Will iterate through specific lists of information such as 
	 *  Students, Courses, and Terms
	 *  
	 * @param idx for specific for each loop 
	 * @throws IOException
	 */
	public void printSpecificLists( int idx  ) throws IOException 
	{
		System.out.println( "_____________________________________"
				+ "________________" );
		Registrar reg = new Registrar();
		reg.loadData( "src/medium-students.txt", "src/medium-courses.txt",
				"src/medium-enrollment.txt" );

		List<Student> allStudents = reg.getAllStudents();
		List<Course> allCourses = reg.getAllCourses();
		List<Term> allTerms = reg.getAllTerms();

		if ( idx == 1 )
		{
			for ( Student s : allStudents )
			{
				System.out.println( s );
			}
		}
		else if ( idx == 2 )
		{
			for ( Course c : allCourses )
			{
				System.out.println( c );
			}
		}
		else if ( idx == 3 )
		{
			for ( Term t : allTerms )
			{
				System.out.println( t );
			}
		}
		else if ( idx == 4 )
		{
			for ( Term t : allTerms )
			{
				System.out.println( t.getAllStudentsEnrolled() );
			}
		}

	}




}
