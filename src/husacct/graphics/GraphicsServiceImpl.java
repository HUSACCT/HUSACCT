package husacct.graphics;

import husacct.common.savechain.ISaveable;
import husacct.common.services.ObservableService;
import husacct.graphics.presentation.GraphicsPresentationController;
import husacct.graphics.task.DrawingController;
import husacct.graphics.task.DrawingTypesEnum;
import husacct.graphics.task.modulelayout.ModuleLayoutsEnum;

import javax.swing.JInternalFrame;

import org.apache.log4j.Logger;
import org.jdom2.Element;

public class GraphicsServiceImpl extends ObservableService implements IGraphicsService, ISaveable {
	
	private GraphicsPresentationController presentationControllersAnalysed;
	private GraphicsPresentationController presentationControllersDefined;
	protected Logger			logger							= Logger.getLogger(GraphicsServiceImpl.class);
	
	public static final String	workspaceServiceName			= "ArchitecureGraphicsService";
	
	public static final String	workspaceAnalysedControllerName	= "analysedController";
	
	public static final String	workspaceDefinedControllerName	= "definedController";
	
	public static final String	workspaceShowDependencies		= "showDependencies";
	
	public static final String	workspaceShowViolations			= "showViolations";
	
	public static final String	workspaceSmartLines				= "smartLines";
	
	public static final String	workspaceLayoutStrategy			= "layoutStrategy";
	
	public GraphicsServiceImpl() {
		
	}
	
	@Override
	public void drawAnalysedArchitecture() {
		createPresentationControllerAnalysed();
		presentationControllersAnalysed.drawArchitectureTopLevel();
	}
	
	@Override
	public void drawDefinedArchitecture() {
		createPresentationControllerDefined();
		presentationControllersDefined.drawArchitectureTopLevel();
	}
	
	@Override
	public JInternalFrame getAnalysedArchitectureGUI() {
		createPresentationControllerAnalysed();
		return presentationControllersAnalysed.getGraphicsFrame();
	}
	
	@Override
	public JInternalFrame getDefinedArchitectureGUI() {
		createPresentationControllerDefined();
		return presentationControllersDefined.getGraphicsFrame();
	}
	
	private void createPresentationControllerAnalysed() {
		if (presentationControllersAnalysed == null) {
			presentationControllersAnalysed = new GraphicsPresentationController(DrawingTypesEnum.IMPLEMENTED_ARCHITECTURE);
		}
	}
	
	private void createPresentationControllerDefined() {
		if (presentationControllersDefined == null) {
			presentationControllersDefined = new GraphicsPresentationController(DrawingTypesEnum.INTENDED_ARCHITECTURE);
		}
	}
	
	private void createControllers() {
		createPresentationControllerAnalysed();
		createPresentationControllerDefined();
	}
	
	@Override
	public Element getWorkspaceData() {
		createControllers();
		Element data = new Element(workspaceServiceName);
		data.addContent(getWorkspaceDataForController(workspaceAnalysedControllerName, presentationControllersAnalysed));
		data.addContent(getWorkspaceDataForController(workspaceDefinedControllerName, presentationControllersDefined));
		return data;
	}
	
	private Element getWorkspaceDataForController(String controllerName, GraphicsPresentationController controller) {
		Element controllerElement = new Element(controllerName);
		controllerElement.setAttribute(workspaceShowDependencies, "" + controller.areDependenciesShown());
		controllerElement.setAttribute(workspaceShowViolations, "" + controller.areViolationsShown());
		controllerElement.setAttribute(workspaceSmartLines, "" + controller.areSmartLinesOn());
		controllerElement.setAttribute(workspaceSmartLines, "" + controller.areSmartLinesOn());
		controllerElement.setAttribute(workspaceLayoutStrategy, controller.getLayoutStrategy().toString());
		return controllerElement;
	}
	
	private boolean isActive(Element controllerElement, String attribute) {
		return Boolean.parseBoolean(controllerElement.getAttribute(attribute).getValue());
	}
	
	@Override
	public void loadWorkspaceData(Element workspaceData) {
		createControllers();
		try {
			Element analysedControllerElement = workspaceData.getChild(workspaceAnalysedControllerName);
			loadWorkspaceDataForController(presentationControllersAnalysed, analysedControllerElement);
		} catch (Exception e) {
			logger.error("Error importing the workspace for analyse.", e);
		}
		try {
			Element definedControllerElement = workspaceData.getChild(workspaceDefinedControllerName);
			loadWorkspaceDataForController(presentationControllersDefined, definedControllerElement);
		} catch (Exception e) {
			logger.error("Error importing the workspace for define.", e);
		}
	}
	
	private void loadWorkspaceDataForController(GraphicsPresentationController controller, Element data) {
		/*// Disabled 2015-12, since it is not useful to store these settings. Furthermore, they were not used at the initialization of a drawing editor. 
		if (isActive(data, workspaceShowDependencies)) 
			controller.dependenciesShow();
		else
			controller.dependenciesHide();
		
		if (isActive(data, workspaceShowViolations)) 
			controller.violationsShow();
		else
			controller.violationsHide();
		
		if (isActive(data, workspaceSmartLines)) 
			controller.smartLinesEnable();
		else
			controller.smartLinesDisable();
		
		DrawingLayoutStrategyEnum selectedStrategy = null;
		for (DrawingLayoutStrategyEnum strategy : DrawingLayoutStrategyEnum.values())
			if (strategy.toString().equals( data.getAttribute(workspaceLayoutStrategy).getValue())) 
				selectedStrategy = strategy;
		if (null != selectedStrategy) 
			controller.layoutStrategyChange(selectedStrategy);
		*/
	}
}