package DrugzLLC;

import DrugzLLC.Tables.*;

import java.sql.*;
import java.util.ArrayList;

public class JDBCTools {

    protected static Connection connect(String databaseLocation) {
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

    protected static void disconnect(Connection connection) {
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
            preparedStatement.setString(1, doctor.getId());
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

    public static boolean insertIntoSee(Connection connection, See see) {
        String statementString = "INSERT INTO see VALUES (?,?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(statementString);
            preparedStatement.setString(1, see.getId());
            preparedStatement.setString(2, see.getsSN());

            // execute insert SQL statement
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insertIntoHave(Connection connection, Have have) {
        String statementString = "INSERT INTO have VALUES (?,?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(statementString);
            preparedStatement.setString(1, have.getsSN());
            preparedStatement.setInt(2, have.getrX());

            // execute insert SQL statement
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean insertIntoPrescripe(Connection connection, Prescribe prescribe) {
        String statementString = "INSERT INTO prescribe VALUES (?,?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(statementString);
            preparedStatement.setInt(1, prescribe.getrX());
            preparedStatement.setInt(2, prescribe.getiD());

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
            preparedStatement.setString(1, patient.getSsn());
            preparedStatement.setString(2, patient.getFirstName());
            preparedStatement.setString(3, patient.getMiddleName());
            preparedStatement.setString(4, patient.getLastName());
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
            preparedStatement.setInt(1, prescription.getRx());
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
    public static boolean deleteFromDoctor(Connection connection, String iD) {
        String statementString = "DELETE From Doctors WHERE ID = ?";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(statementString);
            preparedStatement.setString(1, iD);

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteFromPatient(Connection connection, String sSN) {
        String statementString = "DELETE From Patients WHERE SSN = ?";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(statementString);
            preparedStatement.setString(1, sSN);

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteFromPrescription(Connection connection, int rX) {
        String statementString = "DELETE From Prescriptions WHERE RX = ?";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(statementString);
            preparedStatement.setInt(1, rX);

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //////////////////////////////////////////////////////////////////////
    //                         update statements                        //
    //////////////////////////////////////////////////////////////////////
    public static boolean updateFrom(Connection connection, String tableName, ArrayList<String> attributes, ArrayList<String> values, String condition) {
        String setUpdate = createValuesStringFromAttributes(attributes, values, ",");
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

    private static String createValuesStringFromAttributes(ArrayList<String> attributes, ArrayList<String> values, String separator) {
        if (attributes.size() != values.size()) {
            throw new IllegalArgumentException("Attributes and values must be parallel arrays.");
        }
        StringBuilder setUpdateSB = new StringBuilder();
        for (int i = 0; i < attributes.size(); i++) {
            setUpdateSB.append(attributes.get(i));
            setUpdateSB.append(" = ");
            setUpdateSB.append("'");
            setUpdateSB.append(values.get(i));
            setUpdateSB.append("'");
            // last attribute does not need comma
            if (i != attributes.size() - 1) {
                setUpdateSB.append(separator);
            }
        }
        return setUpdateSB.toString();
    }

    public static ResultSet searchFromIfAvailable(Connection connection, String tableName, ArrayList<String> attributes, ArrayList<String> values) {
        if (attributes.isEmpty()) {
            return null;
        }
        String whereValues = createValuesStringFromAttributes(attributes, values, " AND ");
        String queryStr = "SELECT * FROM " + tableName + " WHERE " + whereValues;

        ResultSet resultSet = null;
        PreparedStatement query;
        try {
            query = connection.prepareStatement(queryStr);
            resultSet = query.executeQuery();
            if (resultSet.next()) {
                query = connection.prepareStatement(queryStr);
                resultSet = query.executeQuery();
            } else {
                resultSet = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    //////////////////////////////////////////////////////////////////////
    //                         other                                    //
    //////////////////////////////////////////////////////////////////////
    public static ResultSet retrieveAllItems(Connection connect, String tableName) {
        String queryStr = "SELECT * FROM " + tableName;

        ResultSet resultSet = null;

        PreparedStatement query;
        try {
            query = connect.prepareStatement(queryStr);
            resultSet = query.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    public static boolean isItemInTable(Connection connect, String table, String keyForItem, String item) {
        String queryStr = "SELECT * FROM " + table + " WHERE " + keyForItem + " = ?";

        ResultSet resultSet = null;

        PreparedStatement query;
        try {
            query = connect.prepareStatement(queryStr);
            query.setString(1, item);

            resultSet = query.executeQuery();

            if(resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ResultSet getResultSetInDB(Connection connect, String table, String keyForItem, String item) {
        String queryStr = "SELECT * FROM "+ table + " WHERE "+ keyForItem + " = ?";
        ResultSet resultSet = null;

        PreparedStatement query;
        try {
            query = connect.prepareStatement(queryStr);
            query.setString(1, item);
            resultSet = query.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static ResultSet getResultSetNaturalJoinInDB(Connection connect, String mainTable, String relationshipTable, String keyForItem, String item) {
        String queryStr = "SELECT * FROM " + mainTable + " NATURAL JOIN " + relationshipTable + " WHERE " + relationshipTable + "." + keyForItem + " = ?";
        ResultSet resultSet = null;

        PreparedStatement query;
        try {
            query = connect.prepareStatement(queryStr);
            query.setString(1, item);
            resultSet = query.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

}
