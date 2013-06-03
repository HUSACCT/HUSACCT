package husaccttest.define;

import static org.junit.Assert.assertTrue;
import husacct.ServiceProvider;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.SoftwareUnitDefinition.Type;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.Layer;
import husacct.define.domain.module.Module;
import husacct.define.domain.module.SubSystem;
import husacct.define.task.components.ExternalLibraryComponent;

import org.junit.Test;

public class DefineSoftwareArchitectureTests {
	private SoftwareArchitecture sA;
	
	private AppliedRuleStrategy rule;
	private Module rootModule;
	private Layer moduleFrom;
	private Layer moduleTo;
	private Layer subModule1;
	private Layer subModule2;
	private Layer subModule3;
	private SubSystem subsubModule1;
	private SoftwareUnitDefinition su1;
	private SoftwareUnitDefinition su2;
	private SoftwareUnitDefinition su3;
	private SoftwareUnitDefinition su4;
	private SoftwareUnitDefinition su5;

	@Test
	public void testSoftwareArchitecture(){
		setInstance();
		addAppliedRule();
		getAppliedRule();
		getAppliedRulesByModule();
		enabledRules();
		removeRule();
		addModule();
		getModule();
		getModulesLogicalPath();
		getModuleByLogicalPath();
		getParentModuleIdByChildId();
		moveLayer();
		removeModule();
		getModuleBySoftwareUnit();
		getSoftwareUnitByName();
	}
	
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
	
	public void addAppliedRule(){
		assertTrue(sA.getAppliedRules().size() == 0);
		moduleFrom = new Layer("Presentation");
		moduleTo = new Layer("Infrastructure");
//		rule = new AppliedRule("IsNotAllowedToUse", "", new String[]{}, "", moduleFrom, moduleTo, true);
		//sA.addAppliedRule(rule);
		assertTrue(sA.getAppliedRules().size() == 1);
	}
	
	public void getAppliedRule(){
		assertTrue(rule.equals(sA.getAppliedRuleById(rule.getId())));
	}
	
	public void getAppliedRulesByModule(){
		assertTrue(sA.getAppliedRulesIdsByModuleFromId(moduleFrom.getId()).size() == 1);
		assertTrue(sA.getAppliedRulesIdsByModuleToId(moduleFrom.getId()).size() == 0);

		assertTrue(sA.getAppliedRulesIdsByModuleToId(moduleTo.getId()).size() == 1);
		assertTrue(sA.getAppliedRulesIdsByModuleFromId(moduleTo.getId()).size() == 0);
	}
	
	public void enabledRules(){
		assertTrue(sA.getEnabledAppliedRules().size() == 1);
		rule.setEnabled(false);
		assertTrue(sA.getEnabledAppliedRules().size() == 0);
	}
	
	public void removeRule(){
		assertTrue(sA.getAppliedRules().size() == 1);
		sA.removeAppliedRule(rule.getId());
		assertTrue(sA.getAppliedRules().size() == 0);
		//sA.addAppliedRule(rule);
		assertTrue(sA.getAppliedRules().size() == 1);
		sA.removeAppliedRules();
		assertTrue(sA.getAppliedRules().size() == 0);
	}
	
	public void addModule(){
		//getModules.size is equal to all the root modules
		assertTrue(sA.getModules().size() == 0);
		sA.addModule(moduleFrom);
		sA.addModule(moduleTo);
		assertTrue(sA.getModules().size() == 2);
		subModule1 = new Layer("subLayer1");
		subModule2 = new Layer("subLayer2");
		subModule3 = new Layer("subLayer3");
		subsubModule1 = new SubSystem("subSystem1");
		moduleFrom.addSubModule(subModule1);
		moduleFrom.addSubModule(subModule2);
		moduleFrom.addSubModule(subModule3);
		subModule1.addSubModule(subsubModule1);
		assertTrue(sA.getModules().size() == 2);
		assertTrue(moduleFrom.getSubModules().size() == 3);
		
		rootModule = sA.getModuleByLogicalPath("**");
		SpecialAddModule();
	}
	
	private void SpecialAddModule(){
		//Add dupicate module
		try {
			sA.addModule(moduleTo);
		} catch (RuntimeException e){
			if (e.getMessage().equals(ServiceProvider.getInstance().getLocaleService().getTranslatedString("SameNameModule"))){
				assertTrue(true);
			} else {
				assertTrue(false);
			}
		}
		//Add a module with the same name
		try {
			sA.addModule(new SubSystem("Infrastructure"));
		} catch (RuntimeException e){
			if (e.getMessage().equals(ServiceProvider.getInstance().getLocaleService().getTranslatedString("SameNameModule"))){
				assertTrue(true);
			} else {
				assertTrue(false);
			}
		}
	}
	
