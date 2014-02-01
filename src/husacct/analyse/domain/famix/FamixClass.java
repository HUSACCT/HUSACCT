package husacct.analyse.domain.famix;

class FamixClass extends FamixEntity {

    public boolean isInnerClass = false;
    public boolean isAbstract = false;
    public boolean hasInnerClasses = false;
    public String belongsToPackage;
    public String belongsToClass = null;

    @Override
    public boolean equals(Object object) {
        return object instanceof FamixClass && super.uniqueName.equals(((FamixClass) object).uniqueName);
    }

    public String toString() {
        String classRepresentation = "";
        classRepresentation += "\nUnique Name: " + super.uniqueName;
        classRepresentation += "\nBelongs to Package: " + belongsToPackage;
        classRepresentation += "\nName: " + super.name;
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
