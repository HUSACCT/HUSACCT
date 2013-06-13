package husaccttest.define;

import static org.junit.Assert.assertTrue;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.PhysicalPathDTO;
import husacct.common.dto.RuleDTO;
import husacct.define.DefineServiceImpl;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.ModuleFactory;
import husacct.define.domain.module.ModuleStrategy;

import org.junit.Before;
import org.junit.Test;

public class DefineServiceTests {
	private SoftwareArchitecture sA = SoftwareArchitecture.getInstance();
	private DefineServiceImpl defineService = new DefineServiceImpl();
	private ModuleFactory facotry= new ModuleFactory();
	@Before
	public void setUp(){
		sA = new SoftwareArchitecture("Test architecture", "This architecture is used for testing purposes");
		SoftwareArchitecture.setInstance(sA);
		ModuleStrategy module1 = facotry.createModule("externalsystem").set(name, description)("SubSystem 1", "This is subsystem 1");
		ModuleStrategy module2 = new SubSystem("SubSystem 2", "This is subsystem 2");
		ModuleStrategy module3 = new SubSystem("SubSystem 3", "This is subsystem 3");

		ModuleStrategy layer1 = new Layer("Layer 1", "This is layer 1", 1);
		ModuleStrategy layer2 = new Layer("Layer 2", "This is layer 2", 2);
		
		ModuleStrategy component1 = new Component("Component 1", "This is component 1");

		ModuleStrategy subModule1 = new SubSystem("SubSystem 4", "This is a subsystem");
		ModuleStrategy subModule11 = new SubSystem("SubSystem 5", "This is a subsystem");
		ModuleStrategy subModule2 = new SubSystem("SubSystem 6", "This is a subsystem");		
		ModuleStrategy subModule3 = new SubSystem("SubSystem 7", "This is a subsystem");		

//		AppliedRule rule1 = new AppliedRule("IsNotAllowedToUse", "Test", new String[]{},
//				"", module1, module2, true);
//
//		AppliedRule exception1 = new AppliedRule("IsAllowedToUse", "Test", new String[]{},
//				"", subModule1, subModule2, true);
		
		//TODO: Test SoftwareUnitDefinitions
		module1.addSubModule(subModule1);
		module1.addSubModule(subModule11);
		module2.addSubModule(subModule2);
		module3.addSubModule(subModule3);
		
//		rule1.addException(exception1);
		
		layer1.addSubModule(module1);
		layer2.addSubModule(module2);
		
		sA.addSeperatedModule(layer1);
		sA.addSeperatedModule(layer2);
		sA.addSeperatedModule(module3);
		sA.addSeperatedModule(component1);
		
		//sA.addAppliedRule(rule1);
		
	}
	
	@Test
	public void isDefined(){
		assertTrue(defineService.isDefined() == true);
	}
	
	@Test
	public void isMapped(){
		assertTrue(defineService.isMapped() == false);
	}
	
	@Test
	public void createAndGetApplication(){
		//defineService.createApplication("Application1", new String[] {"c:/Application1/"}, "Java", "1.0");
		
		ApplicationDTO appDTO = defineService.getApplicationDetails();
		
		assertTrue(appDTO.name.equals("Application1"));
		//assertTrue(areArraysEqual(appDTO.paths, new String[] {"c:/Application1/"}));
		//assertTrue(appDTO.programmingLanguage.equals("Java"));
		assertTrue(appDTO.version.equals("1.0"));
	}
	
	@Test 
	public void getRootModules(){
		ModuleDTO[] rootModuleDTOs = defineService.getRootModules();
		assertTrue(rootModuleDTOs.length == 4);
		
		assertTrue(rootModuleDTOs[0].logicalPath.equals("Layer 1"));
		assertTrue(rootModuleDTOs[0].type.equals("Layer"));
		assertTrue(areArraysEqual(rootModuleDTOs[0].physicalPathDTOs, new PhysicalPathDTO[] {}));
		assertTrue(rootModuleDTOs[0].subModules.length == 0);
		
		assertTrue(rootModuleDTOs[1].logicalPath.equals("Layer 2"));
		assertTrue(rootModuleDTOs[1].type.equals("Layer"));
		assertTrue(areArraysEqual(rootModuleDTOs[1].physicalPathDTOs, new PhysicalPathDTO[] {}));
		assertTrue(rootModuleDTOs[1].subModules.length == 0);
		
		assertTrue(rootModuleDTOs[2].logicalPath.equals("SubSystem 3"));
		assertTrue(rootModuleDTOs[2].type.equals("SubSystem"));
		assertTrue(areArraysEqual(rootModuleDTOs[2].physicalPathDTOs, new PhysicalPathDTO[] {}));
		assertTrue(rootModuleDTOs[2].subModules.length == 0);
	}
	
