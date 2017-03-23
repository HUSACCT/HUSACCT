package husacct.analyse.task.analyse.java.analysing;

import husacct.analyse.task.analyse.java.parsing.JavaParser.PackageDeclarationContext;

class PackageAnalyser extends JavaGenerator {

	private String uniqueName = "";
	private String name = "";
	private String belongsToPackage = "";
	
	public PackageAnalyser(PackageDeclarationContext packageDeclaration) {
		if (packageDeclaration != null && packageDeclaration.qualifiedName() != null) {
			String uniquePackageName = packageDeclaration.qualifiedName().getText();
	        if ((uniquePackageName != null) && !uniquePackageName.isEmpty()) {
	            uniqueName = uniquePackageName;
	            belongsToPackage = getParentPackageName();
	            name = getNameOfPackage();
	        } else {
	            uniqueName = "No_Package";
	            belongsToPackage = "";
	            name = "No_Package_HUSACCT_Defined";
	        }
        } else {
            uniqueName = "No_Package";
            belongsToPackage = "";
            name = "No_Package_HUSACCT_Defined";
        }
        createPackage(name, uniqueName, belongsToPackage);
	}

	public String getPackageUniqueName() {
		return uniqueName;
	}
	
    private String getParentPackageName() {
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

    private String getNameOfPackage() {
        String[] allPackages = splitPackages(uniqueName);
        return allPackages[allPackages.length - 1];
    }

    private String[] splitPackages(String completePackageName) {
        String escapedPoint = "\\.";
        return completePackageName.split(escapedPoint);
    }

    private void createPackage(String name, String uniqueName, String belongsToPackage) {
        modelService.createPackage(uniqueName, belongsToPackage, name);
    }
}
