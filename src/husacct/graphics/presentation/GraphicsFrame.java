package husacct.graphics.presentation;

import husacct.ServiceProvider;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.help.presentation.HelpableJInternalFrame;
import husacct.common.locale.ILocaleService;
import husacct.graphics.domain.DrawingView;
import husacct.graphics.presentation.menubars.GraphicsMenuBar;
import husacct.graphics.presentation.menubars.LocationButtonActionListener;
import husacct.graphics.presentation.menubars.GraphicsLocationBar;
import husacct.graphics.presentation.tables.DependencyTable;
import husacct.graphics.presentation.tables.ViolationTable;
import husacct.graphics.task.modulelayout.ModuleLayoutsEnum;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.apache.log4j.Logger;

public class GraphicsFrame extends HelpableJInternalFrame {
	private static final long				serialVersionUID	= -4683140198375851034L;
	protected ILocaleService				localeService		= ServiceProvider.getInstance().getLocaleService();
	protected Logger						logger				= Logger.getLogger(GraphicsFrame.class);
	
	private GraphicsPresentationController	presentationController;
	private JPanel 							loadingContainerPanel, progressPanel;
	private JProgressBar 					progressBar;
	private DrawingView						drawingView;
	private GraphicsMenuBar					menuBar;
	private GraphicsLocationBar				locationBar;
	private String[]						currentPaths;
	private JScrollPane						drawingScrollPane, propertiesScrollPane, locationScrollPane;
	private JSplitPane						centerPane;
	private String							ROOT_LEVEL;
	private boolean							showLoadingScreen = true;
	private boolean							showProperties	= false;
	
	private int								frameTotalWidth;
	private int								menuBarHeight = 20;
	
	public GraphicsFrame(GraphicsPresentationController graphicsPresentationController) {
		presentationController = graphicsPresentationController;
		setVisible(false);
		frameTotalWidth = getWidth();
		initializeComponents();
		layoutCenterPane();
		updateGUI();
		setVisible(true);
		
		addHierarchyBoundsListener(new HierarchyBoundsListener() {
			@Override
			public void ancestorMoved(HierarchyEvent arg0) {
			}
			
			@Override
			public void ancestorResized(HierarchyEvent arg0) {
				positionLayoutComponents();
			}
		});
	}
	
