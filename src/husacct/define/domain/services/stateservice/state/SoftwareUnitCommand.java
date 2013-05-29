package husacct.define.domain.services.stateservice.state;

import husacct.define.domain.module.ToBeImplemented.ModuleStrategy;
import husacct.define.domain.services.stateservice.interfaces.Istate;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;

public class SoftwareUnitCommand implements Istate {

	public SoftwareUnitCommand(ModuleStrategy module,
			ArrayList<AnalyzedModuleComponent> unitTobeRemoved) {
		
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub
		
	}

}
