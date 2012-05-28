package husacct.validate.task.export.xml;

import husacct.validate.domain.validation.Violation;
import husacct.validate.task.XMLUtils;

import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdom2.Element;

public class ExportViolations {

	public Element exportViolations(List<Violation> violations) {
		Element violationsElement = new Element("violations");
		for(Violation violation : violations) {
			Element violationElement = new Element("violation");

			violationElement.addContent(XMLUtils.createElementWithContent("lineNumber", "" + violation.getLinenumber()));
			violationElement.addContent(XMLUtils.createElementWithContent("severityId", "" + violation.getSeverity().getId().toString()));
			violationElement.addContent(XMLUtils.createElementWithContent("ruletypeKey", violation.getRuletypeKey()));			
			violationElement.addContent(XMLUtils.createElementWithContent("violationtypeKey",violation.getViolationtypeKey()));
			violationElement.addContent(XMLUtils.createElementWithContent("classPathFrom",violation.getClassPathFrom()));
			violationElement.addContent(XMLUtils.createElementWithContent("classPathTo",violation.getClassPathTo()));
			violationElement.addContent(XMLUtils.createLogicalModulesElement(violation.getLogicalModules()));
			violationElement.addContent(XMLUtils.createMessageElementFromMessageObject(violation.getMessage()));
			violationElement.addContent(XMLUtils.createElementWithContent("isIndirect",""+violation.isIndirect()));
			try {
				violationElement.addContent(XMLUtils.createElementWithContent("occured", DatatypeFactory.newInstance().newXMLGregorianCalendar((GregorianCalendar)violation.getOccured()).toXMLFormat()));
			} catch (DatatypeConfigurationException e) {
				Logger.getLogger(ExportViolations.class.getName()).log(Level.ERROR, "There was a error creating a new date in ExportViolations", e);
			}
			violationsElement.addContent(violationElement);
		}
		return violationsElement;
	}

}
