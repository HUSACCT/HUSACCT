package husacct.validate.domain.validation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ViolationHistory {
	
	private List<Violation> violations;
	private Calendar date;
	private List<Severity> severities;
	
	public ViolationHistory() {
		violations = new ArrayList<Violation>();
		date = Calendar.getInstance();
		severities = new ArrayList<Severity>();
	}

	public List<Violation> getViolations() {
		return violations;
	}

	public void setViolations(List<Violation> violations) {
		this.violations = violations;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public List<Severity> getSeverities() {
		return severities;
	}

	public void setSeverities(List<Severity> severities) {
		this.severities = severities;
	}
	
	

}
