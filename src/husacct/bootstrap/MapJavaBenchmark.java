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
	
	private void mapArchitecture(int moduleId, String softwareUnit, String type) {
		getDefineService().getSoftwareUnitController().save(SoftwareArchitecture.getInstance().getModules().get(moduleId).getId(), softwareUnit, type);
	}

	@Override
	public void execute(String[] args) {
		for(int i = 0; i < args.length; i++){
			String argument = args[i];
			String softwareUnit = "";
			String type = "PACKAGE";
			if(argument.contains(">")){
				softwareUnit = argument.substring(0, argument.indexOf('>'));
				type = argument.substring(argument.indexOf('>') + 1);
			}else if(argument.length() > 0){
				softwareUnit = argument;
			}
			mapArchitecture(i, softwareUnit, type);
		}
		execute();
	}
}
