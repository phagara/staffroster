package i.hate.java.staffroster;

import java.util.*;
import java.io.*;


public class StaffRoster implements Serializable {

	private static final long serialVersionUID = 7768192078710327538L;
	
	private List<Employee> employees;
	private int administrativeHours = 0;
	private int documentationHours = 0;
	private int developementHours = 0;
	
	StaffRoster() {
		employees = new ArrayList<>();
	}
	
	// a)
	public void addEmployee(String position, int id, String name, String surname) throws TooMuchWorkException, NoSuchPositionException, AlreadyHasCeoException {
		Employee e;
		
		switch (position) {
			case "assistant":
				e = new Assistant();
				break;
			case "technicalwriter":
				e = new TechnicalWriter();
				break;
			case "developer":
				e = new Developer();
				break;
			case "ceo":
				e = new CEO();
				break;
			default:
				throw new NoSuchPositionException("Work position `" + position + "` does not exist!");
		}
		
		e.setId(id);
		e.setName(name);
		e.setSurname(surname);
		
		if (e.getJobTitle().equals("CEO") && rosterHasCeo()) throw new AlreadyHasCeoException("Company already has a CEO!");
		
		employees.add(e);
		recalculateWorkHours();
	}

	// b) + c)
	public void modifyWork(String workType, int hoursPerMonth) throws TooMuchWorkException, NoSuchWorkException, NegativeWorkException {
		switch (workType) {
			case "administrative":
				if (administrativeHours + hoursPerMonth < 0) throw new NegativeWorkException("Hours worked per month can not be negative!");
				administrativeHours += hoursPerMonth;
				break;
			case "documentation":
				if (documentationHours + hoursPerMonth < 0) throw new NegativeWorkException("Hours worked per month can not be negative!");
				documentationHours += hoursPerMonth;
				break;
			case "developement":
				if (developementHours + hoursPerMonth < 0) throw new NegativeWorkException("Hours worked per month can not be negative!");
				developementHours += hoursPerMonth;
				break;
			default:
				throw new NoSuchWorkException("Work type `" + workType + "` does not exist!");
		}
		recalculateWorkHours();
	}
	
	// d)
	public void fireEmployee(int id) throws NonExistingEmployeeException, TooMuchWorkException {
		Employee e = findEmployeeById(id);
		int hadMonthyWorkHours = e.getMonthlyWorkHours();
		employees.remove(e);
		if (hadMonthyWorkHours != 0) {
			recalculateWorkHours();
		}
	}
	
	// e) + f)
	public void setSickEmployee(int id, boolean sick) throws NonExistingEmployeeException, TooMuchWorkException {
		Employee e = findEmployeeById(id);
		int hadMonthyWorkHours = e.getMonthlyWorkHours();
		e.setSick(sick);
		if (hadMonthyWorkHours != 0) {
			recalculateWorkHours();
		}
	}
	
	// g)
	public void setMaxMonthlyWorkHours(int hours) throws TooMuchWorkException {
		Employee.setMaxMonthlyWorkHours(hours);
		recalculateWorkHours();
	}
	
	// h)
	public String numEmployeesByPosition() throws NoSuchPositionException {
		int assistants = 0;
		int technicalWriters = 0;
		int developers = 0;
		int ceos = 0;
		
		for (Employee e : employees) {
			switch (e.getJobTitle()) {
				case "Assistant":
					assistants++;
					break;
				case "Technical Writer":
					technicalWriters++;
					break;
				case "Developer":
					developers++;
					break;
				case "CEO":
					ceos++;
					break;
				default:
					throw new NoSuchPositionException("Work position `" + e.getJobTitle() + "` does not exist!");
			}
		}
		
		return "{assistants: " + assistants + " ( " + getPosFreeHours("Assistant") + "h free)"
				+ ", technicalWriters: " + technicalWriters + " (" + getPosFreeHours("Technical Writer") + "h free)"
				+ ", developers: " + developers + " (" + getPosFreeHours("Developer") + "h free)"
				+ ", CEOs: " + ceos + " (" + getPosFreeHours("CEO") + "h free)" + "}";
	}
	
