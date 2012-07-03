package husacct.validate.presentation.languageSeverityConfiguration.DTO;

import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.ViolationType;
import java.util.List;
import java.util.Map;

public class ConfigurationViolationTypeDTO {
	private final String language;
	private final List<Severity> severities;
	private final Map<String, List<ViolationType>> violationtypes;

	public ConfigurationViolationTypeDTO(String language, List<Severity> severities, Map<String, List<ViolationType>> violationtypes) {
		this.language = language;
		this.severities = severities;
		this.violationtypes = violationtypes;
	}

	public Map<String, List<ViolationType>> getViolationtypes() {
		return violationtypes;
	}

	public String getLanguage() {
		return language;
	}

	public List<Severity> getSeverities() {
		return severities;
	}
}