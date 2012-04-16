package husacct.graphics.presentation.jhotdraw;

import husacct.graphics.presentation.jhotdraw.figures.JHotDrawModuleFigure;

public class Drawing extends org.jhotdraw.draw.DefaultDrawing
{
	private static final long serialVersionUID = 3212318618672284266L;
	
	public Drawing()
	{
		super();
	}

	public JHotDrawModuleFigure[] getShownModules()
	{
		return new JHotDrawModuleFigure[]{};
	}
}
