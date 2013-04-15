package husacct.validate.domain.validation;

import java.util.Calendar;
import java.util.List;

public class ViolationHistory {

	private final List<Violation> violations;
	private final Calendar date;
	private List<Severity> severities;
	private final String description;

	public ViolationHistory(List<Violation> violations,
			List<Severity> severities, Calendar date, String description) {
		this.violations = violations;
		this.date = date;
		this.severities = severities;
		this.description = description;
	}

	public List<Violation> getViolations() {
		return violations;
	}

	public Calendar getDate() {
		return date;
	}

	public List<Severity> getSeverities() {
		return severities;
	}

	public void addSeverity(Severity severity) {
		severities.add(severity);
	}

	public String getDescription() {
		return description;
	}
}