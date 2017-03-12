package husaccttest.validate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import husacct.validate.ValidateServiceImpl;
import husacct.validate.domain.configuration.ActiveRuleType;
import husacct.validate.domain.configuration.ActiveViolationType;
import husacct.validate.domain.validation.Severity;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.datatype.DatatypeConfigurationException;
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

public class ImportExportWorkspaceTest {
	private ValidateServiceImpl validate;

	@Before
	public void setup() {
		validate = new ValidateServiceImpl();
	}

	@Test
	public void testExportingAndImporting() throws URISyntaxException, ParserConfigurationException, SAXException, IOException, DatatypeConfigurationException {
		testImporting();
		checkSeveritiesTheSameAsSeveritiesElement(validate.getConfiguration()
				.getAllSeverities(), validate.getWorkspaceData().getChild("severities"));
		checkSeveritiesPerTypesPerProgrammingLanguagesTheSameAsSeveritiesPerTypesPerProgrammingLanguagesElement(
				validate.getConfiguration().getAllSeveritiesPerTypesPerProgrammingLanguages(),
				validate.getWorkspaceData().getChild("severitiesPerTypesPerProgrammingLanguages"));
		checkActiveViolationTypesTheSameAsActiveViolationTypesElement(validate
				.getConfiguration().getActiveViolationTypes(), validate.getWorkspaceData().getChild("activeViolationTypes"));
	}

	private void testImporting() throws URISyntaxException, ParserConfigurationException, SAXException, IOException, DatatypeConfigurationException {
		ClassLoader.getSystemResource("husaccttest/validate/Testfile_ImportExportTest.xml").toURI();
		DocumentBuilderFactory domfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dombuilder = domfactory.newDocumentBuilder();
		File file = new File(ClassLoader.getSystemResource("husaccttest/validate/Testfile_ImportExportTest.xml").toURI());
		DOMBuilder domBuilder = new DOMBuilder();
		Document document = domBuilder.build(dombuilder.parse(file));
		validate.loadWorkspaceData(document.getRootElement());

		checkSeveritiesTheSameAsSeveritiesElement(validate.getConfiguration().getAllSeverities(),document.getRootElement().getChild("severities"));
		checkSeveritiesPerTypesPerProgrammingLanguagesTheSameAsSeveritiesPerTypesPerProgrammingLanguagesElement(
				validate.getConfiguration().getAllSeveritiesPerTypesPerProgrammingLanguages(), 
				document.getRootElement().getChild("severitiesPerTypesPerProgrammingLanguages"));

		checkActiveViolationTypesTheSameAsActiveViolationTypesElement(validate
				.getConfiguration().getActiveViolationTypes(), document.getRootElement().getChild("activeViolationTypes"));
	}

	private void checkSeveritiesTheSameAsSeveritiesElement(List<Severity> severities, Element severitiesElement) {
		assertEquals(severitiesElement.getChildren().size(), severities.size());
		
		for(int i = 0; i < severitiesElement.getChildren().size(); i++) {
			Element severityElement = severitiesElement.getChildren().get(i);
			Severity severity = severities.get(i);
			checkSeverityTheSameAsSeverityElement(severity, severityElement);
		}
	}

	private void checkSeveritiesPerTypesPerProgrammingLanguagesTheSameAsSeveritiesPerTypesPerProgrammingLanguagesElement(
			HashMap<String, HashMap<String, Severity>> severitiesPerTypesPerProgrammingLanguages,
			Element severitiesPerTypesPerProgrammingLanguagesElement) {
		assertEquals(severitiesPerTypesPerProgrammingLanguages.size(), severitiesPerTypesPerProgrammingLanguagesElement.getChildren().size());
		for(Entry<String, HashMap<String, Severity>> severityPerTypePerProgrammingLanguage : severitiesPerTypesPerProgrammingLanguages.entrySet()) {
			for(Element severityPerTypePerProgrammingLanguageElement : severitiesPerTypesPerProgrammingLanguagesElement.getChildren()) {
				if(severityPerTypePerProgrammingLanguageElement.getAttribute("language").getValue().equals(severityPerTypePerProgrammingLanguage.getKey())) {
					checkSeverityPerTypePerProgrammingLanguageTheSameAsSeverityPerTypePerProgrammingLanguageElement(
							severityPerTypePerProgrammingLanguage, severityPerTypePerProgrammingLanguageElement);
				}
			}
		}
	}

