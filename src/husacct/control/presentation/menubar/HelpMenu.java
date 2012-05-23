package husacct.control.presentation.menubar;

import husacct.ServiceProvider;
import husacct.control.IControlService;
import husacct.control.ILocaleChangeListener;
import husacct.control.task.MainController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class HelpMenu extends JMenu {
	
	private MainController mainController;
	private JMenuItem aboutItem;
	
	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	
	public HelpMenu(final MainController mainController){
		super();
		this.mainController = mainController;
		setText(controlService.getTranslatedString("Help"));
		addComponents();
		setListeners();
	}
	
	private void addComponents() {
		aboutItem = new JMenuItem(controlService.getTranslatedString("About"));
		aboutItem.setMnemonic(getMnemonicKeycode("AboutMnemonic"));
		this.add(aboutItem);
	}
	
	private void setListeners() {
		aboutItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.getApplicationController().showAboutHusacctGui();
			}
		});
		
		final HelpMenu helpMenu = this;
		controlService.addLocaleChangeListener(new ILocaleChangeListener() {
			public void update(Locale newLocale) {
				helpMenu.setText(controlService.getTranslatedString("Help"));
				aboutItem.setText(controlService.getTranslatedString("About"));	
				aboutItem.setMnemonic(getMnemonicKeycode("AboutMnemonic"));
			}
		});
	}
	
	private int getMnemonicKeycode(String translatedString) {
		String mnemonicString = controlService.getTranslatedString(translatedString);
		int keyCode = KeyStroke.getKeyStroke(mnemonicString).getKeyCode();
		return keyCode;
	}
}
