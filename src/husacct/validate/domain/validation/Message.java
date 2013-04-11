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
	private String regex;

	public Message(RuleDTO appliedRule) {
		this.ruleKey = appliedRule.ruleTypeKey;
		this.violationTypeKeys = Arrays.asList(appliedRule.violationTypeKeys);
		this.exceptionMessage = Collections.emptyList();
		this.regex = appliedRule.regex;
		this.logicalModules = createLogicalModules(appliedRule);
		this.exceptionMessage = new ArrayList<Message>(0);
		this.exceptionMessage.addAll(createExceptionMessage(appliedRule));
	}

	public Message(LogicalModules logicalModules, String ruleKey, List<String> violationTypeKeys) {
		this.logicalModules = logicalModules;
		this.ruleKey = ruleKey;
		this.regex = "";
		this.violationTypeKeys = violationTypeKeys;
		this.exceptionMessage = new ArrayList<Message>(0);
	}

	public Message(LogicalModules logicalModules, String ruleKey, List<String> violationTypeKeys, String regex, List<Message> exceptionMessage) {
		this.logicalModules = logicalModules;
		this.ruleKey = ruleKey;
		this.regex = regex;
		this.violationTypeKeys = violationTypeKeys;
		this.exceptionMessage = exceptionMessage;
	}

	private List<Message> createExceptionMessage(RuleDTO appliedRule) {
		List<Message> exceptionMessages = new ArrayList<Message>();
		for (RuleDTO exceptionRule : appliedRule.exceptionRules) {
			final LogicalModules logicalModules = createLogicalModules(exceptionRule);
			final String ruleKey = exceptionRule.ruleTypeKey;
			final List<String> violationTypeKeys = Arrays.asList(exceptionRule.violationTypeKeys);

			Message exceptionMessage = new Message(logicalModules, ruleKey, violationTypeKeys);

			exceptionMessages.add(exceptionMessage);
		}
		return exceptionMessages;
	}

	public String getRuleKey() {
		return ruleKey;
	}

	public String getRegex() {
		return regex;
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

	private LogicalModules createLogicalModules(RuleDTO appliedRule) {
		final String logicalModuleFromPath = appliedRule.moduleFrom.logicalPath;
		final String logicalModuleFromType = appliedRule.moduleFrom.type;
		final LogicalModule logicalModuleFrom = new LogicalModule(logicalModuleFromPath, logicalModuleFromType);

		final String logicalModuleToPath = appliedRule.moduleTo.logicalPath;
		final String logicalModuleToType = appliedRule.moduleTo.type;
		final LogicalModule logicalModuleTo = new LogicalModule(logicalModuleToPath, logicalModuleToType);

		return new LogicalModules(logicalModuleFrom, logicalModuleTo);
	}
}