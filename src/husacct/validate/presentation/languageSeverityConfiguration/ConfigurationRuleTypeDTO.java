package husacct.validate.presentation.languageSeverityConfiguration;

import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.HashMap;
import java.util.List;

public class ConfigurationRuleTypeDTO {

	private final String language;
	private final List<Severity> severities;
	private final HashMap<String, List<RuleType>> ruletypes;

	public ConfigurationRuleTypeDTO(String language, List<Severity> severities, HashMap<String, List<RuleType>> ruletypes) {
		this.language = language;
		this.severities = severities;
		this.ruletypes = ruletypes;
	}

	public String getLanguage() {
		return language;
	}

	public HashMap<String, List<RuleType>> getRuletypes() {
		return ruletypes;
	}

	public List<Severity> getSeverities() {
		return severities;
	}
}