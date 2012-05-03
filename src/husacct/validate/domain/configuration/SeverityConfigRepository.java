package husacct.validate.domain.configuration;

import husacct.validate.domain.exception.SeverityNotFoundException;
import husacct.validate.domain.validation.DefaultSeverities;
import husacct.validate.domain.validation.Severity;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class SeverityConfigRepository {
	private List<Severity> currentSeverities;
	private final List<Severity> defaultSeverities;

	public SeverityConfigRepository(){
		this.currentSeverities = new ArrayList<Severity>();
		this.defaultSeverities = new ArrayList<Severity>();
		generateDefaultSeverities();

		initializeCurrentSeverities();
	}

	public List<Severity> getAllSeverities(){
		return currentSeverities;
	}

	public void addSeverities(List<Severity> severities){
		this.currentSeverities = severities;
	}

	public Severity getSeverityByName(String severityName){
		for(Severity defaultSeverity : defaultSeverities){
			if(severityName.toLowerCase().equals(defaultSeverity.getDefaultName().toLowerCase())){
				return defaultSeverity;
			}
		}

		for(Severity customSeverity : currentSeverities){
			if(severityName.toLowerCase().equals(customSeverity.getUserName().toLowerCase())){
				return customSeverity;
			}		
		}
		throw new SeverityNotFoundException();
	}

	public int getSeverityValue(Severity severity){
		return currentSeverities.indexOf(severity);
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
			currentSeverities.add(severity);
		}
	}
}