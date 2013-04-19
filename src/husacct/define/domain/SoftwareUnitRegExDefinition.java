package husacct.define.domain;

import java.util.ArrayList;

public class SoftwareUnitRegExDefinition {
	
	private String name;
	private ArrayList<SoftwareUnitDefinition> softwareUnitDefinitions;
	
	public SoftwareUnitRegExDefinition(String name) {
		this.setName(name);
		this.softwareUnitDefinitions = new ArrayList<SoftwareUnitDefinition>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addSoftwareUnitDefinition(SoftwareUnitDefinition softwareUnitDefinition) {
		this.softwareUnitDefinitions.add(softwareUnitDefinition);
	}
	
	public void removeSoftwareUnitDefinition(SoftwareUnitDefinition softwareUnitDefinition) {
		this.softwareUnitDefinitions.remove(softwareUnitDefinition);
	}

	public ArrayList<SoftwareUnitDefinition> getSoftwareUnitDefinitions() {
		return softwareUnitDefinitions;
	}

	public void setSoftwareUnitDefinitions(ArrayList<SoftwareUnitDefinition> softwareUnitDefinitions) {
		this.softwareUnitDefinitions = softwareUnitDefinitions;
	}

}
