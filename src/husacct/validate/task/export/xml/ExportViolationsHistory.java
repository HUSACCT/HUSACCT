package husacct.validate.task.export.xml;

import husacct.validate.domain.validation.ViolationHistory;

import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Logger;
import org.jdom2.Attribute;
import org.jdom2.Element;

public class ExportViolationsHistory extends XmlExportUtils {

	public Element exportViolationsHistory(List<ViolationHistory> violationHistories) {
		final ExportSeverities exportSeverities = new ExportSeverities();
		final ExportViolations exportViolations = new ExportViolations();
		
		Element violationHistoriesElement = new Element("violationHistories");
		
			for(ViolationHistory violationHistory : violationHistories) {
				Element violationHistoryElement = new Element("violationHistory");
				violationHistoriesElement.addContent(violationHistoryElement);
				
				//date
				try {
					violationHistoryElement.setAttribute(new Attribute("date", DatatypeFactory.newInstance().newXMLGregorianCalendar((GregorianCalendar)violationHistory.getDate()).toString()));
				} catch (DatatypeConfigurationException e) {
					Logger.getLogger(ExportViolationsHistory.class).warn("A date of a violation could not be set");
				}
				
				//description
				violationHistoryElement.addContent(createElementWithContent("description", violationHistory.getDescription()));

				//severities
				violationHistoryElement.addContent(exportSeverities.exportSeverities(violationHistory.getSeverities()));
				
				//violations
				violationHistoryElement.addContent(exportViolations.exportViolations(violationHistory.getViolations()));
			}
		
		return violationHistoriesElement;
	}
}