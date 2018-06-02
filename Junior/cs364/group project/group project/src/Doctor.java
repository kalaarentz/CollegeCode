

public class Doctor {

    public static final String ID = "id";
    public static final String LOCATION = "location";
    public static final String NAME = "name";

    // JDBC keys
    public static final String ID_JDBC_KEY = "ID";
    public static final String LOCATION_JDBC_KEY = "location";
    public static final String NAME_JDBC_KEY = "name";

    private String id;
    private String location;
    private String name;

    public Doctor() {

    }

    public Doctor(String id, String location, String name) {
        this.id = id;
        this.location = location;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
