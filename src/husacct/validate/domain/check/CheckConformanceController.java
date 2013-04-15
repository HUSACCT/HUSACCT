package husacct.validate.domain.check;

import husacct.ServiceProvider;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.RuleDTO;
import husacct.control.task.States;
import husacct.define.IDefineService;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.exception.ProgrammingLanguageNotFoundException;
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
	private RuleTypesFactory ruleFactory;
	private Map<String, RuleType> ruleCache;
	private IDefineService defineService = ServiceProvider.getInstance().getDefineService();

	//added by team 1 general gui & control
	//declaration that keeps the progress
	private int appliedRulesHandled = 0;
	//end adding by team1
	
	public CheckConformanceController(ConfigurationServiceImpl configuration, RuleTypesFactory ruleFactory) {
		this.configuration = configuration;
		this.configuration.clearViolations();
		this.ruleCache = new HashMap<String, RuleType>();
		this.ruleFactory = ruleFactory;
	}

	public void checkConformance(RuleDTO[] appliedRules) {
		final ApplicationDTO applicationDetails = defineService.getApplicationDetails();
		if (applicationDetails.projects.get(0).programmingLanguage != null && !applicationDetails.projects.get(0).programmingLanguage.isEmpty()) {
			configuration.clearViolations();
			ruleCache.clear();

			List<Violation> violationList = new ArrayList<Violation>();
			appliedRulesHandled = 0;
			for (RuleDTO appliedRule : appliedRules) {
				
				//Added by Team 1 General GUI & Control
				//needed for interrupting this thread
				if(!ServiceProvider.getInstance().getControlService().getState().contains(States.VALIDATING)) {
					break;
				}
				//calculating percentage for progress bar
				ServiceProvider.getInstance().getControlService().updateProgress((++appliedRulesHandled * 100)/appliedRules.length);
				//end adding by Team 1
				
				try {
					RuleType rule = getRuleType(appliedRule.ruleTypeKey);
					List<Violation> newViolations = rule.check(configuration, appliedRule, appliedRule);
					violationList.addAll(newViolations);

					if (appliedRule.exceptionRules != null) {
						checkConformanceExceptionRules(appliedRule.exceptionRules, appliedRule);
					}
				} catch (RuleTypeNotFoundException e) {
					logger.warn(String.format("RuleTypeKey: %s not found, this rule will not be validated", appliedRule.ruleTypeKey));
				} catch (RuleInstantionException e) {
					logger.warn(String.format("RuleTypeKey: %s can not be instantiated, this rule will not be validated", appliedRule.ruleTypeKey));
				}
			}
			configuration.addViolations(violationList);
		} else {
			throw new ProgrammingLanguageNotFoundException();
		}
	}

	private void checkConformanceExceptionRules(RuleDTO[] exceptionRules, RuleDTO parent) {
		for (RuleDTO appliedRule : exceptionRules) {
			try {
				RuleType rule = getRuleType(appliedRule.ruleTypeKey);
				List<Violation> newViolations = rule.check(configuration, parent, appliedRule);
				configuration.addViolations(newViolations);
			} catch (RuleTypeNotFoundException e) {
				logger.warn(String.format("RuleTypeKey: %s not found, this rule will not be validated", appliedRule.ruleTypeKey));
			} catch (RuleInstantionException e) {
				logger.warn(String.format("RuleTypeKey: %s can not be instantiated, this rule will not be validated", appliedRule.ruleTypeKey));
			}
		}
	}

	private RuleType getRuleType(String ruleKey) throws RuleInstantionException {
		RuleType rule = ruleCache.get(ruleKey);
		if (rule == null) {
			rule = ruleFactory.generateRuleType(ruleKey);
		}
		if (rule != null) {
			ruleCache.put(ruleKey, rule);
		}
		return rule;
	}
}