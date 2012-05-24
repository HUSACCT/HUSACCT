package husacct.validate.domain.validation;

import husacct.common.dto.RuleDTO;
import husacct.validate.domain.validation.logicalmodule.LogicalModule;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Message {
	private LogicalModules logicalModules;
	private String ruleKey;
	private List<String> violationTypeKeys;
	private List<Message> exceptionMessage;
	
	public Message(RuleDTO appliedRule){				
		this.ruleKey = appliedRule.ruleTypeKey;
		this.violationTypeKeys = Arrays.asList(appliedRule.violationTypeKeys);
		this.exceptionMessage = Collections.emptyList();
		this.logicalModules = createLogicalModules(appliedRule);
		createExceptionMessage(appliedRule);
	}	
	
	public Message(LogicalModules logicalModules, String ruleKey, List<String> violationTypeKeys){
		this.logicalModules = logicalModules;
		this.ruleKey = ruleKey;
		this.violationTypeKeys = violationTypeKeys;
		this.exceptionMessage = Collections.emptyList();
	}

	public Message(LogicalModules logicalModules, String ruleKey, List<String> violationTypeKeys, List<Message> exceptionMessage){
		this.logicalModules = logicalModules;
		this.ruleKey = ruleKey;
		this.violationTypeKeys = violationTypeKeys;
		this.exceptionMessage = exceptionMessage;
	}
	
	private void createExceptionMessage(RuleDTO appliedRule) {
		List<Message> exceptionMessages = new ArrayList<Message>();
		for(RuleDTO exceptionRule : appliedRule.exceptionRules){
			final LogicalModules logicalModules = createLogicalModules(exceptionRule);
			final String ruleKey = exceptionRule.ruleTypeKey;
			final List<String> violationTypeKeys = Arrays.asList(exceptionRule.violationTypeKeys);
		
			Message exceptionMessage = new Message(logicalModules, ruleKey, violationTypeKeys);
			
			exceptionMessages.add(exceptionMessage);
		}		
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
	
	private LogicalModules createLogicalModules(RuleDTO appliedRule){
		final String logicalModuleFromPath = appliedRule.moduleFrom.logicalPath;
		final String logicalModuleFromType = appliedRule.moduleFrom.type;
		final LogicalModule logicalModuleFrom = new LogicalModule(logicalModuleFromPath, logicalModuleFromType);
		
		final String logicalModuleToPath = appliedRule.moduleTo.logicalPath;
		final String logicalModuleToType = appliedRule.moduleTo.type;
		final LogicalModule logicalModuleTo = new LogicalModule(logicalModuleToPath, logicalModuleToType);
		
		return new LogicalModules(logicalModuleFrom, logicalModuleTo);
	}
}