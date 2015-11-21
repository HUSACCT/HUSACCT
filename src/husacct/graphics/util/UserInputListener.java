package husacct.graphics.util;

import husacct.graphics.presentation.figures.BaseFigure;

public interface UserInputListener {
	
	public void dependenciesHide();
	
	public void dependenciesShow();
	
	public void exportImage();
	
	public void figureDeselected(BaseFigure[] figures);
	
	public void figureSelected(BaseFigure[] figures);
	
	public void layoutStrategyChange(DrawingLayoutStrategy selectedStrategyEnum);
	
	public void librariesHide();
	
	public void librariesShow();
	
	public void moduleHide();
	
	public void moduleRestoreHiddenModules();
	
	public void moduleOpen(String[] paths);
	
	public void proportionalLinesDisable();
	
	public void proportionalLinesEnable();
	
	public void refreshDrawing();
	
	public void smartLinesDisable();
	
	public void smartLinesEnable();
	
	public void zoomSliderSetZoomFactor(double zoomFactor);
	
	public void usePanTool();
	
	public void useSelectTool();
	
	public void violationsHide();
	
	public void violationsShow();
	
	public void zoomFactorChanged(double zoomFactor);
	
	public void zoomIn();
	
	public void zoomIn(BaseFigure[] zoomedModuleFigure);
	
	public void zoomTypeChange(String zoomType);
	
	public void zoomOut();
	
}
