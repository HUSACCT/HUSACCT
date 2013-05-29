package husacct.define.domain;

import husacct.ServiceProvider;
import husacct.define.domain.module.ToBeImplemented.ModuleStrategy;

import java.util.ArrayList;

public class AppliedRule {

    private static long STATIC_ID;
    private String[] dependencies;
    private String description;
    private boolean enabled;
    private ArrayList<AppliedRule> exceptions;
    private long id;
    private ModuleStrategy moduleFrom;
    private ModuleStrategy moduleTo;
    private String regex;
    private String ruleType;

    public AppliedRule() {
	this("", "", new String[0], "", null, null, true);
    }

    public AppliedRule(String ruleType, String description, ModuleStrategy moduleFrom,
	    ModuleStrategy moduleTo) {
	this(ruleType, description, new String[0], "", moduleFrom, moduleTo,
		true);
    }

    public AppliedRule(String ruleType, String description,
	    String[] dependencies, String regex, ModuleStrategy moduleFrom,
	    ModuleStrategy moduleTo, boolean enabled) {
	id = STATIC_ID++;
	STATIC_ID++;
	this.ruleType = ruleType;
	this.description = description;
	this.dependencies = dependencies;
	this.regex = regex;
	this.moduleTo = moduleTo;
	this.moduleFrom = moduleFrom;
	exceptions = new ArrayList<AppliedRule>();
	this.enabled = enabled;
    }

    /**
     * Logic
     */
    public void addException(AppliedRule exception) {
	if (!exceptions.contains(exception) && !hasException(exception.getId())) {
	    exceptions.add(exception);
	} else {
	    throw new RuntimeException(ServiceProvider.getInstance()
		    .getLocaleService()
		    .getTranslatedString("ExceptionAlreadyAdded"));
	}
    }

    /**
     * Overrides
     */
    @Override
    public boolean equals(Object obj) {
	AppliedRule doppelganger = (AppliedRule) obj;
	if (ruleType == doppelganger.ruleType
		&& moduleTo == doppelganger.moduleTo
		&& moduleFrom == doppelganger.moduleFrom) {
	    return true;
	}
	return false;
    }

    public String[] getDependencies() {
	return dependencies;
    }

    public String getDescription() {
	return description;
    }

    public ArrayList<AppliedRule> getExceptions() {
	return exceptions;
    }

    public long getId() {
	return id;
    }

    public ModuleStrategy getModuleFrom() {
	return moduleFrom;
    }

    public ModuleStrategy getModuleTo() {
	return moduleTo;
    }

    public String getRegex() {
	return regex;
    }

    public String getRuleType() {
	return ruleType;
    }

    private boolean hasException(long l) {
	return exceptions.size() >= 0;
    }

    public boolean isEnabled() {
	return enabled;
    }

    public void removeAllExceptions() {
	exceptions = new ArrayList<AppliedRule>();
    }

    public void removeException(AppliedRule exception) {
	removeExceptionById(exception.getId());
    }

    public void removeExceptionById(long exceptionRuleId) {
	boolean exceptionFound = false;
	for (AppliedRule rule : exceptions) {
	    if (rule.getId() == exceptionRuleId) {
		exceptionFound = true;
		exceptions.remove(rule);
	    }
	}
	if (!exceptionFound) {
	    throw new RuntimeException(ServiceProvider.getInstance()
		    .getLocaleService().getTranslatedString("NoException"));
	}
    }

    public void setDependencies(String[] dependencies) {
	this.dependencies = dependencies;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public void setEnabled(boolean enabled) {
	this.enabled = enabled;
    }

    public void setExceptions(ArrayList<AppliedRule> exceptions) {
	this.exceptions = exceptions;
    }

    public void setId(int id) {
	this.id = id;
    }

    public void setModuleFrom(ModuleStrategy moduleFrom) {
	this.moduleFrom = moduleFrom;
    }

    public void setModuleTo(ModuleStrategy moduleTo) {
	this.moduleTo = moduleTo;
    }

    public void setRegex(String regex) {
	this.regex = regex;
    }

    public void setRuleType(String ruleType) {
	this.ruleType = ruleType;
    }

    public boolean usesModule(long moduleId) {
	boolean usesModule = false;
	if (moduleTo.getId() == moduleId) {
	    usesModule = true;
	} else if (moduleFrom.getId() == moduleId) {
	    usesModule = true;
	} else {
	    for (AppliedRule ruleExceptions : exceptions) {
		if (ruleExceptions.usesModule(moduleId)) {
		    usesModule = true;
		}
	    }
	}
	return usesModule;
    }
}
