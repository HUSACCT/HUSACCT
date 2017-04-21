package husacct.define.domain.services;

import husacct.ServiceProvider;
import husacct.common.dto.CategoryDTO;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleFactory;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

public class AppliedRuleDomainService {

	private SoftwareArchitecture softwareArchitecture = SoftwareArchitecture.getInstance(); 
	private AppliedRuleFactory ruleFactory = new AppliedRuleFactory();
	private Logger logger = Logger.getLogger(AppliedRuleDomainService.class);

	public AppliedRuleStrategy reloadAppliedRule(long ruleId, String ruleTypeKey, String description, String[] dependencies, String regex, 
			long ModuleStrategyFromId, long ModuleStrategyToId, boolean enabled, boolean isException, long parentRuleId) {
		AppliedRuleStrategy newRule = null;
		ModuleStrategy moduleStrategyFrom = SoftwareArchitecture.getInstance().getModuleById(ModuleStrategyFromId);
		ModuleStrategy moduleStrategyTo;
		if (ModuleStrategyToId != -1) 
			moduleStrategyTo = SoftwareArchitecture.getInstance().getModuleById(ModuleStrategyToId);
		else 
			moduleStrategyTo = moduleStrategyFrom;
		if ((moduleStrategyFrom != null) && (moduleStrategyTo != null)) {
			AppliedRuleStrategy parentAppliedRule = null;
			if (parentRuleId != -1) 
				parentAppliedRule = SoftwareArchitecture.getInstance().getAppliedRuleById(parentRuleId);
			
			newRule = ruleFactory.createRule(ruleTypeKey);
			newRule.setAppliedRule(description, dependencies, regex, moduleStrategyFrom, moduleStrategyTo, enabled, isException, parentAppliedRule); 
			newRule.setId(ruleId);
			if (isDuplicate(newRule)) {
				logger.warn(String.format(" Rule already added: " + ruleTypeKey + ", " + moduleStrategyFrom.getName() + ", " + moduleStrategyTo.getName()));
			} else {
				softwareArchitecture.addAppliedRule(newRule);
			}
		}
		return newRule;
	}

	public long addAppliedRule(String ruleTypeKey, String description, String[] dependencies, String regex, ModuleStrategy moduleStrategyFrom, 
			ModuleStrategy moduleStrategyTo, boolean isEnabled, boolean isException, AppliedRuleStrategy parentRule) {
		// Check references
		boolean ruleTypeKeyExists = AppliedRuleFactory.isRuleTypeExisting(ruleTypeKey);
		boolean moduleFromExists = false;
		boolean moduleToExists = false;
		if (moduleStrategyFrom.getId() > 0) {
			moduleFromExists = true;
		}
		if (moduleStrategyTo.getId() > 0) {
			moduleToExists = true;
		}
		// Add rule if references are ok
		AppliedRuleStrategy rule = ruleFactory.createRule(ruleTypeKey);
		rule.setAppliedRule(description, dependencies, regex, moduleStrategyFrom, moduleStrategyTo, isEnabled, isException, parentRule); 
		if (ruleTypeKeyExists && moduleFromExists && moduleToExists && !isDuplicate(rule)) {
			softwareArchitecture.addAppliedRule(rule);
			ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
			return rule.getId();
		} else {
			if (isDuplicate(rule)) {
				logger.warn(String.format(" Rule not added (duplicate): " + ruleTypeKey + ", " + moduleStrategyFrom.getName() + ", " + moduleStrategyTo.getName()));
			} else {
				logger.warn(String.format(" Rule not added: " + ruleTypeKey + ", " + moduleStrategyFrom.getName() + ", " + moduleStrategyTo.getName()));
			}
			return -1;
		}
	}

