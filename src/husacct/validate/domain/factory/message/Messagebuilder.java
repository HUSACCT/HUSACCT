package husacct.validate.domain.factory.message;

import husacct.ServiceProvider;
import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Violation;

import java.util.IllegalFormatException;

import org.apache.log4j.Logger;

public class Messagebuilder {
	private Logger logger = Logger.getLogger(Messagebuilder.class);

	public String createMessage(Violation violation){
		return generateMessage(violation.getMessage());
	}

	public String createMessage(Message message){
		return generateMessage(message);
	}

	private String generateMessage(Message message){
		String messageText = generateSingleMessage(message);

		for(int i = 0; i < message.getExceptionMessage().size(); i ++ ){
			if(i == 0){
				messageText += generateFirstExceptionMessage(message.getExceptionMessage().get(i));
			}
			else{
				messageText += generateRestExceptionMessage(message.getExceptionMessage().get(i));
			}
		}
		return messageText;
	}

	private String generateSingleMessage(Message message){
		final String logicalModuleFromPath = message.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath();
		final String logicalModuleFromType = message.getLogicalModules().getLogicalModuleFrom().getLogicalModuleType();

		final String logicalModuleToPath = message.getLogicalModules().getLogicalModuleTo().getLogicalModulePath();
		final String logicalModuleToType = message.getLogicalModules().getLogicalModuleTo().getLogicalModuleType();

		final String left = appendStrings(logicalModuleFromType, logicalModuleFromPath);
		final String right = appendStrings(logicalModuleToType, logicalModuleToPath);

		final String textFormat = getTextFormat(message.getRuleKey());

		return generateMessage(textFormat, left, right);
	}

	private String appendStrings(String left, String right){
		return String.format("%s %s", left, right);
	}

	private String getTextFormat(String ruleTypeKey){
		try{
			final String ruleTextKey = String.format("%sMessage", ruleTypeKey);
			return ServiceProvider.getInstance().getControlService().getTranslatedString(ruleTextKey);		
		}catch(IllegalFormatException e){
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	private String generateMessage(String textFormat, String left, String right){
		try{
			return String.format(textFormat, left, right);
		}
		catch(IllegalFormatException e){
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	private String generateFirstExceptionMessage(Message message){
		try{
			final String exceptionKey = ServiceProvider.getInstance().getControlService().getTranslatedString("ExceptionMessage");
			return exceptionKey + generateSingleMessage(message);
		}catch(IllegalFormatException e){
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	private String generateRestExceptionMessage(Message message){
		try{
			final String exceptionKey = ServiceProvider.getInstance().getControlService().getTranslatedString("EnumerationMessage");
			return exceptionKey + generateSingleMessage(message);
		}catch(IllegalFormatException e){
			logger.error(e.getMessage(), e);
		}
		return "";
	}
}