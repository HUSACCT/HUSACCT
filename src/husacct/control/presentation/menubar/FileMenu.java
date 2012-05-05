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

@SuppressWarnings("serial")
public class FileMenu extends JMenu {

	private WorkspaceController workspaceController;
	private JMenuItem createWorkspaceItem;
	private JMenuItem openWorkspaceItem;
	private JMenuItem saveWorkspaceItem;
	private JMenuItem closeWorkspaceItem;
	
	public FileMenu(final MainController mainController){
		super("File");
		this.workspaceController = mainController.getWorkspaceController();
		
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
		
		mainController.getStateController().addStateChangeListener(new IStateChangeListener() {
			public void changeState(int state) {
				createWorkspaceItem.setEnabled(false);
				openWorkspaceItem.setEnabled(false);
				saveWorkspaceItem.setEnabled(false);
				closeWorkspaceItem.setEnabled(false);
				switch(state){
					case StateController.VALIDATED:
					case StateController.MAPPED:
					case StateController.ANALYSED:
					case StateController.DEFINED:
					case StateController.EMPTY: {
						closeWorkspaceItem.setEnabled(true);
						saveWorkspaceItem.setEnabled(true);
					}
					case StateController.NONE: {
						createWorkspaceItem.setEnabled(true);
						openWorkspaceItem.setEnabled(true);
					}
				}
			}
		});
		
		this.addMenuListener(new MenuListenerAdapter() {
			public void menuSelected(MenuEvent e) {
				mainController.getStateController().checkState();
			}
		});
	}
}
