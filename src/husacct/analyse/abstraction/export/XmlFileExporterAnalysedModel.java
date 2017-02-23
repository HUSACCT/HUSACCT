package husacct.analyse.abstraction.export;

import org.jdom2.Element;

import husacct.ServiceProvider;
import husacct.analyse.abstraction.dto.ClassDTO;
import husacct.analyse.abstraction.dto.LibraryDTO;
import husacct.analyse.abstraction.dto.PackageDTO;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.UmlLinkDTO;
import husacct.common.imexport.XmlConversionUtils;

public class XmlFileExporterAnalysedModel {

    private Element analysisModelElement;
    private Element applicationElement;
    private Element packagesElement;
    private Element classesElement;
    private Element librariesElement;
    private Element dependenciesElement;
    private Element umlLinksElement;

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
        return XmlConversionUtils.writeDtoToXml(elementName, dto);
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