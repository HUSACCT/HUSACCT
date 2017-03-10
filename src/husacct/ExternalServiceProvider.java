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
	 * Provides the results of a complete Software Architecture Compliance Check, performed in batch mode.
	 * New violations are violations not found in the importFilePreviousViolations AND if
	 * the number of violations has increased for the rule (the last to reduce the number of false positives).
	 * Not all reported new identified violations have to be “really” new. For instance, a violation may be 
	 * reported as new, since the line number of the violation has changed because another statement has 
	 * been inserted. Or it may be reported as new, since the class name has changed. 
	 * A violation is identified by the following key attributes: from class, to class, line number, rule type key, 
	 * dependency type, dependency sub-type, is indirect.
	 * 
	 * @param exportFileNewViolations TODO
	 * @param husacctWorkspaceFile: Refers to a file that contains the definition of the intended architecture (modules, rules, assigned software units, ...).
	 * @param importFilePreviousViolations: (Optional) Path of a file containing a set of previous violations. Used to determine new violations.
	 * @param exportFileAllCurrentViolations: (Optional) Path where an export file with all current violations can be stored. 
	 * @param exportFileNewViolations: (Optional) Path where an export file with all new violations can be stored. 
	 * @return ViolationReportDTO
	 */
	public ViolationReportDTO performSoftwareArchitectureComplianceCheck(String husacctWorkspaceFile, 
			String importFilePreviousViolations, String exportFileAllCurrentViolations, String exportFileNewViolations) {
		ViolationReportDTO violationReport = new ViolationReportDTO();
		IControlService controlService = ServiceProvider.getInstance().getControlService();
		violationReport = controlService.performSoftwareArchitectureComplianceCheck(husacctWorkspaceFile, 
				importFilePreviousViolations, exportFileAllCurrentViolations, exportFileNewViolations);
		return violationReport;
	}

}
