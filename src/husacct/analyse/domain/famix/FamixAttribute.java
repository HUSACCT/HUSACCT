package husacct.analyse.domain.famix;

class FamixAttribute extends FamixStructuralEntity {

    public String accessControlQualifier;
    public boolean hasClassScope;
    public boolean isFinal = false;

    public String toString() {
        String importRepresentation = "";
        importRepresentation += "\nname: " + super.name;
        importRepresentation += "\nuniquename: " + super.uniqueName;
        importRepresentation += "\nbelongsToClass: " + super.belongsToClass;
        importRepresentation += "\naccessControlQualifier: " + accessControlQualifier;
        importRepresentation += "\nhasClassScope: " + hasClassScope + ", isFinal: " + isFinal;
        importRepresentation += "\ndeclareType: " + super.declareType;
        importRepresentation += "\nlineNumber: " + super.lineNumber;
        importRepresentation += "\n";
        importRepresentation += "\n";
        return importRepresentation;
    }
}
