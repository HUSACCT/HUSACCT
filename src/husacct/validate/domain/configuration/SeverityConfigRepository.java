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

	private void checkSeverities(List<Severity> newSeverities){
		if(containsUnidentifiedSeverity(newSeverities)){
			//throw error
		}	
		if(checkDefaultSeverityName(newSeverities)){
			//throw error
		}
	}
	
//	private boolean checkDuplicatedDefaultSeverities(List<Severity> newSeverities){
//		
//	}

	private boolean checkDefaultSeverityName(List<Severity> newSeverities){
		boolean foundError = false;
		for(Severity defaultSeverity : generateDefaultSeverities()){
			for(Severity newSeverity : newSeverities){
				if(newSeverity.getDefaultName().toLowerCase().equals(defaultSeverity.getDefaultName().toLowerCase())){
					if(!newSeverity.getUserName().isEmpty()){
						foundError = true;
					}
				}
			}
		}
		return foundError;
	}

	private boolean containsUnidentifiedSeverity(List<Severity> newSeverities){
		for(Severity defaultSeverity : generateDefaultSeverities()){
			for(Severity newSeverity : newSeverities){		
				if(newSeverity.getDefaultName().toLowerCase().equals(DefaultSeverities.UNIDENTIFIED.toString().toLowerCase())){				
					return true;
				}
				if(defaultSeverity.getDefaultName().equals(DefaultSeverities.UNIDENTIFIED.toString()) && defaultSeverity.getId().equals(newSeverity.getId())){
					return true;
				}
			}
		}
		return false;
	}

	int getSeverityValue(Severity severity){
		return currentSeverities.indexOf(severity);
	}

	void restoreToDefault(){
		initializeCurrentSeverities();
	}

	private List<Severity> generateDefaultSeverities(){
		List<Severity> newDefaultSeverities = new ArrayList<Severity>();
		for(DefaultSeverities defaultSeverity : EnumSet.allOf(DefaultSeverities.class)){
			Severity severity = new Severity(defaultSeverity.toString(), "", defaultSeverity.getColor());
			newDefaultSeverities.add(severity);
		}
		return newDefaultSeverities;
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