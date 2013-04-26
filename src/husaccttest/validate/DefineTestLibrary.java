package husaccttest.validate;

import husacct.define.domain.AppliedRule;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.module.Layer;
import husacct.define.domain.module.Module;

/*
 * IMPORTANT NOTICE
 * The code of this class is a partly modified version of the code from the class husaccttest.define.DefineSoftwareArchitectureTests
 * If changes are made to the DefineSoftwareArchitectureTests class, the changes must be also be applied to the methods of this class, if necessarily
 */

public class DefineTestLibrary {
	private SoftwareArchitecture sA;

	private Layer layerFrom;
	private AppliedRule rule;

	public DefineTestLibrary() {
		this.layerFrom = new Layer("FromLayer");
		setInstance();
	}

	private void setInstance() {
		SoftwareArchitecture.setInstance(new SoftwareArchitecture(
				"TestSoftwareArchitecture", "description"));
		sA = SoftwareArchitecture.getInstance();
	}

	public void addAppliedRule(AppliedRule rule) {
		this.rule = rule;
		sA.addAppliedRule(rule);
	}

	public Layer addLayerModule(Module moduleFrom, Module moduleTo,
			Layer... layers) {
		sA.addModule(moduleFrom);
		sA.addModule(moduleTo);
		return layerFrom;
	}

	public void addSoftwareUnitDefinition(
			SoftwareUnitDefinition... softwareunitdefinitions) {
		for (SoftwareUnitDefinition softwareunitdefintion : softwareunitdefinitions) {
			layerFrom.addSUDefinition(softwareunitdefintion);
		}
	}
	
	public String getAppliedRule(){
		return rule.getRuleType();
	}
}
