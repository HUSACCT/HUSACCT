package husacct.define.presentation.registry;

import husacct.define.domain.softwareunit.SoftwareUnitDefinition;
import husacct.define.domain.warningmessages.CustomWarningMessage;
import husacct.define.domain.warningmessages.NotmappedWarningMessage;
import husacct.define.domain.warningmessages.WarningMessageContainer;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnalyzedUnitRegistry {

	private Map<String,AnalyzedModuleComponent> allAnalyzedUnits = new LinkedHashMap<String,AnalyzedModuleComponent>();
	private Map<String,String> importedUniqnames  =  new LinkedHashMap<String, String>();

	public AnalyzedUnitRegistry()
	{
	}
	
	public void registerAnalyzedUnit(AnalyzedModuleComponent unit) {
		allAnalyzedUnits.put(unit.getUniqueName(),unit);
	}

	public int getUnitsCount() {
		return 0;
	}

	public void reset() {
		allAnalyzedUnits = new LinkedHashMap<String,AnalyzedModuleComponent>();
		importedUniqnames  =  new LinkedHashMap<String, String>();
	}

	public AnalyzedModuleComponent getAnalyzedUnit(SoftwareUnitDefinition unit) {
	return allAnalyzedUnits.get(unit.getName());
	}
	
	public AnalyzedModuleComponent findSoftUnit(String uniqname, List<AnalyzedModuleComponent> list) {
		for (AnalyzedModuleComponent unit : list) {
			if (uniqname.equals(unit.getUniqueName())) {
				return unit;
			}
		}
		return new AnalyzedModuleComponent();

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

	public AnalyzedModuleComponent getAnalyzedUnit(String uniqueName) {
		return allAnalyzedUnits.get(uniqueName);
		
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