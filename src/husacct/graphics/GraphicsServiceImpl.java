package husacct.graphics;

import husacct.graphics.task.Controller;
import husacct.graphics.task.DrawingDetail;

import javax.swing.JInternalFrame;

public class GraphicsServiceImpl implements IGraphicsService {

	private Controller controller;
//	private AnalysedGUIController analysed;
//	private DefinedGUIController defined;

	public GraphicsServiceImpl()
	{
		controller = new Controller();
	}

	@Override
	public JInternalFrame getAnalysedArchitectureGUI()
	{
		return controller.getGUI();
	}

	@Override
	public JInternalFrame getDefinedArchitectureGUI() {

		return controller.getGUI();
	}

	@Override
	public void drawAnalysedArchitecture()
	{
		controller.drawAnalysedArchitecture(DrawingDetail.WITHOUT_VIOLATIONS);
	}

	@Override
	public void drawAnalysedArchitectureWithViolations()
	{
		controller.drawAnalysedArchitecture(DrawingDetail.WITH_VIOLATIONS);
	}

	@Override
	public void drawDefinedArchitecture()
	{
		controller.drawDefinedArchitecture(DrawingDetail.WITHOUT_VIOLATIONS);
	}

	@Override
	public void drawDefinedArchitectureWithViolations()
	{
		controller.drawDefinedArchitecture(DrawingDetail.WITH_VIOLATIONS);
	}
}