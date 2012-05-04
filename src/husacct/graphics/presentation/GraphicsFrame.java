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

	public GraphicsFrame(DrawingView drawingView) {
		this.drawingView = drawingView;
		this.initializeComponents();
		this.setCurrentPathInfo("");
		this.setSize(500, 500);
		this.addHierarchyBoundsListener(new HierarchyBoundsListener() {
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
		this.drawingScollPane = new JScrollPane();
		this.drawingScollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.drawingScollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.drawingScollPane.setViewportView(drawingView);

		this.propertiesScrollPane = new JScrollPane();
		this.propertiesScrollPane
				.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.propertiesScrollPane
				.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		createMenuBars();

		this.setLayout(new java.awt.BorderLayout());
		this.add(this.menuBar, BorderLayout.NORTH);
		this.add(this.locationBar, BorderLayout.SOUTH);

		this.layoutComponents(false);
	}

	private void layoutComponents(boolean showProperties) {
		showingProperties = showProperties;
		if (this.centerPane != null) {
			this.remove(this.centerPane);
		}

		if (!showProperties) {
			this.centerPane = this.drawingScollPane;
		} else {
			this.centerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.drawingScollPane,
					this.propertiesScrollPane);
			positionLayoutComponents();
			((JSplitPane) centerPane).setOneTouchExpandable(true);
			((JSplitPane) centerPane).setContinuousLayout(true);
		}
		this.add(this.centerPane, java.awt.BorderLayout.CENTER);

		if (this.isVisible()) {
			this.validate();
		}
	}

	private void positionLayoutComponents() {
		if (showingProperties) {
			((JSplitPane) centerPane).setSize(this.getWidth(), this.getHeight());
			int smallerSize = ((JSplitPane) centerPane).getSize().height / 5 * 3;
			((JSplitPane) centerPane).setDividerLocation(smallerSize);
		}
	}

	private void createMenuBars() {
		int totalWidth = this.getWidth();
		int menuItemMaxWidth = 120;
		int menuItemMaxHeight = 45;

		this.menuBar = new JMenuBar();
		this.menuBar.setSize(totalWidth, 20);
		JMenuItem goToParentMenu = new JMenuItem("Level up");
		goToParentMenu.setSize(50, 20);
		goToParentMenu.setMaximumSize(new Dimension(70, menuItemMaxHeight));
		goToParentMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moduleZoomOut();
			}
		});
		menuBar.add(goToParentMenu);

		JMenuItem exportToImageMenu = new JMenuItem("Export to image");
		exportToImageMenu.setSize(50, 20);
		exportToImageMenu.setMaximumSize(new Dimension(menuItemMaxWidth, menuItemMaxHeight));
		exportToImageMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportToImage();
			}
		});
		menuBar.add(exportToImageMenu);

		JCheckBoxMenuItem showViolationsOptionMenu = new JCheckBoxMenuItem("Show violations");
		showViolationsOptionMenu.setSize(50, 20);
		showViolationsOptionMenu.setMaximumSize(new Dimension(menuItemMaxWidth, menuItemMaxHeight));
		showViolationsOptionMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleViolations();
			}
		});
		menuBar.add(showViolationsOptionMenu);

		this.add(menuBar, java.awt.BorderLayout.AFTER_LINE_ENDS);

		locationBar = new JMenuBar();
		locationBar.setSize(totalWidth, 20);

		locationString = new JMenuItem(ROOT_LEVEL);
		locationString.setSize(menuItemMaxWidth, menuItemMaxHeight);
		locationString.setMinimumSize(new Dimension(menuItemMaxWidth, menuItemMaxHeight));
		locationBar.add(locationString);

		this.add(locationBar, java.awt.BorderLayout.WEST);
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

	public void addListener(UserInputListener listener) {
		listeners.add(listener);
	}

	public void removeListener(UserInputListener listener) {
		listeners.remove(listener);
	}

	public void showViolationsProperties(ViolationDTO[] violationDTOs) {
		this.propertiesScrollPane.setViewportView(this.createViolationsTable(violationDTOs));
		this.layoutComponents(true);
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
		this.layoutComponents(false);
	}

	public void showDependenciesProperties(DependencyDTO[] dependencyDTOs) {
		this.propertiesScrollPane.setViewportView(this.createDependencyTable(dependencyDTOs));
		this.layoutComponents(true);
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
