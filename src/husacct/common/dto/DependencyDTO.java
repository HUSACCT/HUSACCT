package husacct.common.dto;

public class DependencyDTO extends AbstractDTO{
	
	//unique name of the from-class
	public String from;	//class path of the from-class file; in case of inner classes classPathFrom is shorter than from 
	public String fromClassPath; //unique name of the to-class
	public String to; //class path of the to-class file; in case of inner classes classPathFrom is shorter than from 
	public String toClassPath;
	public String type = "";
    public String subType = "";
	public int lineNumber;
	public boolean isIndirect;
	public String usedEntity = ""; // uniqueName of used FamixStructuralEntity, FamixBehaviouralEntity, or “” (not found)
	public boolean isInheritanceRelated = false; // True, if the invoked method or accessed variable is inherited. Furthermore if type starts with extends. 
	public boolean isInnerClassRelated = false; // True, if the from-class or to-class is an inner class
	
	public DependencyDTO(String from, String fromClassPath, String to, String toClassPath, String type, int lineNumber){
		this.from = from;
		this.fromClassPath = fromClassPath;
		this.to = to;
		this.toClassPath = toClassPath;
		this.type = type;
		this.lineNumber = lineNumber;
		this.isIndirect = false;
	}
	
	public DependencyDTO(String from, String fromClassPath, String to, String toClassPath, String type, String subType, int lineNumber, boolean indirect, boolean inheritanceRelated){
		this.from = from;
		this.fromClassPath = fromClassPath;
		this.to = to;
		this.toClassPath = toClassPath;
		this.type = type;
		this.subType = subType;
		this.lineNumber = lineNumber;
		this.isIndirect = indirect;
		this.isInheritanceRelated = inheritanceRelated;
	}
	
	public boolean equals(DependencyDTO other){
		boolean result = true;
		result = result && (this.from == other.from);
		result = result && (this.fromClassPath == other.fromClassPath);
		result = result && (this.to == other.to);
		result = result && (this.toClassPath == other.toClassPath);
		result = result && (this.type == other.type);
		result = result && (this.lineNumber == other.lineNumber);
		result = result && (this.isIndirect == other.isIndirect);
		return result;
	}
	
	public String toString(){
		String result = "";
		result += "\nFrom: " + from + ", ClassPathfrom: " + fromClassPath + ", ";
		result += "\nTo: " + to + ", ClassPathTo: " + toClassPath + ", ";
		result += "\nType: " + type + ", SubType: " + subType + ", ";
		result += "Line: " + lineNumber + ", ";
		result += "Indirect : " + isIndirect + ".";
		result += "\nusedEntity: " + usedEntity;
		result += "\n";
		return result;
	}
}
