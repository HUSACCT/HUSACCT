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
	//From Library
	public boolean isPackage() {
		return isPackage;
	}

	public void setPackage(boolean isPackage) {
		this.isPackage = isPackage;
	}

	public String getPhysicalPath() {
		return physicalPath;
	}

	public void setPhysicalPath(String physicalPath) {
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
