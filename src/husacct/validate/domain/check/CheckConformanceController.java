package husacct.validate.domain.check;

import husacct.common.dto.RuleDTO;
import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.rulefactory.RuletypesFactory;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckConformanceController {
	private final ConfigurationServiceImpl configuration;
	private List<Violation> violations;
	private RuletypesFactory ruleFactory;
	private Map<String, RuleType> ruleCache;

	public CheckConformanceController(ConfigurationServiceImpl configuration){
		this.configuration = configuration;
		this.configuration.clearViolations();
		this.violations = new ArrayList<Violation>();
		this.ruleCache = new HashMap<String, RuleType>();
		this.ruleFactory = new RuletypesFactory();
	}

	public void checkConformance(RuleDTO[] appliedRules){
		for(RuleDTO appliedRule : appliedRules){
			RuleType rule = getRuleType(appliedRule.ruleTypeKey);
			if(rule != null){
				List<Violation> newViolations = rule.check(appliedRule);
				configuration.addViolations(newViolations);
			}
			else{
				//rule(Key) does not exists, thus ignore appliedRule
			}
		}
	}

	private RuleType getRuleType(String ruleKey){
		RuleType rule = ruleCache.get(ruleKey);

		if(rule == null){
			rule = ruleFactory.generateRuleType(ruleKey);
		}
		if(rule != null){
			ruleCache.put(ruleKey, rule);
		}
		return rule;
	}

	public List<Violation> getViolations(){
		return violations;
	}
}