package husacct.validate.domain;

import husacct.validate.domain.configuration.SeverityConfigRepository;
import husacct.validate.domain.configuration.SeverityPerTypeRepository;
import husacct.validate.domain.configuration.ViolationRepository;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import java.util.HashMap;
import java.util.List;

public class ConfigurationServiceImpl {

	private final SeverityConfigRepository severityConfig;
	private final SeverityPerTypeRepository severityRepository;
	private final ViolationRepository violationRepository;
	
	public ConfigurationServiceImpl() {
		this.severityConfig = new SeverityConfigRepository();
		this.violationRepository = new ViolationRepository();
		this.severityRepository = new SeverityPerTypeRepository(this);
		this.severityRepository.initializeDefaultSeverities();
	}

	public void clearViolations() {
		violationRepository.clear();
	}
	
	public int getSeverityValue(Severity severity){
		return severityConfig.getSeverityValue(severity);
	}

	public List<Violation> getAllViolations() {
		return violationRepository.getAllViolations();
	}

	public void addViolations(List<Violation> violations) {
		violationRepository.addViolation(violations);
	}

	public List<Severity> getAllSeverities() {
		return severityConfig.getAllSeverities();
	}

	public void addSeverities(List<Severity> severities) {
		severityConfig.addSeverities(severities);
	}
	
	public Severity getSeverityByName(String severityName){
		return severityConfig.getSeverityByName(severityName);
	}

	public HashMap<String, HashMap<String, Severity>> getAllSeveritiesPerTypesPerProgrammingLanguages() {
		return severityRepository.getSeveritiesPerTypePerProgrammingLanguage();
	}
	
	public void setSeveritiesPerTypesPerProgrammingLanguages(HashMap<String, HashMap<String, Severity>> severitiesPerTypesPerProgrammingLanguages) {
		severityRepository.setSeverityMap(severitiesPerTypesPerProgrammingLanguages);
	}

	public void setSeveritiesPerTypesPerProgrammingLanguages(String language, HashMap<String, Severity> severitiesPerTypesPerProgrammingLanguages) {
		severityRepository.setSeverityMap(language, severitiesPerTypesPerProgrammingLanguages);
	}
	
	public Severity getSeverityFromKey(String language, String key){
		return severityRepository.getSeverity(language, key);
	}
	
	public void restoreAllToDefault(String language){
		severityRepository.restoreAllToDefault(language);
	}
	
	public void restoreToDefault(String language, String key){
		severityRepository.restoreDefaultSeverity(language, key);
	}
	
	public void restoreSeveritiesToDefault(){
		severityConfig.restoreToDefault();
	}
}