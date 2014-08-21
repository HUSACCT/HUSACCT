package husacct.analyse.domain.famix;

class FamixBehaviouralEntity extends FamixEntity {

    public String accessControlQualifier;
    public String signature;
    public boolean isPureAccessor;
    public String declaredReturnType;
    public String declareReturnClass;
    public String belongsToClass;

    public String toString() {
        String importRepresentation = "";
        importRepresentation += "\nname: " + super.name;
        importRepresentation += "\nuniquename: " + super.uniqueName;
        importRepresentation += "\nbelongsToClass: " + belongsToClass;

        importRepresentation += "\naccessControlQualifier: " + accessControlQualifier;
        importRepresentation += "\nsignature: " + signature;
        importRepresentation += "\nisPureAccessor: " + isPureAccessor;
        importRepresentation += "\ndeclaredReturnType: " + declaredReturnType;

        importRepresentation += "\n";
        importRepresentation += "\n";

        return importRepresentation;
    }

}
