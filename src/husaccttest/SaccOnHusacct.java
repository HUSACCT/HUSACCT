package husaccttest;

import static org.junit.Assert.assertTrue;
import husacct.ExternalServiceProvider;
import husacct.common.dto.ViolationImExportDTO;
import husacct.common.dto.ViolationReportDTO;
import husaccttest.TestResourceFinder;

import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class SaccOnHusacct {
	// Refers to a files that contains the definition of the intended architecture (modules, rules, assigned software units, ...).
	private static final String workspacePath = 
			TestResourceFinder.getSaccFolder("java") 
			+ "HUSACCT_Current_Architecture.xml";
	// Refers to a file containing a set of previous violations. Used to determine new violations.
	private static final String importFilePathAllViolations =
			TestResourceFinder.getSaccFolder("java") 
			+ "ArchitectureViolations_All_ImportFile" + "." + "xml";
	// Path of export file with all current violations. This file can be produced, optionally.
	private static final String exportFilePathAllViolations = 
			TestResourceFinder.getExportFolderForTest("java") 
			+ "ArchitectureViolations_All_ExportFile" + "." + "xml";
	// Path of export file with only the new current violations. This file can be produced, optionally.
	private static final String exportFilePathNewViolations = 
			TestResourceFinder.getExportFolderForTest("java") 
			+ "ArchitectureViolations_OnlyNew_ExportFile" + "." + "xml";

	private static ViolationReportDTO violationReport = null;
	private static Logger logger = Logger.getLogger(SaccOnHusacct.class);


	@BeforeClass
	public static void beforeClass() {
		try {
			setLog4jConfiguration();
			logger.info(String.format(" Test started: SaccOnHusacct_ViaExternalServiceProvider"));
			ExternalServiceProvider externalServiceProvider = ExternalServiceProvider.getInstance();
			violationReport = externalServiceProvider.performSoftwareArchitectureComplianceCheck(workspacePath, importFilePathAllViolations, exportFilePathAllViolations, exportFilePathNewViolations);

		} catch (Exception e){
			String errorMessage =  "Exception: " + e.getCause().toString();
			logger.warn(errorMessage);
		}
	}

	@AfterClass
	public static void tearDown(){
		logger.info(String.format(" Test finished: SaccOnHusacct_ViaExternalServiceProvider"));
	}
	
	@Test
	public void hasNumberOfViolationsIncreased() {
		if (violationReport != null) {
			logger.info(" Previous number of violations: " + violationReport.getNrOfAllPreviousViolations() + "  At: " + violationReport.getTimePreviousCheck());
			logger.info(" Current number of violations: " + violationReport.getNrOfAllCurrentViolations());
		}
		assertTrue((violationReport != null) && (violationReport.getNrOfAllCurrentViolations() <= violationReport.getNrOfAllPreviousViolations()));
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
		assertTrue((violationReport != null) && (violationReport.getNrOfNewViolations() <= 0));
	}
	
	
	private static void setLog4jConfiguration() {
		URL propertiesFile = Class.class.getResource("/husacct/common/resources/log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
	}
	
}
