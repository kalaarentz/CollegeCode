import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DrugzLLCJDBCToolbox {

    public static Connection connect(String databaseLocation) {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(databaseLocation);
            System.out.println("Connection successful!!");
            System.out.println();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void disconnect(Connection connection) {
        try {
            connection.close();
            System.out.println();
            System.out.println("Connection closed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //////////////////////////////////////////////////////////////////////
    //                         insert statements                        //
    //////////////////////////////////////////////////////////////////////
    public static boolean insertIntoDoctor(Connection connection, Doctor doctor) {
        String statementString = "INSERT INTO Doctors VALUES (?,?,?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(statementString);
            preparedStatement.setString(1, doctor.getID());
            preparedStatement.setString(2, doctor.getLocation());
            preparedStatement.setString(3, doctor.getName());

            // execute insert SQL statement
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insertIntoPatient(Connection connection, Patient patient) {
        String statementString = "INSERT INTO Patients VALUES (?,?,?,?,?,?,?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(statementString);
            preparedStatement.setString(1, patient.getsSN());
            preparedStatement.setString(2, patient.getfName());
            preparedStatement.setString(3, patient.getmName());
            preparedStatement.setString(4, patient.getlName());
            preparedStatement.setString(5, patient.getDateOfBirth());
            preparedStatement.setString(6, patient.getInsuranceName());
            preparedStatement.setString(7, patient.getAddress());

            // execute insert SQL statement
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insertIntoPrescription(Connection connection, Prescription prescription) {
        String statementString = "INSERT INTO Prescriptions VALUES (?,?,?,?,?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(statementString);
            preparedStatement.setInt(1, prescription.getrX());
            preparedStatement.setString(2, prescription.getName());
            preparedStatement.setInt(3, prescription.getNumberSupplied());
            preparedStatement.setInt(4, prescription.getNumberOfRefills());
            preparedStatement.setString(5, prescription.getSideEffects());

            // execute insert SQL statement
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //////////////////////////////////////////////////////////////////////
    //                         delete statements                        //
    //////////////////////////////////////////////////////////////////////
    public static boolean deleteFromDoctor(Connection connection, int iD) {
        String statementString = "DELETE From Doctor WHERE ID = ?";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(statementString);
            preparedStatement.setInt(1, iD);

            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteFromPatient(Connection connection, String sSN) {
        String statementString = "DELETE From Patient WHERE SSN = ?";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(statementString);
            preparedStatement.setString(1, sSN);

            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteFromPrescription(Connection connection, int rX) {
        String statementString = "DELETE From Prescription WHERE RX = ?";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(statementString);
            preparedStatement.setInt(1, rX);

            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //////////////////////////////////////////////////////////////////////
    //                         update statements                        //
    //////////////////////////////////////////////////////////////////////
    public static boolean updateFrom(Connection connection, String tableName, String[] attributes, String[] values, String condition) {
        String setUpdate = createSetUpdateSB(attributes, values);
        String statementString = "UPDATE " + tableName + " SET " + setUpdate + " WHERE " + condition;

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(statementString);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String createSetUpdateSB(String[] attributes, String[] values) {
        if (attributes.length != values.length) {
            throw new IllegalArgumentException("Attributes and values must be parallel arrays.");
        }
        StringBuilder setUpdateSB = new StringBuilder();
        for (int i = 0; i < attributes.length; i++) {
            setUpdateSB.append(attributes[i]);
            setUpdateSB.append(" = ");
            setUpdateSB.append("'");
            setUpdateSB.append(values[i]);
            setUpdateSB.append("'");
            // last attribute does not need comma
            if (i != attributes.length - 1) {
                setUpdateSB.append(",");
            }
        }
        return setUpdateSB.toString();
    }

}