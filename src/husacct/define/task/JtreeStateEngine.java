package husacct.define.task;

import husacct.define.analyzer.AnalyzedUnitComparator;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.WarningMessageService;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;
import husacct.define.domain.warningmessages.WarningMessageContainer;
import husacct.define.presentation.registry.AnalyzedUnitRegistry;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;
import java.util.List;

public class JtreeStateEngine {
	private AnalyzedUnitComparator analyzerComparator = new AnalyzedUnitComparator();
	private AnalyzedUnitRegistry allUnitsRegistry = new AnalyzedUnitRegistry();

	public JtreeStateEngine() {
	}

	public void removeSoftwareUnit(ModuleStrategy module, SoftwareUnitDefinition unit) {
		AnalyzedModuleComponent analyzeModuleTobeRestored =  allUnitsRegistry.getAnalyzedUnit(unit);
		//analyzeModuleTobeRestored.detach();
		ArrayList<AnalyzedModuleComponent> data = new ArrayList<AnalyzedModuleComponent>();
		if(analyzeModuleTobeRestored !=null){
			data.add(analyzeModuleTobeRestored);
			StateService.instance().allUnitsRegistry.registerAnalyzedUnit(analyzeModuleTobeRestored);
		}
	}

	public void addSoftwareUnit(ModuleStrategy module, ArrayList<AnalyzedModuleComponent> unitToBeinserted) {
		for (AnalyzedModuleComponent analyzedModuleComponent : unitToBeinserted) {
			JtreeController.instance().removeTreeItem(analyzedModuleComponent);
			WarningMessageService.getInstance().updateWarnings();
		}
	}

	public void analyze() {
		allUnitsRegistry.reset();
		analyzerComparator.getRootModel();
		DefinitionController.getInstance().notifyAnalyzedObservers();
	}

	public void registerAnalyzedUnit(AnalyzedModuleComponent unit) {
		allUnitsRegistry.registerAnalyzedUnit(unit);
	}

	public AnalyzedUnitRegistry getAnalzedModuleRegistry() {
		return allUnitsRegistry;
	}

	public AnalyzedModuleComponent getAnalyzedSoftWareUnit(SoftwareUnitDefinition unit) {
		return allUnitsRegistry.getAnalyzedUnit(unit);
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

	public void registerImportedUnit(SoftwareUnitDefinition unit) {
		allUnitsRegistry.registerImportedUnit(unit);
	}

	public void registerImportedData() {
       for (String unigNames : allUnitsRegistry.getimportedUnits()) {
		AnalyzedModuleComponent result = allUnitsRegistry.getAnalyzedUnit(unigNames);
			if (result!=null) {
				result.freeze();
			}
		}
	}

}
