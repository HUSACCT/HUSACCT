package husacct.validate.domain.configuration;

import husacct.validate.domain.exception.ProgrammingLanguageNotFound;
import husacct.validate.domain.validation.Severity;
import java.util.HashMap;
import java.util.Map.Entry;

public class SeverityPerTypeRepository {
	private HashMap<String, HashMap<String, Severity>> severitiesPerTypePerProgrammingLanguage;

	public SeverityPerTypeRepository(){
		severitiesPerTypePerProgrammingLanguage = new HashMap<String, HashMap<String, Severity>>();
	}

	public HashMap<String, HashMap<String, Severity>> getSeveritiesPerTypePerProgrammingLanguage() {
		return severitiesPerTypePerProgrammingLanguage;
	}
	
	public void restoreDefaultSeverity(String language, String key){
		
	}
	
	public void restoreAllToDefault(String language){
		
	}

	public void setSeverityMap(String language, HashMap<String, Severity> severityMap) {
		HashMap<String, Severity> local = this.severitiesPerTypePerProgrammingLanguage.get(language);
		if(local != null){
			for(Entry<String, Severity> key : severityMap.entrySet()){
				
			}
		}
		else{
			throw new ProgrammingLanguageNotFound();
		}
	}
}