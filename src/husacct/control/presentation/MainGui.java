package husacct.control.presentation;

import husacct.common.Resource;
import husacct.common.help.presentation.HelpableJFrame;
import husacct.common.help.presentation.HelpableJScrollPane;
import husacct.control.presentation.menubar.MenuBar;
import husacct.control.presentation.util.ActionLogPanel;
import husacct.control.presentation.viewcontrol.TaskBar;
import husacct.control.presentation.viewcontrol.TaskBarPane;
import husacct.control.task.MainController;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import com.pagosoft.plaf.PgsLookAndFeel;

public class MainGui extends HelpableJFrame{

	private static final long serialVersionUID = 140205650372010347L;
	private Logger logger = Logger.getLogger(MainGui.class);
	public MainController mainController;
	private String titlePrefix = "HUSACCT";
	private MenuBar menuBar;
	private JDesktopPane desktopPane;
	private HelpableJScrollPane taskBarPane;
	private TaskBar taskBar;
    private static final Color PANELBACKGROUND = UIManager.getColor("Panel.background");
	//private ToolBar toolBar;
	private ActionLogPanel actionLogPanel;
	
	public MainGui(MainController mainController) {
		this.mainController = mainController;
		setup();
		createMenuBar();
		addComponents();
		setVisible(true);
		mainController.getStateController().checkState();
	}

	private void setup(){
		setTitle();
		Image icon = Toolkit.getDefaultToolkit().getImage(Resource.get(Resource.HUSACCT_LOGO));
		setIconImage(icon);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(0, 0, 1020, 750);
		setPgsLookAndFeel();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				mainController.exit();		
			}
		});
	}
	
	private void setPgsLookAndFeel(){
		try {
			UIManager.setLookAndFeel(new PgsLookAndFeel());
		} catch (UnsupportedLookAndFeelException event) {
			logger.warn("Unable to set Pgs look and feel" + event.getMessage());
			setSystemLookAndFeel();
		} 
	}
	
	private void setSystemLookAndFeel(){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException event) {
			logger.warn("Unable to set System look and feel" + event.getMessage());
			setPgsLookAndFeel();
		} catch (ClassNotFoundException e) {
			logger.warn("Unable to set System look and feel" + e.getMessage());
			setPgsLookAndFeel();
		} catch (InstantiationException e) {
			logger.warn("Unable to set System look and feel" + e.getMessage());
			setPgsLookAndFeel();
		} catch (IllegalAccessException e) {
			logger.warn("Unable to set System look and feel" + e.getMessage());
			setPgsLookAndFeel();
		}
	}
	
	private void addComponents(){
		// Create and add contentPane
		JPanel contentPane = new JPanel(new BorderLayout()); 
		actionLogPanel = new ActionLogPanel(mainController);
		contentPane.add(actionLogPanel, BorderLayout.SOUTH);
		//toolBar = new ToolBar(getMenu(), mainController.getStateController()); // Disabled since it takes space and does not add much.
		//contentPane.add(toolBar, BorderLayout.NORTH);
		desktopPane = new JDesktopPane();
		contentPane.add(desktopPane, BorderLayout.CENTER);
		add(contentPane);

		// Create and add taskBarPane
		taskBar = new TaskBar();
	    taskBarPane = new TaskBarPane(taskBar);
	    taskBarPane.setBackground(PANELBACKGROUND);
		add(taskBarPane);
	}
	
	private void createMenuBar() {
		menuBar = new MenuBar(mainController);		
		setJMenuBar(menuBar);
	}
	
	public JDesktopPane getDesktopPane(){
		return desktopPane;
	}
	
	public TaskBar getTaskBar(){
		return taskBar;
	}
	
	public MenuBar getMenu(){
		return menuBar;
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
	
	public ActionLogPanel getActionLogPanel(){
		return actionLogPanel;
	}
}
