package husacct.control.task;

import husacct.control.domain.Workspace;
import husacct.control.presentation.workspace.CreateWorkspaceFrame;
import husacct.control.presentation.workspace.OpenWorkspaceFrame;

import javax.swing.JOptionPane;

public class WorkspaceController {

	private static Workspace currentWorkspace;

	public void showCreateWorkspaceGui() {
		new CreateWorkspaceFrame(this);
	}

	public void showOpenWorkspaceGui() {
		new OpenWorkspaceFrame(this);

	}

	public void saveWorkspace() {
		// TODO Auto-generated method stub

	}

	public void closeWorkspace() {	
		Object[] options = { "Yes", "No", "Cancel" };
		int n = JOptionPane.showOptionDialog(null,
				"Save changes?",
				"Close workspace", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
		if (n == JOptionPane.YES_OPTION) {
			saveWorkspace();
		} else if (n == JOptionPane.NO_OPTION) {
			System.out.println("no");
		} else if (n == JOptionPane.CANCEL_OPTION) {
			System.out.println("cancel");
		} else {
			System.out.println("none");
		}
		
	}

}
