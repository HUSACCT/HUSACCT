package husacct.graphics;

import husacct.common.savechain.ISaveable;
import husacct.graphics.task.AnalysedController;
import husacct.graphics.task.DefinedController;
import husacct.graphics.task.DrawingDetail;

import javax.swing.JInternalFrame;

import org.jdom2.Element;

public class GraphicsServiceImpl implements IGraphicsService, ISaveable {

	private AnalysedController analysedController;
	private DefinedController definedController;

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

	@Override
	public Element getWorkspaceData() {
		createControllers();
		Element data = new Element("ArchitecureGraphicsService");
		data.addContent("testdata");
		return data;
	}

	@Override
	public void loadWorkspaceData(Element workspaceData) {
		createControllers();
		//TODO: Set workspace data.
	}
}