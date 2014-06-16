package husacct.define.presentation.registry;

import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.seperatedinterfaces.ISofwareUnitSeperatedInterface;
import husacct.define.domain.services.UndoRedoService;
import husacct.define.domain.softwareunit.ExpressionUnitDefinition;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;
import husacct.define.domain.warningmessages.CustomWarningMessage;
import husacct.define.domain.warningmessages.NotmappedWarningMessage;
import husacct.define.domain.warningmessages.WarningMessageContainer;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnalyzedUnitRegistry implements ISofwareUnitSeperatedInterface{

	private Map<String,AnalyzedModuleComponent> allAnalyzedUnits = new LinkedHashMap<String,AnalyzedModuleComponent>();
	private Map<Long,AbstractDefineComponent> allDefinedUnits = new LinkedHashMap<Long,AbstractDefineComponent>();
	private Map<String,String> importedUniqnames  =  new LinkedHashMap<String, String>();

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
		allAnalyzedUnits = new LinkedHashMap<String,AnalyzedModuleComponent>();
		allDefinedUnits = new LinkedHashMap<Long,AbstractDefineComponent>();
		importedUniqnames  =  new LinkedHashMap<String, String>();
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
			
			if (unit.getType().equalsIgnoreCase("regex")) {
			
		
		
		
		}else{
			
			if((!unit.isMapped()&& !unit.isAncestorsMapped())){
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
		}
		WarningMessageContainer notMapped = new WarningMessageContainer(new CustomWarningMessage("NotMapped"));
	
		
		((CustomWarningMessage)packagesroot.getvalue()).setDecription(packagesroot.getchildren().size());
		((CustomWarningMessage)classesroot.getvalue()).setDecription(classesroot.getchildren().size());
		((CustomWarningMessage)interfaceroot.getvalue()).setDecription(interfaceroot.getchildren().size());
		((CustomWarningMessage)enumroot.getvalue()).setDecription(enumroot.getchildren().size());
		notMapped.addChild(packagesroot);
		notMapped.addChild(classesroot);
		notMapped.addChild(interfaceroot);
		notMapped.addChild(enumroot);
	   
	    ((CustomWarningMessage)notMapped.getvalue()).setDecription( notMapped.getAllWarningsCount());
		
	    return notMapped;
	}

	@Override
	public void addExpression(long moduleId, ExpressionUnitDefinition expression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeExpression(long moduleId,
			ExpressionUnitDefinition expression) {
		
		
	}

	@Override
	public void editExpression(long moduleId,
			ExpressionUnitDefinition oldExpresion, ExpressionUnitDefinition newExpression) {
	
		
	}

	public AnalyzedModuleComponent getAnalyzedUnit(String uniqueName) {
		return allAnalyzedUnits.get(uniqueName);
		
	}

	@Override
	public void switchSoftwareUnitLocation(long fromModule, long toModule,
			List<String> uniqNames) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<AnalyzedModuleComponent> getAnalyzedUnit(List<String> data) {
		ArrayList<AnalyzedModuleComponent> units = new ArrayList<AnalyzedModuleComponent>();
		for (String uniqNames : data) {
			units.add(allAnalyzedUnits.get(uniqNames));
		}
		
		return units;
	}

	public void registerImportedUnit(SoftwareUnitDefinition unit) {
		
		
		
			importedUniqnames.put(unit.getName(), unit.getName());
		
		
	}

	public List<String> getimportedUnits() {
		List<String> unignames= new ArrayList<String>();
		for (String name : importedUniqnames.values()) {
			unignames.add(name);
		}
             
		importedUniqnames= new LinkedHashMap<String, String>();
	
         return unignames;
		}
		
	
	
	
}