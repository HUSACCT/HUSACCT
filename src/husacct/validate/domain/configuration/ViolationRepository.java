package husacct.validate.domain.configuration;

import husacct.validate.domain.validation.Violation;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

class ViolationRepository {

	private List<Violation> violationsList;
	private HashMap<String, HashMap<String, Violation>> violationToFromHashMap;
	private Calendar repositoryCalendar;
	private final Logger logger = Logger.getLogger(ViolationRepository.class);
    

	public ViolationRepository() {
		this.violationsList = new ArrayList<Violation>();
		this.setRepositoryCalendar(Calendar.getInstance());
	}

	void addViolation(List<Violation> newViolations) {
		this.violationsList = newViolations;
	}

	void addViolation(Violation violation) {
		this.violationsList.add(violation);
	}
	
	// returns a List of Violations; it is empty if no Violation is registered for the specific combination of from-to
	public List<Violation> getViolationsFromTo(String physicalPathFrom, String physicalPathTo) {
		ArrayList<Violation> foundViolations = new ArrayList<Violation>();
		String violationFromToKey = physicalPathFrom + "|" + physicalPathTo;
		violationFromToKey.toLowerCase();
		HashMap<String, Violation> violationDetailsHashMap;
		if(violationToFromHashMap.containsKey(violationFromToKey)){
			violationDetailsHashMap = violationToFromHashMap.get(violationFromToKey);
			Set<String> keySet = violationDetailsHashMap.keySet();
			for (String violationDetailsKey : keySet) {
				foundViolations.add(violationDetailsHashMap.get(violationDetailsKey));
			}
		}
		return foundViolations;
	}

	void filterAndSortAllViolations(){
		int beforeNrOfViolations = violationsList.size();
		ArrayList<Violation> sortedViolationsList = new ArrayList<Violation>();
		String violationFromToKey;
		String violationDetailsKey;
		violationToFromHashMap = new HashMap<String, HashMap<String, Violation>>();
		HashMap<String, Violation> violationDetailsHashMap;
		for(Violation violation : violationsList){
			try{
				violationFromToKey = "";
				violationDetailsKey = "";
				violationDetailsHashMap = null;
				violationFromToKey = violation.getClassPathFrom() + "|" + violation.getClassPathTo();
				violationDetailsKey = violation.getRuletypeKey() + "|" + violation.getLinenumber() + "|" + violation.getViolationTypeKey();
				violationFromToKey.toLowerCase();
				violationDetailsKey.toLowerCase();
				if(violationToFromHashMap.containsKey(violationFromToKey)){
					violationDetailsHashMap = violationToFromHashMap.get(violationFromToKey);
					if(violationDetailsHashMap.containsKey(violationDetailsKey)){
						// Do nothing; violation is already registered
					} else {
						violationDetailsHashMap.put(violationDetailsKey, violation);
						sortedViolationsList.add(violation);
					}
				}
				else{
					violationDetailsHashMap = new HashMap<String, Violation>();
					violationDetailsHashMap.put(violationDetailsKey, violation);
					violationToFromHashMap.put(violationFromToKey, violationDetailsHashMap);
					sortedViolationsList.add(violation);
				}
			} catch (Exception e) {
				this.logger.error(new Date().toString() + " Exception:  " + e);
			}
		}
		
        violationsList = sortedViolationsList;  
        this.logger.info(new Date().toString() + " Before/After filterAndSortAllViolations:  " + beforeNrOfViolations + "/" + violationsList.size());
	}
	
	SimpleEntry<Calendar, List<Violation>> getAllViolations() {
		return new SimpleEntry<Calendar, List<Violation>>(getCurrentCalendar(), violationsList);
	}
	
	Calendar getCurrentCalendar() {
		return Calendar.getInstance();
	}

	void clear() {
		this.violationsList = new ArrayList<Violation>();
	}

	Calendar getRepositoryCalendar() {
		return repositoryCalendar;
	}

	void setRepositoryCalendar(Calendar repositoryCalendar) {
		this.repositoryCalendar = repositoryCalendar;
	}
}