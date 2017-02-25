package husacct.validate.domain.validation;

public class ViolationType {
	private String violationTypeKey;
	private String violationDescriptionKey;
	private boolean isActive;
	private Severity severity;

	public ViolationType(String violationTypeKey, Severity severity) {
		this.violationTypeKey = violationTypeKey;
		this.violationDescriptionKey = violationTypeKey + "Description";
		this.isActive = true;
		this.setSeverity(severity);
	}

	public ViolationType(String violationTypeKey, boolean isActive,	Severity severity) {
		this.violationTypeKey = violationTypeKey;
		this.violationDescriptionKey = violationTypeKey + "Description";
		this.isActive = isActive;
		this.setSeverity(severity);
	}

	public String getViolationTypeKey() {
		return violationTypeKey;
	}

	public void setViolationTypeKey(String violationTypeKey) {
		this.violationTypeKey = violationTypeKey;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getViolationDescriptionKey() {
		return violationDescriptionKey;
	}

	public void setViolationDescriptionKey(String violationDescriptionKey) {
		this.violationDescriptionKey = violationDescriptionKey;
	}

	public Severity getSeverity() {
		return severity;
	}

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}
}