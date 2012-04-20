package husacct.validate.domain.configuration;

import husacct.validate.domain.validation.Severity;

import java.util.Collections;
import java.util.List;

public class SeverityConfigRepository {
	private List<Severity> severities;

	public SeverityConfigRepository(){
		this.severities = Collections.emptyList();		
	}

	public List<Severity> getAllSeverities(){
		return severities;
	}

	public void addSeverities(List<Severity> severities) {
		severities.addAll(severities);
		reorderSeverityValues();
	}
	
	private void reorderSeverityValues(){
		for(int i = 0; i < severities.size(); i++){
			severities.get(i).setValue(i+1);
		}
	}
}