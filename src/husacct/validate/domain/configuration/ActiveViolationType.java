package husacct.validate.domain.configuration;

public class ActiveViolationType {

	private final String violationTypeKey;
	private boolean enabled;

	public ActiveViolationType(String violationTypeKey, boolean enabled) {
		this.violationTypeKey = violationTypeKey;
		this.enabled = enabled;
	}

	public String getType() {
		return violationTypeKey;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}