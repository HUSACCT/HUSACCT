package husacct.validate.domain.configuration;

import husacct.validate.domain.validation.Violation;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import org.apache.log4j.Logger;

class ViolationRepository {

	private List<Violation> violationsList;
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

	void filterAndSortAllViolations(){
		int beforeNrOfViolations = violationsList.size();
		ArrayList<Violation> sortedViolationsList = new ArrayList<Violation>();
		String violationKey;
		TreeMap<String,Violation> violationTreemap = new TreeMap<String,Violation>();
		for(Violation violation : violationsList){
			try{
				violationKey = "";
				violationKey = violation.getClassPathFrom() + "." + violation.getLinenumber() + "." + violation.getViolationTypeKey() + "." + violation.getRuletypeKey() + "." + violation.getClassPathTo();
				violationKey.toLowerCase();
				violationTreemap.put(violationKey, violation);
			} catch (Exception e) {
				this.logger.error(new Date().toString() + " Exception:  " + e);
			}
		}
		
        for (Violation foundViolation : violationTreemap.values()) {
        	sortedViolationsList.add(foundViolation);
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