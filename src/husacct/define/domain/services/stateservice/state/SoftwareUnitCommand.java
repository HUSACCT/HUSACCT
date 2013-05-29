package husacct.define.domain.services.stateservice.state;

import java.util.ArrayList;

import husacct.define.domain.module.Module;
import husacct.define.domain.services.stateservice.interfaces.Istate;
import husacct.define.task.components.AnalyzedModuleComponent;

public class SoftwareUnitCommand implements Istate {

	public SoftwareUnitCommand(Module module,
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
