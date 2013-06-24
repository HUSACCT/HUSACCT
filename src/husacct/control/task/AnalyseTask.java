package husacct.control.task;

import husacct.ServiceProvider;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ProjectDTO;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

public class AnalyseTask implements Runnable {

	private final Logger logger = Logger.getLogger(AnalyseTask.class);

	private final MainController mainController;
	private final ApplicationDTO applicationDTO;

	public AnalyseTask(MainController mainController, ApplicationDTO applicationDTO) {
		this.applicationDTO = applicationDTO;
		this.mainController = mainController;
	}

	@Override
	public void run() {
		try {
			this.mainController.getStateController().setAnalysing(true);
			this.mainController.getStateController().setPreAnalysed(false);
			Thread.sleep(1);
			this.logger.debug("Analysing application");
			mainController.getActionLogController().addAction("Analysing application");
			if (this.applicationDTO.projects.size() > 0) {
				this.mainController.getApplicationController().getCurrentLoader().setAmountOfProcesses(this.applicationDTO.projects.size());
				
				for (int i = 0; i < this.applicationDTO.projects.size(); i++) {

					this.mainController.getApplicationController().getCurrentLoader().setCurrentProcess(i);
					
					ProjectDTO currentProject = this.applicationDTO.projects.get(i);
					ServiceProvider.getInstance().getAnalyseService().analyseApplication(currentProject);
					
					// Add analysed root modules to project
					currentProject.analysedModules = new ArrayList<AnalysedModuleDTO>();
					AnalysedModuleDTO[] analysedRootModules = ServiceProvider.getInstance().getAnalyseService().getRootModules();
					for (AnalysedModuleDTO analysedModule : analysedRootModules) {
						currentProject.analysedModules.add(analysedModule);
					}

					// Update project with analysedRootModules
					this.applicationDTO.projects.remove(i);
					this.applicationDTO.projects.add(i, currentProject);
				}
			}
			
			mainController.getWorkspaceController().getCurrentWorkspace().setApplicationData(applicationDTO);
			
			logger.debug("Analysing finished");
			mainController.getActionLogController().addAction("Analysing finished");
			
			
			if (!mainController.getStateController().isAnalysing()) {
				ServiceProvider.getInstance().resetAnalyseService();
			}
			this.mainController.getStateController().setAnalysing(false);

			logger.debug(new Date().toString() + ": Building cache");
			mainController.getActionLogController().addAction("Building cache");
			
			int cacheSize = ServiceProvider.getInstance().getAnalyseService().buildCache();
			
			logger.debug(new Date().toString() + ": Cache is ready and filled with " + cacheSize + " dependencies");
			mainController.getActionLogController().addAction("Cache is ready and filled with " + cacheSize + " dependencies");
			
			String workspaceName = mainController.getWorkspaceController().getCurrentWorkspace().getName();
			ServiceProvider.getInstance().getAnalyseService().logHistory(applicationDTO, workspaceName);
		} catch (InterruptedException exception) {
			this.logger.debug("RESETTING ANALYSE SERVICE");
			ServiceProvider.getInstance().resetAnalyseService();
			this.mainController.getStateController().setAnalysing(false);

		}
	}
}
