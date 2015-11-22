package husacct.graphics.presentation;

import husacct.ServiceProvider;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.help.presentation.HelpableJInternalFrame;
import husacct.common.locale.ILocaleService;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.menubars.GraphicsMenuBar;
import husacct.graphics.presentation.menubars.LocationButtonActionListener;
import husacct.graphics.presentation.menubars.ZoomLocationBar;
import husacct.graphics.presentation.tables.DependencyTable;
import husacct.graphics.presentation.tables.ViolationTable;
import husacct.graphics.util.DrawingLayoutStrategy;
import husacct.graphics.util.UserInputListener;

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

public class GraphicsFrame extends HelpableJInternalFrame implements UserInputListener {
	private static final long				serialVersionUID	= -4683140198375851034L;
	protected ILocaleService				localeService		= ServiceProvider.getInstance().getLocaleService();
	protected Logger						logger				= Logger.getLogger(GraphicsFrame.class);
	
	private GraphicsPresentationController	presentationController;
	private JPanel 							loadingContainerPanel, progressPanel;
	private JProgressBar 					progressBar;
	private DrawingView						drawingView;
	private GraphicsMenuBar					menuBar;
	private ZoomLocationBar					locationBar;
	private String[]						currentPaths;
	private JScrollPane						drawingScrollPane, propertiesScrollPane, locationScrollPane;
	private JSplitPane						centerPane;
	private String							ROOT_LEVEL;
	private boolean							showLoadingScreen = true;
	private boolean							showProperties	= false;
	
	private int								frameTotalWidth;
	private int								menuBarHeight = 20;
	
	private ArrayList<UserInputListener>	listeners			= new ArrayList<UserInputListener>();
	
