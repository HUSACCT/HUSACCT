package husacct.bootstrap;


public class DefineLogicalArchitecture extends AbstractBootstrap{

	@Override
	public void execute() {
		defineLogicalArchitecture();
		getControlService().getMainController().getMainGui().getMenu().getDefineMenu().getDefineArchitectureItem().doClick();
	}
	
	private void defineLogicalArchitecture(){
		defineLogicalModules();
		defineRules();
		defineMappings();
		getDefineService().getDefinitionController().notifyObservers();
	}
	
	private void defineLogicalModules(){
		getDefineService().getDefinitionController().addLayer(-1, "Layer 1", "This is test data");
		getDefineService().getDefinitionController().addLayer(-1, "Layer 2", "This is test data");
		getDefineService().getDefinitionController().addLayer(-1, "Layer 3", "This is test data");
		getDefineService().getDefinitionController().addLayer(-1, "Layer 4", "This is test data");
		getDefineService().getDefinitionController().addSubSystem(0, "SubSystem 1", "This is test data");
		getDefineService().getDefinitionController().addSubSystem(2, "SubSystem 2", "This is test data");
	}
	
	private void defineRules(){
		
	}
	
	private void defineMappings(){
		
	}
	
}
