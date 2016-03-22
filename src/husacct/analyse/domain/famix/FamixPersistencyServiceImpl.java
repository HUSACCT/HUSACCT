package husacct.analyse.domain.famix;

import husacct.analyse.abstraction.dto.ClassDTO;
import husacct.analyse.abstraction.dto.LibraryDTO;
import husacct.analyse.abstraction.dto.PackageDTO;
import husacct.analyse.abstraction.export.XmlFileExporterAnalysedModel;
import husacct.analyse.abstraction.export.XmlFileImporterAnalysedModel;
import husacct.analyse.domain.IModelPersistencyService;
import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.serviceinterface.dto.DependencyDTO;
import husacct.analyse.serviceinterface.dto.UmlLinkDTO;
import husacct.common.dto.AbstractDTO;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import javax.naming.directory.InvalidAttributesException;

import org.apache.log4j.Logger;
import org.jdom2.Element;

public class FamixPersistencyServiceImpl implements IModelPersistencyService {

    private FamixModel theModel;
    private IModelQueryService queryService;
    private TreeMap<String, FamixClass> classesTreeMap;
    private TreeMap<String, FamixLibrary> librarieTreeMap;
    
    XmlFileExporterAnalysedModel xmlFileExporter;
    XmlFileImporterAnalysedModel xmlFileImporter;

    private Logger husacctLogger = Logger.getLogger(XmlFileExporterAnalysedModel.class);

    // The following Famix types have to be exported and imported in strict order: Packages, Classes, Libraries, Dependencies, UmlLinks;
    // The first three need to be sorted on uniqueName.
    
   public FamixPersistencyServiceImpl(IModelQueryService queryService) {
       theModel = FamixModel.getInstance();
	   this.queryService = queryService;
   }
    
   //EXPORT
   
   @Override
    public Element exportAnalysisModel() {
    	xmlFileExporter = new XmlFileExporterAnalysedModel();
        writePackagesToXML();
        writeClassesToXML();
        writeLibrariesToXML();
        writeDependenciesToXML();
        writeUmlLinksToXML();
        return xmlFileExporter.getXML();
    }

    private void writePackagesToXML() {
        for (String packageKey : theModel.packages.keySet()) {
        	FamixPackage fPackage = theModel.packages.get(packageKey);
        	xmlFileExporter.writePackageToXml(fPackage.getDTO());
        }
    }

    private void writeClassesToXML() {
        this.classesTreeMap = new TreeMap<String, FamixClass>(theModel.classes);
        for (String classKey : classesTreeMap.keySet()) {
        	FamixClass fClass = classesTreeMap.get(classKey);
        	xmlFileExporter.writeClassToXml(fClass.getDTO());
        }
    }

    private void writeLibrariesToXML() {
        this.librarieTreeMap = new TreeMap<String, FamixLibrary>(theModel.libraries);
        for (String Key : librarieTreeMap.keySet()) {
        	FamixLibrary f = librarieTreeMap.get(Key);
        	xmlFileExporter.writeLibraryToXml(f.getDTO());
        }
    }
    
    private void writeDependenciesToXML() {
        for (DependencyDTO dependency : queryService.getAllDependencies()) {
        	xmlFileExporter.writeDependencyToXml(dependency);
        }
    }
    
    private void writeUmlLinksToXML() {
        for (String classUniqueName : classesTreeMap.keySet()) {
        	for (UmlLinkDTO umlLink : queryService.getUmlLinksFromClassToOtherClasses(classUniqueName)) {
        	xmlFileExporter.writeUmlLinkToXml(umlLink);
            }
        }
    }
    // IMPORT
    
    @Override
    public void importAnalysisModel(Element analyseElement) {
        queryService.clearModel();
    	xmlFileImporter = new XmlFileImporterAnalysedModel(analyseElement);
    	readPackagesFromXML();
    	readClassesFromXML();
    	readLibrariesFromXML();
    	readDependenciesFromXML();
        readUmlLinksFromXML();
    	queryService.buildCache();
    }

