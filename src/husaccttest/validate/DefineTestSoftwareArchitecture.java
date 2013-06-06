package husaccttest.validate;

import husacct.define.domain.AppliedRule;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.Module;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;

/*
 * IMPORTANT NOTICE
 * The code of this class is a partly modified version of the code from the class husaccttest.define.DefineSoftwareArchitectureTests
 * If changes are made to the DefineSoftwareArchitectureTests class, the changes must be also be applied to the methods of this class, if necessarily
 */

public class DefineTestSoftwareArchitecture {
	private SoftwareArchitecture softwareA;

	private AppliedRule rule;
	private Module rootModule;
	private Module moduleFrom;
	private Module moduleTo;
	private SoftwareUnitDefinition sud1;
	private SoftwareUnitDefinition sud2;
	private SoftwareUnitDefinition sud3;
	private SoftwareUnitDefinition sud4;
	private SoftwareUnitDefinition sud5;
	
	
	public DefineTestSoftwareArchitecture() {
		setInstance();
	}

	private void setInstance() {
		SoftwareArchitecture.setInstance(new SoftwareArchitecture("TestSoftwareArchitecture", "description"));
		softwareA = SoftwareArchitecture.getInstance();
	}

	public void addModuleFrom(Module moduleFrom) {
		this.moduleFrom = moduleFrom;
		softwareA.addSeperatedModule(moduleFrom);
	}
	
	public void addModuleTo(Module moduleTo) {
		this.moduleTo = moduleTo;
		softwareA.addSeperatedModule(moduleTo);
	}
	
	public void addAppliedRule(AppliedRule rule) {
		this.rule = rule;
		softwareA.addSeperatedAppliedRule(rule);
	}
	

//	
//	
//	
//	
//
//	public Layer addLayerModule(Module moduleFrom, Module moduleTo,
//			Layer... layers) {
//		softwareA.addModule(moduleFrom);
//		softwareA.addModule(moduleTo);
//		return layerFrom;
//	}
//
//	public void addSoftwareUnitDefinition(
//			SoftwareUnitDefinition... softwareunitdefinitions) {
//		for (SoftwareUnitDefinition softwareunitdefintion : softwareunitdefinitions) {
//			layerFrom.addSUDefinition(softwareunitdefintion);
//		}
//	}
//	
//	public String getAppliedRuleType(){
//		return rule.getRuleType();
//	}
//	
//	public ArrayList<AppliedRule> getAppliedRules() {
//		return softwareA.getAppliedRules();
//	}
}
