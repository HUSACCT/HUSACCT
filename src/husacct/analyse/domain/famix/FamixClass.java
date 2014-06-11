package husacct.analyse.domain.famix;

class FamixClass extends FamixDecompositionEntity {

    public boolean isInterface = false;
    public boolean isInnerClass = false;
    public boolean isAbstract = false;
    public boolean hasInnerClasses = false;
    public String belongsToClass = null;

    @Override
    public boolean equals(Object object) {
        return object instanceof FamixClass && super.uniqueName.equals(((FamixClass) object).uniqueName);
    }

    public String toString() {
        String classRepresentation = "";
        classRepresentation += "\nName: " + super.name;
        classRepresentation += "\nBelongs to Package: " + belongsToPackage;
        if (isInterface) {
            classRepresentation += "\nisInterface: true";
        } else {
            classRepresentation += "\nisInterface: false";
        }
        if (isAbstract) {
            classRepresentation += "\nisAbstract: true";
        } else {
            classRepresentation += "\nisAbstract: false";
        }
        classRepresentation += "\nIs Inner Class: ";
        if (isInnerClass) {
            classRepresentation += "true";
        } else {
            classRepresentation += "false";
        }
        if (isInnerClass) {
            classRepresentation += "\nBelongs to class: " + belongsToClass;
        }
        classRepresentation += "\n Visibillity: " + super.visibility;
        classRepresentation += "\n\n";
        return classRepresentation;
    }
}
