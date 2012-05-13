package husacct.validate.domain.check;

import husacct.ServiceProvider;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.RuleDTO;
import husacct.define.IDefineService;
import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.exception.ProgrammingLanguageNotFoundException;
import husacct.validate.domain.exception.RuleInstantionException;
import husacct.validate.domain.exception.RuleTypeNotFoundException;
import husacct.validate.domain.factory.ruletype.RuleTypesFactory;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class CheckConformanceController {
	private final ConfigurationServiceImpl configuration;

	private Logger logger = Logger.getLogger(CheckConformanceController.class);
	private RuleTypesFactory ruleFactory;
	private Map<String, RuleType> ruleCache;
	private IDefineService defineService = ServiceProvider.getInstance().getDefineService();

	public CheckConformanceController(ConfigurationServiceImpl configuration, RuleTypesFactory ruleFactory){
		this.configuration = configuration;
		this.configuration.clearViolations();
		this.ruleCache = new HashMap<String, RuleType>();
		this.ruleFactory = ruleFactory;
	}

	public void checkConformance(RuleDTO[] appliedRules){
		final ApplicationDTO applicationDetails = defineService.getApplicationDetails();
		if(applicationDetails.programmingLanguage != null && !applicationDetails.programmingLanguage.isEmpty()){
			configuration.clearViolations();
			for(RuleDTO appliedRule : appliedRules){
				try{
					RuleType rule = getRuleType(appliedRule.ruleTypeKey);
					List<Violation> newViolations = rule.check(configuration, appliedRule, appliedRule);
					configuration.addViolations(newViolations);
					if(appliedRule.exceptionRules != null){
						checkConformanceExceptionRules(appliedRule.exceptionRules, appliedRule);
					}
				}catch(RuleTypeNotFoundException e){
					logger.warn(String.format("RuleTypeKey: %s not found, this rule will not be validated", appliedRule.ruleTypeKey));
				} catch (RuleInstantionException e) {
					logger.warn(String.format("RuleTypeKey: %s can not be instantiated, this rule will not be validated", appliedRule.ruleTypeKey));
				}
			}
		}
		else{
			throw new ProgrammingLanguageNotFoundException();
		}
	}

	private void checkConformanceExceptionRules(RuleDTO[] exceptionRules, RuleDTO parent){
		for(RuleDTO appliedRule : exceptionRules){
			try{
				RuleType rule = getRuleType(appliedRule.ruleTypeKey);
				List<Violation> newViolations = rule.check(configuration, parent, appliedRule);
				configuration.addViolations(newViolations);
			}catch(RuleTypeNotFoundException e){
				logger.warn(String.format("RuleTypeKey: %s not found, this rule will not be validated", appliedRule.ruleTypeKey));
			} catch (RuleInstantionException e) {
				logger.warn(String.format("RuleTypeKey: %s can not be instantiated, this rule will not be validated", appliedRule.ruleTypeKey));
			}
		}
	}

	private RuleType getRuleType(String ruleKey) throws RuleInstantionException{
		RuleType rule = ruleCache.get(ruleKey);
		if(rule == null){
			rule = ruleFactory.generateRuleType(ruleKey);
		}
		if(rule != null){
			ruleCache.put(ruleKey, rule);
		}
		return rule;
	}
}