package husacct.analyse.domain.famix;

class FamixFormalParameter extends FamixStructuralEntity {

    public String belongsToMethod;

    public String toString() {
        String importRepresentation = "";
        importRepresentation += "\nname: " + super.name;
        importRepresentation += "\nuniquename: " + super.uniqueName;
        importRepresentation += "\nbelongsToClass: " + super.belongsToClass;
        importRepresentation += "\nbelongsToMethod: " + this.belongsToMethod;
        importRepresentation += "\ndeclareType: " + super.declareType;
        importRepresentation += "\nlineNumber: " + super.lineNumber;
        importRepresentation += "\n";
        importRepresentation += "\n";
        return importRepresentation;
    }
}
