package husacct.graphics.presentation;


import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IServiceListener;
import husacct.define.IDefineService;
import husacct.graphics.domain.DrawingView;
import husacct.graphics.domain.figures.BaseFigure;
import husacct.graphics.presentation.GraphicsFrame;
import husacct.graphics.task.DrawingController;
import husacct.graphics.task.DrawingSettingsHolder;
import husacct.graphics.task.modulelayout.DrawingLayoutStrategyEnum;
import husacct.validate.IValidateService;

import javax.swing.JInternalFrame;
import javax.swing.SwingWorker;

import org.apache.log4j.Logger;

public class GraphicsPresentationController implements UserInputListener{
	private String 					drawingType; //AnalysedDrawing or DefinedDrawing
	private DrawingController		drawingController;	
	private DrawingSettingsHolder 	drawingsSettingsHolder;
	private DrawingView				drawingView;
	private GraphicsFrame			graphicsFrame;
	private IAnalyseService			analyseService;
	private IDefineService			defineService;
	private ILocaleService			localeService;
	private IValidateService		validateService;


	private Logger					logger	= Logger.getLogger(GraphicsPresentationController.class);
	
	public GraphicsPresentationController(String typeOfDrawing) {
		try {
			this.drawingType = typeOfDrawing;
			drawingController = DrawingController.getController(drawingType);
			if (drawingController == null) {
				logger.error(" Exception: DrawingController not initialized");
			} else {
				drawingsSettingsHolder = drawingController.getDrawingSettingsHolder();
				drawingView = drawingController.getDrawingView();
				drawingView.addListener(this);
				graphicsFrame = new GraphicsFrame(this);
				loadDefaultSettings();
			}
			initializeServices();
		} catch(Exception e) {
			logger.error(" Exception: " + e.getMessage());
		}
	}

	private void initializeServices() {
		try {
			analyseService = ServiceProvider.getInstance().getAnalyseService();
			analyseService.addServiceListener(new IServiceListener() {
				@Override
				public void update() {
					GraphicsPresentationController.this.refreshDrawing();
				}
			});
	
			defineService = ServiceProvider.getInstance().getDefineService();
			defineService.addServiceListener(new IServiceListener() {
				@Override
				public void update() {
					GraphicsPresentationController.this.refreshDrawing();
				}
			});
	
			localeService = ServiceProvider.getInstance().getLocaleService(); 
			localeService.addServiceListener(new IServiceListener() {
				@Override
				public void update() { // In case language is changed in central Options dialogue.
					GraphicsPresentationController.this.graphicsFrame.refreshFrame();
				}
			});
	
			this.validateService = ServiceProvider.getInstance().getValidateService();
			this.validateService.addServiceListener(new IServiceListener() {
				@Override
				public void update() {
					if (GraphicsPresentationController.this.drawingsSettingsHolder.areViolationsShown()) {
						GraphicsPresentationController.this.refreshDrawing();
					}
				}
			});
		} catch(Exception e) {
			logger.error(" Exception: " + e.getMessage());
		}
	}

	// Main services interacting with DrawingController

	public void drawArchitectureTopLevel() {
		if (drawingView != null) { // Needed to prevent synchronization problems, e.g. after updates of analyse, define and validate tasks.
			try {
				graphicsFrame.attachDrawingViewAndShowDrawing(drawingView);
				graphicsFrame.detachDrawingViewAndShowLoadingScreen();
				drawingView = null;
				SwingWorker<DrawingView, Void> worker = new SwingWorker<DrawingView, Void>() {
			        @Override
			        public DrawingView doInBackground() {
			        	drawingView = drawingController.drawArchitectureTopLevel();
						return drawingView;
			        }
			        @Override
			        public void done() {
			            try {
			            	drawingView = get();
				        	graphicsFrame.attachDrawingViewAndShowDrawing(drawingView);
			            } catch (InterruptedException ignore) {}
			            catch (java.util.concurrent.ExecutionException e) {
			    			logger.error(" Exception: " + e.getMessage());
			            }
			        }
			    };
				worker.execute();
			} catch(Exception e) {
				logger.error(" Exception: " + e.getMessage());
			}
		}
	}

	// Creates a drawing of the contents of the selected path in the GraphicsLocationBar
	@Override
	public void moduleOpen(String[] paths) {
		if (drawingView != null) {
			try {
				graphicsFrame.attachDrawingViewAndShowDrawing(drawingView);
				graphicsFrame.detachDrawingViewAndShowLoadingScreen();
				drawingView = null;
				SwingWorker<DrawingView, Void> worker = new SwingWorker<DrawingView, Void>() {
			        @Override
			        public DrawingView doInBackground() {
			        	drawingView = drawingController.moduleOpen(paths);
						return drawingView;
			        }
			        @Override
			        public void done() {
			            try {
			            	drawingView = get();
				        	graphicsFrame.attachDrawingViewAndShowDrawing(drawingView);
			            } catch (InterruptedException ignore) {}
			            catch (java.util.concurrent.ExecutionException e) {
			    			logger.error(" Exception: " + e.getMessage());
			            }
			        }
			    };
				worker.execute();
			} catch(Exception e) {
				logger.error(" Exception: " + e.getMessage());
			}
		}
	}