	public void createLocationBar() {
		locationBar = new GraphicsLocationBar();
		locationBar.addLocationButtonPressListener(new LocationButtonActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
			@Override
			public void actionPerformed(String[] selectedPaths) {
				presentationController.moduleOpen(selectedPaths);
			}
		});
	}
	
	private void createMenuBar() {
		menuBar = new GraphicsMenuBar();
		menuBar.addListener(presentationController);
		menuBar.setSize(frameTotalWidth, menuBarHeight);
		menuBar.setOutOfDateAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				presentationController.refreshDrawing();
			}
		});
	}
	
	public void attachDrawingViewAndShowDrawing(DrawingView updatedDrawingView) {
		// Attach drawingView
		drawingView = updatedDrawingView;
		drawingScrollPane.setViewportView(drawingView);
		drawingView.initializePanTool(drawingScrollPane.getViewport(), drawingScrollPane);
		drawingView.addListener(presentationController);
		// Hide loading screen and show drawing.
		showLoadingScreen = false;
		hideOutOfDateWarning();
		setCurrentPaths(presentationController.getCurrentPaths());
		layoutCenterPane();
	}
	
	public void detachDrawingViewAndShowLoadingScreen() {
		// Detach links to DrawingView and Drawing to prevent Swing Synchronization problems.
		if (drawingView != null) {
			drawingView.removePanTool();
			drawingScrollPane.setViewportView(null);
			drawingView.removeListeners();
			drawingView = null;
			// Show loading screen.
			showLoadingScreen = true;
			layoutCenterPane();
		}
	}

	public String[] getCurrentPaths() {
		return currentPaths;
	}
	
	GraphicsMenuBar getGraphicsMenuBar() {
		return menuBar;
	}
	
	GraphicsLocationBar getGraphicsLocationBar() {
		return locationBar;
	}
	
	public void hideProperties() {
		showProperties = false;
		layoutCenterPane();
	}
	
	private void initializeComponents() {
		// Create the three main components of GraphicsFrame, and set the layout.
		createMenuBar();

		centerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		add(centerPane, BorderLayout.CENTER);
		
		resetCurrentPaths();
		createLocationBar();
		locationScrollPane = new JScrollPane(locationBar);
		locationScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		locationScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		
		setLayout(new BorderLayout());
		add(menuBar, BorderLayout.NORTH);
		add(locationScrollPane, BorderLayout.SOUTH);
		add(centerPane, BorderLayout.CENTER);

		// Initialize subcomponents of centerPane
		loadingContainerPanel = new JPanel();
		progressPanel = new JPanel();
		loadingContainerPanel.add(progressPanel);
		
		drawingScrollPane = new JScrollPane();
		drawingScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		drawingScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		drawingScrollPane.getVerticalScrollBar().setUnitIncrement(10);

		propertiesScrollPane = new JScrollPane();
		propertiesScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		propertiesScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		// Set the texts in the current language 
		updateComponentsLocaleStrings();
		
		// Add Listeners
		getRootPane().addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent e) {
				// Do nothing
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				// Do nothing
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				positionLayoutComponents();
				resizeLocationBar();
			}
			
			@Override
			public void componentShown(ComponentEvent e) {
				// Do nothing
			}
		});
		
	}
	
	private void layoutCenterPane() {
		centerPane.removeAll();
		if (showLoadingScreen) {
			progressBar = new JProgressBar();
			progressBar.setIndeterminate(true);
			progressPanel.add(progressBar);
			centerPane.add(loadingContainerPanel);
			centerPane.setDividerSize(0);
			locationBar.turnOffBar();
			menuBar.turnOffBar();
		} else {
			if (drawingScrollPane.getViewport().getView() != null) {  
				progressPanel.removeAll();
				if (!showProperties) {
					centerPane.add(drawingScrollPane);
					centerPane.setDividerSize(0);
				} else {
					centerPane.add(drawingScrollPane);
					centerPane.add(propertiesScrollPane);
					positionLayoutComponents();
					centerPane.setOneTouchExpandable(true);
					centerPane.setContinuousLayout(true);
				}
				locationBar.turnOnBar();
				menuBar.turnOnBar();
			} else {
				// Show drawingScrollPane only if drawingView is set.
				logger.error(" drawingScrollPane.getViewport().getView() is null: drawingView not set!" );
			}
		}
		centerPane.validate();
		centerPane.updateUI();
		updateGUI();
		if (isVisible()) {
			validate();
		}
	}
	
	private void positionLayoutComponents() {
		if (showProperties) {
			int centerPaneHeight = getHeight();
			int centerPaneWidth = getWidth();
			centerPane.setSize(centerPaneWidth, centerPaneHeight);
			int smallerSize = centerPaneHeight - (centerPaneHeight / 3);
			centerPane.setDividerLocation(smallerSize);
			centerPane.setDividerSize(10);
		}
	}
	
	public void refreshFrame() {
		updateComponentsLocaleStrings();
	}
	
	public void resetCurrentPaths() {
		currentPaths = new String[] {};
	}
	
	private void resizeLocationBar() {
		if (locationScrollPane.getHorizontalScrollBar().isShowing()) 
			locationScrollPane.setPreferredSize(new Dimension(900, 50));
		else
			locationScrollPane.setPreferredSize(new Dimension(900, 35));
	}
	
	public void setCurrentPaths(String[] paths) {
		currentPaths = paths;
	}
	
	public void showOutOfDateWarning() {
		menuBar.showOutOfDateWarning();
	}
	
	public void setSelectedLayout(ModuleLayoutsEnum layoutStrategyOption) {
		menuBar.setSelectedLayoutStrategyItem(layoutStrategyOption);
	}
	
	public void hideOutOfDateWarning() {
		menuBar.hideOutOfDateWarning();
	}
	
	public void zoomSliderSetZoomFactor(double zoomFactor) {
		menuBar.zoomSliderSetZoomFactor(zoomFactor);
	}
	
	public void setDependeciesButtonsToShow() {
		menuBar.setDependeciesButtonsToShow();
	}
	
	public void showDependenciesProperties(DependencyDTO[] dependencyDTOs) {
		showProperties();
		DependencyTable propertiesTable = new DependencyTable(dependencyDTOs);
		propertiesScrollPane.setViewportView(propertiesTable);
	}
	
	public void showProperties() {
		showProperties = true;
		layoutCenterPane();
	}
	
	public void showViolationsProperties(ViolationDTO[] violationDTOs) {
		showProperties();
		ViolationTable propertiesTable = new ViolationTable(violationDTOs);
		propertiesScrollPane.setViewportView(propertiesTable);
	}
	
	public void setSmartLinesButtonsToDontShow() {
		menuBar.setSmartLinesButtonsToDontShow();
	}
	
	public void setViolationsButtonsToDontShow() {
		menuBar.setViolationsButtonsToDontShow();
	}
	
	public void turnOnSmartLines() {
		menuBar.setSmartLinesButtonsToShow();
	}
	
	public void setViolationsButtonsToShow() {
		menuBar.setViolationsButtonsToShow();
	}
	
	private void updateComponentsLocaleStrings() {
		HashMap<String, String> menuBarLocale = new HashMap<String, String>();
		menuBarLocale.put("DiagramOptions",
				localeService.getTranslatedString("DiagramOptions"));
		menuBarLocale.put("Options",
				localeService.getTranslatedString("Options"));
		menuBarLocale.put("Ok", localeService.getTranslatedString("OkButton"));
		menuBarLocale.put("Cancel",
				localeService.getTranslatedString("CancelButton"));
		menuBarLocale.put("Zoom", localeService.getTranslatedString("Zoom"));
		menuBarLocale
				.put("ZoomIn", localeService.getTranslatedString("ZoomIn"));
		menuBarLocale.put("ZoomOut",
				localeService.getTranslatedString("ZoomOut"));
		menuBarLocale.put("Refresh",
				localeService.getTranslatedString("Refresh"));
		menuBarLocale.put("HideDependencies",
				localeService.getTranslatedString("HideDependencies"));
		menuBarLocale.put("ShowDependencies",
				localeService.getTranslatedString("ShowDependencies"));
		menuBarLocale.put("HideViolations",
				localeService.getTranslatedString("HideViolations"));
		menuBarLocale.put("ShowViolations",
				localeService.getTranslatedString("ShowViolations"));
		menuBarLocale.put("HideExternalLibraries",
				localeService.getTranslatedString("HideExternalLibraries"));
		menuBarLocale.put("ShowExternalLibraries",
				localeService.getTranslatedString("ShowExternalLibraries"));
		menuBarLocale.put("LineContextUpdates",
				localeService.getTranslatedString("LineContextUpdates"));
		menuBarLocale.put("ExportToImage",
				localeService.getTranslatedString("ExportToImage"));
		menuBarLocale.put("LayoutStrategy",
				localeService.getTranslatedString("LayoutStrategy"));
		menuBarLocale.put("DrawingOutOfDate",
				localeService.getTranslatedString("DrawingOutOfDate"));
		menuBarLocale.put("HideModules",
				localeService.getTranslatedString("HideModules"));
		menuBarLocale.put("RestoreHiddenModules",
				localeService.getTranslatedString("RestoreHiddenModules"));
		menuBar.setLocale(menuBarLocale);
		
		ROOT_LEVEL = localeService.getTranslatedString("Root");
		locationBar.setLocale(ROOT_LEVEL);
	}
	
	public void updateGUI() {
		locationBar.updateLocationBar(getCurrentPaths());
		updateUI();
	}
	
}
