package husacct.graphics.presentation;

import husacct.ServiceProvider;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.control.IControlService;
import husacct.graphics.presentation.menubars.GraphicsMenuBar;
import husacct.graphics.presentation.menubars.LocationButtonActionListener;
import husacct.graphics.presentation.menubars.ZoomLocationBar;
import husacct.graphics.task.UserInputListener;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class GraphicsFrame extends JInternalFrame {
	protected IControlService controlService;
	private static final long serialVersionUID = -4683140198375851034L;

	private DrawingView drawingView;
	private GraphicsMenuBar menuBar;
	private ZoomLocationBar locationBar;
	private String currentPath;
	private JScrollPane drawingScrollPane, propertiesScrollPane;
	private JSplitPane centerPane;
	private String ROOT_LEVEL;
	private boolean showingProperties = false;

	private int frameTotalWidth;
	
	private String[] violationColumnKeysArray;
	private String[] dependencyColumnKeysArray;
	private ArrayList<String> violationColumnNames;
	private ArrayList<String> dependencyColumnNames;

	private ArrayList<UserInputListener> listeners = new ArrayList<UserInputListener>();

	public GraphicsFrame(DrawingView givenDrawingView) {
		setVisible(false);
		frameTotalWidth = getWidth();

		controlService = ServiceProvider.getInstance().getControlService();
		ROOT_LEVEL = controlService.getTranslatedString("Root");
		resetCurrentPath();

		drawingView = givenDrawingView;
		initializeComponents();
		setSize(500, 500);
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
	
	public String getCurrentPath() {
		return currentPath;
	}

	public void resetCurrentPath() {
		currentPath = "";
	}

	public void setCurrentPath(String path) {
		currentPath = path;
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

		setLayout(new BorderLayout());
		add(menuBar, BorderLayout.NORTH);
		add(locationBar, BorderLayout.SOUTH);

		dependencyColumnKeysArray = new String[] { "From", "To", "LineNumber", "DependencyType" };
		violationColumnKeysArray = new String[] { "ErrorMessage", "RuleType", "ViolationType", "Severity", "LineNumber" };

		updateComponentsLocaleStrings();

		layoutComponents();
	}

	private void updateComponentsLocaleStrings() {
		HashMap<String, String> menuBarLocale = new HashMap<String, String>();
		menuBarLocale.put("LevelUp", controlService.getTranslatedString("LevelUp"));
		menuBarLocale.put("Refresh", controlService.getTranslatedString("Refresh"));
		menuBarLocale.put("ShowViolations", controlService.getTranslatedString("ShowViolations"));
		menuBarLocale.put("ExportToImage", controlService.getTranslatedString("ExportToImage"));
		menuBar.setLocale(menuBarLocale);
		
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
		if (centerPane != null) {
			remove(centerPane);
		}

		centerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
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
		add(centerPane, java.awt.BorderLayout.CENTER);

		if (isVisible()) {
			validate();
		}
	}

	private void positionLayoutComponents() {
		if (showingProperties) {
			centerPane.setSize(getWidth(), getHeight());
			int smallerSize = centerPane.getSize().height / 5 * 3;
			centerPane.setDividerLocation(smallerSize);
		}
	}

	private void createMenuBar() {
		menuBar = new GraphicsMenuBar();
		menuBar.setSize(frameTotalWidth, 20);
		menuBar.setLevelUpAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moduleZoomOut();
			}
		});
		menuBar.setRefreshAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshDrawing();
			}
		});
		menuBar.setToggleViolationsAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleViolations();
			}
		});
		menuBar.setExportToImageAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportToImage();
			}
		});
		add(menuBar, java.awt.BorderLayout.NORTH);
	}

	public void createLocationBar() {
		locationBar = new ZoomLocationBar();
		locationBar.addLocationButtonPressListener(new LocationButtonActionListener() {
			@Override
			public void actionPerformed(String selectedPath) {
				moduleOpen(selectedPath);
			}
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		locationBar.setSize(frameTotalWidth, 20);

		updateGUI();

		add(locationBar, java.awt.BorderLayout.SOUTH);
	}

	public void updateGUI() {
		locationBar.updateLocationBar(getCurrentPath());
		updateUI();
	}

	private void moduleOpen(String path) {
		for (UserInputListener l : listeners) {
			l.moduleOpen(path);
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

	private void toggleViolations() {
		for (UserInputListener l : listeners) {
			l.toggleViolations();
		}
	}

	private void refreshDrawing() {
		for (UserInputListener l : listeners) {
			l.refreshDrawing();
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
	
	public void turnOnViolations() {
		menuBar.setViolationToggle(true);
	}

	public void turnOffViolations() {
		menuBar.setViolationToggle(false);
	}
}
