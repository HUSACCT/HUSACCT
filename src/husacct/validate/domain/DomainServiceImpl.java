package husacct.validate.domain;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.MessageDTO;
import husacct.common.dto.RuleDTO;
import husacct.validate.domain.assembler.AssemblerController;
import husacct.validate.domain.check.CheckConformanceController;
import husacct.validate.domain.messagefactory.Messagebuilder;
import husacct.validate.domain.rulefactory.RuletypesFactory;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.List;

public class DomainServiceImpl {
	private RuletypesFactory ruletypefactory;
	private final CheckConformanceController checkConformanceController;

	public DomainServiceImpl(ConfigurationServiceImpl configuration){
		this.checkConformanceController = new CheckConformanceController(configuration);
	}

	public List<RuleType> getAllRuleTypes(){
		initializeRuletypesFactory();
		return ruletypefactory.getRuleTypes();
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
			this.ruletypefactory = new RuletypesFactory();
		}
	}

	public String buildMessage(MessageDTO message) {	
		return new Messagebuilder().createMessage(message);
	}
}