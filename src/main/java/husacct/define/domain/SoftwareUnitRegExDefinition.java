package husacct.define.domain;

import husacct.define.domain.softwareunit.SoftwareUnitDefinition;

import java.util.ArrayList;

public class SoftwareUnitRegExDefinition {

	private String name;
	private ArrayList<SoftwareUnitDefinition> softwareUnitDefinitions;

	public SoftwareUnitRegExDefinition(String name) {
		setName(name);
		softwareUnitDefinitions = new ArrayList<>();
	}

	public void addSoftwareUnitDefinition(
			SoftwareUnitDefinition softwareUnitDefinition) {
		softwareUnitDefinitions.add(softwareUnitDefinition);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof SoftwareUnitRegExDefinition) {
			SoftwareUnitRegExDefinition unit = (SoftwareUnitRegExDefinition) o;
			if (unit.name.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public ArrayList<SoftwareUnitDefinition> getSoftwareUnitDefinitions() {
		return softwareUnitDefinitions;
	}

	public void removeSoftwareUnitDefinition(
			SoftwareUnitDefinition softwareUnitDefinition) {
		softwareUnitDefinitions.remove(softwareUnitDefinition);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSoftwareUnitDefinitions(
			ArrayList<SoftwareUnitDefinition> softwareUnitDefinitions) {
		this.softwareUnitDefinitions = softwareUnitDefinitions;
	}

}
