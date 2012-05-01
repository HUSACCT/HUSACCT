package husacct.analyse.domain.famix;

class FamixInheritanceDefinition extends FamixAssociation{

	//public String accessControlQualifier;
	public int index;  //index??
	public String type; //implement of extend

	public String toString(){
		String importRepresentation = "";
		importRepresentation += "\ntype: " + type;
		importRepresentation += "\nfrom: " + super.from;
		importRepresentation += "\nto: " + super.to;
		importRepresentation += "\nlineNumber: " + super.lineNumber;

		importRepresentation += "\n";
		importRepresentation += "\n";

		return importRepresentation;
	}
}