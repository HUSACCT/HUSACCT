package husacct.validate.task;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.ViolationDTO;
import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.DomainServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.presentation.ViolationHistoryRepositoryObserver;
import husacct.validate.task.export.ExportController;
import husacct.validate.task.fetch.ImportController;
import husacct.validate.task.filter.FilterController;

import java.awt.Color;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

	public void setFilterValues(ArrayList<String> ruletypesKeys,
			ArrayList<String> violationtypesKeys,
			ArrayList<String> paths, boolean hideFilter) {
		filterController.setFilterValues(ruletypesKeys, violationtypesKeys, paths, hideFilter);
	}

	public ArrayList<Violation> applyFilterViolations(Boolean applyfilter) {
		return filterController.filterViolations(applyfilter);
	}

	public ArrayList<String> loadRuletypesForFilter() {
		return filterController.loadRuletypes();
	}

	public ArrayList<String> loadViolationtypesForFilter() {
		return filterController.loadViolationtypes();
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

	public void applySeverities(List<Object[]> list){
		List<Severity> severityList = new ArrayList<Severity>();

		for (int i = 0; i < list.size(); i++) {
			try{
				Severity severity = getAllSeverities().get(i);
				severity.setUserName((String) list.get(i)[0]);
				severity.setColor((Color) list.get(i)[1]);
				severityList.add(severity);
			} catch (IndexOutOfBoundsException e){
				severityList.add(new Severity((String) list.get(i)[0], (Color) list.get(i)[1]));
			}

		}
		addSeverities(severityList);
	}
	public void addSeverities(List<Severity> severities) {
		configuration.addSeverities(severities);
	}

	public void updateSeverityPerType(HashMap<String, Severity> map, String language){
		configuration.setSeveritiesPerTypesPerProgrammingLanguages(language, map);
	}

	public ViolationDTO[] getViolationsByPhysicalPath(String physicalPathFrom,
			String physicalPathTo) {
		return filterController.getViolationsByPhysicalPath(physicalPathFrom, physicalPathTo);
	}

	public Map<String, List<ViolationType>> getViolationTypes(
			String language) {
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

	public LinkedHashMap<Severity, Integer> getViolationsPerSeverity(boolean applyFilter){
		return filterController.getViolationsPerSeverity(applyFilter);
	}

	public void restoreAllToDefault(String language){
		configuration.restoreAllToDefault(language);
	}

	public void restoreToDefault(String language, String key){
		configuration.restoreToDefault(language, key);
	}

	public void restoreSeveritiesToDefault(){
		configuration.restoreSeveritiesToDefault();
	}

	public List<Violation> getViolationsByDate(Calendar date) {
		for(ViolationHistory violationHistory : configuration.getViolationHistory()) {
			if(violationHistory.getDate().equals(date)) {
				return violationHistory.getViolations();
			}
		}
		throw new NullPointerException("no violations found at date given");
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

	public void saveInHistory(String description) {
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
	
	public void attachViolationHistoryObserver(ViolationHistoryRepositoryObserver observer) {
		configuration.attachViolationHistoryRepositoryObserver(observer);
	}
}