package husacct.bootstrap;

public class MapJavaBenchmark extends AbstractBootstrap {

	@Override
	public void execute() {
		mapArchitecture();
		getControlService().getMainController().getMainGui().getMenu().getDefineMenu().getDefineArchitectureItem().doClick();
	}

	private void mapArchitecture() {
		getDefineService().getSoftwareUnitController().save(0L, "presentation", "PACKAGE");
		getDefineService().getSoftwareUnitController().save(2L, "domain", "PACKAGE");
		getDefineService().getSoftwareUnitController().save(4L, "infrastructure", "PACKAGE");
	}
	
}
