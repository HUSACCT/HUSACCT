package husacct.validate.domain;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.assembler.AssemblerController;
import husacct.validate.domain.check.CheckConformanceController;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.factory.message.Messagebuilder;
import husacct.validate.domain.factory.ruletype.RuleTypesFactory;
import husacct.validate.domain.factory.violationtype.AbstractViolationType;
import husacct.validate.domain.factory.violationtype.ViolationTypeFactory;
import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class DomainServiceImpl {
	private Logger logger = Logger.getLogger(DomainServiceImpl.class);

	private RuleTypesFactory ruletypefactory;
	private ViolationTypeFactory violationtypefactory;
	private final Messagebuilder messagebuilder;
	private final CheckConformanceController checkConformanceController;
	private final ConfigurationServiceImpl configuration;

	public DomainServiceImpl(ConfigurationServiceImpl configuration){	
		this.configuration = configuration;
		this.ruletypefactory = configuration.getRuleTypesFactory();
		this.checkConformanceController = new CheckConformanceController(configuration, ruletypefactory);
		this.messagebuilder = new Messagebuilder();
	}

	public HashMap<String, List<RuleType>> getAllRuleTypes(String programmingLanguage){
		return ruletypefactory.getRuleTypes(programmingLanguage);
	}

	/**
	 * Gets all the possible violationtypes of the given programmingLanguage
	 * Gives always the defaultSeverity back, despite what there is configured in the configuration,
	 * this is because a violationtype is configurable per ruletype
	 */
	public Map<String, List<ViolationType>> getAllViolationTypes(String programmingLanguage){
		initializeViolationtypeFactory();

		AbstractViolationType violationtypefactory = this.violationtypefactory.getViolationTypeFactory(programmingLanguage, configuration);
		if(violationtypefactory != null){
			return violationtypefactory.getAllViolationTypes();
		}
		else{
			logger.debug("Warning no language specified in define component");
			return Collections.emptyMap();
		}
	}

	private void initializeViolationtypeFactory(){
		if(violationtypefactory == null){
			this.violationtypefactory = new ViolationTypeFactory();
		}
	}

	public void checkConformance(RuleDTO[] appliedRules){
		checkConformanceController.checkConformance(appliedRules);
	}

	public CategoryDTO[] getCategories(){
		List<RuleType> ruleTypes = ruletypefactory.getRuleTypes();
		return new AssemblerController().createCategoryDTO(ruleTypes);
	}

	public RuleTypesFactory getRuleTypesFactory(){
		return ruletypefactory;
	}

	public String getMessage(Message message) {
		// TODO Auto-generated method stub
		return null;
	}
}