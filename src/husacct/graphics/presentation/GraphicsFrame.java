package husacct.graphics.presentation;

import husacct.ServiceProvider;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.control.IControlService;
import husacct.graphics.presentation.menubars.GraphicsMenuBar;
import husacct.graphics.presentation.menubars.LocationButtonActionListener;
import husacct.graphics.presentation.menubars.ZoomLocationBar;
import husacct.graphics.util.DrawingLayoutStrategy;
import husacct.graphics.util.UserInputListener;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

public class GraphicsFrame extends JInternalFrame {
	private static final long serialVersionUID = -4683140198375851034L;
	protected IControlService controlService;
	protected Logger logger = Logger.getLogger(GraphicsFrame.class);

	private DrawingView drawingView;
	private GraphicsMenuBar menuBar;
	private ZoomLocationBar locationBar;
	private String[] currentPaths;
	private JScrollPane drawingScrollPane, propertiesScrollPane, locationScrollPane;
	private JSplitPane centerPane;
	private String ROOT_LEVEL;
	private boolean showingProperties = false;

	private int frameTotalWidth;
	
	private String[] violationColumnKeysArray;
	private String[] dependencyColumnKeysArray;
	private ArrayList<String> violationColumnNames;
	private ArrayList<String> dependencyColumnNames;
	private String[] layoutStrategyItems; 
	private HashMap<String, DrawingLayoutStrategy> layoutStrategiesTranslations;

	private ArrayList<UserInputListener> listeners = new ArrayList<UserInputListener>();

