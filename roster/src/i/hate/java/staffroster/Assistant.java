package i.hate.java.staffroster;

public class Assistant extends Employee {
	
	private static final long serialVersionUID = -7456367470982069083L;

	@Override
	public String getJobTitle() {
		return "Assistant";
	}

	@Override
	public int getHourlyWage() {
		return 150;
	}

	@Override
	public boolean canPerformAdministrative() {
		return true;
	}

	@Override
	public boolean canPerformDocumentation() {
		return false;
	}

	@Override
	public boolean canPerformDevelopement() {
		return false;
	}

}
