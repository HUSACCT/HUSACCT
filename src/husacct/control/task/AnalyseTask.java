package husacct.control.task;

import husacct.ServiceProvider;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ProjectDTO;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class AnalyseTask implements Runnable {

	private final Logger logger = Logger.getLogger(AnalyseTask.class);

	private final MainController mainController;
	private final ApplicationDTO applicationDTO;

	public AnalyseTask(MainController mainController,
			ApplicationDTO applicationDTO) {
		this.applicationDTO = applicationDTO;
		this.mainController = mainController;
	}

	@Override
	public void run() {
		// Thread.sleep added to support InterruptedException catch
		// InterruptedException is not yet implemented by analyse
		// Therefore this thread can never be interrupted.
		try {
			this.mainController.getStateController().setAnalysing(true);
			this.mainController.getStateController().setPreAnalysed(false);
			Thread.sleep(1);
			this.logger.debug("Analysing application");
			if (this.applicationDTO.projects.size() > 0) {
				this.mainController.getApplicationController().getCurrentLoader().setAmountOfProcesses(this.applicationDTO.projects.size());
				
				for (int i = 0; i < this.applicationDTO.projects.size(); i++) {

					ProjectDTO currentProject = this.applicationDTO.projects.get(i);
					ServiceProvider.getInstance().getAnalyseService().analyseApplication(currentProject);
					
					this.mainController.getApplicationController().getCurrentLoader().setCurrentProcess(i);

					// Add analysed root modules to project
					currentProject.analysedModules = new ArrayList<AnalysedModuleDTO>();
					AnalysedModuleDTO[] analysedRootModules = ServiceProvider.getInstance().getAnalyseService().getRootModules();
					for (AnalysedModuleDTO analysedModule : analysedRootModules) {
						currentProject.analysedModules.add(analysedModule);
					}

					//ServiceProvider.getInstance().getAnalyseService().analyseApplication(currentProject);
					
					// Update project with analysedRootModules
					this.applicationDTO.projects.remove(i);
					this.applicationDTO.projects.add(i, currentProject);
				}
			}
			
			mainController.getWorkspaceController().getCurrentWorkspace().setApplicationData(applicationDTO);
			
			logger.debug("Analysing finished");
			
			String workspaceName = mainController.getWorkspaceController().getCurrentWorkspace().getName();
			ServiceProvider.getInstance().getAnalyseService().logHistory(applicationDTO, workspaceName);
			
			if (!mainController.getStateController().isAnalysing()) {
				ServiceProvider.getInstance().resetAnalyseService();
			}
			this.mainController.getStateController().setAnalysing(false);
			// ServiceProvider.getInstance().getDefineService().isReAnalyzed();
		} catch (InterruptedException exception) {
			this.logger.debug("RESETTING ANALYSE SERVICE");
			ServiceProvider.getInstance().resetAnalyseService();
			this.mainController.getStateController().setAnalysing(false);

		}
	}
}