	// i)
	public int getTotalMonthlyWages() {
		int total = 0;
		for (Employee e : employees) {
			total += e.getMonthlyWorkHours() * e.getHourlyWage();
		}
		return total;
	}
	
	// j) by ID
	public String[] getListOfEmployeesById() {
		Collections.sort(employees, new Comparator<Employee>() {
			@Override
			public int compare(Employee e1, Employee e2) {
				return e1.getId() - e2.getId();
			}
		});
		return dumpEmployees();
	}
	
	// j) by surname
	public String[] getListOfEmployeesBySurname() {
		Collections.sort(employees, new Comparator<Employee>() {
			@Override
			public int compare(Employee e1, Employee e2) {
				return e1.getSurname().compareTo(e2.getSurname());
			}
		});
		return dumpEmployees();
	}
	
	private int getPosFreeHours(String position) {
		int free = 0;
		for (Employee e : employees) {
			if (e.getJobTitle().equals(position) && !e.getSick()) {
				free += Employee.maxMonthlyWorkHours - e.getMonthlyWorkHours();
			}
		}
		return free;
	}
	
	private Employee findEmployeeWithFreeHours(String position) {
		for (Employee e : employees) {
			if (e.getJobTitle().equals(position) && e.getMonthlyWorkHours() < Employee.maxMonthlyWorkHours && !e.getSick()) {
				return e;
			}
		}
		return null;
	}
	
