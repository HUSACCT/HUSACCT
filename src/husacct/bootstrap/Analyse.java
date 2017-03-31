package husacct.bootstrap;



public class Analyse extends AbstractBootstrap{
	
	@Override
	public void execute() {
		getControlService().getMainController().getApplicationController().analyseApplication();
		getControlService().getMainController().getMainGui().getMenu().getAnalyseMenu().getAnalysedArchitectureDiagramItem().doClick();
	}

	@Override
	public void execute(String[] args) {
		execute();
	}
}
