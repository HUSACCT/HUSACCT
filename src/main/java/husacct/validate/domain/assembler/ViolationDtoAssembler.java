package husacct.validate.domain.assembler;

import husacct.common.dto.ViolationDTO;
import husacct.validate.domain.validation.Violation;
import java.util.ArrayList;
import java.util.List;

public class ViolationDtoAssembler {

	public ViolationDtoAssembler() {
	}

	public List<ViolationDTO> createViolationDTO(List<Violation> violations) {
		List<ViolationDTO> violationDTOList = new ArrayList<>();

		for (Violation violation : violations) {
			ViolationDTO violationDTO = createViolationDTO(violation);
			violationDTOList.add(violationDTO);
		}
		return violationDTOList;
	}

	private ViolationDTO createViolationDTO(Violation violation) {
		final String classPathFrom = violation.getClassPathFrom();
		final String classPathTo = violation.getClassPathTo();
		final String logicalModuleFromPath = violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath();
		final String logicalModuleToPath = violation.getLogicalModules().getLogicalModuleTo().getLogicalModulePath();
		final String dependencySubType = violation.getDependencySubType();
		final int linenumber = violation.getLinenumber();
		final String violationTypeKey = violation.getViolationTypeKey();
		final String ruleTypeKey = violation.getRuletypeKey();
		final boolean isIndirect = violation.getIsIndirect();
		String severityKey = "";
		if (violation.getSeverity() != null) {
			severityKey = violation.getSeverity().getSeverityKeyTranslated();
		}
		return new ViolationDTO(classPathFrom, classPathTo, logicalModuleFromPath, logicalModuleToPath, violationTypeKey, ruleTypeKey, dependencySubType, linenumber, severityKey, isIndirect);
	}

}