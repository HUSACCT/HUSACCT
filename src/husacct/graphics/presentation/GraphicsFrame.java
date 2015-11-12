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
	
	private DrawingView						drawingView;
	private GraphicsMenuBar					menuBar;
	private ZoomLocationBar					locationBar;
	private String[]						currentPaths;
	private JScrollPane						drawingScrollPane, propertiesScrollPane, locationScrollPane;
	private JSplitPane						centerPane;
	private String							ROOT_LEVEL;
	private boolean							showingProperties	= false;
	
	private int								frameTotalWidth;
	private int								centerPaneHeight;
	
	private ArrayList<UserInputListener>	listeners			= new ArrayList<UserInputListener>();
	
	public GraphicsFrame(DrawingView givenDrawingView) {
		centerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		add(centerPane, BorderLayout.CENTER);
		
		setVisible(false);
		frameTotalWidth = getWidth();
		
		ROOT_LEVEL = localeService.getTranslatedString("Root");
		resetCurrentPaths();
		
		drawingView = givenDrawingView;
		
		initializeComponents();
		
		//logger.info("listeners added");
		
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
	public void changeLayoutStrategy(DrawingLayoutStrategy selectedStrategyEnum) {
		for (UserInputListener l : listeners)
			l.changeLayoutStrategy(selectedStrategyEnum);
	}
	
	public void createLocationBar() {
		locationBar = new ZoomLocationBar();
		locationBar
				.addLocationButtonPressListener(new LocationButtonActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
					}
					
					@Override
					public void actionPerformed(String[] selectedPaths) {
						moduleOpen(selectedPaths);
					}
				});
		updateGUI();
	}
	
	private void createMenuBar() {
		menuBar = new GraphicsMenuBar();
		menuBar.addListener(this);
		menuBar.setSize(frameTotalWidth, 20);
		
		menuBar.setOutOfDateAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setUpToDate();
				refreshDrawing();
			}
		});
		add(menuBar, java.awt.BorderLayout.NORTH);
	}
	
	@Override
	public void drawingZoomChanged(double zoomFactor) {
		double scaleFactor = menuBar.getScaleFactor();
		for (UserInputListener l : listeners)
			l.drawingZoomChanged(scaleFactor);
	}
	
	@Override
	public void exportToImage() {
		for (UserInputListener l : listeners)
			l.exportToImage();
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
	public void hideDependencies() {
		for (UserInputListener l : listeners)
			l.hideDependencies();
	}
	
	@Override
	public void hideLibraries() {
		for (UserInputListener l : listeners)
			l.hideLibraries();
	}
	
	public void hideLoadingScreen() {
		layoutComponents();
		locationBar.turnOnBar();
		menuBar.turnOnBar();
		
		if (isVisible()) validate();
	}
	
	@Override
	public void hideModules() {
		for (UserInputListener listener : listeners)
			listener.hideModules();
	}
	
	public void hideProperties() {
		showingProperties = false;
		layoutComponents();
	}
	
	@Override
	public void hideSmartLines() {
		for (UserInputListener l : listeners)
			l.hideSmartLines();
	}
	
	@Override
	public void hideViolations() {
		for (UserInputListener l : listeners)
			l.hideViolations();
	}
	
	private void initializeComponents() {
		drawingScrollPane = new JScrollPane();
		drawingScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		drawingScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		drawingScrollPane.getVerticalScrollBar().setUnitIncrement(10);
		drawingScrollPane.setViewportView(drawingView);

		propertiesScrollPane = new JScrollPane();
		propertiesScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		propertiesScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		createMenuBar();
		createLocationBar();
		
		locationScrollPane = new JScrollPane(locationBar);
		locationScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		locationScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		
		setLayout(new BorderLayout());
		add(menuBar, BorderLayout.NORTH);
		add(locationScrollPane, BorderLayout.SOUTH);
		
		updateComponentsLocaleStrings();
		layoutComponents();
		
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
		
		drawingView.initializePanTool(drawingScrollPane.getViewport(),
				drawingScrollPane);
	}
	
	private void layoutComponents() {
		centerPane.removeAll();
		if (!showingProperties) {
			centerPane.add(drawingScrollPane);
			centerPane.setDividerSize(0);
		} else {
			centerPane.add(drawingScrollPane);
			centerPane.add(propertiesScrollPane);
			positionLayoutComponents();
			centerPane.setOneTouchExpandable(true);
			centerPane.setContinuousLayout(true);
		}
		
		if (isVisible()) validate();
	}
	
	@Override
	public void moduleOpen(String[] paths) {
		for (UserInputListener l : listeners)
			l.moduleOpen(paths);
	}
	
	@Override
	public void moduleZoom() {
		for (UserInputListener l : listeners)
			l.moduleZoom();
	}
	
	@Override
	public void moduleZoom(BaseFigure[] zoomedModuleFigure) {
		// Not used through this GUI
	}
	
	@Override
	public void moduleZoom(String zoomType) {
		for (UserInputListener l : listeners)
			l.moduleZoom(zoomType);
	}
	
	@Override
	public void moduleZoomOut() {
		String[] secondLastPath = locationBar.getSecondLastPath();
		if (secondLastPath.length == 0) for (UserInputListener l : listeners)
			l.moduleZoomOut();
		else
			moduleOpen(secondLastPath);
	}
	
	private void positionLayoutComponents() {
		if (showingProperties) {
			int centerPaneHeight = getHeight();
			int centerPaneWidth = getWidth();
			centerPane.setSize(centerPaneWidth, centerPaneHeight);
			int smallerSize = centerPaneHeight - (centerPaneHeight / 3);
			centerPane.setDividerLocation(smallerSize);
			centerPane.setDividerSize(10);
			centerPane.validate();
			centerPane.updateUI();
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
	public void restoreModules() {
		for (UserInputListener listener : listeners)
			listener.restoreModules();
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
	public void setZoomSlider(double zoomFactor) {
		menuBar.setZoomSlider(zoomFactor);
	}
	
	@Override
	public void showDependencies() {
		for (UserInputListener l : listeners)
			l.showDependencies();
	}
	
	public void showDependenciesProperties(DependencyDTO[] dependencyDTOs) {
		showProperties();
		DependencyTable propertiesTable = new DependencyTable(dependencyDTOs);
		propertiesScrollPane.setViewportView(propertiesTable);
		//propertiesTable.setAutoCreateRowSorter(true);
	}
	
	@Override
	public void showLibraries() {
		for (UserInputListener l : listeners)
			l.showLibraries();
	}
	
	public void showLoadingScreen() {
		locationBar.turnOffBar();
		menuBar.turnOffBar();
		centerPane.removeAll();
		
		JPanel loadingContainerPanel = new JPanel();
		JPanel progressPanel = new JPanel();
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		
		progressPanel.add(progressBar);
		loadingContainerPanel.add(progressPanel);
		
		centerPane.add(loadingContainerPanel);
		centerPane.setDividerSize(0);
		
		add(centerPane, java.awt.BorderLayout.CENTER);
		
		if (isVisible()) validate();
	}
	
	public void showProperties() {
		showingProperties = true;
		layoutComponents();
	}
	
	@Override
	public void showSmartLines() {
		for (UserInputListener l : listeners)
			l.showSmartLines();
	}
	
	@Override
	public void showViolations() {
		for (UserInputListener l : listeners)
			l.showViolations();
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
	public void disableThickLines() {
		for (UserInputListener l : listeners)
			l.disableThickLines();
	}

	@Override
	public void enableThickLines() {
		for (UserInputListener l : listeners)
			l.enableThickLines();
	}
}
