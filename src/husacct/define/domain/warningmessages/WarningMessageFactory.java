package husacct.define.domain.warningmessages;

import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.services.WarningMessageService;
import husacct.define.domain.services.stateservice.StateService;

public class WarningMessageFactory {









	public WarningMessageContainer getsortedMessages()
	{
		
		WarningMessageService.getInstance().updateWarnings();
		WarningMessageContainer root = new WarningMessageContainer(new CustomWarningMessage("WARNINGS"));
		WarningMessageContainer codelevelContainer = new WarningMessageContainer(new CustomWarningMessage("Code Level "));
		WarningMessageContainer implevelContainer = new WarningMessageContainer(new CustomWarningMessage("Implementation Level"));
		
		WarningMessageContainer customContainer = new WarningMessageContainer(new CustomWarningMessage("Custom"));
	WarningMessageContainer notMapped=	getNotmapped();
		addNotCodeLevel(codelevelContainer);
		addNotMappedModule(implevelContainer);
	
		
		root.addChild(implevelContainer);
		root.addChild(codelevelContainer);
		root.addChild(notMapped);
	
		
	return root;
	
	}

	

	private WarningMessageContainer getNotmapped() {
		
	return StateService.instance().getNotMappedUnits();
}
	
	private void addNotMappedModule(WarningMessageContainer implevelContainer) {
		SoftwareArchitecture.getInstance().updateWarnings();
		for (WarningMessage message : WarningMessageService.getInstance().getWarningMessages()) {
			
			 if (message instanceof ImplementationLevelWarning) {
				
				implevelContainer.addChild(new WarningMessageContainer(message));
			}
		
		}
		
	}
	
	private void addNotCodeLevel(WarningMessageContainer rootOfNotmapped)
	{
		for (CodeLevelWarning code: WarningMessageService.getInstance().getNotCodeLevelWarnings()){
			
			rootOfNotmapped.addChild(new WarningMessageContainer(code));
		}
		
	}




}
