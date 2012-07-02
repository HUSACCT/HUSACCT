package husacct.control.presentation.menubar;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IServiceListener;
import husacct.control.task.MainController;

import javax.swing.JMenuBar;
import javax.swing.KeyStroke;

public class MenuBar extends JMenuBar{
	private static final long serialVersionUID = 1L;

	private FileMenu fileMenu;
	private DefineMenu defineMenu;
	private AnalyseMenu analyseMenu;
	private ValidateMenu validateMenu;
	private LanguageMenu languageMenu;
	private HelpMenu helpMenu;

	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private MainController mainController;

	public MenuBar(MainController mainController){
		this.mainController = mainController;
		addComponents();
		addListeners();
	}

	private void addComponents() {

		fileMenu = new FileMenu(mainController);
		defineMenu = new DefineMenu(mainController);
		analyseMenu = new AnalyseMenu(mainController);
		validateMenu = new ValidateMenu(mainController);
		languageMenu = new LanguageMenu(mainController);
		helpMenu = new HelpMenu(mainController);		

		fileMenu.setMnemonic(getMnemonicKeycode("FileMenuMnemonic"));
		defineMenu.setMnemonic(getMnemonicKeycode("DefineMenuMnemonic"));
		analyseMenu.setMnemonic(getMnemonicKeycode("AnalyseMenuMnemonic"));
		validateMenu.setMnemonic(getMnemonicKeycode("ValidateMenuMnemonic"));
		languageMenu.setMnemonic(getMnemonicKeycode("LanguageMenuMnemonic"));
		helpMenu.setMnemonic(getMnemonicKeycode("HelpMenuMnemonic"));

		add(fileMenu);
		add(defineMenu);
		add(analyseMenu);
		add(validateMenu);
		add(languageMenu);
		add(helpMenu);
	}

	private void addListeners(){
		localeService.addServiceListener(new IServiceListener() {
			public void update() {
				fileMenu.setMnemonic(getMnemonicKeycode("FileMenuMnemonic"));
				defineMenu.setMnemonic(getMnemonicKeycode("DefineMenuMnemonic"));
				analyseMenu.setMnemonic(getMnemonicKeycode("AnalyseMenuMnemonic"));
				validateMenu.setMnemonic(getMnemonicKeycode("ValidateMenuMnemonic"));
				languageMenu.setMnemonic(getMnemonicKeycode("LanguageMenuMnemonic"));
				helpMenu.setMnemonic(getMnemonicKeycode("HelpMenuMnemonic"));
			}
		});
	}

	private int getMnemonicKeycode(String translatedString) {
		String mnemonicString = localeService.getTranslatedString(translatedString);
		int keyCode = KeyStroke.getKeyStroke(mnemonicString).getKeyCode();
		return keyCode;
	}

	public FileMenu getFileMenu(){
		return fileMenu;
	}

	public DefineMenu getDefineMenu(){
		return defineMenu;
	}

	public AnalyseMenu getAnalyseMenu(){
		return analyseMenu;
	}

	public ValidateMenu getValidateMenu(){
		return validateMenu;
	}
}
