package husacct.validate.domain.assembler;

import husacct.common.dto.MessageDTO;
import husacct.validate.domain.validation.Message;

public class MessageAssembler {
	
	public MessageDTO createMessageDTO(Message message){
		final String logicalModuleFromPath = message.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath();
		final String logicalModuleFromType = message.getLogicalModules().getLogicalModuleFrom().getLogicalModuleType();
	
		final String logicalModuleToPath = message.getLogicalModules().getLogicalModuleTo().getLogicalModulePath();
		final String logicalModuleToType = message.getLogicalModules().getLogicalModuleTo().getLogicalModuleType();
		
		final String left = String.format("%s %s", logicalModuleFromPath, logicalModuleFromType);
		final String right = String.format("%s %s", logicalModuleToPath, logicalModuleToType);
		
		return new MessageDTO(left, right, message.getRuleKey());			
	}
}