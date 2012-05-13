package husacct.control.presentation.menubar;

import husacct.ServiceProvider;
import husacct.control.IControlService;
import husacct.control.ILocaleChangeListener;
import husacct.control.task.LocaleController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;

@SuppressWarnings("serial")
public class LanguageMenu extends JMenu{
	
	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	
	public LanguageMenu(final LocaleController localeController){
		super();
		
		setText(controlService.getTranslatedString("Language"));
		
		controlService = ServiceProvider.getInstance().getControlService();

		for(Locale locale : localeController.getAvailableLocales()){
			
			final JCheckBoxMenuItem item = new JCheckBoxMenuItem(locale.getLanguage());
			if(LocaleController.getLocale().getLanguage().equals(locale.getLanguage())){
				item.setSelected(true);
			}

			controlService.addLocaleChangeListener(new ILocaleChangeListener() {
				@Override
				public void update(Locale newLocale) {
					if(newLocale.getLanguage().equals(item.getText())){
						item.setSelected(true);
					} else {
						item.setSelected(false);
					}
				}
			});
			
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JCheckBoxMenuItem source = (JCheckBoxMenuItem)e.getSource();
					if(source.isSelected()){
						localeController.setNewLocaleFromString(source.getText());
					} else {
						if(source.getText().equals(LocaleController.getLocale().getLanguage())){
							source.setSelected(true);
						}
					}
				}
			});
			this.add(item);
		}
		
		final LanguageMenu languageMenu = this;
		controlService.addLocaleChangeListener(new ILocaleChangeListener() {
			public void update(Locale newLocale) {
				languageMenu.setText(controlService.getTranslatedString("Language"));				
			}
		});
	}
}
