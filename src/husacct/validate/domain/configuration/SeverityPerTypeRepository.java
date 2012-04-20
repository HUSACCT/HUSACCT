package husacct.validate.domain.configuration;

import husacct.validate.domain.validation.Severity;

import java.util.HashMap;

public class SeverityPerTypeRepository {
	private HashMap<String, HashMap<String, Severity>> severitiesPerTypePerProgrammingLanguage;
}