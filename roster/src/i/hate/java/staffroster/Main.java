package i.hate.java.staffroster;

import java.io.*;
import java.util.*;


public class Main {

	private static StaffRoster roster = new StaffRoster();
	
	public static void main(String[] args) {
        	// https://www.youtube.com/watch?v=-ZI5eiggWbQ
		System.out.println("Available commands:");
		System.out.println("  * hire             adds a new employee");
		System.out.println("  * work             changes the amount of specific work type performed per month");
		System.out.println("  * fire             removes an employee");
		System.out.println("  * sick             marks an employee as unable to work");
		System.out.println("  * heal             marks an employee as able to work");
		System.out.println("  * max              sets the max number of hours worked per month for all employees");
		System.out.println("  * free             shows unallocated time per position");
		System.out.println("  * upkeep           amount spent on wages monthly");
		System.out.println("  * listbyid         lists employees sorted by ID");
		System.out.println("  * listbysurname    lists employees sorted by surname");
		System.out.println("  * save             saves staff roster into a file");
		System.out.println("  * load             loads staff roster from a file");
		System.out.println("  * quit             exits the application");
		System.out.println("");
		
		Scanner sc = new Scanner(System.in);
		System.out.print("Action? ");
		String action;
		
		while (sc.hasNext()) {
			action = sc.next();
			
			switch (action) {
				// a)
				case "hire":
					try {
						addEmployee(sc);
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
					break;
				// b) + c)
				case "work":
					try {
						modWork(sc);
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
					break;
				// d)
				case "fire":
					try {
						fire(sc);
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
					break;
				// e)
				case "sick":
					try {
						sick(sc);
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
					break;
				// f)
				case "heal":
					try {
						heal(sc);
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
					break;
				// g)
				case "max":
					try {
						maxWork(sc);
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
					break;
				// h)
				case "free":
					try {
						free(sc);
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
					break;
				// i)
				case "upkeep":
					try {
						upkeep(sc);
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
					break;
				// j)
				case "listbyid":
					try {
						listById(sc);
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
					break;
				// j)
				case "listbysurname":
					try {
						listBySurname(sc);
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
					break;
				// k)
				case "save":
					try {
						saveRoster(sc);
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
					break;
				// l)
				case "load":
					try {
						loadRoster(sc);
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
					break;
				case "quit":
					sc.close();
					System.exit(0);
				default:
					System.out.println("Invalid command!");
					break;
			}
			
			System.out.print("Action? ");
		}
		
		sc.close();
	}
	
	// a)
	private static void addEmployee(Scanner sc) throws Exception {
		String position, name, surname;
		int id;
		
		System.out.print("Position? [assistant/technicalwriter/developer/ceo] ");
		if (sc.hasNext()) position = sc.next();
		else throw new Exception();
		
		System.out.print("Id? ");
		if (sc.hasNextInt()) id = sc.nextInt();
		else throw new Exception();
		
		System.out.print("Name? ");
		if (sc.hasNext()) name = sc.next();
		else throw new Exception();
		
		System.out.print("Surname? ");
		if (sc.hasNext()) surname = sc.next();
		else throw new Exception();
		
		roster.addEmployee(position, id, name, surname);
	}
	
	// b) + c)
	private static void modWork(Scanner sc) throws Exception {
		String workType;
		int hoursPerMonth;
		
		System.out.print("Work type? [administrative/documentation/developement] ");
		if (sc.hasNext()) workType = sc.next();
		else throw new Exception();
		
		System.out.print("Delta hours per month? ");
		if (sc.hasNextInt()) hoursPerMonth = sc.nextInt();
		else throw new Exception();
		
		roster.modifyWork(workType, hoursPerMonth);
	}
	
	// d)
	private static void fire(Scanner sc) throws Exception {
		int id;
		
		System.out.print("Employee ID? ");
		if (sc.hasNextInt()) id = sc.nextInt();
		else throw new Exception();
		
		roster.fireEmployee(id);
	}
	
	// e)
	private static void sick(Scanner sc) throws Exception {
		int id;
		
		System.out.print("Employee ID? ");
		if (sc.hasNextInt()) id = sc.nextInt();
		else throw new Exception();
		
		roster.setSickEmployee(id, true);
	}
	
	// f)
	private static void heal(Scanner sc) throws Exception {
		int id;
		
		System.out.print("Employee ID? ");
		if (sc.hasNextInt()) id = sc.nextInt();
		else throw new Exception();
		
		roster.setSickEmployee(id, false);
	}
	
	// g)
	private static void maxWork(Scanner sc) throws Exception {
		int hours;
		
		System.out.print("Max hours per month per employee? ");
		if (sc.hasNextInt()) hours = sc.nextInt();
		else throw new Exception();
		
		roster.setMaxMonthlyWorkHours(hours);
	}
	
	// h)
	private static void free(Scanner sc) throws Exception {
		System.out.println(roster.numEmployeesByPosition());
	}
	
	// i)
	private static void upkeep(Scanner sc) throws Exception {
		System.out.println(roster.getTotalMonthlyWages());
	}
	
	// j)
	private static void listById(Scanner sc) throws Exception {
		for (String e : roster.getListOfEmployeesById()) {
			System.out.println(e);
		}
	}
	
	// j) again
	private static void listBySurname(Scanner sc) throws Exception {
		for (String e : roster.getListOfEmployeesBySurname()) {
			System.out.println(e);
		}
	}
	
	// k)
	private static void saveRoster(Scanner sc) throws Exception {
		System.out.print("File name? ");
		String filename;
		if (sc.hasNext()) filename = sc.next();
		else throw new Exception();
		
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;

		try {
		    fout = new FileOutputStream(filename);
		    oos = new ObjectOutputStream(fout);
		    oos.writeObject(roster);
		} finally {
		    if (oos != null) oos.close();
		    if (fout != null) fout.close();
		}
	}
	
	// l)
	private static void loadRoster(Scanner sc) throws Exception {
		System.out.print("File name? ");
		String filename;
		if (sc.hasNext()) filename = sc.next();
		else throw new Exception();
		
		FileInputStream fin = null;
		ObjectInputStream ois = null;
		
		try {
		    fin = new FileInputStream(filename);
		    ois = new ObjectInputStream(fin);
		    roster = (StaffRoster) ois.readObject();
		} finally {
		    if (ois != null) ois.close();
		    if (fin != null) fin.close();
		}
	}

}
