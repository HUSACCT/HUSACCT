package husacct.validate.task.filter;

import husacct.ServiceProvider;
import husacct.common.dto.ViolationDTO;
import husacct.validate.domain.assembler.ViolationAssembler;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.factory.ruletype.RuleTypesFactory;
import husacct.validate.domain.validation.Regex;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.internal_transfer_objects.PathDTO;
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

	public void setFilterValues(PathDTO dto, boolean hideFilter, List<Violation> violations) {
		ArrayList<String> modulesFilter = new ArrayList<String>();
		for(Violation violation : violations){
			for(String path : dto.getPaths()){
				if(!modulesFilter.contains(violation.getClassPathFrom()) && Regex.matchRegex(Regex.makeRegexString(path), violation.getClassPathFrom())){
					modulesFilter.add(violation.getClassPathFrom());
				}
			}
		}
		this.ruletypes = dto.getRuletypes();
		this.violationtypes = dto.getViolationtypes();
		this.paths = modulesFilter;
		this.hidefilter = hideFilter;
	}

	public ArrayList<Violation> filterViolations(List<Violation> violations) {
		ArrayList<Violation> filteredViolations = new ArrayList<Violation>();
		for (Violation violation : violations) {
			if (hidefilter && ( !ruletypes.contains(ServiceProvider.getInstance().getLocaleService().getTranslatedString(violation.getRuletypeKey())) && !violationtypes.contains(ServiceProvider.getInstance().getLocaleService().getTranslatedString(violation.getViolationtypeKey())) && !paths.contains(violation.getClassPathFrom()) ) ) {
				filteredViolations.add(violation);
			} else if ((!hidefilter) && (ruletypes.contains(ServiceProvider.getInstance().getLocaleService().getTranslatedString(violation.getRuletypeKey())) || violationtypes.contains(ServiceProvider.getInstance().getLocaleService().getTranslatedString(violation.getViolationtypeKey())) || paths.contains(violation.getClassPathFrom()) ) ) {
				filteredViolations.add(violation);
			}
		}
		return filteredViolations;
	}

	public ArrayList<String> loadRuletypes(List<Violation> violations){
		ArrayList<String> AppliedRuletypes = new ArrayList<String>();

		for (Violation violation : violations) {
			if(!AppliedRuletypes.contains(ServiceProvider.getInstance().getLocaleService().getTranslatedString(violation.getRuletypeKey()))){
				AppliedRuletypes.add(ServiceProvider.getInstance().getLocaleService().getTranslatedString(violation.getRuletypeKey()));
			}
		}

		return AppliedRuletypes;
	}

	public ArrayList<String> loadViolationtypes(List<Violation> violations){
		ArrayList<String> appliedViolationtypes = new ArrayList<String>();

		for (Violation violation : violations) {

			if(!appliedViolationtypes.contains(ServiceProvider.getInstance().getLocaleService().getTranslatedString(violation.getViolationtypeKey()))){
				appliedViolationtypes.add(ServiceProvider.getInstance().getLocaleService().getTranslatedString(violation.getViolationtypeKey()));
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

	public LinkedHashMap<Severity, Integer> getViolationsPerSeverity(List<Violation> shownViolations, List<Severity> severities) {
		LinkedHashMap<Severity, Integer> violationsPerSeverity = new LinkedHashMap<Severity, Integer>();

		for(Severity severity : severities){
			violationsPerSeverity.put(severity, 0);
		}
		
		for(Violation violation : shownViolations) {
			if(violation.getSeverity() != null) {
				int count = 0;
				try{
					count = violationsPerSeverity.get(violation.getSeverity());
				} catch(Exception e){
				} finally{
					violationsPerSeverity.remove(violation.getSeverity());
					count = count + 1;
					violationsPerSeverity.put(violation.getSeverity(), count);
				}
				
			}
		}

		for(Severity severity : severities){
			int amount = violationsPerSeverity.get(severity);
			violationsPerSeverity.remove(severity);
			violationsPerSeverity.put(severity, amount);
		}
		
		return violationsPerSeverity;
	}
}