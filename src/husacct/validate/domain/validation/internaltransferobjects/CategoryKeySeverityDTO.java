package husacct.validate.domain.validation.internaltransferobjects;

import husacct.validate.domain.validation.DefaultSeverities;

public class CategoryKeySeverityDTO {

	private final String key;
	private final String category;
	private final DefaultSeverities defaultSeverity;

	public CategoryKeySeverityDTO(String key, String category, DefaultSeverities defaultSeverity) {
		this.key = key;
		this.category = category;
		this.defaultSeverity = defaultSeverity;
	}

	public DefaultSeverities getDefaultSeverity() {
		return defaultSeverity;
	}

	public String getCategory() {
		return category;
	}

	public String getKey() {
		return key;
	}
}