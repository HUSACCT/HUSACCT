package husaccttest;

import static org.junit.Assert.assertTrue;
import husacct.ExternalServiceProvider;
import husacct.common.dto.ViolationImExportDTO;
import husacct.common.dto.ViolationReportDTO;
import husaccttest.TestResourceFinder;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class SaccOnHusacct {
	// Refers to a files that contains the definition of the intended architecture (modules, rules, assigned software units, ...).
	private static final String workspacePath = 
			TestResourceFinder.getSaccFolder("java") 
			+ "HUSACCT_Current_Architecture.xml";
	// Refers to a file containing a set of previous violations. Used to determine new violations.
	private static final String importFilePathAllPreviousViolations =
			TestResourceFinder.getSaccFolder("java") 
			+ "HUSACCT_ArchitectureViolations_All_ImportFile.xml";
	// Path of export file with all current violations. This file can be produced, optionally.
	private static final String exportFilePathAllViolations = 
			TestResourceFinder.getExportFolderForTest("java") 
			+ "HUSACCT_ArchitectureViolations_All_ExportFile.xml";
	// Path of export file with only the new current violations. This file can be produced, optionally.
	private static final String exportFilePathNewViolations = 
			TestResourceFinder.getExportFolderForTest("java") 
			+ "HUSACCT_ArchitectureViolations_OnlyNew_ExportFile.xml";

	private static ViolationReportDTO violationReport = null;
	private static Logger logger = Logger.getLogger(SaccOnHusacct.class);


	@BeforeClass
	public static void beforeClass() {
		try {
			setLog4jConfiguration();
			logger.info(String.format(" Test started: SaccOnHusacct_ViaExternalServiceProvider"));
			ExternalServiceProvider externalServiceProvider = ExternalServiceProvider.getInstance();
			violationReport = externalServiceProvider.performSoftwareArchitectureComplianceCheck(workspacePath, 
					importFilePathAllPreviousViolations, exportFilePathAllViolations, exportFilePathNewViolations);
		} catch (Exception e){
			logger.warn("Exception: " + e.getCause().toString());
		}
	}

	@AfterClass
	public static void tearDown(){
		// Note: Do not delete the created files if you want to use the exported files after the test.
		File exportFileAllViolations = new File(exportFilePathAllViolations);
		exportFileAllViolations.delete();
		File exportFileNewViolations = new File(exportFilePathNewViolations);
		exportFileNewViolations.delete();
		logger.info(String.format(" Test finished: SaccOnHusacct_ViaExternalServiceProvider"));
	}
	
	@Test
	public void hasNumberOfViolationsIncreased() {
		boolean numberOfViolationsHasIncreased = false;
		assertTrue(violationReport != null);
		if (violationReport != null) {
			logger.info(" Previous number of violations: " + violationReport.getNrOfAllPreviousViolations() 
					+ "  At: " + violationReport.getTimePreviousCheck());
			logger.info(" Current number of violations: " + violationReport.getNrOfAllCurrentViolations());
			if (violationReport.getNrOfAllCurrentViolations() <= violationReport.getNrOfAllPreviousViolations()) {
				numberOfViolationsHasIncreased = true;
			}
			if (violationReport.getNrOfAllCurrentViolations() < violationReport.getNrOfAllPreviousViolations()) {
				replaceImportFileAllPreviousViolations();
			}
		}
		assertTrue(numberOfViolationsHasIncreased);
	}
	
	private void replaceImportFileAllPreviousViolations() {
		try {
			File exportFileAllViolations = new File(exportFilePathAllViolations);
			if (exportFileAllViolations.exists()) {
				// Get XML document from exportFileAllViolations
				SAXBuilder sax = new SAXBuilder();
				Document document = new Document();
				document = sax.build(exportFileAllViolations);
				// Delete existing importFileAllPreviousViolations
				File importFileAllPreviousViolations = new File(importFilePathAllPreviousViolations);
				importFileAllPreviousViolations.delete();
				// Create new importFileAllPreviousViolations with contents of exportFileAllViolations
				XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
				FileWriter fileWriter = new FileWriter(importFilePathAllPreviousViolations);
				outputter.output(document, fileWriter);
				fileWriter.close();
				logger.warn(String.format(" Replaced: importFileAllPreviousViolations"));
			}
		} catch (Exception exception){
			logger.warn(String.format(" Unable to replace importFileAllPreviousViolations: " + exception.getCause().toString()));
		}
	}


	@Test
	public void areNewArchitecturalViolationsDetected() {
		if (violationReport != null) {
			if (violationReport.getNrOfNewViolations() > 0) {
				logger.info(" New architectural violations detected! Number of new violations = " + violationReport.getNrOfNewViolations());
			} else {
				logger.info(" No new architectural violations detected!");
			}
			for (ViolationImExportDTO newViolation : violationReport.getNewViolations()) {
				logger.info(" Violation in class: " + newViolation.getFrom() + " Line: " + newViolation.getLine() + " Message: " + newViolation.getMessage());
			}
		}
		//assertTrue((violationReport != null) && (violationReport.getNrOfNewViolations() <= 0));
		
	}
	
	
	private static void setLog4jConfiguration() {
		URL propertiesFile = Class.class.getResource("/husacct/common/resources/log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
	}
	
}
