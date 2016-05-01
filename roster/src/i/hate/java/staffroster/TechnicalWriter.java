package i.hate.java.staffroster;

public class TechnicalWriter extends Employee {

	private static final long serialVersionUID = -6282745186802018246L;

	@Override
	public String getJobTitle() {
		return "Technical Writer";
	}

	@Override
	public int getHourlyWage() {
		return 200;
	}

	@Override
	public boolean canPerformAdministrative() {
		return true;
	}

	@Override
	public boolean canPerformDocumentation() {
		return true;
	}

	@Override
	public boolean canPerformDevelopement() {
		return false;
	}

}
