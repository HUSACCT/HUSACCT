package husacct.common.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
		this.subModules = new ArrayList<>();
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
		result = result && (Objects.equals(this.uniqueName, other.uniqueName));
		result = result && (Objects.equals(this.name, other.name));
		result = result && (Objects.equals(this.type, other.type));
		result = result && (Objects.equals(this.visibility, other.visibility));
		return result;
	}
	
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
