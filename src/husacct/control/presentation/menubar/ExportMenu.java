package husacct.control.presentation.menubar;


import husacct.control.presentation.menubar.popup.ExportLogicalArchitectureFrame;
import husacct.control.task.IStateChangeListener;
import husacct.control.task.StateController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class ExportMenu extends JMenu{
	private StateController controller;
	private JMenuItem exportViolationReportItem;
	private JMenuItem exportLogicalArchitectureItem;
	private JMenuItem exportPhysicalArchitectureItem;
	private int currentState;
	private JFrame exportLogicalArchitectureFrame = new ExportLogicalArchitectureFrame();
	
	public ExportMenu(StateController stateController){
		super("Export");
		
		this.controller = stateController;
		currentState = controller.getState();
		
		
		exportLogicalArchitectureItem = new JMenuItem("Logical architecture");
		this.add(exportLogicalArchitectureItem);
		exportLogicalArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				exportLogicalArchitectureFrame.setVisible(true);
			}
		});


		exportPhysicalArchitectureItem = new JMenuItem("Physical architecture");
		this.add(exportPhysicalArchitectureItem);
		exportPhysicalArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: physical architecture
			}
		});
		
		exportViolationReportItem = new JMenuItem("Violation report");

		this.add(exportViolationReportItem);
		
		exportViolationReportItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: Violation report
			}
		});
		
		//disable buttons on start
		exportViolationReportItem.setEnabled(false);
		exportLogicalArchitectureItem.setEnabled(false);
		exportPhysicalArchitectureItem.setEnabled(false);
		
		controller.addStateChangeListener(new IStateChangeListener() {

			@Override
			public void changeState(int state) {
				currentState = controller.getState();
				
				if(currentState == controller.NONE){
					exportViolationReportItem.setEnabled(false);
					exportLogicalArchitectureItem.setEnabled(false);
					exportPhysicalArchitectureItem.setEnabled(false);
				}else if(currentState == controller.EMPTY){
					exportViolationReportItem.setEnabled(false);
					exportLogicalArchitectureItem.setEnabled(false);
					exportPhysicalArchitectureItem.setEnabled(false);
				}else if(currentState == controller.DEFINED){
					exportViolationReportItem.setEnabled(false);
					exportLogicalArchitectureItem.setEnabled(true);
					exportPhysicalArchitectureItem.setEnabled(false);
				}else if(currentState == controller.MAPPED){
					exportViolationReportItem.setEnabled(false);
					exportLogicalArchitectureItem.setEnabled(true);
					exportPhysicalArchitectureItem.setEnabled(false);
				}else if(currentState == controller.VALIDATED){
					exportViolationReportItem.setEnabled(true);
					exportLogicalArchitectureItem.setEnabled(true);
					exportPhysicalArchitectureItem.setEnabled(true);
				}
			}
			
		});
	}
}
