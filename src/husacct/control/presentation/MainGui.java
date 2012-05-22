package husacct.control.presentation;
import husacct.ServiceProvider;
import husacct.control.IControlService;
import husacct.control.ILocaleChangeListener;
import husacct.control.presentation.menubar.AnalyseMenu;
import husacct.control.presentation.menubar.DefineMenu;
import husacct.control.presentation.menubar.FileMenu;
import husacct.control.presentation.menubar.HelpMenu;
import husacct.control.presentation.menubar.LanguageMenu;
import husacct.control.presentation.menubar.ValidateMenu;
import husacct.control.presentation.taskbar.TaskBar;
import husacct.control.presentation.toolbar.ToolBar;
import husacct.control.presentation.util.MoonWalkPanel;
import husacct.control.task.MainController;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import com.pagosoft.plaf.PgsLookAndFeel;

public class MainGui extends JFrame{

	private static final long serialVersionUID = 140205650372010347L;

	private Logger logger = Logger.getLogger(MainGui.class);
	
	private MainController mainController;
	private JMenuBar menuBar;
	private String titlePrefix = "HUSACCT";
	private JDesktopPane desktopPane;
	private TaskBar taskBar;
	private MoonWalkPanel moonwalkPanel;
	private Thread moonwalkThread;
	private ToolBar toolBar;
	private FileMenu fileMenu;
	private DefineMenu defineMenu;
	private AnalyseMenu analyseMenu;
	private ValidateMenu validateMenu;
	private LanguageMenu languageMenu;
	private HelpMenu helpMenu;
	
	IControlService controlService = ServiceProvider.getInstance().getControlService();
	
	public MainGui(MainController controller) {
		this.mainController = controller;
		setup();
		addComponents();
		addListeners();
		createMenuBar();
		setVisible(true);
	}

	private void setup(){
		setTitle();
		Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/husacct/common/resources/husacct.png"));
		setIconImage(icon);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 783, 535);
		setLookAndFeel();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				mainController.exit();		
			}
		});
	}
	
	private void setLookAndFeel(){
		try {
			UIManager.setLookAndFeel(new PgsLookAndFeel());
		} catch (UnsupportedLookAndFeelException event) {
			logger.debug("Unable to set look and feel" + event.getMessage());
			
		}
	}
	
	private void addComponents(){
		JPanel contentPane = new JPanel(new BorderLayout()); 
		desktopPane = new JDesktopPane();
		JPanel taskBarPane = new JPanel(new GridLayout());
		moonwalkPanel = new MoonWalkPanel();
		moonwalkThread = new Thread(moonwalkPanel);
		toolBar = new ToolBar(mainController);
		taskBar = new TaskBar();
		
		taskBarPane.add(taskBar);
		
		contentPane.add(toolBar, BorderLayout.NORTH);
		contentPane.add(desktopPane, BorderLayout.CENTER);
		
		add(contentPane);
		add(moonwalkPanel);
		add(taskBarPane);
	}
	
	private void addListeners(){
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent event) {
				if(event.getKeyCode() == KeyEvent.VK_F12){
					try {
						moonwalkThread.start();
					} catch (Exception e){
						logger.debug("Unable to start Moonwalk");
					}
				}
				return false;
			}
		});
		
		controlService.addLocaleChangeListener(new ILocaleChangeListener() {
			public void update(Locale newLocale) {
				fileMenu.setMnemonic(getMnemonicKeycode("FileMenuMnemonic"));
				defineMenu.setMnemonic(getMnemonicKeycode("DefineMenuMnemonic"));
				analyseMenu.setMnemonic(getMnemonicKeycode("AnalyseMenuMnemonic"));
				validateMenu.setMnemonic(getMnemonicKeycode("ValidateMenuMnemonic"));
				languageMenu.setMnemonic(getMnemonicKeycode("LanguageMenuMnemonic"));
				helpMenu.setMnemonic(getMnemonicKeycode("HelpMenuMnemonic"));
			}
		});
	}
	
	private void createMenuBar() {
		this.menuBar = new JMenuBar();
		
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
		
		menuBar.add(fileMenu);
		menuBar.add(defineMenu);
		menuBar.add(analyseMenu);
		menuBar.add(validateMenu);
		menuBar.add(languageMenu);
		menuBar.add(helpMenu);
		
		setJMenuBar(menuBar);

	}
	
	public JDesktopPane getDesktopPane(){
		return desktopPane;
	}
	
	public TaskBar getTaskBar(){
		return taskBar;
	}
	
	public void setTitle(String title){
		if(title.length() > 0){
			super.setTitle(titlePrefix + " - " + title);
		} else {
			super.setTitle(titlePrefix);
		}
	}
	
	private void setTitle(){
		setTitle("");
	}

	private int getMnemonicKeycode(String translatedString) {
		String mnemonicString = controlService.getTranslatedString(translatedString);
		int keyCode = KeyStroke.getKeyStroke(mnemonicString).getKeyCode();
		return keyCode;
	}
	
}
