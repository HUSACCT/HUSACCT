package husacct.control.presentation;
import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.common.help.presentation.HelpableJFrame;
import husacct.common.locale.ILocaleService;
import husacct.control.IControlService;
import husacct.control.presentation.menubar.MenuBar;
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
import java.awt.event.WindowFocusListener;

import javax.swing.BoxLayout;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.pagosoft.plaf.PgsLookAndFeel;

public class MainGui extends HelpableJFrame{

	private static final long serialVersionUID = 140205650372010347L;
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private Logger logger = Logger.getLogger(MainGui.class);
	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	
	private MainController mainController;
	private MenuBar menuBar;
	private String titlePrefix = "HUSACCT";
	private JDesktopPane desktopPane;
	private TaskBar taskBar;
	private MoonWalkPanel moonwalkPanel;
	private Thread moonwalkThread;
	private JDialog actionLogDialog;
	private ToolBar toolBar;
	
	
	public MainGui(MainController mainController) {
		this.mainController = mainController;
		setup();
		createMenuBar();
		addComponents();
		addListeners();
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
		toolBar = new ToolBar(getMenu(), mainController.getStateController());
		taskBar = new TaskBar();
		
		taskBarPane.add(taskBar);
		
		contentPane.add(toolBar, BorderLayout.NORTH);
		contentPane.add(desktopPane, BorderLayout.CENTER);
		
		//createActionLogDialog();
		
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
		
		addWindowFocusListener(new WindowFocusListener() {  
			@Override  
			public void windowGainedFocus(WindowEvent e) {
				actionLogDialog.setVisible(true);
			}  
			@Override  
			public void windowLostFocus(WindowEvent e) {
				actionLogDialog.setVisible(false);
			}  
		});  
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
	
	private void createActionLogDialog(){
		actionLogDialog = new JDialog(this);
		actionLogDialog.setAlwaysOnTop(true);
		actionLogDialog.setFocusableWindowState(false);
		actionLogDialog.setFocusable(false);
		actionLogDialog.setTitle(localeService.getTranslatedString("ActionLog"));
		actionLogDialog.setVisible(true);
		actionLogDialog.setSize(250, 250);
		actionLogDialog.setLocation(533, 285);
		
		DefaultTableModel logTableModel = new DefaultTableModel();
		JTable logTable = new JTable(logTableModel){
			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false;
			}
		};

		logTableModel.addColumn(localeService.getTranslatedString("ActionLog"));
		
		
		
		/*for (Entry<String, HashMap<String, String>> entry : tableData.entrySet()) {
		    Long analysisTimestampLong = Long.parseLong(entry.getKey());
			Date analysisTimestampDate = new Date(analysisTimestampLong*1000);
			DateFormat analysisTimestampFormat = new SimpleDateFormat("dd-MM-yyyy hh:MM:ss");
			
		    HashMap<String, String> analysisData = entry.getValue();
		    logTableModel.addRow(new Object[]{
		    		analysisData.get("application"), 
		    		analysisData.get("project"), 
		    		analysisData.get("path"), 
		    		analysisTimestampFormat.format(analysisTimestampDate),
		    		analysisData.get("packages"), 
		    		analysisData.get("classes"), 
		    		analysisData.get("interfaces"), 
		    		analysisData.get("dependencies"), 
		    		analysisData.get("violations")
		    });
		}*/
		
		actionLogDialog.add(new JScrollPane(logTable));
	}
	
}