	public void getModule(){
		assertTrue(rootModule.equals(sA.getModuleById(rootModule.getId())));
		assertTrue(moduleFrom.equals(sA.getModuleById(moduleFrom.getId())));
		assertTrue(moduleTo.equals(sA.getModuleById(moduleTo.getId())));
		assertTrue(subModule1.equals(sA.getModuleById(subModule1.getId())));
		assertTrue(subModule2.equals(sA.getModuleById(subModule2.getId())));
		assertTrue(subModule3.equals(sA.getModuleById(subModule3.getId())));
	}
	
	public void getModulesLogicalPath(){
		assertTrue("**".equals(sA.getModulesLogicalPath(rootModule.getId())));
		assertTrue("Presentation".equals(sA.getModulesLogicalPath(moduleFrom.getId())));
		assertTrue("Infrastructure".equals(sA.getModulesLogicalPath(moduleTo.getId())));
		assertTrue("Presentation.subLayer1".equals(sA.getModulesLogicalPath(subModule1.getId())));
		assertTrue("Presentation.subLayer2".equals(sA.getModulesLogicalPath(subModule2.getId())));
		assertTrue("Presentation.subLayer3".equals(sA.getModulesLogicalPath(subModule3.getId())));
		assertTrue("Presentation.subLayer1.subSystem1".equals(sA.getModulesLogicalPath(subsubModule1.getId())));
	}
	
	public void getModuleByLogicalPath(){
		assertTrue(rootModule.equals(sA.getModuleByLogicalPath("**")));
		assertTrue(moduleFrom.equals(sA.getModuleByLogicalPath("Presentation")));
		assertTrue(moduleTo.equals(sA.getModuleByLogicalPath("Infrastructure")));
		assertTrue(subModule1.equals(sA.getModuleByLogicalPath("Presentation.subLayer1")));
		assertTrue(subModule2.equals(sA.getModuleByLogicalPath("Presentation.subLayer2")));
		assertTrue(subModule3.equals(sA.getModuleByLogicalPath("Presentation.subLayer3")));
		assertTrue(subsubModule1.equals(sA.getModuleByLogicalPath("Presentation.subLayer1.subSystem1")));
	}
	
	public void getParentModuleIdByChildId(){
		assertTrue(-1 == sA.getParentModuleIdByChildId(rootModule.getId()));
		assertTrue(rootModule.getId() == sA.getParentModuleIdByChildId(moduleFrom.getId()));
		assertTrue(rootModule.getId() == sA.getParentModuleIdByChildId(moduleTo.getId()));
		assertTrue(moduleFrom.getId() == sA.getParentModuleIdByChildId(subModule1.getId()));
		assertTrue(moduleFrom.getId() == sA.getParentModuleIdByChildId(subModule2.getId()));
		assertTrue(moduleFrom.getId() == sA.getParentModuleIdByChildId(subModule3.getId()));
		assertTrue(subModule1.getId() == sA.getParentModuleIdByChildId(subsubModule1.getId()));
	}
	
	public void moveLayer() {
		assertTrue(moduleFrom.getHierarchicalLevel() == 1);
		assertTrue(moduleTo.getHierarchicalLevel() == 2);
		sA.moveLayerDown(moduleFrom.getId());
		assertTrue(moduleFrom.getHierarchicalLevel() == 2);
		assertTrue(moduleTo.getHierarchicalLevel() == 1);
		sA.moveLayerUp(moduleFrom.getId());
		assertTrue(moduleFrom.getHierarchicalLevel() == 1);
		assertTrue(moduleTo.getHierarchicalLevel() == 2);
		sA.moveLayerUp(moduleFrom.getId());
		assertTrue(moduleFrom.getHierarchicalLevel() == 1);
		assertTrue(moduleTo.getHierarchicalLevel() == 2);
		moduleTo.setHierarchicalLevel(3);
		assertTrue(moduleFrom.getHierarchicalLevel() == 1);
		assertTrue(moduleTo.getHierarchicalLevel() == 3);
		sA.moveLayerDown(moduleFrom.getId());
		assertTrue(moduleFrom.getHierarchicalLevel() == 3);
		assertTrue(moduleTo.getHierarchicalLevel() == 1);
		//Testing moving subLayers
		assertTrue(subModule1.getHierarchicalLevel() == 3);
		assertTrue(subModule2.getHierarchicalLevel() == 4);
		assertTrue(subModule3.getHierarchicalLevel() == 5);
		sA.moveLayerDown(subModule1.getId());
		assertTrue(subModule1.getHierarchicalLevel() == 4);
		assertTrue(subModule2.getHierarchicalLevel() == 3);
		assertTrue(subModule3.getHierarchicalLevel() == 5);
		sA.moveLayerUp(subModule3.getId());
		assertTrue(subModule1.getHierarchicalLevel() == 5);
		assertTrue(subModule2.getHierarchicalLevel() == 3);
		assertTrue(subModule3.getHierarchicalLevel() == 4);
	}
	
