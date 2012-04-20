package husacct.validate.domain.assembler;

import husacct.common.dto.MessageDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.dto.ViolationTypeDTO;
import husacct.validate.domain.rulefactory.RuletypesFactory;
import husacct.validate.domain.rulefactory.ViolationTypeFactory;
import husacct.validate.domain.rulefactory.violationtypeutil.AbstractViolationType;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.ArrayList;
import java.util.List;

public class ViolationAssembler {
	private AbstractViolationType violationtypeFactory;
	private RuletypesFactory ruleFactory;
	private RuletypeAssembler ruleAssembler;
	private MessageAssembler messageAssembler;

	public ViolationAssembler(){
		ViolationTypeFactory abstractViolationtypeFactory = new ViolationTypeFactory();
		this.violationtypeFactory = abstractViolationtypeFactory.getViolationTypeFactory();

		this.ruleFactory = new RuletypesFactory();
		this.ruleAssembler = new RuletypeAssembler();
		this.messageAssembler = new MessageAssembler();
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

		final String classPathFrom = violation.getClassPathFrom();
		final String classPathTo = violation.getClassPathTo();
		final String logicalModuleFromPath = violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath();
		final String logicalModuleToPath = violation.getLogicalModules().getLogicalModuleTo().getLogicalModulePath();
		final MessageDTO message = messageAssembler.createMessageDTO(violation.getMessage());
		
		return new ViolationDTO(classPathFrom, classPathTo, logicalModuleFromPath, logicalModuleToPath, violationtype, rule, message);
	}

	private RuleTypeDTO createRuleTypeDTO(Violation violation){
		ViolationType violationtype = violationtypeFactory.createViolationType(violation.getViolationtypeKey());
		RuleType rule = ruleFactory.generateRuleType(violation.getRuletypeKey());

		RuleTypeDTO ruleDTO = ruleAssembler.createRuleTypeDTO(rule, violationtype);
		return ruleDTO;
	}
}