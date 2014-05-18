package husacct.define.domain.services;

import husacct.ServiceProvider;
import husacct.common.dto.CategoryDTO;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleFactory;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.stateservice.StateService;
import java.util.ArrayList;

public class AppliedRuleDomainService {

	private AppliedRuleFactory ruleFactory = new AppliedRuleFactory();

	public AppliedRuleStrategy reloadAppliedRule(long ruleId, String ruleTypeKey, String description, String[] dependencies, String regex, 
			long ModuleStrategyFromId, long ModuleStrategyToId, boolean enabled, boolean isException, long parentRuleId) {
		ModuleStrategy moduleStrategyFrom = SoftwareArchitecture.getInstance().getModuleById(ModuleStrategyFromId);
		ModuleStrategy moduleStrategyTo;
		if (ModuleStrategyToId != -1) 
			moduleStrategyTo = SoftwareArchitecture.getInstance().getModuleById(ModuleStrategyToId);
		else 
			moduleStrategyTo = moduleStrategyFrom;

		AppliedRuleStrategy parentAppliedRule = null;
		if (parentRuleId != -1) 
			parentAppliedRule = SoftwareArchitecture.getInstance().getAppliedRuleById(parentRuleId);
		
		long newID = addAppliedRule(ruleTypeKey, description, dependencies, regex, moduleStrategyFrom, moduleStrategyTo, enabled, isException, parentAppliedRule);
		AppliedRuleStrategy newRule = getAppliedRuleById(newID);
		newRule.setId(ruleId);
		return newRule;
	}

	public long addAppliedRule(String ruleTypeKey, String description, String[] dependencies, String regex, ModuleStrategy ModuleStrategyFrom, 
			ModuleStrategy ModuleStrategyTo, boolean enabled, boolean isException, AppliedRuleStrategy parentRule) {

		AppliedRuleStrategy rule = ruleFactory.createRule(ruleTypeKey);
		rule.setAppliedRule(description, dependencies, regex, ModuleStrategyFrom, ModuleStrategyTo, enabled, isException, parentRule); 
		if (isDuplicate(rule)) {
			return -1;
		}
		StateService.instance().addAppliedRule(rule);
		SoftwareArchitecture.getInstance().addAppliedRule(rule);
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();

		return rule.getId();
	}

