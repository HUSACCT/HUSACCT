package husacct.control.presentation.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import husacct.ServiceProvider;
import husacct.control.IControlService;
import husacct.control.task.IStateChangeListener;
import husacct.control.task.StateController;

@SuppressWarnings("serial")
public class DefineMenu extends JMenu{
	private StateController controller;
	private JMenuItem importArchitectureItem;
	private JMenuItem importLogicalArchitectureItem;
	private JMenuItem defineLogicalArchitectureItem;
	private int currentState;
	
	public DefineMenu(StateController stateController){
		super("Define");
		
		this.controller = stateController;
		currentState = controller.getState();
		
		defineLogicalArchitectureItem = new JMenuItem("Define logical architecture");
		this.add(defineLogicalArchitectureItem);

		defineLogicalArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: define logical
				
				controller.setState(2);
			}
		});
		
		importLogicalArchitectureItem = new JMenuItem("Import logical architecture");
		this.add(importLogicalArchitectureItem);
		importLogicalArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: import
				
				controller.setState(2);
			}
		});
		
		importArchitectureItem = new JMenuItem("Map architecture to project");
		this.add(importArchitectureItem);
		importArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: map
				
				controller.setState(3);
			}
		});
		
		stateController.addStateChangeListener(new IStateChangeListener() {

			public void changeState(int state) {
				currentState = controller.getState();
				
				if(currentState == 0){
					importArchitectureItem.setEnabled(false);
					defineLogicalArchitectureItem.setEnabled(false);
					importLogicalArchitectureItem.setEnabled(false);
				}else if(currentState == 1){
					importArchitectureItem.setEnabled(false);
					defineLogicalArchitectureItem.setEnabled(true);
					importLogicalArchitectureItem.setEnabled(true);
				}else if(currentState == 2){
					importArchitectureItem.setEnabled(true);
					defineLogicalArchitectureItem.setEnabled(true);
					importLogicalArchitectureItem.setEnabled(true);
				}else if(currentState == 3){
					importArchitectureItem.setEnabled(true);
					defineLogicalArchitectureItem.setEnabled(false);
					importLogicalArchitectureItem.setEnabled(true);
				}else if(currentState == 4){
					importArchitectureItem.setEnabled(true);
					defineLogicalArchitectureItem.setEnabled(true);
					importLogicalArchitectureItem.setEnabled(true);
				}
			}
			
		});
	}
}
