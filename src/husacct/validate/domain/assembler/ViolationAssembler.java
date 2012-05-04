package husacct.validate.domain.assembler;

import husacct.Main;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.dto.ViolationTypeDTO;
import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.exception.LanguageNotFoundException;
import husacct.validate.domain.exception.RuleInstantionException;
import husacct.validate.domain.exception.RuleTypeNotFoundException;
import husacct.validate.domain.exception.ViolationTypeNotFoundException;
import husacct.validate.domain.factory.message.Messagebuilder;
import husacct.validate.domain.factory.ruletype.RuleTypesFactory;
import husacct.validate.domain.factory.violationtype.java.AbstractViolationType;
import husacct.validate.domain.factory.violationtype.java.ViolationTypeFactory;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ViolationAssembler {
	private Logger logger = Logger.getLogger(Main.class);

	private AbstractViolationType violationtypeFactory;
	private RuleTypesFactory ruleFactory;
	private RuletypeAssembler ruleAssembler;
	private Messagebuilder messagebuilder;

	public ViolationAssembler(RuleTypesFactory ruleFactory, ConfigurationServiceImpl configuration){
		ViolationTypeFactory abstractViolationtypeFactory = new ViolationTypeFactory();
		this.violationtypeFactory = abstractViolationtypeFactory.getViolationTypeFactory(configuration);

		this.ruleFactory = ruleFactory;
		this.ruleAssembler = new RuletypeAssembler();
		this.messagebuilder = new Messagebuilder();
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
			final String message = messagebuilder.createMessage(violation.getMessage());
			final int linenumber = violation.getLinenumber();

			if(violation.getSeverity() != null){
				final Severity severity = violation.getSeverity();
				final Color color = severity.getColor();
				final  String userDefinedName = severity.getUserName();
				final String systemDefinedName = severity.getDefaultName();
				//FIXME: get severityValue from config attributte in this class
				return new ViolationDTO(classPathFrom, classPathTo, logicalModuleFromPath, logicalModuleToPath, violationtype, rule, message, linenumber, color, userDefinedName, systemDefinedName, 0);
			}
			else{				
				return new ViolationDTO(classPathFrom, classPathTo, logicalModuleFromPath, logicalModuleToPath, violationtype, rule, message, linenumber, Color.BLACK, "", "", 0);
			}
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