package husacct.analyse.domain.famix;

class FamixClass extends FamixEntity{

	public boolean isInnerClass = false;
	public boolean isAbstract;
	public String belongsToPackage;
	public String belongsToClass = null;
	
	@Override
	public boolean equals(Object object){
		return object instanceof FamixClass && super.uniqueName.equals(((FamixClass) object).uniqueName);
	}
	
	public String toString(){
		String classRepresentation = "";
		classRepresentation += "\nUnique Name: " + super.uniqueName;
		classRepresentation += "\nBelongs to Package: " + belongsToPackage;
		classRepresentation += "\nName: " + super.name;
		if(isAbstract) classRepresentation += "\nisAbstract: true";
		else classRepresentation += "\nisAbstract: false";
		classRepresentation += "\nIs Inner Class: ";
		if(isInnerClass) classRepresentation += "true";
		else classRepresentation += "false";
		classRepresentation += "\n\n";
		return classRepresentation;
	}
}
