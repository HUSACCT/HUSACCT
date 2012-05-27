package husacct.graphics.task;

import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.util.DrawingLayoutStrategy;

public interface UserInputListener {
	
	public void moduleOpen(String[] paths);

	public void moduleZoom(BaseFigure[] zoomedModuleFigure);

	public void moduleZoomOut();

	public void figureSelected(BaseFigure[] figures);

	public void figureDeselected(BaseFigure[] figures);

	public void exportToImage();

	public void toggleViolations();
	
	public void refreshDrawing();

	public void changeLayoutStrategy(DrawingLayoutStrategy selectedStrategyEnum);

	public void toggleDependencies();

	public void toggleContextUpdates();
}
