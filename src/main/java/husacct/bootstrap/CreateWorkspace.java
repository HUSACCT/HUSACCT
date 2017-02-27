package husacct.bootstrap;

public class CreateWorkspace extends AbstractBootstrap {

	@Override
	public void execute() {
		getControlService().getMainController().getWorkspaceController().createWorkspace("BootstrapWorkspace");
	}

	@Override
	public void execute(String[] args) {
		execute();
	}
}
