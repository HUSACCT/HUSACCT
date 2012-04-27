package husacct.validate.domain.assembler;

import husacct.common.dto.ViolationTypeDTO;
import husacct.validate.domain.validation.ViolationType;

import java.util.ArrayList;
import java.util.List;

public class ViolationtypeAssembler {
	public ViolationTypeDTO[] createViolationTypeDTO(List<ViolationType> violationtypes){
		List<ViolationTypeDTO> violationtypeDTOs = new ArrayList<ViolationTypeDTO>();

		for(ViolationType violationtype:violationtypes){
			ViolationTypeDTO dto = createViolationTypeDTO(violationtype);
			violationtypeDTOs.add(dto);
		}
		return violationtypeDTOs.toArray(new ViolationTypeDTO[violationtypeDTOs.size()]);
	}

	public ViolationTypeDTO createViolationTypeDTO(ViolationType violationtype){
		String key = violationtype.getViolationtypeKey();
		String description = violationtype.getViolationDescriptionKey();
		boolean isActive = violationtype.isActive();

		return new ViolationTypeDTO(key, description, isActive);
	}
}