package husacct.analyse.domain.famix;

class FamixMethod extends FamixBehaviouralEntity {

    public boolean hasClassScope;
    public boolean isAbstract;
    public boolean isConstructor;

    public String toString() {
        String importRepresentation = "";
        importRepresentation += "\nname: " + super.name;
        importRepresentation += "\nuniquename: " + super.uniqueName;

        importRepresentation += "\nbelongsToClass: " + belongsToClass;
        importRepresentation += "\nhasClassScope: " + hasClassScope;
        importRepresentation += "\nisAbstract: " + isAbstract;
        importRepresentation += "\nisConstructor: " + isConstructor;

        importRepresentation += "\nvisibility: " + super.visibility;
        importRepresentation += "\nsignature: " + signature;
        importRepresentation += "\ndeclaredReturnType: " + declaredReturnType;

        importRepresentation += "\n";
        importRepresentation += "\n";

        return importRepresentation;
    }
}
