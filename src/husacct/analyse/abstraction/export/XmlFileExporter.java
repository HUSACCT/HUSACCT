package husacct.analyse.abstraction.export;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.jdom2.Element;

import husacct.ServiceProvider;
import husacct.analyse.abstraction.dto.ClassDTO;
import husacct.analyse.abstraction.dto.LibraryDTO;
import husacct.analyse.abstraction.dto.PackageDTO;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.ApplicationDTO;

public class XmlFileExporter {

    private Element analysisModelElement;
    private Element applicationElement;
    private Element packagesElement;
    private Element classesElement;
    private Element librariesElement;
    private Logger husacctLogger = Logger.getLogger(XmlFileExporter.class);

    public XmlFileExporter() {
        analysisModelElement = new Element("AnalysisModel");
        writeApplicationElement();
		packagesElement = new Element("Packages");
		classesElement = new Element("Classes");
		librariesElement = new Element("Libraries");
    }

    private void writeApplicationElement() {
		ApplicationDTO applicationDTO = ServiceProvider.getInstance().getDefineService().getApplicationDetails();
		applicationElement = new Element("Application");
		applicationElement.addContent(new Element("ApplicationName").setText(applicationDTO.name));
    }
    
    public void writePackageToXml(PackageDTO pDTO) {
    	Element packageElement = writeDtoToXml("Package", pDTO);
    	packagesElement.addContent(packageElement);
    }
    
    public void writeClassToXml(ClassDTO cDTO) {
    	Element classElement = writeDtoToXml("Class", cDTO);
    	classesElement.addContent(classElement);
    }
    
    public void writeLibraryToXml(LibraryDTO dto) {
    	Element libraryElement = writeDtoToXml("Library", dto);
    	librariesElement.addContent(libraryElement);
    }
    
     public Element writeDtoToXml(String elementName, AbstractDTO dto) {
    	Element dtoElement = new Element(elementName);
    	Class<?> d = dto.getClass();
    	try {
			String propertyName;
    		String valueString;
    		Field[] fields = d.getDeclaredFields();
    		for( Field field : fields ){
    			propertyName = field.getName().toString();
    			valueString = field.get(dto).toString();
    			dtoElement.addContent(new Element(propertyName).setText(valueString));
    		 }
		} catch (IllegalAccessException e) {
            husacctLogger.warn("Analyse - Couldn export package to xls: " + e.getMessage());
			//e.printStackTrace();
		} catch (IllegalArgumentException e) {
            husacctLogger.warn("Analyse - Couldn export package to xls: " + e.getMessage());
			//e.printStackTrace();
		}
        return dtoElement;
    }

    
    public Element getXML() {
		analysisModelElement.addContent(applicationElement);
    	analysisModelElement.addContent(packagesElement);
    	analysisModelElement.addContent(classesElement);
    	analysisModelElement.addContent(librariesElement);
    	return analysisModelElement;
    }
    
    public void writeToFile(String path) throws NoDataException {
        if (analysisModelElement.getChildren().size() <= 0 ) {
            throw new NoDataException();
        } else {
            write(path);
        }
    }

    protected void write(String path) {
    }

    protected String translate(String key) {
        return ServiceProvider.getInstance().getLocaleService().getTranslatedString(key);
    }


}