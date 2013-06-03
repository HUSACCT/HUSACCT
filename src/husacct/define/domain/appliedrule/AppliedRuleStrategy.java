package husacct.define.domain.appliedrule;

import husacct.ServiceProvider;
import husacct.define.domain.module.ModuleStrategy;

import java.util.ArrayList;

public abstract class AppliedRuleStrategy {
	protected String type;
	protected static long STATIC_ID;
	private long id;
	private String description;
	private String[] dependencies;
	private String regex;
	private ModuleStrategy moduleTo;
	private ModuleStrategy moduleFrom;
	private boolean enabled;
	private ArrayList<AppliedRuleStrategy> exceptions;
	
	public void setAppliedRule(String description, String[] dependencies,
			String regex, ModuleStrategy moduleFrom,
			ModuleStrategy moduleTo, boolean enabled) {
		this.id = STATIC_ID++;
		STATIC_ID++;
		this.description = description;
		this.dependencies = dependencies;
		this.regex = regex;
		this.moduleTo = moduleTo;
		this.moduleFrom = moduleFrom;
		this.exceptions = new ArrayList<AppliedRuleStrategy>();
		this.enabled = enabled;
	}
	
	public void setAppliedRule(AppliedRuleStrategy clone){
		this.description = clone.getDescription();
		this.dependencies = clone.getDependencies();
		this.regex = clone.getRegex();
		this.moduleTo = clone.getModuleTo();
		this.moduleFrom = clone.getModuleFrom();
		this.exceptions = clone.getExceptions();
		this.enabled = clone.isEnabled();
	}
	
	public void setAppliedRule(String description, ModuleStrategy moduleFrom, ModuleStrategy moduleTo){
		setAppliedRule(description, new String[0], "",moduleFrom,moduleTo, true);
	}

	public void setAppliedRule() {
		setAppliedRule("",new String[0], "",null,null, true);
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

	public String getRuleType() {
		return type;
	}
	
	public void setRuleType(String type){
		this.type = type;
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
	
	private boolean sameExceptions(AppliedRuleStrategy doppelganger){
		if(this.exceptions.size() == doppelganger.exceptions.size()){
			for(AppliedRuleStrategy exception : exceptions){
				if(!doppelganger.exceptions.contains(exception)){
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Overrides
	 */
	@Override
	public boolean equals(Object obj){
		boolean returnme=false;
		AppliedRuleStrategy doppelganger = (AppliedRuleStrategy) obj;
		if(this.type==doppelganger.type 
				&& 
				this.moduleTo == doppelganger.moduleTo
				&& 
				this.moduleFrom == doppelganger.moduleFrom 
				&& 
				this.sameExceptions(doppelganger)){
		
			returnme= true;
		}
			return returnme;
	}
	
	/**
	 * Abstract
	 */
	public abstract boolean checkConvention();
	
}
