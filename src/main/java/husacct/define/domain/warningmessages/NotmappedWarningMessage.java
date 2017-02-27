package husacct.define.domain.warningmessages;

import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.Observable;

public class NotmappedWarningMessage  extends WarningMessage{

	private AnalyzedModuleComponent unitData;
	
	public NotmappedWarningMessage(AnalyzedModuleComponent data) {
		this.unitData=data;
		generateMessage();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generateMessage() {
		description="the "+unitData.getType()+" is not mapped to an module";
		resource=unitData.getUniqueName();
		
		
	}

	@Override
	public Object[] getValue() {
		// TODO Auto-generated method stub
		return new Object[]{unitData};
	}

}
