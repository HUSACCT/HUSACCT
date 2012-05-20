package husacct.analyse.domain.famix;

class FamixImport extends FamixAssociation{
	
	public String importingClass;
	public String importedModule;
	public String completeImportString;
	public boolean importsCompletePackage;
	
	public FamixImport(){
		super.type = "Import";
	}
	
	public String toString(){
		String importRepresentation = "";
		importRepresentation += "\ntype: " + super.type;
		importRepresentation += "\nimportingClass: " + this.importingClass;
		importRepresentation += "\nimportedModule: " + this.importedModule;
		importRepresentation += "\nlinenumber: " + super.lineNumber;
		importRepresentation += "\nisCompletePackage: ";
		if(importsCompletePackage) importRepresentation += "true";
		else importRepresentation += "false";
		importRepresentation += "\ncompleteImportString: " + completeImportString + "\n\n";
		return importRepresentation;
	}
}