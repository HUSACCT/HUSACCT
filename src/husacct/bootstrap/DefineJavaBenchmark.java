package husacct.bootstrap;

import husacct.define.domain.SoftwareArchitecture;

import java.util.HashMap;

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
		getDefineService().getDefinitionController().addLayer(0, "Presentation Layer", "This is the presentation layer of the benchmark");
		getDefineService().getDefinitionController().addLayer(0, "Domain Layer", "This is the domain layer of the benchmark");
		getDefineService().getDefinitionController().addLayer(0, "Infrastructure Layer", "This is the infrastructure layer of the benchmark");
	}
	
	private void defineRules(){
		//TODO! mooie nieuwe custom rules!
	}
	
	private String[] getViolationTypeByRuleType(String ruleTypeKey){
		String[] violationTypes = new String[12];
		violationTypes[0] = "InvocMethod";
		violationTypes[1] = "Exception";
		violationTypes[2] = "AccessPropertyOrField";
		violationTypes[3] = "ExtendsInterface";
		violationTypes[4] = "Import";
		violationTypes[5] = "ExtendsConcrete";
		violationTypes[6] = "Annotation";
		violationTypes[7] = "Declaration";
		violationTypes[8] = "InvocConstructor";
		violationTypes[9] = "ExtendsLibrary";		
		violationTypes[10] = "ExtendsAbstract";
		violationTypes[11] = "Implements";

		return violationTypes;
	}

	@Override
	public void execute(String[] args) {
		execute();
	}

}
