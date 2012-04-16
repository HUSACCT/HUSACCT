package husacct.graphics;

import husacct.graphics.task.AnalysedGUIController;
import husacct.graphics.task.DefinedGUIController;
import husacct.graphics.task.DrawingDetail;

import javax.swing.JInternalFrame;

public class GraphicsServiceImpl implements IGraphicsService {

	private AnalysedGUIController analysed;
	private DefinedGUIController defined;

	public GraphicsServiceImpl()
	{
		analysed = new AnalysedGUIController();
		defined = new DefinedGUIController();
	}

	@Override
	public JInternalFrame getAnalysedArchitectureGUI()
	{
		return analysed.getGUI();
	}

	@Override
	public JInternalFrame getDefinedArchitectureGUI() {

		return defined.getGUI();
	}

	@Override
	public void drawAnalysedArchitecture()
	{
		analysed.drawAnalysedArchitecture(DrawingDetail.WITHOUT_VIOLATIONS);
	}

	@Override
	public void drawAnalysedArchitectureWithViolations()
	{
		analysed.drawAnalysedArchitecture(DrawingDetail.WITH_VIOLATIONS);
	}

	@Override
	public void drawDefinedArchitecture()
	{
		defined.drawDefinedArchitecture(DrawingDetail.WITHOUT_VIOLATIONS);
	}

	@Override
	public void drawDefinedArchitectureWithViolations()
	{
		defined.drawDefinedArchitecture(DrawingDetail.WITH_VIOLATIONS);
	}
}