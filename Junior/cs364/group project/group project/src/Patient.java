

public class Patient {

    public static final String SSN = "ssn";
    public static final String FIRST_NAME = "firstName";
    public static final String MIDDLE_NAME = "middleName";
    public static final String LAST_NAME = "lastName";
    public static final String DATE_OF_BIRTH = "dateOfBirth";
    public static final String INSURANCE_NAME = "insuranceName";
    public static final String ADDRESS = "address";

    // JDBC keys
    public static final String SSN_JDBC_KEY = "SSN";
    public static final String FNAME_JDBC_KEY = "fName";
    public static final String MNAME_JDBC_KEY = "mName";
    public static final String LNAME_JDBC_KEY = "lName";
    public static final String DOB_JDBC_KEY = "dateOfBirth";
    public static final String INSURANCE_JDBC_KEY = "insuranceName";
    public static final String ADDRESS_JDBC_KEY = "address";

    private String ssn;
    private String firstName;
    private String middleName;
    private String lastName;
    private String dateOfBirth;
    private String insuranceName;
    private String address;

    public Patient() {

    }

    public Patient(String ssn, String firstName, String middleName, String lastName, String dateOfBirth, String insuranceName,
                   String address) {
        this.ssn = ssn;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.insuranceName = insuranceName;
        this.address = address;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}

