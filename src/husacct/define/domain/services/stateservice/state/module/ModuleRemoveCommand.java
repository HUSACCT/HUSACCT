package husacct.define.domain.services.stateservice.state.module;

import java.util.ArrayList;
import java.util.Collections;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;

import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.DefaultRuleDomainService;
import husacct.define.domain.services.ModuleDomainService;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.domain.services.stateservice.interfaces.Istate;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;
import husacct.define.task.AppliedRuleController;
import husacct.define.task.DefinitionController;
import husacct.define.task.JtreeController;
import husacct.define.task.SoftwareUnitController;
import husacct.define.task.components.AnalyzedModuleComponent;

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
		
		ModuleDomainService service = new ModuleDomainService();
		ModuleStrategy  module = (ModuleStrategy)data.get(0)[0];
		DefinitionController.getInstance().removeModuleById(module.getId());
	}

}
