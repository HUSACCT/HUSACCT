package husacct.analyse.abstraction.dto;

import husacct.common.dto.AbstractDTO;

public class PackageDTO extends AbstractDTO {
	// From Entity
    public String name = "";
    public String uniqueName = "";
    public String visibility = "public";
	//From DecompositionEntity
    public boolean external = false;
    public String belongsToPackage = "";
    //From Package
    //-
    
	public PackageDTO(String name, String uniqueName, String visibility, boolean external, String belongsToPackage){
		this.name = name;
		this.uniqueName = uniqueName;
		this.visibility = visibility;
		this.external = external;
		this.belongsToPackage = belongsToPackage;
	}

    //Getters and Setters
    //From Entity
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUniqueName() {
		return uniqueName;
	}
	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}
	public String getVisibility() {
		return visibility;
	}
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	//From DecompositionEntity
	public boolean isExternal() {
		return external;
	}
	public void setExternal(boolean external) {
		this.external = external;
	}
	public String getBelongsToPackage() {
		return belongsToPackage;
	}
	public void setBelongsToPackage(String belongsToPackage) {
		this.belongsToPackage = belongsToPackage;
	}
	//From Package

	@Override
	public String toString() {
		return "PackageDTO [name=" + name + ", uniqueName=" + uniqueName
				+ ", visibility=" + visibility + ", external=" + external
				+ ", belongsToPackage=" + belongsToPackage + "]";
	}
}
