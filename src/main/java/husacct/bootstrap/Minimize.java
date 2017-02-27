package husacct.bootstrap;

import java.awt.Frame;

public class Minimize extends AbstractBootstrap {
	@Override
	public void execute() {
		this.getControlService().getMainController().getMainGui().setState ( Frame.ICONIFIED );
	}

	@Override
	public void execute(String[] args) {
		execute();
	}
}