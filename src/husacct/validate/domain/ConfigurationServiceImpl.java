package husacct.validate.domain;

import husacct.validate.domain.configuration.SeverityConfigRepository;
import husacct.validate.domain.configuration.ViolationRepository;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;

import java.util.HashMap;
import java.util.List;

public class ConfigurationServiceImpl {
	private final SeverityConfigRepository severityConfig;
	private final ViolationRepository violationRepository;
	private HashMap<String, HashMap<String, Severity>> severitiesPerTypesPerProgrammingLanguages;

	public ConfigurationServiceImpl(){
		this.severityConfig = new SeverityConfigRepository();
		this.violationRepository = new ViolationRepository();
		this.severitiesPerTypesPerProgrammingLanguages = new HashMap<String, HashMap<String,Severity>>();
	}
	
	public void clearViolations(){
		violationRepository.clear();
	}

	public List<Violation> getAllViolations(){
		return violationRepository.getAllViolations();
	}

	public void addViolations(List<Violation> violations){
		violationRepository.addViolation(violations);
	}

	public List<Severity> getAllSeverities(){
		return severityConfig.getAllSeverities();
	}

	public void addSeverities(List<Severity> severities) {
		severityConfig.addSeverities(severities);
	}

	public HashMap<String, HashMap<String, Severity>> getAllSeveritiesPerTypesPerProgrammingLanguages() {
		return severitiesPerTypesPerProgrammingLanguages;
	}

	public void setSeveritiesPerTypesPerProgrammingLanguages(
			HashMap<String, HashMap<String, Severity>> severitiesPerTypesPerProgrammingLanguages) {
		this.severitiesPerTypesPerProgrammingLanguages = severitiesPerTypesPerProgrammingLanguages;
	}
}
