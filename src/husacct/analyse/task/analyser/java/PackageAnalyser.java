package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.Java7Parser;

class PackageAnalyser extends JavaGenerator {

	private String thePackage;
	
	@Override 
	public void enterPackageDeclaration(Java7Parser.PackageDeclarationContext ctx) { 
		String uniqueName = ctx.qualifiedName().getText();
        if (uniqueName != null) {
            addPackageToModel(uniqueName);
            this.thePackage = uniqueName;
        } else {
    		this.thePackage = generateNoPackage_Package();
        }
	}

	public String getPackage() {
		return thePackage;
	}
	
	public void addPackageToModel(String uniqueName) {
        String belongsToPackage = getParentPackageName(uniqueName);
        String name = getNameOfPackage(uniqueName);
        createPackage(name, uniqueName, belongsToPackage);
    }

    private String getParentPackageName(String uniqueName) {
        String[] allPackages = splitPackages(uniqueName);
        String parentPackage = "";
        for (int i = 0; i < allPackages.length - 1; i++) {
            if (parentPackage == "") {
                parentPackage += allPackages[i];
            } else {
                parentPackage += "." + allPackages[i];
            }
        }
        return parentPackage;
    }

    private String getNameOfPackage(String uniqueName) {
        String[] allPackages = splitPackages(uniqueName);
        return allPackages[allPackages.length - 1];
    }

    private String[] splitPackages(String completePackageName) {
        String escapedPoint = "\\.";
        return completePackageName.split(escapedPoint);
    }

    private String generateNoPackage_Package() {
        String uniqueName = "No_Package";
        String belongsToPackage = "";
        String name = "No_Package_HUSACCT_Defined";
        createPackage(name, uniqueName, belongsToPackage);
        return uniqueName;
    }

    private void createPackage(String name, String uniqueName, String belongsToPackage) {
        modelService.createPackage(uniqueName, belongsToPackage, name);
    }
}
