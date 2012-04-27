package husacct.validate.domain.assembler;

import java.util.List;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;

public class AssemblerController {
	public CategoryDTO[] createCategoryDTO(List<RuleType> rules){
		CategoryAssembler assembler = new CategoryAssembler();
		return assembler.createCategoryDTO(rules);
	}

	public RuleTypeDTO createRuleTypeDTO(RuleType rule, ViolationType violationtype){
		RuletypeAssembler assembler = new RuletypeAssembler();
		return assembler.createRuleTypeDTO(rule, violationtype);
	}
}