package husacct.validate.task;

import husacct.common.dto.ViolationDTO;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface ITaskService {
	public ViolationDTO[] getViolations(String logicalpathFrom, String logicalpathTo);
	public void setFilterValues(ArrayList<String> ruletypes, ArrayList<String> violationtypes, ArrayList<String> paths, boolean hideFilter);
	public ArrayList<Violation> filterViolations(Boolean applyfilter);
	public ArrayList<String> loadRuletypes();
	public ArrayList<String> loadViolationtypes();

	public HashMap<String, List<RuleType>> getRuletypes(String language);
//	public void getViolationtypes(String ruletypeKey);
	public List<Severity> getAllSeverities();
}
