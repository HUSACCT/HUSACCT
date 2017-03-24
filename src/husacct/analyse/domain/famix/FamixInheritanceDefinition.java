package husacct.analyse.domain.famix;

class FamixInheritanceDefinition extends FamixAssociation {

    public FamixInheritanceDefinition() {
     	super.type = "Inheritance";
        super.isInheritanceRelated = true;
    }
    public int index;

    @Override
	public String toString() {
        String inheritRepesentation = "";
        inheritRepesentation += "\ntype: " + type + ", subType: " + super.subType;
        inheritRepesentation += "\nfrom: " + super.from;
        inheritRepesentation += "\nto: " + super.to;
        inheritRepesentation += "\nlineNumber: " + super.lineNumber;

        inheritRepesentation += "\n";
        inheritRepesentation += "\n";

        return inheritRepesentation;
    }
}