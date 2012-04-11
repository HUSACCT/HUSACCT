package husacct.validate.domain.assembler;

import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.dto.ViolationTypeDTO;
import husacct.validate.domain.factory.AbstractViolationType;
import husacct.validate.domain.factory.RuletypesFactory;
import husacct.validate.domain.factory.ViolationTypeFactory;
import husacct.validate.domain.ruletype.Rule;
import husacct.validate.domain.violation.Violation;
import husacct.validate.domain.violationtype.ViolationType;

import java.util.ArrayList;
import java.util.List;

public class ViolationAssembler {
	private AbstractViolationType violationtypeFactory;
	private RuletypesFactory ruleFactory;
	private RuletypeAssembler ruleAssembler;

	public ViolationAssembler(){
		ViolationTypeFactory abstractViolationtypeFactory = new ViolationTypeFactory();		
		this.violationtypeFactory = abstractViolationtypeFactory.getViolationTypeFactory();

		this.ruleFactory = new RuletypesFactory();
		this.ruleAssembler = new RuletypeAssembler();
	}

	public List<ViolationDTO> createViolationDTO(List<Violation> violations) {
		List<ViolationDTO> violationDTOList = new ArrayList<ViolationDTO>();

		for (Violation violation : violations) {

			violationDTOList.add(createViolationDTO(violation));
		}
		return violationDTOList;
	}

	private ViolationDTO createViolationDTO(Violation violation){
		RuleTypeDTO rule = createRuleTypeDTO(violation);	
		ViolationTypeDTO violationtype = rule.getViolationTypes()[0];
		return new ViolationDTO(violation.getClassPathFrom(),violation.getClassPathTo(), violation.getMessage(), violation.getLogicalModuleFrom(), violation.getLogicalModuleTo(), violationtype, rule);
	}

	private RuleTypeDTO createRuleTypeDTO(Violation violation){
		ViolationType violationtype = violationtypeFactory.createViolationType(violation.getViolationtypeKey());
		Rule rule = ruleFactory.generateRuleType(violation.getRuletypeKey());

		RuleTypeDTO ruleDTO = ruleAssembler.createRuleTypeDTO(rule, violationtype);
		return ruleDTO;
	}
}