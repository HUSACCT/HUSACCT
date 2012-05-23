package husaccttest.define;

import static org.junit.Assert.assertTrue;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.PhysicalPathDTO;
import husacct.common.dto.RuleDTO;
import husacct.define.DefineServiceImpl;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.Layer;
import husacct.define.domain.module.Module;
import husacct.define.domain.module.SubSystem;

import org.junit.Before;
import org.junit.Test;

public class DefineServiceTests {
	private SoftwareArchitecture sA = SoftwareArchitecture.getInstance();
	private DefineServiceImpl defineService = new DefineServiceImpl();
	
	//Before
	@Before
	public void setUp(){
		sA = new SoftwareArchitecture("Test architecture", "This architecture is used for testing purposes");
		SoftwareArchitecture.setInstance(sA);
		Module module1 = new SubSystem("SubSystem 1", "This is subsystem 1");
		Module module2 = new SubSystem("SubSystem 2", "This is subsystem 2");
		Module module3 = new SubSystem("SubSystem 3", "This is subsystem 3");

		Module layer1 = new Layer("Layer 1", "This is layer 1", 1);
		Module layer2 = new Layer("Layer 2", "This is layer 2", 2);

		Module subModule1 = new SubSystem("SubSystem 4", "This is a subsystem");
		Module subModule11 = new SubSystem("SubSystem 5", "This is a subsystem");
		Module subModule2 = new SubSystem("SubSystem 6", "This is a subsystem");		
		Module subModule3 = new SubSystem("SubSystem 7", "This is a subsystem");		

		AppliedRule rule1 = new AppliedRule("IsNotAllowedToUse", "Test", new String[]{},
				"", module2, module1, true);

		AppliedRule exception1 = new AppliedRule("IsAllowedToUse", "Test", new String[]{},
				"", subModule2, subModule1, true);
		
		//TODO: Test SoftwareUnitDefinitions
		module1.addSubModule(subModule1);
		module1.addSubModule(subModule11);
		module2.addSubModule(subModule2);
		module3.addSubModule(subModule3);
		
		rule1.addException(exception1);
		
		layer1.addSubModule(module1);
		layer2.addSubModule(module2);
		
		sA.addModule(layer1);
		sA.addModule(layer2);
		sA.addModule(module3);
		
		sA.addAppliedRule(rule1);
		
	}
	
	@Test
	public void isDefined(){
		boolean testWorks = true;
		defineService.isDefined();
		assertTrue(testWorks);
	}
	@Test
	public void isMapped(){
		boolean testWorks = true;
		defineService.isMapped();
		assertTrue(testWorks);
	}
	
	@Test
	public void createAndGetApplication(){
		boolean testWorks = true;
		defineService.createApplication("Application1", new String[] {"c:/Application1/"}, "Java", "1.0");
		
		ApplicationDTO appDTO = defineService.getApplicationDetails();
		
		testWorks = testWorks && appDTO.name.equals("Application1");
		testWorks = testWorks && areArraysEqual(appDTO.paths, new String[] {"c:/Application1/"});
		testWorks = testWorks && appDTO.programmingLanguage.equals("Java");
		testWorks = testWorks && appDTO.version.equals("1.0");
		assertTrue(testWorks);
	}
	
	@Test 
	public void getRootModules(){
		boolean testWorks = true;
		ModuleDTO[] rootModuleDTOs = defineService.getRootModules();
		testWorks = testWorks && rootModuleDTOs.length == 3;
		
		testWorks = testWorks && rootModuleDTOs[0].logicalPath.equals("Layer 1");
		testWorks = testWorks && rootModuleDTOs[0].type.equals("Layer");
		testWorks = testWorks && areArraysEqual(rootModuleDTOs[0].physicalPathDTOs, new PhysicalPathDTO[] {});
		testWorks = testWorks && rootModuleDTOs[0].subModules.length == 0;
		
		testWorks = testWorks && rootModuleDTOs[1].logicalPath.equals("Layer 2");
		testWorks = testWorks && rootModuleDTOs[1].type.equals("Layer");
		testWorks = testWorks && areArraysEqual(rootModuleDTOs[1].physicalPathDTOs, new PhysicalPathDTO[] {});
		testWorks = testWorks && rootModuleDTOs[1].subModules.length == 0;
		
		testWorks = testWorks && rootModuleDTOs[2].logicalPath.equals("SubSystem 3");
		testWorks = testWorks && rootModuleDTOs[2].type.equals("SubSystem");
		testWorks = testWorks && areArraysEqual(rootModuleDTOs[2].physicalPathDTOs, new PhysicalPathDTO[] {});
		testWorks = testWorks && rootModuleDTOs[2].subModules.length == 0;
		
		assertTrue(testWorks);
	}
	
