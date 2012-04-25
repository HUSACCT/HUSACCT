package husacct.validate.domain.validation;

import husacct.common.dto.RuleDTO;
import husacct.validate.domain.validation.logicalmodule.LogicalModule;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;

import java.util.Collections;
import java.util.List;

public class Message {
	private LogicalModules logicalModules;
	private String ruleKey;
	private List<String> violationTypeKeys;
	private List<Message> exceptionMessage;
	
	public Message(RuleDTO appliedRule){		
		final String logicalModuleFromPath = appliedRule.moduleFrom.logicalPath;
		final String logicalModuleFromType = appliedRule.moduleFrom.type;
		final LogicalModule logicalModuleFrom = new LogicalModule(logicalModuleFromPath, logicalModuleFromType);
		
		final String logicalModuleToPath = appliedRule.moduleTo.logicalPath;
		final String logicalModuleToType = appliedRule.moduleTo.type;
		final LogicalModule logicalModuleTo = new LogicalModule(logicalModuleToPath, logicalModuleToType);
		
		this.setLogicalModules(new LogicalModules(logicalModuleFrom, logicalModuleTo));
		
		this.ruleKey = appliedRule.ruleTypeKey;
		this.violationTypeKeys = Collections.emptyList();
		this.exceptionMessage = Collections.emptyList();		
	}	
	
	public Message(LogicalModules logicalModules, String ruleKey, List<String> violationTypeKeys, List<Message> exceptionMessage){
		this.logicalModules = logicalModules;
		this.ruleKey = ruleKey;
		this.violationTypeKeys = violationTypeKeys;
		this.exceptionMessage = exceptionMessage;
	}
	
	public Message(LogicalModules logicalModules, String ruleKey, List<String> violationTypeKeys){
		this.logicalModules = logicalModules;
		this.ruleKey = ruleKey;
		this.violationTypeKeys = violationTypeKeys;
		this.exceptionMessage = Collections.emptyList();
	}
	
	public Message(LogicalModules logicalModules, String ruleKey){
		this.logicalModules = logicalModules;
		this.ruleKey = ruleKey;
		this.violationTypeKeys = Collections.emptyList();
		this.exceptionMessage = Collections.emptyList();
	}

	public String getRuleKey() {
		return ruleKey;
	}

	public List<String> getViolationTypeKeys() {
		return violationTypeKeys;
	}

	public List<Message> getExceptionMessage() {
		return exceptionMessage;
	}

	public LogicalModules getLogicalModules() {
		return logicalModules;
	}

	public void setLogicalModules(LogicalModules logicalModules) {
		this.logicalModules = logicalModules;
	}
}