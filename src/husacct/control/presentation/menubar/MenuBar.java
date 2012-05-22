package husacct.control.presentation.menubar;

import husacct.control.task.MainController;

import javax.swing.JMenuBar;

public class MenuBar extends JMenuBar{
	private static final long serialVersionUID = 1L;
	
	FileMenu fileMenu;
	DefineMenu defineMenu;
	AnalyseMenu analyseMenu;
	ValidateMenu validateMenu;
	LanguageMenu languageMenu;
	HelpMenu helpMenu;
	
	public MenuBar(MainController mainController){
		fileMenu = new FileMenu(mainController);
		defineMenu = new DefineMenu(mainController);
		analyseMenu = new AnalyseMenu(mainController);
		validateMenu = new ValidateMenu(mainController);
		languageMenu = new LanguageMenu(mainController);
		helpMenu = new HelpMenu(mainController);

		fileMenu.setMnemonic('F');
		defineMenu.setMnemonic('D');
		analyseMenu.setMnemonic('A');
		validateMenu.setMnemonic('V');
		languageMenu.setMnemonic('L');
		helpMenu.setMnemonic('H');
		
		add(fileMenu);
		add(defineMenu);
		add(analyseMenu);
		add(validateMenu);
		add(languageMenu);
		add(helpMenu);
		
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
