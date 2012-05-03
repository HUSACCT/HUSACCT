package husacct.control.presentation.menubar;

import husacct.control.task.IStateChangeListener;
import husacct.control.task.MainController;
import husacct.control.task.StateController;
import husacct.control.task.WorkspaceController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

@SuppressWarnings("serial")
public class FileMenu extends JMenu {

	private StateController stateController;
	private WorkspaceController workspaceController;
	private JMenuItem createWorkspaceItem;
	private JMenuItem openWorkspaceItem;
	private JMenuItem saveWorkspaceItem;
	private JMenuItem closeWorkspaceItem;
	private int currentState;
	
	public FileMenu(final MainController mainController){
		super("File");
		this.workspaceController = mainController.getWorkspaceController();
		
		this.stateController = mainController.getStateController();
		currentState = stateController.getState();
		
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
				workspaceController.showSaveWorkspaceGui();
			}
		});
		
		closeWorkspaceItem = new JMenuItem("Close workspace");
		this.add(closeWorkspaceItem);
		closeWorkspaceItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				workspaceController.showCloseWorkspaceGui();
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
		
		//disable buttons on start
		saveWorkspaceItem.setEnabled(false);
		closeWorkspaceItem.setEnabled(false);
		
		mainController.getStateController().addStateChangeListener(new IStateChangeListener() {

			@Override
			public void changeState(int state) {
				currentState = stateController.getState();
				if(currentState == StateController.NONE){
					createWorkspaceItem.setEnabled(true);
					openWorkspaceItem.setEnabled(true);
					saveWorkspaceItem.setEnabled(false);
					closeWorkspaceItem.setEnabled(false);
				}else if(currentState == StateController.EMPTY){
					createWorkspaceItem.setEnabled(true);
					openWorkspaceItem.setEnabled(true);
					saveWorkspaceItem.setEnabled(true);
					closeWorkspaceItem.setEnabled(true);
				}else if(currentState == StateController.DEFINED){
					createWorkspaceItem.setEnabled(true);
					openWorkspaceItem.setEnabled(true);
					saveWorkspaceItem.setEnabled(true);
					closeWorkspaceItem.setEnabled(true);
				}else if(currentState == StateController.MAPPED){
					createWorkspaceItem.setEnabled(true);
					openWorkspaceItem.setEnabled(true);
					saveWorkspaceItem.setEnabled(true);
					closeWorkspaceItem.setEnabled(true);
				}else if(currentState == StateController.VALIDATED){
					createWorkspaceItem.setEnabled(true);
					openWorkspaceItem.setEnabled(true);
					saveWorkspaceItem.setEnabled(true);
					closeWorkspaceItem.setEnabled(true);
				}
			}
			
		});
		
		// TODO: refactor including adapter
		this.addMenuListener(new MenuListener() {
			
			@Override
			public void menuSelected(MenuEvent arg0) {
				stateController.checkState();
				
			}

			@Override
			public void menuCanceled(MenuEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void menuDeselected(MenuEvent e) {
				// TODO Auto-generated method stub
				
			}

		});
	}
}
