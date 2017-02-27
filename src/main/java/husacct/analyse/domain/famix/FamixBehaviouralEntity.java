package husacct.analyse.domain.famix;

class FamixBehaviouralEntity extends FamixEntity {

    public String signature;
    public String declaredReturnType;
    public String declareReturnClass;
    public String belongsToClass;

    public String toString() {
        String importRepresentation = "";
        importRepresentation += "\nname: " + super.name;
        importRepresentation += "\nuniquename: " + super.uniqueName;
        importRepresentation += "\nbelongsToClass: " + belongsToClass;
        importRepresentation += "\nvisibility: " + super.visibility;
        importRepresentation += "\nsignature: " + signature;
        importRepresentation += "\ndeclaredReturnType: " + declaredReturnType;

        importRepresentation += "\n";
        importRepresentation += "\n";

        return importRepresentation;
    }

}
