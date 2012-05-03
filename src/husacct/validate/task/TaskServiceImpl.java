package husacct.validate.task;

import husacct.analyse.AnalyseServiceStub;
import husacct.common.dto.ViolationDTO;
import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.DomainServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.task.export.ExportController;
import husacct.validate.task.extensiontypes.ExtensionTypes;
import husacct.validate.task.fetch.ImportController;
import husacct.validate.task.filter.FilterController;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;

import org.jdom2.Element;


public class TaskServiceImpl implements ITaskService{
	private final FilterController filterController;
	private final ConfigurationController configurationController;
	private final ConfigurationServiceImpl configuration;
	private final DomainServiceImpl domain;
	private final AnalyseServiceStub acs;


	public TaskServiceImpl(ConfigurationServiceImpl configuration, DomainServiceImpl domain) {
		this.configuration = configuration;
		this.domain = domain;				
		filterController = new FilterController(this, domain.getRuleTypesFactory(), configuration);
		acs = new AnalyseServiceStub();
		configurationController = new ConfigurationController(this);
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

		configuration.setSeveritiesPerTypesPerProgrammingLanguages(language.toLowerCase(), map);
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
	
	public Severity getSeverityFromKey(String language, String key){
		return configuration.getSeverityFromKey(language, key);
	}
	
	public void importValidationWorkspace(Element element) throws DatatypeConfigurationException   {
		ImportController importController = new ImportController(element);
		configuration.addSeverities(importController.getSeverities());
		configuration.addViolations(importController.getViolations());
		configuration.setSeveritiesPerTypesPerProgrammingLanguages(importController.getSeveritiesPerTypesPerProgrammingLanguages());
	}
	public Element exportValidationWorkspace() {
		Element rootValidateElement = new Element("validate");
		ExportController exportController = new ExportController();
		rootValidateElement.addContent(exportController.exportViolationsXML(configuration.getAllViolations()));
		rootValidateElement.addContent(exportController.exportSeveritiesXML(configuration.getAllSeverities()));
		rootValidateElement.addContent(exportController.exportSeveritiesPerTypesPerProgrammingLanguagesXML(configuration.getAllSeveritiesPerTypesPerProgrammingLanguages()));
		return rootValidateElement;
	}
	
	public String[] getExportExtentions() {
		return new ExtensionTypes().getExtensionTypes();
	}
}
