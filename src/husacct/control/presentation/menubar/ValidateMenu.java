package husacct.control.presentation.menubar;

import husacct.control.task.IStateChangeListener;
import husacct.control.task.MainController;
import husacct.control.task.States;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;

@SuppressWarnings("serial")
public class ValidateMenu extends JMenu{
	private JMenuItem configureItem;
	private JMenuItem validateNowItem;
	private JMenuItem exportViolationReportItem;
	
	public ValidateMenu(final MainController mainController){
		super("Validate");
		
		validateNowItem = new JMenuItem("Validate now");
		validateNowItem.setAccelerator(KeyStroke.getKeyStroke('V', KeyEvent.CTRL_DOWN_MASK));
		validateNowItem.setMnemonic('v');
		this.add(validateNowItem);
		validateNowItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showViolationsGui();
			}
		});
		
		configureItem = new JMenuItem("Configuration");
		configureItem.setMnemonic('c');
		this.add(configureItem);
		configureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showConfigurationGui();
			}
		});

		exportViolationReportItem = new JMenuItem("Violation report");
		exportViolationReportItem.setMnemonic('i');
		this.add(exportViolationReportItem);
		
		exportViolationReportItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getImportExportController().showExportViolationsReportGui();
			}
		});

		mainController.getStateController().addStateChangeListener(new IStateChangeListener() {
			public void changeState(List<States> states) {
				validateNowItem.setEnabled(false);
				configureItem.setEnabled(false);
				exportViolationReportItem.setEnabled(false);
				
				if(states.contains(States.VALIDATED)){
					exportViolationReportItem.setEnabled(true);
				}
				
				if(states.contains(States.MAPPED) || states.contains(States.VALIDATED)){
					validateNowItem.setEnabled(true);
				}
				
				if(states.contains(States.OPENED)){
					configureItem.setEnabled(true);
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
