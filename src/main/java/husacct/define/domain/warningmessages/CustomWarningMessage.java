package husacct.define.domain.warningmessages;

import java.util.Observable;

public class CustomWarningMessage extends WarningMessage {
   private int count=0;
   private String descr;
	public CustomWarningMessage(String desc) {
		this.descr=desc;
		this.description=descr+"("+count+")";
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

	public void setDecription(int num) {
		this.count=num;
		this.description=descr+"("+count+")";
		
	}
	
	
}
