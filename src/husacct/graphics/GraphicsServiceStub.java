package husacct.graphics;

import husacct.common.savechain.ISaveable;
import husacct.common.services.ObservableService;
import husacct.graphics.task.AnalysedController;
import husacct.graphics.task.DefinedController;
import husacct.graphics.task.DrawingDetail;

import javax.swing.JInternalFrame;

import org.jdom2.Element;

public class GraphicsServiceStub extends ObservableService implements IGraphicsService, ISaveable {
	private AnalysedController analysedController;
	private DefinedController definedController;

	public GraphicsServiceStub() {
		analysedController = new AnalysedController();
		definedController = new DefinedController();
	}

	@Override
	public JInternalFrame getAnalysedArchitectureGUI() {
		return analysedController.getGUI();
	}

	@Override
	public JInternalFrame getDefinedArchitectureGUI() {
		return analysedController.getGUI();
	}

	@Override
	public void drawAnalysedArchitecture() {
		analysedController.drawArchitecture(DrawingDetail.WITHOUT_VIOLATIONS);
	}

	@Override
	public void drawAnalysedArchitectureWithViolations() {
		analysedController.drawArchitecture(DrawingDetail.WITH_VIOLATIONS);
	}

	@Override
	public void drawDefinedArchitecture() {
		definedController.drawArchitecture(DrawingDetail.WITHOUT_VIOLATIONS);
	}

	@Override
	public void drawDefinedArchitectureWithViolations() {
		definedController.drawArchitecture(DrawingDetail.WITH_VIOLATIONS);
	}

	@Override
	public Element getWorkspaceData() {
		Element data = new Element("ArchitecureGraphicsService");
		data.addContent("testdata");
		return data;
	}

	@Override
	public void loadWorkspaceData(Element workspaceData) {
		//TODO: Set workspace data.
	}
	
}
