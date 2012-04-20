package husacct.control.presentation.menubar;

import husacct.control.task.IStateChangeListener;
import husacct.control.task.StateController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class ValidateMenu extends JMenu{
	private StateController controller;
	private int currentState;
	private JMenuItem mntmConfigure;
	private JMenuItem mntmValidateNow;
	
	public ValidateMenu(StateController stateController){
		super("Validate");
		
		this.controller = stateController;
		currentState = controller.getState();
		
		mntmValidateNow = new JMenuItem("Validate now");
		this.add(mntmValidateNow);
		mntmValidateNow.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: Validate now
				
				controller.setState(4);
			}
		});
		
		mntmConfigure = new JMenuItem("Configuration");
		this.add(mntmConfigure);
		mntmConfigure.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: Configuration
				
			}
		});
		
		//disable buttons on start
		mntmValidateNow.setEnabled(false);
		mntmConfigure.setEnabled(false);
		
		
		controller.addStateChangeListener(new IStateChangeListener() {

			@Override
			public void changeState(int state) {
				currentState = controller.getState();
				if(currentState == controller.NONE){
					mntmValidateNow.setEnabled(false);
					mntmConfigure.setEnabled(false);
				}else if(currentState == controller.EMPTY){
					mntmValidateNow.setEnabled(false);
					mntmConfigure.setEnabled(false);
				}else if(currentState == controller.DEFINED){
					mntmValidateNow.setEnabled(false);
					mntmConfigure.setEnabled(false);
				}else if(currentState == controller.MAPPED){
					mntmValidateNow.setEnabled(true);
					mntmConfigure.setEnabled(false);
				}else if(currentState == controller.VALIDATED){
					mntmValidateNow.setEnabled(true);
					mntmConfigure.setEnabled(true);
				}
			}
			
		});
	}
	
	
}
