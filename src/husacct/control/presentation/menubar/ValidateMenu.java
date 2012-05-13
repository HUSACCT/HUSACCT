package husacct.control.presentation.menubar;

import husacct.ServiceProvider;
import husacct.control.IControlService;
import husacct.control.ILocaleChangeListener;
import husacct.control.task.IStateChangeListener;
import husacct.control.task.MainController;
import husacct.control.task.States;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Locale;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;

@SuppressWarnings("serial")
public class ValidateMenu extends JMenu{
	private JMenuItem configureItem;
	private JMenuItem validateNowItem;
	private JMenuItem exportViolationReportItem;
	
	IControlService controlService = ServiceProvider.getInstance().getControlService();
	
	public ValidateMenu(final MainController mainController){
		super();
		setText(controlService.getTranslatedString("Validate"));
		
		validateNowItem = new JMenuItem(controlService.getTranslatedString("ValidateNow"));
		validateNowItem.setAccelerator(KeyStroke.getKeyStroke('V', KeyEvent.CTRL_DOWN_MASK));
		validateNowItem.setMnemonic('v');
		this.add(validateNowItem);
		validateNowItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showViolationsGui();
			}
		});
		
		configureItem = new JMenuItem(controlService.getTranslatedString("Configuration"));
		configureItem.setMnemonic('c');
		this.add(configureItem);
		configureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showConfigurationGui();
			}
		});

		exportViolationReportItem = new JMenuItem(controlService.getTranslatedString("ViolationReport"));
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
		
		final ValidateMenu validateMenu = this;
		controlService.addLocaleChangeListener(new ILocaleChangeListener() {
			public void update(Locale newLocale) {
				validateMenu.setText(controlService.getTranslatedString("Validate"));
				configureItem.setText(controlService.getTranslatedString("Configuration"));
				validateNowItem.setText(controlService.getTranslatedString("ValidateNow"));
				exportViolationReportItem.setText(controlService.getTranslatedString("ViolationReport"));
			}
		});
	}
}
