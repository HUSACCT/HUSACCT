package husacct.analyse.domain.famix;

class FamixTypeCast extends FamixAssociation {

    public String belongsToBehaviour;
    public String fromType;
    public String toType;
    
    public String toString() {
        String typeCastRepresentation = "";
        typeCastRepresentation += "\ntype: " + type + ", subType: " + super.subType;
        typeCastRepresentation += "\nfrom: " + super.from;
        typeCastRepresentation += "\nto: " + super.to;
        typeCastRepresentation += "\nlineNumber: " + super.lineNumber;

        typeCastRepresentation += "\n";

        return typeCastRepresentation;
    }

}
