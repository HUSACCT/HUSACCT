package husacct.control.task;

import husacct.ServiceProvider;
import husacct.common.dto.ApplicationDTO;
import husacct.control.presentation.util.AboutHusacctFrame;
import husacct.control.presentation.util.SetApplicationFrame;

public class ApplicationController {

	private MainController mainController;
	public ApplicationController(MainController mainController) {
		this.mainController = mainController;
	}

	public void showApplicationDetailsGui(){
		new SetApplicationFrame(mainController);
	}
	
	public void setApplicationData(ApplicationDTO applicationDTO) {
		ServiceProvider.getInstance().getDefineService().createApplication(
				applicationDTO.name, 
				applicationDTO.paths, 
				applicationDTO.programmingLanguage, 
				applicationDTO.version
		);
		ServiceProvider.getInstance().getAnalyseService().analyseApplication();
	}
	
	public void showAboutHusacctGui(){
		new AboutHusacctFrame();
	}
}