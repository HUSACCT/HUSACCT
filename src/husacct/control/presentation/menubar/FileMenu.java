package husacct.control.presentation.menubar;

import husacct.control.task.MainController;
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
	
	public FileMenu(MainController controller){
		super("File");
		this.mainController = controller;
		this.workspaceController = controller.getWorkspaceController();
		
		
		
		JMenuItem createWorkspaceItem = new JMenuItem("Create workspace");
		this.add(createWorkspaceItem);
		createWorkspaceItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				workspaceController.showCreateWorkspaceGui();
			}
		});
		
		JMenuItem openWorkspaceItem = new JMenuItem("Open workspace");
		this.add(openWorkspaceItem);
		openWorkspaceItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				workspaceController.showOpenWorkspaceGui();
			}
		});
		
		JMenuItem saveWorkspaceItem = new JMenuItem("Save workspace");
		this.add(saveWorkspaceItem);
		saveWorkspaceItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				workspaceController.saveWorkspace();
			}
		});
		
		JMenuItem closeWorkspaceItem = new JMenuItem("Close workspace");
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
		
		
	}
}
