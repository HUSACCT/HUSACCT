package husacct.validate.domain.validation.internaltransferobjects;

import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
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
	
	public String toString(){
		String result = "";
		result += "Key: " + key + "\n";
		result += "Category: " + category + "\n";
		result += "DefaultSeverity: " + defaultSeverity + "\n";
		result += "\n";
		return result;
	}
}