package husacct.graphics;

import husacct.common.savechain.ISaveable;
import husacct.common.services.ObservableService;
import husacct.graphics.task.AnalysedController;
import husacct.graphics.task.DefinedController;
import husacct.graphics.task.DrawingDetail;

import javax.swing.JInternalFrame;

import org.apache.log4j.Logger;
import org.jdom2.Element;

public class GraphicsServiceImpl extends ObservableService implements IGraphicsService, ISaveable {

	private AnalysedController analysedController;
	private DefinedController definedController;
	protected Logger logger = Logger.getLogger(GraphicsServiceImpl.class);

	public GraphicsServiceImpl() {

	}

	private void createControllers() {
		if (analysedController == null) {
			analysedController = new AnalysedController();
		}
		if (definedController == null) {
			definedController = new DefinedController();
		}
	}

	@Override
	public JInternalFrame getAnalysedArchitectureGUI() {
		createControllers();
		return analysedController.getGUI();
	}

	@Override
	public JInternalFrame getDefinedArchitectureGUI() {
		createControllers();
		return definedController.getGUI();
	}

	@Override
	public void drawAnalysedArchitecture() {
		createControllers();
		analysedController.drawArchitecture(DrawingDetail.WITHOUT_VIOLATIONS);
	}

	@Override
	public void drawAnalysedArchitectureWithViolations() {
		createControllers();
		analysedController.drawArchitecture(DrawingDetail.WITH_VIOLATIONS);
	}

	@Override
	public void drawDefinedArchitecture() {
		createControllers();
		definedController.drawArchitecture(DrawingDetail.WITHOUT_VIOLATIONS);
	}

	@Override
	public void drawDefinedArchitectureWithViolations() {
		createControllers();
		definedController.drawArchitecture(DrawingDetail.WITH_VIOLATIONS);
	}

	public static final String workspaceServiceName = "ArchitecureGraphicsService";
	public static final String workspaceAnalysedControllerName = "analysedController";
	public static final String workspaceDefinedControllerName = "analysedController";
	public static final String workspaceShowViolations = "showViolations";

	@Override
	public Element getWorkspaceData() {
		createControllers();
		Element data = new Element(workspaceServiceName);

		Element analysedControllerElement = new Element(workspaceAnalysedControllerName);
		analysedControllerElement.setAttribute(workspaceShowViolations, "" + analysedController.areViolationsShown());
		data.addContent(analysedControllerElement);

		Element definedControllerElement = new Element(workspaceDefinedControllerName);
		definedControllerElement.setAttribute(workspaceShowViolations, "" + definedController.areViolationsShown());
		data.addContent(definedControllerElement);

		return data;
	}

	@Override
	public void loadWorkspaceData(Element workspaceData) {
		createControllers();
		try {
			Element analysedControllerElement = workspaceData.getChild(workspaceAnalysedControllerName);
			System.out.println(Boolean.parseBoolean(analysedControllerElement.getAttribute(workspaceShowViolations)
					.getValue()));
			if (Boolean.parseBoolean(analysedControllerElement.getAttribute(workspaceShowViolations).getValue())) {
				analysedController.showViolations();
			} else {
				analysedController.hideViolations();
			}
			Element definedControllerElement = workspaceData.getChild(workspaceDefinedControllerName);
			System.out.println(Boolean.parseBoolean(definedControllerElement.getAttribute(workspaceShowViolations)
					.getValue()));
			if (Boolean.parseBoolean(definedControllerElement.getAttribute(workspaceShowViolations).getValue())) {
				definedController.showViolations();
			} else {
				definedController.hideViolations();
			}
		} catch (Exception e) {
			logger.error("Error exporting the workspace: " + e.getMessage(), e);
		}
	}
}