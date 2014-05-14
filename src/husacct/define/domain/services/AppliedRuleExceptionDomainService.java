package husacct.define.domain.services;

import husacct.ServiceProvider;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleFactory;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.stateservice.StateService;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class AppliedRuleExceptionDomainService {

	private Logger logger = Logger.getLogger(AppliedRuleExceptionDomainService.class);

    public void addExceptionToAppliedRule(long parentRuleId, String ruleType, String description, 
    	ModuleStrategy moduleFrom, ModuleStrategy moduleTo, String[] dependencies) {
    	try{
			AppliedRuleStrategy parentRule = SoftwareArchitecture.getInstance().getAppliedRuleById(parentRuleId);
			String parentRuleType = parentRule.getRuleType(); 
			ModuleStrategy parentModuleTo = parentRule.getModuleTo();
	    	// Check constraint: If ruleType IsAllowdToUse, then moduleFrom and moduleTo should be the same or a subset of of these of the parentModuleTo.
	        if ((parentRuleType.equals("FacadeConvention")) || (parentRuleType.equals("IsNotAllowedToUse"))){
	        	boolean matching = doesExceptionModuleMatchWithParentModule(parentModuleTo, moduleTo);
	        	if (!matching){
	        		throw new RuntimeException("Illegal module selected: The exception module has to match with the main rule module, or one of its submodules");
	        	} 
	        } 
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
		} catch (RuntimeException rt) {
			String message = "IncorrectToModuleFacadeConvExc";
			logger.info(ServiceProvider.getInstance().getLocaleService().getTranslatedString("IncorrectToModuleFacadeConvExc"));
			throw new RuntimeException(message);
		}
    }
    
    private boolean doesExceptionModuleMatchWithParentModule(ModuleStrategy parentModule, ModuleStrategy exceptionModule){
        boolean exceptionModuleToIsChildOfParentModuleTo = false;
    	String pathToParent = SoftwareArchitecture.getInstance().getModulesLogicalPath(parentModule.getId());
    	String pathToException = SoftwareArchitecture.getInstance().getModulesLogicalPath(exceptionModule.getId());
        if (pathToParent.equals(pathToException) || (pathToParent == "**")) {
    		exceptionModuleToIsChildOfParentModuleTo = true;
        } else {
        	String currentParent = pathToException;
        	while (currentParent.length() > pathToException.lastIndexOf(".")){
		        currentParent = pathToException.substring(0, pathToException.lastIndexOf("."));
		        if (pathToParent.equals(currentParent))
		        	exceptionModuleToIsChildOfParentModuleTo = true;
        	}
        }
	    return exceptionModuleToIsChildOfParentModuleTo;
    }
    
    public ArrayList<Long> getExceptionIdsByAppliedRule(long parentRuleId) {
		AppliedRuleStrategy parentRule = SoftwareArchitecture.getInstance().getAppliedRuleById(parentRuleId);
		ArrayList<AppliedRuleStrategy> exceptionRules = parentRule.getExceptions();
		ArrayList<Long> exceptionIds = new ArrayList<Long>();
		for (AppliedRuleStrategy exception : exceptionRules) {
		    exceptionIds.add(exception.getId());
		}
		return exceptionIds;
    }

    public ArrayList<AppliedRuleStrategy> getExceptionsByAppliedRule(long parentRuleId) {
    	return SoftwareArchitecture.getInstance().getAppliedRuleById(parentRuleId).getExceptions();
    }

    public void removeAppliedRuleException(long parentRuleId, long exceptionRuleId) {
		AppliedRuleStrategy parentRule = SoftwareArchitecture.getInstance().getAppliedRuleById(parentRuleId);
		parentRule.removeExceptionById(exceptionRuleId);
		StateService.instance().removeAppliedRuleExeption(parentRuleId,parentRule.getExeptionByID(exceptionRuleId));
    }

}
