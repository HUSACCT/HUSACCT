package husacct.validate.domain.assembler;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategoryDtoAssembler {

	private RuleTypeDtoAssembler ruleAssembler;

	public CategoryDtoAssembler() {
		this.ruleAssembler = new RuleTypeDtoAssembler();
	}

	public CategoryDTO[] createCategoryDTO(List<RuleType> rules) {
		List<CategoryDTO> categories = new ArrayList<CategoryDTO>();
		List<String> categoryKeys = getCategoryKeyList(rules);

		for (String key : categoryKeys) {
			List<RuleType> categoryRules = getRulesByCategorykey(rules, key);

			RuleTypeDTO[] ruletypes = ruleAssembler.createRuleTypeDTO(categoryRules);
			categories.add(new CategoryDTO(key, ruletypes));
		}
		return categories.toArray(new CategoryDTO[categories.size()]);
	}

	private List<String> getCategoryKeyList(List<RuleType> rules) {
		Set<String> categoryKeys = new HashSet<String>();

		for (RuleType rule : rules) {
			categoryKeys.add(rule.getCategoryKey());
		}
		return new ArrayList<String>(categoryKeys);
	}

	private List<RuleType> getRulesByCategorykey(List<RuleType> rules, String categoryKey) {
		List<RuleType> categoryRules = new ArrayList<RuleType>();

		for (RuleType rule : rules) {
			if (rule.getCategoryKey().equals(categoryKey)) {
				categoryRules.add(rule);
			}
		}
		return categoryRules;
	}
}