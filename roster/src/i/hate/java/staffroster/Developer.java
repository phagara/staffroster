package i.hate.java.staffroster;

public class Developer extends Employee {

	private static final long serialVersionUID = -2507720160886785928L;

	@Override
	public String getJobTitle() {
		return "Developer";
	}

	@Override
	public int getHourlyWage() {
		return 250;
	}

	@Override
	public boolean canPerformAdministrative() {
		return false;
	}

	@Override
	public boolean canPerformDocumentation() {
		return true;
	}

	@Override
	public boolean canPerformDevelopement() {
		return true;
	}

}
