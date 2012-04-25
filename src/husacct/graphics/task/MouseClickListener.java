package husacct.graphics.task;

import husacct.graphics.presentation.figures.BaseFigure;

public interface MouseClickListener {

	public void moduleZoom(BaseFigure zoomedModuleFigure);
	
	public void figureSelected(BaseFigure clickedFigure);
	
	public void figureDeselected();
}