    private void readPackagesFromXML() {
    	List<PackageDTO> list = xmlFileImporter.readPackagesfromXml();
    	for (PackageDTO dto: list) {
    		FamixPackage fobj = new FamixPackage();
    		HashMap<String, Class<?>> fobjFieldsMap = getHashMapWithFields(fobj);
    		writeDtoToFamixObject(dto, fobj, fobjFieldsMap);
    	}
    }
    
    private void readClassesFromXML() {
    	List<ClassDTO> list = xmlFileImporter.readClassesfromXml();
    	for (ClassDTO dto: list) {
    		FamixClass fobj = new FamixClass();
    		HashMap<String, Class<?>> fobjFieldsMap = getHashMapWithFields(fobj);
    		writeDtoToFamixObject(dto, fobj, fobjFieldsMap);
    	}
    }
    
    private void readLibrariesFromXML() {
    	List<LibraryDTO> list = xmlFileImporter.readLibrariesfromXml();
    	for (LibraryDTO dto: list) {
    		FamixLibrary fobj = new FamixLibrary();
    		HashMap<String, Class<?>> fobjFieldsMap = getHashMapWithFields(fobj);
    		writeDtoToFamixObject(dto, fobj, fobjFieldsMap);
    	}
    }
    
    private void readDependenciesFromXML() {
    	List<DependencyDTO> list = xmlFileImporter.readDependenciesfromXml();
    	queryService.importDependencies(list);
    }
    
    private void readUmlLinksFromXML() {
    	List<UmlLinkDTO> list = xmlFileImporter.readUmlLinksFromXML();
    	for (UmlLinkDTO dto: list) {
    		FamixUmlLink fobj = new FamixUmlLink();
    		HashMap<String, Class<?>> fobjFieldsMap = getHashMapWithFields(fobj);
    		writeDtoToFamixObject(dto, fobj, fobjFieldsMap);
    	}
    }
    
    private HashMap<String, Class<?>> getHashMapWithFields(FamixObject fobj) {	
	    Class<?> f = fobj.getClass();
	    List<Field> fobjFields = fobj.getFields(f);
	    HashMap<String, Class<?>> fobjFieldsMap = new HashMap<String, Class<?>>();
	   	for (Field field : fobjFields) {
	   		String name = field.getName().toString();
	   		Class<?> type = field.getType();
	   		fobjFieldsMap.put(name, type);
	   	}
	   	return fobjFieldsMap;
    }
    
    private FamixObject writeDtoToFamixObject(AbstractDTO dto, FamixObject fobj, HashMap<String, Class<?>> fobjFieldsMap) {	
    Class<?> d = dto.getClass();
   	try {
		String fieldName;
		Class<?> fieldType;
   		Field[] fields = d.getFields();
   		for( Field field : fields ){
   			if ((field.getName() != null)) {
   	   			fieldName = field.getName().toString();
   	   			if (fobjFieldsMap.containsKey(fieldName)) {
	    			fieldType = field.getType();
	        		if (fieldType.equals(fobjFieldsMap.get(fieldName))) {
		    			Field fobjField = fobj.getClass().getField(field.getName());
		    			fobjField.set(fobj, field.get(dto));
	        		}
   	   			}
   			}
   		}
		theModel.addObject(fobj);
		} catch (IllegalAccessException e) {
           husacctLogger.warn("Analyse - Couldn export package to xls: " + e.getMessage());
			//e.printStackTrace();
		} catch (IllegalArgumentException e) {
           husacctLogger.warn("Analyse - Couldn export package to xls: " + e.getMessage());
			//e.printStackTrace();
		} catch (NoSuchFieldException e) {
           husacctLogger.warn("Analyse - Couldn export package to xls: " + e.getMessage());
			//e.printStackTrace();
		} catch (SecurityException e) {
           //e.printStackTrace();
		} catch (InvalidAttributesException e) {
           husacctLogger.warn("Analyse - Couldn export package to xls: " + e.getMessage());
			//e.printStackTrace();
		}
       return fobj;
   }
	
    // EXPORT & IMPORT of Workspace Data
    
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

}
