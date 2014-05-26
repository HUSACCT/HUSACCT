package husacct.validate.task.filter;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.ViolationDTO;
import husacct.common.locale.ILocaleService;
import husacct.define.IDefineService;
import husacct.validate.domain.assembler.ViolationAssembler;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.factory.ruletype.RuleTypesFactory;
import husacct.validate.domain.validation.Regex;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.internaltransferobjects.PathDTO;
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
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();

	public FilterController(TaskServiceImpl ts, RuleTypesFactory ruletypesfactory, ConfigurationServiceImpl configuration) {
		this.taskServiceImpl = ts;
		this.ruletypesfactory = ruletypesfactory;
		this.configuration = configuration;
	}

	public void setFilterValues(PathDTO dto, boolean hideFilter, List<Violation> violations) {
		ArrayList<String> modulesFilter = new ArrayList<String>();
		for (Violation violation : violations) {
			for (String path : dto.getPaths()) {
				if (!modulesFilter.contains(violation.getClassPathFrom()) && Regex.matchRegex(Regex.makeRegexString(path), violation.getClassPathFrom())) {
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
			if (hidefilter && (!ruletypes.contains(localeService.getTranslatedString(violation.getRuletypeKey())) && !violationtypes.contains(localeService.getTranslatedString(violation.getViolationTypeKey())) && !paths.contains(violation.getClassPathFrom()))) {
				filteredViolations.add(violation);
			} else if ((!hidefilter) && (ruletypes.contains(localeService.getTranslatedString(violation.getRuletypeKey())) || violationtypes.contains(localeService.getTranslatedString(violation.getViolationTypeKey())) || paths.contains(violation.getClassPathFrom()))) {
				filteredViolations.add(violation);
			}
		}
		return filteredViolations;
	}

	public ArrayList<String> loadRuletypes(List<Violation> violations) {
		ArrayList<String> AppliedRuletypes = new ArrayList<String>();

		for (Violation violation : violations) {
			if (!AppliedRuletypes.contains(localeService.getTranslatedString(violation.getRuletypeKey()))) {
				AppliedRuletypes.add(localeService.getTranslatedString(violation.getRuletypeKey()));
			}
		}

		return AppliedRuletypes;
	}

	public ArrayList<String> loadViolationtypes(List<Violation> violations) {
		ArrayList<String> appliedViolationtypes = new ArrayList<String>();

		for (Violation violation : violations) {
			String violationTypeKey = violation.getViolationTypeKey();
			if ((violationTypeKey != null) && (violationTypeKey != "")){
				if (!appliedViolationtypes.contains(localeService.getTranslatedString(violationTypeKey))) {
					appliedViolationtypes.add(localeService.getTranslatedString(violationTypeKey));
				}
			}
			appliedViolationtypes.add("-");
		}
		return appliedViolationtypes;
	}

	public ViolationDTO[] getViolationsByLogicalPath(String logicalpathFrom, String logicalpathTo) {
		IDefineService defineService = ServiceProvider.getInstance().getDefineService();
		@SuppressWarnings("unchecked")
		List<String> physicalPathsFrom = (List<String>) defineService.getModule_AllPhysicalClassPathsOfModule(logicalpathFrom);
		@SuppressWarnings("unchecked")
		List<String> physicalPathsTo = (List<String>) defineService.getModule_AllPhysicalClassPathsOfModule(logicalpathTo);
		ViolationDTO[] returnValue = getViolationsByPhysicalPathLists(physicalPathsFrom, physicalPathsTo);
		return returnValue;
	}

	public ViolationDTO[] getViolationsByPhysicalPath(String physicalPathFrom, String physicalPathTo) {
		IAnalyseService analyseService = ServiceProvider.getInstance().getAnalyseService();
		List<String> physicalPathsFrom = analyseService.getAllPhysicalClassPathsOfSoftwareUnit(physicalPathFrom);
		List<String> physicalPathsTo = analyseService.getAllPhysicalClassPathsOfSoftwareUnit(physicalPathTo);
		ViolationDTO[] returnValue = getViolationsByPhysicalPathLists(physicalPathsFrom, physicalPathsTo);
		return returnValue;
	}

	private ViolationDTO[] getViolationsByPhysicalPathLists(List<String> physicalPathsFrom, List<String> physicalPathsTo){
		List<Violation> violations = new  ArrayList<Violation>();
		for (String pathFrom : physicalPathsFrom){
			for (String pathTo : physicalPathsTo){
				violations.addAll(configuration.getViolationsFromTo(pathFrom, pathTo));
			}
		}
		ViolationAssembler assembler = new ViolationAssembler(ruletypesfactory, configuration);
		List<ViolationDTO> violationDTOs = assembler.createViolationDTO(violations);
		ViolationDTO[] returnValue = violationDTOs.toArray(new ViolationDTO[violationDTOs.size()]);
		return returnValue;
	}
	
	public LinkedHashMap<Severity, Integer> getViolationsPerSeverity(List<Violation> shownViolations, List<Severity> severities) {
		LinkedHashMap<Severity, Integer> violationsPerSeverity = new LinkedHashMap<Severity, Integer>();

		for (Severity severity : severities) {
			violationsPerSeverity.put(severity, 0);
		}

		for (Violation violation : shownViolations) {
			if (violation.getSeverity() != null) {
				int count = 0;
				try {
					count = violationsPerSeverity.get(violation.getSeverity());
				} catch (Exception e) {
				} finally {
					violationsPerSeverity.remove(violation.getSeverity());
					count = count + 1;
					violationsPerSeverity.put(violation.getSeverity(), count);
				}

			}
		}

		for (Severity severity : severities) {
			int amount = violationsPerSeverity.get(severity);
			violationsPerSeverity.remove(severity);
			violationsPerSeverity.put(severity, amount);
		}

		return violationsPerSeverity;
	}

	public ArrayList<String> getEnabledFilterRuleTypes() {
		return this.ruletypes;
	}

	public ArrayList<String> getEnabledFilterViolations() {
		return this.violationtypes;
	}
	
	public ArrayList<String> getEnabledFilterPaths() {
		return this.paths;
	}
}