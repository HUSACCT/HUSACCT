package husacct.control.presentation.menubar;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IServiceListener;
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
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public LanguageMenu(MainController mainController){
		super();
		setText(localeService.getTranslatedString("Language"));
		addComponents();
		addListeners();
	}
	
	private void addComponents(){
		for(Locale locale : localeService.getAvailableLocales()){
			String language = locale.getLanguage();
			final JCheckBoxMenuItem languageItem = new JCheckBoxMenuItem(language);
			
			if(language.equals(localeService.getLocale().getLanguage())){
				languageItem.setSelected(true);
			}
			
			languageItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setLocaleFromString(languageItem.getText());
				}
			});
			
			localeService.addServiceListener(new IServiceListener() {
				@Override
				public void update() {
					if(localeService.getLocale().getLanguage().equals(languageItem.getText())){
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
		localeService.addServiceListener(new IServiceListener() {
			@Override
			public void update() {
				setText(localeService.getTranslatedString("Language"));
			}
		});
	}
	
	private void setLocaleFromString(String locale){
		logger.debug("User sets language to: " + locale);
		localeService.setLocale(new Locale(locale, locale));
	}
}
