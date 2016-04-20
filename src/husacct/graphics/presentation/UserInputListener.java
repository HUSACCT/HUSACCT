package husacct.graphics.presentation;

import husacct.graphics.domain.figures.BaseFigure;
import husacct.graphics.task.modulelayout.ModuleLayoutsEnum;

public interface UserInputListener {
	
	public void dependenciesHide();
	
	public void dependenciesShow();
	
	public void exportImage();
	
	public void layoutStrategyChange(ModuleLayoutsEnum selectedStrategyEnum);
	
	public void librariesHide();
	
	public void librariesShow();
	
	public void moduleHide();
	
	public void moduleRestoreHiddenModules();
	
	public void moduleOpen(String[] paths);
	
	public void propertiesPaneHide();

	public void propertiesPaneShowDependencies(BaseFigure selectedLine);
	
	public void propertiesPaneShowViolations(BaseFigure selectedLine);
	
	public void propertiesPaneShowRules(BaseFigure selectedFigure);
	
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
	
	public void zoomTypeChange(String zoomType);
	
	public void zoomOut();
	
}
