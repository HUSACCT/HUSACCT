package husacct;

import husacct.common.dto.ViolationReportDTO;
import husacct.control.IControlService;

public class ExternalServiceProvider {

	private static ExternalServiceProvider _instance;

	private ExternalServiceProvider() {
		ServiceProvider.getInstance();
		_instance = this;
	}

	public static ExternalServiceProvider getInstance() {
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
	 * @param husacctWorkspaceFile: Refers to a file that contains the definition of the intended architecture (modules, rules, assigned software units, ...) of the project.
	 * @param importFilePreviousViolations: (Optional) Path of a previous exportFileAllCurrentViolations. 
	 * 		  Based on this input, new violations can be determined.
	 * @param exportAllViolations: (Optional) Indicates if an XML document with all current violations should be created.
	 * @param exportNewViolations: (Optional) Indicates if an XML document with only the new current violations should be created.
	 * @return ViolationReportDTO
	 */
	public ViolationReportDTO performSoftwareArchitectureComplianceCheck(String husacctWorkspaceFile, 
			String importFilePreviousViolations, boolean exportAllViolations, boolean exportNewViolations) {
		ViolationReportDTO violationReport = new ViolationReportDTO();
		IControlService controlService = ServiceProvider.getInstance().getControlService();
		violationReport = controlService.performSoftwareArchitectureComplianceCheck(husacctWorkspaceFile, 
				importFilePreviousViolations, exportAllViolations, exportNewViolations);
		return violationReport;
	}

}
