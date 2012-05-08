package husacct.validate.task.export.xml;

import husacct.validate.domain.validation.Violation;
import husacct.validate.task.XMLUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdom2.Attribute;
import org.jdom2.Element;

public class ExportViolationsHistory {

	public Element exportViolationsHistory(Map<Calendar, List<Violation>> violationHistory) {
		Element violationHistoryElement = new Element("violationHistory");
		for(Entry<Calendar, List<Violation>> violations : violationHistory.entrySet()) {
			try {
				Element violationsElement = new Element("violations");
				violationHistoryElement.addContent(violationsElement);
				violationsElement.setAttribute(new Attribute("date", DatatypeFactory.newInstance().newXMLGregorianCalendar((GregorianCalendar)violations.getKey()).toString()));
				for(Violation violation : violations.getValue()) {
					Element violationElement = new Element("violation");

					XMLUtils.createElementWithContent("lineNumber", "" + violation.getLinenumber(), violationElement);
					XMLUtils.createElementWithContent("severityId", "" + violation.getSeverity().getId().toString(), violationElement);
					XMLUtils.createElementWithContent("ruletypeKey", violation.getRuletypeKey(), violationElement);
					XMLUtils.createElementWithContent("violationtypeKey",violation.getViolationtypeKey(), violationElement);
					XMLUtils.createElementWithContent("classPathFrom",violation.getClassPathFrom(), violationElement);
					XMLUtils.createElementWithContent("classPathTo",violation.getClassPathTo(), violationElement);
					XMLUtils.createLogicalModulesElement(violation.getLogicalModules(), violationElement);
					XMLUtils.addMessage(violationElement, violation.getMessage());
					XMLUtils.createElementWithContent("isIndirect",""+violation.isIndirect(), violationElement);
					try {
						XMLUtils.createElementWithContent("occured", DatatypeFactory.newInstance().newXMLGregorianCalendar((GregorianCalendar)violation.getOccured()).toXMLFormat(), violationElement);
					} catch (DatatypeConfigurationException e) {
						Logger.getLogger(ExportViolations.class.getName()).log(Level.ERROR, "There was a error creating a new date in ExportViolations", e);
					}
					violationsElement.addContent(violationElement);
				}
				
			} catch (DatatypeConfigurationException e) {
				Logger.getLogger(ExportViolationsHistory.class).log(Level.FATAL, "Error creating calendar in importviolationhistory");
			}
		}
		return violationHistoryElement;
	}

}
