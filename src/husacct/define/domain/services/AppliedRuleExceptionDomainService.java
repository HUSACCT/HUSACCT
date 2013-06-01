package husacct.define.domain.services;

import husacct.ServiceProvider;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleFactory;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleFactory;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.stateservice.StateService;

import java.util.ArrayList;

import com.sun.xml.internal.ws.api.server.Module;

public class AppliedRuleExceptionDomainService {

    public void addExceptionToAppliedRule(long parentRuleId, String ruleType,
	    String description, long moduleFromId, long moduleToId,
	    String[] dependencies) {
	ModuleStrategy moduleFrom = SoftwareArchitecture.getInstance().getModuleById(
		moduleFromId);
	ModuleStrategy moduleTo;
	if (moduleToId != -1) {
	    moduleTo = SoftwareArchitecture.getInstance().getModuleById(
		    moduleToId);
	} else {
	    moduleTo = new ModuleFactory().createDummy("blank");
	}
	addExceptionToAppliedRule(parentRuleId, ruleType, description,
		moduleFrom, moduleTo, dependencies);
    }

    public void addExceptionToAppliedRule(long parentRuleId, String ruleType,
	    String description, ModuleStrategy moduleFrom, ModuleStrategy moduleTo,
	    String[] dependencies) {
	AppliedRuleFactory ruleFactory = new AppliedRuleFactory();
	AppliedRuleStrategy exceptionRule = ruleFactory.createRule(ruleType);
	exceptionRule.setAppliedRule(description, moduleFrom, moduleTo);
	exceptionRule.setDependencies(dependencies);

	AppliedRuleStrategy parentRule = SoftwareArchitecture.getInstance()
		.getAppliedRuleById(parentRuleId);
	parentRule.addException(exceptionRule);
	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();
    }
    
    

    public ArrayList<Long> getExceptionIdsByAppliedRule(long parentRuleId) {
	AppliedRuleStrategy parentRule = SoftwareArchitecture.getInstance()
		.getAppliedRuleById(parentRuleId);
	ArrayList<AppliedRuleStrategy> exceptionRules = parentRule.getExceptions();
	ArrayList<Long> exceptionIds = new ArrayList<Long>();
	for (AppliedRuleStrategy exception : exceptionRules) {
	    exceptionIds.add(exception.getId());
	}
	return exceptionIds;
    }

    public ArrayList<AppliedRuleStrategy> getExceptionsByAppliedRule(long parentRuleId) {
	return SoftwareArchitecture.getInstance()
		.getAppliedRuleById(parentRuleId).getExceptions();
    }

    public void removeAllAppliedRuleExceptions(long appliedRuleId) {
	AppliedRuleStrategy parentRule = SoftwareArchitecture.getInstance()
		.getAppliedRuleById(appliedRuleId);
	parentRule.removeAllExceptions();
	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();
    }

    public void removeAppliedRuleException(long parentRuleId,
	    long exceptionRuleId) {
	AppliedRuleStrategy parentRule = SoftwareArchitecture.getInstance()
		.getAppliedRuleById(parentRuleId);
	parentRule.removeExceptionById(exceptionRuleId);
	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();
    }

}
