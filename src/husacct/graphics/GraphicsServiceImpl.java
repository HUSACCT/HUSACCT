package husacct.graphics;

import husacct.graphics.task.AnalysedController;
import husacct.graphics.task.DefinedController;
import husacct.graphics.task.DrawingDetail;

import javax.swing.JInternalFrame;

public class GraphicsServiceImpl implements IGraphicsService {

	private AnalysedController analysedController;
	private DefinedController definedController;

	public GraphicsServiceImpl() {
		analysedController = new AnalysedController();
		definedController = new DefinedController();
	}

	@Override
	public JInternalFrame getAnalysedArchitectureGUI() {
		return analysedController.getGUI();
	}

	@Override
	public JInternalFrame getDefinedArchitectureGUI() {

		return definedController.getGUI();
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
}