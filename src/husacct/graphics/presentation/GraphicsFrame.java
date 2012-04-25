package husacct.graphics.presentation;

import java.awt.Dimension;
import java.util.ArrayList;

import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.dto.ViolationTypeDTO;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class GraphicsFrame extends JInternalFrame {
	private static final long serialVersionUID = -4683140198375851034L;

	private DrawingView drawingView;
	
	private JMenuBar menuBar;
	private JScrollPane drawingScollPane,propertiesScrollPane;
	private JComponent centerPane;

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
		
		this.menuBar = new javax.swing.JMenuBar();
		javax.swing.JMenu nmenu = new javax.swing.JMenu("North menu");
		nmenu.addMenuListener(new MenuListener(){
			public void menuSelected(MenuEvent e){
				//controller.
			}

			@Override
			public void menuCanceled(MenuEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void menuDeselected(MenuEvent arg0) {
				// TODO Auto-generated method stub
				
			}			
		});
		menuBar.add(nmenu);
	}
	
	public void showViolations(ViolationDTO[] violationDTOs)
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
			
			rows.add(new String[]{
				violation.getErrorMessage(), 
				ruleTypeDescription,
				violationTypeDescription
			});
		}
		
		return new JTable(rows.toArray(new String[][]{}), columnNames);
	}

	public void hidePropertiesPane() {
		this.layoutComponents(false);		
	}
}
