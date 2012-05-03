package husacct.control.presentation.workspace;

import husacct.control.task.MainController;

import javax.swing.JOptionPane;

public class CreateWorkspaceDialog{
	
	public CreateWorkspaceDialog(MainController mainController){
		String helpText = "Workspace Name";
		String defaultText = "myHusacctWorkspace";
		String dialogTitle = "Create workspace";
		String s = (String) JOptionPane.showInputDialog(mainController.getMainGui(), helpText, dialogTitle, JOptionPane.PLAIN_MESSAGE, null , null, defaultText);

		if ((s != null) && (s.length() > 0)) {
			mainController.getWorkspaceController().createWorkspace(s);
		}
	}

}