	public void removeModule(){
		//Keep in mind that when you remove a module, the related rules should disappear aswell.
		//sA.addAppliedRule(rule);
		assertTrue(subModule1.getSubModules().size() == 1);
		assertTrue(subModule2.getSubModules().size() == 0);
		assertTrue(moduleFrom.getSubModules().size() == 3);
		assertTrue(moduleTo.getSubModules().size() == 0);
		
		sA.removeModule(subsubModule1);
		assertTrue(subModule1.getSubModules().size() == 0);
		assertTrue(subModule2.getSubModules().size() == 0);
		assertTrue(moduleFrom.getSubModules().size() == 3);
		assertTrue(moduleTo.getSubModules().size() == 0);
	
		
		sA.removeModule(moduleFrom);
		assertTrue(sA.getModules().size() == 1);
		assertTrue(moduleTo.getSubModules().size() == 0);
	
		sA.addModule(moduleFrom);
		sA.removeAllModules();
		assertTrue(sA.getModules().size() == 0);
		
		//rootModule should not be removeable
		Long id =sA.getModuleById(rootModule.getId()).getId();
		assertTrue(rootModule.getId()==id);
		sA.removeModule(rootModule);
		assertTrue(rootModule.equals(sA.getModuleById(rootModule.getId())));
	}
	
	
	public void getModuleBySoftwareUnit(){
		su1 = new SoftwareUnitDefinition("presentation", Type.PACKAGE);
		moduleFrom.addSUDefinition(su1);
		su2 = new SoftwareUnitDefinition("infrastructure", Type.PACKAGE);
		moduleTo.addSUDefinition(su2);
		sA.addModule(moduleFrom);
		sA.addModule(moduleTo);
		su3 = new SoftwareUnitDefinition("presentation.gui", Type.PACKAGE);
		su4 = new SoftwareUnitDefinition("presentation.upload", Type.PACKAGE);
		su5 = new SoftwareUnitDefinition("presentation.post", Type.PACKAGE);
		subModule1.addSUDefinition(su3);
		subModule2.addSUDefinition(su4);
		subsubModule1.addSUDefinition(su5);
		subModule1.addSubModule(subsubModule1);
		assertTrue(moduleFrom.equals(sA.getModuleBySoftwareUnit(su1.getName())));
		assertTrue(moduleTo.equals(sA.getModuleBySoftwareUnit(su2.getName())));
		assertTrue(subModule1.equals(sA.getModuleBySoftwareUnit(su3.getName())));
		assertTrue(subModule2.equals(sA.getModuleBySoftwareUnit(su4.getName())));
		assertTrue(subsubModule1.equals(sA.getModuleBySoftwareUnit(su5.getName())));
	}
	
	public void getSoftwareUnitByName(){
		assertTrue(su1.equals(sA.getSoftwareUnitByName(su1.getName())));
		assertTrue(su2.equals(sA.getSoftwareUnitByName(su2.getName())));
		assertTrue(su3.equals(sA.getSoftwareUnitByName(su3.getName())));
		assertTrue(su4.equals(sA.getSoftwareUnitByName(su4.getName())));
		assertTrue(su5.equals(sA.getSoftwareUnitByName(su5.getName())));
	}
	@Test
	public void testifunitisremoved()
	
	{
		ExternalLibraryComponent testComp = new ExternalLibraryComponent();
		ExternalLibraryComponent externaltoberemoved = new ExternalLibraryComponent();
		
		testComp.addChild(externaltoberemoved);
		
		
		assertTrue(testComp.getChildren().size()==1);
		
		testComp.removeChild(externaltoberemoved);
		
		assertTrue(testComp.getChildren().size()==0);
		
		
		
		
		
	}
}
