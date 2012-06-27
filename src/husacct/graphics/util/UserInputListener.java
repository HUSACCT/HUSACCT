package husacct.graphics.util;

import husacct.graphics.presentation.figures.BaseFigure;

public interface UserInputListener {

	public void moduleZoom();

	public void moduleOpen(String[] paths);

	public void moduleZoom(BaseFigure[] zoomedModuleFigure);

	public void moduleZoomOut();

	public void figureSelected(BaseFigure[] figures);

	public void figureDeselected(BaseFigure[] figures);

	public void exportToImage();

	public void refreshDrawing();

	public void changeLayoutStrategy(DrawingLayoutStrategy selectedStrategyEnum);

	public void showDependencies();

	public void hideDependencies();

	public void showViolations();

	public void hideViolations();

	public void showSmartLines();

	public void hideSmartLines();

	public void drawingZoomChanged(double zoomFactor);

	public void hideModules();

	public void restoreModules();
}
