package husacct.control.presentation.menubar;

import husacct.control.task.IStateChangeListener;
import husacct.control.task.MainController;
import husacct.control.task.StateController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class DefineMenu extends JMenu{
	private MainController maincontroller;
	private JMenuItem importLogicalArchitectureItem;
	private JMenuItem defineLogicalArchitectureItem;
	private int currentState;

	public DefineMenu(MainController mainController){
		super("Define");

		this.maincontroller = mainController;
		currentState = maincontroller.getStateController().getState();

		defineLogicalArchitectureItem = new JMenuItem("Define logical architecture");
		this.add(defineLogicalArchitectureItem);

		defineLogicalArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				maincontroller.getViewController().showDefineGui();
			}
		});

		importLogicalArchitectureItem = new JMenuItem("Import logical architecture");
		this.add(importLogicalArchitectureItem);
		importLogicalArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: import
			}
		});

		//disable buttons on start
		defineLogicalArchitectureItem.setEnabled(false);
		importLogicalArchitectureItem.setEnabled(false);

		maincontroller.getStateController().addStateChangeListener(new IStateChangeListener() {

			public void changeState(int state) {
				currentState = maincontroller.getStateController().getState();

				if(currentState == StateController.NONE){
					defineLogicalArchitectureItem.setEnabled(false);
					importLogicalArchitectureItem.setEnabled(false);
				}else if(currentState >= StateController.EMPTY){
					defineLogicalArchitectureItem.setEnabled(true);
					importLogicalArchitectureItem.setEnabled(true);
				}
			}
		});
	}
}
