package husacct.graphics;

import husacct.graphics.task.DemoController;

import javax.swing.JInternalFrame;

public class GraphicsServiceDemo implements IGraphicsService
{
	private DemoController controller;
	
	public GraphicsServiceDemo()
	{
		this.controller = new DemoController();
	}

	@Override
	public JInternalFrame getAnalysedArchitectureGUI()
	{
		return controller.getGUI();
	}

	@Override
	public JInternalFrame getDefinedArchitectureGUI()
	{
		return controller.getGUI();
	}

	@Override
	public void drawAnalysedArchitecture() {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawAnalysedArchitectureWithViolations() {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawDefinedArchitecture() {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawDefinedArchitectureWithViolations() {
		// TODO Auto-generated method stub

	}

}
