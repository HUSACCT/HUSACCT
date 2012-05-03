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
	private JMenuItem configureItem;
	private JMenuItem validateNowItem;
	private JMenuItem exportViolationReportItem;
	
	public ValidateMenu(final MainController mainController){
		super("Validate");
		
		this.maincontroller = mainController;
		currentState = maincontroller.getStateController().getState();
		
		validateNowItem = new JMenuItem("Validate now");
		this.add(validateNowItem);
		validateNowItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: Validate now
				maincontroller.getViewController().showViolationsGui();
			}
		});
		
		configureItem = new JMenuItem("Configuration");
		this.add(configureItem);
		configureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				maincontroller.getViewController().showConfigurationGui();
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
		validateNowItem.setEnabled(false);
		configureItem.setEnabled(true);
		exportViolationReportItem.setEnabled(false);
		
		maincontroller.getStateController().addStateChangeListener(new IStateChangeListener() {

			@Override
			public void changeState(int state) {
				currentState = maincontroller.getStateController().getState();
				if(currentState == StateController.NONE){
					validateNowItem.setEnabled(false);
					exportViolationReportItem.setEnabled(false);
				}else if(currentState == StateController.EMPTY){
					validateNowItem.setEnabled(false);
					exportViolationReportItem.setEnabled(false);
				}else if(currentState == StateController.DEFINED){
					validateNowItem.setEnabled(true);
					exportViolationReportItem.setEnabled(false);
				}else if(currentState == StateController.MAPPED){
					validateNowItem.setEnabled(true);
					exportViolationReportItem.setEnabled(false);
				}else if(currentState == StateController.VALIDATED){
					validateNowItem.setEnabled(true);
					exportViolationReportItem.setEnabled(true);
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
