package husacct.validate.domain.assembler;

import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationTypeDTO;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.ArrayList;
import java.util.List;

public class RuletypeAssembler {
	private ViolationtypeAssembler violationtypeassembler;

	public RuletypeAssembler(){
		this.violationtypeassembler = new ViolationtypeAssembler();
	}

	public RuleTypeDTO[] createRuleTypeDTO(List<RuleType> rules){
		List<RuleTypeDTO> ruletypes = new ArrayList<RuleTypeDTO>();

		for(RuleType rule: rules){
			RuleTypeDTO dto = createRuleTypeDTOWithViolationtypes(rule);
			ruletypes.add(dto);
		}
		return ruletypes.toArray(new RuleTypeDTO[ruletypes.size()]);
	}

	private RuleTypeDTO createRuleTypeDTOWithViolationtypes(RuleType rule){
		String key = rule.getKey();
		String description = rule.getDescriptionKey();
		ViolationTypeDTO[] violationtypes = violationtypeassembler.createViolationTypeDTO(rule.getViolationTypes());
		return new RuleTypeDTO(key, description, violationtypes, new RuleTypeDTO[]{});
	}

	public RuleTypeDTO createRuleTypeDTO(RuleType rule, ViolationType violationtype){
		String key = rule.getKey();
		String description = rule.getDescriptionKey();
		ViolationTypeDTO violationtypeDTO = violationtypeassembler.createViolationTypeDTO(violationtype);

		return new RuleTypeDTO(key, description, new ViolationTypeDTO[]{violationtypeDTO}, new RuleTypeDTO[]{});
	}
}