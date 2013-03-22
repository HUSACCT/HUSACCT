package husacct.analyse.domain.famix;

class FamixAttribute extends FamixStructuralEntity {

    public String accessControlQualifier;
    public boolean hasClassScope;

    public String toString() {
        String importRepresentation = "";
        importRepresentation += "\nname: " + super.name;
        importRepresentation += "\nuniquename: " + super.uniqueName;
        importRepresentation += "\nbelongsToClass: " + super.belongsToClass;
        importRepresentation += "\naccessControlQualifier: " + accessControlQualifier;
        importRepresentation += "\nhasClassScope: " + hasClassScope;
        importRepresentation += "\ndeclareType: " + super.declareType;
        importRepresentation += "\nlineNumber: " + super.lineNumber;
        importRepresentation += "\n";
        importRepresentation += "\n";
        return importRepresentation;
    }
}
