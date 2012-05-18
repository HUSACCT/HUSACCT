package husaccttest.validate;

import static org.junit.Assert.assertEquals;
import husacct.validate.ValidateServiceImpl;
import husacct.validate.domain.configuration.ActiveRuleType;
import husacct.validate.domain.configuration.ActiveViolationType;
import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.AssertionFailedError;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.DOMBuilder;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class ImportExportTest {
	private ValidateServiceImpl validate;

	@Before
	public void setup()
	{
		validate = new ValidateServiceImpl();
	}


	public void testImporting() throws URISyntaxException, ParserConfigurationException, SAXException, IOException, DatatypeConfigurationException {
		ClassLoader.getSystemResource("husaccttest/validate/testfile.xml").toURI();
		DocumentBuilderFactory domfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dombuilder = domfactory.newDocumentBuilder();
		File file = new File(ClassLoader.getSystemResource("husaccttest/validate/testfile.xml").toURI());
		DOMBuilder domBuilder = new DOMBuilder();
		Document document = domBuilder.build(dombuilder.parse(file));
		validate.loadWorkspaceData(document.getRootElement());
		checkSeveritiesTheSameAsSeveritiesElement(validate.getConfiguration().getAllSeverities(), document.getRootElement().getChild("severities"));
		checkSeveritiesPerTypesPerProgrammingLanguagesTheSameAsSeveritiesPerTypesPerProgrammingLanguagesElement(validate.getConfiguration().getAllSeveritiesPerTypesPerProgrammingLanguages(), document.getRootElement().getChild("severitiesPerTypesPerProgrammingLanguages"));
		checkViolationsTheSameAsViolationsElement(validate.getConfiguration().getAllViolations().getValue(), document.getRootElement().getChild("violations"));
		checkViolationHistoriesTheSameAsViolationHistoriesElement(validate.getConfiguration().getViolationHistories(), document.getRootElement().getChild("violationHistories"));
		checkActiveViolationTypesTheSameAsActiveViolationTypesElement(validate.getConfiguration().getActiveViolationTypes(), document.getRootElement().getChild("activeViolationTypes"));
	}

	private void checkActiveViolationTypesTheSameAsActiveViolationTypesElement(
			Map<String, List<ActiveRuleType>> activeViolationTypes, Element child) {
		int i = 0;
		for(Entry<String, List<ActiveRuleType>> activeViolationType : activeViolationTypes.entrySet()) {
			Element activeViolationTypeElement = child.getChildren().get(i);
			assertEquals(activeViolationType.getKey(), activeViolationTypeElement.getAttributeValue("language"));
			for(int ruleTypeIndex = 0; ruleTypeIndex < activeViolationTypeElement.getChildren().size(); ruleTypeIndex++) {
				ActiveRuleType activeRuleType = activeViolationType.getValue().get(ruleTypeIndex);
				Element activeRuleTypeElement = activeViolationTypeElement.getChildren().get(ruleTypeIndex);
				assertEquals(activeRuleType.getRuleType(), activeRuleTypeElement.getAttributeValue("type"));
				for(int violationTypeIndex = 0; violationTypeIndex < 0; violationTypeIndex++) {
					ActiveViolationType violationType = activeRuleType.getViolationTypes().get(violationTypeIndex);
					Element violationTypeElement = activeRuleTypeElement.getChildren().get(violationTypeIndex);
					assertEquals(violationType.getType(), violationTypeElement.getChildText("violationKey"));
					assertEquals(violationType.isEnabled(), Boolean.parseBoolean(violationTypeElement.getChildText("enabled")));
				}
			}
			i++;
		}
	}

	private void checkViolationHistoriesTheSameAsViolationHistoriesElement(
			List<ViolationHistory> violationHistories, Element child) throws DatatypeConfigurationException {
		for(int i = 0; i < child.getChildren().size(); i++) {
			Element violationHistoryElement = child.getChildren().get(i);
			ViolationHistory violationHistory = violationHistories.get(i);
			assertEquals(violationHistory.getDescription(), violationHistoryElement.getChildText("description"));
			checkViolationsTheSameAsViolationsElement(violationHistory.getViolations(), violationHistoryElement.getChild("violations"));
			checkSeveritiesTheSameAsSeveritiesElement(violationHistory.getSeverities(), violationHistoryElement.getChild("severities"));
		}
	}

	public void checkViolationsTheSameAsViolationsElement(List<Violation> violations, Element violationsElement) throws DatatypeConfigurationException {
		assertEquals(violations.size(), violationsElement.getChildren().size());
		for(int i = 0; i < violationsElement.getChildren().size(); i++) {
			Element violationElement = violationsElement.getChildren().get(i);
			Violation violation = violations.get(i);
			checkViolationTheSameAsViolationElement(violationElement, violation);
		}
	}

	public void checkSeveritiesTheSameAsSeveritiesElement(List<Severity> severities, Element severitiesElement) {
		assertEquals(severitiesElement.getChildren().size(), severities.size());
		for(int i = 0; i < severitiesElement.getChildren().size(); i++) {
			Element severityElement = severitiesElement.getChildren().get(i);
			Severity severity = severities.get(i);
			checkSeverityTheSameAsSeverityElement(severity, severityElement);
		}
	}
	//TODO assert a programming language is found like findSeverityPerTypeElement();
	public void checkSeveritiesPerTypesPerProgrammingLanguagesTheSameAsSeveritiesPerTypesPerProgrammingLanguagesElement(HashMap<String, HashMap<String, Severity>> severitiesPerTypesPerProgrammingLanguages, Element severitiesPerTypesPerProgrammingLanguagesElement) {
		assertEquals(severitiesPerTypesPerProgrammingLanguages.size(), severitiesPerTypesPerProgrammingLanguagesElement.getChildren().size());
		for(Entry<String, HashMap<String, Severity>> severityPerTypePerProgrammingLanguage : severitiesPerTypesPerProgrammingLanguages.entrySet()) {
			for(Element severityPerTypePerProgrammingLanguageElement : severitiesPerTypesPerProgrammingLanguagesElement.getChildren()) {
				if(severityPerTypePerProgrammingLanguageElement.getAttribute("language").getValue().equals(severityPerTypePerProgrammingLanguage.getKey())) {
					checkSeverityPerTypePerProgrammingLanguageTheSameAsSeverityPerTypePerProgrammingLanguageElement(severityPerTypePerProgrammingLanguage, severityPerTypePerProgrammingLanguageElement);
				}
			}
		}
	}

	public void checkViolationTheSameAsViolationElement(Element violationElement, Violation violation) throws DatatypeConfigurationException {
		assertEquals(violation.getLinenumber(), Integer.parseInt(violationElement.getChildText("lineNumber")));
		assertEquals(violation.getSeverity().getId().toString(), violationElement.getChildText("severityId"));
		assertEquals(violation.getRuletypeKey(), violationElement.getChildText("ruletypeKey"));
		assertEquals(violation.getClassPathFrom(), violationElement.getChildText("classPathFrom"));
		assertEquals(violation.getClassPathTo(), violationElement.getChildText("classPathTo"));
		checkLogicalModulesTheSameAsLogicalModulesElement(violationElement.getChild("logicalModules"), violation.getLogicalModules());
		checkMessageTheSameAsMessageElement(violationElement.getChild("message"), violation.getMessage());
		assertEquals(violation.isIndirect(), Boolean.parseBoolean(violationElement.getChildText("isIndirect")));
		assertEquals(DatatypeFactory.newInstance().newXMLGregorianCalendar((GregorianCalendar)violation.getOccured()), DatatypeFactory.newInstance().newXMLGregorianCalendar(violationElement.getChildText("occured")));
	}

	public void checkSeverityTheSameAsSeverityElement(Severity severity, Element severityElement) {
		assertEquals(severity.getDefaultName(), severityElement.getChildText("defaultName"));
		assertEquals(severity.getUserName(), severityElement.getChildText("userName"));
		assertEquals(severity.getId().toString(), severityElement.getChildText("id"));
		assertEquals(severity.getColor(), new Color(Integer.parseInt(severityElement.getChildText("color"))));
	}

	public void checkSeverityPerTypePerProgrammingLanguageTheSameAsSeverityPerTypePerProgrammingLanguageElement(Entry<String, HashMap<String, Severity>> severityPerTypePerProgrammingLanguage, Element severityPerTypePerProgrammingLanguageElement) {
		assertEquals(severityPerTypePerProgrammingLanguageElement.getChildren().size(), severityPerTypePerProgrammingLanguage.getValue().size());
		for(Entry<String, Severity> severityPerType : severityPerTypePerProgrammingLanguage.getValue().entrySet()) {
			String severityId = findSeverityPerTypeElement(severityPerTypePerProgrammingLanguageElement, severityPerType);
			assertEquals(severityId, severityPerType.getValue().getId().toString());
		}
	}

	public String findSeverityPerTypeElement(Element severityPerTypePerProgrammingLanguageElement, Entry<String, Severity> severityPerType) {
		for(Element severityPerTypeElement : severityPerTypePerProgrammingLanguageElement.getChildren()) {
			if(severityPerTypeElement.getChildText("typeKey").equals(severityPerType.getKey())) {
				return severityPerTypeElement.getChildText("severityId");
			}
		}
		throw new AssertionFailedError("There was an error finding a type by the key: " + severityPerType.getKey());
	}

	@Test
	public void testExportingAndImporting() throws URISyntaxException, ParserConfigurationException, SAXException, IOException, DatatypeConfigurationException {
		testImporting();
		checkViolationsTheSameAsViolationsElement(validate.getConfiguration().getAllViolations().getValue(), validate.getWorkspaceData().getChild("violations"));
		checkSeveritiesTheSameAsSeveritiesElement(validate.getConfiguration().getAllSeverities(), validate.getWorkspaceData().getChild("severities"));
		checkSeveritiesPerTypesPerProgrammingLanguagesTheSameAsSeveritiesPerTypesPerProgrammingLanguagesElement(validate.getConfiguration().getAllSeveritiesPerTypesPerProgrammingLanguages(), validate.getWorkspaceData().getChild("severitiesPerTypesPerProgrammingLanguages"));
		checkViolationHistoriesTheSameAsViolationHistoriesElement(validate.getConfiguration().getViolationHistories(), validate.getWorkspaceData().getChild("violationHistories"));
		checkActiveViolationTypesTheSameAsActiveViolationTypesElement(validate.getConfiguration().getActiveViolationTypes(), validate.getWorkspaceData().getChild("activeViolationTypes"));
	}

	public void checkLogicalModulesTheSameAsLogicalModulesElement(Element logicalModulesElement, LogicalModules logicalModiles) {
		assertEquals(logicalModiles.getLogicalModuleFrom().getLogicalModulePath(), logicalModulesElement.getChild("logicalModuleFrom").getChildText("logicalModulePath"));
		assertEquals(logicalModiles.getLogicalModuleFrom().getLogicalModuleType(), logicalModulesElement.getChild("logicalModuleFrom").getChildText("logicalModuleType"));
		assertEquals(logicalModiles.getLogicalModuleTo().getLogicalModulePath(), logicalModulesElement.getChild("logicalModuleTo").getChildText("logicalModulePath"));
		assertEquals(logicalModiles.getLogicalModuleTo().getLogicalModuleType(), logicalModulesElement.getChild("logicalModuleTo").getChildText("logicalModuleType"));
	}

	public void checkMessageTheSameAsMessageElement(Element messageElement, Message message) {
		checkLogicalModulesTheSameAsLogicalModulesElement(messageElement.getChild("logicalModules"), message.getLogicalModules());
		assertEquals(message.getRuleKey(), messageElement.getChildText("ruleKey"));
		for(int i = 0; i < message.getViolationTypeKeys().size(); i++) {
			assertEquals(message.getViolationTypeKeys().get(i), messageElement.getChild("violationTypeKeys").getChildren().get(i).getText());
		}
		for(int i = 0; i < message.getExceptionMessage().size(); i++){
			checkMessageTheSameAsMessageElement(messageElement.getChild("exceptionMessages").getChildren().get(i), message.getExceptionMessage().get(i));
		}
	}
}
