package i.hate.java.staffroster;

public class CEO extends Employee {

	private static final long serialVersionUID = -6395337687001981675L;

	@Override
	public String getJobTitle() {
		return "CEO";
	}

	@Override
	public int getHourlyWage() {
		return 350;
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
		return true;
	}

}