	public GraphicsFrame(DrawingView givenDrawingView) {
		centerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		add(centerPane, BorderLayout.CENTER);
		
		setVisible(false);
		frameTotalWidth = getWidth();

		controlService = ServiceProvider.getInstance().getControlService();
		ROOT_LEVEL = controlService.getTranslatedString("Root");
		resetCurrentPaths();

		drawingView = givenDrawingView;
		initializeComponents();
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
	
	public void refreshFrame() {
		updateComponentsLocaleStrings();
	}
	
	public String[] getCurrentPaths() {
		return currentPaths;
	}

	public void resetCurrentPaths() {
		currentPaths = new String[]{};
	}

	public void setCurrentPaths(String[] paths) {
		currentPaths = paths;
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
		locationScrollPane.setPreferredSize(new Dimension(900, 35));
		locationScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		locationScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

		setLayout(new BorderLayout());
		add(menuBar, BorderLayout.NORTH);
		add(locationScrollPane, BorderLayout.SOUTH);

		dependencyColumnKeysArray = new String[] { "From", "To", "LineNumber", "DependencyType" };
		violationColumnKeysArray = new String[] { "ErrorMessage", "RuleType", "ViolationType", "Severity", "LineNumber" };

		updateComponentsLocaleStrings();
		layoutComponents();
		
		this.getRootPane().addComponentListener(new ComponentListener(){
			@Override
			public void componentResized(ComponentEvent e) {
				positionLayoutComponents();
				resizeLocationBar();
			}
			@Override
			public void componentShown(ComponentEvent e) {
				// Do nothing
			}
			@Override
			public void componentHidden(ComponentEvent e) {
				// Do nothing
			}
			@Override
			public void componentMoved(ComponentEvent e) {
				// Do nothing
			}
		});
	}
	
	private void resizeLocationBar(){
		if(locationScrollPane.getHorizontalScrollBar().isShowing()){
			locationScrollPane.setPreferredSize(new Dimension(900, 50));
		}
		else{
			locationScrollPane.setPreferredSize(new Dimension(900, 35));
		}
	}

	private void updateComponentsLocaleStrings() {
		HashMap<String, String> menuBarLocale = new HashMap<String, String>();
		menuBarLocale.put("Options", controlService.getTranslatedString("Options"));
		menuBarLocale.put("Zoom", controlService.getTranslatedString("Zoom"));
		menuBarLocale.put("ZoomIn", controlService.getTranslatedString("ZoomIn"));
		menuBarLocale.put("ZoomOut", controlService.getTranslatedString("ZoomOut"));
		menuBarLocale.put("Refresh", controlService.getTranslatedString("Refresh"));
		menuBarLocale.put("HideDependencies", controlService.getTranslatedString("HideDependencies"));
		menuBarLocale.put("ShowDependencies", controlService.getTranslatedString("ShowDependencies"));
		menuBarLocale.put("HideViolations", controlService.getTranslatedString("HideViolations"));
		menuBarLocale.put("ShowViolations", controlService.getTranslatedString("ShowViolations"));
		menuBarLocale.put("LineContextUpdates", controlService.getTranslatedString("LineContextUpdates"));
		menuBarLocale.put("ExportToImage", controlService.getTranslatedString("ExportToImage"));
		menuBarLocale.put("LayoutStrategy", controlService.getTranslatedString("LayoutStrategy"));
		menuBarLocale.put("DrawingOutOfDate", controlService.getTranslatedString("DrawingOutOfDate"));
		menuBar.setLocale(menuBarLocale);
		
		layoutStrategiesTranslations = new HashMap<String, DrawingLayoutStrategy>();
		int i = 0;
		layoutStrategyItems = new String[DrawingLayoutStrategy.values().length];
		for(DrawingLayoutStrategy strategy : DrawingLayoutStrategy.values()){
			String translation = controlService.getTranslatedString(strategy.toString());
			layoutStrategiesTranslations.put(translation, strategy);
			layoutStrategyItems[i] = translation;
			i++;
		}
		menuBar.setLayoutStrategyItems(layoutStrategyItems);
		
		dependencyColumnNames = new ArrayList<String>();
		for (String key : dependencyColumnKeysArray) {
			dependencyColumnNames.add(controlService.getTranslatedString(key));
		}

		violationColumnNames = new ArrayList<String>();
		for (String key : violationColumnKeysArray) {
			violationColumnNames.add(controlService.getTranslatedString(key));
		}

		ROOT_LEVEL = controlService.getTranslatedString("Root");
		locationBar.setLocale(ROOT_LEVEL);
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

		if (isVisible()) {
			validate();
		}
	}

	private void positionLayoutComponents() {
		if (showingProperties) {
			centerPane.setSize(getWidth(), getHeight());
			int smallerSize = centerPane.getSize().height / 5 * 3;
			centerPane.setDividerLocation(smallerSize);
			centerPane.setDividerSize(10);
			centerPane.validate();
			centerPane.updateUI();
		}
	}

	private void createMenuBar() {
		menuBar = new GraphicsMenuBar();
		menuBar.setSize(frameTotalWidth, 20);

		menuBar.setZoomInAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				moduleZoomIn();
			}
		});
		
		menuBar.setZoomOutAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] secondLastPath = locationBar.getSecondLastPath();
				if(secondLastPath.length==0){
					moduleZoomOut();
				}else{
					moduleOpen(secondLastPath);
				}
			}
		});
		menuBar.setRefreshAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshDrawing();
			}
		});
		menuBar.setToggleDependenciesAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleDependencies();
			}
		});
		menuBar.setToggleViolationsAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleViolations();
			}
		});
		menuBar.setToggleSmartLinesAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleContextUpdates();
			}
		});
		menuBar.setExportToImageAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportToImage();
			}
		});
		menuBar.setLayoutStrategyAction(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				DrawingLayoutStrategy selectedStrategy = null;
				try{
					selectedStrategy = layoutStrategiesTranslations.get(menuBar.getSelectedLayoutStrategyItem());
					changeLayoutStrategy(selectedStrategy);
				}catch(Exception ex){
					logger.debug("Could not find the selected layout strategy \""+(selectedStrategy==null ? "null" :selectedStrategy.toString())+"\".");
				}
			}
		});
		menuBar.setZoomChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
		    	zoomChanged();
			}
		});
		menuBar.setOutOfDateAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setUpToDate();
				refreshDrawing();
			}
		});
		add(menuBar, java.awt.BorderLayout.NORTH);
	}

	public void createLocationBar() {
		locationBar = new ZoomLocationBar();
		
		locationBar.addLocationButtonPressListener(new LocationButtonActionListener() {
			@Override
			public void actionPerformed(String[] selectedPaths) {
				moduleOpen(selectedPaths);
			}
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		updateGUI();
	}

	public void updateGUI() {
		locationBar.updateLocationBar(getCurrentPaths());
		updateUI();
	}
	
	public void setSelectedLayout(DrawingLayoutStrategy layoutStrategyOption) {
		menuBar.setSelectedLayoutStrategyItem(controlService.getTranslatedString(layoutStrategyOption.toString()));
	}

	private void moduleZoomIn() {
		for (UserInputListener l : listeners) {
			l.moduleZoom();
		}
	}
	
	private void moduleOpen(String[] paths) {
		for (UserInputListener l : listeners) {
			l.moduleOpen(paths);
		}
	}

	private void moduleZoomOut() {
		for (UserInputListener l : listeners) {
			l.moduleZoomOut();
		}
	}

	private void exportToImage() {
		for (UserInputListener l : listeners) {
			l.exportToImage();
		}
	}
	
	private void zoomChanged() {
		double scaleFactor = menuBar.getScaleFactor();
		for (UserInputListener l : listeners) {
			l.drawingZoomChanged(scaleFactor);
		}
	}	
	
	protected void toggleDependencies() {
		for (UserInputListener l : listeners) {
			l.toggleDependencies();
		}
	}

	private void toggleViolations() {
		for (UserInputListener l : listeners) {
			l.toggleViolations();
		}
	}

	protected void toggleContextUpdates() {
		for (UserInputListener l : listeners) {
			l.toggleSmartLines();
		}
	}

	private void refreshDrawing() {
		for (UserInputListener l : listeners) {
			l.refreshDrawing();
		}
	}

	private void changeLayoutStrategy(DrawingLayoutStrategy selectedStrategyEnum) {
		for (UserInputListener l : listeners) {
			l.changeLayoutStrategy(selectedStrategyEnum);
		}
	}

	public void addListener(UserInputListener listener) {
		listeners.add(listener);
	}

	public void removeListener(UserInputListener listener) {
		listeners.remove(listener);
	}

	public void showViolationsProperties(ViolationDTO[] violationDTOs) {
		propertiesScrollPane.setViewportView(createViolationsTable(violationDTOs));
		showProperties();
	}

	// TODO: Sort violations based on severity value
	private JTable createViolationsTable(ViolationDTO[] violationDTOs) {
		ArrayList<String[]> rows = new ArrayList<String[]>();
		for (ViolationDTO violation : violationDTOs) {
			String message = violation.message;

			String ruleTypeDescription = "none";
			if (null != violation.ruleType) {
				ruleTypeDescription = violation.ruleType.getDescriptionKey();
			}

			String violationTypeDescription = "none";
			if (null != violation.violationType) {
				violationTypeDescription = violation.violationType.getDescriptionKey();
			}

			String severity = "" + violation.severityValue;
			String line = "" + violation.linenumber;
			rows.add(new String[] { message, ruleTypeDescription, violationTypeDescription, severity, line });
		}

		JTable propertiesTable = new JTable(rows.toArray(new String[][] {}), violationColumnNames.toArray(new String[] {}));
		setColumnWidths(propertiesTable, violationColumnKeysArray);
		return propertiesTable;
	}

	public void hidePropertiesPane() {
		hideProperties();
	}

	public void showDependenciesProperties(DependencyDTO[] dependencyDTOs) {
		propertiesScrollPane.setViewportView(createDependencyTable(dependencyDTOs));
		showProperties();
	}

	private Component createDependencyTable(DependencyDTO[] dependencyDTOs) {
		ArrayList<String[]> rows = new ArrayList<String[]>();
		for (DependencyDTO dependency : dependencyDTOs) {
			rows.add(new String[] { dependency.from, dependency.to, "" + dependency.lineNumber, dependency.type });
		}
		JTable propertiesTable = new JTable(rows.toArray(new String[][] {}), dependencyColumnNames.toArray(new String[] {}));
		setColumnWidths(propertiesTable, dependencyColumnKeysArray);
		return propertiesTable;
	}
	
	private void setColumnWidths(JTable table, String[] columnNames){
		TableColumn column = null;
		int lineNumberColumnWidth = 50;
		int otherColumnWidth = (getWidth() / (table.getColumnCount())) - (lineNumberColumnWidth / table.getColumnCount());
		for (int i = 0; i < table.getColumnCount(); i++) {
			column = table.getColumnModel().getColumn(i);
			if (columnNames[i] == "LineNumber") {
				column.setPreferredWidth(lineNumberColumnWidth);
			} else {
				column.setPreferredWidth(otherColumnWidth);
			}
		}
	}
	
	public void showProperties(){
		showingProperties = true;
		layoutComponents();
	}
	
	public void hideProperties(){
		showingProperties = false;
		layoutComponents();
	}
	
	public void turnOnDependencies() {
		menuBar.setDependencyToggle(true);
	}
	
	public void turnOffDependencies() {
		menuBar.setDependencyToggle(false);
	}
	
	public void turnOnViolations() {
		menuBar.setViolationToggle(true);
	}

	public void turnOffViolations() {
		menuBar.setViolationToggle(false);
	}

	public void turnOnSmartLines() {
		menuBar.setContextUpdatesToggle(true);
	}
	
	public void turnOffSmartLines() {
		menuBar.setContextUpdatesToggle(false);
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

		if (isVisible()) {
			validate();
		}
	}

	public void hideLoadingScreen() {
		layoutComponents();
		locationBar.turnOnBar();
		menuBar.turnOnBar();
		
		if (isVisible()) {
			validate();
		}
	}
	
	public void setUpToDate() {
		menuBar.setUpToDate();
	}

	public void setOutOfDate() {
		menuBar.setOutOfDate();
	}
}
