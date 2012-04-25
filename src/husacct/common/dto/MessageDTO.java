package husacct.common.dto;

import java.util.Collections;
import java.util.List;

public class MessageDTO extends AbstractDTO {
	private final String leftText;
	private final String rightText;
	private final String ruleKey;
	private final List<MessageDTO> exceptionMessages;
	
	public MessageDTO(String leftText, String rightText, String ruleKey){
		this.leftText = leftText;
		this.rightText = rightText;
		this.ruleKey = ruleKey;
		this.exceptionMessages = Collections.emptyList();
	}
	
	public MessageDTO(String leftText, String rightText, String ruleKey, List<MessageDTO> exceptionMessages){
		this.leftText = leftText;
		this.rightText = rightText;
		this.ruleKey = ruleKey;
		this.exceptionMessages = exceptionMessages;
	}

	public String getLeftText() {
		return leftText;
	}

	public String getRightText() {
		return rightText;
	}

	public String getRuleKey() {
		return ruleKey;
	}

	public List<MessageDTO> getExceptionMessages() {
		return exceptionMessages;
	}	
}