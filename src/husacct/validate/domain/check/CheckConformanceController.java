package husacct.validate.domain.check;

import husacct.common.dto.RuleDTO;
import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.exception.RuleInstantionException;
import husacct.validate.domain.exception.RuleTypeNotFoundException;
import husacct.validate.domain.factory.ruletype.RuleTypesFactory;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class CheckConformanceController {
	private final ConfigurationServiceImpl configuration;

	private Logger logger = Logger.getLogger(CheckConformanceController.class);
	private List<Violation> violations;
	private RuleTypesFactory ruleFactory;
	private Map<String, RuleType> ruleCache;

	public CheckConformanceController(ConfigurationServiceImpl configuration, RuleTypesFactory ruleFactory){
		this.configuration = configuration;
		this.configuration.clearViolations();
		this.violations = new ArrayList<Violation>();
		this.ruleCache = new HashMap<String, RuleType>();
		this.ruleFactory = ruleFactory;
	}

	public void checkConformance(RuleDTO[] appliedRules){
		for(RuleDTO appliedRule : appliedRules){
			try{
				RuleType rule = getRuleType(appliedRule.ruleTypeKey);
				List<Violation> newViolations = rule.check(appliedRule);
				configuration.addViolations(newViolations);
			}catch(RuleTypeNotFoundException e){
				logger.warn(String.format("RuleTypeKey: %s not found, this rule will not be validated", appliedRule.ruleTypeKey));
			} catch (RuleInstantionException e) {
				logger.warn(String.format("RuleTypeKey: %s can not be instantiated, this rule will not be validated", appliedRule.ruleTypeKey));
			}
		}
	}

	private RuleType getRuleType(String ruleKey) throws RuleInstantionException, RuleTypeNotFoundException{
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