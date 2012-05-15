package husacct.validate.task.filter;

import husacct.common.dto.ViolationDTO;
import husacct.validate.abstraction.language.ValidateTranslator;
import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.assembler.ViolationAssembler;
import husacct.validate.domain.factory.ruletype.RuleTypesFactory;
import husacct.validate.domain.validation.Regex;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.task.TaskServiceImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FilterController {
	private final TaskServiceImpl taskServiceImpl;
	private final RuleTypesFactory ruletypesfactory;
	private final ConfigurationServiceImpl configuration;

	private ArrayList<String> ruletypes = new ArrayList<String>();
	private ArrayList<String> violationtypes = new ArrayList<String>();
	private ArrayList<String> paths = new ArrayList<String>();
	private boolean hidefilter = true;

	public FilterController(TaskServiceImpl ts, RuleTypesFactory ruletypesfactory, ConfigurationServiceImpl configuration){
		this.taskServiceImpl = ts;
		this.ruletypesfactory = ruletypesfactory;
		this.configuration = configuration;
	}

	public void setFilterValues(ArrayList<String> ruletypes, ArrayList<String> violationtypes, ArrayList<String> paths, Boolean hideFilter, List<Violation> violations) {
		Regex regex = new Regex();
		ArrayList<String> modulesFilter = new ArrayList<String>();
		for(Violation violation : violations){
			for(String path : paths){
				if(!modulesFilter.contains(violation.getClassPathFrom()) && regex.matchRegex(regex.makeRegexString(path), violation.getClassPathFrom())){
					modulesFilter.add(violation.getClassPathFrom());
				}
			}
		}
		this.ruletypes = ruletypes;
		this.violationtypes = violationtypes;
		this.paths = modulesFilter;
		this.hidefilter = hideFilter;
	}

	public ArrayList<Violation> filterViolations(Boolean applyfilter, List<Violation> violations) {
		ArrayList<Violation> filteredViolations = new ArrayList<Violation>();

		for (Violation violation : violations) {
			if(applyfilter){
				if (hidefilter && ( !ruletypes.contains(ValidateTranslator.getValue(violation.getRuletypeKey())) && !violationtypes.contains(ValidateTranslator.getValue(violation.getViolationtypeKey())) && !paths.contains(violation.getClassPathFrom()) ) ) {
					filteredViolations.add(violation);
				} else if ((!hidefilter) && (ruletypes.contains(ValidateTranslator.getValue(violation.getRuletypeKey())) || violationtypes.contains(ValidateTranslator.getValue(violation.getViolationtypeKey())) || paths.contains(violation.getClassPathFrom()) ) ) {
					filteredViolations.add(violation);
				}
			} else{
				filteredViolations.add(violation);
			}
		}

		return filteredViolations;
	}

	public ArrayList<String> loadRuletypes(List<Violation> violations){
		ArrayList<String> AppliedRuletypes = new ArrayList<String>();

		for (Violation violation : violations) {
			if(!AppliedRuletypes.contains(ValidateTranslator.getValue(violation.getRuletypeKey()))){
				AppliedRuletypes.add(ValidateTranslator.getValue(violation.getRuletypeKey()));
			}
		}

		return AppliedRuletypes;
	}

	public ArrayList<String> loadViolationtypes(List<Violation> violations){
		ArrayList<String> appliedViolationtypes = new ArrayList<String>();

		for (Violation violation : violations) {

			if(!appliedViolationtypes.contains(ValidateTranslator.getValue(violation.getViolationtypeKey()))){
				appliedViolationtypes.add(ValidateTranslator.getValue(violation.getViolationtypeKey()));
			}
		}
		return appliedViolationtypes;
	}
	public ViolationDTO[] getViolationsByLogicalPath(String logicalpathFrom, String logicalpathTo) {
		ViolationAssembler assembler = new ViolationAssembler(ruletypesfactory, configuration);
		ArrayList<Violation> violations = new ArrayList<Violation>();
		for (Violation violation : taskServiceImpl.getAllViolations().getValue()) {
			if (violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath().startsWith(logicalpathFrom)) {
				if (violation.getLogicalModules().getLogicalModuleTo().getLogicalModulePath().startsWith(logicalpathFrom)) {
					violations.add(violation);
				} else if (violation.getLogicalModules().getLogicalModuleTo().getLogicalModulePath().startsWith(logicalpathTo)) {
					violations.add(violation);
				}
			}
		}
		List<ViolationDTO> violationDTOs = assembler.createViolationDTO(violations);
		return violationDTOs.toArray(new ViolationDTO[violationDTOs.size()]);
	}

	public ViolationDTO[] getViolationsByPhysicalPath(String physicalPathFrom, String physicalPathTo) {
		List<Violation> violations = new ArrayList<Violation>();
		for (Violation violation : taskServiceImpl.getAllViolations().getValue()) {
			if(violation.getClassPathFrom().startsWith(physicalPathFrom) && violation.getClassPathTo().startsWith(physicalPathTo)) {
				violations.add(violation);
			}
			else if(violation.getClassPathFrom().startsWith(physicalPathFrom) && physicalPathFrom.equals(physicalPathTo) && violation.getClassPathTo().isEmpty()){
				violations.add(violation);
			}
		}
		ViolationAssembler assembler = new ViolationAssembler(ruletypesfactory, configuration);
		List<ViolationDTO> violationDTOs = assembler.createViolationDTO(violations);

		return violationDTOs.toArray(new ViolationDTO[violationDTOs.size()]);
	}
	
	public LinkedHashMap<Severity, Integer> getViolationsPerSeverity(boolean applyFilter) {
		LinkedHashMap<Severity, Integer> violationsPerSeverity = new LinkedHashMap<Severity, Integer>();
		for(Severity severity : taskServiceImpl.getAllSeverities()) {
			int violationsCount = 0;
			List<Violation> violations;
			if(!applyFilter) {
				violations = taskServiceImpl.getAllViolations().getValue();
			} else {
				violations = taskServiceImpl.applyFilterViolations(true, null);//TODO set Date!!
			}
			for(Violation violation : violations) {
				if(violation.getSeverity() != null) {
					if(violation.getSeverity().equals(severity)) {
						violationsCount++;
					}
				}
			}

			violationsPerSeverity.put(severity, violationsCount);
		}
		return violationsPerSeverity;
	}
}