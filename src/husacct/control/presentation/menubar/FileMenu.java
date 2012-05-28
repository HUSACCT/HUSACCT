package husacct.control.presentation.menubar;

import husacct.ServiceProvider;
import husacct.control.IControlService;
import husacct.control.ILocaleChangeListener;
import husacct.control.task.IStateChangeListener;
import husacct.control.task.MainController;
import husacct.control.task.States;
import husacct.control.task.WorkspaceController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Locale;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class FileMenu extends JMenu {

	private WorkspaceController workspaceController;
	private MainController mainController;
	private JMenuItem createWorkspaceItem;
	private JMenuItem openWorkspaceItem;
	private JMenuItem saveWorkspaceItem;
	private JMenuItem closeWorkspaceItem;
	private JMenuItem exitItem;

	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	
	public FileMenu(final MainController mainController){
		super();
		this.mainController = mainController;
		this.workspaceController = mainController.getWorkspaceController();
		setText(controlService.getTranslatedString("File"));
		addComponents();
		setListeners();
	}
	
	private void addComponents(){
		createWorkspaceItem = new JMenuItem(controlService.getTranslatedString("CreateWorkspace"));
		createWorkspaceItem.setMnemonic(getMnemonicKeycode("CreateWorkspaceMnemonic"));
		createWorkspaceItem.setAccelerator(KeyStroke.getKeyStroke('N', KeyEvent.CTRL_DOWN_MASK));
				
		openWorkspaceItem = new JMenuItem(controlService.getTranslatedString("OpenWorkspace"));
		openWorkspaceItem.setMnemonic(getMnemonicKeycode("OpenWorkspaceMnemonic"));
		openWorkspaceItem.setAccelerator(KeyStroke.getKeyStroke('O', KeyEvent.CTRL_DOWN_MASK));
				
		saveWorkspaceItem = new JMenuItem(controlService.getTranslatedString("SaveWorkspace"));
		saveWorkspaceItem.setMnemonic(getMnemonicKeycode("SaveWorkspaceMnemonic"));
		saveWorkspaceItem.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_DOWN_MASK));
				
		closeWorkspaceItem = new JMenuItem(controlService.getTranslatedString("CloseWorkspace"));
		closeWorkspaceItem.setMnemonic(getMnemonicKeycode("CloseWorkspaceMnemonic"));
				
		JSeparator separator = new JSeparator();		
		exitItem = new JMenuItem(controlService.getTranslatedString("Exit"));
		exitItem.setMnemonic(getMnemonicKeycode("ExitMnemonic"));
		
		this.add(createWorkspaceItem);
		this.add(openWorkspaceItem);
		this.add(saveWorkspaceItem);
		this.add(closeWorkspaceItem);
		this.add(separator);
		this.add(exitItem);
	}
	
	private void setListeners(){
		createWorkspaceItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				workspaceController.showCreateWorkspaceGui();
			}
		});
		
		openWorkspaceItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				workspaceController.showOpenWorkspaceGui();
			}
		});
		
		saveWorkspaceItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				workspaceController.showSaveWorkspaceGui();
			}
		});
		
		closeWorkspaceItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				workspaceController.closeWorkspace();
			}
		});	
		
		exitItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.exit();
			}
		});
		
		mainController.getStateController().addStateChangeListener(new IStateChangeListener() {
			public void changeState(List<States> states) {
				createWorkspaceItem.setEnabled(false);
				openWorkspaceItem.setEnabled(false);
				saveWorkspaceItem.setEnabled(false);
				closeWorkspaceItem.setEnabled(false);
				
				if(states.contains(States.OPENED)){
					closeWorkspaceItem.setEnabled(true);
					saveWorkspaceItem.setEnabled(true);					
				}
				
				if(states.contains(States.NONE) || states.contains(States.OPENED)){
					createWorkspaceItem.setEnabled(true);
					openWorkspaceItem.setEnabled(true);
				}
			}
		});
		
		final FileMenu fileMenu = this;
		controlService.addLocaleChangeListener(new ILocaleChangeListener() {
			public void update(Locale newLocale) {
				fileMenu.setText(controlService.getTranslatedString("File"));
				createWorkspaceItem.setText(controlService.getTranslatedString("CreateWorkspace"));
				openWorkspaceItem.setText(controlService.getTranslatedString("OpenWorkspace"));
				saveWorkspaceItem.setText(controlService.getTranslatedString("SaveWorkspace"));
				closeWorkspaceItem.setText(controlService.getTranslatedString("CloseWorkspace"));
				exitItem.setText(controlService.getTranslatedString("Exit"));
				createWorkspaceItem.setMnemonic(getMnemonicKeycode("CreateWorkspaceMnemonic"));
				openWorkspaceItem.setMnemonic(getMnemonicKeycode("OpenWorkspaceMnemonic"));
				saveWorkspaceItem.setMnemonic(getMnemonicKeycode("SaveWorkspaceMnemonic"));
				closeWorkspaceItem.setMnemonic(getMnemonicKeycode("CloseWorkspaceMnemonic"));
				exitItem.setMnemonic(getMnemonicKeycode("ExitMnemonic"));
			}
		});
	}
	
	public JMenuItem getCreateWorkspaceItem(){
		return createWorkspaceItem;
	}
	
	public JMenuItem getOpenWorkspaceItem(){
		return openWorkspaceItem;
	}
	
	public JMenuItem getSaveWorkspaceItem(){
		return saveWorkspaceItem;
	}
	
	public JMenuItem getCloseWorkspace(){
		return closeWorkspaceItem;
	}
	
	public JMenuItem getExitItem(){
		return exitItem;
	}
	
	private int getMnemonicKeycode(String translatedString) {
		String mnemonicString = controlService.getTranslatedString(translatedString);
		int keyCode = KeyStroke.getKeyStroke(mnemonicString).getKeyCode();
		return keyCode;
	}
}
