package husacct.validate.domain.configuration;

import husacct.validate.domain.validation.Violation;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

class ViolationRepository {

	private List<Violation> violationsList;
	private HashMap<String, HashMap<String, Violation>> violationFromToHashMap;
	private TreeMap<String, List<Violation>> violationsPerRuleTreeMap;
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
		String violationFromToKey = physicalPathFrom + "::" + physicalPathTo;
		violationFromToKey.toLowerCase();
		HashMap<String, Violation> violationDetailsHashMap;
		if(violationFromToHashMap.containsKey(violationFromToKey)){
			violationDetailsHashMap = violationFromToHashMap.get(violationFromToKey);
			Set<String> keySet = violationDetailsHashMap.keySet();
			for (String violationDetailsKey : keySet) {
				foundViolations.add(violationDetailsHashMap.get(violationDetailsKey));
			}
		}
		return foundViolations;
	}

	// Objectives: 1) create and fill violationFromToHashMap, 2) remove duplicates.
	void filterAndSortAllViolations(){
		int beforeNrOfViolations = violationsList.size();
		ArrayList<Violation> filteredViolationsList = new ArrayList<Violation>();
		String violationFromToKey;
		String violationDetailsKey;
		violationFromToHashMap = new HashMap<String, HashMap<String, Violation>>();
		HashMap<String, Violation> violationDetailsHashMap;
		for(Violation violation : violationsList){
			try{
				violationFromToKey = "";
				violationDetailsKey = "";
				violationDetailsHashMap = null;
				violationFromToKey = violation.getClassPathFrom() + "::" + violation.getClassPathTo();
				violationDetailsKey = violation.getRuletypeKey() + "::" + violation.getLinenumber() + "::" + violation.getViolationTypeKey();
				violationFromToKey.toLowerCase();
				violationDetailsKey.toLowerCase();
				if(violationFromToHashMap.containsKey(violationFromToKey)){
					violationDetailsHashMap = violationFromToHashMap.get(violationFromToKey);
					if(violationDetailsHashMap.containsKey(violationDetailsKey)){
						// Do nothing; violation is already registered
					} else {
						violationDetailsHashMap.put(violationDetailsKey, violation);
						filteredViolationsList.add(violation);
					}
				}
				else{
					violationDetailsHashMap = new HashMap<String, Violation>();
					violationDetailsHashMap.put(violationDetailsKey, violation);
					violationFromToHashMap.put(violationFromToKey, violationDetailsHashMap);
					filteredViolationsList.add(violation);
				}
			} catch (Exception e) {
				this.logger.error(new Date().toString() + " Exception:  " + e);
			}
		}
		
        violationsList = filteredViolationsList;
        sortViolationsPerRule(); // Do this afterwards, to prevent duplicate violations.
        this.logger.info(new Date().toString() + " Before/After filterAndSortAllViolations:  " + beforeNrOfViolations + "/" + violationsList.size());
	}
	
	// Objectives: 1) create and fill violationsPerRuleHashMap.
	private void sortViolationsPerRule() {
		violationsPerRuleTreeMap = new TreeMap<String, List<Violation>>();
		List<Violation> violationsPerRuleList;
		String moduleFrom;
		String moduleTo;
		String searchKey;
		for (Violation violation : violationsList) {
			// Note: message refers to moduleFrom and moduleTo of the original rule, while the violation itself may refer to another moduleFrom or moduleTo 
			moduleFrom = violation.getMessage().getLogicalModules().getLogicalModuleFrom().getLogicalModulePath();
			moduleTo = violation.getMessage().getLogicalModules().getLogicalModuleTo().getLogicalModulePath();
			searchKey = "";
			searchKey = moduleFrom + "::" + moduleTo + "::" + violation.getRuletypeKey();
			if(violationsPerRuleTreeMap.containsKey(searchKey)) {
				violationsPerRuleList = violationsPerRuleTreeMap.get(searchKey);
				violationsPerRuleList.add(violation);
				violationsPerRuleTreeMap.put(searchKey, violationsPerRuleList);
			} else {
				violationsPerRuleList = new ArrayList<Violation>();
				violationsPerRuleList.add(violation);
				violationsPerRuleTreeMap.put(searchKey, violationsPerRuleList);
			}
		}
		
		/* Test helpers
		for (String rule : violationsPerRuleTreeMap.keySet()) {
			violationsPerRuleList = violationsPerRuleTreeMap.get(rule);
			logger.info(violationsPerRuleList.size() + " violations for rule: " + rule);
		} */
	}

	public Set<String> getViolatedRules() {
		if (violationsPerRuleTreeMap != null) {
			return violationsPerRuleTreeMap.keySet();
		} else {
			return new HashSet<String>();
		}
	}
	
	public List<Violation> getViolationsByRule(String moduleFrom, String moduleTo, String ruleTypeKey) {
		ArrayList<Violation> foundViolations = new ArrayList<Violation>();
		String searchKey = moduleFrom + "::" + moduleTo + "::" + ruleTypeKey;
		if(violationsPerRuleTreeMap.containsKey(searchKey)) {
			foundViolations.addAll(violationsPerRuleTreeMap.get(searchKey));
		}
		return foundViolations;
	}

	SimpleEntry<Calendar, List<Violation>> getAllViolations() {
		return new SimpleEntry<Calendar, List<Violation>>(getCurrentCalendar(), violationsList);
	}
	
	void clear() {
		this.violationsList = new ArrayList<Violation>();
	}

	Calendar getCurrentCalendar() {
		return Calendar.getInstance();
	}

	Calendar getRepositoryCalendar() {
		return repositoryCalendar;
	}

	void setRepositoryCalendar(Calendar repositoryCalendar) {
		this.repositoryCalendar = repositoryCalendar;
	}
}