package husacct.validate.domain.configuration;

import husacct.validate.domain.exception.ContainsUnidentifiedSeverityException;
import husacct.validate.domain.exception.DuplicateSeverityException;
import husacct.validate.domain.exception.EmptySeverityNameException;
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
			if(!severity.getDefaultName().toLowerCase().equals("unidentified")){
				currentSeverities.add(severity);
			}
		}
	}
	
	Severity getSeverityByName(String severityName){
		for(Severity customSeverity : currentSeverities){
			if(severityName.toLowerCase().equals(customSeverity.getUserName().toLowerCase()) || severityName.toLowerCase().equals(customSeverity.getDefaultName().toLowerCase())){
				return customSeverity;	
			}		
		}
		throw new SeverityNotFoundException();
	}
	
	int getSeverityValue(Severity severity){
		return currentSeverities.indexOf(severity);
	}

	List<Severity> getAllSeverities(){
		return currentSeverities;
	}
	
	List<Severity> getAllSeveritiesCloned(){
		List<Severity> severities = new ArrayList<Severity>(currentSeverities.size());
		for(Severity currentSeverity : currentSeverities){
			severities.add(currentSeverity.clone());
		}
		return severities;
	}

	void setSeverities(List<Severity> severities){
		checkSeverities(severities);
		this.currentSeverities = matchNewWithCurrentSeverities(severities);
	}

	private List<Severity> matchNewWithCurrentSeverities(List<Severity> newSeverities){
		List<Severity> finalSeverityList = new ArrayList<Severity>();
		for(Severity newSeverity : newSeverities){
			for(Severity oldSeverity : currentSeverities){			
				if(oldSeverity == newSeverity){
					finalSeverityList.add(newSeverity);
				}
				else{
					finalSeverityList.add(newSeverity);
				}
			}
		}
		return finalSeverityList;
	}
	
	/** 	 
	 * @throws: DuplicateSeverityException, EmptySeverityNameException, ContainsUnidentifiedSeverityException
	 */
	private void checkSeverities(List<Severity> newSeverities){
		checkSeverityName(newSeverities);
		containsUnidentifiedSeverity(newSeverities);
		checkDuplicatedSeverities(newSeverities);
	}

	private void checkDuplicatedSeverities(List<Severity> newSeverities){
		for(Severity severity : newSeverities){
			int idCounter = 0;
			int nameCounter = 0;
			for(Severity checkDuplicateSeverity : newSeverities){
				if(checkDuplicateSeverity.getId().equals(severity.getId())){
					idCounter++;
				}
				if(checkDuplicateSeverity.getDefaultName().toLowerCase().equals(severity.getDefaultName().toLowerCase()) || checkDuplicateSeverity.getUserName().toLowerCase().equals(severity.getUserName().toLowerCase())){
					nameCounter++;					
				}

				if(idCounter >= 3 || nameCounter >= 3){
					String severityName = "";
					if(checkDuplicateSeverity.getDefaultName() == null || checkDuplicateSeverity.getDefaultName().isEmpty()){
						severityName = checkDuplicateSeverity.getUserName();
					}
					else{
						severityName = checkDuplicateSeverity.getDefaultName();
					}
					throw new DuplicateSeverityException(severityName, checkDuplicateSeverity.getId());
				}
			}
		}
	}

	private void checkSeverityName(List<Severity> newSeverities){		
		for(Severity severity : newSeverities){
			if(severity.getUserName() == null || severity.getDefaultName() == null || (severity.getDefaultName().isEmpty() && severity.getUserName().isEmpty())){
				throw new EmptySeverityNameException();
			}
		}
	}

	private void containsUnidentifiedSeverity(List<Severity> newSeverities){
		boolean foundError = false;
		for(Severity defaultSeverity : defaultSeverities){
			for(Severity newSeverity : newSeverities){
				if(defaultSeverity.getDefaultName().equals(DefaultSeverities.UNIDENTIFIED.toString()) || defaultSeverity.getId().equals(newSeverity.getId())){
					System.out.println("deze1");
					foundError = true;					
				}
				if(newSeverity.getDefaultName().toLowerCase().equals(DefaultSeverities.UNIDENTIFIED.toString().toLowerCase())){				
					System.out.println("deze2");
					foundError = true;					
				}
				if(foundError){
					throw new ContainsUnidentifiedSeverityException();
				}
			}
		}
	}

	private List<Severity> generateDefaultSeverities(){
		List<Severity> newDefaultSeverities = new ArrayList<Severity>();
		for(DefaultSeverities defaultSeverity : EnumSet.allOf(DefaultSeverities.class)){
			Severity severity = new Severity(defaultSeverity.toString(), "", defaultSeverity.getColor());
			newDefaultSeverities.add(severity);
		}
		return newDefaultSeverities;
	}
}