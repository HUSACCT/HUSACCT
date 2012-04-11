package husacct.validate.domain.assembler;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.validate.domain.ruletype.Rule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategoryAssembler {
	private RuletypeAssembler ruleAssembler;
	
	public CategoryAssembler(){
		this.ruleAssembler = new RuletypeAssembler();
	}
	
	public CategoryDTO[] createCategoryDTO(List<Rule> rules){
		List<CategoryDTO> categories = new ArrayList<CategoryDTO>();
		List<String> categoryKeys = getCategoryKeyList(rules);

		for(String key : categoryKeys){
			List<Rule> categoryRules = getRulesByCategorykey(rules, key);
			
			RuleTypeDTO[] ruletypes = ruleAssembler.createRuleTypeDTO(categoryRules);
			categories.add(new CategoryDTO(key, ruletypes));			
		}
		return categories.toArray(new CategoryDTO[categories.size()]);
	}
	
	private List<String> getCategoryKeyList(List<Rule> rules){
		Set<String> categoryKeys = new HashSet<String>();

		for(Rule rule : rules){
			categoryKeys.add(rule.getCategoryKey());
		}		
		return new ArrayList<String>(categoryKeys);
	}

	private List<Rule> getRulesByCategorykey(List<Rule> rules, String categoryKey){
		List<Rule> categoryRules = new ArrayList<Rule>();

		for(Rule rule : rules){
			if(rule.getCategoryKey().equals(categoryKey)){
				categoryRules.add(rule);
			}
		}
		return categoryRules;
	}
}