package husacct.analyse.domain.famix;

import java.util.ArrayList;
import java.util.HashMap;
import javax.naming.directory.InvalidAttributesException;
import husacct.analyse.domain.IModelPersistencyService;
import org.jdom2.Element;

public class FamixPersistencyServiceImpl implements IModelPersistencyService{

	private FamixModel theModel;

	private Element rootNode, packages, classes, methods, variables, associations;
	private HashMap<String, FamixPackage> packagesList; 
	private HashMap<String, FamixClass> classList;
	private HashMap<String, FamixBehaviouralEntity> methodsList;
	private HashMap<String, FamixStructuralEntity> variablesList;
	private HashMap<String, FamixInterface> interfacesList;
	private ArrayList<FamixAssociation> associationList;


	@Override
	public Element saveModel() {
		theModel = FamixModel.getInstance();
		initiateNodes();
		loadObjects();
		Element totalNode = createXml();
		reset();
		return totalNode;
	}

	private void reset(){
		this.packagesList.clear(); this.packagesList = null;
		this.classList.clear(); this.classList = null;
		this.methodsList.clear(); this.methodsList = null;
		this.variablesList.clear(); this.variablesList = null;
		this.associationList.clear(); this.associationList = null;
	}

	private void initiateNodes(){
		this.rootNode = new Element("Root");
		this.packages = new Element("Packages");
		this.classes = new Element("Classes");
		this.methods = new Element("Methods");
		this.variables = new Element("Variables");
		this.associations = new Element("Associations");
	}

	private void loadObjects(){
		this.packagesList = theModel.packages;
		this.classList = theModel.classes;
		this.methodsList = theModel.behaviouralEntities;
		this.variablesList = theModel.structuralEntities;
		this.associationList = theModel.associations;
	}

	private Element createXml(){
		Element analysedProject = new Element("AnalysedApplication");

		Element ElemPackages = new Element("Packages");
		for(String famPackageKey: packagesList.keySet()){
			Element ElemPackage = new Element("Package");
			ElemPackage.addContent(new Element("UniqueName").setText(packagesList.get(famPackageKey).uniqueName));
			ElemPackage.addContent(new Element("Name").setText(packagesList.get(famPackageKey).name));
			ElemPackage.addContent(new Element("BelongsToPackage").setText(packagesList.get(famPackageKey).belongsToPackage));
			ElemPackages.addContent(ElemPackage);
		}
		analysedProject.addContent(ElemPackages);


		Element elemClasses = new Element("Classes");
		for(String famClassKey: classList.keySet()){
			Element elemClass = new Element("Class");
			elemClass.addContent(new Element("UniqueName").setText(classList.get(famClassKey).uniqueName));
			elemClass.addContent(new Element("Name").setText(classList.get(famClassKey).name));
			elemClass.addContent(new Element("BelongsToPackage").setText(classList.get(famClassKey).belongsToPackage));
			elemClass.addContent(new Element("IsAbstract").setText( new Boolean(classList.get(famClassKey).isAbstract).toString()));
			elemClass.addContent(new Element("IsInnerClass").setText( new Boolean(classList.get(famClassKey).isInnerClass).toString()));
			elemClasses.addContent(elemClass);
		}
		analysedProject.addContent(elemClasses);

		Element elemMethods = new Element("Methods");
		for(String famMethKey: methodsList.keySet()){
			Element elemMethod = new Element("Method");
			elemMethod.addContent(new Element("UniqueName").setText(methodsList.get(famMethKey).uniqueName));
			elemMethod.addContent(new Element("Name").setText(methodsList.get(famMethKey).name));
			elemMethod.addContent(new Element("accessControlQualifier").setText(methodsList.get(famMethKey).accessControlQualifier));
			elemMethod.addContent(new Element("signature").setText(methodsList.get(famMethKey).signature));
			elemMethod.addContent(new Element("declaredReturnType").setText(methodsList.get(famMethKey).declaredReturnType));
			elemMethods.addContent(elemMethod);
		}
		analysedProject.addContent(elemMethods);

		Element elemVariables = new Element("Variables");
		for(String famVarKey: variablesList.keySet()){
			Element elemVariable = new Element("Variable");
			elemVariable.addContent(new Element("UniqueName").setText(variablesList.get(famVarKey).uniqueName));
			elemVariable.addContent(new Element("Name").setText(variablesList.get(famVarKey).name));
			elemVariable.addContent(new Element("BelongsToClass").setText(variablesList.get(famVarKey).belongsToClass));
			elemVariable.addContent(new Element("declareType").setText(variablesList.get(famVarKey).declareType));
			elemVariables.addContent(elemVariable);
		}
		analysedProject.addContent(elemVariables);

		Element elemAssociations = new Element("Associations");
		for(FamixAssociation famAssoCurrent: associationList){
			String From = famAssoCurrent.from;
			String To = famAssoCurrent.to;
			String Type = famAssoCurrent.type;
			String LineNr = Integer.toString(famAssoCurrent.lineNumber);
			Element elemAssociation = new Element("Association");
			elemAssociation.addContent(new Element("From").setText(From));
			elemAssociation.addContent(new Element("To").setText(To));
			elemAssociation.addContent(new Element("Type").setText(Type));
			elemAssociation.addContent(new Element("linenumber").setText(LineNr));
			elemAssociations.addContent(elemAssociation);
		}
		analysedProject.addContent(elemAssociations);
		return analysedProject;
	}

