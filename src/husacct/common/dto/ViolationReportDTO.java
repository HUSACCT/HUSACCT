package husacct.common.dto;

import java.util.Calendar;

// Used to report the results of a software architecture compliance check (SACC)
public class ViolationReportDTO extends AbstractDTO {
	private int nrOfAllCurrentViolations; 			// Total number of violations during the current SACC.
	private int nrOfAllPreviousViolations; 			// Total number of violations during the previous SACC.
	private int nrOfNewViolations; 					// Number of new violations during the current SACC.
	private Calendar timeCurrentCheck;				// Time of the current SACC.
	private Calendar timePreviousCheck;				// Time of the previous SACC. Its result are used to determine the new violations.
	private ViolationImExportDTO[] allViolations; 	// Contains all violations detected during current SACC.
	private ViolationImExportDTO[] newViolations; 	// Contains all new violations detected during current SACC.
	
	public ViolationReportDTO() {
		nrOfAllCurrentViolations = 0;
		nrOfNewViolations = 0;
		timeCurrentCheck = Calendar.getInstance();
		timePreviousCheck = Calendar.getInstance();
		allViolations = new ViolationImExportDTO[0];
		newViolations = new ViolationImExportDTO[0];
	}
	
	public int getNrOfAllCurrentViolations() {
		return nrOfAllCurrentViolations;
	}
	public void setNrOfAllCurrentViolations(int nrOfAllViolations) {
		this.nrOfAllCurrentViolations = nrOfAllViolations;
	}
	public int getNrOfAllPreviousViolations() {
		return nrOfAllPreviousViolations;
	}
	public void setNrOfAllPreviousViolations(int nrOfAllPreviousViolations) {
		this.nrOfAllPreviousViolations = nrOfAllPreviousViolations;
	}
	public int getNrOfNewViolations() {
		return nrOfNewViolations;
	}
	public void setNrOfNewViolations(int nrOfNewViolations) {
		this.nrOfNewViolations = nrOfNewViolations;
	}
	public Calendar getTimeCurrentCheck() {
		return timeCurrentCheck;
	}
	public void setTimeCurrentCheck(Calendar timeCurrentCheck) {
		this.timeCurrentCheck = timeCurrentCheck;
	}
	public Calendar getTimePreviousCheck() {
		return timePreviousCheck;
	}
	public void setTimePreviousCheck(Calendar timePreviousCheck) {
		this.timePreviousCheck = timePreviousCheck;
	}
	public ViolationImExportDTO[] getAllViolations() {
		return allViolations;
	}
	public void setAllViolations(ViolationImExportDTO[] allViolations) {
		this.allViolations = allViolations;
	}
	public ViolationImExportDTO[] getNewViolations() {
		return newViolations;
	}
	public void setNewViolations(ViolationImExportDTO[] newViolations) {
		this.newViolations = newViolations;
	}
}