	@Test
	public void getAppliedRules(){
		boolean testWorks = true;
		RuleDTO[] ruleDTOs = defineService.getDefinedRules();
		testWorks = testWorks && ruleDTOs.length == 1;
		
		//Rule1
		testWorks = testWorks && ruleDTOs[0].ruleTypeKey.equals("IsNotAllowedToUse");
		//Rule1 ModuleFrom
		testWorks = testWorks && ruleDTOs[0].moduleFrom.logicalPath.equals("Layer 1.SubSystem 1");
		testWorks = testWorks && ruleDTOs[0].moduleFrom.type.equals("SubSystem");
		testWorks = testWorks && areArraysEqual(ruleDTOs[0].moduleFrom.physicalPathDTOs, new PhysicalPathDTO[] {});
		//Rule1 ModuleTo
		testWorks = testWorks && ruleDTOs[0].moduleTo.logicalPath.equals("Layer 2.SubSystem 2");
		testWorks = testWorks && ruleDTOs[0].moduleTo.type.equals("SubSystem");
		testWorks = testWorks && areArraysEqual(ruleDTOs[0].moduleTo.physicalPathDTOs, new PhysicalPathDTO[] {});
		testWorks = testWorks && areArraysEqual(ruleDTOs[0].violationTypeKeys, new String[] {});
		testWorks = testWorks && ruleDTOs[0].exceptionRules.length == 1;
		//Rule1 Exception	
		testWorks = testWorks && ruleDTOs[0].exceptionRules[0].ruleTypeKey.equals("IsAllowedToUse");
		//Rule1 ModuleFrom
		testWorks = testWorks && ruleDTOs[0].exceptionRules[0].moduleFrom.logicalPath.equals("Layer 1.SubSystem 1.SubSystem 4");
		testWorks = testWorks && ruleDTOs[0].exceptionRules[0].moduleFrom.type.equals("SubSystem");
		testWorks = testWorks && areArraysEqual(ruleDTOs[0].exceptionRules[0].moduleFrom.physicalPathDTOs, new PhysicalPathDTO[] {});
		//Rule1 ModuleTo
		testWorks = testWorks && ruleDTOs[0].exceptionRules[0].moduleTo.logicalPath.equals("Layer 2.SubSystem 2.SubSystem 6");
		testWorks = testWorks && ruleDTOs[0].exceptionRules[0].moduleTo.type.equals("SubSystem");
		testWorks = testWorks && areArraysEqual(ruleDTOs[0].exceptionRules[0].moduleTo.physicalPathDTOs, new PhysicalPathDTO[] {});
		testWorks = testWorks && areArraysEqual(ruleDTOs[0].exceptionRules[0].violationTypeKeys, new String[] {});
		testWorks = testWorks && ruleDTOs[0].exceptionRules[0].exceptionRules.length == 0;
		
		assertTrue(testWorks);
	}
	
	@Test
	public void getChildsFromModule(){
		boolean testWorks = true;
		ModuleDTO[] childModuleDTOs = defineService.getChildrenFromModule("Layer 1");
		testWorks = testWorks && childModuleDTOs.length == 1;
		
		testWorks = testWorks && childModuleDTOs[0].logicalPath.equals("Layer 1.SubSystem 1");
		testWorks = testWorks && childModuleDTOs[0].type.equals("SubSystem");
		testWorks = testWorks && areArraysEqual(childModuleDTOs[0].physicalPathDTOs, new PhysicalPathDTO[] {});
		testWorks = testWorks && childModuleDTOs[0].subModules.length == 0;
		
		childModuleDTOs = defineService.getChildrenFromModule("Layer 1.SubSystem 1");
		testWorks = testWorks && childModuleDTOs.length == 2;
		
		assertTrue(testWorks);
	}
	
	@Test
	public void getParentFromModule(){
		boolean testWorks = true;
		String parentModuleName;
		
		parentModuleName = defineService.getParentFromModule("Layer 1.SubSystem 1");
		testWorks = testWorks && parentModuleName.equals("Layer 1");
		parentModuleName = defineService.getParentFromModule("Layer 1");
		testWorks = testWorks && parentModuleName.equals("**");
		
		parentModuleName = defineService.getParentFromModule("Layer 1.SubSystem 1.SubSystem 4");
		testWorks = testWorks && parentModuleName.equals("Layer 1.SubSystem 1");

		assertTrue(testWorks);
	}
	
	private boolean areArraysEqual(Object[] list, Object[] list2){
		boolean areEqual = true;
		if (list.length == list2.length){
			for (int i = 0;i<list.length;i++){
				areEqual = areEqual && list[i].equals(list2[i]);
			}
		}
		else{
			areEqual = false;
		}
		return areEqual;
	}

}
