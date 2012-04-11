package husacct.analyse.domain.famix;

public class FamixImport extends FamixAssociation{
	
	private String importingClass;
	private String importDeclaration;
	private String completeImportString;
	private boolean isCompletePackage;


	public String getImportingClass() {
		return importingClass;
	}

	public void setImportingClass(String importingClass) {
		this.importingClass = importingClass;
	}
	
	public boolean isCompletePackage() {
		return isCompletePackage;
	}

	public void setIsCompletePackage(boolean isCompletePackage) {
		this.isCompletePackage = isCompletePackage;
	}

	public String getImportDeclaration() {
		return importDeclaration;
	}

	public void setImportDeclaration(String importDeclaration) {
		this.importDeclaration = importDeclaration;
	}
	
	public String getCompleteImportString() {
		return completeImportString;
	}

	public void setCompleteImportString(String completeImportString) {
		this.completeImportString = completeImportString;
	}
	
	public String toString(){
		String importRepresentation = "";
		importRepresentation += "\nimportingClass: " + this.importingClass;
		importRepresentation += "\nimportDeclaration " + importDeclaration;
		importRepresentation += "\nisCompletePackage: ";
		if(isCompletePackage) importRepresentation += "true";
		else importRepresentation += "false";
		importRepresentation += "\ncompleteImportString: " + completeImportString + "\n";
		return importRepresentation;
	}
	
	public String getTestDetails(boolean showAvailableVariables){
		String details = "";
		details += "Complete Declaration of import: " + completeImportString;
		if(this.isCompletePackage){
			details += "\n The complete package "  + this.importDeclaration + " was imported";
		}
		else{
			details += "\n Only Class "  + this.importDeclaration + " was imported";
		}
		
		if(showAvailableVariables){
			details += "\n\nImported by class: " + this.importingClass + "\n";
			details += "Variables available in FamixImport-class:\n";
			details += "Complete Import String: " + completeImportString + "\n"; 
			details += "Import Declaration: " + importDeclaration + "\n";
			details += "Boolean isCompletePackage: ";
			if(isCompletePackage) details += "true\n";
			else details += "false\n";
		}
		
		return details;
	}
}

