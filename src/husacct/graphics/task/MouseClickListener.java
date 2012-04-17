package husacct.graphics.task;

import husacct.graphics.presentation.figures.ModuleFigure;

public interface MouseClickListener {

	public void moduleZoom(ModuleFigure selectedFigure);

	public void moduleSelected(ModuleFigure selectedFigure);
}
