package husacct.validate.task;

import husacct.analyse.AnalyseServiceStub;
import husacct.common.dto.ViolationDTO;
import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.DomainServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.task.TableModels.ColorTableModel;
import husacct.validate.task.TableModels.ComboBoxTableModel;
import husacct.validate.task.filter.FilterController;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TaskServiceImpl implements ITaskService{
	private final FilterController fc;
	private final ConfigurationController conficurationController;
	private final ConfigurationServiceImpl configuration;
	private final DomainServiceImpl domain;
	private final AnalyseServiceStub acs;


	public TaskServiceImpl(ConfigurationServiceImpl configuration, DomainServiceImpl domain) {
		this.configuration = configuration;
		this.domain = domain;
		fc = new FilterController(this);
		acs = new AnalyseServiceStub();
		conficurationController = new ConfigurationController();
	}

	public List<Violation> getAllViolations(){
		return configuration.getAllViolations();
	}

	@Override
	public ViolationDTO[] getViolations(String logicalpathFrom, String logicalpathTo) {
		return fc.getViolations(logicalpathFrom, logicalpathTo);
	}

	@Override
	public void setFilterValues(ArrayList<String> ruletypes,
			ArrayList<String> violationtypes,
			ArrayList<String> paths, boolean hideFilter) {
		fc.setFilterValues(ruletypes, violationtypes, paths, hideFilter);
	}

	@Override
	public ArrayList<Violation> filterViolations(Boolean applyfilter) {
		return fc.filterViolations(applyfilter);
	}

	@Override
	public ArrayList<String> loadRuletypes() {
		return fc.loadRuletypes();
	}

	@Override
	public ArrayList<String> loadViolationtypes() {
		return fc.loadViolationtypes();
	}

	@Override
	public List<RuleType> getRuletypes(String language) {
		return domain.getAllRuleTypes(language);
	}

	@Override
	public List<Severity> getAllSeverities(){
		return configuration.getAllSeverities();
	}

	public void addSeverities(List<Severity> severities) {
		configuration.addSeverities(severities);
	}

	public String[] getAvailableLanguages(){
		return acs.getAvailableLanguages();
	}

	public void ApplySeverities(ColorTableModel ctm){
		List<Severity> severityList = new ArrayList<Severity>();
		for (int i = 0; i < ctm.getRowCount(); i++) {
			try{
				Severity s = getAllSeverities().get(i);
				s.setColor((Color) ctm.getValueAt(i, 1));
				s.setUserName((String) ctm.getValueAt(i, 0));
				s.setValue(i + 1);
				severityList.add(s);
			} catch(Exception e){
				Severity s = new Severity();
				s.setColor((Color) ctm.getValueAt(i, 1));
				s.setUserName((String) ctm.getValueAt(i, 0));
				s.setValue(i + 1);
				severityList.add(s);
			}
		}
		addSeverities(severityList);
	}

	public void UpdateRuletype(ComboBoxTableModel ruletypeModel, ComboBoxTableModel violationtypeModel, String language){
		List<RuleType> ruletypes = getRuletypes(language);
		for (int i = 0; i < ruletypeModel.getRowCount(); i++) {
			HashMap<String, Severity> ruletypeKeySeverityMap = new HashMap<String, Severity>();
			ruletypeKeySeverityMap.put(ruletypes.get(i).getKey(), (Severity)ruletypeModel.getValueAt(i, 1));
			HashMap<String, HashMap<String, Severity>> languageKeySeverityMap = new HashMap<String, HashMap<String, Severity>>();
			languageKeySeverityMap.put(language, ruletypeKeySeverityMap);
			configuration.setSeveritiesPerTypesPerProgrammingLanguages(languageKeySeverityMap);

			List<ViolationType> vt = ruletypes.get(i).getViolationTypes();

			for (int j = 0; j < violationtypeModel.getRowCount(); j++) {
				vt.get(j).setActive((Boolean) violationtypeModel.getValueAt(j, 2));
				HashMap<String, Severity> violationtypeKeySeverityMap = new HashMap<String, Severity>();
				violationtypeKeySeverityMap.put(vt.get(i).getViolationtypeKey(), (Severity)violationtypeModel.getValueAt(j, 1));
				HashMap<String, HashMap<String, Severity>> languageViolationtypeKeySeverityMap = new HashMap<String, HashMap<String, Severity>>();
				languageViolationtypeKeySeverityMap.put(language, violationtypeKeySeverityMap);
				configuration.setSeveritiesPerTypesPerProgrammingLanguages(languageViolationtypeKeySeverityMap);
			}
		}
	}
}
