package husacct.graphics.presentation;

import husacct.ServiceProvider;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.control.IControlService;
import husacct.graphics.task.UserInputListener;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

public class GraphicsFrame extends JInternalFrame {
	protected IControlService controlService;
	private static final long serialVersionUID = -4683140198375851034L;

	private DrawingView drawingView;
	private JMenuBar menuBar, locationBar;
	private JCheckBox showViolationsOptionMenu;
	private String currentPath;
	private JScrollPane drawingScrollPane, propertiesScrollPane;
	private JComponent centerPane;
	private String ROOT_LEVEL;
	private final String LOCATION_SEPERATOR = ".";
	private boolean showingProperties = false;

	int frameTotalWidth = getWidth();
	int menuItemMaxWidth = 140;
	int menuItemMaxHeight = 45;

	private JButton goToParentMenu, refreshMenu, exportToImageMenu, rootLocationButton;
	private ArrayList<String> violationColumnNames;
	private ArrayList<String> dependencyColumnNames;

	private ArrayList<UserInputListener> listeners = new ArrayList<UserInputListener>();
	private HashMap<JButton, String> buttonPaths = new HashMap<JButton, String>();

	public GraphicsFrame(DrawingView givenDrawingView) {
		setVisible(false);

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

		setLayout(new java.awt.BorderLayout());
		add(menuBar, BorderLayout.NORTH);
		add(locationBar, BorderLayout.SOUTH);

		updateComponents();

		layoutComponents(false);
	}

	private void updateComponents() {
		goToParentMenu.setText(controlService.getTranslatedString("LevelUp"));
		refreshMenu.setText(controlService.getTranslatedString("Refresh"));
		showViolationsOptionMenu.setText(controlService.getTranslatedString("ShowViolations"));
		exportToImageMenu.setText(controlService.getTranslatedString("ExportToImage"));

		String[] dependencyColumnNamesArray = { "From", "To", "LineNumber", "DependencyType" };
		dependencyColumnNames = new ArrayList<String>();
		for (String key : dependencyColumnNamesArray) {
			dependencyColumnNames.add(controlService.getTranslatedString(key));
		}

		violationColumnNames = new ArrayList<String>();
		String[] violationColumnNamesArray = { "ErrorMessage", "RuleType", "ViolationType", "Severity", "LineNumber" };
		for (String key : violationColumnNamesArray) {
			violationColumnNames.add(controlService.getTranslatedString(key));
		}

		ROOT_LEVEL = controlService.getTranslatedString("Root");
		if (null != rootLocationButton) {
			rootLocationButton.setText(ROOT_LEVEL);
		}
	}

	public void refreshFrame() {
		updateComponents();
	}

	private void layoutComponents(boolean showProperties) {
		showingProperties = showProperties;
		if (centerPane != null) {
			remove(centerPane);
		}

		if (!showProperties) {
			centerPane = drawingScrollPane;
		} else {
			centerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, drawingScrollPane, propertiesScrollPane);
			positionLayoutComponents();
			((JSplitPane) centerPane).setOneTouchExpandable(true);
			((JSplitPane) centerPane).setContinuousLayout(true);
		}
		add(centerPane, java.awt.BorderLayout.CENTER);

