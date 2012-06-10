package husacct.validate.task;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.ViolationDTO;
import husacct.validate.domain.DomainServiceImpl;
import husacct.validate.domain.configuration.ActiveRuleType;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.iternal_tranfer_objects.PathDTO;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.task.export.ExportController;
import husacct.validate.task.fetch.ImportController;
import husacct.validate.task.filter.FilterController;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observer;

import javax.xml.datatype.DatatypeConfigurationException;

import org.jdom2.Element;

public class TaskServiceImpl{
	private final FilterController filterController;
	private final ConfigurationServiceImpl configuration;
	private final DomainServiceImpl domain;
	private final IAnalyseService analyseService = ServiceProvider.getInstance().getAnalyseService();

	private final ExportController exportController;
	private final ImportController importController;

	public TaskServiceImpl(ConfigurationServiceImpl configuration, DomainServiceImpl domain) {
		this.configuration = configuration;
		this.domain = domain;	
		this.exportController = new ExportController();
		this.importController = new ImportController(configuration);
		this.filterController = new FilterController(this, domain.getRuleTypesFactory(), configuration);
	}

	public SimpleEntry<Calendar, List<Violation>> getAllViolations(){
		return configuration.getAllViolations();
	}

	public ViolationDTO[] getViolationsByLogicalPath(String logicalpathFrom, String logicalpathTo) {
		return filterController.getViolationsByLogicalPath(logicalpathFrom, logicalpathTo);
	}

	public void setFilterValues(PathDTO dto, boolean hideFilter, Calendar date) {
		filterController.setFilterValues(dto, hideFilter, getHistoryViolations(date));
	}

	public ArrayList<Violation> applyFilterViolations(List<Violation> violations) {
		return filterController.filterViolations(violations);
	}

	public ArrayList<String> loadRuletypesForFilter(Calendar date) {
		return filterController.loadRuletypes(getHistoryViolations(date));
	}

	public ArrayList<String> loadViolationtypesForFilter(Calendar date) {
		return filterController.loadViolationtypes(getHistoryViolations(date));
	}

	public HashMap<String, List<RuleType>> getRuletypes(String language) {
		return domain.getAllRuleTypes(language);
	}	

	public List<Severity> getAllSeverities(){
		return configuration.getAllSeverities();
	}

	public String[] getAvailableLanguages(){
		return analyseService.getAvailableLanguages();
	}

	public void addSeverities(List<Severity> severities) {
		configuration.setSeverities(severities);
	}

	public void updateSeverityPerType(HashMap<String, Severity> map, String language){
		configuration.setSeveritiesPerTypesPerProgrammingLanguages(language, map);
	}

	public ViolationDTO[] getViolationsByPhysicalPath(String physicalPathFrom, String physicalPathTo) {
		return filterController.getViolationsByPhysicalPath(physicalPathFrom, physicalPathTo);
	}

	public Map<String, List<ViolationType>> getViolationTypes(String language) {
		return domain.getAllViolationTypes(language);
	}

	public Severity getSeverityFromKey(String language, String key){
		return configuration.getSeverityFromKey(language, key);
	}

	public void importValidationWorkspace(Element element) throws DatatypeConfigurationException {
		importController.importWorkspace(element);
	}

	public Element exportValidationWorkspace() {
		return exportController.exportAllData(configuration);
	}

	public LinkedHashMap<Severity, Integer> getViolationsPerSeverity(List<Violation> shownViolations, List<Severity> severities){
		return filterController.getViolationsPerSeverity(shownViolations, severities);
	}

	public void restoreAllKeysToDefaultSeverities(String language){
		configuration.restoreAllKeysToDefaultSeverities(language);
	}

	public void restoreKeyToDefaultSeverity(String language, String key){
		configuration.restoreKeyToDefaultSeverity(language, key);
	}

	public void restoreSeveritiesToDefault(){
		configuration.restoreSeveritiesToDefault();
	}

	public List<Violation> getHistoryViolations(Calendar date) {
		for(ViolationHistory violationHistory : configuration.getViolationHistory()) {
			if(violationHistory.getDate().equals(date)) {
				return violationHistory.getViolations();
			}
		}
		return getAllViolations().getValue();
	}

	public Calendar[] getViolationHistoryDates() {
		Calendar[] calendars = new Calendar[configuration.getViolationHistory().size()];
		int i = 0;
		for(ViolationHistory violationHistory : configuration.getViolationHistory()) {
			calendars[i] = violationHistory.getDate();
			i++;
		}
		return calendars;
	}

	public void createHistoryPoint(String description) {
		configuration.createHistoryPoint(description);
	}

	public void removeViolationHistory(Calendar date) {
		configuration.removeViolationHistory(date);
	}

	public ViolationHistory getViolationHistoryByDate(Calendar date) {
		return configuration.getViolationHistoryByDate(date);
	}

	public List<ViolationHistory> getViolationHistories() {
		return configuration.getViolationHistories();
	}

	public Map<String, List<ActiveRuleType>> getActiveViolationTypes(){
		return configuration.getActiveViolationTypes();
	}

	public void setActiveViolationTypes(String language, List<ActiveRuleType> activeViolations){
		configuration.setActiveViolationTypes(language, activeViolations);
	}
	
	public String getMessage(Message message){
		return domain.getMessage(message);
	}

	public void subscribe(Observer frame){
		configuration.addObserver(frame);
	}
}