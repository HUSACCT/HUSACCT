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
	//From Class
	public String getSourceFilePath() {
		return sourceFilePath;
	}

	public void setSourceFilePath(String sourceFilePath) {
		this.sourceFilePath = sourceFilePath;
	}

	public int getLinesOfCode() {
		return linesOfCode;
	}

	public void setLinesOfCode(int linesOfCode) {
		this.linesOfCode = linesOfCode;
	}

	public boolean isInterface() {
		return isInterface;
	}

	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}

	public boolean isInnerClass() {
		return isInnerClass;
	}

	public void setInnerClass(boolean isInnerClass) {
		this.isInnerClass = isInnerClass;
	}

	public boolean isEnumeration() {
		return isEnumeration;
	}

	public void setEnumeration(boolean isEnumeration) {
		this.isEnumeration = isEnumeration;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public boolean isHasInnerClasses() {
		return hasInnerClasses;
	}

	public void setHasInnerClasses(boolean hasInnerClasses) {
		this.hasInnerClasses = hasInnerClasses;
	}

	public String getBelongsToClass() {
		return belongsToClass;
	}

	public void setBelongsToClass(String belongsToClass) {
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
