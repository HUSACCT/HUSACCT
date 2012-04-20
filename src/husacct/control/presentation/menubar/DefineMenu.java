package husacct.control.presentation.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

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
		
		//disable buttons on start
		importArchitectureItem.setEnabled(false);
		defineLogicalArchitectureItem.setEnabled(false);
		importLogicalArchitectureItem.setEnabled(false);
		
		stateController.addStateChangeListener(new IStateChangeListener() {

			public void changeState(int state) {
				currentState = controller.getState();
				
				if(currentState == controller.NONE){
					importArchitectureItem.setEnabled(false);
					defineLogicalArchitectureItem.setEnabled(false);
					importLogicalArchitectureItem.setEnabled(false);
				}else if(currentState == controller.EMPTY){
					importArchitectureItem.setEnabled(false);
					defineLogicalArchitectureItem.setEnabled(true);
					importLogicalArchitectureItem.setEnabled(true);
				}else if(currentState == controller.DEFINED){
					importArchitectureItem.setEnabled(true);
					defineLogicalArchitectureItem.setEnabled(true);
					importLogicalArchitectureItem.setEnabled(true);
				}else if(currentState == controller.MAPPED){
					importArchitectureItem.setEnabled(true);
					defineLogicalArchitectureItem.setEnabled(true);
					importLogicalArchitectureItem.setEnabled(true);
				}else if(currentState == controller.VALIDATED){
					importArchitectureItem.setEnabled(true);
					defineLogicalArchitectureItem.setEnabled(true);
					importLogicalArchitectureItem.setEnabled(true);
				}
			}
			
		});
	}
}
