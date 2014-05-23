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
			if (this.applicationDTO.projects.size() > 0) {
				this.mainController.getApplicationController().getCurrentLoader().setAmountOfProcesses(this.applicationDTO.projects.size());
				
				for (int i = 0; i < this.applicationDTO.projects.size(); i++) {

					this.mainController.getApplicationController().getCurrentLoader().setCurrentProcess(i);
					
					ProjectDTO currentProject = this.applicationDTO.projects.get(i);
					
					this.logger.info(new Date().toString() + " Control-AnalyseTask is Starting: Analyse project " + currentProject);
					mainController.getActionLogController().addAction("Analysing project " + currentProject);
					
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
			ServiceProvider.getInstance().getControlService().finishPreAnalysing();
			logger.info(new Date().toString() + " Control-AnalyseTask is Starting: getDefineService().analyze()");
			ServiceProvider.getInstance().getDefineService().analyze();

			logger.info(new Date().toString() + " Control-AnalyseTask has Finished: Analyse application; state isAnalyzing=false");
			logger.info(new Date().toString() + " Added: " + ServiceProvider.getInstance().getAnalyseService().getAmountOfPackages() + " packages; " + ServiceProvider.getInstance().getAnalyseService().getAmountOfClasses() + " classes; " + ServiceProvider.getInstance().getAnalyseService().getAmountOfInterfaces() + " interfaces");
			int nrOfDependencies = ServiceProvider.getInstance().getAnalyseService().getAmountOfDependencies();
			logger.info(new Date().toString() + " Added: " + nrOfDependencies + " dependencies");
			mainController.getActionLogController().addAction("Analysing finished, added: " + ServiceProvider.getInstance().getAnalyseService().getAmountOfPackages() + " packages; " + ServiceProvider.getInstance().getAnalyseService().getAmountOfClasses() + " classes; " + ServiceProvider.getInstance().getAnalyseService().getAmountOfInterfaces() + " interfaces; " + ServiceProvider.getInstance().getAnalyseService().getAmountOfDependencies() + " dependencies");
			
			String workspaceName = mainController.getWorkspaceController().getCurrentWorkspace().getName();
			ServiceProvider.getInstance().getAnalyseService().logHistory(applicationDTO, workspaceName);
			if (!mainController.getStateController().isAnalysing()) {
				ServiceProvider.getInstance().resetAnalyseService();
			}
			this.mainController.getStateController().setAnalysing(false);

		} catch (InterruptedException exception) {
			this.logger.debug("RESETTING ANALYSE SERVICE");
			ServiceProvider.getInstance().resetAnalyseService();
			this.mainController.getStateController().setAnalysing(false);

		}
	}
}
