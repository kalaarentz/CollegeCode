

public class Prescription {

    public static final String RX = "rx";
    public static final String NAME = "name";
    public static final String NUMBER_SUPPLIED = "numberSupplied";
    public static final String NUMBER_REFILLS = "numberOfRefills";
    public static final String SIDE_EFFECTS = "sideEffects";

    // JDBC keys
    public static final String RX_JDBC_KEY = "RX";
    public static final String NAME_JDBC_KEY = "name";
    public static final String NUM_SUPPLIED_JDBC_KEY = "numberSupplied";
    public static final String NUM_REFILLS_JDBC_KEY = "numberOfRefills";
    public static final String SIDE_EFFECTS_JDBC_KEY = "sideEffects";

    private int rx;
    private String name;
    private int numberSupplied;
    private int numberOfRefills;
    private String sideEffects;

    public Prescription() {

    }

    public Prescription(int rx, String name, int numberSupplied, int numberOfRefills, String sideEffects) {
        this.rx = rx;
        this.name = name;
        this.numberSupplied = numberSupplied;
        this.numberOfRefills = numberOfRefills;
        this.sideEffects = sideEffects;
    }

    public int getNumberOfRefills() {
        return numberOfRefills;
    }

    public void setNumberOfRefills(int numberOfRefills) {
        this.numberOfRefills = numberOfRefills;
    }

    public int getRx() {
        return rx;
    }

    public void setRx(int rx) {
        this.rx = rx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberSupplied() {
        return numberSupplied;
    }

    public void setNumberSupplied(int numberSupplied) {
        this.numberSupplied = numberSupplied;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

}

