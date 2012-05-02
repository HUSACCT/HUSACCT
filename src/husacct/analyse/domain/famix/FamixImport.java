package husacct.analyse.domain.famix;

class FamixImport extends FamixAssociation{
	
	public String importingClass;
	public String importedModule;
	public String completeImportString;
	public boolean importsCompletePackage;
	
	public FamixImport(){
		super.type = "import";
	}
	
	public String toString(){
		String importRepresentation = "";
		importRepresentation += "\nimportingClass: " + this.importingClass;
		importRepresentation += "\nimportedModule: " + this.importedModule;
		importRepresentation += "\nisCompletePackage: ";
		if(importsCompletePackage) importRepresentation += "true";
		else importRepresentation += "false";
		importRepresentation += "\ncompleteImportString: " + completeImportString + "\n\n";
		return importRepresentation;
	}
}

