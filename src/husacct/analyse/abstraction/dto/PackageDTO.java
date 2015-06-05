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
    
	public PackageDTO() {
	}
	
    public PackageDTO(String name, String uniqueName, String visibility, boolean external, String belongsToPackage){
		this.name = name;
		this.uniqueName = uniqueName;
		this.visibility = visibility;
		this.external = external;
		this.belongsToPackage = belongsToPackage;
	}

	@Override
	public String toString() {
		return "PackageDTO [name=" + name + ", uniqueName=" + uniqueName
				+ ", visibility=" + visibility + ", external=" + external
				+ ", belongsToPackage=" + belongsToPackage + "]";
	}
}