	@Override
	public void loadModel(Element analyseElement) {
		theModel = FamixModel.getInstance();

		for (Element rootElem : analyseElement.getChildren()) {
			for (Element rootChild1Elem : rootElem.getChildren()) {
				if(rootChild1Elem.getName().equalsIgnoreCase("Package")){
					FamixPackage famTempPackage = new FamixPackage();
					for (Element rootChild1Attrs : rootChild1Elem.getChildren()) {
						if(rootChild1Attrs.getName().equalsIgnoreCase("UniqueName")){
							famTempPackage.uniqueName = rootChild1Attrs.getText();
						}
						else if(rootChild1Attrs.getName().equalsIgnoreCase("Name")){
							famTempPackage.name = rootChild1Attrs.getText();
						}
						else if(rootChild1Attrs.getName().equalsIgnoreCase("BelongsToPackage")){
							famTempPackage.belongsToPackage = rootChild1Attrs.getText();
						}
					}	
					try {
						theModel.addObject(famTempPackage);
					} catch (InvalidAttributesException e) {
						e.printStackTrace();
					}
				}
				if(rootChild1Elem.getName().equalsIgnoreCase("Class")){
					FamixClass famTempClass = new FamixClass();
					for (Element rootChild1Attrs : rootChild1Elem.getChildren()) {

						if(rootChild1Attrs.getName().equalsIgnoreCase("UniqueName")){
							famTempClass.uniqueName = rootChild1Attrs.getText();
						}
						else if(rootChild1Attrs.getName().equalsIgnoreCase("Name")){
							famTempClass.name = rootChild1Attrs.getText();
						}
						else if(rootChild1Attrs.getName().equalsIgnoreCase("BelongsToPackage")){
							famTempClass.belongsToPackage = rootChild1Attrs.getText();
						}
						else if(rootChild1Attrs.getName().equalsIgnoreCase("IsAbstract")){

							if(rootChild1Attrs.getText().equalsIgnoreCase("true")){
								famTempClass.isAbstract = true;
							}
							else { famTempClass.isAbstract = false;	}
						}
						else if(rootChild1Attrs.getName().equalsIgnoreCase("IsInnerClass")){
							if(rootChild1Attrs.getText().equalsIgnoreCase("true")){
								famTempClass.isInnerClass = true;
							}
							else { famTempClass.isInnerClass = false;	}
						}
					}	
					try {
						theModel.addObject(famTempClass);
					} catch (InvalidAttributesException e) {
						e.printStackTrace();
					}
				}
				if(rootChild1Elem.getName().equalsIgnoreCase("Method")){
					FamixMethod famixMethod = new FamixMethod();
					for (Element rootChild1Attrs : rootChild1Elem.getChildren()) {

						famixMethod.isPureAccessor = false;
						famixMethod.belongsToClass = "";
						famixMethod.isConstructor = false;
						famixMethod.isAbstract = false;
						famixMethod.hasClassScope = false;

						if(rootChild1Attrs.getName().equalsIgnoreCase("Name")){
							famixMethod.name = rootChild1Attrs.getText();
						}
						else if(rootChild1Attrs.getName().equalsIgnoreCase("UniqueName")){
							famixMethod.uniqueName =  rootChild1Attrs.getText();
						}
						else if(rootChild1Attrs.getName().equalsIgnoreCase("accessControlQualifier")){
							famixMethod.accessControlQualifier =  rootChild1Attrs.getText();
						}
						else if(rootChild1Attrs.getName().equalsIgnoreCase("signature")){
							famixMethod.signature =  rootChild1Attrs.getText();
						}
						else if(rootChild1Attrs.getName().equalsIgnoreCase("declaredReturnType")){
							famixMethod.declaredReturnType =  rootChild1Attrs.getText();							}
					}	
					try {
						theModel.addObject(famixMethod);
					} catch (InvalidAttributesException e) {
						e.printStackTrace();
					}
				}
				if(rootChild1Elem.getName().equalsIgnoreCase("Variable")){

					FamixAttribute famTempVariable = new FamixAttribute();
					for (Element rootChild1Attrs : rootChild1Elem.getChildren()) {

						if(rootChild1Attrs.getName().equalsIgnoreCase("UniqueName")){
							famTempVariable.uniqueName = rootChild1Attrs.getText();
						}
						else if(rootChild1Attrs.getName().equalsIgnoreCase("Name")){
							famTempVariable.name = rootChild1Attrs.getText();
						}
						else if(rootChild1Attrs.getName().equalsIgnoreCase("BelongsToClass")){
							famTempVariable.belongsToClass = rootChild1Attrs.getText();
						}
						else if(rootChild1Attrs.getName().equalsIgnoreCase("declareType")){
							famTempVariable.declareType = rootChild1Attrs.getText();
						}
					}	
					try {
						theModel.addObject(famTempVariable);
					} catch (InvalidAttributesException e) {
						e.printStackTrace();
					}

				}
				if(rootChild1Elem.getName().equalsIgnoreCase("Association")){
					FamixAssociation famTempAssociation = new FamixAssociation();
					for (Element rootChild1Attrs : rootChild1Elem.getChildren()) {
						if(rootChild1Attrs.getName().equalsIgnoreCase("From")){
							famTempAssociation.from = rootChild1Attrs.getText();
						}
						else if(rootChild1Attrs.getName().equalsIgnoreCase("Type")){
							famTempAssociation.type = rootChild1Attrs.getText();
						}
						else if(rootChild1Attrs.getName().equalsIgnoreCase("linenumber")){
							famTempAssociation.lineNumber = Integer.parseInt(rootChild1Attrs.getText());
						}
						else if(rootChild1Attrs.getName().equalsIgnoreCase("To")){
							famTempAssociation.to = rootChild1Attrs.getText();
						}
					}	
					try {
						theModel.addObject(famTempAssociation);
					} catch (InvalidAttributesException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
