package husacct.define.task;

import husacct.define.analyzer.AnalyzedUnitComparator;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.UndoRedoService;
import husacct.define.domain.services.WarningMessageService;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.domain.services.stateservice.state.StateDefineController;
import husacct.define.domain.services.stateservice.state.appliedrule.AppliedRuleAddCommand;
import husacct.define.domain.services.stateservice.state.appliedrule.ExceptionAddRuleCommand;
import husacct.define.domain.services.stateservice.state.appliedrule.RemoveAppliedRuleCommand;
import husacct.define.domain.services.stateservice.state.module.LayerDownCommand;
import husacct.define.domain.services.stateservice.state.module.LayerUpCommand;
import husacct.define.domain.services.stateservice.state.module.ModuleAddCommand;
import husacct.define.domain.services.stateservice.state.module.ModuleRemoveCommand;
import husacct.define.domain.services.stateservice.state.module.UpdateModuleCommand;
import husacct.define.domain.services.stateservice.state.module.UpdateModuleTypeCommand;
import husacct.define.domain.services.stateservice.state.softwareunit.SoftwareUnitAddCommand;
import husacct.define.domain.services.stateservice.state.softwareunit.SoftwareUnitRemoveCommand;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;
import husacct.define.domain.warningmessages.WarningMessageContainer;
import husacct.define.presentation.registry.AnalyzedUnitRegistry;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public abstract class JtreeStateEngine {
	private Logger logger;
	private StateDefineController stateController = new StateDefineController();
	private AnalyzedUnitComparator analyzerComparator = new AnalyzedUnitComparator();
	private AnalyzedUnitRegistry allUnitsRegistry = new AnalyzedUnitRegistry();

	public JtreeStateEngine() {
		logger = Logger.getLogger(JtreeStateEngine.class);
		UndoRedoService.getInstance().registerObserver(SoftwareArchitecture.getInstance());
	}

	public boolean undo() {
		boolean res =stateController.undo();
		DefinitionController.getInstance().notifyObservers();
		
				
		return res;
		

	}

	public boolean redo() {
		boolean res =stateController.redo();;
		DefinitionController.getInstance().notifyObservers();
		return res;
	}

	

	public void removeModule(ArrayList<Object[]> data) {
		stateController.insertCommand(new ModuleRemoveCommand(data));
	}

	public void addUpdateModule(long moduleId, String[] moduleold,
			String[] modulenew) {
		stateController.insertCommand(new UpdateModuleCommand(moduleId,
				modulenew, moduleold));

	}

	public void addUpdateModule(ModuleStrategy module,
			ModuleStrategy updatedModule) {
		stateController.insertCommand(new UpdateModuleTypeCommand(module,
				updatedModule));

	}

	public void layerUp(long moduleId) {
		stateController.insertCommand(new LayerUpCommand(moduleId));
		DefinitionController.getInstance().notifyObservers();
	}

	public void layerDown(long moduleId) {
		stateController.insertCommand(new LayerDownCommand(moduleId));
		DefinitionController.getInstance().notifyObservers();
	}

	

	public void removeSoftwareUnit(ModuleStrategy module,
			SoftwareUnitDefinition unit) {

		AnalyzedModuleComponent analyzeModuleTobeRestored =  allUnitsRegistry.getAnalyzedUnit(unit);
				
		
		
		ArrayList<AnalyzedModuleComponent> data = new ArrayList<AnalyzedModuleComponent>();
		data.add(analyzeModuleTobeRestored);
		StateService.instance().allUnitsRegistry
				.registerAnalyzedUnit(analyzeModuleTobeRestored);
		
	}

	public void addSoftwareUnit(ModuleStrategy module,
			ArrayList<AnalyzedModuleComponent> unitToBeinserted) {

		stateController.insertCommand(new SoftwareUnitAddCommand(module,
				unitToBeinserted));
		for (AnalyzedModuleComponent analyzedModuleComponent : unitToBeinserted) {
			JtreeController.instance().removeTreeItem(analyzedModuleComponent);
		
			WarningMessageService.getInstance().updateWarnings();
		}
	}



	public AnalyzedModuleComponent getRootModel() {

		return analyzerComparator.getRootModel();
	}

	public void analyze() {
		getRootModel();
		DefinitionController.getInstance().notifyAnalyzedObservers();
	}

	public void registerAnalyzedUnit(AnalyzedModuleComponent unit) {
		allUnitsRegistry.registerAnalyzedUnit(unit);
	}

	public AnalyzedUnitRegistry getAnalzedModuleRegistry() {

		return allUnitsRegistry;

	}

	public void reset() {
		allUnitsRegistry.reset();

	}



	public boolean[] getRedoAndUndoStates() {
		return stateController.getStatesStatus();

	}

	public void removeRules(ArrayList<AppliedRuleStrategy> selectedRules) {
		stateController.insertCommand(new RemoveAppliedRuleCommand(
				selectedRules));

	}

	public void addRules(ArrayList<AppliedRuleStrategy> rules) {
		stateController.insertCommand(new AppliedRuleAddCommand(rules));
	}

	public void addExceptionRule(AppliedRuleStrategy parent,
			ArrayList<AppliedRuleStrategy> rules) {
		stateController
				.insertCommand(new ExceptionAddRuleCommand(parent, rules));
	}

	public void removeSoftwareUnit(List<String> selectedModules) {
		
	stateController.insertCommand(new SoftwareUnitRemoveCommand(DefinitionController.getInstance().getSelectedModuleId(), selectedModules));




	}

public AnalyzedModuleComponent getAnalyzedSoftWareUnit(SoftwareUnitDefinition unit)
{

return allUnitsRegistry.getAnalyzedUnit(unit);

}

	public void addModule(ModuleStrategy subModule) {
		stateController.insertCommand(new ModuleAddCommand(subModule));
		
	}

	public ModuleStrategy getModulebySoftwareUnitUniqName(String uniqueName) {
		
		return SoftwareArchitecture.getInstance().getModuleBySoftwareUnit(uniqueName);
	}

	public WarningMessageContainer getNotMappedUnits() {
	
return	allUnitsRegistry.getNotMappedUnits();
	
	}

	public AnalyzedModuleComponent getAnalyzedSoftWareUnit(String uniqueName) {
		return  allUnitsRegistry.getAnalyzedUnit(uniqueName);
		
	}

	public ArrayList<AnalyzedModuleComponent> getAnalyzedSoftWareUnit(
			List<String> data) {
		return allUnitsRegistry.getAnalyzedUnit(data);
		
	}

	public void removeRules(List<Long> selectedRules) {
	
		
	}




}
