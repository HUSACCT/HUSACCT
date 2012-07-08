package husacct.control.task;

import husacct.ServiceProvider;
import husacct.common.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;

public class ViewController {
	
	private AbstractViewContainer defineContainer;
	private AbstractViewContainer definedArchitectureDiagramContainer;
	private AbstractViewContainer analysedApplicationOverviewContainer;
	private AbstractViewContainer analysedArchitectureDiagramContainer;
	private AbstractViewContainer validateContainer;
	private AbstractViewContainer validateConfigurationContainer;
	
	private List<AbstractViewContainer> viewContainers = new ArrayList<AbstractViewContainer>();
	
	public ViewController(MainController mainController){
		defineContainer = new AbstractViewContainer(mainController, new ImageIcon(Resource.get(Resource.ICON_DEFINE_ARCHITECTURE)), "DefineArchitecture"){
			@Override
			public JInternalFrame getInternalFrame() {
				return ServiceProvider.getInstance().getDefineService().getDefinedGUI();
			}
		};
		
		definedArchitectureDiagramContainer = new AbstractViewContainer(mainController, new ImageIcon(Resource.get(Resource.ICON_DEFINE_ARCHITECTURE_DIAGRAM)), "DefinedArchitectureDiagram"){
			@Override
			public JInternalFrame getInternalFrame() {
				return ServiceProvider.getInstance().getGraphicsService().getDefinedArchitectureGUI();
			}
		};
		
		analysedApplicationOverviewContainer = new AbstractViewContainer(mainController, new ImageIcon(Resource.get(Resource.ICON_APPLICATION_OVERVIEW)), "AnalysedArchitectureOverview") {
			@Override
			public JInternalFrame getInternalFrame() {
				return ServiceProvider.getInstance().getAnalyseService().getJInternalFrame();
			}
		};
		
		analysedArchitectureDiagramContainer = new AbstractViewContainer(mainController, new ImageIcon(Resource.get(Resource.ICON_ANALYSED_ARCHITECTURE_DIAGRAM)), "AnalysedArchitectureDiagram") {
			@Override
			public JInternalFrame getInternalFrame() {
				return ServiceProvider.getInstance().getGraphicsService().getAnalysedArchitectureGUI();
			}
		};
		
		validateContainer = new AbstractViewContainer(mainController, new ImageIcon(Resource.get(Resource.ICON_VALIDATE)), "Validate") {
			@Override
			public JInternalFrame getInternalFrame() {
				return ServiceProvider.getInstance().getValidateService().getBrowseViolationsGUI();
			}
		};
		
		validateConfigurationContainer = new AbstractViewContainer(mainController, new ImageIcon(Resource.get(Resource.ICON_VALIDATE)), "Configuration") {
			@Override
			public JInternalFrame getInternalFrame() {
				return ServiceProvider.getInstance().getValidateService().getConfigurationGUI();
			}
		};
		
		viewContainers.add(defineContainer);
		viewContainers.add(definedArchitectureDiagramContainer);
		viewContainers.add(analysedApplicationOverviewContainer);
		viewContainers.add(analysedArchitectureDiagramContainer);
		viewContainers.add(validateContainer);
		viewContainers.add(validateConfigurationContainer);
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
	
	public void closeAll(){		
		for(AbstractViewContainer container : viewContainers){
			container.closeFrame();
		}
	}
}
