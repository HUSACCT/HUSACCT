package husacct.analyse.abstraction.export;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.jdom2.Element;

import husacct.ServiceProvider;
import husacct.analyse.abstraction.dto.ClassDTO;
import husacct.analyse.abstraction.dto.LibraryDTO;
import husacct.analyse.abstraction.dto.PackageDTO;
import husacct.analyse.serviceinterface.dto.DependencyDTO;
import husacct.analyse.serviceinterface.dto.UmlLinkDTO;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.ApplicationDTO;

public class XmlFileExporterAnalysedModel {

    private Element analysisModelElement;
    private Element applicationElement;
    private Element packagesElement;
    private Element classesElement;
    private Element librariesElement;
    private Element dependenciesElement;
    private Element umlLinksElement;
    private Logger husacctLogger = Logger.getLogger(XmlFileExporterAnalysedModel.class);

    public XmlFileExporterAnalysedModel() {
        writeApplicationElement();
		packagesElement = new Element("Packages");
		classesElement = new Element("Classes");
		librariesElement = new Element("Libraries");
		dependenciesElement = new Element("Dependencies");
		umlLinksElement = new Element("UmlLinks");
    }

    private void writeApplicationElement() {
		ApplicationDTO applicationDTO = ServiceProvider.getInstance().getDefineService().getApplicationDetails();
		applicationElement = new Element("Application");
		applicationElement.addContent(new Element("ApplicationName").setText(applicationDTO.name));
    }
    
    public void writePackageToXml(PackageDTO dto) {
    	Element packageElement = writeDtoToXml("Package", dto);
    	packagesElement.addContent(packageElement);
    }
    
    public void writeClassToXml(ClassDTO dto) {
    	Element classElement = writeDtoToXml("Class", dto);
    	classesElement.addContent(classElement);
    }
    
    public void writeLibraryToXml(LibraryDTO dto) {
    	Element libraryElement = writeDtoToXml("Library", dto);
    	librariesElement.addContent(libraryElement);
    }
    
    public void writeDependencyToXml(DependencyDTO dto) {
    	Element dependencyElement = writeDtoToXml("Dependency", dto);
    	dependenciesElement.addContent(dependencyElement);
    }

    public void writeUmlLinkToXml(UmlLinkDTO dto) {
    	Element umlLinkElement = writeDtoToXml("UmlLink", dto);
    	umlLinksElement.addContent(umlLinkElement);
    }

    public Element writeDtoToXml(String elementName, AbstractDTO dto) {
    	Element dtoElement = new Element(elementName);
    	Class<?> d = dto.getClass();
    	try {
			String propertyName = "";
    		String valueString = "";
    		Field[] fields = d.getDeclaredFields();
    		for( Field field : fields ){
    			propertyName = field.getName();
    			Object value = field.get(dto);
    			if (value != null) {
    				valueString = field.get(dto).toString();
    			}
    			dtoElement.addContent(new Element(propertyName).setText(valueString));
    		 }
		} catch (IllegalAccessException e) {
            husacctLogger.warn("Analyse - Couldn export package to xls: " + e.getMessage());
			//e.printStackTrace();
		} catch (IllegalArgumentException e) {
            husacctLogger.warn("Analyse - Couldn export package to xls: " + e.getMessage());
			//e.printStackTrace();
		} catch (Exception e) {
            husacctLogger.warn("Analyse - Couldn export package to xls: " + e.getMessage());
			//e.printStackTrace();
		}
        return dtoElement;
    }
    
    public Element getXML() {
        analysisModelElement = new Element("AnalysisModel");
		analysisModelElement.addContent(applicationElement);
    	analysisModelElement.addContent(packagesElement);
    	analysisModelElement.addContent(classesElement);
    	analysisModelElement.addContent(librariesElement);
    	analysisModelElement.addContent(dependenciesElement);
    	analysisModelElement.addContent(umlLinksElement);
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

}