	// Updates the drawing to reflect the current settings.
	@Override
	public void refreshDrawing(){
		if (drawingView != null) {
			try {
				graphicsFrame.detachDrawingViewAndShowLoadingScreen();
				drawingView = null;
				SwingWorker<DrawingView, Void> worker = new SwingWorker<DrawingView, Void>() {
			        @Override
			        public DrawingView doInBackground() {
			        	drawingView = drawingController.refreshDrawing();
						return drawingView;
			        }
			        @Override
			        public void done() {
			            try {
			            	drawingView = get();
				        	graphicsFrame.attachDrawingViewAndShowDrawing(drawingView);
			            } catch (InterruptedException ignore) {}
			            catch (java.util.concurrent.ExecutionException e) {
			    			logger.error(" Exception: " + e.getMessage());
			            }
			        }
			    };
				worker.execute();
			} catch(Exception e) {
				logger.error(" Exception: " + e.getMessage());
			}
		}
	}
	
	@Override
	public void zoomIn() {
		if (drawingView != null) {
			try {
				graphicsFrame.detachDrawingViewAndShowLoadingScreen();
				drawingView = null;
				SwingWorker<DrawingView, Void> worker = new SwingWorker<DrawingView, Void>() {
			        @Override
			        public DrawingView doInBackground() {
			        	drawingView = drawingController.zoomIn();
						return drawingView;
			        }
			        @Override
			        public void done() {
			            try {
			            	drawingView = get();
				        	graphicsFrame.attachDrawingViewAndShowDrawing(drawingView);
			            } catch (InterruptedException ignore) {}
			            catch (java.util.concurrent.ExecutionException e) {
			    			logger.error(" Exception: " + e.getMessage());
			            }
			        }
			    };
				worker.execute();
			} catch(Exception e) {
				logger.error(" Exception: " + e.getMessage());
			}
		}
	}

	@Override
	public void zoomOut() {
		if (drawingView != null) {
			try {
				graphicsFrame.detachDrawingViewAndShowLoadingScreen();
				drawingView = null;
				SwingWorker<DrawingView, Void> worker = new SwingWorker<DrawingView, Void>() {
			        @Override
			        public DrawingView doInBackground() {
			        	drawingView = drawingController.zoomOut();
						return drawingView;
			        }
			        @Override
			        public void done() {
			            try {
			            	drawingView = get();
				        	graphicsFrame.attachDrawingViewAndShowDrawing(drawingView);
			            } catch (InterruptedException ignore) {}
			            catch (java.util.concurrent.ExecutionException e) {
			    			logger.error(" Exception: " + e.getMessage());
			            }
			        }
			    };
				worker.execute();
			} catch(Exception e) {
				logger.error(" Exception: " + e.getMessage());
			}
		}
	}
	

	
// Other methods and services
	
	public boolean areDependenciesShown() {
		return drawingsSettingsHolder.areDependenciesShown();
	}
	
	public boolean areSmartLinesOn() {
		return drawingsSettingsHolder.areSmartLinesOn();
	}
	
	public boolean areViolationsShown() {
		return drawingsSettingsHolder.areViolationsShown();
	}
	
	public boolean areExternalLibrariesShown() {
		return drawingsSettingsHolder.areExternalLibrariesShown();
	}
	
	public boolean areLinesThick(){
		return drawingsSettingsHolder.areLinesProportionalWide();
	}
	
	@Override
	public void dependenciesHide() {
		if (drawingsSettingsHolder.areDependenciesShown()) {
			drawingsSettingsHolder.dependenciesHide();
			graphicsFrame.getGraphicsMenuBar().setDependeciesButtonToDontShow();
		}
	}
	
	@Override
	public void dependenciesShow() {
		if (!drawingsSettingsHolder.areDependenciesShown()) {
			drawingsSettingsHolder.dependenciesShow();
			graphicsFrame.getGraphicsMenuBar().setDependeciesButtonsToShow();
		}
	}
	
	public void exportImage() {
		// To do: move from drawingView to presentation or control class 
		drawingView.getDrawingHusacct().showExportToImagePanel();
	}
	
	public String[] getCurrentPaths() {
		return drawingsSettingsHolder.getCurrentPaths();
	}
	
