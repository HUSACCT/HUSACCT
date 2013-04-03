package husacct.validate.domain.factory.message;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Violation;

import java.util.IllegalFormatException;
import java.util.List;

import org.apache.log4j.Logger;

public class Messagebuilder {
	private Logger logger = Logger.getLogger(Messagebuilder.class);
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private final String whiteSpace = " ";
	
	public String createMessage(Violation violation) {
		return generateMessage(violation.getMessage());
	}

	public String createMessage(Message message) {
		return generateMessage(message);
	}	

	private String generateMessage(Message message) {
		String messageText = generateSingleMessage(message);

		for(int i = 0; i < message.getExceptionMessage().size(); i++) {
			if(i == 0) {
				messageText += generateFirstExceptionMessage(message.getExceptionMessage().get(i));
			}
			else{
				messageText += generateRestExceptionMessage(message.getExceptionMessage().get(i));
			}
		}
		return messageText;
	}

	private String generateSingleMessage(Message message) {
		final String left = generateLeftMessage(message);		
		final String right = generateRightMessage(message);

		final String textFormat = getTextFormat(message.getRuleKey());

		return generateMessage(textFormat, left, right);
	}

	private String generateLeftMessage(Message message) {
		final String logicalModuleFromPath = message.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath();
		final String logicalModuleFromType = message.getLogicalModules().getLogicalModuleFrom().getLogicalModuleType();

		return appendStrings(logicalModuleFromType, logicalModuleFromPath);
	}

	private String generateRightMessage(Message message) {
		if(message.getRuleKey().toLowerCase().equals("namingconvention")) {
			return generateNamingConventionMessage(message);
		}
		else if(message.getRuleKey().toLowerCase().equals("visibilityconvention")) {
			return generateInterfaceConventionMessage(message);
		}
		else {
			final String logicalModuleToPath = message.getLogicalModules().getLogicalModuleTo().getLogicalModulePath();
			final String logicalModuleToType = message.getLogicalModules().getLogicalModuleTo().getLogicalModuleType();
			return appendStrings(logicalModuleToType, logicalModuleToPath);
		}
	}

	private String generateNamingConventionMessage(Message message) {
		return message.getRegex();
	}

	private String generateInterfaceConventionMessage(Message message) {
		List<String> violationTypeKeys = message.getViolationTypeKeys();

		StringBuilder sb = new StringBuilder();
		String seperationCharacter = ", ";
		for(int iterator = 0; iterator < violationTypeKeys.size(); iterator++) {	

			sb.append(violationTypeKeys.get(iterator));
			if(iterator != violationTypeKeys.size()-1) {				
				if(iterator == violationTypeKeys.size()-2) {
					sb.append(whiteSpace);
					sb.append(localeService.getTranslatedString("OrMessage"));
					sb.append("whiteSpace");
				}
				else {
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
		try{
			final String ruleTextKey = String.format("%sMessage", ruleTypeKey);
			return localeService.getTranslatedString(ruleTextKey);		
		}
		catch(IllegalFormatException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	private String generateMessage(String textFormat, String left, String right) {
		try {
			return String.format(textFormat, left, right);
		}
		catch(IllegalFormatException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	private String generateFirstExceptionMessage(Message message) {
		StringBuilder sb = new StringBuilder();		
		try {
			sb.append(whiteSpace);
			sb.append(localeService.getTranslatedString("ExceptionMessage"));
			sb.append(whiteSpace);

			final String exceptionKey = sb.toString();
			return exceptionKey + generateSingleMessage(message);
		}
		catch(IllegalFormatException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	private String generateRestExceptionMessage(Message message) {
		StringBuilder sb = new StringBuilder();
		try {			
			sb.append(whiteSpace);
			sb.append(localeService.getTranslatedString("EnumerationMessage"));
			sb.append(whiteSpace);			

			final String exceptionKey = sb.toString();
			return exceptionKey + generateSingleMessage(message);
		}
		catch(IllegalFormatException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}
}