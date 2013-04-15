package husacct.validate.domain.validation;

public class ViolationType {

	private String violationtypeKey;
	private String violationDescriptionKey;
	private boolean isActive;
	private Severity severity;

	public ViolationType(String violationtypeKey, Severity severity) {
		this.violationtypeKey = violationtypeKey;
		this.violationDescriptionKey = violationtypeKey + "Description";
		this.isActive = true;
		this.setSeverity(severity);
	}

	public ViolationType(String violationtypeKey, boolean isActive,
			Severity severity) {
		this.violationtypeKey = violationtypeKey;
		this.violationDescriptionKey = violationtypeKey + "Description";
		this.isActive = isActive;
		this.setSeverity(severity);
	}

	public String getViolationtypeKey() {
		return violationtypeKey;
	}

	public void setViolationtypeKey(String violationtypeKey) {
		this.violationtypeKey = violationtypeKey;
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