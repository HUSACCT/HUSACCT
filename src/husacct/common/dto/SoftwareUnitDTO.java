package husacct.common.dto;

import java.util.ArrayList;
import java.util.List;

//Owner: Analyse

public class SoftwareUnitDTO extends AbstractDTO{
	
	public String uniqueName;
	public String name;
	public String type;
	public String visibility;
	public List<SoftwareUnitDTO> subModules;
	
	public SoftwareUnitDTO(String uniqueName, String name, String type, String visibility){
		this.uniqueName = uniqueName;
		this.name = name;
		this.type = type;
		this.visibility = visibility;
		this.subModules = new ArrayList<SoftwareUnitDTO>();
	}
	
	public SoftwareUnitDTO(String uniqueName, String name, String type, String visibility, List<SoftwareUnitDTO> subModules){
		this.uniqueName = uniqueName;
		this.name = name;
		this.type = type;
		this.visibility = visibility;
		this.subModules = subModules;
	}
	
	public boolean equals(SoftwareUnitDTO other){
		boolean result = true;
		result = result && (this.uniqueName == other.uniqueName);
		result = result && (this.name == other.name);
		result = result && (this.type == other.type);
		result = result && (this.visibility == other.visibility);
		return result;
	}
	
	@Override
	public String toString(){
		String result = "";
		result += "Type: " + type + "\n";
		result += "Uniqename: " + uniqueName + "\n";
		result += "name: " + name + "\n";
		result += "Visibility: " + visibility + "\n";
		result += "Submodules:\n";
		for(SoftwareUnitDTO module: subModules){
			result += module.toString();
		}
		result += "\n";
		return result;
	}
}