	@Test
	public void getAppliedRules(){
		RuleDTO[] ruleDTOs = defineService.getDefinedRules();
		assertTrue(ruleDTOs.length == 1);
		
		//Rule1
		assertTrue(ruleDTOs[0].ruleTypeKey.equals("IsNotAllowedToUse"));
		//Rule1 ModuleFrom
		assertTrue(ruleDTOs[0].moduleFrom.logicalPath.equals("Layer 1.SubSystem 1"));
		assertTrue(ruleDTOs[0].moduleFrom.type.equals("SubSystem"));
		assertTrue(areArraysEqual(ruleDTOs[0].moduleFrom.physicalPathDTOs, new PhysicalPathDTO[] {}));
		//Rule1 ModuleTo
		assertTrue(ruleDTOs[0].moduleTo.logicalPath.equals("Layer 2.SubSystem 2"));
		assertTrue(ruleDTOs[0].moduleTo.type.equals("SubSystem"));
		assertTrue(areArraysEqual(ruleDTOs[0].moduleTo.physicalPathDTOs, new PhysicalPathDTO[] {}));
		assertTrue(areArraysEqual(ruleDTOs[0].violationTypeKeys, new String[] {}));
		assertTrue(ruleDTOs[0].exceptionRules.length == 1);
		//Rule1 Exception	
		assertTrue(ruleDTOs[0].exceptionRules[0].ruleTypeKey.equals("IsAllowedToUse"));
		//Rule1 ModuleFrom
		assertTrue(ruleDTOs[0].exceptionRules[0].moduleFrom.logicalPath.equals("Layer 1.SubSystem 1.SubSystem 4"));
		assertTrue(ruleDTOs[0].exceptionRules[0].moduleFrom.type.equals("SubSystem"));
		assertTrue(areArraysEqual(ruleDTOs[0].exceptionRules[0].moduleFrom.physicalPathDTOs, new PhysicalPathDTO[] {}));
		//Rule1 ModuleTo
		assertTrue(ruleDTOs[0].exceptionRules[0].moduleTo.logicalPath.equals("Layer 2.SubSystem 2.SubSystem 6"));
		assertTrue(ruleDTOs[0].exceptionRules[0].moduleTo.type.equals("SubSystem"));
		assertTrue(areArraysEqual(ruleDTOs[0].exceptionRules[0].moduleTo.physicalPathDTOs, new PhysicalPathDTO[] {}));
		assertTrue(areArraysEqual(ruleDTOs[0].exceptionRules[0].violationTypeKeys, new String[] {}));
		assertTrue(ruleDTOs[0].exceptionRules[0].exceptionRules.length == 0);

	}
	
	@Test
	public void getChildsFromModule(){
		ModuleDTO[] childModuleDTOs = defineService.getChildrenFromModule("Layer 1");
		assertTrue(childModuleDTOs.length == 1);
		
		assertTrue(childModuleDTOs[0].logicalPath.equals("Layer 1.SubSystem 1"));
		assertTrue(childModuleDTOs[0].type.equals("SubSystem"));
		assertTrue(areArraysEqual(childModuleDTOs[0].physicalPathDTOs, new PhysicalPathDTO[] {}));
		assertTrue(childModuleDTOs[0].subModules.length == 0);
		
		childModuleDTOs = defineService.getChildrenFromModule("Layer 1.SubSystem 1");
		assertTrue(childModuleDTOs.length == 2);
	}
	
	@Test
	public void getParentFromModule(){
		String parentModuleName;
		
		parentModuleName = defineService.getParentFromModule("Layer 1.SubSystem 1");
		assertTrue(parentModuleName.equals("Layer 1"));
		parentModuleName = defineService.getParentFromModule("Layer 1");
		assertTrue(parentModuleName.equals("**"));
		
		parentModuleName = defineService.getParentFromModule("Layer 1.SubSystem 1.SubSystem 4");
		assertTrue(parentModuleName.equals("Layer 1.SubSystem 1"));
	}
	
	//public void Ifremove
	
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
