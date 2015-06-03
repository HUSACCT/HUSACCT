package husacct.analyse.domain.famix;

import husacct.analyse.abstraction.export.XmlFileExporter;
import husacct.analyse.domain.IModelPersistencyService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import javax.naming.directory.InvalidAttributesException;

import org.jdom2.Element;

public class FamixPersistencyServiceImpl implements IModelPersistencyService {

    private FamixModel theModel;
    private Element rootNode, packages, libraries, classes, methods, variables, associations;
    private TreeMap<String, FamixPackage> packagesMap;
    private TreeMap<String, FamixClass> classesTreeMap;
    private TreeMap<String, FamixLibrary> librarieTreeMap;
    private HashMap<String, FamixBehaviouralEntity> methodsList;
    private HashMap<String, FamixStructuralEntity> variablesList;
    private ArrayList<FamixAssociation> associationList;
    
    XmlFileExporter xmlFileExporter;

    // Used for the generic mechanism to save workspace data of all components; e.g. configuration settings  
	@Override
	public Element getWorkspaceData() {
		Element rootElement = new Element("rootElementAnalyse");
		// No configuration data are saved and loaded, currently.
		return rootElement;
	}

    // Used for the generic mechanism to load workspace data of all components; e.g. configuration settings  
	@Override
	public void loadWorkspaceData(Element rootElement) {
		// No configuration data are saved and loaded, currently.
	}

    @Override
    public Element exportAnalysisModel() {
    	xmlFileExporter = new XmlFileExporter();
        theModel = FamixModel.getInstance();
        initiateNodes();
        loadObjects();
        writePackagesToXML();
        writeClassesToXML();
        writeLibrariesToXML();
        return xmlFileExporter.getXML();
    }

    private void initiateNodes() {
        this.rootNode = new Element("Root");
        this.packages = new Element("Packages");
        this.libraries = new Element("Libraries");
        this.classes = new Element("Classes");
        this.methods = new Element("Methods");
        this.variables = new Element("Variables");
        this.associations = new Element("Associations");
    }

    private void loadObjects() {
        this.packagesMap = theModel.packages;
        this.librarieTreeMap = new TreeMap<String, FamixLibrary>(theModel.libraries);
        this.classesTreeMap = new TreeMap<String, FamixClass>(theModel.classes);
        this.methodsList = theModel.behaviouralEntities;
        this.variablesList = theModel.structuralEntities;
        this.associationList = theModel.associations;
    }

    private void writePackagesToXML() {
        for (String packageKey : theModel.packages.keySet()) {
        	FamixPackage fPackage = theModel.packages.get(packageKey);
        	xmlFileExporter.writePackageToXml(fPackage.getDTO());
        }
    }

    private void writeClassesToXML() {
        for (String classKey : classesTreeMap.keySet()) {
        	FamixClass fClass = classesTreeMap.get(classKey);
        	xmlFileExporter.writeClassToXml(fClass.getDTO());
        }
    }

    private void writeLibrariesToXML() {
        for (String Key : librarieTreeMap.keySet()) {
        	FamixLibrary f = librarieTreeMap.get(Key);
        	xmlFileExporter.writeLibraryToXml(f.getDTO());
        }
    }
    