	private void checkActiveViolationTypesTheSameAsActiveViolationTypesElement(Map<String, List<ActiveRuleType>> activeViolationTypes, Element child) {
		int i = 0;
		for(Entry<String, List<ActiveRuleType>> activeViolationType : activeViolationTypes.entrySet()) {
			Element activeViolationTypeElement = child.getChildren().get(i);
			assertEquals(activeViolationType.getKey(), activeViolationTypeElement.getAttributeValue("language"));

			for(int ruleTypeIndex = 0; ruleTypeIndex < activeViolationTypeElement.getChildren().size(); ruleTypeIndex++) {
				Element activeRuleTypeElement = activeViolationTypeElement.getChildren().get(ruleTypeIndex);

				assertNotNull(containsActiveRuleType(activeRuleTypeElement.getAttributeValue("type"), activeViolationType.getValue()));

				ActiveRuleType activeRuleType = containsActiveRuleType(activeRuleTypeElement.getAttributeValue("type"), activeViolationType.getValue());
				assertEquals(activeRuleType.getRuleType(), activeRuleTypeElement.getAttributeValue("type"));

				for(Element violationTypesElement : activeRuleTypeElement
						.getChildren("violationTypes")) {
					for(int violationTypesRootIndex = 0; violationTypesRootIndex < violationTypesElement
							.getChildren().size(); violationTypesRootIndex++) {

						assertNotNull(containsActiveViolationType(
								violationTypesElement.getChildren()
								.get(violationTypesRootIndex)
								.getChildText("violationKey"),
								activeRuleType.getViolationTypes()));
						ActiveViolationType violationType = containsActiveViolationType(
								violationTypesElement.getChildren()
								.get(violationTypesRootIndex)
								.getChildText("violationKey"),
								activeRuleType.getViolationTypes());
						assertEquals(
								violationType.getType(),
								violationTypesElement.getChildren()
								.get(violationTypesRootIndex)
								.getChildText("violationKey"));
						assertEquals(
								violationType.isEnabled(),
								Boolean.parseBoolean(violationTypesElement
										.getChildren()
										.get(violationTypesRootIndex)
										.getChildText("enabled")));
					}
				}
			}
			i++;
		}
	}

	private ActiveRuleType containsActiveRuleType(String key, List<ActiveRuleType> activeViolationTypes) {
		for(ActiveRuleType activeRuleType : activeViolationTypes) {
			if(activeRuleType.getRuleType().equals(key)) {
				return activeRuleType;
			}
		}
		return null;
	}

	private ActiveViolationType containsActiveViolationType(String key, List<ActiveViolationType> activeViolationTypes) {
		for(ActiveViolationType activeViolationType : activeViolationTypes) {
			if(activeViolationType.getType().equals(key)) {
				return activeViolationType;
			}
		}
		return null;
	}

	private void checkSeverityTheSameAsSeverityElement(Severity severity, Element severityElement) {
		assertEquals(severity.getSeverityKey(),	severityElement.getChildText("severityKey"));
		assertEquals(severity.getColor(), new Color(Integer.parseInt(severityElement.getChildText("color"))));
	}

	private void checkSeverityPerTypePerProgrammingLanguageTheSameAsSeverityPerTypePerProgrammingLanguageElement(
			Entry<String, HashMap<String, Severity>> severityPerTypePerProgrammingLanguage, Element severityPerTypePerProgrammingLanguageElement) {
		assertEquals(severityPerTypePerProgrammingLanguageElement.getChildren().size(), severityPerTypePerProgrammingLanguage.getValue().size());
		
		for(Entry<String, Severity> severityPerType : severityPerTypePerProgrammingLanguage.getValue().entrySet()) {
			String severityId = findSeverityPerTypeElement(severityPerTypePerProgrammingLanguageElement, severityPerType);
			assertEquals(severityId, severityPerType.getValue().getId().toString());
		}
	}

	private String findSeverityPerTypeElement(Element severityPerTypePerProgrammingLanguageElement,	Entry<String, Severity> severityPerType) {
		for(Element severityPerTypeElement : severityPerTypePerProgrammingLanguageElement.getChildren()) {
			if(severityPerTypeElement.getChildText("typeKey").equals(severityPerType.getKey())) {
				return severityPerTypeElement.getChildText("severityId");
			}
		}
		throw new AssertionFailedError("There was an error finding a type by the key: "	+ severityPerType.getKey());
	}

}