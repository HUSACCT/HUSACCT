package husacct.graphics.presentation;

import husacct.ServiceProvider;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.task.MouseClickListener;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private JMenuBar menuBar;
	private JScrollPane drawingScollPane,propertiesScrollPane;
	private JComponent centerPane;
	private JMenuItem pathInfoMenu;
	
	private ArrayList<MouseClickListener> listeners = new ArrayList<MouseClickListener>();

	public GraphicsFrame(DrawingView drawingView) {
		this.drawingView = drawingView;

		this.initializeComponents();
	}

	private void initializeComponents() {
		this.drawingScollPane = new JScrollPane();
		this.drawingScollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.drawingScollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.drawingScollPane.setViewportView(drawingView);
		this.drawingScollPane.setMinimumSize(new Dimension(500, 300));
		
		this.propertiesScrollPane = new JScrollPane();
		this.propertiesScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.propertiesScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.propertiesScrollPane.setMinimumSize(new Dimension(500, 50));
		
		createMenuBar();
		
		this.setLayout(new java.awt.BorderLayout());
		this.add(this.menuBar, java.awt.BorderLayout.NORTH);
		
		this.layoutComponents(false);
	}
	
	private void layoutComponents(boolean showProperties) {
		if(this.centerPane != null)
		{
			this.remove(this.centerPane);
		}
		
		if(!showProperties) {
			this.centerPane = this.drawingScollPane;
		}
		else {
			this.centerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
					this.drawingScollPane,
					this.propertiesScrollPane);
			((JSplitPane)centerPane).setOneTouchExpandable(true);
			((JSplitPane)centerPane).setContinuousLayout(true);
		}
		this.add(this.centerPane, java.awt.BorderLayout.CENTER);
		
		if(this.isVisible())
		{
			this.validate();
		}
	}
	
	private void createMenuBar(){
		this.menuBar = new JMenuBar();
		this.menuBar.setSize(200,20);
		int menuItemMaxWidth = 120;
		int menuItemMaxHeight = 45;
		JMenuItem goToParentMenu = new JMenuItem("Level up");
		goToParentMenu.setSize(50,20);
		goToParentMenu.setMaximumSize(new Dimension(70,menuItemMaxHeight));
		goToParentMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				moduleZoomOut();
			}
		});
		menuBar.add(goToParentMenu);
		
		JMenuItem exportToImageMenu = new JMenuItem("Export to image");
		exportToImageMenu.setSize(50,20);
		exportToImageMenu.setMaximumSize(new Dimension(menuItemMaxWidth,menuItemMaxHeight));
		exportToImageMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				exportToImage();
			}
		});
		menuBar.add(exportToImageMenu);
		
		JCheckBoxMenuItem showViolationsOptionMenu = new JCheckBoxMenuItem("Show violations");
		showViolationsOptionMenu.setSize(50,20);
		showViolationsOptionMenu.setMaximumSize(new Dimension(menuItemMaxWidth,menuItemMaxHeight));
		showViolationsOptionMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleViolations();
			}
		});
		menuBar.add(showViolationsOptionMenu);
		
		pathInfoMenu = new JMenuItem("");
		pathInfoMenu.setSize(50,20);
		pathInfoMenu.setMaximumSize(new Dimension(1000,menuItemMaxHeight));
		menuBar.add(pathInfoMenu);
		
		this.add(menuBar, java.awt.BorderLayout.NORTH);
	}
	
	public void setCurrentPathInfo(String path){
		pathInfoMenu.setText(path);
	}

	private void moduleZoomOut() {
		for (MouseClickListener l : listeners) {
			l.moduleZoomOut();
		}
	}
	
	private void exportToImage() {
		for (MouseClickListener l : listeners) {
			l.exportToImage();
		}
	}
	
	private void toggleViolations(){
		for (MouseClickListener l : listeners) {
			l.toggleViolations();
		}
	}

	public void addListener(MouseClickListener listener) {
		listeners.add(listener);
	}

	public void removeListener(MouseClickListener listener) {
		listeners.remove(listener);
	}
	
	public void showViolationsProperties(ViolationDTO[] violationDTOs)
	{
		this.propertiesScrollPane.setViewportView(this.createViolationsTable(violationDTOs));
		this.layoutComponents(true);
	}
	
	private JTable createViolationsTable(ViolationDTO[] violationDTOs)
	{
		String[] columnNames = { "Error Message", "Rule Type", "Violation Type" };
		
		ArrayList<String[]> rows = new ArrayList<String[]>();
		for(ViolationDTO violation : violationDTOs)
		{
			String ruleTypeDescription = "none";
			String violationTypeDescription = "none";
			
			if(violation.getRuleType() != null) {
				ruleTypeDescription = violation.getRuleType().getDescriptionKey();
			}
			
			if(violation.getViolationType() != null) {
				violationTypeDescription = violation.getViolationType().getDescriptionKey();
			}
			
			String message = ServiceProvider.getInstance().getValidateService().buildDefinedRuleMessage(violation.getMessage());
			
			rows.add(new String[]{
				message, 
				ruleTypeDescription,
				violationTypeDescription,
			});
		}
		
		return new JTable(rows.toArray(new String[][]{}), columnNames);
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
		for(DependencyDTO dependency: dependencyDTOs)
		{
			rows.add(new String[]{
				dependency.from, 
				dependency.to,
				""+dependency.lineNumber,
				dependency.type
			});
		}
		
		return new JTable(rows.toArray(new String[][]{}), columnNames);
	}
}
