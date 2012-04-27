package husacct.control.presentation.workspace.loaders;

import husacct.control.task.WorkspaceController;

public interface ILoaderFrame {
	public void setWorkspaceController(WorkspaceController workspaceController);
	public void setVisible(boolean visible);
}
