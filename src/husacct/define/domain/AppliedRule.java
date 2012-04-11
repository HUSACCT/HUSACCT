package husacct.define.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import husacct.define.domain.module.Module;

public class AppliedRule {
	
	private static long STATIC_ID;
	private long id;
	private String description;
	private String[] dependencies;
	private String prefix;
	private String suffix;
	private Module usedModule;
	private Module restrictedModule;
	private String ruleType;
	private boolean enabled;
	private ArrayList<AppliedRule> exceptions;

	
	//CONSTRUCTORS
	//CONSTRUCTORS
	public AppliedRule(String ruleType, String description, String[] dependencies,
			String prefix, String suffix, Module usedModule,
			Module restrictedModule) {
		this.id = STATIC_ID++;
		STATIC_ID++;
		this.ruleType = ruleType;
		this.description = description;
		this.dependencies = dependencies;
		this.prefix = prefix;
		this.suffix = suffix;
		this.usedModule = usedModule;
		this.restrictedModule = restrictedModule;
		this.exceptions = new ArrayList<AppliedRule>();
		this.enabled = true;
	}
	
	public AppliedRule(String ruleType, String description, Module usedModule, Module restrictedModule){
		this(ruleType, description, new String[0], "","",usedModule,restrictedModule);
	}

	public AppliedRule() {
		this("", "",new String[0], "","",null,null);
	}

	//LOGIC
	//LOGIC
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
		if (usedModule.getId() == moduleId){
			usesModule = true;
		}else if( restrictedModule.getId() == moduleId){
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
	
	//GETTER & SETTERS
	//GETTER & SETTERS
	//GETTER & SETTERS
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

	public void setUsedModule(Module usedModule) {
		this.usedModule = usedModule;
	}


	public Module getUsedModule() {
		return usedModule;
	}


	public void setRestrictedModule(Module restrictedModule) {
		this.restrictedModule = restrictedModule;
	}


	public Module getRestrictedModule() {
		return restrictedModule;
	}

	public void setDependencies(String[] dependencies) {
		this.dependencies = dependencies;
	}

	public String[] getDependencies() {
		return dependencies;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getSuffix() {
		return suffix;
	}
}
