package husacct.common.dto;

import java.util.ArrayList;
import java.util.List;

public class AnalysedModuleDTO extends AbstractDTO{
	
	public String uniqueName;
	public String name;
	public String type;
	public String visibility;
	public List<AnalysedModuleDTO> subModules;
	
	public AnalysedModuleDTO(String uniqueName, String name, String type, String visibility){
		this.uniqueName = uniqueName;
		this.name = name;
		this.type = type;
		this.visibility = visibility;
		this.subModules = new ArrayList<AnalysedModuleDTO>();
	}
	
	public AnalysedModuleDTO(String uniqueName, String name, String type, String visibility, List<AnalysedModuleDTO> subModules){
		this.uniqueName = uniqueName;
		this.name = name;
		this.type = type;
		this.visibility = visibility;
		this.subModules = subModules;
	}
	
	public boolean equals(AnalysedModuleDTO other){
		boolean result = true;
		result = result && (this.uniqueName == other.uniqueName);
		result = result && (this.name == other.name);
		result = result && (this.type == other.type);
		result = result && (this.visibility == other.visibility);
		return result;
	}
	
	public String toString(){
		String result = "";
		result += "Type: " + type + "\n";
		result += "Uniqename: " + uniqueName + "\n";
		result += "name: " + name + "\n";
		result += "Visibility: " + visibility + "\n\n";
		result += "Submodules:\n";
		for(AnalysedModuleDTO module: subModules){
			result += module.toString();
		}
		return result;
	}
}
