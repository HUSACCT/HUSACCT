package husacct.analyse.domain;

public interface ModelService {
	
	public void createPackage(String uniqieName, String belongsToPackage, String name);
	public void createClass(String uniqueName, String name, String belongsToPackage, boolean isAbstract, boolean isInnerClass);
	public void createClass(String uniqueName, String name, String belongsToPackage, boolean isAbstract, boolean isInnerClass, String belongsToClass);
	public void createImport(String importingClass, String importedModule, String completeImportString, boolean importsCompletePackage);
	public void createAttribute(Boolean classScope, String accesControlQualifier, String belongsToClass, String declareType, String name, String uniqueName);
	
	//Debug-functies voor in de main:
	public void printModel();
	public String represent();
}
