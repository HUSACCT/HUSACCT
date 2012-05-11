package husacct.control.presentation.menubar;

import husacct.control.task.IStateChangeListener;
import husacct.control.task.MainController;
import husacct.control.task.StateController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;

@SuppressWarnings("serial")
public class ValidateMenu extends JMenu{
	private JMenuItem configureItem;
	private JMenuItem validateNowItem;
	private JMenuItem exportViolationReportItem;
	
	public ValidateMenu(final MainController mainController){
		super("Validate");
		
		validateNowItem = new JMenuItem("Validate now");
		this.add(validateNowItem);
		validateNowItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showViolationsGui();
			}
		});
		
		configureItem = new JMenuItem("Configuration");
		this.add(configureItem);
		configureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showConfigurationGui();
			}
		});

		exportViolationReportItem = new JMenuItem("Violation report");

		this.add(exportViolationReportItem);
		
		exportViolationReportItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getImportExportController().showExportViolationsReportGui();
			}
		});

		mainController.getStateController().addStateChangeListener(new IStateChangeListener() {
			public void changeState(int state) {
				validateNowItem.setEnabled(false);
				configureItem.setEnabled(false);
				exportViolationReportItem.setEnabled(false);
				switch(state){
					case StateController.VALIDATED: {
						exportViolationReportItem.setEnabled(true);
					}
					case StateController.MAPPED: {
						validateNowItem.setEnabled(true);
					}
					case StateController.ANALYSED:
					case StateController.DEFINED:
					case StateController.EMPTY:
					case StateController.NONE: {
						configureItem.setEnabled(true);
					}
				}
			}
		});
		
		this.addMenuListener(new MenuListenerAdapter() {
			public void menuSelected(MenuEvent e) {
				mainController.getStateController().checkState();		
			}
		});
	}
}
