package husacct.validate.task;

import husacct.analyse.AnalyseServiceStub;
import husacct.common.dto.ViolationDTO;
import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.DomainServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.presentation.TableModels.ColorTableModel;
import husacct.validate.presentation.TableModels.ComboBoxTableModel;
import husacct.validate.task.filter.FilterController;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class TaskServiceImpl implements ITaskService{
	private final FilterController filterController;
	private final ConfigurationController conficurationController;
	private final ConfigurationServiceImpl configuration;
	private final DomainServiceImpl domain;
	private final AnalyseServiceStub acs;


	public TaskServiceImpl(ConfigurationServiceImpl configuration, DomainServiceImpl domain) {
		this.configuration = configuration;
		this.domain = domain;
		filterController = new FilterController(this);
		acs = new AnalyseServiceStub();
		conficurationController = new ConfigurationController(this);
	}

	public List<Violation> getAllViolations(){
		return configuration.getAllViolations();
	}

	@Override
	public ViolationDTO[] getViolationsByLogicalPath(String logicalpathFrom, String logicalpathTo) {
		return filterController.getViolationsByLogicalPath(logicalpathFrom, logicalpathTo);
	}

	@Override
	public void setFilterValues(ArrayList<String> ruletypesKeys,
			ArrayList<String> violationtypesKeys,
			ArrayList<String> paths, boolean hideFilter) {
		filterController.setFilterValues(ruletypesKeys, violationtypesKeys, paths, hideFilter);
	}

	@Override
	public ArrayList<Violation> applyFilterViolations(Boolean applyfilter) {
		return filterController.filterViolations(applyfilter);
	}

	@Override
	public ArrayList<String> loadRuletypesForFilter() {
		return filterController.loadRuletypes();
	}

	@Override
	public ArrayList<String> loadViolationtypesForFilter() {
		return filterController.loadViolationtypes();
	}

	@Override
	public HashMap<String, List<RuleType>> getRuletypes(String language) {
		return domain.getAllRuleTypes(language);
	}
	
	

	@Override
	public List<Severity> getAllSeverities(){
		return configuration.getAllSeverities();
	}

	public String[] getAvailableLanguages(){
		return acs.getAvailableLanguages();
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

	@Override
	public Map<String, List<ViolationType>> getViolationTypes(
			String language) {
		return domain.getAllViolationTypes(language);
	}
}
