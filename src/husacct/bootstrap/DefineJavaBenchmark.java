package husacct.bootstrap;


public class DefineJavaBenchmark extends AbstractBootstrap{

	@Override
	public void execute() {
		defineLogicalArchitecture();
		getControlService().getMainController().getMainGui().getMenu().getDefineMenu().getDefineArchitectureItem().doClick();
	}
	
	private void defineLogicalArchitecture(){
		defineLogicalModules();
		defineRules();
		getDefineService().getDefinitionController().notifyObservers();
	}
	
	private void defineLogicalModules(){
	    getDefineService().getDefinitionController().addModule("Presentation Layer", "This is the presentation layer of the benchmark","Layer");
		getDefineService().getDefinitionController().addModule("Domain Layer", "This is the domain layer of the benchmark","Layer");
		getDefineService().getDefinitionController().addModule("Infrastructure Layer", "This is the infrastructure layer of the benchmark","Layer");
	}
	
	private void defineRules(){
		//TODO! mooie nieuwe custom rules!
	}

	@Override
	public void execute(String[] args) {
		execute();
	}

}
