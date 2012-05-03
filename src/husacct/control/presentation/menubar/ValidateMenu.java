package husacct.control.presentation.menubar;

import husacct.control.task.IStateChangeListener;
import husacct.control.task.MainController;
import husacct.control.task.StateController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

@SuppressWarnings("serial")
public class ValidateMenu extends JMenu{
	private MainController maincontroller;
	private int currentState;
	private JMenuItem mntmConfigure;
	private JMenuItem mntmValidateNow;
	
	public ValidateMenu(final MainController mainController){
		super("Validate");
		
		this.maincontroller = mainController;
		currentState = maincontroller.getStateController().getState();
		
		mntmValidateNow = new JMenuItem("Validate now");
		this.add(mntmValidateNow);
		mntmValidateNow.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: Validate now
				maincontroller.getViewController().showViolationsGui();
			}
		});
		
		mntmConfigure = new JMenuItem("Configuration");
		this.add(mntmConfigure);
		mntmConfigure.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				maincontroller.getViewController().showConfigurationGui();
			}
		});
		
		//disable buttons on start
		mntmValidateNow.setEnabled(false);
		mntmConfigure.setEnabled(true);
		
		
		maincontroller.getStateController().addStateChangeListener(new IStateChangeListener() {

			@Override
			public void changeState(int state) {
				currentState = maincontroller.getStateController().getState();
				if(currentState == StateController.NONE){
					mntmValidateNow.setEnabled(false);
				}else if(currentState == StateController.EMPTY){
					mntmValidateNow.setEnabled(false);
				}else if(currentState == StateController.DEFINED){
					mntmValidateNow.setEnabled(true);
				}else if(currentState == StateController.MAPPED){
					mntmValidateNow.setEnabled(true);
				}else if(currentState == StateController.VALIDATED){
					mntmValidateNow.setEnabled(true);
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
