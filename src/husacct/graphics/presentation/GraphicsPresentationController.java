package husacct.graphics.presentation;


import husacct.common.dto.AbstractDTO;
import husacct.graphics.presentation.Drawing;
import husacct.graphics.presentation.DrawingView;
import husacct.graphics.presentation.GraphicsFrame;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.task.AnalysedController;
import husacct.graphics.task.DefinedController;
import husacct.graphics.task.DrawingController;
import javax.swing.JInternalFrame;
import org.apache.log4j.Logger;

public class GraphicsPresentationController {
	private String 					drawingType; //AnalysedDrawing or DefinedDrawing
	private DrawingController		controller;	
	private Drawing					drawing;
	private DrawingView				drawingView;
	private GraphicsFrame			graphicsFrame;
	private Logger					logger	= Logger.getLogger(GraphicsPresentationController.class);
	
	public GraphicsPresentationController(String typeOfDrawing) {
		try {
			drawing = new Drawing();
			drawingView = new DrawingView(drawing);
			graphicsFrame = new GraphicsFrame(drawingView);
			this.drawingType = typeOfDrawing;
			if (drawingType.equals("AnalysedDrawing")) {
				controller = new AnalysedController(this);
			} else if (drawingType.equals("DefinedDrawing")) {
				controller = new DefinedController(this);
			}
			drawingView.addListener(controller);
			graphicsFrame.addListener(controller);
			graphicsFrame.setSelectedLayout(controller.getLayoutStrategy());
		} catch(Exception e) {
			logger.error(" Exception during initialization of Graphics GUI and Controllers: " + e.getMessage());
		}
	}
	
	protected void changeDrawing(AbstractDTO[] modules) { // Prototype for drawArchitectureTopLevel(), refresh(), zoomIn(), zoomOut() 
		//showLoadingScreen();
		//drawing = runSwingThread ...
		//hideLoadingScreen();
		graphicsFrame.setUpToDate();
		//graphicsFrame.setCurrentPaths(getCurrentPaths());
		graphicsFrame.updateGUI();
	}
	
	public void drawArchitectureTopLevel() {
		controller.drawArchitectureTopLevel();
	}

	public void exportToImage() {
		// To do: move from drawing to presentation or control class 
		drawing.showExportToImagePanel();
	}
	
	public void figureDeselected(BaseFigure[] figures) {
		if (drawingView.getSelectionCount() == 0) graphicsFrame.hideProperties();
	}

	public DrawingController getController() {
		return controller;
	}
	public Drawing getDrawing() {
		return drawing;
	}
	
	public DrawingView getDrawingView() {
		return drawingView;
	}
	
	public JInternalFrame getGraphicsFrame() {
		return graphicsFrame;
	}
	
	public BaseFigure[] getSelectedFigures() {
		return drawingView.toFigureArray(drawingView.getSelectedFigures());
	}
	
	protected boolean hasDependencyBetween(BaseFigure figureFrom, BaseFigure figureTo){
		boolean b = false;
		return b;
	}	
	
	public void hideDependencies() {
		controller.hideDependencies();
		graphicsFrame.turnOffDependencies();
	}
	
	public void hideModules() {
		drawingView.hideSelectedFigures();
	}
	
	public void hideSmartLines() {
		controller.hideSmartLines();
		graphicsFrame.turnOffSmartLines();
	}
	
	public void hideViolations() {
		controller.hideViolations();
		graphicsFrame.turnOffViolations();
	}
	
	public boolean isDrawingVisible() {
		return drawingView.isVisible();
	}
	
	public void moduleZoom() {
	}
	
	public void refreshDrawing(){
		drawing.restoreHiddenFigures();
	}
	
	public void refreshFrame() {
		graphicsFrame.refreshFrame();
	}
	
	public void restoreModules() {
		drawingView.restoreHiddenFigures();
	}
	
	public void setZoomSlider(double zoomFactor) {
		graphicsFrame.setZoomSlider(zoomFactor);
	}
	
	public void showLoadingScreen() {
		drawingView.setVisible(false);
		graphicsFrame.showLoadingScreen();
	}
	
	public void hideLoadingScreen() {
		graphicsFrame.hideLoadingScreen();
		drawingView.setVisible(true);
	}
	
	public void showDependencies() {
		controller.showDependencies();
		graphicsFrame.turnOnDependencies();
	}
	
	public void showSmartLines() {
		controller.showSmartLines();
		graphicsFrame.turnOnSmartLines();
	}
	
	public void showViolations() {
		controller.showViolations();
		graphicsFrame.turnOnViolations();
	}
	
	public void usePanTool() {
		drawingView.usePanTool();
	}
	
	public void useSelectTool() {
		drawingView.useSelectTool();
	}
}
