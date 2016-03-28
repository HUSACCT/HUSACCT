package husacct.common.dto;


//Owner: Analyse

public class DependencyDTO extends AbstractDTO{
	//from FamixAssociation
	public String from = "";	//unique name of the from-class 
	public String to = ""; //unique name of the to-class 
	public String type = "";
    public String subType = "";
	public int lineNumber = 0;
	public boolean isIndirect = false;
	// From FamixInvocation
	public String usedEntity = ""; // uniqueName of used FamixStructuralEntity, FamixBehaviouralEntity, or "" (not found)
	public String belongsToMethod = "";	// Unique name of the method of the from-class that contains the association-causing statement.
	public String statement = ""; // Part of originalToString that causes association
	// Derived attributes
	public boolean isInheritanceRelated = false; // True, if the invoked method or accessed variable is inherited. Furthermore if type starts with extends. 
	public boolean isInnerClassRelated = false; // True, if the from-class or to-class is an inner class
	
	public DependencyDTO() {
		
	}
	
	public DependencyDTO(String from, String to, String type, String subType, int lineNumber, boolean indirect, boolean inheritanceRelated){
		this.from = from;
		this.to = to;
		this.type = type;
		this.subType = subType;
		this.lineNumber = lineNumber;
		this.isIndirect = indirect;
		this.isInheritanceRelated = inheritanceRelated;
	}
	
	public boolean equals(DependencyDTO other){
		boolean result = true;
		result = result && (this.from == other.from);
		result = result && (this.to == other.to);
		result = result && (this.lineNumber == other.lineNumber);
		result = result && (this.type == other.type);
		result = result && (this.subType == other.subType);
		result = result && (this.isIndirect == other.isIndirect);
		return result;
	}
	
	public String getFrom() {
		return from;
	}
	
	public String toString(){
		String result = "";
		result += "\nFrom: " + from;
		result += "\nTo: " + to;
		result += "\nType: " + type + ", SubType: " + subType;
		result += "\nLine: " + lineNumber + ", statement: " + statement + ", belongsToMethod: " + belongsToMethod;
		result += "\nusedEntity: " + usedEntity;
		result += "\nIndirect : " + isIndirect + ", isInheritanceRelated: " + isInheritanceRelated + ", isInnerClassRelated: " + isInnerClassRelated;
		result += "\n";
		return result;
	}
}
