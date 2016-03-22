package husacct.analyse.domain.famix;

class FamixLocalVariable extends FamixStructuralEntity {

    public String belongsToMethod;

    public String toString() {
        String importRepresentation = "";
        importRepresentation += "\nname: " + super.name;
        importRepresentation += "\nuniquename: " + super.uniqueName;
        importRepresentation += "\nbelongsToClass: " + super.belongsToClass;
        importRepresentation += "\nbelongsToMethod: " + belongsToMethod;
        importRepresentation += "\ndeclareType: " + super.declareType;
        importRepresentation += "\n";
        importRepresentation += "\n";
        return importRepresentation;
    }
}
