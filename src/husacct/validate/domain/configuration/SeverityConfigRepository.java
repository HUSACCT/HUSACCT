package husacct.validate.domain.configuration;

import husacct.validate.domain.exception.SeverityNotFoundException;
import husacct.validate.domain.validation.DefaultSeverities;
import husacct.validate.domain.validation.Severity;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

class SeverityConfigRepository {
	private List<Severity> currentSeverities;
	private final List<Severity> defaultSeverities;

	public SeverityConfigRepository(){
		this.currentSeverities = new ArrayList<Severity>();
		this.defaultSeverities = generateDefaultSeverities();		

		initializeCurrentSeverities();
	}

	void restoreToDefault(){
		initializeCurrentSeverities();
	}

	private void initializeCurrentSeverities(){	
		this.currentSeverities = new ArrayList<Severity>(defaultSeverities.size());
		for(Severity severity : defaultSeverities){
			currentSeverities.add(severity);
		}
	}

	Severity getSeverityByName(String severityName){
		for(Severity customSeverity : currentSeverities){
			if(!severityName.isEmpty() && (severityName.toLowerCase().equals(customSeverity.getSeverityKey().toLowerCase()) || severityName.toLowerCase().equals(customSeverity.getSeverityName().toLowerCase()))){
				return customSeverity;	
			}		
		}
		throw new SeverityNotFoundException(severityName);
	}

	int getSeverityValue(Severity severity){
		return currentSeverities.indexOf(severity);
	}

	List<Severity> getAllSeverities(){
		return currentSeverities;
	}

	void setSeverities(List<Severity> severities){
		this.currentSeverities = severities;
	}

	private List<Severity> generateDefaultSeverities(){
		List<Severity> newDefaultSeverities = new ArrayList<Severity>();
		for(DefaultSeverities defaultSeverity : EnumSet.allOf(DefaultSeverities.class)){
			Severity severity = new Severity(defaultSeverity.toString(), defaultSeverity.getColor());
			newDefaultSeverities.add(severity);
		}
		return newDefaultSeverities;
	}
}