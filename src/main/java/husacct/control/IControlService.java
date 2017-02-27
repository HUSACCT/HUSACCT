package husacct.control;

import husacct.common.dto.ApplicationDTO;
import husacct.common.services.IObservableService;
import husacct.control.task.IFileChangeListener;
import husacct.control.task.States;
import husacct.control.task.threading.ThreadWithLoader;
import husacct.validate.domain.validation.Severity;

import java.awt.Component;
import java.util.HashMap;
import java.util.List;

import javax.swing.JDialog;

public interface IControlService extends IObservableService{

	void parseCommandLineArguments(String[] commandLineArguments);
	void startApplication();
	void showErrorMessage(String message);
	void showInfoMessage(String message);
	void centerDialog(JDialog dialog);
	ThreadWithLoader getThreadWithLoader(String progressInfoText, Runnable threadTask);
	void setServiceListeners();

	List<States> getState();
	void updateProgress(int progressPercentage);
	
	void setValidate(boolean validate);
	
	ApplicationDTO getApplicationDTO();
	
	void displayErrorsInFile(String fileName, HashMap<Integer, Severity> errors);
	void displayErrorInFile(String fileName, int lineNumber, Severity severity);
	
	void showHelpDialog(Component component);
	
	void addProjectForListening(String path);
	void addFileChangeListener(IFileChangeListener listener);
	
	String showMojoExportImportDialog(boolean isExport);
}
