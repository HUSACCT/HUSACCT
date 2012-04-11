package husacct.validate.domain.check;

import husacct.common.dto.RuleDTO;
import husacct.validate.domain.factory.RuletypesFactory;
import husacct.validate.domain.ruletype.Rule;
import husacct.validate.domain.violation.Violation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckConformanceController {
	private List<Violation> violations;
	private RuletypesFactory ruleFactory;
	private Map<String, Rule> ruleCache;

	public CheckConformanceController(){
		this.violations = new ArrayList<Violation>();
		this.ruleCache = new HashMap<String, Rule>();
		this.ruleFactory = new RuletypesFactory();
	}

	public void CheckConformance(RuleDTO[] appliedRules){
		for(RuleDTO appliedRule : appliedRules){
			Rule rule = getRuleType(appliedRule.ruleTypeKey);
			if(rule != null){
				List<Violation> newViolations = rule.check(appliedRule);
				violations.addAll(newViolations);
			}
			else{
				//rule(Key) does not exists, thus ignore appliedRule
			}
		}
	}

	private Rule getRuleType(String ruleKey){
		Rule rule = ruleCache.get(ruleKey);

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