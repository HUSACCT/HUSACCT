package husacct.bootstrap;

public class Validate extends AbstractBootstrap {

	@Override
	public void execute() {
		getControlService().getMainController().getMainGui().getMenu().getValidateMenu().getValidateItem().doClick();
		getValidateService().checkConformance();
	}

	@Override
	public void execute(String[] args) {
		execute();
	}

}
