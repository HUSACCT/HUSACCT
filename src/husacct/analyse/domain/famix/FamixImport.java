package husacct.analyse.domain.famix;

public class FamixImport extends FamixAssociation{
	
	public String importingClass;
	public String importedModule;
	public String completeImportString;
	public boolean importsCompletePackage;
	
	public String toString(){
		String importRepresentation = "";
		importRepresentation += "\nimportingClass: " + this.importingClass;
		importRepresentation += "\nimportedModule: " + this.importedModule;
		importRepresentation += "\nisCompletePackage: ";
		if(importsCompletePackage) importRepresentation += "true";
		else importRepresentation += "false";
		importRepresentation += "\ncompleteImportString: " + completeImportString + "\n";
		return importRepresentation;
	}
	
	public String getTestDetails(boolean showAvailableVariables){
		String details = "";
		details += "Complete Declaration of import: " + completeImportString;
		if(this.importsCompletePackage){
			details += "\n The complete package "  + this.importedModule + " was imported";
		}
		else{
			details += "\n Only Class "  + this.importedModule + " was imported";
		}
		
		if(showAvailableVariables){
			details += "\n\nImported by class: " + this.importingClass + "\n";
			details += "Variables available in FamixImport-class:\n";
			details += "Complete Import String: " + completeImportString + "\n"; 
			details += "Import Declaration: " + importedModule + "\n";
			details += "Boolean isCompletePackage: ";
			if(importsCompletePackage) details += "true\n";
			else details += "false\n";
		}
		
		return details;
	}
}

