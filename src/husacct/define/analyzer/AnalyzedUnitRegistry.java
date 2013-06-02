package husacct.define.analyzer;

import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;
import java.util.List;

public class AnalyzedUnitRegistry {
	private ArrayList<AnalyzedModuleComponent> packeges = new ArrayList<AnalyzedModuleComponent>();
	private ArrayList<AnalyzedModuleComponent> classes = new ArrayList<AnalyzedModuleComponent>();
	private ArrayList<AnalyzedModuleComponent> interfaces = new ArrayList<AnalyzedModuleComponent>();
	private ArrayList<AnalyzedModuleComponent> enums = new ArrayList<AnalyzedModuleComponent>();

	public void registerAnalyzedUnit(AnalyzedModuleComponent unit) {
		String type = unit.getType().toLowerCase();

		switch (type) {
		case "class":
			registerClass(unit);
			break;

		case "package":
			registerPackage(unit);
			break;
		case "interface":
			registerInterface(unit);
			break;
		case "enum":
			registerEnum(unit);
			break;

		default:
			break;
		}

	}

	public void removeAnalyzedUnit(AnalyzedModuleComponent unit) {
		String type = unit.getType().toLowerCase();

		switch (type) {
		case "class":
			removeClass(unit);
			break;

		case "package":
			removePackage(unit);
			break;
		case "interface":
			removeInterface(unit);
			break;
		case "enum":
			removeEnum(unit);
			break;

		default:
			break;
		}

	}

	public ArrayList<AnalyzedModuleComponent> getAnalyzedUnit(String type) {

		ArrayList<AnalyzedModuleComponent> result = new ArrayList<AnalyzedModuleComponent>();
		switch (type) {
		case "class":
			result = classes;
			break;

		case "package":
			result = packeges;
			break;
		case "interface":
			result = interfaces;
			break;
		case "enum":
			result = enums;
			break;

		default:
			break;
		}
		return result;
	}

	public void registerClass(AnalyzedModuleComponent analyzedClass) {
		classes.add(analyzedClass);

	}

	public void registerPackage(AnalyzedModuleComponent analyzedPackage) {

		packeges.add(analyzedPackage);

	}

	public void registerInterface(AnalyzedModuleComponent analyzedInterfaces) {
		interfaces.add(analyzedInterfaces);

	}

	public void registerEnum(AnalyzedModuleComponent analyzedenum) {
		enums.add(analyzedenum);

	}

	public void removeClass(AnalyzedModuleComponent analyzedClass) {
		int index = 0;
		String right = analyzedClass.getUniqueName().toLowerCase();
		for (int i = 0; i < classes.size(); i++) {
			String left = classes.get(i).getUniqueName().toLowerCase();
			if (left.equals(right)) {
				index = i;
				classes.remove(index);
				break;
			}

		}

	}

	public void removePackage(AnalyzedModuleComponent analyzedPackage) {
		int index = 0;
		String right = analyzedPackage.getUniqueName().toLowerCase();
		for (int i = 0; i < packeges.size(); i++) {
			String left = packeges.get(i).getUniqueName().toLowerCase();
			if (left.equals(right)) {
				index = i;
				packeges.remove(index);
				break;
			}

		}

	}

	public void removeInterface(AnalyzedModuleComponent analyzedInterfaces) {

		int index = -1;
		String right = analyzedInterfaces.getUniqueName().toLowerCase();
		for (int i = 0; i < interfaces.size(); i++) {
			String left = interfaces.get(i).getUniqueName().toLowerCase();
			if (left.equals(right)) {
				index = i;
				interfaces.remove(index);
				break;
			}

		}

	}

	public void removeEnum(AnalyzedModuleComponent analyzedenum) {

		int index = 0;
		String right = analyzedenum.getUniqueName().toLowerCase();
		for (int i = 0; i < enums.size(); i++) {
			String left = enums.get(i).getUniqueName().toLowerCase();
			if (left.equals(right)) {
				index = i;
				enums.remove(index);
				break;
			}

		}

	}

	public int getUnitsCount() {

		return classes.size() + packeges.size() + enums.size()
				+ interfaces.size();

	}

	public void reset() {
		classes = new ArrayList<AnalyzedModuleComponent>();
		packeges = new ArrayList<AnalyzedModuleComponent>();
		interfaces = new ArrayList<AnalyzedModuleComponent>();
		enums = new ArrayList<AnalyzedModuleComponent>();

	}

	public AnalyzedModuleComponent getAnalyzedUnit(SoftwareUnitDefinition unit) {
   AnalyzedModuleComponent resultingSoftwareUnit=null;
		switch (unit.getType()) {
		case CLASS:
		resultingSoftwareUnit=	findSoftUnit(unit.getName(), classes);
			break;
		case PACKAGE:
			resultingSoftwareUnit=	findSoftUnit(unit.getName(), packeges);
			break;

		case EXTERNALLIBRARY:
		
			break;

		case INTERFACE:
			resultingSoftwareUnit=	findSoftUnit(unit.getName(), interfaces);
			break;

		case REGEX:

			break;

		case SUBSYSTEM:

			break;
		
		default:
			break;
		}

		return resultingSoftwareUnit;
	}
	
	
public AnalyzedModuleComponent findSoftUnit(String uniqname,List<AnalyzedModuleComponent> list)
{
	for (AnalyzedModuleComponent unit : list) {
		if (uniqname.toLowerCase().equals(unit.getUniqueName().toLowerCase())) {
			return unit;
		}
	}
return new AnalyzedModuleComponent();

}
}
