package husacct.analyse.abstraction.dto;

import husacct.common.dto.AbstractDTO;

public class LibraryDTO extends AbstractDTO {
	// From Entity
    public String name = "";
    public String uniqueName = "";
    public String visibility = "public";
	//From DecompositionEntity
    public boolean external = false;
    public String belongsToPackage = "";
    //From Library
    public boolean isPackage = true;
	public String physicalPath;
    
	public LibraryDTO() {
	}
	
	public LibraryDTO(String name, String uniqueName, String visibility, boolean external, 
			String belongsToPackage, boolean isPackage, String physicalPath){
		this.name = name;
		this.uniqueName = uniqueName;
		this.visibility = visibility;
		this.external = external;
		this.belongsToPackage = belongsToPackage;
		this.isPackage = isPackage;
		this.physicalPath = physicalPath;
	}

	@Override
	public String toString() {
		return "LibraryDTO [name=" + name + ", uniqueName=" + uniqueName
				+ ", visibility=" + visibility + ", external=" + external
				+ ", belongsToPackage=" + belongsToPackage + ", isPackage="
				+ isPackage + ", physicalPath=" + physicalPath + "]";
	}

}
