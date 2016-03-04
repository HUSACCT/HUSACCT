package husacct.analyse.domain.famix;

class FamixAttribute extends FamixStructuralEntity {

    public String accessControlQualifier;
    public boolean hasClassScope;
    public boolean isFinal = false;
    public String typeInClassDiagram;	// E.g., this value is Person in case of an instance variable with a generic type ArrayList<Person>, while declareType = ArrayList in this case.
    public boolean isComposite; 		// False if the type allows one value only, like Person; True in case of a generic type, or e.g. Person[]. 


    public String toString() {
        String importRepresentation = "";
        importRepresentation += "\nname: " + super.name + ", nuniquename: " + super.uniqueName;
        importRepresentation += "\nbelongsToClass: " + super.belongsToClass;
        importRepresentation += "\naccessControlQualifier: " + accessControlQualifier + ", hasClassScope: " + hasClassScope + ", isFinal: " + isFinal;
        importRepresentation += "\ndeclareType: " + super.declareType;
        importRepresentation += "\nlineNumber: " + super.lineNumber;
        importRepresentation += "\nisComposite: " + isComposite + ", typeInClassDiagram: " + typeInClassDiagram;
        importRepresentation += "\n";
        importRepresentation += "\n";
        return importRepresentation;
    }
}
