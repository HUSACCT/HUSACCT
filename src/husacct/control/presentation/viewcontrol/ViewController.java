package husacct.control.presentation.viewcontrol;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.control.task.MainController;

public class ViewController {
	private InternalFrameController defineContainer;
	private InternalFrameController definedArchitectureDiagramContainer;
	// private InternalFrameController moduleAndRuleDiagramContainer; //Disabled in vs 5.1; waiting for improvement
	private InternalFrameController analysedApplicationOverviewContainer;
	private InternalFrameController analysedArchitectureDiagramContainer;
	private InternalFrameController validateContainer;
	private InternalFrameController validateConfigurationContainer;
	private InternalFrameController codeViewerContainer;
	private InternalFrameController analyseSARController;
	
	private List<InternalFrameController> viewContainers = new ArrayList<>();

	public ViewController(MainController mainController){

		defineContainer = new InternalFrameController(mainController, new ImageIcon(Resource.get(Resource.ICON_DEFINE_ARCHITECTURE)), "DefineArchitecture"){
			@Override
			public JInternalFrame getNewInternalFrame() {
				return ServiceProvider.getInstance().getDefineService().getDefinedGUI();
			}
		};
		
		definedArchitectureDiagramContainer = new InternalFrameController(mainController, new ImageIcon(Resource.get(Resource.ICON_DEFINE_ARCHITECTURE_DIAGRAM)), "DefinedArchitectureDiagram"){
			@Override
			public JInternalFrame getNewInternalFrame() {
				return ServiceProvider.getInstance().getGraphicsService().getDefinedArchitectureGUI();
			}
		};
		
		/*
		moduleAndRuleDiagramContainer = new InternalFrameController(mainController, new ImageIcon(Resource.get(Resource.ICON_DEFINE_ARCHITECTURE_DIAGRAM)), "ModuleAndRuleDiagram"){
			@Override
			public JInternalFrame getInternalFrame() {
				return ServiceProvider.getInstance().getGraphicsService().getModuleAndRuleGUI();
			}
		}; */
		
		analysedApplicationOverviewContainer = new InternalFrameController(mainController, new ImageIcon(Resource.get(Resource.ICON_APPLICATION_OVERVIEW)), "AnalysedArchitectureOverview") {
			@Override
			public JInternalFrame getNewInternalFrame() {
				return ServiceProvider.getInstance().getAnalyseService().getAnalyseFrame();
			}
		};
		
		analyseSARController = new InternalFrameController(mainController, new ImageIcon(Resource.get(Resource.ICON_APPLICATION_OVERVIEW)), "SoftwareArchitectureReconstruction") {
			@Override
			public JInternalFrame getNewInternalFrame() {
				return ServiceProvider.getInstance().getAnalyseService().getSARDialog(mainController.getMainGui());
			}
		};
		
		analysedArchitectureDiagramContainer = new InternalFrameController(mainController, new ImageIcon(Resource.get(Resource.ICON_ANALYSED_ARCHITECTURE_DIAGRAM)), "AnalysedArchitectureDiagram") {
			@Override
			public JInternalFrame getNewInternalFrame() {
				return ServiceProvider.getInstance().getGraphicsService().getAnalysedArchitectureGUI();
			}
		};
		
		validateContainer = new InternalFrameController(mainController, new ImageIcon(Resource.get(Resource.ICON_VALIDATE)), "Validate") {
			@Override
			public JInternalFrame getNewInternalFrame() {
				return ServiceProvider.getInstance().getValidateService().getBrowseViolationsGUI();
			}
		};
		
		validateConfigurationContainer = new InternalFrameController(mainController, new ImageIcon(Resource.get(Resource.ICON_VALIDATE)), "Configuration") {
			@Override
			public JInternalFrame getNewInternalFrame() {
				return ServiceProvider.getInstance().getValidateService().getConfigurationGUI();
			}
		};
		
		codeViewerContainer = new InternalFrameController(mainController, new ImageIcon(Resource.get(Resource.ICON_SOURCE)), "ConfigGeneralCodeviewer") {
			@Override
			public JInternalFrame getNewInternalFrame() {
				return mainController.getCodeViewerController().getCodeViewInternalFrame();
			}
		};

		viewContainers.add(defineContainer);
		viewContainers.add(definedArchitectureDiagramContainer);
		viewContainers.add(analysedApplicationOverviewContainer);
		// viewContainers.add(moduleAndRuleDiagramContainer);
		viewContainers.add(analyseSARController);
		viewContainers.add(analysedArchitectureDiagramContainer);
		viewContainers.add(validateContainer);
		viewContainers.add(validateConfigurationContainer);
		viewContainers.add(codeViewerContainer);
	}
	
	public void showDefineArchitecture() {
		defineContainer.showView();
	}
	
	public void showDefinedArchitectureDiagram() {
		definedArchitectureDiagramContainer.showView();
		ServiceProvider.getInstance().getGraphicsService().drawDefinedArchitecture();
	}
	
	/*
	public void showModuleAndRuleDiagram() {
		moduleAndRuleDiagramContainer.showView();
		ServiceProvider.getInstance().getGraphicsService().drawModuleAndRuleArchitecture();
	} */
	
	public void showApplicationOverviewGui() {
		analysedApplicationOverviewContainer.showView();
	}
	
	public void showAnalyseSarGui() {
		// Iconify all shown InternalFrames
		for (InternalFrameController ifController : viewContainers) {
			JInternalFrame internalFrame = ifController.getInternalFrame();
			if ((internalFrame != null) && internalFrame.isShowing()) {
				if (!internalFrame.isIcon()) {
					ifController.iconifyInternalFrame();
				}
			}
		}
		// Show the Define frame in its restored size
		if (!defineContainer.getNewInternalFrame().isShowing()) {
			defineContainer.showView();
		}
		defineContainer.restoreInternalFrame();
		// Show the AnalyseSAR frame in its restored size (the size en position are set in the frame's overridden setBounds().
		analyseSARController.showView();
		analyseSARController.restoreInternalFrame();
	}
	
	public void showAnalysedArchitectureDiagram() {	
		analysedArchitectureDiagramContainer.showView();
		ServiceProvider.getInstance().getGraphicsService().drawAnalysedArchitecture();
	}
	
	public void showValidateGui() {
		validateContainer.showView();
	}
	
	public void showConfigurationGui() {
		validateConfigurationContainer.showView();
	}
	
	public void showCodeViewer() {
		codeViewerContainer.showView();
	}
	
	public void closeAll(){		
		for(InternalFrameController container : viewContainers){
			container.closeFrame();
		}
		InternalFrameController.resetLastStartPosition();
	}
	
	public void setLocaleListeners(){
		for(InternalFrameController container : viewContainers){
			container.setLocaleListener();
		}
	}
}
