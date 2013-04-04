package husacct.validate.domain.validation.violationtype;

public enum ViolationCategories {
	DEPENDENCY_RECOGNITION("DependencyRecognition"), ACCESS_MODIFIERS("AccessModifiers"), PACKAGING("Packaging");

	private final String key;

	ViolationCategories(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return key;
	}
}