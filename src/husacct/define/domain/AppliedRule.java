package husacct.define.domain;

import husacct.define.domain.module.Module;

import java.util.ArrayList;

public class AppliedRule {
	
	private static long STATIC_ID;
	private long id;
	private String description;
	private String[] dependencies;
	private String regex;
	private Module moduleTo;
	private Module moduleFrom;
	private String ruleType;
	private boolean enabled;
	private ArrayList<AppliedRule> exceptions;

	
	/**
	 * Contructors
	 */
	public AppliedRule(String ruleType, String description, String[] dependencies,
			String regex, Module moduleTo,
			Module moduleFrom, boolean enabled) {
		this.id = STATIC_ID++;
		STATIC_ID++;
		this.ruleType = ruleType;
		this.description = description;
		this.dependencies = dependencies;
		this.regex = regex;
		this.moduleTo = moduleTo;
		this.moduleFrom = moduleFrom;
		this.exceptions = new ArrayList<AppliedRule>();
		this.enabled = enabled;
	}
	
	public AppliedRule(String ruleType, String description, Module moduleTo, Module moduleFrom){
		this(ruleType, description, new String[0], "",moduleTo,moduleFrom, true);
	}

	public AppliedRule() {
		this("", "",new String[0], "",null,null, true);
	}

	/**
	 * Logic
	 */
	public void addException(AppliedRule exception)
	{
		if(!exceptions.contains(exception) && !this.hasException(exception.getId())) {
			exceptions.add(exception);
		} else {
			throw new RuntimeException("This exception has already been added!");
		}
	}
	
	private boolean hasException(long l) 
	{
		for(AppliedRule exception : exceptions) 
		{
			if(exception.getId() == l)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void removeException(AppliedRule exception)
	{
		removeExceptionById(exception.getId());
	}
	
	public void removeExceptionById(long exceptionRuleId) {
		boolean exceptionFound = false;
		for (AppliedRule rule : exceptions){
			if (rule.getId() == exceptionRuleId){
				exceptionFound = true;
				exceptions.remove(rule);
			}
		}
		if (!exceptionFound){throw new RuntimeException("This exception does not exist!");}
	}
	
	public void removeAllExceptions() {
		exceptions = new ArrayList<AppliedRule>();
	}
	
	public boolean usesModule(long moduleId) {
		boolean usesModule = false;
		if (moduleTo.getId() == moduleId){
			usesModule = true;
		}else if( moduleFrom.getId() == moduleId){
			usesModule = true;
		}else{			
			for (AppliedRule ruleExceptions : exceptions){
				if (ruleExceptions.usesModule(moduleId)){
					usesModule = true;
				}
			}
		}
		return usesModule;
	}
	
	/**
	 * Getters & Setters
	 */
	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
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


	public void setId(int id) {
		this.id = id;
	}


	public long getId() {
		return id;
	}

	public void setExceptions(ArrayList<AppliedRule> exceptions) {
		this.exceptions = exceptions;
	}


	public ArrayList<AppliedRule> getExceptions() {
		return exceptions;
	}

	public void setDependencies(String[] dependencies) {
		this.dependencies = dependencies;
	}

	public String[] getDependencies() {
		return dependencies;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getRegex() {
		return regex;
	}

	public Module getModuleTo() {
		return moduleTo;
	}

	public void setModuleTo(Module moduleTo) {
		this.moduleTo = moduleTo;
	}

	public Module getModuleFrom() {
		return moduleFrom;
	}

	public void setModuleFrom(Module moduleFrom) {
		this.moduleFrom = moduleFrom;
	}

}
