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
		this.defaultSeverities = new ArrayList<Severity>();
		generateDefaultSeverities();

		initializeCurrentSeverities();
	}

	List<Severity> getAllSeverities(){
		return currentSeverities;
	}

	void addSeverities(List<Severity> severities){
		this.currentSeverities = severities;
	}

	Severity getSeverityByName(String severityName){
		for(Severity customSeverity : currentSeverities){
			if(severityName.toLowerCase().equals(customSeverity.getUserName().toLowerCase()) || severityName.toLowerCase().equals(customSeverity.getDefaultName().toLowerCase())){
				return customSeverity;
			}		
		}
		throw new SeverityNotFoundException();
	}

	private void checkDefaultSeveritiesChanged(List<Severity> severities){
		for(Severity defaultSeverity : defaultSeverities){
			boolean defaultSeverityFound = false;

			for(Severity severity : severities){		
				if(severity.getId().equals(severity.getId())){
					defaultSeverityFound = true;
				}
				if(severity.getDefaultName().isEmpty() && severity.getUserName().isEmpty()){
					//severity user name must not be empty
				}			
			}
			if(!defaultSeverityFound){
				//throw new DefaultSeverityNotFoundException	
			}			
		}
	}

	int getSeverityValue(Severity severity){
		return currentSeverities.indexOf(severity);
	}

	void restoreToDefault(){
		initializeCurrentSeverities();
	}

	private void generateDefaultSeverities(){
		for(DefaultSeverities defaultSeverity : EnumSet.allOf(DefaultSeverities.class)){
			Severity severity = new Severity(defaultSeverity.toString(), "", defaultSeverity.getColor());
			this.defaultSeverities.add(severity);
		}
	}

	private void initializeCurrentSeverities(){	
		this.currentSeverities = new ArrayList<Severity>(defaultSeverities.size());
		for(Severity severity : defaultSeverities){
			if(!severity.getDefaultName().toLowerCase().equals("unidentified")){
				currentSeverities.add(severity);
			}
		}
	}
}