package husacct.validate.domain;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.assembler.AssemblerController;
import husacct.validate.domain.check.CheckConformanceController;
import husacct.validate.domain.factory.ruletype.RuleTypesFactory;
import husacct.validate.domain.factory.violationtype.java.AbstractViolationType;
import husacct.validate.domain.factory.violationtype.java.ViolationTypeFactory;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DomainServiceImpl {
	private RuleTypesFactory ruletypefactory;
	private ViolationTypeFactory violationtypefactory;
	private final CheckConformanceController checkConformanceController;

	public DomainServiceImpl(ConfigurationServiceImpl configuration){
		this.checkConformanceController = new CheckConformanceController(configuration);
	}

	public HashMap<String, List<RuleType>> getAllRuleTypes(String programmingLanguage){
		initializeRuletypesFactory();
		return ruletypefactory.getRuleTypes(programmingLanguage);
	}
	
	public Map<String, List<ViolationType>> getAllViolationTypes(String programmingLanguage){
		initializeViolationtypeFactory();
		
		AbstractViolationType violationtypefactory = new ViolationTypeFactory().getViolationTypeFactory(programmingLanguage);
		if(violationtypefactory != null){
			return violationtypefactory.getAllViolationTypes();
		}
		else{
			return Collections.emptyMap();
		}
	}

	public void checkConformance(RuleDTO[] appliedRules){
		checkConformanceController.checkConformance(appliedRules);
	}
	
	public CategoryDTO[] getCategories(){
		initializeRuletypesFactory();
		return new AssemblerController().createCategoryDTO(ruletypefactory.getRuleTypes());
	}
	
	private void initializeRuletypesFactory(){
		if(ruletypefactory == null){
			this.ruletypefactory = new RuleTypesFactory();
		}
	}
	
	private void initializeViolationtypeFactory(){
		if(violationtypefactory == null){
			this.violationtypefactory = new ViolationTypeFactory();
		}
	}
}