package husacct.control.presentation;
import husacct.control.presentation.menubar.AnalyseMenu;
import husacct.control.presentation.menubar.DefineMenu;
import husacct.control.presentation.menubar.FileMenu;
import husacct.control.presentation.menubar.HelpMenu;
import husacct.control.presentation.menubar.LanguageMenu;
import husacct.control.presentation.menubar.ValidateMenu;
import husacct.control.task.MainController;

import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

public class MainGui extends JFrame{

	private static final long serialVersionUID = 140205650372010347L;

	private MainController mainController;
	private JMenuBar menuBar;
	private String titlePrefix = "HUSACCT";
	
	public MainGui(MainController controller) {
		this.mainController = controller;
		setupFrame();
		createMenuBar();
		this.setContentPane(new JDesktopPane());
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setVisible(true);	
	}

	private void createMenuBar() {
		this.menuBar = new JMenuBar();
		
		FileMenu fileMenu = new FileMenu(mainController);
		DefineMenu defineMenu = new DefineMenu(mainController);
		AnalyseMenu analyseMenu = new AnalyseMenu(mainController);
		ValidateMenu validateMenu = new ValidateMenu(mainController);
		LanguageMenu languageMenu = new LanguageMenu(mainController.getLocaleController());
		HelpMenu helpMenu = new HelpMenu();

		menuBar.add(fileMenu);
		menuBar.add(defineMenu);
		menuBar.add(analyseMenu);
		menuBar.add(validateMenu);
		menuBar.add(languageMenu);
		menuBar.add(helpMenu);
		
		setJMenuBar(menuBar);

	}

	private void setupFrame(){
		setTitle();
		Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("husacct/common/resources/husacct.png"));
		setIconImage(icon);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setBounds(100, 100, 783, 535);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				mainController.exit();		
			}
		});
	}
	
	public void setTitle(String title){
		if(title.length() > 0){
			super.setTitle(titlePrefix + " - " + title);
		} else {
			super.setTitle(titlePrefix);
		}
	}
	
	public void setTitle(){
		setTitle("");
	}

}
