package husacct.validate.task.export.xml;

import husacct.validate.domain.validation.Violation;

import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdom2.Element;

public class ExportViolations extends XmlExportUtils {

	public Element exportViolations(List<Violation> violations) {
		Element violationsElement = new Element("violations");
		for(Violation violation : violations) {
			Element violationElement = new Element("violation");

			violationElement.addContent(createElementWithContent("lineNumber", "" + violation.getLinenumber()));
			violationElement.addContent(createElementWithContent("severityId", "" + violation.getSeverity().getId().toString()));
			violationElement.addContent(createElementWithContent("ruletypeKey", violation.getRuletypeKey()));			
			violationElement.addContent(createElementWithContent("violationtypeKey",violation.getViolationtypeKey()));
			violationElement.addContent(createElementWithContent("classPathFrom",violation.getClassPathFrom()));
			violationElement.addContent(createElementWithContent("classPathTo",violation.getClassPathTo()));
			violationElement.addContent(createLogicalModulesElement(violation.getLogicalModules()));
			violationElement.addContent(createMessageElementFromMessageObject(violation.getMessage()));
			violationElement.addContent(createElementWithContent("isIndirect",""+violation.isIndirect()));
			try {
				violationElement.addContent(createElementWithContent("occured", DatatypeFactory.newInstance().newXMLGregorianCalendar((GregorianCalendar)violation.getOccured()).toXMLFormat()));
			} catch (DatatypeConfigurationException e) {
				Logger.getLogger(ExportViolations.class.getName()).log(Level.ERROR, "There was a error creating a new date in ExportViolations", e);
			}
			violationsElement.addContent(violationElement);
		}
		return violationsElement;
	}
}