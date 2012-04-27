package husacct.control.presentation.menubar;

import husacct.control.task.IStateChangeListener;
import husacct.control.task.MainController;
import husacct.control.task.StateController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class ValidateMenu extends JMenu{
	private MainController maincontroller;
	private int currentState;
	private JMenuItem mntmConfigure;
	private JMenuItem mntmValidateNow;
	
	public ValidateMenu(MainController mainController){
		super("Validate");
		
		this.maincontroller = mainController;
		currentState = maincontroller.getStateController().getState();
		
		mntmValidateNow = new JMenuItem("Validate now");
		this.add(mntmValidateNow);
		mntmValidateNow.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: Validate now
				maincontroller.getViewController().setViolationsGui();
				maincontroller.getViewController().showViolationsGui();
				maincontroller.getStateController().setState(4);
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
		
		
		maincontroller.getStateController().addStateChangeListener(new IStateChangeListener() {

			@Override
			public void changeState(int state) {
				currentState = maincontroller.getStateController().getState();
				if(currentState == maincontroller.getStateController().NONE){
					mntmValidateNow.setEnabled(false);
					mntmConfigure.setEnabled(false);
				}else if(currentState == maincontroller.getStateController().EMPTY){
					mntmValidateNow.setEnabled(false);
					mntmConfigure.setEnabled(false);
				}else if(currentState == maincontroller.getStateController().DEFINED){
					mntmValidateNow.setEnabled(true);
					mntmConfigure.setEnabled(false);
				}else if(currentState == maincontroller.getStateController().MAPPED){
					mntmValidateNow.setEnabled(true);
					mntmConfigure.setEnabled(false);
				}else if(currentState == maincontroller.getStateController().VALIDATED){
					mntmValidateNow.setEnabled(true);
					mntmConfigure.setEnabled(true);
				}
			}
			
		});
	}
	
	
}
