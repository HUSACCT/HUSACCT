package husacct.validate.task.export.xml;

import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.task.XMLUtils;

import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdom2.Attribute;
import org.jdom2.Element;

public class ExportViolationsHistory {

	public Element exportViolationsHistory(List<ViolationHistory> violationHistories) {
		Element violationHistoriesElement = new Element("violationHistories");
		try {
			for(ViolationHistory violationHistory : violationHistories) {
				Element violationHistoryElement = XMLUtils.createElementWithoutContent("violationHistory", violationHistoriesElement);

				//date
				violationHistoryElement.setAttribute(new Attribute("date", DatatypeFactory.newInstance().newXMLGregorianCalendar((GregorianCalendar)violationHistory.getDate()).toString()));
				
				//description
				XMLUtils.createElementWithContent("description", violationHistory.getDescription(), violationHistoryElement);

				//severities
				Element severitiesElement = XMLUtils.createElementWithoutContent("severities", violationHistoryElement);
				for(Severity severity : violationHistory.getSeverities()) {
					Element severityElement = XMLUtils.createElementWithoutContent("severity", severitiesElement);
					XMLUtils.createElementWithContent("defaultName", severity.getDefaultName(), severityElement);
					XMLUtils.createElementWithContent("userName", severity.getUserName(), severityElement);
					XMLUtils.createElementWithContent("id", "" + severity.getId().toString(), severityElement);
					XMLUtils.createElementWithContent("color", "" + severity.getColor().getRGB(), severityElement);
				}

				//violations
				Element violationsElement = XMLUtils.createElementWithoutContent("violations", violationHistoryElement);
				for(Violation violation : violationHistory.getViolations()) {
					Element violationElement = XMLUtils.createElementWithoutContent("violation", violationsElement);

					XMLUtils.createElementWithContent("lineNumber", "" + violation.getLinenumber(), violationElement);
					XMLUtils.createElementWithContent("severityId", "" + violation.getSeverity().getId().toString(), violationElement);
					XMLUtils.createElementWithContent("ruletypeKey", violation.getRuletypeKey(), violationElement);
					XMLUtils.createElementWithContent("violationtypeKey",violation.getViolationtypeKey(), violationElement);
					XMLUtils.createElementWithContent("classPathFrom",violation.getClassPathFrom(), violationElement);
					XMLUtils.createElementWithContent("classPathTo",violation.getClassPathTo(), violationElement);
					XMLUtils.createLogicalModulesElement(violation.getLogicalModules(), violationElement);
					XMLUtils.addMessage(violationElement, violation.getMessage());
					XMLUtils.createElementWithContent("isIndirect",""+violation.isIndirect(), violationElement);
					XMLUtils.createElementWithContent("occured", DatatypeFactory.newInstance().newXMLGregorianCalendar((GregorianCalendar)violation.getOccured()).toXMLFormat(), violationElement);

				}
			}
		} catch (Exception e) {
			Logger.getLogger(ExportViolationsHistory.class).log(Level.FATAL, e.getMessage());
		}
		return violationHistoriesElement;
	}

}
