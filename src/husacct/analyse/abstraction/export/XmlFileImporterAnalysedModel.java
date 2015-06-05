package husacct.analyse.abstraction.export;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom2.Element;

import husacct.analyse.abstraction.dto.ClassDTO;
import husacct.analyse.abstraction.dto.LibraryDTO;
import husacct.analyse.abstraction.dto.PackageDTO;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.DependencyDTO;

public class XmlFileImporterAnalysedModel {

    private Element packagesElement = new Element("Packages");
    private Element classesElement = new Element("Classes");
    private Element librariesElement = new Element("Libraries");
    private Element dependenciesElement = new Element("Dependencies");
    private Logger husacctLogger = Logger.getLogger(XmlFileImporterAnalysedModel.class);

    public XmlFileImporterAnalysedModel(Element analyseElement) {
		for (Element rootElement : analyseElement.getChildren()) {
			String name = rootElement.getName();
			if (name.equals("Packages")) {
				packagesElement = rootElement;
			} else if (name.equals("Classes")){
				classesElement = rootElement;
			} else if (name.equals("Libraries")){
				librariesElement = rootElement;
			} else if (name.equals("Dependencies")){
				dependenciesElement = rootElement;
			}
		}
    }

    public List<PackageDTO> readPackagesfromXml() {
    	List<PackageDTO> dtoList = new ArrayList<PackageDTO>();
    	for (Element e: packagesElement.getChildren()){
    		if (e.getName().equals("Package")) {
    			PackageDTO dto = new PackageDTO();
    			dto = (PackageDTO) writeElementToDto(e, dto);
    			dtoList.add(dto);
    		}
    	}
    	return dtoList;
    }
    
    public List<ClassDTO> readClassesfromXml() {
    	List<ClassDTO> dtoList = new ArrayList<ClassDTO>();
    	for (Element e: classesElement.getChildren()){
    		if (e.getName().equals("Class")) {
    			ClassDTO dto = new ClassDTO();
    			dto = (ClassDTO) writeElementToDto(e, dto);
    			dtoList.add(dto);
    		}
    	}
    	return dtoList;
    }
    
    public List<LibraryDTO> readLibrariesfromXml() {
    	List<LibraryDTO> dtoList = new ArrayList<LibraryDTO>();
    	for (Element e: librariesElement.getChildren()){
    		if (e.getName().equals("Library")) {
    			LibraryDTO dto = new LibraryDTO();
    			dto = (LibraryDTO) writeElementToDto(e, dto);
    			dtoList.add(dto);
    		}
    	}

    	return dtoList;
    }
    
    public List<DependencyDTO> readDependenciesfromXml() {
    	List<DependencyDTO> dtoList = new ArrayList<DependencyDTO>();
    	for (Element e: dependenciesElement.getChildren()){
    		if (e.getName().equals("Dependency")) {
    			DependencyDTO dto = new DependencyDTO();
    			dto = (DependencyDTO) writeElementToDto(e, dto);
    			dtoList.add(dto);
    		}
    	}
    	return dtoList;
    }

    public AbstractDTO writeElementToDto(Element element, AbstractDTO dto) {
    	Class<?> d = dto.getClass();
    	try {
			String propertyName;
			Class<?> propertyType;
    		String valueString = "";
    		boolean valueBoolean = false;
    		int valueInt = 0;
    		Field[] fields = d.getDeclaredFields();
    		for( Field field : fields ){
    			propertyName = field.getName().toString();
    			if (element.getChildText(propertyName) != null) {
	    			propertyType = field.getType();
	        		if (propertyType == String.class) {
		    			valueString = element.getChildText(propertyName);
		        		field.set(dto, valueString);
	        		} else {
	        			if (propertyType == boolean.class) {
		        			valueBoolean = Boolean.parseBoolean(element.getChildText(propertyName));
			        		field.set(dto, valueBoolean);
		        		} else if (propertyType == int.class) {
		        			valueInt = Integer.parseInt(element.getChildText(propertyName));
			        		field.set(dto, valueInt);
		        		}
	        		}
    			}
    		}
		} catch (IllegalAccessException e) {
            husacctLogger.warn("Analyse - Couldn export package to xls: " + e.getMessage());
			//e.printStackTrace();
		} catch (IllegalArgumentException e) {
            husacctLogger.warn("Analyse - Couldn export package to xls: " + e.getMessage());
			//e.printStackTrace();
		}
        return dto;
    }

}