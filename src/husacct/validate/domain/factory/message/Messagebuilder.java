package husacct.validate.domain.factory.message;

import java.util.IllegalFormatException;

import org.apache.log4j.Logger;

import husacct.common.dto.MessageDTO;
import husacct.validate.abstraction.language.ResourceBundles;
import husacct.validate.domain.assembler.MessageAssembler;
import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Violation;

public class Messagebuilder {
	private Logger logger = Logger.getLogger(Messagebuilder.class);

	public String createMessage(MessageDTO message){
		return generateMessage(message);
	}	

	public String createMessage(Violation violation){
		MessageAssembler assembler = new MessageAssembler();
		MessageDTO messageDTO = assembler.createMessageDTO(violation.getMessage());
		return createMessage(messageDTO);
	}

	public String createMessage(Message message){
		MessageAssembler assembler = new MessageAssembler();
		MessageDTO messageDTO = assembler.createMessageDTO(message);
		return createMessage(messageDTO);
	}

	private String generateMessage(MessageDTO message){
		try{
			final String ruleTextKey = String.format("%sMessage", message.getRuleKey());
			final String textFormat = ResourceBundles.getValue(ruleTextKey);		
			return String.format(textFormat, message.getLeftText(), message.getRightText());
		}catch(IllegalFormatException e){
			logger.error(e.getMessage(), e);
		}
		return "";
	}
	
//	private String exceptionMessage(String mainMessage){
//		try{
//			final String exceptionKey = ResourceBundles.getValue("ExceptionMessage");
//			return mainMessage += exceptionKey;
//		}catch(IllegalFormatException e){
//			logger.error(e.getMessage(), e);
//		}
//		return mainMessage;
//	}
}