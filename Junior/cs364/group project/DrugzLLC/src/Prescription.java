
public class Prescription {
	private int rX, numberSupplied, numberOfRefills;
	private String name, sideEffects;
	
	public Prescription(int rX, int numberSupplied, int numberOfRefills,
			String name, String sideEffects) {
		super();
		this.rX = rX;
		this.numberSupplied = numberSupplied;
		this.numberOfRefills = numberOfRefills;
		this.name = name;
		this.sideEffects = sideEffects;
	}
	
	public int getrX() {
		return rX;
	}
	public void setrX(int rX) {
		this.rX = rX;
	}
	public int getNumberSupplied() {
		return numberSupplied;
	}
	public void setNumberSupplied(int numberSupplied) {
		this.numberSupplied = numberSupplied;
	}
	public int getNumberOfRefills() {
		return numberOfRefills;
	}
	public void setNumberOfRefills(int numberOfRefills) {
		this.numberOfRefills = numberOfRefills;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSideEffects() {
		return sideEffects;
	}
	public void setSideEffects(String sideEffects) {
		this.sideEffects = sideEffects;
	}

	

}
