package husacct.control;

import java.util.List;

import husacct.common.dto.ApplicationDTO;
import husacct.common.services.IObservableService;
import husacct.control.task.States;
import husacct.control.task.threading.ThreadWithLoader;

import javax.swing.JDialog;

public interface IControlService extends IObservableService{

	public void parseCommandLineArguments(String[] commandLineArguments);
	public void startApplication();
	public void showErrorMessage(String message);
	public void showInfoMessage(String message);
	public void centerDialog(JDialog dialog);
	public ThreadWithLoader getThreadWithLoader(String progressInfoText, Runnable threadTask);
	public void setServiceListeners();

	public List<States> getState();
	public void updateProgress(int progressPercentage);
	public boolean isPreAnalysed();
	public void finishPreAnalysing();
	
	public void setValidate(boolean validate);
	
	public ApplicationDTO getApplicationDTO();
}