    @Override
    public void loadModel(Element analyseElement) {
    	//2014-01-21 Decision: Saving and loading analysed data is not useful
    	boolean saveAnalysedData = false; 
    	if (!saveAnalysedData)
    		return;
    	else {
    		theModel = FamixModel.getInstance();
    		
    		for (Element rootElem : analyseElement.getChildren()) {
    			for (Element rootChild1Elem : rootElem.getChildren()) {
    				if (rootChild1Elem.getName().equalsIgnoreCase("Package")) {
    					FamixPackage famTempPackage = new FamixPackage();
    					for (Element rootChild1Attrs : rootChild1Elem.getChildren()) {
    						if (rootChild1Attrs.getName().equalsIgnoreCase("UniqueName")) {
    							famTempPackage.uniqueName = rootChild1Attrs.getText();
    						} else if (rootChild1Attrs.getName().equalsIgnoreCase("Name")) {
    							famTempPackage.name = rootChild1Attrs.getText();
    						} else if (rootChild1Attrs.getName().equalsIgnoreCase("BelongsToPackage")) {
    							famTempPackage.belongsToPackage = rootChild1Attrs.getText();
    						}
    					}
    					try {
    						theModel.addObject(famTempPackage);
    					} catch (InvalidAttributesException e) {
    						e.printStackTrace();
    					}
    				}
    				if (rootChild1Elem.getName().equalsIgnoreCase("Class")) {
    					FamixClass famTempClass = new FamixClass();
    					for (Element rootChild1Attrs : rootChild1Elem.getChildren()) {

    						if (rootChild1Attrs.getName().equalsIgnoreCase("UniqueName")) {
    							famTempClass.uniqueName = rootChild1Attrs.getText();
    						} else if (rootChild1Attrs.getName().equalsIgnoreCase("Name")) {
    							famTempClass.name = rootChild1Attrs.getText();
    						} else if (rootChild1Attrs.getName().equalsIgnoreCase("BelongsToPackage")) {
    							famTempClass.belongsToPackage = rootChild1Attrs.getText();
    						} else if (rootChild1Attrs.getName().equalsIgnoreCase("IsAbstract")) {

    							if (rootChild1Attrs.getText().equalsIgnoreCase("true")) {
    								famTempClass.isAbstract = true;
    							} else {
    								famTempClass.isAbstract = false;
    							}
    						} else if (rootChild1Attrs.getName().equalsIgnoreCase("IsInnerClass")) {
    							if (rootChild1Attrs.getText().equalsIgnoreCase("true")) {
    								famTempClass.isInnerClass = true;
    							} else {
    								famTempClass.isInnerClass = false;
    							}
    						}
    					}
    					try {
    						theModel.addObject(famTempClass);
    					} catch (InvalidAttributesException e) {
    						e.printStackTrace();
    					}
    				}
    				if (rootChild1Elem.getName().equalsIgnoreCase("Method")) {
    					FamixMethod famixMethod = new FamixMethod();
    					for (Element rootChild1Attrs : rootChild1Elem.getChildren()) {

    						famixMethod.isPureAccessor = false;
    						famixMethod.belongsToClass = "";
    						famixMethod.isConstructor = false;
    						famixMethod.isAbstract = false;
    						famixMethod.hasClassScope = false;

    						if (rootChild1Attrs.getName().equalsIgnoreCase("Name")) {
    							famixMethod.name = rootChild1Attrs.getText();
    						} else if (rootChild1Attrs.getName().equalsIgnoreCase("UniqueName")) {
    							famixMethod.uniqueName = rootChild1Attrs.getText();
    						} else if (rootChild1Attrs.getName().equalsIgnoreCase("accessControlQualifier")) {
    							famixMethod.accessControlQualifier = rootChild1Attrs.getText();
    						} else if (rootChild1Attrs.getName().equalsIgnoreCase("signature")) {
    							famixMethod.signature = rootChild1Attrs.getText();
    						} else if (rootChild1Attrs.getName().equalsIgnoreCase("declaredReturnType")) {
    							famixMethod.declaredReturnType = rootChild1Attrs.getText();
    						}
    					}
    					try {
    						theModel.addObject(famixMethod);
    					} catch (InvalidAttributesException e) {
    						e.printStackTrace();
    					}
    				}
    				if (rootChild1Elem.getName().equalsIgnoreCase("Variable")) {

    					FamixAttribute famTempVariable = new FamixAttribute();
    					for (Element rootChild1Attrs : rootChild1Elem.getChildren()) {

    						if (rootChild1Attrs.getName().equalsIgnoreCase("UniqueName")) {
    							famTempVariable.uniqueName = rootChild1Attrs.getText();
    						} else if (rootChild1Attrs.getName().equalsIgnoreCase("Name")) {
    							famTempVariable.name = rootChild1Attrs.getText();
    						} else if (rootChild1Attrs.getName().equalsIgnoreCase("BelongsToClass")) {
    							famTempVariable.belongsToClass = rootChild1Attrs.getText();
    						} else if (rootChild1Attrs.getName().equalsIgnoreCase("declareType")) {
    							famTempVariable.declareType = rootChild1Attrs.getText();
    						}
    					}
    					try {
    						theModel.addObject(famTempVariable);
    					} catch (InvalidAttributesException e) {
    						e.printStackTrace();
    					}

    				}
    				if (rootChild1Elem.getName().equalsIgnoreCase("Association")) {
    					FamixAssociation famTempAssociation = new FamixAssociation();
    					for (Element rootChild1Attrs : rootChild1Elem.getChildren()) {
    						if (rootChild1Attrs.getName().equalsIgnoreCase("From")) {
    							famTempAssociation.from = rootChild1Attrs.getText();
    						} else if (rootChild1Attrs.getName().equalsIgnoreCase("Type")) {
    							famTempAssociation.type = rootChild1Attrs.getText();
    						} else if (rootChild1Attrs.getName().equalsIgnoreCase("linenumber")) {
    							famTempAssociation.lineNumber = Integer.parseInt(rootChild1Attrs.getText());
    						} else if (rootChild1Attrs.getName().equalsIgnoreCase("To")) {
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
    

}
