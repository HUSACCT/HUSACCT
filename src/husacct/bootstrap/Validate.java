package husacct.bootstrap;

public class Validate extends AbstractBootstrap {

    @Override
    public void execute() {
        getControlService().getMainController().getMainGui().getMenu().getValidateMenu().getValidateNowItem().doClick();
        getValidateService().checkConformance();
    }
}
