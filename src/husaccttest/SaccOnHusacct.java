package husaccttest;

import static org.junit.Assert.assertTrue;
import husacct.externalinterface.ExternalServiceProvider;
import husacct.externalinterface.SaccCommandDTO;
import husacct.externalinterface.ViolationImExportDTO;
import husacct.externalinterface.ViolationReportDTO;
import husaccttest.TestResourceFinder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeSet;

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

	private static SaccCommandDTO saccCommandDTO;
	private static ViolationReportDTO violationReport = null;
	private static Logger logger = Logger.getLogger(SaccOnHusacct.class);


	@BeforeClass
	public static void beforeClass() {
		try {
			setLog4jConfiguration();
			logger.info(String.format(" Test started: SaccOnHusacct_ViaExternalServiceProvider"));

			saccCommandDTO = new SaccCommandDTO();
			saccCommandDTO.setHusacctWorkspaceFile(workspacePath);
			ArrayList<String> paths = new ArrayList<>();
			paths.add("src/husacct");
			saccCommandDTO.setSourceCodePaths(paths);
			saccCommandDTO.setImportFilePreviousViolations(importFilePathAllPreviousViolations);
			saccCommandDTO.setExportAllViolations(true);
			saccCommandDTO.setExportNewViolations(false);
			
			ExternalServiceProvider externalServiceProvider = ExternalServiceProvider.getInstance();
			violationReport = externalServiceProvider.performSoftwareArchitectureComplianceCheck(saccCommandDTO);
		} catch (Exception e){
			logger.error("Exception: " + e.getMessage());
			//e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDown(){
		logger.info(String.format(" Test finished: SaccOnHusacct_ViaExternalServiceProvider"));
	}
	
	@Test
	public void T1_isSourceCodeAnalysedSuccessfully() {
		boolean numberOfDependenciesNotZero = false;
		assertTrue(violationReport != null);
		if (violationReport != null) {
			if (violationReport.getNrOfAllCurrentDependencies() > 0) {
				numberOfDependenciesNotZero = true;
			}
		}
		assertTrue(numberOfDependenciesNotZero);
	}
	
	@Test
	public void T2_hasNumberOfViolationsIncreased() {
		boolean numberOfViolationsHasNotIncreased = true;
		assertTrue(violationReport != null);
		if (violationReport != null) {
			System.out.println(" SACC results:");
			System.out.println(" Previous number of violations: " + violationReport.getNrOfAllPreviousViolations() 
					+ "  At: " + getFormattedDate(violationReport.getTimePreviousCheck()));
			System.out.println(" Current number of violations: " + violationReport.getNrOfAllCurrentViolations());
			if (violationReport.getNrOfAllCurrentViolations() > violationReport.getNrOfAllPreviousViolations()) {
				numberOfViolationsHasNotIncreased = false;
			}
			/* Activate to renew the previous violations file. Only temporarily by one person, to prevent merging problems.  
			if (violationReport.getNrOfAllCurrentViolations() < violationReport.getNrOfAllPreviousViolations()) {
				replaceImportFileAllPreviousViolations();
			}
			*/
		}
		// Report on new architecture violations 
		if (violationReport != null) {
			if (violationReport.getNrOfNewViolations() > 0) {
				System.out.println(" New architectural violations detected! Number of new violations = " + violationReport.getNrOfNewViolations());
				TreeSet<String> messageAndFromClassSet = new TreeSet<>();
				int numberOfPrintLines = 0;
				ViolationImExportDTO[] newViolations = violationReport.getNewViolations();
				for (ViolationImExportDTO newViolation : newViolations) {
					String key = newViolation.getMessage() + newViolation.getFrom();
					if (!messageAndFromClassSet.contains(key)) {
						messageAndFromClassSet.add(key);
						if (numberOfPrintLines <= 25) {
							System.out.println(" Violated rule: " + newViolation.getMessage() + "; Violating class: " + newViolation.getFrom());
							numberOfPrintLines ++;
						} else {
							System.out.println(" More violations detected; study ViolationReportDTO.newViolations");
							break;
						}
					}
				}
			} else {
				System.out.println(" No new architectural violations detected!");
			}
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
						logger.warn(String.format(" Cannot delete importFilePathAllPreviousViolations " + exception.getMessage()));
					}
					// Create new importFileAllPreviousViolations with contents of exportFileAllViolations
					XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
					try {
						FileWriter fileWriter = new FileWriter(importFilePathAllPreviousViolations);
						outputter.output(violationReport.getExportDocAllViolations(), fileWriter);
						fileWriter.close();
					} catch (IOException exception){
						logger.warn(String.format(" Cannot create new importFilePathAllPreviousViolations " + exception.getMessage()));
					}
					logger.warn(String.format(" Replaced: importFileAllPreviousViolations"));
				}
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
