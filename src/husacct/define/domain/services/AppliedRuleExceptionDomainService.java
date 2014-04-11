package husacct.define.domain.services;

import husacct.ServiceProvider;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleFactory;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleFactory;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.stateservice.StateService;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class AppliedRuleExceptionDomainService {

	private Logger logger = Logger.getLogger(AppliedRuleExceptionDomainService.class);

    public void addExceptionToAppliedRule(long parentRuleId, String ruleType, String description, 
    		long moduleFromId, long moduleToId, String[] dependencies) {
		ModuleStrategy moduleFrom = SoftwareArchitecture.getInstance().getModuleById(moduleFromId);
		ModuleStrategy moduleTo;
		if (moduleToId != -1) {
		    moduleTo = SoftwareArchitecture.getInstance().getModuleById(moduleToId);
		} else {
		    moduleTo = new ModuleFactory().createDummy("blank");
		}
		addExceptionToAppliedRule(parentRuleId, ruleType, description, moduleFrom, moduleTo, dependencies);
    }

    public void addExceptionToAppliedRule(long parentRuleId, String ruleType, String description, 
    	ModuleStrategy moduleFrom, ModuleStrategy moduleTo, String[] dependencies) {
    	try{
    	// Check constraint: If ruleType of parent is FacadeConvention, then moduleTo should be parentModuleTo or a child.
		AppliedRuleStrategy parentRule = SoftwareArchitecture.getInstance().getAppliedRuleById(parentRuleId);
		ModuleStrategy parentModuleTo = parentRule.getModuleTo();
		String parentRuleType = parentRule.getRuleType(); 
        if ((parentRuleType.equals("FacadeConvention")) && (!(isExceptionModuleToChildOfParentModuleTo(parentModuleTo, moduleTo)))){
        	String test = "t";
			throw new RuntimeException("wrong input");
        } else {
	        // Create exception rule
			AppliedRuleFactory ruleFactory = new AppliedRuleFactory();
			AppliedRuleStrategy exceptionRule = ruleFactory.createRule(ruleType);
			exceptionRule.setAppliedRule(description, moduleFrom, moduleTo);
			exceptionRule.setDependencies(dependencies);
			// Add exception rule to Parent rule
			ArrayList<AppliedRuleStrategy> rules = new ArrayList<AppliedRuleStrategy>();
			rules.add(exceptionRule);
			StateService.instance().addExceptionRule(parentRule, rules);
			parentRule.addException(exceptionRule);
			ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
        }
		} catch (Exception rt) {
			String message = "IncorrectToModuleFacadeConvExc";
			logger.info(ServiceProvider.getInstance().getLocaleService().getTranslatedString("IncorrectToModuleFacadeConvExc"));
			throw new RuntimeException(message);
		}
    }
    
    private boolean isExceptionModuleToChildOfParentModuleTo(ModuleStrategy parentModuleTo, ModuleStrategy moduleTo){
        boolean exceptionModuleToIsChildOfParentModuleTo = false;
    	String pathToParent = SoftwareArchitecture.getInstance().getModulesLogicalPath(parentModuleTo.getId());
    	String pathToException = SoftwareArchitecture.getInstance().getModulesLogicalPath(moduleTo.getId());
        if (pathToParent.equals(pathToException)) {
    		exceptionModuleToIsChildOfParentModuleTo = true;
        } else if (pathToParent == "**") {	// Parent is Root
    		exceptionModuleToIsChildOfParentModuleTo = true;
        } else {
        	while (pathToException != "**"){
		        String currentParent = pathToException.substring(0, pathToException.lastIndexOf("."));
		        if (pathToParent == currentParent)
		        	exceptionModuleToIsChildOfParentModuleTo = true;
        	}
        }
	    return exceptionModuleToIsChildOfParentModuleTo;
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
	StateService.instance().removeAppliedRuleExeption(parentRuleId,parentRule.getExeptionByID(exceptionRuleId));
	parentRule.removeExceptionById(exceptionRuleId);
	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();
    }

}
