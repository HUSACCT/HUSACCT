package husacct.control.presentation.menubar;

import husacct.ServiceProvider;
import husacct.control.IControlService;
import husacct.control.task.IStateChangeListener;
import husacct.control.task.MainController;
import husacct.control.task.StateController;
import husacct.control.task.WorkspaceController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

@SuppressWarnings("serial")
public class FileMenu extends JMenu {
	
	private WorkspaceController workspaceController;
	private MainController mainController;
	private JMenuItem createWorkspaceItem;
	private JMenuItem openWorkspaceItem;
	private JMenuItem saveWorkspaceItem;
	private JMenuItem closeWorkspaceItem;
	private int currentState;
	
	public FileMenu(MainController controller){
		super("File");
		this.mainController = controller;
		this.workspaceController = controller.getWorkspaceController();
		
		
		
		createWorkspaceItem = new JMenuItem("Create workspace");
		this.add(createWorkspaceItem);
		createWorkspaceItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				workspaceController.showCreateWorkspaceGui();
			}
		});
		
		openWorkspaceItem = new JMenuItem("Open workspace");
		this.add(openWorkspaceItem);
		openWorkspaceItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				workspaceController.showOpenWorkspaceGui();
			}
		});
		
		saveWorkspaceItem = new JMenuItem("Save workspace");
		this.add(saveWorkspaceItem);
		saveWorkspaceItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				workspaceController.saveWorkspace();
			}
		});
		
		closeWorkspaceItem = new JMenuItem("Close workspace");
		this.add(closeWorkspaceItem);
		closeWorkspaceItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				workspaceController.closeWorkspace();
			}
		});		

		JSeparator separator = new JSeparator();
		this.add(separator);

		JMenuItem exitItem = new JMenuItem("Exit");
		this.add(exitItem);
		exitItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				mainController.exit();
			}
		});
		
		controller.getStateController().addStateChangeListener(new IStateChangeListener() {

			@Override
			public void changeState(int state) {
				currentState = mainController.getStateController().getState();
				if(currentState == 0){
					createWorkspaceItem.setEnabled(true);
					openWorkspaceItem.setEnabled(true);
					saveWorkspaceItem.setEnabled(false);
					closeWorkspaceItem.setEnabled(false);
				}else if(currentState == 1){
					createWorkspaceItem.setEnabled(true);
					openWorkspaceItem.setEnabled(true);
					saveWorkspaceItem.setEnabled(true);
					closeWorkspaceItem.setEnabled(true);
				}else if(currentState == 2){
					createWorkspaceItem.setEnabled(true);
					openWorkspaceItem.setEnabled(true);
					saveWorkspaceItem.setEnabled(true);
					closeWorkspaceItem.setEnabled(true);
				}else if(currentState == 3){
					createWorkspaceItem.setEnabled(true);
					openWorkspaceItem.setEnabled(true);
					saveWorkspaceItem.setEnabled(true);
					closeWorkspaceItem.setEnabled(true);
				}else if(currentState == 4){
					createWorkspaceItem.setEnabled(true);
					openWorkspaceItem.setEnabled(true);
					saveWorkspaceItem.setEnabled(true);
					closeWorkspaceItem.setEnabled(true);
				}
			}
			
		});
	}
}
