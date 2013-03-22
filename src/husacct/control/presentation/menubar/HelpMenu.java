package husacct.control.presentation.menubar;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IServiceListener;
import husacct.control.task.MainController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class HelpMenu extends JMenu {

    private MainController mainController;
    private JMenuItem aboutItem;
    private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();

    public HelpMenu(final MainController mainController) {
        super();
        this.mainController = mainController;
        setText(localeService.getTranslatedString("Help"));
        addComponents();
        setListeners();
    }

    private void addComponents() {
        aboutItem = new JMenuItem(localeService.getTranslatedString("About"));
        aboutItem.setMnemonic(getMnemonicKeycode("AboutMnemonic"));
        this.add(aboutItem);
    }

    private void setListeners() {
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainController.getApplicationController().showAboutHusacctGui();
            }
        });

        final HelpMenu helpMenu = this;
        localeService.addServiceListener(new IServiceListener() {
            public void update() {
                helpMenu.setText(localeService.getTranslatedString("Help"));
                aboutItem.setText(localeService.getTranslatedString("About"));
                aboutItem.setMnemonic(getMnemonicKeycode("AboutMnemonic"));
            }
        });
    }

    private int getMnemonicKeycode(String translatedString) {
        String mnemonicString = localeService.getTranslatedString(translatedString);
        int keyCode = KeyStroke.getKeyStroke(mnemonicString).getKeyCode();
        return keyCode;
    }
}
