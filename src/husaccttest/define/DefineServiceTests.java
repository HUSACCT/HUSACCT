package husaccttest.define;

import static org.junit.Assert.*;

import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.PhysicalPathDTO;
import husacct.common.dto.RuleDTO;
import husacct.define.DefineServiceImpl;
import husacct.define.domain.Application;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.Layer;
import husacct.define.domain.module.Module;
import husacct.define.domain.services.SoftwareArchitectureDomainService;

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
		Module module1 = new Module("Module 1", "This is module 1");
		Module module2 = new Module("Module 2", "This is module 2");
		Module module3 = new Module("Module 3", "This is module 3");

		Module layer1 = new Layer("Layer 1", "This is layer 1", 1);
		Module layer2 = new Layer("Layer 2", "This is layer 2", 2);

		Module subModule1 = new Module("Sub Module 1", "This is a submodule");
		Module subModule11 = new Module("Sub Module 11", "This is a submodule");
		Module subModule2 = new Module("Sub Module 2", "This is a submodule");		
		Module subModule3 = new Module("Sub Module 3", "This is a submodule");		

		AppliedRule rule1 = new AppliedRule("IsNotAllowedToUse", "Test", new String[]{},
				"", module1, module2, true);

		AppliedRule exception1 = new AppliedRule("IsAllowedToUse", "Test", new String[]{},
				"", subModule1,	subModule2, true);
		
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
	public void createApplication(){
		boolean testWorks = true;
		SoftwareArchitectureDomainService domainService = new SoftwareArchitectureDomainService();
		defineService.createApplication("Application1", new String[] {"c:/Application1/"}, "Java", "1.0");
		Application app = domainService.getApplicationDetails();
		
		testWorks = testWorks && app.getName().equals("Application1");
		testWorks = testWorks && areArraysEqual(app.getPaths(), new String[] {"c:/Application1/"});
		testWorks = testWorks && app.getLanguage().equals("Java");
		testWorks = testWorks && app.getVersion().equals("1.0");
		assertTrue(testWorks);
	}
	
	@Test
	public void getApplication(){
		boolean testWorks = true;
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
		
		testWorks = testWorks && rootModuleDTOs[2].logicalPath.equals("Module 3");
		testWorks = testWorks && rootModuleDTOs[2].type.equals("Module");
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
		testWorks = testWorks && ruleDTOs[0].moduleFrom.logicalPath.equals("Layer 1.Module 1");
		testWorks = testWorks && ruleDTOs[0].moduleFrom.type.equals("Module");
		testWorks = testWorks && areArraysEqual(ruleDTOs[0].moduleFrom.physicalPathDTOs, new PhysicalPathDTO[] {});
		//Rule1 ModuleTo
		testWorks = testWorks && ruleDTOs[0].moduleTo.logicalPath.equals("Layer 2.Module 2");
		testWorks = testWorks && ruleDTOs[0].moduleTo.type.equals("Module");
		testWorks = testWorks && areArraysEqual(ruleDTOs[0].moduleTo.physicalPathDTOs, new PhysicalPathDTO[] {});
		testWorks = testWorks && areArraysEqual(ruleDTOs[0].violationTypeKeys, new String[] {});
		testWorks = testWorks && ruleDTOs[0].exceptionRules.length == 1;
		//Rule1 Exception	
		testWorks = testWorks && ruleDTOs[0].exceptionRules[0].ruleTypeKey.equals("IsAllowedToUse");
		//Rule1 ModuleFrom
		testWorks = testWorks && ruleDTOs[0].exceptionRules[0].moduleFrom.logicalPath.equals("Layer 1.Module 1.Sub Module 1");
		testWorks = testWorks && ruleDTOs[0].exceptionRules[0].moduleFrom.type.equals("Module");
		testWorks = testWorks && areArraysEqual(ruleDTOs[0].exceptionRules[0].moduleFrom.physicalPathDTOs, new PhysicalPathDTO[] {});
		//Rule1 ModuleTo
		testWorks = testWorks && ruleDTOs[0].exceptionRules[0].moduleTo.logicalPath.equals("Layer 2.Module 2.Sub Module 2");
		testWorks = testWorks && ruleDTOs[0].exceptionRules[0].moduleTo.type.equals("Module");
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
		
		testWorks = testWorks && childModuleDTOs[0].logicalPath.equals("Layer 1.Module 1");
		testWorks = testWorks && childModuleDTOs[0].type.equals("Module");
		testWorks = testWorks && areArraysEqual(childModuleDTOs[0].physicalPathDTOs, new PhysicalPathDTO[] {});
		testWorks = testWorks && childModuleDTOs[0].subModules.length == 1;
		
		assertTrue(testWorks);
	}
	
	@Test
	public void getParentFromModule(){
		boolean testWorks = true;
		String parentModuleName;
		
		parentModuleName = defineService.getParentFromModule("Layer 1.Module 1");
		testWorks = testWorks && parentModuleName.equals("Layer 1");
		parentModuleName = defineService.getParentFromModule("Layer 1");
		testWorks = testWorks && parentModuleName.equals("**");
		
		parentModuleName = defineService.getParentFromModule("Layer 1.Module 1.Sub Module 1");
		testWorks = testWorks && parentModuleName.equals("Layer 1.Module 1");

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
