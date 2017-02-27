package husacct.validate.domain;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.validate.domain.assembler.CategoryDtoAssembler;
import husacct.validate.domain.assembler.MessageTextAssembler;
import husacct.validate.domain.assembler.RuleTypeDtoAssembler;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.factory.ruletype.RuleTypesFactory;
import husacct.validate.domain.factory.violationtype.AbstractViolationType;
import husacct.validate.domain.factory.violationtype.ViolationTypeFactory;
import husacct.validate.domain.validation.CheckConformanceController;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.moduletype.ModuleFactory;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class DomainServiceImpl {
	private Logger logger = Logger.getLogger(DomainServiceImpl.class);
	private RuleTypesFactory ruleTypeFactory;
	private ModuleFactory moduleFactory = null;
	private ViolationTypeFactory violationTypeFactory;
	private final CheckConformanceController checkConformanceController;
	private final ConfigurationServiceImpl configuration;

	public DomainServiceImpl(ConfigurationServiceImpl configuration) {
		this.configuration = configuration;
		this.ruleTypeFactory = configuration.getRuleTypesFactory();
		this.checkConformanceController = new CheckConformanceController(configuration, ruleTypeFactory);
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
		CategoryDtoAssembler assembler = new CategoryDtoAssembler();
		return assembler.createCategoryDTO(ruleTypes);
	}

	public RuleTypesFactory getRuleTypesFactory() {
		return ruleTypeFactory;
	}

	public String getMessage(Violation violation) {
		return new MessageTextAssembler().createMessageTextOfMainRule(violation);
	}

	public RuleTypeDTO[] getDefaultRuleTypeOfModule(String moduleType) {
		checkModuleTypeFactoryInstance();
		List<RuleType> defaultRuleTypes = moduleFactory.getDefaultRuleTypesOfModule(moduleType);
		RuleTypeDtoAssembler assembler = new RuleTypeDtoAssembler();
		return assembler.createRuleTypeDTO(defaultRuleTypes);
	}

	public RuleTypeDTO[] getAllowedRuleTypeOfModule(String moduleType) {
		checkModuleTypeFactoryInstance();
		List<RuleType> allowedRuleTypes = moduleFactory.getAllowedRuleTypesOfModule(moduleType);
		RuleTypeDtoAssembler assembler = new RuleTypeDtoAssembler();
		return assembler.createRuleTypeDTO(allowedRuleTypes);
	}
	
	public Set<String> getViolatedRules() {
		return configuration.getViolatedRules();
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