package husacct.validate.domain.assembler;

import java.util.List;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.validate.domain.ruletype.Rule;
import husacct.validate.domain.violationtype.ViolationType;

public class AssemblerController {	
	public CategoryDTO[] createCategoryDTO(List<Rule> rules){
		CategoryAssembler assembler = new CategoryAssembler();
		return assembler.createCategoryDTO(rules);
	}
	
	public RuleTypeDTO createRuleTypeDTO(Rule rule, ViolationType violationtype){
		RuletypeAssembler assembler = new RuletypeAssembler();
		return assembler.createRuleTypeDTO(rule, violationtype);
	}
}