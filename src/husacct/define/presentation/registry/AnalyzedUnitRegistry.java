package husacct.define.presentation.registry;

import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.seperatedinterfaces.ISofwareUnitSeperatedInterface;
import husacct.define.domain.services.UndoRedoService;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;
import husacct.define.domain.warningmessages.CustomWarningMessage;
import husacct.define.domain.warningmessages.NotmappedWarningMessage;
import husacct.define.domain.warningmessages.WarningMessageContainer;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnalyzedUnitRegistry implements ISofwareUnitSeperatedInterface{

	private Map<String,AnalyzedModuleComponent> allAnalyzedUnits = new LinkedHashMap<String,AnalyzedModuleComponent>();
	private Map<Long,AbstractDefineComponent> allDefinedUnits = new LinkedHashMap<Long,AbstractDefineComponent>();
    
	

	public AnalyzedUnitRegistry()
	{
		UndoRedoService.getInstance().registerObserver(this);
		
	}
	
	public void registerAnalyzedUnit(AnalyzedModuleComponent unit) {
		allAnalyzedUnits.put(unit.getUniqueName(),unit);

	}








	public int getUnitsCount() {

		return 0;

	}

	public void reset() {
		

	}

	public AnalyzedModuleComponent getAnalyzedUnit(SoftwareUnitDefinition unit) {
	return allAnalyzedUnits.get(unit.getName());
	}
	
	public AbstractDefineComponent getDefinedUnit(ModuleStrategy module)
	{
		return allDefinedUnits.get(module.getId());
	}



	public AnalyzedModuleComponent findSoftUnit(String uniqname,
			List<AnalyzedModuleComponent> list) {
		for (AnalyzedModuleComponent unit : list) {
			if (uniqname.toLowerCase().equals(
					unit.getUniqueName().toLowerCase())) {
				return unit;
			}
		}
		return new AnalyzedModuleComponent();

	}


	@Override
	public void addSeperatedSoftwareUnit(List<SoftwareUnitDefinition> units,
			long moduleID) {
	for (SoftwareUnitDefinition softwareUnitDefinition : units) {
            getAnalyzedUnit(softwareUnitDefinition);
	}
		
	}

	@Override
	public void removeSeperatedSoftwareUnit(List<SoftwareUnitDefinition> units,
			long moduleId) {
		
		
	}

	public WarningMessageContainer getNotMappedUnits() {
		WarningMessageContainer classesroot = new WarningMessageContainer(new CustomWarningMessage("Class"));
		WarningMessageContainer packagesroot = new WarningMessageContainer(new CustomWarningMessage("Package"));
		WarningMessageContainer interfaceroot = new WarningMessageContainer(new CustomWarningMessage("Interface"));
		WarningMessageContainer enumroot = new WarningMessageContainer(new CustomWarningMessage("Enum"));
		
		for (AnalyzedModuleComponent  unit :  allAnalyzedUnits.values()) {
			if(!unit.isMapped()&& !unit.isAncestorsMapped()){
			String type = unit.getType().toLowerCase();

			switch (type) {
			case "class":
				classesroot.addChild(new WarningMessageContainer( new NotmappedWarningMessage(unit)));
				break;

			case "package":
				packagesroot.addChild(new WarningMessageContainer( new NotmappedWarningMessage(unit)));
				break;
			case "interface":
				interfaceroot.addChild(new WarningMessageContainer( new NotmappedWarningMessage(unit)));
				break;
			case "enum":
				enumroot.addChild(new WarningMessageContainer( new NotmappedWarningMessage(unit)));
				break;

			default:
				break;
			}
			
			}
		}
		WarningMessageContainer notMapped = new WarningMessageContainer(new CustomWarningMessage("NotMapped("+StateService.instance().getAnalzedModuleRegistry().getUnitsCount()+")"));
		notMapped.addChild(packagesroot);
		notMapped.addChild(classesroot);
		notMapped.addChild(interfaceroot);
		notMapped.addChild(enumroot);
		
		
		return notMapped;
	}
}