	public GraphicsFrame(GraphicsPresentationController graphicsPresentationController) {
		presentationController = graphicsPresentationController;
		setVisible(false);
		frameTotalWidth = getWidth();
		initializeComponents();
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
	
	public void addListener(UserInputListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void layoutStrategyChange(DrawingLayoutStrategy selectedStrategyEnum) {
		for (UserInputListener l : listeners)
			l.layoutStrategyChange(selectedStrategyEnum);
	}
	
	public void createLocationBar() {
		locationBar = new ZoomLocationBar();
		locationBar.addLocationButtonPressListener(new LocationButtonActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
			@Override
			public void actionPerformed(String[] selectedPaths) {
				moduleOpen(selectedPaths);
			}
		});
		locationBar.updateLocationBar(currentPaths);

	}
	
	private void createMenuBar() {
		menuBar = new GraphicsMenuBar();
		menuBar.addListener(this);
		menuBar.setSize(frameTotalWidth, menuBarHeight);
		menuBar.setOutOfDateAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setUpToDate();
				refreshDrawing();
			}
		});
	}
	
	public void showDrawing(DrawingView newDrawingView) {
		this.drawingView = newDrawingView;
		drawingScrollPane.setViewportView(drawingView);
		drawingView.initializePanTool(drawingScrollPane.getViewport(), drawingScrollPane);
		showLoadingScreen = false;
		layoutComponents();
		menuBar.turnOnBar();
		locationBar.turnOnBar();
	}
	
	public void hideDrawingAndShowLoadingScreen() {
		// Detach links to DrawingView and Drawing to prevent Swing Synchronization problems.
		this.drawingView.removePanTool();
		this.drawingScrollPane.setViewportView(null);
		this.drawingView = null;
		// Show loading screen.
		showLoadingScreen = true;
		layoutComponents();
	}

	@Override
	public void zoomFactorChanged(double zoomFactor) {
		double scaleFactor = menuBar.getScaleFactor();
		for (UserInputListener l : listeners)
			l.zoomFactorChanged(scaleFactor);
	}
	
	@Override
	public void exportImage() {
		for (UserInputListener l : listeners)
			l.exportImage();
	}
	
	@Override
	public void figureDeselected(BaseFigure[] figures) {
		// Not used in this UI
	}
	
	@Override
	public void figureSelected(BaseFigure[] figures) {
		// Not used in this UI
	}
	
	public String[] getCurrentPaths() {
		return currentPaths;
	}
	
	@Override
	public void dependenciesHide() {
		for (UserInputListener l : listeners)
			l.dependenciesHide();
	}
	
	@Override
	public void librariesHide() {
		for (UserInputListener l : listeners)
			l.librariesHide();
	}
	
	public void hideLoadingScreen() {
		showLoadingScreen = false;
		progressPanel.removeAll();
		layoutComponents();
		locationBar.turnOnBar();
		menuBar.turnOnBar();
		/*
		if (isVisible()) {
			validate();
		}
		*/
	}
	
	@Override
	public void moduleHide() {
		for (UserInputListener listener : listeners)
			listener.moduleHide();
	}
	
	public void hideProperties() {
		showProperties = false;
		layoutComponents();
	}
	
	@Override
	public void smartLinesDisable() {
		for (UserInputListener l : listeners)
			l.smartLinesDisable();
	}
	
	@Override
	public void violationsHide() {
		for (UserInputListener l : listeners)
			l.violationsHide();
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

		// Initialize subcomponents
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
		
		// Update contents of the main components of GraphicsFrame
		updateComponentsLocaleStrings();
		layoutComponents();
		updateGUI();
		
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
	
	private void layoutComponents() {
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
	
	@Override
	public void moduleOpen(String[] paths) {
		for (UserInputListener l : listeners)
			l.moduleOpen(paths);
	}
	
	@Override
	public void zoomIn() {
		//for (UserInputListener l : listeners)
		//	l.zoomIn();
		presentationController.zoomIn();
	}
	
	@Override
	public void zoomIn(BaseFigure[] zoomedModuleFigure) {
		// Not used through this GUI
	}
	
	@Override
	public void zoomTypeChange(String zoomType) {
		for (UserInputListener l : listeners)
			l.zoomTypeChange(zoomType);
	}
	
	@Override
	public void zoomOut() {
		String[] secondLastPath = locationBar.getSecondLastPath();
		if (secondLastPath.length == 0) 
			for (UserInputListener l : listeners)
				l.zoomOut();
		else
			moduleOpen(secondLastPath);
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
	
	@Override
	public void refreshDrawing() {
		for (UserInputListener l : listeners)
			l.refreshDrawing();
	}
	
	public void refreshFrame() {
		updateComponentsLocaleStrings();
	}
	
	public void removeListener(UserInputListener listener) {
		listeners.remove(listener);
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
	
	@Override
	public void moduleRestoreHiddenModules() {
		for (UserInputListener listener : listeners)
			listener.moduleRestoreHiddenModules();
	}
	
	public void setCurrentPaths(String[] paths) {
		currentPaths = paths;
	}
	
	public void setOutOfDate() {
		menuBar.setOutOfDate();
	}
	
	public void setSelectedLayout(DrawingLayoutStrategy layoutStrategyOption) {
		menuBar.setSelectedLayoutStrategyItem(layoutStrategyOption);
	}
	
	public void setUpToDate() {
		menuBar.setUpToDate();
	}
	
	@Override
	public void zoomSliderSetZoomFactor(double zoomFactor) {
		menuBar.zoomSliderSetZoomFactor(zoomFactor);
	}
	
	@Override
	public void dependenciesShow() {
		for (UserInputListener l : listeners)
			l.dependenciesShow();
	}
	
	public void showDependenciesProperties(DependencyDTO[] dependencyDTOs) {
		showProperties();
		DependencyTable propertiesTable = new DependencyTable(dependencyDTOs);
		propertiesScrollPane.setViewportView(propertiesTable);
		//propertiesTable.setAutoCreateRowSorter(true);
	}
	
	@Override
	public void librariesShow() {
		for (UserInputListener l : listeners)
			l.librariesShow();
	}
	
	public void showLoadingScreen() {
		showLoadingScreen = true;
		layoutComponents();
		locationBar.turnOffBar();
		menuBar.turnOffBar();
	}
	
	public void showProperties() {
		showProperties = true;
		layoutComponents();
	}
	
	@Override
	public void smartLinesEnable() {
		for (UserInputListener l : listeners)
			l.smartLinesEnable();
	}
	
	@Override
	public void violationsShow() {
		for (UserInputListener l : listeners)
			l.violationsShow();
	}
	
	public void showViolationsProperties(ViolationDTO[] violationDTOs) {
		showProperties();
		ViolationTable propertiesTable = new ViolationTable(violationDTOs);
		propertiesScrollPane.setViewportView(propertiesTable);
	}
	
	public void turnOffDependencies() {
		menuBar.setDependeciesUIToInactive();
	}
	
	public void turnOffSmartLines() {
		menuBar.setSmartLinesUIToInactive();
	}
	
	public void turnOffViolations() {
		menuBar.setViolationsUIToInactive();
	}
	
	public void turnOnDependencies() {
		menuBar.setDependeciesUIToActive();
	}
	
	public void turnOnSmartLines() {
		menuBar.setSmartLinesUIToActive();
	}
	
	public void turnOnViolations() {
		menuBar.setViolationsUIToActive();
	}
	
	private void updateComponentsLocaleStrings() {
		HashMap<String, String> menuBarLocale = new HashMap<String, String>();
		menuBarLocale.put("DiagramOptions",
				localeService.getTranslatedString("DiagramOptions"));
		menuBarLocale.put("Options",
				localeService.getTranslatedString("Options"));
		menuBarLocale.put("Ok", localeService.getTranslatedString("OkButton"));
		menuBarLocale.put("Apply", localeService.getTranslatedString("Apply"));
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
	
	@Override
	public void usePanTool() {
		for (UserInputListener l : listeners)
			l.usePanTool();
	}
	
	@Override
	public void useSelectTool() {
		for (UserInputListener l : listeners)
			l.useSelectTool();
	}

	@Override
	public void proportionalLinesDisable() {
		for (UserInputListener l : listeners)
			l.proportionalLinesDisable();
	}

	@Override
	public void proportionalLinesEnable() {
		for (UserInputListener l : listeners)
			l.proportionalLinesEnable();
	}
}
