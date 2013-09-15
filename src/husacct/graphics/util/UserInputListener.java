package husacct.graphics.util;

import husacct.graphics.presentation.figures.BaseFigure;

public interface UserInputListener {
	
	public void changeLayoutStrategy(DrawingLayoutStrategy selectedStrategyEnum);
	
	public void drawingZoomChanged(double zoomFactor);
	
	public void disableThickLines();
	
	public void enableThickLines();
	
	public void exportToImage();
	
	public void figureDeselected(BaseFigure[] figures);
	
	public void figureSelected(BaseFigure[] figures);
	
	public void hideDependencies();
	
	public void hideModules();
	
	public void hideSmartLines();
	
	public void hideViolations();
	
	public void hideLibraries();
	
	public void moduleOpen(String[] paths);
	
	public void moduleZoom();
	
	public void moduleZoom(BaseFigure[] zoomedModuleFigure);
	
	public void moduleZoom(String zoomType);
	
	public void moduleZoomOut();
	
	public void refreshDrawing();
	
	public void restoreModules();
	
	public void setZoomSlider(double zoomFactor);
	
	public void showDependencies();
	
	public void showSmartLines();
	
	public void showViolations();
	
	public void showLibraries();
	
	public void usePanTool();
	
	public void useSelectTool();
	
}
