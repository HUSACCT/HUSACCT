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
			graphicsFrame = new GraphicsFrame();
			this.drawingType = typeOfDrawing;
			controller = DrawingController.getController(this);
			if (controller == null) {
				logger.error(" Exception: DrawingController not initialized");
			} else {
				//drawingView = controller.getDrawingView(); // To do when Presentation and Task are decoupled
				drawingView.addListener(controller);
				graphicsFrame.addListener(controller);
				graphicsFrame.setSelectedLayout(controller.getLayoutStrategy());
			}
		} catch(Exception e) {
			logger.error(" Exception during initialization of Graphics GUI and Controllers: " + e.getMessage());
		}
	}
	
	protected void changeDrawing(AbstractDTO[] modules) { // Prototype for drawArchitectureTopLevel(), refresh(), zoomIn(), zoomOut() 
		//ServiceProvider.getInstance().getGraphicsService().notifyServiceListeners(); // Seems pointless: Removed.
		//showLoadingScreen();
		//drawing = runSwingThread ...
		//hideLoadingScreen();
		graphicsFrame.showDrawing(drawingView);
		graphicsFrame.setUpToDate();
		//graphicsFrame.setCurrentPaths(getCurrentPaths());
		graphicsFrame.updateGUI();
	}
	
	public void drawArchitectureTopLevel() {
		try {
			//graphicsFrame.hideDrawingAndShowLoadingScreen();
			controller.drawArchitectureTopLevel();
			graphicsFrame.showDrawing(drawingView);
		} catch(Exception e) {
			logger.error(" Exception: " + e.getMessage());
		}
	}

	public void zoomIn() {
		graphicsFrame.hideDrawingAndShowLoadingScreen();
		controller.zoomIn();
		graphicsFrame.showDrawing(drawingView);
	}
	
	public void exportImage() {
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
	
	public String getDrawingType() {
		return drawingType;
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
	
	public void dependenciesHide() {
		controller.dependenciesHide();
		graphicsFrame.turnOffDependencies();
	}
	
	public void dependenciesShow() {
		controller.dependenciesShow();
		graphicsFrame.turnOnDependencies();
	}
	
	public boolean isDrawingVisible() {
		return drawingView.isVisible();
	}
	
	public void moduleHide() {
		drawingView.hideSelectedFigures();
	}
	
	public void moduleRestoreHiddenModules() {
		drawingView.restoreHiddenFigures();
	}
	
	public void refreshDrawing(){
		drawing.restoreHiddenFigures();
	}
	
	public void refreshFrame() {
		graphicsFrame.refreshFrame();
	}
	
	public void zoomSliderSetZoomFactor(double zoomFactor) {
		graphicsFrame.zoomSliderSetZoomFactor(zoomFactor);
	}
	
	public void showLoadingScreen() {
		drawingView.setVisible(false);
		graphicsFrame.showLoadingScreen();
		graphicsFrame.updateGUI();
	}
	
	public void hideLoadingScreen() {
		graphicsFrame.hideLoadingScreen();
		drawingView.setVisible(true);
		graphicsFrame.setUpToDate();
		graphicsFrame.updateGUI();
	}
	
	public void smartLinesDisable() {
		controller.smartLinesDisable();
		graphicsFrame.turnOffSmartLines();
	}
	
	public void smartLinesEnable() {
		controller.smartLinesEnable();
		graphicsFrame.turnOnSmartLines();
	}
	
	public void violationsHide() {
		controller.violationsHide();
		graphicsFrame.turnOffViolations();
	}
	
	public void violationsShow() {
		controller.violationsShow();
		graphicsFrame.turnOnViolations();
	}
	
	public void usePanTool() {
		drawingView.usePanTool();
	}
	
	public void useSelectTool() {
		drawingView.useSelectTool();
	}
}
