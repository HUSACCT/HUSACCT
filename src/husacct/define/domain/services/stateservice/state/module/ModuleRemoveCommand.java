package husacct.define.domain.services.stateservice.state.module;

import java.util.ArrayList;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.DefaultRuleDomainService;
import husacct.define.domain.services.stateservice.interfaces.Istate;
import husacct.define.task.AppliedRuleController;
import husacct.define.task.DefinitionController;

public class ModuleRemoveCommand implements Istate {

	
	private ArrayList<Object[]> data;
	
	public ModuleRemoveCommand(ArrayList<Object[]> data) {
		this.data=data;
	}

	@Override
	public void undo() {
		// module,moduleRules
	for (Object[] info : data) {
		ModuleStrategy module =(ModuleStrategy) info[0];
		ArrayList<AppliedRuleStrategy> rules= (ArrayList<AppliedRuleStrategy>)info[1];
		
		DefinitionController.getInstance().passModuleToService(module.getparent().getId(), module);
		AppliedRuleController appliedruleController = new AppliedRuleController(module.getId(), -1);
		for (AppliedRuleStrategy appliedRuleStrategy : rules) {
			
			boolean chek= DefaultRuleDomainService.getInstance().isMandatoryRule(appliedRuleStrategy);
			if(!chek)
			{
				SoftwareArchitecture.getInstance().addAppliedRule(appliedRuleStrategy);
			}
			
		}
		for (SoftwareUnitDefinition unit : module.getUnits()) {
			System.out.println(unit.getName()+"  haaa");
		}
		
	}	
	

	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub

	}

}
