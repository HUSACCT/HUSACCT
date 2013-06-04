package husacct.define.domain.warningmessages;

import java.util.Observable;

public class CustomWarningMessage extends WarningMessage {

	public CustomWarningMessage(String desc) {
		this.description=desc;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		
		
	}

	@Override
	public void generateMessage() {
		
		
	}

	@Override
	public Object[] getValue() {
	
		return new Object[]{description};
	}
	
	
}
