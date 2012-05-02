package husacct.validate.domain.configuration;

import husacct.validate.domain.exception.SeverityNotFoundException;
import husacct.validate.domain.validation.Severity;

import java.awt.Color;
import java.util.ArrayList;
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
	
	
//	private boolean equalsMemoryLocation(Severity severity){
//		for(Severity defaultSeverity : defaultSeverities){
//			if(defaultSeverity == severity){
//				return true;
//			}
//		}
//		
//		for(Severity currentSeverity : currentSeverities){
//			if(currentSeverity == severity){
//				return true;
//			}
//		}		
//		return false;
//	}
//	
//	private boolean equalsContent(Severity severity){
//		
//	}

	private void generateDefaultSeverities(){
		Severity defaultLowSeverity = new Severity("Low", "", Color.GREEN);
		Severity defaultMediumSeverity = new Severity("Medium", "", Color.ORANGE);
		Severity defaultHighSeverity = new Severity("High", "", Color.RED);

		this.defaultSeverities.add(defaultLowSeverity);
		this.defaultSeverities.add(defaultMediumSeverity);
		this.defaultSeverities.add(defaultHighSeverity);
	}

	private void initializeCurrentSeverities(){	
		this.currentSeverities = new ArrayList<Severity>(defaultSeverities.size());
		for(Severity severity : defaultSeverities){
			currentSeverities.add(severity);
		}
	}
}