package husacct.graphics;

import husacct.graphics.task.Controller;

import javax.swing.JInternalFrame;

public class GraphicsServiceStub implements IGraphicsService {
	private Controller controller;

	public GraphicsServiceStub() {
		controller = new Controller();
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
	}

	@Override
	public void drawAnalysedArchitectureWithViolations() {
	}

	@Override
	public void drawDefinedArchitecture() {
	}

	@Override
	public void drawDefinedArchitectureWithViolations() {
	}

}
