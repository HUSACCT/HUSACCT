package husacct.graphics.task;

import husacct.graphics.task.figures.IViolatedFigure;
import husacct.graphics.task.figures.ModuleFigure;

public interface IGraphicsGUIListener
{
	public void onModuleFigureZoom(ModuleFigure moduleFigure);
	public void onViolatedFigureSelect(IViolatedFigure violatedFigure);
}
