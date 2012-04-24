package husacct.validate.domain.configuration;

import husacct.validate.domain.validation.Severity;
import java.util.HashMap;

public class SeverityPerTypeRepository {
	private HashMap<String, HashMap<String, Severity>> severitiesPerTypePerProgrammingLanguage;

	public SeverityPerTypeRepository(){
		severitiesPerTypePerProgrammingLanguage = new HashMap<String, HashMap<String, Severity>>();
	}

	public HashMap<String, HashMap<String, Severity>> getSeveritiesPerTypePerProgrammingLanguage() {
		return severitiesPerTypePerProgrammingLanguage;
	}

	public void setSeveritiesPerTypePerProgrammingLanguage(HashMap<String, HashMap<String, Severity>> severitiesPerTypePerProgrammingLanguage) {
		this.severitiesPerTypePerProgrammingLanguage =
				severitiesPerTypePerProgrammingLanguage;
	}
}