		if (isVisible()) {
			validate();
		}
	}

	private void positionLayoutComponents() {
		if (showingProperties) {
			((JSplitPane) centerPane).setSize(getWidth(), getHeight());
			int smallerSize = ((JSplitPane) centerPane).getSize().height / 5 * 3;
			((JSplitPane) centerPane).setDividerLocation(smallerSize);
		}
	}

	private void createMenuBar() {
		menuBar = new JMenuBar();
		menuBar.setSize(frameTotalWidth, 20);
		goToParentMenu = new JButton();
		goToParentMenu.setSize(50, menuItemMaxHeight);
		goToParentMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moduleZoomOut();
			}
		});
		menuBar.add(goToParentMenu);

		refreshMenu = new JButton();
		refreshMenu.setSize(50, menuItemMaxHeight);
		refreshMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshDrawing();
			}
		});
		menuBar.add(refreshMenu);

		showViolationsOptionMenu = new JCheckBox();
		showViolationsOptionMenu.setSize(40, menuItemMaxHeight);
		showViolationsOptionMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleViolations();
			}
		});
		menuBar.add(showViolationsOptionMenu);

		exportToImageMenu = new JButton();
		exportToImageMenu.setSize(50, menuItemMaxHeight);
		exportToImageMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportToImage();
			}
		});
		menuBar.add(exportToImageMenu);

		add(menuBar, java.awt.BorderLayout.NORTH);
	}

	public void createLocationBar() {
		locationBar = new JMenuBar();
		locationBar.setSize(frameTotalWidth, 20);

		updateLocationBar();

		add(locationBar, java.awt.BorderLayout.SOUTH);
	}

	private void updateLocationBar() {
		clearLocationBar();
		createAndAddRootLocationButton();

		String path = getCurrentPath();
		String pathUntilNow = "";
		String[] pathParts = new String[] {};
		if (!path.equals("")) {
			pathParts = path.split("\\" + LOCATION_SEPERATOR);
		}
		if (pathParts.length > 0) {
			addLocationSeperator();
			for (String part : pathParts) {
				pathUntilNow = pathUntilNow + part;
				createAndAddLocationButton(part, pathUntilNow);

				if (!pathParts[pathParts.length - 1].equals(part)) {
					addLocationSeperator();
					pathUntilNow = pathUntilNow + LOCATION_SEPERATOR;
				}
			}
		}
		updateUI();
	}

	private void createAndAddRootLocationButton() {
		rootLocationButton = createLocationButton(ROOT_LEVEL, "");
		locationBar.add(rootLocationButton);
	}

	private void createAndAddLocationButton(String levelName, String fullPath) {
		locationBar.add(createLocationButton(levelName, fullPath));
	}

	private JButton createLocationButton(String levelName, String fullPath) {
		JButton locationStringButton = new JButton(levelName);
		locationStringButton.setSize(10, menuItemMaxHeight);
		locationStringButton.setMargin(new Insets(0, 0, 0, 0));
		buttonPaths.put(locationStringButton, fullPath);
		locationStringButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String selectedPath = buttonPaths.get(event.getSource());
				moduleOpen(selectedPath);
			}
		});
		return locationStringButton;
	}

	private void addLocationSeperator() {
		locationBar.add(new JLabel(" " + LOCATION_SEPERATOR + " "));
	}

	private void clearLocationBar() {
		locationBar.removeAll();
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

	public void setCurrentPathAndUpdateGUI(String path) {
		setCurrentPath(path);
		updateLocationBar();
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
		layoutComponents(true);
	}

	// TODO: Sort violations based on severity value
	// TODO: Make small columns smaller in the GUI
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

		return new JTable(rows.toArray(new String[][] {}), violationColumnNames.toArray(new String[] {}));
	}

	public void hidePropertiesPane() {
		layoutComponents(false);
	}

	public void showDependenciesProperties(DependencyDTO[] dependencyDTOs) {
		propertiesScrollPane.setViewportView(createDependencyTable(dependencyDTOs));
		layoutComponents(true);
	}

	private Component createDependencyTable(DependencyDTO[] dependencyDTOs) {
		ArrayList<String[]> rows = new ArrayList<String[]>();
		for (DependencyDTO dependency : dependencyDTOs) {
			rows.add(new String[] { dependency.from, dependency.to, "" + dependency.lineNumber, dependency.type });
		}
		return new JTable(rows.toArray(new String[][] {}), dependencyColumnNames.toArray(new String[]{}));
	}

	public void turnOnViolations() {
		showViolationsOptionMenu.setSelected(true);
	}

	public void turnOffViolations() {
		showViolationsOptionMenu.setSelected(false);
	}
}
