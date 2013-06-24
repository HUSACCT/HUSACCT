package husacct.define.domain.services.stateservice.state.module;

import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.DefaultRuleDomainService;
import husacct.define.domain.services.ModuleDomainService;
import husacct.define.domain.services.UndoRedoService;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.domain.services.stateservice.interfaces.Istate;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;
import husacct.define.task.AppliedRuleController;
import husacct.define.task.DefinitionController;
import husacct.define.task.SoftwareUnitController;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;

public class ModuleRemoveCommand implements Istate {

	
	private ArrayList<Object[]> data;
	
	public ModuleRemoveCommand(ArrayList<Object[]> data) {
		this.data=data;
	}

	@Override
	public void undo() {
		
	for (Object[] info : data) {
		ModuleStrategy module =(ModuleStrategy) info[0];
		ArrayList<AppliedRuleStrategy> rules= (ArrayList<AppliedRuleStrategy>)info[1];
		UndoRedoService.getInstance().addSeperatedModule(module);
	
		
		for (AppliedRuleStrategy appliedRuleStrategy : rules) {
			
			boolean chek= DefaultRuleDomainService.getInstance().isMandatoryRule(appliedRuleStrategy);
			if(!chek)
			{
				rules.add(appliedRuleStrategy);
				
			
			}
			
		}
		
		UndoRedoService.getInstance().addSeperatedAppliedRule(rules);
		ArrayList<AnalyzedModuleComponent> units = new ArrayList<AnalyzedModuleComponent>();
		for (SoftwareUnitDefinition unit : module.getUnits()) {
		
			units.add(StateService.instance().getAnalyzedSoftWareUnit(unit));
			
			
		}
		SoftwareUnitController controller = new SoftwareUnitController(module.getId());
		controller = new SoftwareUnitController(module.getId());
		controller.save(units);
		
	}	
	

	}

	@Override
	public void redo() {
		

		
		ModuleStrategy  module = (ModuleStrategy)data.get(0)[0];
	
		UndoRedoService.getInstance().removeSeperatedModule(module);
		
	}

}
