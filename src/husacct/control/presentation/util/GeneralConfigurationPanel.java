package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IServiceListener;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public class GeneralConfigurationPanel extends JPanel {
	
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private Logger logger = Logger.getLogger(GeneralConfigurationPanel.class);
	
	JRadioButton languageEnglish, languageDutch;
	ButtonGroup languageGroup = new ButtonGroup();
	
	public GeneralConfigurationPanel() {
		this.setLayout(new GridLayout(0,1));
		
		initialiseLanguage();
	}
	
	public void initialiseLanguage() {
		
		JPanel languagePanel = new JPanel();
		languagePanel.setBorder(BorderFactory.createTitledBorder("Language"));
		
		
		for(Locale locale : localeService.getAvailableLocales()){
			String language = locale.getLanguage();
			final JRadioButton languageItem = new JRadioButton(language);
			
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
			languageGroup.add(languageItem);
			languagePanel.add(languageItem);
		}
		this.add(languagePanel);
	}
	
	private void setLocaleFromString(String locale){
		logger.debug("User sets language to: " + locale);
		localeService.setLocale(new Locale(locale, locale));
	}
	
	public JPanel getGUI() {
		return this;
	}
}