	public AppliedRuleStrategy getAppliedRuleById(long appliedRuleId) {
		return SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId);
	}

	/** Gets the main rule that matches the arguments. So, no exception rule.
	 * 	Returns null if no matching rule is found.
	 *  Returns the last found rule, if several rules are found. In that case a warning message is logged.
	 */
	public AppliedRuleStrategy getAppliedMainRuleBy_From_To_RuleTypeKey(String moduleFromLogicalPath, String moduleTologicalPath, String ruleTypeKey) {
		AppliedRuleStrategy foundRule = null;
		long fromId = -1;
		long toId = -1;
		ModuleDomainService moduleService = new ModuleDomainService();
		if (moduleFromLogicalPath != null && !moduleFromLogicalPath.equals("")) {
			ModuleStrategy moduleFrom = moduleService.getModuleByLogicalPath(moduleFromLogicalPath);
			if ((moduleFrom != null) && (moduleFrom.getId() >= 0)) {
				fromId = moduleFrom.getId();
			}
		}
		if (moduleTologicalPath != null && !moduleTologicalPath.equals("")) {
			ModuleStrategy moduleTo = moduleService.getModuleByLogicalPath(moduleTologicalPath);
			if ((moduleTo != null) && (moduleTo.getId() >= 0)) {
				toId = moduleTo.getId();
			}
		}
		if (fromId != -1 && toId != -1 ) {
			ArrayList<AppliedRuleStrategy> allDefinedRules = getAllAppliedRules(); // Includes exceptions.
			int numberOfFoundRules = 0;
			for (AppliedRuleStrategy definedRule : allDefinedRules) {
				if (!definedRule.isException() && (definedRule.getModuleFrom().getId() == fromId) && (definedRule.getModuleTo().getId() == toId) && definedRule.getRuleTypeKey().equals(ruleTypeKey)) {
					foundRule = definedRule;
					numberOfFoundRules ++;
				}
			}
			if (numberOfFoundRules > 1) {
		        this.logger.warn(new Date().toString() + " Duplicate rules with logical key (from, to, ruleTypeKey: "  + moduleFromLogicalPath + ", " + moduleTologicalPath + ", " + ruleTypeKey);
			}
		}
		return foundRule;
	}

	public boolean getAppliedRuleIsEnabled(long appliedRuleId) {
		return SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId).isEnabled();
	}

	// Returns a flat list of main rules and exception rules
	public ArrayList<AppliedRuleStrategy> getAllAppliedRules() {
		ArrayList<AppliedRuleStrategy> ruleList = SoftwareArchitecture.getInstance().getAppliedRules();
		return ruleList;
	}

	// Returns a flat list of main rules and exception rules
	public ArrayList<AppliedRuleStrategy> getAllEnabledAppliedRules() {
		ArrayList<AppliedRuleStrategy> ruleList = SoftwareArchitecture.getInstance().getAppliedRules();
		ArrayList<AppliedRuleStrategy> enabledRuleList = new ArrayList<AppliedRuleStrategy>();
		for (AppliedRuleStrategy ar : ruleList) {
			if (ar.isEnabled()) {
				enabledRuleList.add(ar);
			}
		}
		return enabledRuleList;
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
			 if ((rule.getId() == appliedRule.getId()) || (rule.equals(appliedRule))) {
				 return true;
			 }
		 }
		 return false;
	 }

	 public void removeAppliedRule(long appliedrule_id) {
		 softwareArchitecture.removeAppliedRule(appliedrule_id);
		 ServiceProvider.getInstance().getDefineService()
		 .notifyServiceListeners();
	 }

	 public void removeAppliedRules() {
		 softwareArchitecture.removeAppliedRules();
		 ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	 }

	 public void removeExceptionById(long parentRuleId, long exceptionRuleId) {
		if (exceptionRuleId != -1) {
			try {
				AppliedRuleStrategy parentRule = softwareArchitecture.getAppliedRuleById(parentRuleId);
				parentRule.removeExceptionById(exceptionRuleId);
				softwareArchitecture.removeAppliedRule(exceptionRuleId);
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

	 public void updateAppliedRule(long appliedRuleId, Boolean isGenerated, String ruleTypeKey, String description, String[] dependencies,
			 String regex, long ModuleStrategyFromId, long ModuleStrategyToId, boolean enabled) {
		 ModuleStrategy moduleStrategyFrom = SoftwareArchitecture.getInstance().getModuleById(ModuleStrategyFromId);
		 ModuleStrategy moduleStrategyTo = SoftwareArchitecture.getInstance().getModuleById(ModuleStrategyToId);
		 if ((moduleStrategyFrom != null) && (moduleStrategyTo != null)) {
			 updateAppliedRule(appliedRuleId, ruleTypeKey, description, dependencies, regex, moduleStrategyFrom, moduleStrategyTo, enabled);
		 }
	 }

	 public void updateAppliedRule(long appliedRuleId, String ruleTypeKey,
			 String description, String[] dependencies, String regex,
			 ModuleStrategy ModuleStrategyFrom, ModuleStrategy ModuleStrategyTo, boolean enabled) {
		 AppliedRuleStrategy rule = SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId);
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
	        	String regex, ModuleStrategy moduleFrom, ModuleStrategy moduleTo, String[] dependencies) {
	        	try{
	    			AppliedRuleStrategy parentRule = SoftwareArchitecture.getInstance().getAppliedRuleById(parentRuleId);
	    			String parentRuleType = parentRule.getRuleTypeKey(); 
	    			ModuleStrategy parentModuleTo = parentRule.getModuleTo();
	    	    	// Check constraint: If ruleType IsAllowdToUse, then moduleFrom and moduleTo should be the same or a subset of of these of the parentModuleTo.
	    	        if ((parentRuleType.equals("FacadeConvention")) || (parentRuleType.equals("IsNotAllowedToUse"))){
	    	        	boolean matching = doesExceptionModuleMatchWithParentModule(parentModuleTo, moduleTo);
	    	        	if (!matching){
	    	        		throw new RuntimeException("IncorrectToModuleFacadeConvExc");
	    	        	} 
	    	        } 
	        	    // Create exception rule
	    	        boolean isEnabled = true;
	    	        boolean isException = true;
	    			long exceptionRuleId = addAppliedRule(ruleTypeKey, description, dependencies, regex, moduleFrom, moduleTo, isEnabled, isException, parentRule);
	    			AppliedRuleStrategy exceptionRule = getAppliedRuleById(exceptionRuleId);

	    	        // Add exception rule to Parent rule
	        		ArrayList<AppliedRuleStrategy> rules = new ArrayList<AppliedRuleStrategy>();
	        		rules.add(exceptionRule);
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
	            if (pathToParent.equals(pathToException) || (pathToParent.equals("**"))) {
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
