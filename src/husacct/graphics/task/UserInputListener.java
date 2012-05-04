package husacct.graphics.task;

import husacct.graphics.presentation.figures.BaseFigure;

public interface UserInputListener {

	public void moduleZoom(BaseFigure[] zoomedModuleFigure);

	public void moduleZoomOut();

	public void figureSelected(BaseFigure[] figures);

	public void figureDeselected(BaseFigure[] figures);

	public void exportToImage();

	public void toggleViolations();
}
