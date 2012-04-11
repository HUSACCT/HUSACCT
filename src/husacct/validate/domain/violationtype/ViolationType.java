package husacct.validate.domain.violationtype;

public class ViolationType {
	private String violationtypeKey;
	private String violationDescriptionKey;
	private boolean isActive;

	public ViolationType(String violationtypeKey){
		this.violationtypeKey = violationtypeKey;
		this.violationDescriptionKey = violationtypeKey + "Description";
		this.isActive = true;
	}

	public ViolationType(String violationtypeKey, boolean isActive){
		this.violationtypeKey = violationtypeKey;
		this.violationDescriptionKey = violationtypeKey + "Description";
		this.isActive = isActive;
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
}