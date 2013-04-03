package husacct.validate.domain.assembler;

import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.dto.ViolationTypeDTO;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.exception.LanguageNotFoundException;
import husacct.validate.domain.exception.RuleInstantionException;
import husacct.validate.domain.exception.RuleTypeNotFoundException;
import husacct.validate.domain.exception.ViolationTypeNotFoundException;
import husacct.validate.domain.factory.message.Messagebuilder;
import husacct.validate.domain.factory.ruletype.RuleTypesFactory;
import husacct.validate.domain.factory.violationtype.AbstractViolationType;
import husacct.validate.domain.factory.violationtype.ViolationTypeFactory;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

public class ViolationAssembler {
	private Logger logger = Logger.getLogger(ViolationAssembler.class);

	private AbstractViolationType violationtypeFactory;
	private RuleTypesFactory ruleFactory;
	private RuletypeAssembler ruleAssembler;
	private Messagebuilder messageBuilder;
	private final ConfigurationServiceImpl configuration;

	public ViolationAssembler(RuleTypesFactory ruleFactory, ConfigurationServiceImpl configuration) {
		this.configuration = configuration;
		this.ruleFactory = ruleFactory;
		this.ruleAssembler = new RuletypeAssembler();
		this.messageBuilder = new Messagebuilder();

		ViolationTypeFactory abstractViolationtypeFactory = new ViolationTypeFactory();
		this.violationtypeFactory = abstractViolationtypeFactory.getViolationTypeFactory(configuration);
		if(violationtypeFactory == null) {
			logger.debug("Warning no language specified in define component");
		}
	}

	public List<ViolationDTO> createViolationDTO(List<Violation> violations) throws RuleTypeNotFoundException {
		List<ViolationDTO> violationDTOList = new ArrayList<ViolationDTO>();

		for (Violation violation : violations) {
			try{		
				ViolationDTO violationDTO = createViolationDTO(violation);
				violationDTOList.add(violationDTO);
			}
			catch(ViolationTypeNotFoundException e){
				logger.warn(String.format("ViolationtypeKey: %s not found in violation", violation.getViolationtypeKey()));
			}	
			catch(LanguageNotFoundException e){
				logger.warn(e.getMessage());
			}
			catch (RuleInstantionException e) {
				logger.warn(e.getMessage());
			}
		}	
		Collections.sort(violationDTOList, violationSeverityComparator);
		return violationDTOList;
	}

	private Comparator<ViolationDTO> violationSeverityComparator = new Comparator<ViolationDTO>() {
		@Override
		public int compare(ViolationDTO o1, ViolationDTO o2) {
			Integer severityValue1 = new Integer(o1.severityValue);
			Integer severityValue2 = new Integer(o2.severityValue);
			return severityValue2.compareTo(severityValue1);			
		}
	};

	private ViolationDTO createViolationDTO(Violation violation) throws RuleInstantionException, LanguageNotFoundException {
		try {
			RuleTypeDTO rule = createRuleTypeDTO(violation);
			ViolationTypeDTO violationtype = rule.getViolationTypes()[0];

			final String classPathFrom = violation.getClassPathFrom();
			final String classPathTo = violation.getClassPathTo();
			final String logicalModuleFromPath = violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath();
			final String logicalModuleToPath = violation.getLogicalModules().getLogicalModuleTo().getLogicalModulePath();
			final String message = messageBuilder.createMessage(violation.getMessage());
			final int linenumber = violation.getLinenumber();

			if(violation.getSeverity() != null) {
				final Severity severity = violation.getSeverity();
				final Color color = severity.getColor();
				final String severityName = severity.getSeverityName();
				final int severityValue = configuration.getSeverityValue(violation.getSeverity());
				final boolean isIndirect = violation.isIndirect();

				return new ViolationDTO(classPathFrom, classPathTo, logicalModuleFromPath, logicalModuleToPath, violationtype, rule, message, linenumber, color, severityName, severityValue, isIndirect);
			}
			else {				
				return new ViolationDTO(classPathFrom, classPathTo, logicalModuleFromPath, logicalModuleToPath, violationtype, rule, message, linenumber, Color.BLACK, "", 0, false);
			}
		}
		catch(ViolationTypeNotFoundException e) {
			throw new ViolationTypeNotFoundException();
		}
	}


	private RuleTypeDTO createRuleTypeDTO(Violation violation) throws RuleInstantionException, LanguageNotFoundException {
		try {
			if(violationtypeFactory == null) {
				throw new LanguageNotFoundException();
			}			
			ViolationType violationtype = violationtypeFactory.createViolationType(violation.getRuletypeKey(), violation.getViolationtypeKey());
			RuleType rule = ruleFactory.generateRuleType(violation.getRuletypeKey());

			RuleTypeDTO ruleDTO = ruleAssembler.createRuleTypeDTO(rule, violationtype);
			return ruleDTO;
		}
		catch(ViolationTypeNotFoundException e){
			throw new ViolationTypeNotFoundException();
		}		
	}
}