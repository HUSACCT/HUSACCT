package husacct.externalinterface;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import husacct.ServiceProvider;
import husacct.control.IControlService;

public class ExternalServiceProvider {

	private static ExternalServiceProvider _instance;

	private ExternalServiceProvider() {
		ServiceProvider.getInstance();
		_instance = this;
	}

	public static ExternalServiceProvider getInstance() {
		setLog4jConfiguration();
		if (ExternalServiceProvider._instance == null) {
			new ExternalServiceProvider();
		}
		return ExternalServiceProvider._instance;
	}

	public static ExternalServiceProvider getInstance(Properties log4jProperties) {
		setLog4jConfiguration(log4jProperties);
		if (ExternalServiceProvider._instance == null) {
			new ExternalServiceProvider();
		}
		return ExternalServiceProvider._instance;
	}
	
	/**
	 * Provides the results of a complete Software Architecture Compliance Check (SACC), performed in batch mode.
	 * Based on the data in a HUSACCT workspace file, the source code of a project is analysed and the architectural rules checked.
	 * Next a violation report is created with the results of the SACC. Read the comments in ViolationReportDTO and ViolationImExportDTO.
	 * Optionally, a specification of the new violations may be included in the violation report.
	 * New violations are violations not found in the importFilePreviousViolations. 
	 * To reduce the number of false positives new violations, a second condition applies: the number of violations for 
	 * the related rule has to be increased. Namely, not all reported new identified violations have to be really new. 
	 * For instance, a violation may be reported as new, since the line number of the violation has changed because another statement has 
	 * been inserted. Or it may be reported as new, since the class name has changed. 
	 * A violation is identified by the following key attributes: from class, to class, line number, rule type key, 
	 * dependency type, dependency sub-type, is indirect.
	 * Finally, XML documents of all violations or only the new violations will be created if the related parameter argument are 
	 * provided. The XML documents can be used for export purposes. These documents can easily be stored as files or passed to other tools.  
	 * 
	 * @param exportNewViolations: (Optional) Indicates if an XML document with only the new current violations should be created.
	 * @return ViolationReportDTO: Read the Javadoc of this class. 
	 */
	public ViolationReportDTO performSoftwareArchitectureComplianceCheck(SaccCommandDTO saccCommandDTO) {
		ViolationReportDTO violationReport = new ViolationReportDTO();
		IControlService controlService = ServiceProvider.getInstance().getControlService();
		violationReport = controlService.performSoftwareArchitectureComplianceCheck(saccCommandDTO);
		return violationReport;
	}

	private static void setLog4jConfiguration(Properties log4jProperties) {
		PropertyConfigurator.configure(log4jProperties);
	}

	private static void setLog4jConfiguration() {
		Properties props = new Properties();
		try {
			props.load(Class.class.getResourceAsStream("/husacct/common/resources/log4j.properties"));
		} catch (IOException e) {
			System.out.println("Exception in ExternalServiceProvider line 59: Loading Log4J properties file.");
			//e.printStackTrace();
		}
		PropertyConfigurator.configure(props);
	}
	
}
