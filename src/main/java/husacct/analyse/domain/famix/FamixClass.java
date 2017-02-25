package husacct.analyse.domain.famix;

import husacct.analyse.abstraction.dto.ClassDTO;

class FamixClass extends FamixDecompositionEntity {
	public String sourceFilePath = "";
	public int linesOfCode = 0;
    public boolean isInterface = false;
    public boolean isInnerClass = false;
    public boolean isEnumeration = false;
    public boolean isAbstract = false;
    public boolean hasInnerClasses = false;
    public String belongsToClass = null;

    @Override
    public boolean equals(Object object) {
        return object instanceof FamixClass && super.uniqueName.equals(((FamixClass) object).uniqueName);
    }

    public ClassDTO getDTO() {
    	ClassDTO cDTO = new ClassDTO(name, uniqueName, visibility, external, belongsToPackage, sourceFilePath, linesOfCode, 
    			isInterface, isInnerClass, isEnumeration, isAbstract, hasInnerClasses, belongsToClass);
    	return cDTO;
    }
    
    public String toString() {
        String classRepresentation = "";
        classRepresentation += "\nName: " + super.name;
        classRepresentation += "\nBelongs to Package: " + belongsToPackage;
        classRepresentation += "\nSourceFilePath: " + sourceFilePath + ", LOC: " + linesOfCode;
        if (isInterface) {
            classRepresentation += "\nisInterface: true" + ", isEnumeration: " + isEnumeration;
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
