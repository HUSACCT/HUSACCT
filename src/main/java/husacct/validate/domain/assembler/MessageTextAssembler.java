package husacct.validate.domain.assembler;

import husacct.ServiceProvider;
import husacct.common.dto.RuleDTO;
import husacct.common.locale.ILocaleService;
import husacct.define.IDefineService;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.List;

import org.apache.log4j.Logger;

public class MessageTextAssembler {

	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private Logger logger = Logger.getLogger(MessageTextAssembler.class);
	private final String whiteSpace = " ";

	/**
	 * 	Creates message for the given rule + message for exception-rules included in the given rule	
	 * @param violation
	 * @return
	 */
	public String createMessageText(Violation violation) {
		String messageText = "";
		RuleDTO rule = getRule(violation);
		if (rule != null) {
			messageText = generateCompleteMessageText(rule);
		}
		return messageText;
	}

	/**
	 * 	Generates message for the given rule only, not for exception-rules included in the given rule
	 * @param violation
	 * @return
	 */
	public String createMessageTextOfMainRule(Violation violation) {
		String messageText = "";
		RuleDTO rule = getRule(violation);
		if (rule != null) {
			messageText = generateMessageTextForRule(rule);
		}
		return messageText;
	}

	private RuleDTO getRule(Violation violation) {
		RuleDTO rule = null;
		String fromModule = violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath();
		String toModule = violation.getLogicalModules().getLogicalModuleTo().getLogicalModulePath();
		String ruleTypeKey = violation.getRuletypeKey();
		IDefineService defineService = ServiceProvider.getInstance().getDefineService();
		rule = defineService.getMainRuleBy_From_To_RuleTypeKey(fromModule, toModule, ruleTypeKey);
		return rule;
	}
	
	private String generateCompleteMessageText(RuleDTO rule) {
		String messageText = generateMessageTextForRule(rule);
		int i = 1;
		for (RuleDTO exception : rule.exceptionRules) {
			if (i == 0) {
				messageText += generateFirstExceptionMessage(exception);
			} else {
				messageText += generateRestExceptionMessage(exception);
			}
			i ++;
		}
		return messageText;
	}

	private String generateMessageTextForRule(RuleDTO rule) {
		final String left = generateFromModuleText(rule);
		final String right = generateToModuleText(rule);
		final String textFormat = getTextFormat(rule.ruleTypeKey);
		return formatMessageText(textFormat, left, right);
	}

	private String generateFromModuleText(RuleDTO rule) {
		final String logicalModuleFromPath = rule.moduleFrom.logicalPath;
		final String logicalModuleFromType = rule.moduleFrom.type;
		return appendStrings(logicalModuleFromType, logicalModuleFromPath);
	}

	private String generateToModuleText(RuleDTO rule) {
		if ((rule.ruleTypeKey.toLowerCase().equals(RuleTypes.FACADE_CONVENTION.toString().toLowerCase())) ||
				(rule.ruleTypeKey.toLowerCase().equals(RuleTypes.IS_NOT_ALLOWED_BACK_CALL.toString().toLowerCase())) ||
				(rule.ruleTypeKey.toLowerCase().equals(RuleTypes.IS_NOT_ALLOWED_SKIP_CALL.toString().toLowerCase()))) {
			return appendStrings("", "");
		} else
		if (rule.ruleTypeKey.toLowerCase().equals(RuleTypes.NAMING_CONVENTION.toString().toLowerCase())) {
			return generateNamingConventionMessage(rule);
		} else if (rule.ruleTypeKey.toLowerCase().equals(RuleTypes.VISIBILITY_CONVENTION.toString().toLowerCase())) {
			return generateVisibilityConventionMessage(rule);
		} else {
			final String logicalModuleToPath = rule.moduleTo.logicalPath;
			final String logicalModuleToType = rule.moduleTo.type;
			return appendStrings(logicalModuleToType, logicalModuleToPath);
		}
	}

	private String generateNamingConventionMessage(RuleDTO rule) {
		return rule.regex;
	}

	private String generateVisibilityConventionMessage(RuleDTO rule) {
		List<String> violationTypeKeys = Arrays.asList(rule.violationTypeKeys);
		StringBuilder sb = new StringBuilder();
		String seperationCharacter = ", ";
		for (int iterator = 0; iterator < violationTypeKeys.size(); iterator++) {
			sb.append(violationTypeKeys.get(iterator));
			if (iterator != violationTypeKeys.size() - 1) {
				if (iterator == violationTypeKeys.size() - 2) {
					sb.append(whiteSpace);
					sb.append(localeService.getTranslatedString("OrMessage"));
					sb.append(whiteSpace);
				} else {
					sb.append(seperationCharacter);
				}
			}
		}
		return sb.toString();
	}

	private String appendStrings(String left, String right) {
		return String.format("%s %s", left, right);
	}

	private String getTextFormat(String ruleTypeKey) {
		try {
			final String ruleTextKey = String.format("%sMessage", ruleTypeKey);
			return localeService.getTranslatedString(ruleTextKey);
		} catch (IllegalFormatException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	private String formatMessageText(String textFormat, String left, String right) {
		try {
			return String.format(textFormat, left, right);
		} catch (IllegalFormatException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	private String generateFirstExceptionMessage(RuleDTO exceptionRule) {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(whiteSpace);
			sb.append(localeService.getTranslatedString("ExceptionMessage"));
			sb.append(whiteSpace);

			final String exceptionKey = sb.toString();
			return exceptionKey + generateMessageTextForRule(exceptionRule);
		} catch (IllegalFormatException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	private String generateRestExceptionMessage(RuleDTO exceptionRule) {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(whiteSpace);
			sb.append(localeService.getTranslatedString("EnumerationMessage"));
			sb.append(whiteSpace);

			final String exceptionKey = sb.toString();
			return exceptionKey + generateMessageTextForRule(exceptionRule);
		} catch (IllegalFormatException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}
}