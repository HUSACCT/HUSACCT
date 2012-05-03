package husacct.control.presentation.menubar;


import husacct.control.presentation.menubar.popup.ExportLogicalArchitectureFrame;
import husacct.control.task.IStateChangeListener;
import husacct.control.task.MainController;
import husacct.control.task.StateController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

@SuppressWarnings("serial")
public class ExportMenu extends JMenu{
	private JMenuItem exportViolationReportItem;
	private JMenuItem exportLogicalArchitectureItem;
	private JMenuItem exportPhysicalArchitectureItem;
	private int currentState;
	private JFrame exportLogicalArchitectureFrame = new ExportLogicalArchitectureFrame();
	
	public ExportMenu(final MainController mainController){
		super("Export");
		
		currentState = mainController.getStateController().getState();
		
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
		
		mainController.getStateController().addStateChangeListener(new IStateChangeListener() {

			@Override
			public void changeState(int state) {
				currentState = mainController.getStateController().getState();
				
				if(currentState == StateController.NONE){
					exportViolationReportItem.setEnabled(false);
					exportLogicalArchitectureItem.setEnabled(false);
					exportPhysicalArchitectureItem.setEnabled(false);
				}else if(currentState == StateController.EMPTY){
					exportViolationReportItem.setEnabled(false);
					exportLogicalArchitectureItem.setEnabled(false);
					exportPhysicalArchitectureItem.setEnabled(false);
				}else if(currentState == StateController.DEFINED){
					exportViolationReportItem.setEnabled(false);
					exportLogicalArchitectureItem.setEnabled(true);
					exportPhysicalArchitectureItem.setEnabled(false);
				}else if(currentState == StateController.MAPPED){
					exportViolationReportItem.setEnabled(false);
					exportLogicalArchitectureItem.setEnabled(true);
					exportPhysicalArchitectureItem.setEnabled(false);
				}else if(currentState == StateController.VALIDATED){
					exportViolationReportItem.setEnabled(true);
					exportLogicalArchitectureItem.setEnabled(true);
					exportPhysicalArchitectureItem.setEnabled(true);
				}
			}			
		});
		
		// TODO: refactor including adapter
		this.addMenuListener(new MenuListener() {
			
			@Override
			public void menuSelected(MenuEvent arg0) {
				mainController.getStateController().checkState();
				
			}

			@Override
			public void menuCanceled(MenuEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void menuDeselected(MenuEvent e) {
				// TODO Auto-generated method stub
				
			}

		});
	}
}
