package husacct.analyse.abstraction.dto;

import husacct.common.dto.AbstractDTO;

public class ClassDTO extends AbstractDTO {
	// From Entity
    public String name = "";
    public String uniqueName = "";
    public String visibility = "public";
	//From DecompositionEntity
    public boolean external = false;
    public String belongsToPackage = "";
    //From Class
	public String sourceFilePath = "";
	public int linesOfCode = 0;
    public boolean isInterface = false;
    public boolean isInnerClass = false;
    public boolean isEnumeration = false;
    public boolean isAbstract = false;
    public boolean hasInnerClasses = false;
    public String belongsToClass = null;

    
	public ClassDTO() {
	}
	
	public ClassDTO(String name, String uniqueName, String visibility, boolean external, String belongsToPackage, 
			String sourceFilePath, int linesOfCode, boolean isInterface, boolean isInnerClass, boolean isEnumeration, 
			boolean isAbstract, boolean hasInnerClasses, String belongsToClass){
		this.name = name;
		this.uniqueName = uniqueName;
		this.visibility = visibility;
		this.external = external;
		this.belongsToPackage = belongsToPackage;
		this.sourceFilePath = sourceFilePath;
		this.linesOfCode = linesOfCode;
		this.isInterface = isInterface;
		this.isInnerClass = isInnerClass;
		this.isEnumeration = isEnumeration;
		this.isAbstract = isAbstract;
		this.hasInnerClasses = hasInnerClasses;
		this.belongsToClass = belongsToClass;
	}

	@Override
	public String toString() {
		return "ClassDTO [name=" + name + ", uniqueName=" + uniqueName
				+ ", visibility=" + visibility + ", external=" + external
				+ ", belongsToPackage=" + belongsToPackage
				+ ", sourceFilePath=" + sourceFilePath + ", linesOfCode="
				+ linesOfCode + ", isInterface=" + isInterface
				+ ", isInnerClass=" + isInnerClass + ", isEnumeration="
				+ isEnumeration + ", isAbstract=" + isAbstract
				+ ", hasInnerClasses=" + hasInnerClasses + ", belongsToClass="
				+ belongsToClass + "]";
	}

}
