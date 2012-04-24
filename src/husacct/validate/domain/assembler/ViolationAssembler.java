package husacct.validate.domain.assembler;

import husacct.Main;
import husacct.common.dto.MessageDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.dto.ViolationTypeDTO;
import husacct.validate.domain.exception.LanguageNotFoundException;
import husacct.validate.domain.exception.RuleInstantionException;
import husacct.validate.domain.exception.RuleTypeNotFoundException;
import husacct.validate.domain.exception.ViolationTypeNotFoundException;
import husacct.validate.domain.rulefactory.RuleTypesFactory;
import husacct.validate.domain.rulefactory.ViolationTypeFactory;
import husacct.validate.domain.rulefactory.violationtypeutil.AbstractViolationType;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ViolationAssembler {
	private Logger logger = Logger.getLogger(Main.class);

	private AbstractViolationType violationtypeFactory;
	private RuleTypesFactory ruleFactory;
	private RuletypeAssembler ruleAssembler;
	private MessageAssembler messageAssembler;

	public ViolationAssembler(){
		ViolationTypeFactory abstractViolationtypeFactory = new ViolationTypeFactory();
		this.violationtypeFactory = abstractViolationtypeFactory.getViolationTypeFactory();

		this.ruleFactory = new RuleTypesFactory();
		this.ruleAssembler = new RuletypeAssembler();
		this.messageAssembler = new MessageAssembler();
	}

	public List<ViolationDTO> createViolationDTO(List<Violation> violations) {
		List<ViolationDTO> violationDTOList = new ArrayList<ViolationDTO>();

		for (Violation violation : violations) {
			try{		
				ViolationDTO violationDTO = createViolationDTO(violation);
				violationDTOList.add(violationDTO);
			}catch(ViolationTypeNotFoundException e){
				logger.warn(String.format("ViolationtypeKey: %s not found in violation", violation.getViolationtypeKey()));
			}	
			catch(LanguageNotFoundException e){
				logger.warn(e.getMessage());
			}
			catch(RuleTypeNotFoundException e){
				logger.warn(e.getMessage());
			} catch (RuleInstantionException e) {
				logger.warn(e.getMessage());
			}
		}
		return violationDTOList;
	}

	private ViolationDTO createViolationDTO(Violation violation) throws RuleInstantionException, LanguageNotFoundException, RuleTypeNotFoundException{
		try{
			RuleTypeDTO rule = createRuleTypeDTO(violation);
			ViolationTypeDTO violationtype = rule.getViolationTypes()[0];

			final String classPathFrom = violation.getClassPathFrom();
			final String classPathTo = violation.getClassPathTo();
			final String logicalModuleFromPath = violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath();
			final String logicalModuleToPath = violation.getLogicalModules().getLogicalModuleTo().getLogicalModulePath();
			final MessageDTO message = messageAssembler.createMessageDTO(violation.getMessage());

			return new ViolationDTO(classPathFrom, classPathTo, logicalModuleFromPath, logicalModuleToPath, violationtype, rule, message);
		}catch(ViolationTypeNotFoundException e){
			throw new ViolationTypeNotFoundException();
		}
	}


	private RuleTypeDTO createRuleTypeDTO(Violation violation) throws RuleInstantionException, LanguageNotFoundException, RuleTypeNotFoundException{
		try{
			if(violationtypeFactory == null){
				throw new LanguageNotFoundException();
			}			
			ViolationType violationtype = violationtypeFactory.createViolationType(violation.getViolationtypeKey());
			RuleType rule = ruleFactory.generateRuleType(violation.getRuletypeKey());

			RuleTypeDTO ruleDTO = ruleAssembler.createRuleTypeDTO(rule, violationtype);
			return ruleDTO;
		}catch(ViolationTypeNotFoundException e){
			throw new ViolationTypeNotFoundException();
		}		
	}
}