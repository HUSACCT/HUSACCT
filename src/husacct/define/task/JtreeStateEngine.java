package husacct.define.task;

import husacct.define.analyzer.AnalyzedUnitComparator;
import husacct.define.analyzer.AnalyzedUnitRegistry;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
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
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public abstract class JtreeStateEngine {
	private Logger logger;
	private Map<String, Object[]> mapRegistry = new LinkedHashMap<String, Object[]>();
	private StateDefineController stateController = new StateDefineController();
	private AnalyzedUnitComparator analyzerComparator = new AnalyzedUnitComparator();
	private AnalyzedUnitRegistry allUnitsRegistry = new AnalyzedUnitRegistry();

	public JtreeStateEngine() {
		logger = Logger.getLogger(JtreeStateEngine.class);
	}

	public boolean undo() {
		return stateController.undo();

	}

	public boolean redo() {
		return stateController.redo();
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

		AnalyzedModuleComponent analyzeModuleTobeRestored = (AnalyzedModuleComponent) mapRegistry
				.get(unit.getName().toLowerCase())[1];
		
		mapRegistry.remove(unit.getName());
		ArrayList<AnalyzedModuleComponent> data = new ArrayList<AnalyzedModuleComponent>();
		data.add(analyzeModuleTobeRestored);
		StateService.instance().allUnitsRegistry
				.registerAnalyzedUnit(analyzeModuleTobeRestored);
		stateController.insertCommand(new SoftwareUnitRemoveCommand(module,
				data));
	}

	public void addSoftwareUnit(ModuleStrategy module,
			ArrayList<AnalyzedModuleComponent> unitToBeinserted) {

		stateController.insertCommand(new SoftwareUnitAddCommand(module,
				unitToBeinserted));
		for (AnalyzedModuleComponent analyzedModuleComponent : unitToBeinserted) {
			JtreeController.instance().removeTreeItem(analyzedModuleComponent);
			mapRegistry.put(analyzedModuleComponent.getUniqueName()
					.toLowerCase(), new Object[] { module,
					analyzedModuleComponent });
			StateService.instance().allUnitsRegistry
					.removeAnalyzedUnit(analyzedModuleComponent);
			WarningMessageService.getInstance().updateWarnings();
		}
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

	public void registerAnalyzedUnit(AnalyzedModuleComponent unit) {
		allUnitsRegistry.registerAnalyzedUnit(unit);
	}

	public AnalyzedUnitRegistry getAnalzedModuleRegistry() {

		return allUnitsRegistry;

	}

	public void reset() {
		allUnitsRegistry.reset();

	}

	public void fromGui() {
		stateController.unlock();

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
		ArrayList<AnalyzedModuleComponent> units = new ArrayList<AnalyzedModuleComponent>();
	
	Object[] m=	mapRegistry.get(selectedModules.get(0));

		for (String uniqname : selectedModules) {
			AnalyzedModuleComponent softwareUnit = (AnalyzedModuleComponent) mapRegistry
					.get(uniqname)[1];
			units.add(softwareUnit);
		}

	//	stateController.insertCommand(new SoftwareUnitRemoveCommand(m, units));

	}

public AnalyzedModuleComponent getAnalyzedSoftWareUnit(SoftwareUnitDefinition unit)
{

return allUnitsRegistry.getAnalyzedUnit(unit);

}

	public void addModule(ModuleStrategy subModule) {
		stateController.insertCommand(new ModuleAddCommand(subModule));
		
	}

	public ArrayList<AnalyzedModuleComponent>  getmappedUnits() {
    ArrayList<AnalyzedModuleComponent> data = new ArrayList<AnalyzedModuleComponent>();
		for (Object[] obj : mapRegistry.values()) {
			data.add((AnalyzedModuleComponent)obj[1]);
		}
    		
		
		
		return data;
	}

}
