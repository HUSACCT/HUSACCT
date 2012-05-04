package husacct.control.presentation.workspace;

import husacct.control.task.MainController;

import javax.swing.JOptionPane;

public class CloseWorkspaceDialog{
	
	public CloseWorkspaceDialog(MainController mainController){
		mainController.getWorkspaceController().closeWorkspace();
	}

}
