package husacct.graphics.presentation;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.task.UserInputListener;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

public class GraphicsFrame extends JInternalFrame {
	private static final long serialVersionUID = -4683140198375851034L;

	private DrawingView drawingView;
	private JMenuBar menuBar, locationBar;
	private JCheckBoxMenuItem showViolationsOptionMenu;
	private String currentPath;
	private JScrollPane drawingScollPane, propertiesScrollPane;
	private JComponent centerPane;
	private String ROOT_LEVEL = "Root";
	private final String LOCATION_SEPERATOR = ".";
	private boolean showingProperties = false;

	int frameTotalWidth = getWidth();
	int menuItemMaxWidth = 140;
	int menuItemMaxHeight = 45;

	private ArrayList<UserInputListener> listeners = new ArrayList<UserInputListener>();
	private HashMap<JButton, String> buttonPaths = new HashMap<JButton, String>();

	public GraphicsFrame(DrawingView givenDrawingView) {
		resetCurrentPath();
		setVisible(false);
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
		drawingScollPane = new JScrollPane();
		drawingScollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		drawingScollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		drawingScollPane.setViewportView(drawingView);

		propertiesScrollPane = new JScrollPane();
		propertiesScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		propertiesScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		createMenuBar();
		createLocationBar();

		setLayout(new java.awt.BorderLayout());
		add(menuBar, BorderLayout.NORTH);
		add(locationBar, BorderLayout.SOUTH);

		layoutComponents(false);
	}

	private void layoutComponents(boolean showProperties) {
		showingProperties = showProperties;
		if (centerPane != null) {
			remove(centerPane);
		}

		if (!showProperties) {
			centerPane = drawingScollPane;
		} else {
			centerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, drawingScollPane, propertiesScrollPane);
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
		JButton goToParentMenu = new JButton("Level up");
		goToParentMenu.setSize(50, menuItemMaxHeight);
		goToParentMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moduleZoomOut();
			}
		});
		menuBar.add(goToParentMenu);

		JButton refreshMenu = new JButton("Refresh");
		refreshMenu.setSize(50, menuItemMaxHeight);
		refreshMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshDrawing();
			}
		});
		menuBar.add(refreshMenu);

		showViolationsOptionMenu = new JCheckBoxMenuItem("Show violations");
		showViolationsOptionMenu.setSize(40, menuItemMaxHeight);
		showViolationsOptionMenu.setMaximumSize(new Dimension(menuItemMaxWidth, menuItemMaxHeight));
		showViolationsOptionMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleViolations();
			}
		});
		menuBar.add(showViolationsOptionMenu);

		JButton exportToImageMenu = new JButton("Export to image");
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
		addLocationButton(ROOT_LEVEL, "");
		
		String path = getCurrentPath();
		String pathUntilNow = "";
		String[] pathParts = new String[]{};
		if(!path.equals("")){
			pathParts = path.split("\\" + LOCATION_SEPERATOR);
		}
		if(pathParts.length > 0){
			addLocationSeperator();
			for (String part : pathParts) {
				pathUntilNow = pathUntilNow + part;
				addLocationButton(part, pathUntilNow);
	
				if (!pathParts[pathParts.length - 1].equals(part)) {
					addLocationSeperator();
					pathUntilNow = pathUntilNow + LOCATION_SEPERATOR;
				}
			}
		}
		updateUI();
	}
	
	private void addLocationButton(String levelName, String fullPath){
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
		locationBar.add(locationStringButton);
	}
	
	private void addLocationSeperator(){
		locationBar.add(new JLabel(" " + LOCATION_SEPERATOR + " "));
	}

	private void clearLocationBar() {
		locationBar.removeAll();
	}

	public String getCurrentPath() {
		return currentPath;
	}
	
	public void resetCurrentPath(){
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
		String[] columnNames = { "Error Message", "Rule Type", "Violation Type", "Severity", "Line" };

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

		return new JTable(rows.toArray(new String[][] {}), columnNames);
	}

	public void hidePropertiesPane() {
		layoutComponents(false);
	}

	public void showDependenciesProperties(DependencyDTO[] dependencyDTOs) {
		propertiesScrollPane.setViewportView(createDependencyTable(dependencyDTOs));
		layoutComponents(true);
	}

	private Component createDependencyTable(DependencyDTO[] dependencyDTOs) {
		String[] columnNames = { "From", "To", "Line number", "Dependency Type" };

		ArrayList<String[]> rows = new ArrayList<String[]>();
		for (DependencyDTO dependency : dependencyDTOs) {
			rows.add(new String[] { dependency.from, dependency.to, "" + dependency.lineNumber, dependency.type });
		}

		return new JTable(rows.toArray(new String[][] {}), columnNames);
	}

	public void turnOnViolations() {
		showViolationsOptionMenu.setState(true);
	}
	
	public void turnOffViolations() {
		showViolationsOptionMenu.setState(false);
	}
}
