package husacct.validate.domain;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.MessageDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.assembler.AssemblerController;
import husacct.validate.domain.check.CheckConformanceController;
import husacct.validate.domain.messagefactory.Messagebuilder;
import husacct.validate.domain.rulefactory.RuleTypesFactory;
import husacct.validate.domain.rulefactory.ViolationTypeFactory;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.HashMap;
import java.util.List;

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
	
	public HashMap<String, List<ViolationType>> getAllViolationTypes(String programmingLanguage){
		initializeViolationtypeFactory();
		//TODO;
		//FIXME;
		return null;
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

	public String buildMessage(MessageDTO message) {	
		return new Messagebuilder().createMessage(message);
	}
}