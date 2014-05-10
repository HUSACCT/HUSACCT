package husacct.validate.domain;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.validate.domain.assembler.AssemblerController;
import husacct.validate.domain.check.CheckConformanceController;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.factory.message.Messagebuilder;
import husacct.validate.domain.factory.ruletype.RuleTypesFactory;
import husacct.validate.domain.factory.violationtype.AbstractViolationType;
import husacct.validate.domain.factory.violationtype.ViolationTypeFactory;
import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.moduletype.ModuleFactory;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class DomainServiceImpl {
	private Logger logger = Logger.getLogger(DomainServiceImpl.class);
	private RuleTypesFactory ruleTypeFactory;
	private ModuleFactory moduleFactory = null;
	private ViolationTypeFactory violationTypeFactory;
	private final Messagebuilder messagebuilder;
	private final CheckConformanceController checkConformanceController;
	private final ConfigurationServiceImpl configuration;

	public DomainServiceImpl(ConfigurationServiceImpl configuration) {
		this.configuration = configuration;
		this.ruleTypeFactory = configuration.getRuleTypesFactory();
		this.checkConformanceController = new CheckConformanceController(configuration, ruleTypeFactory);
		this.messagebuilder = new Messagebuilder();
	}

	public HashMap<String, List<RuleType>> getAllRuleTypes(String programmingLanguage) {
		return ruleTypeFactory.getRuleTypes(programmingLanguage);
	}

	/**
	 * Gets all the possible violationtypes of the given programmingLanguage
	 * Gives always the defaultSeverity back, despite what there is configured
	 * in the configuration, this is because a violationtype is configurable per
	 * ruletype
	 */
	public Map<String, List<ViolationType>> getAllViolationTypes(String programmingLanguage) {
		initializeViolationtypeFactory();

		AbstractViolationType violationtypefactory = this.violationTypeFactory.getViolationTypeFactory(programmingLanguage, configuration);
		if (violationtypefactory != null) {
			return violationtypefactory.getAllViolationTypes();
		} else {
			logger.warn("Warning no language specified in define component");
			return Collections.emptyMap();
		}
	}

	private void initializeViolationtypeFactory() {
		if (violationTypeFactory == null) {
			this.violationTypeFactory = new ViolationTypeFactory();
		}
	}

	public void checkConformance(RuleDTO[] appliedRules) {
		checkConformanceController.checkConformance(appliedRules);
	}

	public CategoryDTO[] getCategories() {
		List<RuleType> ruleTypes = ruleTypeFactory.getRuleTypes();
		return new AssemblerController().createCategoryDTO(ruleTypes);
	}

	public RuleTypesFactory getRuleTypesFactory() {
		return ruleTypeFactory;
	}

	public String getMessage(Message message, Violation violation) {
		return messagebuilder.createMessage(message, violation);
	}

	public RuleTypeDTO[] getDefaultRuleTypeOfModule(String moduleType) {
		checkModuleTypeFactoryInstance();
		List<RuleType> moduleRuleTypes = moduleFactory.getDefaultRuleTypesOfModule(moduleType);
		return new AssemblerController().createRuleTypeDTO(moduleRuleTypes);
	}

	public RuleTypeDTO[] getAllowedRuleTypeOfModule(String moduleType) {
		checkModuleTypeFactoryInstance();
		List<RuleType> moduleRuleTypes = moduleFactory.getAllowedRuleTypesOfModule(moduleType);
		return new AssemblerController().createRuleTypeDTO(moduleRuleTypes);
	}
	
	public void setDefaultRuleTypeOfModule(String moduleType, String ruleTypeKey, boolean value) {
		checkModuleTypeFactoryInstance();
		moduleFactory.setDefaultRuleTypeOfModule(moduleType, ruleTypeKey, value);
	}

	public void setAllowedRuleTypeOfModule(String moduleType, String ruleTypeKey, boolean value) {
		checkModuleTypeFactoryInstance();
		moduleFactory.setAllowedRuleTypeOfModule(moduleType, ruleTypeKey, value);
	}
	
	public void checkModuleTypeFactoryInstance() {
		if (this.moduleFactory == null) {
			this.moduleFactory = new ModuleFactory(ruleTypeFactory.getRuleTypes());
			logger.info("new ModuleFactory within checkModuleTypeFactoryInstance()");
		}	
	}
}