	private void recalculateWorkHours() throws TooMuchWorkException {
		for (Employee e : employees) {
			try {
				e.setMonthlyWorkHours(0, 0, 0);
			} catch (CannotPerformException ex) {
				// cannot happen as we're setting all to zero
				ex.printStackTrace();
			}
		}
		
		int adminHoursLeft = administrativeHours;
		int docHoursLeft = documentationHours;
		int devHoursLeft = developementHours;
		
		// ADMINISTRATIVE WORK
		// assistant is cheapest for administrative work
		while (adminHoursLeft > 0 && getPosFreeHours("Assistant") > 0) {
			Employee e = findEmployeeWithFreeHours("Assistant");  // cannot be null
			int howLong = Math.min(adminHoursLeft, Employee.maxMonthlyWorkHours - e.getMonthlyWorkHours());
			try {
				e.setMonthlyWorkHours(e.getAdministrativeHours() + howLong, e.getDocumentationHours(), e.getDevelopementHours());
			} catch (CannotPerformException ex) {
				// cannot happen
				ex.printStackTrace();
			}
			adminHoursLeft -= howLong;
		}
		
		// try technical writer next
		while (adminHoursLeft > 0 && getPosFreeHours("Technical Writer") > 0) {
			Employee e = findEmployeeWithFreeHours("Technical Writer");  // cannot be null
			int howLong = Math.min(adminHoursLeft, Employee.maxMonthlyWorkHours - e.getMonthlyWorkHours());
			try {
				e.setMonthlyWorkHours(e.getAdministrativeHours() + howLong, e.getDocumentationHours(), e.getDevelopementHours());
			} catch (CannotPerformException ex) {
				// cannot happen
				ex.printStackTrace();
			}
			adminHoursLeft -= howLong;
		}
			
		// CEO as a last resort
		while (adminHoursLeft > 0 && getPosFreeHours("CEO") > 0) {
			Employee e = findEmployeeWithFreeHours("CEO");  // cannot be null
			int howLong = Math.min(adminHoursLeft, Employee.maxMonthlyWorkHours - e.getMonthlyWorkHours());
			try {
				e.setMonthlyWorkHours(e.getAdministrativeHours() + howLong, e.getDocumentationHours(), e.getDevelopementHours());
			} catch (CannotPerformException ex) {
				// cannot happen
				ex.printStackTrace();
			}
			adminHoursLeft -= howLong;
		}
		
		if (adminHoursLeft > 0) throw new TooMuchWorkException("Not enough employees for administrative work!");
		
		// DOCUMENTATION WORK
		// technical writer is cheapest for documentation work
		while (docHoursLeft > 0 && getPosFreeHours("Technical Writer") > 0) {
			Employee e = findEmployeeWithFreeHours("Technical Writer");  // cannot be null
			int howLong = Math.min(docHoursLeft, Employee.maxMonthlyWorkHours - e.getMonthlyWorkHours());
			try {
				e.setMonthlyWorkHours(e.getAdministrativeHours(), e.getDocumentationHours() + howLong, e.getDevelopementHours());
			} catch (CannotPerformException ex) {
				// cannot happen
				ex.printStackTrace();
			}
			docHoursLeft -= howLong;
		}
		
		// try developer next
		while (docHoursLeft > 0 && getPosFreeHours("Developer") > 0) {
			Employee e = findEmployeeWithFreeHours("Developer");  // cannot be null
			int howLong = Math.min(docHoursLeft, Employee.maxMonthlyWorkHours - e.getMonthlyWorkHours());
			try {
				e.setMonthlyWorkHours(e.getAdministrativeHours(), e.getDocumentationHours() + howLong, e.getDevelopementHours());
			} catch (CannotPerformException ex) {
				// cannot happen
				ex.printStackTrace();
			}
			docHoursLeft -= howLong;
		}
		
		// CEO as the last resort
		while (docHoursLeft > 0 && getPosFreeHours("CEO") > 0) {
			Employee e = findEmployeeWithFreeHours("CEO");  // cannot be null
			int howLong = Math.min(docHoursLeft, Employee.maxMonthlyWorkHours - e.getMonthlyWorkHours());
			try {
				e.setMonthlyWorkHours(e.getAdministrativeHours(), e.getDocumentationHours() + howLong, e.getDevelopementHours());
			} catch (CannotPerformException ex) {
				// cannot happen
				ex.printStackTrace();
			}
			docHoursLeft -= howLong;
		}
		
		if (docHoursLeft > 0) throw new TooMuchWorkException("Not enough employees for documentation work!");
		
		// DEVELOPEMENT WORK
		// developer is cheapest for developement work
		while (devHoursLeft > 0 && getPosFreeHours("Developer") > 0) {
			Employee e = findEmployeeWithFreeHours("Developer");  // cannot be null
			int howLong = Math.min(devHoursLeft, Employee.maxMonthlyWorkHours - e.getMonthlyWorkHours());
			try {
				e.setMonthlyWorkHours(e.getAdministrativeHours(), e.getDocumentationHours(), e.getDevelopementHours() + howLong);
			} catch (CannotPerformException ex) {
				// cannot happen
				ex.printStackTrace();
			}
			devHoursLeft -= howLong;
		}
		
		// CEO as the last resort
		while (devHoursLeft > 0 && getPosFreeHours("CEO") > 0) {
			Employee e = findEmployeeWithFreeHours("CEO");  // cannot be null
			int howLong = Math.min(devHoursLeft, Employee.maxMonthlyWorkHours - e.getMonthlyWorkHours());
			try {
				e.setMonthlyWorkHours(e.getAdministrativeHours(), e.getDocumentationHours(), e.getDevelopementHours() + howLong);
			} catch (CannotPerformException ex) {
				// cannot happen
				ex.printStackTrace();
			}
			devHoursLeft -= howLong;
		}
		
		if (devHoursLeft > 0) throw new TooMuchWorkException("Not enough employees for developement work!");
		
		return;
	}
	
	private boolean rosterHasCeo() {
		for (Employee e : employees) {
			if (e.getJobTitle().equals("CEO")) return true;
		}
		return false;
	}
	
	private Employee findEmployeeById(int id) throws NonExistingEmployeeException {
		for (Employee e : employees) {
			if (e.getId() == id) {
				return e;
			}
		}
		throw new NonExistingEmployeeException("Employee with ID " + id + " does not exist!");
	}
	
	private String[] dumpEmployees() {
		String[] ret = new String[employees.size()];
		int i = 0;
		for (Employee e : employees) {
			ret[i] = e.toString();
			i++;
		}
		return ret;
	}
	
}