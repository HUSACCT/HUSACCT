package husacct.validate.abstraction;

import husacct.validate.abstraction.export.ExportController;
import husacct.validate.abstraction.fetch.ImportController;
import husacct.validate.domain.ConfigurationServiceImpl;

import javax.xml.datatype.DatatypeConfigurationException;

import org.jdom2.Element;

public class AbstractionServiceImpl {
	private final ConfigurationServiceImpl configuration;
	
	public AbstractionServiceImpl(ConfigurationServiceImpl configuration){
		this.configuration = configuration;
	}

	public void importValidationWorkspace(Element element) throws DatatypeConfigurationException   {
		ImportController importController = new ImportController(element);
		configuration.addSeverities(importController.getSeverities());
		configuration.addViolations(importController.getViolations());
		configuration.setSeveritiesPerTypes(importController.getSeveritiesPerTypes());
	}
	public Element exportValidationWorkspace() {
		Element rootValidateElement = new Element("validate");
		ExportController exportController = new ExportController();
		rootValidateElement.addContent(exportController.exportViolationsXML(configuration.getAllViolations()));
		rootValidateElement.addContent(exportController.exportSeveritiesXML(configuration.getAllSeverities()));
		rootValidateElement.addContent(exportController.exportSeveritiesPerTypes(configuration.getAllSeveritiesPerTypes()));
		return rootValidateElement;
	}
}