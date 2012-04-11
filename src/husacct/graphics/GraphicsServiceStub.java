package husacct.graphics;

import husacct.graphics.task.DemoGUIController;
import husacct.graphics.task.DrawingDetail;
import husacct.graphics.task.GUIController;

import javax.swing.JInternalFrame;

public class GraphicsServiceStub implements IGraphicsService {

	private GUIController controller;

	public GraphicsServiceStub() {
		controller = new DemoGUIController();
	}	
	
	@Override
	public JInternalFrame getAnalysedArchitectureGUI() {

		return controller.getGUI();
	}

	@Override
	public JInternalFrame getDefinedArchitectureGUI() {

		return controller.getGUI();
	}

	@Override
	public void drawAnalysedArchitecture() {
		controller.drawAnalysedArchitecture(DrawingDetail.WITHOUT_VIOLATIONS);
	}

	@Override
	public void drawAnalysedArchitectureWithViolations() {
		controller.drawAnalysedArchitecture(DrawingDetail.WITH_VIOLATIONS);
	}

	@Override
	public void drawDefinedArchitecture() {
		controller.drawDefinedArchitecture(DrawingDetail.WITHOUT_VIOLATIONS);
	}

	@Override
	public void drawDefinedArchitectureWithViolations() {
		controller.drawDefinedArchitecture(DrawingDetail.WITH_VIOLATIONS);
	}
}
