package husacct.control.presentation.menubar;

import husacct.ServiceProvider;
import husacct.control.IControlService;
import husacct.control.task.IStateChangeListener;
import husacct.control.task.StateController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class ExportMenu extends JMenu{
	private StateController controller;
	private JMenuItem exportViolationReportItem;
	private JMenuItem exportLogicalArchitectureItem;
	private int currentState;
	
	public ExportMenu(StateController stateController){
		super("Export");
		
		this.controller = stateController;
		currentState = controller.getState();
		
		
		exportLogicalArchitectureItem = new JMenuItem("Logical architecture");
		this.add(exportLogicalArchitectureItem);
		exportLogicalArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: logical architecture
			}
		});

				
		exportViolationReportItem = new JMenuItem("Violation report");
		this.add(exportViolationReportItem);
		
		exportViolationReportItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: Violation report
			}
		});
		
		controller.addStateChangeListener(new IStateChangeListener() {

			@Override
			public void changeState(int state) {
				currentState = controller.getState();
				
				if(currentState == 0){
					System.out.println("Hier!");
					exportViolationReportItem.setEnabled(false);
					exportLogicalArchitectureItem.setEnabled(false);
				}else if(currentState == 1){
					exportViolationReportItem.setEnabled(false);
					exportLogicalArchitectureItem.setEnabled(false);
				}else if(currentState == 2){
					exportViolationReportItem.setEnabled(false);
					exportLogicalArchitectureItem.setEnabled(false);
				}else if(currentState == 3){
					exportViolationReportItem.setEnabled(false);
					exportLogicalArchitectureItem.setEnabled(true);
				}else if(currentState == 4){
					exportViolationReportItem.setEnabled(true);
					exportLogicalArchitectureItem.setEnabled(true);
				}
			}
			
		});
	}
}
