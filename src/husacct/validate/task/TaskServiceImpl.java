package husacct.validate.task;

import husacct.common.dto.ViolationDTO;
import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.DomainServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.task.filter.FilterController;

import java.util.ArrayList;
import java.util.List;


public class TaskServiceImpl implements ITaskService{
	private FilterController fc;
	private final ConfigurationServiceImpl configuration;
	private DomainServiceImpl domain;

	public TaskServiceImpl(ConfigurationServiceImpl configuration, DomainServiceImpl domain) {
		this.configuration = configuration;
		this.domain = domain;
		fc = new FilterController(this);
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
	public List<RuleType> getRuletypes() {
		return domain.getAllRuleTypes();
	}

	@Override
	public void getViolationtypes(String ruletypeKey) {
	}

	@Override
	public List<Severity> getAllSeverities(){
		return configuration.getAllSeverities();
	}

}
