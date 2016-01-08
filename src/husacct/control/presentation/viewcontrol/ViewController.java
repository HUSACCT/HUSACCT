package husacct.control.presentation.viewcontrol;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.control.task.MainController;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;

public class ViewController {
	
	private InternalFrameController defineContainer;
	private InternalFrameController definedArchitectureDiagramContainer;
	private InternalFrameController analysedApplicationOverviewContainer;
	private InternalFrameController analysedArchitectureDiagramContainer;
	private InternalFrameController validateContainer;
	private InternalFrameController validateConfigurationContainer;
	private InternalFrameController codeViewerContainer;
	
	private List<InternalFrameController> viewContainers = new ArrayList<InternalFrameController>();
	
	public ViewController(MainController mainController){
		defineContainer = new InternalFrameController(mainController, new ImageIcon(Resource.get(Resource.ICON_DEFINE_ARCHITECTURE)), "DefineArchitecture"){
			@Override
			public JInternalFrame getInternalFrame() {
				return ServiceProvider.getInstance().getDefineService().getDefinedGUI();
			}
		};
		
		definedArchitectureDiagramContainer = new InternalFrameController(mainController, new ImageIcon(Resource.get(Resource.ICON_DEFINE_ARCHITECTURE_DIAGRAM)), "DefinedArchitectureDiagram"){
			@Override
			public JInternalFrame getInternalFrame() {
				return ServiceProvider.getInstance().getGraphicsService().getDefinedArchitectureGUI();
			}
		};
		
		analysedApplicationOverviewContainer = new InternalFrameController(mainController, new ImageIcon(Resource.get(Resource.ICON_APPLICATION_OVERVIEW)), "AnalysedArchitectureOverview") {
			@Override
			public JInternalFrame getInternalFrame() {
				return ServiceProvider.getInstance().getAnalyseService().getJInternalFrame();
			}
		};
		
		analysedArchitectureDiagramContainer = new InternalFrameController(mainController, new ImageIcon(Resource.get(Resource.ICON_ANALYSED_ARCHITECTURE_DIAGRAM)), "AnalysedArchitectureDiagram") {
			@Override
			public JInternalFrame getInternalFrame() {
				return ServiceProvider.getInstance().getGraphicsService().getAnalysedArchitectureGUI();
			}
		};
		
		validateContainer = new InternalFrameController(mainController, new ImageIcon(Resource.get(Resource.ICON_VALIDATE)), "Validate") {
			@Override
			public JInternalFrame getInternalFrame() {
				return ServiceProvider.getInstance().getValidateService().getBrowseViolationsGUI();
			}
		};
		
		validateConfigurationContainer = new InternalFrameController(mainController, new ImageIcon(Resource.get(Resource.ICON_VALIDATE)), "Configuration") {
			@Override
			public JInternalFrame getInternalFrame() {
				return ServiceProvider.getInstance().getValidateService().getConfigurationGUI();
			}
		};
		
		codeViewerContainer = new InternalFrameController(mainController, new ImageIcon(Resource.get(Resource.ICON_SOURCE)), "ConfigGeneralCodeviewer") {
			@Override
			public JInternalFrame getInternalFrame() {
				return mainController.getCodeViewerController().getCodeViewInternalFrame();
			}
		};

		viewContainers.add(defineContainer);
		viewContainers.add(definedArchitectureDiagramContainer);
		viewContainers.add(analysedApplicationOverviewContainer);
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
	
	public void showApplicationOverviewGui() {
		analysedApplicationOverviewContainer.showView();
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
