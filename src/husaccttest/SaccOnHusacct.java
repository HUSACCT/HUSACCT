package husaccttest;

import static org.junit.Assert.assertTrue;
import husacct.ExternalServiceProvider;
import husacct.common.dto.ViolationImExportDTO;
import husacct.common.dto.ViolationReportDTO;
import husaccttest.TestResourceFinder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class SaccOnHusacct {
	// Refers to a files that contains the definition of the intended architecture (modules, rules, assigned software units, ...).
	private static final String workspacePath = 
			TestResourceFinder.getSaccFolder("java") + "HUSACCT_Workspace_Current_Architecture.xml";
	// Refers to a file containing a set of previous violations. Used to determine new violations.
	private static final String importFilePathAllPreviousViolations =
			TestResourceFinder.getSaccFolder("java") + "HUSACCT_ArchitectureViolations_All_ImportFile.xml";
	// Indicates if an XML document with all current violations should be created.
	private static final boolean exportAllViolations = true;
	// Indicates if an XML document with only the new current violations should be created.
	private static final boolean exportNewViolations = false;

	private static ViolationReportDTO violationReport = null;
	private static Logger logger = Logger.getLogger(SaccOnHusacct.class);


	@BeforeClass
	public static void beforeClass() {
		try {
			setLog4jConfiguration();
			logger.info(String.format(" Test started: SaccOnHusacct_ViaExternalServiceProvider"));
			ExternalServiceProvider externalServiceProvider = ExternalServiceProvider.getInstance();
			violationReport = externalServiceProvider.performSoftwareArchitectureComplianceCheck(workspacePath, 
					importFilePathAllPreviousViolations, exportAllViolations, exportNewViolations);
		} catch (Exception e){
			logger.warn("Exception: " + e.getCause().toString());
		}
	}

	@AfterClass
	public static void tearDown(){
		logger.info(String.format(" Test finished: SaccOnHusacct_ViaExternalServiceProvider"));
	}
	
	@Test
	public void hasNumberOfViolationsIncreased() {
		boolean numberOfViolationsHasNotIncreased = true;
		assertTrue(violationReport != null);
		if (violationReport != null) {
			logger.info(" Previous number of violations: " + violationReport.getNrOfAllPreviousViolations() 
					+ "  At: " + getFormattedDate(violationReport.getTimePreviousCheck()));
			logger.info(" Current number of violations: " + violationReport.getNrOfAllCurrentViolations());
			if (violationReport.getNrOfAllCurrentViolations() > violationReport.getNrOfAllPreviousViolations()) {
				numberOfViolationsHasNotIncreased = false;
			}
			/* Activate to renew the previous violations file. Only temporarily by one person, to prevent merging problems.  
			if (violationReport.getNrOfAllCurrentViolations() < violationReport.getNrOfAllPreviousViolations()) {
				replaceImportFileAllPreviousViolations();
			}
			*/
		}
		assertTrue(numberOfViolationsHasNotIncreased);
	}
	
	@SuppressWarnings("unused")
	private void replaceImportFileAllPreviousViolations() {
		if (importFilePathAllPreviousViolations != null) {
			File importFileAllPreviousViolations = new File(importFilePathAllPreviousViolations);
			if (violationReport.getExportDocAllViolations() != null) {
				if (importFileAllPreviousViolations.exists()) {
					try {
						importFileAllPreviousViolations.delete();
					} catch (SecurityException exception){
						logger.warn(String.format(" Cannot delete importFilePathAllPreviousViolations " + exception.getCause().toString()));
					}
					// Create new importFileAllPreviousViolations with contents of exportFileAllViolations
					XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
					try {
						FileWriter fileWriter = new FileWriter(importFilePathAllPreviousViolations);
						outputter.output(violationReport.getExportDocAllViolations(), fileWriter);
						fileWriter.close();
					} catch (IOException exception){
						logger.warn(String.format(" Cannot create new importFilePathAllPreviousViolations " + exception.getCause().toString()));
					}
					logger.warn(String.format(" Replaced: importFileAllPreviousViolations"));
				}
			}
		}
	}


	@Test
	public void areNewArchitecturalViolationsDetected() {
		if (violationReport != null) {
			if (violationReport.getNrOfNewViolations() > 0) {
				logger.info(" New architectural violations detected! Number of new violations = " + violationReport.getNrOfNewViolations());
				for (ViolationImExportDTO newViolation : violationReport.getNewViolations()) {
					logger.info(" Violation in class: " + newViolation.getFrom() + " Line: " + newViolation.getLine() + " Message: " + newViolation.getMessage());
				}
			} else {
				logger.info(" No new architectural violations detected!");
			}
		}
	}
	
	
	private static void setLog4jConfiguration() {
		URL propertiesFile = Class.class.getResource("/husacct/common/resources/log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
	}
	
	private static String getFormattedDate(Calendar calendar) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
		return dateFormat.format(calendar.getTime());
	}


}
