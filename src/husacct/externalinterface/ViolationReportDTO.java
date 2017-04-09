package husacct.externalinterface;

import husacct.common.dto.AbstractDTO;

import java.util.Calendar;

import org.jdom2.Document;

// Used to report the results of a software architecture compliance check (SACC)
public class ViolationReportDTO extends AbstractDTO {
	private int nrOfAllCurrentDependencies; 		// Total number of dependencies during the current SACC.
	private int nrOfAllCurrentViolations; 			// Total number of violations during the current SACC.
	private int nrOfAllPreviousViolations; 			// Total number of violations during the previous SACC.
	private int nrOfNewViolations; 					// Number of new violations during the current SACC.
	private Calendar timeCurrentCheck;				// Time of the current SACC.
	private Calendar timePreviousCheck;				// Time of the previous SACC. Its result are used to determine the new violations.
	private ViolationImExportDTO[] allViolations; 	// Contains all violations detected during current SACC.
	private ViolationImExportDTO[] newViolations; 	// Contains only new violations detected during current SACC.
	private Document exportDocAllViolations;		// XML document containing all new violations detected during current SACC. Option, so may be null!
	private Document exportDocNewViolations;		// XML document containing only new violations detected during current SACC. Option, so may be null!
	
	public ViolationReportDTO() {
		nrOfAllCurrentViolations = 0;
		nrOfNewViolations = 0;
		timeCurrentCheck = Calendar.getInstance();
		timePreviousCheck = Calendar.getInstance();
		allViolations = new ViolationImExportDTO[0];
		newViolations = new ViolationImExportDTO[0];
	}
	
	/**
	 * @return int nrOfAllCurrentDependencies = Total number of dependencies during the current SACC.
	 */
	public int getNrOfAllCurrentDependencies() {
		return nrOfAllCurrentDependencies;
	}

	public void setNrOfAllCurrentDependencies(int nrOfAllDependencies) {
		this.nrOfAllCurrentDependencies = nrOfAllDependencies;
	}

	/**
	 * @return int nrOfAllCurrentViolations = Total number of violations during the current SACC.
	 */
	public int getNrOfAllCurrentViolations() {
		return nrOfAllCurrentViolations;
	}

	public void setNrOfAllCurrentViolations(int nrOfAllViolations) {
		this.nrOfAllCurrentViolations = nrOfAllViolations;
	}

	/**
	 * @return int nrOfAllPreviousViolations = Total number of violations during the previous SACC.
	 */
	public int getNrOfAllPreviousViolations() {
		return nrOfAllPreviousViolations;
	}

	public void setNrOfAllPreviousViolations(int nrOfAllPreviousViolations) {
		this.nrOfAllPreviousViolations = nrOfAllPreviousViolations;
	}

	/**
	 * @return int nrOfNewViolations = Number of new violations during the current SACC.
	 */
	public int getNrOfNewViolations() {
		return nrOfNewViolations;
	}

	public void setNrOfNewViolations(int nrOfNewViolations) {
		this.nrOfNewViolations = nrOfNewViolations;
	}

	/**
	 * @return Calendar timeCurrentCheck = Time of the current SACC.
	 */
	public Calendar getTimeCurrentCheck() {
		return timeCurrentCheck;
	}

	public void setTimeCurrentCheck(Calendar timeCurrentCheck) {
		this.timeCurrentCheck = timeCurrentCheck;
	}

	/**
	 * @return Calendar timePreviousCheck = Time of the previous SACC. 
	 * Its result are used to determine the new violations.
	 */
	public Calendar getTimePreviousCheck() {
		return timePreviousCheck;
	}

	public void setTimePreviousCheck(Calendar timePreviousCheck) {
		this.timePreviousCheck = timePreviousCheck;
	}

	/**
	 * @return ViolationImExportDTO[] allViolations = Contains all violations detected during current SACC.
	 */
	public ViolationImExportDTO[] getAllViolations() {
		return allViolations;
	}

	public void setAllViolations(ViolationImExportDTO[] allViolations) {
		this.allViolations = allViolations;
	}

	/**
	 * @return ViolationImExportDTO[] newViolations = Contains only new violations detected during current SACC.
	 */
	public ViolationImExportDTO[] getNewViolations() {
		return newViolations;
	}

	public void setNewViolations(ViolationImExportDTO[] newViolations) {
		this.newViolations = newViolations;
	}

	/**
	 * @return org.jdom2.Document exportDocAllViolations = XML document containing all new violations detected during current SACC. May be null!
	 */
	public Document getExportDocAllViolations() {
		return exportDocAllViolations;
	}

	public void setExportDocAllViolations(Document exportDocAllViolations) {
		this.exportDocAllViolations = exportDocAllViolations;
	}

	/**
	 * @return org.jdom2.Document exportDocNewViolations = XML document containing only new violations detected during current SACC. May be null!
	 */
	public Document getExportDocNewViolations() {
		return exportDocNewViolations;
	}

	public void setExportDocNewViolations(Document exportDocNewViolations) {
		this.exportDocNewViolations = exportDocNewViolations;
	}
}
