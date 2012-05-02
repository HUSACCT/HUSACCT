package husacct.analyse.domain.famix;

class FamixInheritanceDefinition extends FamixAssociation{
	
	public FamixInheritanceDefinition(String type){
		super.type = type;
	}
	//public String accessControlQualifier;
	public int index;  //index??

	public String toString(){
		String inheritRepesentation = "";
		inheritRepesentation += "\ntype: " + type;
		inheritRepesentation += "\nfrom: " + super.from;
		inheritRepesentation += "\nto: " + super.to;
		inheritRepesentation += "\nlineNumber: " + super.lineNumber;

		inheritRepesentation += "\n";
		inheritRepesentation += "\n";

		return inheritRepesentation;
	}
}