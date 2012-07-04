package husacct.bootstrap;

import husacct.define.domain.SoftwareArchitecture;

public class MapJavaBenchmark extends AbstractBootstrap {

	@Override
	public void execute() {
		mapArchitecture();
		getControlService().getMainController().getMainGui().getMenu().getDefineMenu().getDefineArchitectureItem().doClick();
	}

	private void mapArchitecture() {
		getDefineService().getSoftwareUnitController().save(SoftwareArchitecture.getInstance().getModules().get(0).getId(), "presentation", "PACKAGE");
		getDefineService().getSoftwareUnitController().save(SoftwareArchitecture.getInstance().getModules().get(1).getId(), "domain", "PACKAGE");
		getDefineService().getSoftwareUnitController().save(SoftwareArchitecture.getInstance().getModules().get(2).getId(), "infrastructure", "PACKAGE");
	}
	
}
