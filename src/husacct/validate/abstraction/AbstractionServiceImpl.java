package husacct.validate.abstraction;

import javax.xml.datatype.DatatypeConfigurationException;

import husacct.validate.abstraction.fetch.ImportController;
import husacct.validate.domain.ConfigurationServiceImpl;

import org.jdom2.input.DOMBuilder;
import org.w3c.dom.Element;

public class AbstractionServiceImpl {
	private final ConfigurationServiceImpl configuration;
	
	public AbstractionServiceImpl(ConfigurationServiceImpl configuration){
		this.configuration = configuration;
	}

	public void importValidationWorkspace(Element element) throws DatatypeConfigurationException  {
		org.jdom2.Element jDomElement = new DOMBuilder().build(element);
		ImportController importController = new ImportController(jDomElement);
		configuration.addSeverities(importController.getSeverities());
		configuration.addViolations(importController.getViolations());
		configuration.setSeveritiesPerRuleTypes(importController.getSeveritiesPerRuleTypes());
	}
}