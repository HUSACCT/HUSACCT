package husacct.validate.domain.validation;

import husacct.ServiceProvider;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.enums.States;
import husacct.define.IDefineService;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.exception.ProgrammingLanguageNotFoundException;
import husacct.validate.domain.exception.RuleInstantionException;
import husacct.validate.domain.exception.RuleTypeNotFoundException;
import husacct.validate.domain.factory.ruletype.RuleTypesFactory;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.ArrayList;
import java.util.Date;
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
	//Declaration of variable that holds the progress
	private int appliedRulesHandled = 0;

	public CheckConformanceController(ConfigurationServiceImpl configuration, RuleTypesFactory ruleFactory) {
		this.configuration = configuration;
		this.configuration.clearViolations();
		this.ruleCache = new HashMap<String, RuleType>();
		this.ruleFactory = ruleFactory;
	}

	public void checkConformance(RuleDTO[] appliedRules) {
		try {
			this.logger.info(new Date().toString() + " Start ConformanceCheck");
			ServiceProvider.getInstance().getControlService().updateProgress(0);
			final ApplicationDTO applicationDetails = defineService.getApplicationDetails();
			for (ProjectDTO project : applicationDetails.projects) {
				if (project.programmingLanguage != null && !project.programmingLanguage.isEmpty()) {
					configuration.clearViolations();
					ruleCache.clear();
					List<Violation> violationList = new ArrayList<Violation>();
					appliedRulesHandled = 0;
					for (RuleDTO appliedRule : appliedRules) {
						// Abort, when state != VALIDATING
						if (!ServiceProvider.getInstance().getControlService().getStates().contains(States.VALIDATING)) {
							break;
						}
						// Update progress
						ServiceProvider.getInstance().getControlService().updateProgress((++appliedRulesHandled * 100) / appliedRules.length);
						
						try {
							if (!appliedRule.isException){
								List<Violation> newViolations = null;
								RuleType rule = getRuleType(appliedRule.ruleTypeKey);
								newViolations = rule.check(configuration, appliedRule);
								violationList.addAll(newViolations);
							}
	
						} catch (RuleTypeNotFoundException e) {
							logger.warn(String.format("RuleTypeKey: %s not found, this rule will not be validated", appliedRule.ruleTypeKey));
						} catch (RuleInstantionException e) {
							logger.warn(String.format("RuleTypeKey: %s can not be instantiated, rules of this type will not be validated", appliedRule.ruleTypeKey));
						}
					}
					configuration.addViolations(violationList);
					configuration.filterAndSortAllViolations();
					this.logger.info(new Date().toString() + " Finished ConformanceCheck");
	
				} else {
					logger.error(String.format(" Programming language not found for project: " + project.name));
					throw new ProgrammingLanguageNotFoundException();
				}
			}
        } catch (Exception e) {
	        this.logger.error(new Date().toString() + " Exception: "  + e );
	        //e.printStackTrace();
        }

	}

	private RuleType getRuleType(String ruleTypeKey) throws RuleInstantionException {
		RuleType ruleType = null;
		if(ruleCache.containsKey(ruleTypeKey)){
			ruleType = ruleCache.get(ruleTypeKey);
		}
		else {
			ruleType = ruleFactory.generateRuleType(ruleTypeKey);
			if (ruleType != null)
				ruleCache.put(ruleTypeKey, ruleType);
			else
				throw new RuleInstantionException();
		}
		return ruleType;
	}
}