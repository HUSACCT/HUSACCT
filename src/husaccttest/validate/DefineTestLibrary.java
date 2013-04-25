package husaccttest.validate;

import static org.junit.Assert.assertTrue;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.module.Layer;

/*
 * IMPORTANT NOTICE
 * The code of this class is a partly modified version of the code from the class husaccttest.define.DefineSoftwareArchitectureTests
 * If changes are made to the DefineSoftwareArchitectureTests class, the changes must be also be applied to the methods of this class, if necessarily
 */

public class DefineTestLibrary {

	private SoftwareArchitecture sA;

	private Layer moduleFrom;
	private Layer moduleTo;

	private AppliedRule rule;

	public void setInstance(){
		SoftwareArchitecture.setInstance(new SoftwareArchitecture());
		sA = SoftwareArchitecture.getInstance();
		
		assertTrue(sA.getName().equals("SoftwareArchitecture"));
		assertTrue(sA.getDescription().equals("This is the root of the architecture"));
		SoftwareArchitecture.setInstance(new SoftwareArchitecture("Test", "description"));
		sA = SoftwareArchitecture.getInstance();
		assertTrue(sA.getName().equals("Test"));
		assertTrue(sA.getDescription().equals("description"));
	}

	public void addAppliedRule(AppliedRule rule) {
		this.rule = rule;
		sA.addAppliedRule(rule);
	}

	public Layer addLayerModule(Layer... layers) {
		sA.addModule(moduleFrom);
		sA.addModule(moduleTo);
		for (Layer layer : layers) {
			moduleFrom.addSubModule(layer);
		}
		return moduleFrom;
	}

	public void addModule(Layer... layers) {
		sA.addModule(moduleFrom);
		sA.addModule(moduleTo);
		for (Layer layer : layers) {
			moduleFrom.addSubModule(layer);
		}
	}

	public void addSoftwareDefinition(
			SoftwareUnitDefinition... softwareunitdefinitions) {
		for (SoftwareUnitDefinition softwareunitdefintion : softwareunitdefinitions) {
			moduleFrom.addSUDefinition(softwareunitdefintion);
		}
	}
}
