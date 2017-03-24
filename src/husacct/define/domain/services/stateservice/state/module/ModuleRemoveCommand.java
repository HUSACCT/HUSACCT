package husacct.define.domain.services.stateservice.state.module;

import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.UndoRedoService;
import husacct.define.domain.services.stateservice.interfaces.Istate;
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
	
		/*
		for (AppliedRuleStrategy appliedRuleStrategy : rules) {
			
			boolean chek= DefaultRuleDomainService.getInstance().isMandatoryRule(appliedRuleStrategy);
			if(!chek)
			{
				rules.add(appliedRuleStrategy);
				
			
			}
			
		}
		*/
		//UndoRedoService.getInstance().addSeperatedAppliedRule(rules);
		
		
	
		UndoRedoService.getInstance().addSeperatedSoftwareUnit(module.getUnits(), module.getId());
		
		
	}	
	

	}

	@Override
	public void redo() {
		

		
		ModuleStrategy  module = (ModuleStrategy)data.get(0)[0];
	
		UndoRedoService.getInstance().removeSeperatedModule(module);
		
	}

}