	public AppliedRuleStrategy getAppliedRuleById(long appliedRuleId) {
		return SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId);
	}

	public boolean getAppliedRuleIsEnabled(long appliedRuleId) {
		return SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId).isEnabled();
	}

	// Returns a flat list of main rules and exception rules
	public ArrayList<AppliedRuleStrategy> getAllAppliedRules() {
		ArrayList<AppliedRuleStrategy> ruleList = SoftwareArchitecture.getInstance().getAppliedRules();
		return ruleList;
	}

	// Returns all main rules, enabled or disabled (so no exception rules)
	public AppliedRuleStrategy[] getAllMainRules() {
		ArrayList<AppliedRuleStrategy> ruleList = SoftwareArchitecture.getInstance().getAppliedRules();
		ArrayList<AppliedRuleStrategy> mainRuleList = new ArrayList<AppliedRuleStrategy>();
		for (AppliedRuleStrategy ar : ruleList) {
			if (!ar.isException()) {
				mainRuleList.add(ar);
			}
		}
		AppliedRuleStrategy[] rules = new AppliedRuleStrategy[mainRuleList.size()];
		mainRuleList.toArray(rules);
		return rules;
	}

	// Returns all enabled main rules (so no disabled rules or exception rules)
	public AppliedRuleStrategy[] getAllEnabledMainRules() {
		ArrayList<AppliedRuleStrategy> ruleList = SoftwareArchitecture.getInstance().getAppliedRules();
		ArrayList<AppliedRuleStrategy> enabledMainRuleList = new ArrayList<AppliedRuleStrategy>();
		for (AppliedRuleStrategy ar : ruleList) {
			if ((ar.isEnabled()) && (!ar.isException())) {
				enabledMainRuleList.add(ar);
			}
		}
		AppliedRuleStrategy[] mainRuleArray = new AppliedRuleStrategy[enabledMainRuleList.size()];
		enabledMainRuleList.toArray(mainRuleArray);
		return mainRuleArray;
	}

	// Returns a flat list of exception rules (so no main rules)
	public ArrayList<AppliedRuleStrategy> getAllExceptionRules() {
		ArrayList<AppliedRuleStrategy> ruleList = SoftwareArchitecture.getInstance().getAppliedRules();
		ArrayList<AppliedRuleStrategy> exceptionRuleList = new ArrayList<AppliedRuleStrategy>();
		for (AppliedRuleStrategy ar : ruleList) {
			if (ar.isException()) {
				exceptionRuleList.add(ar);
			}
		}
		return exceptionRuleList;
	}
	

	public ArrayList<Long> getAppliedRulesIdsByModuleFromId(long ModuleStrategyId) {
		return SoftwareArchitecture.getInstance().getAppliedRulesIdsByModuleFromId(ModuleStrategyId);
	}

	public ArrayList<Long> getAppliedRulesIdsByModuleToId(long ModuleStrategyId) {
		return SoftwareArchitecture.getInstance()
				.getAppliedRulesIdsByModuleToId(ModuleStrategyId);
	}

	public long getModuleStrategyToIdOfAppliedRule(long appliedRuleId) {
		return SoftwareArchitecture.getInstance()
				.getAppliedRuleById(appliedRuleId).getModuleTo().getId();
	}

	public String getRuleTypeByAppliedRule(long appliedruleId) {
		return SoftwareArchitecture.getInstance()
				.getAppliedRuleById(appliedruleId).getRuleTypeKey();
	}

	/**
	 * Domain checks
	 */
	 public boolean isDuplicate(AppliedRuleStrategy rule) {
		 AppliedRuleStrategy[] appliedRules = getAllMainRules();
		 for (AppliedRuleStrategy appliedRule : appliedRules) {
			 if (rule.equals(appliedRule)) {
				 return true;
			 }
		 }
		 return false;
	 }

	 public void removeAppliedRule(long appliedrule_id) {
		 StateService.instance().removeAppliedRule(SoftwareArchitecture.getInstance().getAppliedRuleById(appliedrule_id));
		 SoftwareArchitecture.getInstance().removeAppliedRule(appliedrule_id);
		 ServiceProvider.getInstance().getDefineService()
		 .notifyServiceListeners();
	 }

	 public void removeAppliedRules() {
		 SoftwareArchitecture.getInstance().removeAppliedRules();
		 ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	 }

	 public void removeExceptionById(long parentRuleId, long exceptionRuleId) {
		if (exceptionRuleId != -1) {
			try {
				AppliedRuleStrategy parentRule = SoftwareArchitecture.getInstance().getAppliedRuleById(parentRuleId);
				parentRule.removeExceptionById(exceptionRuleId);
				SoftwareArchitecture.getInstance().removeAppliedRule(exceptionRuleId);
			} catch (Exception e) {
			}
		}
	 }

	public void setAppliedRuleIsEnabled(long appliedRuleId, boolean enabled) {
		 SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId)
		 .setEnabled(enabled);
		 ServiceProvider.getInstance().getDefineService()
		 .notifyServiceListeners();
	 }

	 public void updateAppliedRule(long appliedRuleId, Boolean isGenerated,
			 String ruleTypeKey, String description, String[] dependencies,
			 String regex, long ModuleStrategyFromId, long ModuleStrategyToId, boolean enabled) {

		 ModuleStrategy ModuleStrategyFrom = SoftwareArchitecture.getInstance().getModuleById(
				 ModuleStrategyFromId);
		 ModuleStrategy ModuleStrategyTo = SoftwareArchitecture.getInstance().getModuleById(
				 ModuleStrategyToId);
		 updateAppliedRule(appliedRuleId, ruleTypeKey, description,
				 dependencies, regex, ModuleStrategyFrom, ModuleStrategyTo, enabled);
	 }

	 public void updateAppliedRule(long appliedRuleId, String ruleTypeKey,
			 String description, String[] dependencies, String regex,
			 ModuleStrategy ModuleStrategyFrom, ModuleStrategy ModuleStrategyTo, boolean enabled) {
		 AppliedRuleStrategy rule = SoftwareArchitecture.getInstance()
				 .getAppliedRuleById(appliedRuleId);
		 StateService.instance().editAppliedRule(rule,new Object[]{ruleTypeKey,description,dependencies,regex,ModuleStrategyFrom,
				 ModuleStrategyTo,enabled});
		 rule.setRuleType(ruleTypeKey);
		 rule.setDescription(description);
		 rule.setDependencyTypes(dependencies);
		 rule.setRegex(regex);
		 rule.setModuleFrom(ModuleStrategyFrom);
		 rule.setModuleTo(ModuleStrategyTo);
		 rule.setEnabled(enabled);
		 ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	 }

	 public CategoryDTO[] getCategories() {
		 return ruleFactory.getCategories();
	 }

	 public boolean isMandatory(String ruleTypeKey, ModuleStrategy moduleFrom) {
		 DefaultRuleDomainService defaultRuleService = new DefaultRuleDomainService();
		 return defaultRuleService.isMandatoryRule(ruleTypeKey, moduleFrom);
	 }
	 
	 // Exception rules
	    public void addExceptionToAppliedRule(long parentRuleId, String ruleTypeKey, String description, 
	        	ModuleStrategy moduleFrom, ModuleStrategy moduleTo, String[] dependencies) {
	        	try{
	    			AppliedRuleStrategy parentRule = SoftwareArchitecture.getInstance().getAppliedRuleById(parentRuleId);
	    			String parentRuleType = parentRule.getRuleTypeKey(); 
	    			ModuleStrategy parentModuleTo = parentRule.getModuleTo();
	    	    	// Check constraint: If ruleType IsAllowdToUse, then moduleFrom and moduleTo should be the same or a subset of of these of the parentModuleTo.
	    	        if ((parentRuleType.equals("FacadeConvention")) || (parentRuleType.equals("IsNotAllowedToUse"))){
	    	        	boolean matching = doesExceptionModuleMatchWithParentModule(parentModuleTo, moduleTo);
	    	        	if (!matching){
	    	        		throw new RuntimeException("Illegal module selected: The exception module has to match with the main rule module, or one of its submodules");
	    	        	} 
	    	        } 
	        	    // Create exception rule
	        		//AppliedRuleFactory ruleFactory = new AppliedRuleFactory();
	        		//AppliedRuleStrategy exceptionRule = ruleFactory.createRule(ruleType);
	        		//exceptionRule.setAppliedRule(description, moduleFrom, moduleTo);
	        		//exceptionRule.setDependencyTypes(dependencies);
	    	        String regex = null;
	    	        boolean isEnabled = true;
	    	        boolean isException = true;
	    			long exceptionRuleId = addAppliedRule(ruleTypeKey, description, dependencies, regex, moduleFrom, moduleTo, isEnabled, isException, parentRule);
	    			AppliedRuleStrategy exceptionRule = getAppliedRuleById(exceptionRuleId);

	    	        // Add exception rule to Parent rule
	        		ArrayList<AppliedRuleStrategy> rules = new ArrayList<AppliedRuleStrategy>();
	        		rules.add(exceptionRule);
	        		StateService.instance().addExceptionRule(parentRule, rules);
	        		parentRule.addException(exceptionRule);
	    		} catch (RuntimeException rt) {
	    			String message = "IncorrectToModuleFacadeConvExc";
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
	        

}
