package husacct.graphics.presentation;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.task.UserInputListener;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

public class GraphicsFrame extends JInternalFrame {
	private static final long serialVersionUID = -4683140198375851034L;

	private DrawingView drawingView;
	private JMenuBar menuBar, locationBar;
	private JMenuItem locationString;
	private JScrollPane drawingScollPane, propertiesScrollPane;
	private JComponent centerPane;
	private String ROOT_LEVEL = "Root";
	private boolean showingProperties = false;

	private ArrayList<UserInputListener> listeners = new ArrayList<UserInputListener>();

	public GraphicsFrame(DrawingView givenDrawingView) {
		setVisible(false);
		drawingView = givenDrawingView;
		initializeComponents();
		setCurrentPathInfo("");
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
		propertiesScrollPane
				.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		propertiesScrollPane
				.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		createMenuBars();

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
			centerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, drawingScollPane,
					propertiesScrollPane);
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

	private void createMenuBars() {
		int totalWidth = getWidth();
		int menuItemMaxWidth = 140;
		int menuItemMaxHeight = 45;

		menuBar = new JMenuBar();
		menuBar.setSize(totalWidth, 20);
		JButton goToParentMenu = new JButton("Level up");
		goToParentMenu.setSize(50, menuItemMaxHeight);
		goToParentMenu.setMaximumSize(new Dimension(90, menuItemMaxHeight));
		goToParentMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moduleZoomOut();
			}
		});
		menuBar.add(goToParentMenu);
		
		JButton refreshMenu = new JButton("Refresh");
		refreshMenu.setSize(50, menuItemMaxHeight);
		refreshMenu.setMaximumSize(new Dimension(90, menuItemMaxHeight));
		refreshMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshDrawing();
			}
		});
		menuBar.add(refreshMenu);

		JCheckBoxMenuItem showViolationsOptionMenu = new JCheckBoxMenuItem("Show violations");
		showViolationsOptionMenu.setSize(50, menuItemMaxHeight);
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
		exportToImageMenu.setMaximumSize(new Dimension(menuItemMaxWidth, menuItemMaxHeight));
		exportToImageMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportToImage();
			}
		});
		menuBar.add(exportToImageMenu);

		add(menuBar, java.awt.BorderLayout.NORTH);

		locationBar = new JMenuBar();
		locationBar.setSize(totalWidth, 20);

		locationString = new JMenuItem(ROOT_LEVEL);
		locationString.setSize(menuItemMaxWidth, menuItemMaxHeight);
		locationString.setMinimumSize(new Dimension(menuItemMaxWidth, menuItemMaxHeight));
		locationBar.add(locationString);

		add(locationBar, java.awt.BorderLayout.WEST);
	}

	public void setCurrentPathInfo(String path) {
		if (path.equals("")) {
			path = ROOT_LEVEL;
		}
		locationString.setText(path);
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
}
