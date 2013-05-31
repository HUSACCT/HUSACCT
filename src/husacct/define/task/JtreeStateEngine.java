package husacct.define.task;

import husacct.define.analyzer.AnalyzedUnitComparator;
import husacct.define.analyzer.AnalyzedUnitRegistry;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.WarningMessageService;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.domain.services.stateservice.state.ModuleCommand;
import husacct.define.domain.services.stateservice.state.SoftwareUnitCommand;
import husacct.define.domain.services.stateservice.state.StateDefineController;
import husacct.define.domain.services.stateservice.state.UpdateModuleCommand;
import husacct.define.domain.services.stateservice.state.UpdateModuleTypeCommand;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public abstract class JtreeStateEngine {
	private Logger logger;
	private Map<String, Object[]> mapRegistry = new LinkedHashMap<String, Object[]>();
	private StateDefineController stateController = new StateDefineController();
    private AnalyzedUnitComparator analyzerComparator = new AnalyzedUnitComparator();
    private AnalyzedUnitRegistry allUnitsRegistry = new AnalyzedUnitRegistry();

	public JtreeStateEngine() {
		logger = Logger.getLogger(JtreeStateEngine.class);
	}

	public void removeSoftwareUnit(ModuleStrategy module, SoftwareUnitDefinition unit) {

		AnalyzedModuleComponent analyzeModuleTobeRestored = (AnalyzedModuleComponent) mapRegistry
				.get(unit.getName().toLowerCase())[1];
		mapRegistry.remove(unit.getName());
		ArrayList<AnalyzedModuleComponent> data = new ArrayList<AnalyzedModuleComponent>();
		data.add(analyzeModuleTobeRestored);
		StateService.instance().allUnitsRegistry.registerAnalyzedUnit(analyzeModuleTobeRestored);
		
		
		WarningMessageService.getInstance().updateWarnings();
		JtreeController.instance().restoreTreeItem(analyzeModuleTobeRestored);
		stateController.insertCommand(new SoftwareUnitCommand(module,data));
		

	}



	public void addModule(ModuleStrategy module) {

		stateController.insertCommand(new ModuleCommand(module));
	}

	public void removeModule(ModuleStrategy module) {
		ArrayList<AnalyzedModuleComponent> unitTobeRemoved = new ArrayList<AnalyzedModuleComponent>();
		stateController.insertCommand(new ModuleCommand(module));
		
		for (SoftwareUnitDefinition analyzedmodule : module.getUnits()) {
			String key = analyzedmodule.getName().toLowerCase();
			AnalyzedModuleComponent analyzedModule = (AnalyzedModuleComponent) mapRegistry
					.get(key)[1];
			
			unitTobeRemoved.add(analyzedModule);
			mapRegistry.remove(key);
			StateService.instance().allUnitsRegistry.registerAnalyzedUnit(analyzedModule);
			
			
			WarningMessageService.getInstance().updateWarnings();

		}
		
		stateController.insertCommand(new SoftwareUnitCommand(module,unitTobeRemoved) );
		
	}

	public void addSoftwareUnit(ModuleStrategy module,
			AnalyzedModuleComponent unitToBeinserted) {

		mapRegistry.put(unitToBeinserted.getUniqueName().toLowerCase(),
				new Object[] { module, unitToBeinserted });
		StateService.instance().allUnitsRegistry.removeAnalyzedUnit(unitToBeinserted);
	
		ArrayList<AnalyzedModuleComponent> data = new ArrayList<AnalyzedModuleComponent>();
		data.add(unitToBeinserted);
		stateController.insertCommand(new SoftwareUnitCommand(module,data));
		JtreeController.instance().removeTreeItem(unitToBeinserted);
	}

	public void addSoftwareUnit(ModuleStrategy module,
			ArrayList<AnalyzedModuleComponent> unitToBeinserted) {

		stateController.insertCommand(new SoftwareUnitCommand(module,unitToBeinserted));
		for (AnalyzedModuleComponent analyzedModuleComponent : unitToBeinserted) {
			JtreeController.instance().removeTreeItem(analyzedModuleComponent);
			mapRegistry.put(analyzedModuleComponent.getUniqueName().toLowerCase(),
					new Object[] { module, analyzedModuleComponent });
			StateService.instance().allUnitsRegistry.removeAnalyzedUnit(analyzedModuleComponent);
			WarningMessageService.getInstance().updateWarnings();
		}
	}

	public boolean undo() {
	return 	stateController.undo();

	}

	public boolean redo()  {
	return	stateController.redo();
	}
	

	



	public ModuleStrategy getModulebySoftwareUnitUniqName(String Uniqname) {

		Object[] result = mapRegistry.get(Uniqname.toLowerCase());
		ModuleStrategy resultModule = (ModuleStrategy) result[0];

		return resultModule;

	}

	public AnalyzedModuleComponent getRootModel() {

		return analyzerComparator.getRootModel();
	}

	public void analyze() {
   getRootModel();
	
	}


	
	
	public void registerAnalyzedUnit(AnalyzedModuleComponent unit)
	{
		allUnitsRegistry.registerAnalyzedUnit(unit);
	}

	public AnalyzedUnitRegistry getAnalzedModuleRegistry()
	{
		
		return allUnitsRegistry;
		
	}

	public void reset() {
		allUnitsRegistry.reset();
		
	}
	
	public ArrayList<AnalyzedModuleComponent> getMappedUnits()
	{
	ArrayList<AnalyzedModuleComponent> analyzedUnits = new ArrayList<AnalyzedModuleComponent>();	
	Collection<Object[]> units=	mapRegistry.values();
	for (Object[] unit : units) {
		analyzedUnits.add( (AnalyzedModuleComponent)unit[1]);
	}
	
	return analyzedUnits;
	
		
		
		
		
	}

	public void fromGui() {
		stateController.unlock();
		
	}
	public boolean[] getRedoAndUndoStates()
	{
		return stateController.getStatesStatus();
		
	}

	public void addUpdateModule(long moduleId, String[] moduleold,
			String[] modulenew) {
	stateController.insertCommand(new UpdateModuleCommand(moduleId, modulenew,moduleold ));
		
	}

	public void addUpdateModule(ModuleStrategy module,
			ModuleStrategy updatedModule) {
		stateController.insertCommand(new UpdateModuleTypeCommand(module, updatedModule));
		
	}

	public void removeRules(ArrayList<AppliedRuleStrategy> selectedRules) {
		stateController.insertCommand(new RemoveAppliedRuleCommand(selectedRules));
		
	}
	
}
