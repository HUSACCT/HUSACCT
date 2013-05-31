package husacct.define.domain.services.stateservice.state;

import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.stateservice.interfaces.Istate;
import husacct.define.task.DefinitionController;
import husacct.define.task.SoftwareUnitController;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;

import antlr.collections.List;

public class SoftwareUnitAddCommand implements Istate {
	private ModuleStrategy module;
	private ArrayList<AnalyzedModuleComponent> units;
	
	
	public SoftwareUnitAddCommand(ModuleStrategy module,
			ArrayList<AnalyzedModuleComponent> unitTobeRemoved) {
		this.module=module;
		this.units=unitTobeRemoved;
	}

	@Override
	public void undo() {
	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> types = new ArrayList<String>();
	for (AnalyzedModuleComponent unit : units) {
		
	}
	
		
	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub
		
	}

}
