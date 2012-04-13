package husacct.graphics.presentation.jhotdraw;

import husacct.graphics.task.figures.ModuleFigure;

public class Drawing extends org.jhotdraw.draw.DefaultDrawing
{
	private static final long serialVersionUID = 3212318618672284266L;
	
	public Drawing()
	{
		super();
	}

	public ModuleFigure[] getShownModules()
	{
		return new ModuleFigure[]{};
	}
}
