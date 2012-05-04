package husacct.analyse.domain.famix;

import java.util.ArrayList;
import java.util.List;
import javax.naming.directory.InvalidAttributesException;
import husacct.analyse.domain.ModelCreationService;
import husacct.common.dto.AnalysedModuleDTO;

public class FamixCreationServiceImpl implements ModelCreationService{
	
	private FamixModel model;
	private FamixQueryServiceImpl queryService;
	private List<FamixAssociation> dependencyHolder;
	
	public FamixCreationServiceImpl(){
		model = FamixModel.getInstance();
		queryService = new FamixQueryServiceImpl();
		dependencyHolder = new ArrayList<FamixAssociation>();;
	}
	
	public void createPackage(String uniqueName, String belongsToPackage, String name){
		FamixPackage fPackage = new FamixPackage();
		fPackage.uniqueName = uniqueName;
		fPackage.belongsToPackage = belongsToPackage;
		fPackage.name = name;
		addToModel(fPackage);
	}
	
	@Override
	public void createClass(String uniqueName, String name, String belongsToPackage, boolean isAbstract, boolean isInnerClass) {
		FamixClass fClass = new FamixClass();
		fClass.uniqueName = uniqueName;
		fClass.isAbstract = isAbstract;
		fClass.belongsToPackage = belongsToPackage;
		fClass.isInnerClass = isInnerClass;
		fClass.name = name;
		addToModel(fClass);
	}

	@Override
	public void createClass(String uniqueName, String name, String belongsToPackage, boolean isAbstract, boolean isInnerClass, String belongsToClass) {
		FamixClass fClass = new FamixClass();
		fClass.uniqueName = uniqueName;
		fClass.isAbstract = isAbstract;
		fClass.belongsToPackage = belongsToPackage;
		fClass.isInnerClass = isInnerClass;
		fClass.name = name;
		fClass.belongsToClass = belongsToClass;
		addToModel(fClass);
		walkDependencies();
	}
	
	@Override
	public void createInterface(String uniqueName, String name, String belongsToPackage) {
		FamixInterface fInterface = new FamixInterface();
		fInterface.uniqueName = uniqueName;
		fInterface.name = name;
		fInterface.belongsToPackage = belongsToPackage;
		addToModel(fInterface);
	}

	@Override
	public void createImport(String importingClass, String importedModule, int lineNumber, String completeImportString, boolean importsCompletePackage) {
		FamixImport fImport = new FamixImport();
		fImport.from = importingClass;
		fImport.to = importedModule;
		fImport.lineNumber = lineNumber;
		fImport.importingClass = importingClass;
		fImport.completeImportString = completeImportString;
		fImport.importedModule = completeImportString;
		fImport.importsCompletePackage = importsCompletePackage;
		addToModel(fImport);
	}
	
	@Override
	public void createMethod(String name, String uniqueName, String accessControlQualifier, String signature, boolean isPureAccessor, String declaredReturnType,
			String belongsToClass, boolean isConstructor, boolean isAbstract, boolean hasClassScope) {
		
		FamixMethod famixMethod = new FamixMethod();
		famixMethod.name = name;
		famixMethod.uniqueName = uniqueName;

		famixMethod.accessControlQualifier = accessControlQualifier;
		famixMethod.signature = signature;
		famixMethod.isPureAccessor = isPureAccessor;
		famixMethod.declaredReturnType = declaredReturnType;

		famixMethod.belongsToClass = belongsToClass;
		famixMethod.isConstructor = isConstructor;
		famixMethod.isAbstract = isAbstract;
		famixMethod.hasClassScope = hasClassScope;
		addToModel(famixMethod);
	}
	
	@Override
	public void createAttribute(Boolean classScope, String accesControlQualifier, String belongsToClass, String declareType, String name, String uniqueName) {
		FamixAttribute famixAttribute = new FamixAttribute();
		famixAttribute.hasClassScope = classScope;
		famixAttribute.accessControlQualifier = accesControlQualifier;
		famixAttribute.belongsToClass = belongsToClass;
		famixAttribute.declareType = declareType;
		famixAttribute.name = name;
		famixAttribute.uniqueName = uniqueName;
		addToModel(famixAttribute);
	}
	
	@Override
	public void createException(String fromClass, String ExceptionClass, int lineNumber, String declarationType) {
		FamixException exception = new FamixException();
		exception.from = fromClass;
		exception.to = ExceptionClass;
		exception.lineNumber = lineNumber;
		exception.exceptionType = declarationType;
		addToModel(exception);
	}
	
	@Override
	public void createInheritanceDefinition(String from, String to, int lineNumber, String type) {
		FamixInheritanceDefinition famixInheritanceDefinition = new FamixInheritanceDefinition(type);
		famixInheritanceDefinition.from = from;
		famixInheritanceDefinition.to = to;
		famixInheritanceDefinition.lineNumber = lineNumber;
		
		String fromPackage = "";
		String[] fromParts = from.split("\\.");
		for(int i=0; i<fromParts.length -1; i++){
			fromPackage += fromParts[i] + ".";
		}
		fromPackage = fromPackage.substring(0, fromPackage.length() -1); //delete the last point.
		List<AnalysedModuleDTO> classes = queryService.searchClassesInPackage(fromPackage, to);
		if(!classes.isEmpty()){
			famixInheritanceDefinition.to = classes.get(0).uniqueName;
			addToModel(famixInheritanceDefinition);
		}else{
			dependencyHolder.add(famixInheritanceDefinition);
		}
	}
	
	private boolean addToModel(FamixObject newObject){
		try {
			model.addObject(newObject);
			return true;
		} catch (InvalidAttributesException e) {
			return false;
		}
	}
	
	//This function can be called to retry creation of dependencies after a new classe was analysed
	private void walkDependencies(){
		//TODO Retry setting the to-parameter of depdencies in the holder. 
	}
	
	public String represent(){
		return model.toString();
	}
}
