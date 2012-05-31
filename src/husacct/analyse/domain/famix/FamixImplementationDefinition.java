package husacct.analyse.domain.famix;

class FamixImplementationDefinition extends FamixAssociation{

	public FamixImplementationDefinition(){
		super.type = "Implements";
	}

	public int index;  
	
	public String toString(){
		String inheritRepesentation = "";
		inheritRepesentation += "\ntype: " + super.type;
		inheritRepesentation += "\nfrom: " + super.from;
		inheritRepesentation += "\nto: " + super.to;
		inheritRepesentation += "\nlineNumber: " + super.lineNumber;

		inheritRepesentation += "\n";
		inheritRepesentation += "\n";
		return inheritRepesentation;
	}
}
