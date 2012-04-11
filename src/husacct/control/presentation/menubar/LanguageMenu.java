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
	LocaleController controller;
	
	public LanguageMenu(LocaleController localeController){
		super("Language");
		this.controller = localeController;
		
		IControlService controlService = ServiceProvider.getInstance().getControlService();
		
		
		for(Locale locale : controller.getAvailableLocales()){
			
			final JCheckBoxMenuItem item = new JCheckBoxMenuItem(locale.getLanguage());
			if(controller.getLocale().getLanguage().equals(locale.getLanguage())){
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
						controller.setNewLocaleFromString(source.getText());
					} else {
						// TODO: mag niet uitgezet worden als het de currentLocale is
					}
					
				}
			});
			
			this.add(item);

		}		
		
	}

}
