package husacct.control.presentation.menubar;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IServiceListener;
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

@SuppressWarnings("serial")
public class ValidateMenu extends JMenu{
	private MainController mainController;
	private JMenuItem configureItem;
	private JMenuItem validateItem;
	private JMenuItem exportViolationReportItem;
	
	ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public ValidateMenu(final MainController mainController){
		super();
		this.mainController = mainController;
		setText(localeService.getTranslatedString("Validate"));
		addComponents();
		setListeners();
	}
	
	private void addComponents() {
		validateItem = new JMenuItem(localeService.getTranslatedString("Validate"));
		validateItem.setAccelerator(KeyStroke.getKeyStroke('V', KeyEvent.CTRL_DOWN_MASK));
		validateItem.setMnemonic(getMnemonicKeycode("ValidateMnemonic"));
				
		configureItem = new JMenuItem(localeService.getTranslatedString("Configuration"));
		configureItem.setMnemonic(getMnemonicKeycode("ConfigurationMnemonic"));
				
		exportViolationReportItem = new JMenuItem(localeService.getTranslatedString("ViolationReport"));
		exportViolationReportItem.setMnemonic(getMnemonicKeycode("ViolationReportMnemonic"));
		
		this.add(validateItem);
		this.add(configureItem);
		this.add(exportViolationReportItem);
	}
	
	private void setListeners() {
		validateItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showValidateGui();
			}
		});
		
		configureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showConfigurationGui();
			}
		});
		
		exportViolationReportItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getExportController().showExportViolationsReportGui();
			}
		});
		
		mainController.getStateController().addStateChangeListener(new IStateChangeListener() {
			public void changeState(List<States> states) {
				validateItem.setEnabled(false);
				configureItem.setEnabled(false);
				exportViolationReportItem.setEnabled(false);
				
				if(states.contains(States.VALIDATED)){
					exportViolationReportItem.setEnabled(true);
				}
				
				if(states.contains(States.MAPPED) && states.contains(States.ANALYSED) || states.contains(States.VALIDATED)){
					validateItem.setEnabled(true);
				}
				
				if(states.contains(States.OPENED)){
					configureItem.setEnabled(true);
				}
			}
		});
		
		final ValidateMenu validateMenu = this;
		localeService.addServiceListener(new IServiceListener() {
			public void update() {
				validateMenu.setText(localeService.getTranslatedString("Validate"));
				configureItem.setText(localeService.getTranslatedString("Configuration"));
				validateItem.setText(localeService.getTranslatedString("ValidateNow"));
				exportViolationReportItem.setText(localeService.getTranslatedString("ViolationReport"));
				validateItem.setMnemonic(getMnemonicKeycode("ValidateMnemonic"));
				configureItem.setMnemonic(getMnemonicKeycode("ConfigurationMnemonic"));
				exportViolationReportItem.setMnemonic(getMnemonicKeycode("ViolationReportMnemonic"));
			}
		});
	}
	
	public JMenuItem getConfigureItem(){
		return configureItem;
	}
	
	public JMenuItem getValidateItem(){
		return validateItem;
	}

	public JMenuItem getExportViolationReportItem(){
		return exportViolationReportItem;
	}
	
	private int getMnemonicKeycode(String translatedString) {
		String mnemonicString = localeService.getTranslatedString(translatedString);
		int keyCode = KeyStroke.getKeyStroke(mnemonicString).getKeyCode();
		return keyCode;
	}
}