	public JInternalFrame getGraphicsFrame() {
		return graphicsFrame;
	}
	
	public DrawingLayoutStrategyEnum getLayoutStrategy() {
		return drawingController.getLayoutStrategy();
	}
	
	@Override
	public void layoutStrategyChange(DrawingLayoutStrategyEnum selectedStrategyEnum) {
			drawingController.layoutStrategyChange(selectedStrategyEnum);
	}
	
	private void loadDefaultSettings() {
		drawingsSettingsHolder.dependenciesShow();
		graphicsFrame.getGraphicsMenuBar().setDependeciesButtonsToShow();
		drawingsSettingsHolder.librariesHide(); // Initializes in GraphicsOptionsDialogue with this setting.
		drawingsSettingsHolder.proportionalLinesDisable();
		drawingsSettingsHolder.smartLinesEnable();
		graphicsFrame.turnOnSmartLines();
		drawingsSettingsHolder.violationsHide();
		graphicsFrame.setViolationsButtonsToDontShow();
		graphicsFrame.setSelectedLayout(drawingController.getLayoutStrategy());
	}
	
	@Override
	public void librariesHide() {
		if (drawingsSettingsHolder.areExternalLibrariesShown()) {
			drawingsSettingsHolder.librariesHide();
		}
	}

	@Override
	public void librariesShow() {
		if (!drawingsSettingsHolder.areExternalLibrariesShown()) {
			drawingsSettingsHolder.librariesShow();
		}
	}

	@Override
	public void moduleHide() {
		drawingView.hideSelectedFigures();
	}
	
	@Override
	public void moduleRestoreHiddenModules() {
		drawingView.restoreHiddenFigures();
	}
	
	@Override
	public void proportionalLinesDisable() {
		if (drawingsSettingsHolder.areLinesProportionalWide()) {
			drawingsSettingsHolder.proportionalLinesDisable();
		}
	}
	
	@Override
	public void proportionalLinesEnable() {
		if (!drawingsSettingsHolder.areLinesProportionalWide()) {
			drawingsSettingsHolder.proportionalLinesEnable();
		}
	}
	
	@Override
	public void propertiesPaneHide(){
		graphicsFrame.hideProperties();
	}
	
	@Override
	public void propertiesPaneShowDependencies(BaseFigure selectedLine) {
		DependencyDTO[] dependencyDTOs = drawingController.getDependenciesOfLine(selectedLine);
		graphicsFrame.showDependenciesProperties(dependencyDTOs);
	}

	@Override
	public void propertiesPaneShowViolations(BaseFigure selectedLine) {
		ViolationDTO[] violationDTOs = drawingController.getViolationsOfLine(selectedLine);
		graphicsFrame.showViolationsProperties(violationDTOs);
	}
	
	@Override
	public void smartLinesDisable() {
		if (drawingsSettingsHolder.areSmartLinesOn()) {
			drawingsSettingsHolder.smartLinesDisable();
			graphicsFrame.setSmartLinesButtonsToDontShow();
		}
	}
	
	@Override
	public void smartLinesEnable() {
		if (!drawingsSettingsHolder.areSmartLinesOn()) {
			drawingsSettingsHolder.smartLinesEnable();
			graphicsFrame.turnOnSmartLines();
		}
	}
	
	@Override
	public void violationsHide() {
		if (drawingsSettingsHolder.areViolationsShown()) {
			drawingsSettingsHolder.violationsHide();
			graphicsFrame.setViolationsButtonsToDontShow();
		}
	}
	
	@Override
	public void violationsShow() {
		if (validateService.isValidated()) {
			if (!drawingsSettingsHolder.areViolationsShown()) {
				drawingsSettingsHolder.violationsShow();
				graphicsFrame.setViolationsButtonsToShow();
			}
		} else {
			drawingsSettingsHolder.violationsHide();
			graphicsFrame.setViolationsButtonsToDontShow();
		}
	}
	
	@Override
	public void usePanTool() {
		drawingView.usePanTool();
	}
	
	@Override
	public void useSelectTool() {
		drawingView.useSelectTool();
	}

	@Override
	public void zoomFactorChanged(double zoomFactor) {
		double scaleFactor = graphicsFrame.getGraphicsMenuBar().getScaleFactor(); //Needed in case the event is fired from DrawingView
		drawingController.zoomFactorChanged(scaleFactor);
	}
	
	@Override
	public void zoomSliderSetZoomFactor(double zoomFactor) {
		graphicsFrame.zoomSliderSetZoomFactor(zoomFactor);
	}
	@Override
	public void zoomTypeChange(String zoomType) {
		drawingsSettingsHolder.zoomTypeChange(zoomType);
	}
	
}
