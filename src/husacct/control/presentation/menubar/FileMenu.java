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
import javax.swing.event.MenuEvent;

@SuppressWarnings("serial")
public class FileMenu extends JMenu {

	private WorkspaceController workspaceController;
	private JMenuItem createWorkspaceItem;
	private JMenuItem openWorkspaceItem;
	private JMenuItem saveWorkspaceItem;
	private JMenuItem closeWorkspaceItem;
	private JMenuItem exitItem;
	
	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	
	public FileMenu(final MainController mainController){
		super();
		setText(controlService.getTranslatedString("File"));
		this.workspaceController = mainController.getWorkspaceController();
		
		createWorkspaceItem = new JMenuItem(controlService.getTranslatedString("CreateWorkspace"));
		createWorkspaceItem.setMnemonic('c');
		createWorkspaceItem.setAccelerator(KeyStroke.getKeyStroke('N', KeyEvent.CTRL_DOWN_MASK));
		this.add(createWorkspaceItem);
		createWorkspaceItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				workspaceController.showCreateWorkspaceGui();
			}
		});
		
		openWorkspaceItem = new JMenuItem(controlService.getTranslatedString("OpenWorkspace"));
		openWorkspaceItem.setMnemonic('o');
		openWorkspaceItem.setAccelerator(KeyStroke.getKeyStroke('O', KeyEvent.CTRL_DOWN_MASK));
		this.add(openWorkspaceItem);
		openWorkspaceItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				workspaceController.showOpenWorkspaceGui();
			}
		});
		
		saveWorkspaceItem = new JMenuItem(controlService.getTranslatedString("SaveWorkspace"));
		saveWorkspaceItem.setMnemonic('s');
		saveWorkspaceItem.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_DOWN_MASK));
		this.add(saveWorkspaceItem);
		saveWorkspaceItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				workspaceController.showSaveWorkspaceGui();
			}
		});
		
		closeWorkspaceItem = new JMenuItem(controlService.getTranslatedString("CloseWorkspace"));
		closeWorkspaceItem.setMnemonic('l');
		this.add(closeWorkspaceItem);
		closeWorkspaceItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				workspaceController.closeWorkspace();
			}
		});		

		JSeparator separator = new JSeparator();
		this.add(separator);

		exitItem = new JMenuItem(controlService.getTranslatedString("Exit"));
		exitItem.setMnemonic('x');
		this.add(exitItem);
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
		
		this.addMenuListener(new MenuAdapter() {
			public void menuSelected(MenuEvent e) {
				mainController.getStateController().checkState();
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
			}
		});
	}
}
