import java.sql.Connection;

public class DrugzLLC {

	public static void main(String[] args) {
		// connect to the database
		Connection connect = DrugzLLCJDBCToolbox.connect("jdbc:sqlite:/Users/kalaarentz/Desktop/cs364/Pharmacy.db");
		Patient pat = new Patient("77893", "Kelly", "Rose", "Smith", "04/18/2015", "Hello", "La Crosse");
		Doctor d = new Doctor("344595","La Crosse", "Dr. Richard");
		
		DrugzLLCJDBCToolbox.insertIntoDoctor(connect, d);
		
		DrugzLLCJDBCToolbox.insertIntoPatient(connect,pat);
		
		//disconnect from the database
		DrugzLLCJDBCToolbox.disconnect(connect);

	}

}
