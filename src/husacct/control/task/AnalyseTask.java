package husacct.control.task;

import husacct.ServiceProvider;
import husacct.common.dto.AnalysisStatisticsDTO;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.control.presentation.util.LoadingDialog;

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
			if ((this.applicationDTO.projects.size() > 0) && (this.applicationDTO.projects.get(0).paths.size() > 0)) {
				this.mainController.getStateController().setAnalysing(true);
				Thread.sleep(1);
				LoadingDialog loadingDialog = mainController.getApplicationController().getCurrentLoadingDialog();
				if (loadingDialog != null) {
					loadingDialog.setAmountOfProcesses(this.applicationDTO.projects.size());
				}
				for (int i = 0; i < this.applicationDTO.projects.size(); i++) {
					ProjectDTO currentProject = this.applicationDTO.projects.get(i);
					if (currentProject.paths.size() > 0) {
						if (loadingDialog != null) {
							loadingDialog.setCurrentProcess(i);
						}
						this.logger.info(new Date().toString() + " Control-AnalyseTask is Starting: Analyse project " + currentProject);
						mainController.getActionLogController().addAction("Analysing project " + currentProject);
						
						ServiceProvider.getInstance().getAnalyseService().analyseApplication(currentProject);
						
						// Add analysed root modules to project
						currentProject.analysedModules = new ArrayList<SoftwareUnitDTO>();
						SoftwareUnitDTO[] analysedRootModules = ServiceProvider.getInstance().getAnalyseService().getSoftwareUnitsInRoot();
						for (SoftwareUnitDTO analysedModule : analysedRootModules) {
							currentProject.analysedModules.add(analysedModule);
						}
	
						// Update project with analysedRootModules
						this.applicationDTO.projects.remove(i);
						this.applicationDTO.projects.add(i, currentProject);
					}
				}
				mainController.getWorkspaceController().getCurrentWorkspace().setApplicationData(applicationDTO);
				ServiceProvider.getInstance().getDefineService().analyze();

				AnalysisStatisticsDTO statistics = ServiceProvider.getInstance().getAnalyseService().getAnalysisStatistics(null);
				logger.info(new Date().toString() + " Finished: Analyse application. Added: " + statistics.totalNrOfPackages + " packages; " + statistics.totalNrOfClasses + " classes; " + statistics.totalNrOfDependencies + " dependencies");
				mainController.getActionLogController().addAction("Analysing finished, added: " + statistics.totalNrOfPackages + " packages; " + statistics.totalNrOfClasses + " classes; " + statistics.totalNrOfDependencies + " dependencies");
				
				String workspaceName = mainController.getWorkspaceController().getCurrentWorkspace().getName();
				ServiceProvider.getInstance().getAnalyseService().logHistory(applicationDTO, workspaceName);
				if (!mainController.getStateController().isAnalysing()) {
					ServiceProvider.getInstance().resetAnalyseService();
				}
				this.mainController.getStateController().setAnalysing(false);
				this.mainController.getViewController().showApplicationOverviewGui();

			} else {
				logger.info(new Date().toString() + " No project specified, or no project path specified");
			}
		} catch (InterruptedException exception) {
			this.logger.debug("RESETTING ANALYSE SERVICE");
			ServiceProvider.getInstance().resetAnalyseService();
			this.mainController.getStateController().setAnalysing(false);
		}
	}
}
