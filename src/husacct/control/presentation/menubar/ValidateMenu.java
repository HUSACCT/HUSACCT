package husacct.control.presentation.menubar;

import husacct.ServiceProvider;
import husacct.common.enums.States;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IServiceListener;
import husacct.control.task.IStateChangeListener;
import husacct.control.task.MainController;

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
	private JMenuItem validateItem;
	private JMenuItem exportViolationsItem;
	private JMenuItem reportViolationsItem;
	
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
				
		exportViolationsItem = new JMenuItem(localeService.getTranslatedString("ValidateExport"));
		exportViolationsItem.setMnemonic(getMnemonicKeycode("ValidateExportMnemonic"));
		
		reportViolationsItem = new JMenuItem(localeService.getTranslatedString("ValidateReport"));
		reportViolationsItem.setMnemonic(getMnemonicKeycode("ValidateReportMnemonic"));
		
		this.add(validateItem);
		this.add(exportViolationsItem);
		this.add(reportViolationsItem);
	}
	
	private void setListeners() {
		validateItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getViewController().showValidateGui();
			}
		});
		
		exportViolationsItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getExportImportController().showExportViolationsGui();
			}
		});
		
		reportViolationsItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getExportImportController().showReportViolationsGui();
			}
		});
		
		mainController.getStateController().addStateChangeListener(new IStateChangeListener() {
			public void changeState(List<States> states) {
				validateItem.setEnabled(false);
				exportViolationsItem.setEnabled(false);
				reportViolationsItem.setEnabled(false);
				
				if(states.contains(States.VALIDATED)){
					exportViolationsItem.setEnabled(true);
					reportViolationsItem.setEnabled(true);
				}
				
				if(states.contains(States.MAPPED) && states.contains(States.ANALYSED)){
					validateItem.setEnabled(true);
				}
			}
		});
		
		final ValidateMenu validateMenu = this;
		localeService.addServiceListener(new IServiceListener() {
			public void update() {
				validateMenu.setText(localeService.getTranslatedString("Validate"));
				validateItem.setText(localeService.getTranslatedString("ValidateNow"));
				validateItem.setMnemonic(getMnemonicKeycode("ValidateMnemonic"));
				exportViolationsItem.setText(localeService.getTranslatedString("ValidateExport"));
				exportViolationsItem.setMnemonic(getMnemonicKeycode("ValidateExportMnemonic"));
				reportViolationsItem.setText(localeService.getTranslatedString("ValidateReport"));
				reportViolationsItem.setMnemonic(getMnemonicKeycode("ValidateReportMnemonic"));
			}
		});
	}
	
	private int getMnemonicKeycode(String translatedString) {
		String mnemonicString = localeService.getTranslatedString(translatedString);
		int keyCode = KeyStroke.getKeyStroke(mnemonicString).getKeyCode();
		return keyCode;
	}

	public JMenuItem getValidateItem(){
		return validateItem;
	}
	
}
