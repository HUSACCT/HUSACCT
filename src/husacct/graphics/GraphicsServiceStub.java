package husacct.graphics;

import husacct.graphics.task.DemoGUIController;

import javax.swing.JInternalFrame;

public class GraphicsServiceStub implements IGraphicsService
{
	private DemoGUIController controller;
	
	public GraphicsServiceStub()
	{
		this.controller = new DemoGUIController();
	}

	@Override
	public JInternalFrame getAnalysedArchitectureGUI()
	{
		return this.controller.getGUI();
	}

	@Override
	public JInternalFrame getDefinedArchitectureGUI()
	{
		return this.controller.getGUI();
	}

	@Override
	public void drawAnalysedArchitecture()
	{		
	}

	@Override
	public void drawAnalysedArchitectureWithViolations()
	{
	}

	@Override
	public void drawDefinedArchitecture()
	{		
	}

	@Override
	public void drawDefinedArchitectureWithViolations()
	{		
	}
	
}
