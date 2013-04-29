package husacct.control.task;

import husacct.control.domain.Workspace;
import husacct.control.presentation.log.AnalysisHistoryOverviewFrame;

import org.apache.log4j.Logger;

public class LogController {

	private Logger logger = Logger.getLogger(LogController.class);
	private Workspace currentWorkspace;

	private MainController mainController;
	
	public LogController(MainController mainController){
		this.mainController = mainController;
		currentWorkspace = null;
	}

	public void showApplicationAnalysisHistoryOverview() {
		new AnalysisHistoryOverviewFrame(mainController);
	}
	
	public Workspace getCurrentWorkspace(){
		return currentWorkspace;
	}
}
