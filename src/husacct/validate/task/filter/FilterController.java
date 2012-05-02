package husacct.validate.task.filter;

import husacct.common.dto.ViolationDTO;
import husacct.validate.abstraction.language.ResourceBundles;
import husacct.validate.domain.assembler.ViolationAssembler;
import husacct.validate.domain.validation.Violation;
import husacct.validate.task.TaskServiceImpl;
import java.util.ArrayList;
import java.util.List;

public class FilterController {
	private TaskServiceImpl taskServiceImpl;

	private ArrayList<String> ruletypes = new ArrayList<String>();
	private ArrayList<String> violationtypes = new ArrayList<String>();
	private ArrayList<String> paths = new ArrayList<String>();
	private boolean hidefilter = true;


	public FilterController(TaskServiceImpl ts){
		this.taskServiceImpl = ts;
	}
	public void setFilterValues(ArrayList<String> ruletypes, ArrayList<String> violationtypes, ArrayList<String> paths, Boolean hideFilter) {
		Regex regex = new Regex();
		ArrayList<String> modulesFilter = new ArrayList<String>();
		for(Violation violation : taskServiceImpl.getAllViolations()){
			for(String path : paths){
				if(!modulesFilter.contains(violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath()) && regex.matchRegex(regex.makeRegexString(path), violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath())){
					modulesFilter.add(violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath());
				}
			}
		}
		this.ruletypes = ruletypes;
		this.violationtypes = violationtypes;
		this.paths = modulesFilter;
		this.hidefilter = hideFilter;
	}

	public ArrayList<Violation> filterViolations(Boolean applyfilter) {
		ArrayList<Violation> tempViolations = new ArrayList<Violation>();

		for (Violation violation : taskServiceImpl.getAllViolations()) {
			if(applyfilter){
				if (hidefilter && ( !ruletypes.contains(ResourceBundles.getValue(violation.getRuletypeKey())) && !violationtypes.contains(ResourceBundles.getValue(violation.getViolationtypeKey())) && !paths.contains(violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath()) ) ) {
						tempViolations.add(violation);
				} else if ((!hidefilter) && (ruletypes.contains(ResourceBundles.getValue(violation.getRuletypeKey())) || violationtypes.contains(ResourceBundles.getValue(violation.getViolationtypeKey())) || paths.contains(violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath()) ) ) {
						tempViolations.add(violation);
				}
			} else{
				tempViolations.add(violation);
			}
		}

		return tempViolations;
	}

	public ArrayList<String> loadRuletypes(){
		ArrayList<String> AppliedRuletypes = new ArrayList<String>();

		for (Violation violation : taskServiceImpl.getAllViolations()) {
			if(!AppliedRuletypes.contains(ResourceBundles.getValue(violation.getRuletypeKey()))){
				AppliedRuletypes.add(ResourceBundles.getValue(violation.getRuletypeKey()));
			}
		}

		return AppliedRuletypes;
	}

	public ArrayList<String> loadViolationtypes(){
		ArrayList<String> AppliedViolationtypes = new ArrayList<String>();

		for (Violation violation : taskServiceImpl.getAllViolations()) {
			if(!AppliedViolationtypes.contains(ResourceBundles.getValue(violation.getViolationtypeKey()))){
				AppliedViolationtypes.add(ResourceBundles.getValue(violation.getViolationtypeKey()));
			}
		}

		return AppliedViolationtypes;
	}
	public ViolationDTO[] getViolationsByLogicalPath(String logicalpathFrom, String logicalpathTo) {
		ViolationAssembler assembler = new ViolationAssembler();
		ArrayList<Violation> violations = new ArrayList<Violation>();

		for (Violation violation : taskServiceImpl.getAllViolations()) {
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

	public ViolationDTO[] getViolationsByPhysicalPath(String physicalPathFrom,
			String physicalPathTo) {
		List<Violation> violations = new ArrayList<Violation>();
		for (Violation violation : taskServiceImpl.getAllViolations()) {
			if(violation.getClassPathFrom().startsWith(physicalPathFrom) || violation.getClassPathTo().startsWith(physicalPathTo)) {
				violations.add(violation);
			}
		}
		ViolationAssembler assembler = new ViolationAssembler();
		List<ViolationDTO> violationDTOs = assembler.createViolationDTO(violations);
		return violationDTOs.toArray(new ViolationDTO[violationDTOs.size()]);
	}
}