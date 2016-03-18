package husacct.analyse.domain.famix;

class FamixAnnotation extends FamixStructuralEntity {

    public String annotatedElement; // Currently: "class", "variable", "method".

    public String toString() {
        String importRepresentation = "";
        importRepresentation += "\nname: " + super.name;
        importRepresentation += "\nuniquename: " + super.uniqueName;
        importRepresentation += "\nbelongsToClass: " + super.belongsToClass;
        importRepresentation += "\nannotatedElement: " + annotatedElement;
        importRepresentation += "\ndeclareType: " + super.declareType;
        importRepresentation += "\n";
        importRepresentation += "\n";
        return importRepresentation;
    }
}
