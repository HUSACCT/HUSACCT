package husacct.control.presentation.menubar;

import husacct.ServiceProvider;
import husacct.control.IControlService;
import husacct.control.ILocaleChangeListener;
import husacct.control.task.LocaleController;
import husacct.control.task.MainController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class LanguageMenu extends JMenu{
	
	private Logger logger = Logger.getLogger(LanguageMenu.class);
	private LocaleController localeController;
	private IControlService controlService;
	
	public LanguageMenu(MainController mainController){
		super();
		controlService = ServiceProvider.getInstance().getControlService();
		setText(controlService.getTranslatedString("Language"));
		this.localeController = mainController.getLocaleController();
		addComponents();
		addListeners();
	}
	
	private void addComponents(){
		for(Locale locale : localeController.getAvailableLocales()){
			String language = locale.getLanguage();
			final JCheckBoxMenuItem languageItem = new JCheckBoxMenuItem(language);
			
			if(language.equals(localeController.getLocale().getLanguage())){
				languageItem.setSelected(true);
			}
			
			languageItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setLocaleFromString(languageItem.getText());
				}
			});
			
			controlService.addLocaleChangeListener(new ILocaleChangeListener() {
				@Override
				public void update(Locale newLocale) {
					if(newLocale.getLanguage().equals(languageItem.getText())){
						languageItem.setSelected(true);
					} else {
						languageItem.setSelected(false);
					}
				}
			});
			
			add(languageItem);
		}
	}
	
	private void addListeners(){
		controlService.addLocaleChangeListener(new ILocaleChangeListener() {
			@Override
			public void update(Locale newLocale) {
				setText(controlService.getTranslatedString("Language"));
			}
		});
	}
	
	private void setLocaleFromString(String locale){
		logger.debug("User sets language to: " + locale);
		localeController.setNewLocaleFromString(locale);
	}
}
