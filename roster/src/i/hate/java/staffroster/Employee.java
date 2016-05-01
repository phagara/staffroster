package i.hate.java.staffroster;

import java.io.*;

public abstract class Employee implements Serializable {

	private static final long serialVersionUID = 8134983961842209955L;
	
	static final int sickMonthlySalary = 500;
	static int maxMonthlyWorkHours = 160;

	private int id;
	private String name;
	private String surname;
	private boolean sick = false;
	private int administrativeHours = 0;
	private int documentationHours = 0;
	private int developementHours = 0;

	public static void setMaxMonthlyWorkHours(int hours) {
		maxMonthlyWorkHours = hours;
	}

	abstract public String getJobTitle();
	abstract public int getHourlyWage();
	abstract public boolean canPerformAdministrative();
	abstract public boolean canPerformDocumentation();
	abstract public boolean canPerformDevelopement();
	
	public String toString() {
		String sickString = getSick() == true ? "yes" : "no";
		return "[" + getId() + "] " + getSurname() + " " + getName()
				+ " (" + getJobTitle() + ") " + getJobStats()
				+ " [sick: " + sickString + "]";
	}
	
	private String getJobStats() {
		return "{administrative: " + administrativeHours
				+ ", documentation: " + documentationHours
				+ ", developement: " + developementHours
				+ ", total: " + getMonthlyWorkHours() + "}";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public boolean getSick() {
		return sick;
	}

	public void setSick(boolean sick) {
		if (sick) {
			administrativeHours = 0;
			documentationHours = 0;
			developementHours = 0;
		}
		
		this.sick = sick;
	}

	public int getMonthlyWorkHours() {
		return administrativeHours + documentationHours + developementHours;
	}
	
	public int getAdministrativeHours() {
		return administrativeHours;
	}
	
	public int getDocumentationHours() {
		return documentationHours;
	}
	
	public int getDevelopementHours() {
		return developementHours;
	}

	public void setMonthlyWorkHours(int administrativeHours, int technicalWritingHours, int developementHours) throws CannotPerformException {		
		if (administrativeHours != 0 && (sick || !canPerformAdministrative())) throw new CannotPerformException("Cannot perform administrative work or sick!");
		if (technicalWritingHours != 0 && (sick || !canPerformDocumentation())) throw new CannotPerformException("Cannot perform documentation work or sick!");
		if (developementHours != 0 && (sick || !canPerformDevelopement())) throw new CannotPerformException("Cannot perform developement work or sick!");

		if (administrativeHours + technicalWritingHours + developementHours > maxMonthlyWorkHours) throw new CannotPerformException("Total employee hours would be higher than max allowed!");

		this.administrativeHours = administrativeHours;
		this.documentationHours = technicalWritingHours;
		this.developementHours = developementHours;
	}

}
