package husacct.control;

import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ViolationReportDTO;
import husacct.common.enums.States;
import husacct.common.services.IObservableService;
import husacct.control.task.IFileChangeListener;
import husacct.control.task.threading.ThreadWithLoader;
import husacct.validate.domain.validation.Severity;

import java.awt.Component;
import java.util.HashMap;
import java.util.List;

import javax.swing.JDialog;

public interface IControlService extends IObservableService{

	public void parseCommandLineArguments(String[] commandLineArguments);
	public void startApplication();
	public void showErrorMessage(String message);
	public void showInfoMessage(String message);
	public void centerDialog(JDialog dialog);
	public ThreadWithLoader getThreadWithLoader(String progressInfoText, Runnable threadTask);
	public void setServiceListeners();

	public List<States> getStates();
	public void updateProgress(int progressPercentage);
	
	public void setValidating(boolean validate);
	
	public ApplicationDTO getApplicationDTO();
	
	public void displayErrorsInFile(String fileName, HashMap<Integer, Severity> errors);
	public void displayErrorInFile(String fileName, int lineNumber, Severity severity);
	
	public void showHelpDialog(Component component);
	
	public void addProjectForListening(String path);
	public void addFileChangeListener(IFileChangeListener listener);
	
	public String showMojoExportImportDialog(boolean isExport);
	public boolean isGuiEnabled();
	public ViolationReportDTO performSoftwareArchitectureComplianceCheck(
			String husacctWorkspaceFile, String importFilePreviousViolations,
			boolean exportAllViolations, boolean exportNewViolations);
}
