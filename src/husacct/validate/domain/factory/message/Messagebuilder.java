package husacct.validate.domain.factory.message;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.IllegalFormatException;
import java.util.List;

import org.apache.log4j.Logger;

public class Messagebuilder {

	private Logger logger = Logger.getLogger(Messagebuilder.class);
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private final String whiteSpace = " ";

	public String createMessage(Violation violation) {
		return generateMessage(violation.getMessage(), violation);
	}

	public String createMessage(Message message, Violation violation) {
		return generateMessage(message, violation);
	}

	private String generateMessage(Message message, Violation violation) {
		String messageText = generateSingleMessage(message, violation);

		for (int i = 0; i < message.getExceptionMessage().size(); i++) {
			if (i == 0) {
				messageText += generateFirstExceptionMessage(message.getExceptionMessage().get(i), violation);
			} else {
				messageText += generateRestExceptionMessage(message.getExceptionMessage().get(i), violation);
			}
		}
		return messageText;
	}

	private String generateSingleMessage(Message message, Violation violation) {
		final String left = generateLeftMessage(message, violation);
		final String right = generateRightMessage(message, violation);

		final String textFormat = getTextFormat(message.getRuleKey());

		return generateMessage(textFormat, left, right);
	}

	private String generateLeftMessage(Message message, Violation violation) {
		if (message.getRuleKey().toLowerCase().equals(RuleTypes.FACADE_CONVENTION.toString().toLowerCase())) {
			return generateLeftFacadeConventionMessage(violation);
		}
		final String logicalModuleFromPath = message.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath();
		final String logicalModuleFromType = message.getLogicalModules().getLogicalModuleFrom().getLogicalModuleType();

		return appendStrings(logicalModuleFromType, logicalModuleFromPath);
	}

	private String generateRightMessage(Message message, Violation violation) {
		if (message.getRuleKey().toLowerCase().equals(RuleTypes.FACADE_CONVENTION.toString().toLowerCase())) {
			return generateRightFacadeConventionMessage(violation);
		} else if (message.getRuleKey().toLowerCase().equals(RuleTypes.NAMING_CONVENTION.toString().toLowerCase())) {
			return generateNamingConventionMessage(message);
		} else if (message.getRuleKey().toLowerCase().equals(RuleTypes.VISIBILITY_CONVENTION.toString().toLowerCase())) {
			return generateInterfaceConventionMessage(message);
		} else {
			final String logicalModuleToPath = message.getLogicalModules().getLogicalModuleTo().getLogicalModulePath();
			final String logicalModuleToType = message.getLogicalModules().getLogicalModuleTo().getLogicalModuleType();
			return appendStrings(logicalModuleToType, logicalModuleToPath);
		}
	}

	private String generateLeftFacadeConventionMessage(Violation violation) {
		return violation.getClassPathFrom();
		
	}
	
	private String generateRightFacadeConventionMessage(Violation violation) {
		return violation.getClassPathTo();
	}
	
	private String generateNamingConventionMessage(Message message) {
		return message.getRegex();
	}

	private String generateInterfaceConventionMessage(Message message) {
		List<String> violationTypeKeys = message.getViolationTypeKeys();

		StringBuilder sb = new StringBuilder();
		String seperationCharacter = ", ";
		for (int iterator = 0; iterator < violationTypeKeys.size(); iterator++) {

			sb.append(violationTypeKeys.get(iterator));
			if (iterator != violationTypeKeys.size() - 1) {
				if (iterator == violationTypeKeys.size() - 2) {
					sb.append(whiteSpace);
					sb.append(localeService.getTranslatedString("OrMessage"));
					sb.append("whiteSpace");
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

	private String generateMessage(String textFormat, String left, String right) {
		try {
			return String.format(textFormat, left, right);
		} catch (IllegalFormatException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	private String generateFirstExceptionMessage(Message message, Violation violation) {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(whiteSpace);
			sb.append(localeService.getTranslatedString("ExceptionMessage"));
			sb.append(whiteSpace);

			final String exceptionKey = sb.toString();
			return exceptionKey + generateSingleMessage(message, violation);
		} catch (IllegalFormatException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	private String generateRestExceptionMessage(Message message, Violation violation) {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(whiteSpace);
			sb.append(localeService.getTranslatedString("EnumerationMessage"));
			sb.append(whiteSpace);

			final String exceptionKey = sb.toString();
			return exceptionKey + generateSingleMessage(message, violation);
		} catch (IllegalFormatException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}
}