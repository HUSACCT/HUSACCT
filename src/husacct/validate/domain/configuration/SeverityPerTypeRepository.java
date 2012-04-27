package husacct.validate.domain.configuration;

import husacct.validate.domain.exception.ProgrammingLanguageNotFound;
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

	public void restoreDefaultSeverity(String language, String key){
		//TODO
	}

	public void restoreAllToDefault(String language){
		//TODO
	}

	public void setSeverityMap(HashMap<String, HashMap<String, Severity>> severitiesPerTypePerProgrammingLanguage){
		this.severitiesPerTypePerProgrammingLanguage = severitiesPerTypePerProgrammingLanguage;
	}

	public void setSeverityMap(String language, HashMap<String, Severity> severityMap) {
		HashMap<String, Severity> local = this.severitiesPerTypePerProgrammingLanguage.get(language);
		if(local != null){
			local = severityMap;
		}
		else{
			throw new ProgrammingLanguageNotFound();
		}
	}
}