package husacct.control.task;

import husacct.ServiceProvider;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ProjectDTO;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class AnalyseTask implements Runnable {

	private Logger logger = Logger.getLogger(AnalyseTask.class);

	private MainController mainController;
	private ApplicationDTO applicationDTO;

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
			mainController.getStateController().setAnalysing(true);
			mainController.getStateController().setPreAnalysed(false);
			Thread.sleep(1);
			logger.debug("Analysing application");
			// ServiceProvider.getInstance().resetAnalyseService();
			if (applicationDTO.projects.size() > 0) {
				
				for (int i = 0; i < applicationDTO.projects.size(); i++) {
					ProjectDTO currentProject = applicationDTO.projects.get(i);

					ServiceProvider.getInstance().getAnalyseService().analyseApplication(currentProject);

					// Add analysed root modules to project
					currentProject.analysedModules = new ArrayList<AnalysedModuleDTO>();
					AnalysedModuleDTO[] analysedRootModules = ServiceProvider
							.getInstance().getAnalyseService().getRootModules();
					for (AnalysedModuleDTO analysedModule : analysedRootModules) {
						currentProject.analysedModules.add(analysedModule);
					}

					// Update project with analysedRootModules
					applicationDTO.projects.remove(i);
					applicationDTO.projects.add(i, currentProject);
				}
			}
			
			logger.debug("Analysing finished");
			if (!mainController.getStateController().isAnalysing()) {
				ServiceProvider.getInstance().resetAnalyseService();
			}
			mainController.getStateController().setAnalysing(false);
			// ServiceProvider.getInstance().getDefineService().isReAnalyzed();
		} catch (InterruptedException exception) {
			logger.debug("RESETTING ANALYSE SERVICE");
			ServiceProvider.getInstance().resetAnalyseService();
			mainController.getStateController().setAnalysing(false);

		}
	}

}
