package husacct.bootstrap;

public class Analyse extends AbstractBootstrap {

    @Override
    public void execute() {
        getControlService().getMainController().getApplicationController().analyseApplication();
        getControlService().getMainController().getMainGui().getMenu().getAnalyseMenu().getAnalysedApplicationOverviewItem().doClick();
        getControlService().getMainController().getMainGui().getMenu().getAnalyseMenu().getAnalysedArchitectureDiagramItem().doClick();
    }
}
