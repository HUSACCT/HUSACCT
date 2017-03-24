package husacct.define.domain.appliedrule;

import husacct.ServiceProvider;
import husacct.define.domain.module.ModuleStrategy;
import java.util.ArrayList;

public abstract class AppliedRuleStrategy {
	protected static long STATIC_ID = 1;
	private long id;
	protected String ruleTypeKey;
	private String description;
	private String[] dependencyTypes;
	private String regex;
	private ModuleStrategy moduleTo;
	private ModuleStrategy moduleFrom;
	private boolean enabled;
	private ArrayList<AppliedRuleStrategy> exceptions;
	private boolean isException = false;
	// Reference to the main rule of which it is an exception
	private AppliedRuleStrategy parentAppliedRule = null;
	
	public static void setStaticId(long highestId){
		STATIC_ID = highestId++;
		STATIC_ID++;
	}
	
	public void setAppliedRule(String description, String[] dependencies,
			String regex, ModuleStrategy moduleFrom,
			ModuleStrategy moduleTo, boolean enabled, boolean isException, AppliedRuleStrategy parentRule) {
		long newId = STATIC_ID++;
		this.id = newId;
		STATIC_ID++;
		this.description = description;
		this.dependencyTypes = dependencies;
		this.regex = regex;
		this.moduleTo = moduleTo;
		this.moduleFrom = moduleFrom;
		this.exceptions = new ArrayList<AppliedRuleStrategy>();
		this.enabled = enabled;
		this.isException = isException;
		this.parentAppliedRule = parentRule;
	}
	
	public void setAppliedRule(String description, ModuleStrategy moduleFrom, ModuleStrategy moduleTo){
		setAppliedRule(description, new String[0], "",moduleFrom,moduleTo, true, false, null);
	}

	public void setAppliedRule() {
		setAppliedRule("", new String[0], "",null,null, true, false, null);
	}
	
	/**
	 * Logic
	 */
	
	public void addException(AppliedRuleStrategy exception){
		exceptions.add(exception);
	}

	public void removeException(AppliedRuleStrategy exception){
		removeExceptionById(exception.getId());
	}

	public void removeExceptionById(long exceptionRuleId) {
		boolean exceptionFound = false;
		for (AppliedRuleStrategy rule : exceptions){
			if (rule.getId() == exceptionRuleId){
				exceptionFound = true;
				exceptions.remove(rule);
				break;
			}
		}
		if (!exceptionFound){throw new RuntimeException(ServiceProvider.getInstance().getLocaleService().getTranslatedString("NoException"));}
	}

	public void removeAllExceptions() {
		exceptions = new ArrayList<AppliedRuleStrategy>();
	}

	public boolean usesModule(long moduleId) {
		boolean usesModule = false;
		if (moduleTo.getId() == moduleId){
			usesModule = true;
		}else if( moduleFrom.getId() == moduleId){
			usesModule = true;
		}else{			
			for (AppliedRuleStrategy ruleExceptions : exceptions){
				if (ruleExceptions.usesModule(moduleId)){
					usesModule = true;
				}
			}
		}
		return usesModule;
	}

	public String getRuleTypeKey() {
		return ruleTypeKey;
	}
	
	public void setRuleType(String ruleTypeKey){
		this.ruleTypeKey = ruleTypeKey;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setExceptions(ArrayList<AppliedRuleStrategy> exceptions) {
		this.exceptions = exceptions;
	}

	public ArrayList<AppliedRuleStrategy> getExceptions() {
		return exceptions;
	}
	public AppliedRuleStrategy getExeptionByID(long exeptionId){
		for (AppliedRuleStrategy result : exceptions) {
			if (result.getId()==exeptionId) {
				return result;
			}
		}	
		return null;
	}

	public void setDependencyTypes(String[] dependencyTypes) {
		this.dependencyTypes = dependencyTypes;
	}

	public String[] getDependencyTypes() {
		return dependencyTypes;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getRegex() {
		return regex;
	}

	public ModuleStrategy getModuleTo() {
		return moduleTo;
	}

	public void setModuleTo(ModuleStrategy moduleTo) {
		this.moduleTo = moduleTo;
	}

	public ModuleStrategy getModuleFrom() {
		return moduleFrom;
	}

	public void setModuleFrom(ModuleStrategy moduleFrom) {
		this.moduleFrom = moduleFrom;
	}
	
	public boolean isException() {
		return isException;
	}

	public void getIsExeption(boolean isException) {
		this.isException = isException;
	}

	public AppliedRuleStrategy getParentAppliedRule() {
		return parentAppliedRule;
	}

	public void setParentAppliedRule(AppliedRuleStrategy parentAppliedRule) {
		this.parentAppliedRule = parentAppliedRule;
	}


	public boolean equals(AppliedRuleStrategy doppelganger){
		boolean result = false;
		if((this.id == doppelganger.getId()) || (this.ruleTypeKey==doppelganger.ruleTypeKey && 
				this.moduleTo == doppelganger.moduleTo && this.moduleFrom == doppelganger.moduleFrom)){
			result = true;
		}
			return result;
	}
	
	/**
	 * Abstract
	 */
	public abstract boolean checkConvention();
	
    @Override
	public String toString() {
        String representation = "";
        representation += "\nId: " + id + ", Type: " + ruleTypeKey;
        representation += "\nEnabled: " + enabled + ", IsException: " + isException;
        representation += "\nFrom: " + moduleFrom.getName();
        representation += "\nTo: " + moduleTo.getName();
        representation += "\nExceptions: " + exceptions.size();
        representation += "\n";
        return representation;
    }

	
}
