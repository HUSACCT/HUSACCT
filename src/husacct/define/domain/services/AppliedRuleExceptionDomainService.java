package husacct.define.domain.services;

import husacct.ServiceProvider;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.Module;

import java.util.ArrayList;

public class AppliedRuleExceptionDomainService {
	
	public void addExceptionToAppliedRule(long parentRuleId, String ruleType, String description, long moduleFromId, long moduleToId) {
		Module moduleFrom = SoftwareArchitecture.getInstance().getModuleById(moduleFromId);
		Module moduleTo;
		if (moduleToId != -1){
			moduleTo = SoftwareArchitecture.getInstance().getModuleById(moduleToId);
		} else {
			moduleTo = new Module();
		}
		addExceptionToAppliedRule(parentRuleId, ruleType, description, moduleFrom, moduleTo);
	}
	
	public void addExceptionToAppliedRule(long parentRuleId, String ruleType, String description, Module moduleFrom, Module moduleTo) {
		AppliedRule exceptionRule = new AppliedRule(ruleType,description, moduleTo, moduleFrom);
		
		AppliedRule parentRule = SoftwareArchitecture.getInstance().getAppliedRuleById(parentRuleId);
		parentRule.addException(exceptionRule);
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}
	
	public void removeAppliedRuleException(long parentRuleId, long exceptionRuleId) {
		AppliedRule parentRule = SoftwareArchitecture.getInstance().getAppliedRuleById(parentRuleId);
		parentRule.removeExceptionById(exceptionRuleId);
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}

	public void removeAllAppliedRuleExceptions(long appliedRuleId) {
		AppliedRule parentRule = SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId);
		parentRule.removeAllExceptions();
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}

	public ArrayList<Long> getExceptionIdsByAppliedRule(long parentRuleId) {
		AppliedRule parentRule = SoftwareArchitecture.getInstance().getAppliedRuleById(parentRuleId);
		ArrayList<AppliedRule> exceptionRules = parentRule.getExceptions();
		ArrayList<Long> exceptionIds = new ArrayList<Long>();
		for (AppliedRule exception : exceptionRules){
			exceptionIds.add(exception.getId());
		}
		return exceptionIds;
	}
	
	public ArrayList<AppliedRule> getExceptionsByAppliedRule(long parentRuleId) {
		AppliedRule parentRule = SoftwareArchitecture.getInstance().getAppliedRuleById(parentRuleId);
		ArrayList<AppliedRule> exceptionRules = parentRule.getExceptions();
		return exceptionRules;
	}

}
