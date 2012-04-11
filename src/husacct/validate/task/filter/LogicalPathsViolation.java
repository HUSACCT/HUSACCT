package husacct.validate.task.filter;

import husacct.common.dto.ViolationDTO;
import husacct.validate.domain.assembler.ViolationAssembler;
import husacct.validate.domain.check.CheckConformanceController;
import husacct.validate.domain.violation.Violation;
import husacct.validate.domain.violation.ViolationStub;
import husacct.validate.task.BrowseViolationController;
import java.util.ArrayList;
import java.util.List;

public class LogicalPathsViolation {
	private BrowseViolationController bvc;
	private List<Violation> allViolations = new ViolationStub().getViolations();

	public LogicalPathsViolation(){

	}

	public LogicalPathsViolation(BrowseViolationController bvc) {
		this.bvc = bvc;
	}

	public ViolationDTO[] getViolations(String logicalpathFrom, String logicalpathTo, CheckConformanceController conformance) {
		ViolationAssembler assembler = new ViolationAssembler();
		List<Violation> violations = conformance.getViolations();

		for (Violation violation : allViolations) {
			if (violation.getLogicalModuleFrom().contains(logicalpathFrom)) {
				if (violation.getLogicalModuleTo().contains(logicalpathFrom)) {
					violations.add(violation);
				} else if (violation.getLogicalModuleTo().contains(logicalpathTo)) {
					violations.add(violation);
				}
			}
		}
		List<ViolationDTO> violationDTOs = assembler.createViolationDTO(violations);
		return violationDTOs.toArray(new ViolationDTO[violationDTOs.size()]);
	}

	public void setFilterValues(ArrayList<String> ruletypes, ArrayList<String> violationtypes, ArrayList<String> paths, Boolean hideFilter) {
		Regex regex = new Regex();
		ArrayList<String> modulesFilter = new ArrayList<String>();
		for(Violation violation : allViolations){
			for(String path : paths){
				if(regex.matchRegex(regex.makeRegexString(path), violation.getLogicalModuleFrom())){
					modulesFilter.add(violation.getLogicalModuleFrom());
				}
			}
		}
		if(bvc != null){
			bvc.setRuletypes(ruletypes);
			bvc.setViolationtypes(violationtypes);
			bvc.setPaths(modulesFilter);
			bvc.setHidefilter(hideFilter);
		}
	}
}