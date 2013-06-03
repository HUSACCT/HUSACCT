package husacct.define.analyzer;

import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.task.JtreeController;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;
import java.util.List;

public class AnalyzedUnitRegistry {
	private ArrayList<AnalyzedModuleComponent> packeges = new ArrayList<AnalyzedModuleComponent>();
	private ArrayList<AnalyzedModuleComponent> classes = new ArrayList<AnalyzedModuleComponent>();
	private ArrayList<AnalyzedModuleComponent> interfaces = new ArrayList<AnalyzedModuleComponent>();
	private ArrayList<AnalyzedModuleComponent> enums = new ArrayList<AnalyzedModuleComponent>();
	private ArrayList<AnalyzedModuleComponent> regex = new ArrayList<AnalyzedModuleComponent>();

	private ArrayList<AnalyzedModuleComponent> mappedPackeges = new ArrayList<AnalyzedModuleComponent>();
	private ArrayList<AnalyzedModuleComponent> mappedclasses = new ArrayList<AnalyzedModuleComponent>();
	private ArrayList<AnalyzedModuleComponent> mappedinterfaces = new ArrayList<AnalyzedModuleComponent>();
	private ArrayList<AnalyzedModuleComponent> mappedEnum = new ArrayList<AnalyzedModuleComponent>();

	public void registerAnalyzedUnit(AnalyzedModuleComponent unit) {
		String type = unit.getType().toLowerCase();

		switch (type) {
		case "class":
			registerEntity(classes, unit);
			break;

		case "package":
			registerEntity(packeges, unit);
			break;
		case "interface":
			registerEntity(interfaces, unit);
			break;
		case "enum":
			registerEntity(enums, unit);
			break;

		default:
			break;
		}

	}

	public void removeAnalyzedUnit(AnalyzedModuleComponent unit) {
		String type = unit.getType().toLowerCase();

		switch (type) {
		case "class":
			removeEnity(classes, unit);
			break;

		case "package":
			removeEnity(packeges, unit);
			break;
		case "interface":
			removeEnity(interfaces, unit);
			break;
		case "enum":
			removeEnity(enums, unit);
			break;

		default:
			break;
		}

	}

	public void registerEntity(List<AnalyzedModuleComponent> list,
			AnalyzedModuleComponent data) {
		JtreeController.instance().restoreTreeItem(data);
		    list.add(data);
	}

	public void removeEnity(List<AnalyzedModuleComponent> list,
			AnalyzedModuleComponent analyzedClass) {
		int index = 0;
		String right = analyzedClass.getUniqueName().toLowerCase();
		for (int i = 0; i < list.size(); i++) {
			String left = list.get(i).getUniqueName().toLowerCase();
			if (left.equals(right)) {
				index = i;
              JtreeController.instance().removeTreeItem(analyzedClass);
				list.remove(index);
				break;
			}

		}

	}

	// used by warnings
	public ArrayList<AnalyzedModuleComponent> getNotAnalyzedUnit(String type) {

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
		AnalyzedModuleComponent resultingSoftwareUnit = null;
		switch (unit.getType()) {
		case CLASS:
			resultingSoftwareUnit = findSoftUnit(unit.getName(), classes);
			break;
		case PACKAGE:
			resultingSoftwareUnit = findSoftUnit(unit.getName(), packeges);
			break;

		case EXTERNALLIBRARY:

			break;

		case INTERFACE:
			resultingSoftwareUnit = findSoftUnit(unit.getName(), interfaces);
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

	public AnalyzedModuleComponent getMappedUnit(SoftwareUnitDefinition unit) {
		AnalyzedModuleComponent resultingSoftwareUnit = null;
		switch (unit.getType()) {
		case CLASS:
			resultingSoftwareUnit = findSoftUnit(unit.getName(), mappedclasses);
			break;
		case PACKAGE:
			resultingSoftwareUnit = findSoftUnit(unit.getName(), mappedPackeges);
			break;

		case EXTERNALLIBRARY:

			break;

		case INTERFACE:
			resultingSoftwareUnit = findSoftUnit(unit.getName(),
					mappedinterfaces);
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
}
