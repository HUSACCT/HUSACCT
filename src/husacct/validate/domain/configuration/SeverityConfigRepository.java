package husacct.validate.domain.configuration;

import husacct.validate.domain.exception.ContainsUnidentifiedSeverityException;
import husacct.validate.domain.exception.DuplicateSeverityException;
import husacct.validate.domain.exception.EmptySeverityNameException;
import husacct.validate.domain.exception.SeverityNotFoundException;
import husacct.validate.domain.validation.DefaultSeverities;
import husacct.validate.domain.validation.Severity;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
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
			if(!severityName.isEmpty() && (severityName.toLowerCase().equals(customSeverity.getUserName().toLowerCase()) || severityName.toLowerCase().equals(customSeverity.getDefaultName().toLowerCase()))){
				return customSeverity;	
			}		
		}

		for(Severity defaultSeverity : defaultSeverities){
			if(severityName.toLowerCase().equals(defaultSeverity.getDefaultName().toLowerCase()) && !severityName.isEmpty()){
				return defaultSeverity;
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
		checkSeverities(severities);
		this.currentSeverities = matchNewWithCurrentSeverities(severities);
	}

	private List<Severity> matchNewWithCurrentSeverities(List<Severity> newSeverities){
		ArrayList<Severity> finalSeveritiesList = new ArrayList<Severity>();
		HashSet<Integer> severityHash = new HashSet<Integer>(newSeverities.size());

		for(Severity newSeverity : newSeverities){
			for(Severity oldSeverity : currentSeverities){
				if(!(severityHash.contains(oldSeverity.hashCode()) || severityHash.contains(newSeverity.hashCode()))){
					if(oldSeverity == newSeverity){
						finalSeveritiesList.add(newSeverity);
						severityHash.add(newSeverity.hashCode());
					}
					else{
						if(oldSeverity.getId().equals(newSeverity.getId())){
							finalSeveritiesList.add(oldSeverity);
							severityHash.add(oldSeverity.hashCode());
						}						
						else if(oldSeverity.hashCode() == newSeverity.hashCode()){
							finalSeveritiesList.add(oldSeverity);
							severityHash.add(oldSeverity.hashCode());
						}
						else{							
							finalSeveritiesList.add(newSeverity);
							severityHash.add(newSeverity.hashCode());
						}
					}
				}
			}
		}
		return new ArrayList<Severity>(finalSeveritiesList);
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
				if((checkDuplicateSeverity.getDefaultName().toLowerCase().equals(severity.getDefaultName().toLowerCase()) && !severity.getDefaultName().isEmpty()) || checkDuplicateSeverity.getUserName().toLowerCase().equals(severity.getUserName().toLowerCase())&& !severity.getUserName().isEmpty()){
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
				if(newSeverity.getDefaultName().equals(DefaultSeverities.UNIDENTIFIED.toString()) || (defaultSeverity.getId().equals(newSeverity.getId()) && defaultSeverity.getDefaultName().equals(DefaultSeverities.UNIDENTIFIED))){
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