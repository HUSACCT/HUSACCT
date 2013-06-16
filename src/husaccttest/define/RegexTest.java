package husaccttest.define;

import static org.junit.Assert.assertTrue;
import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.presentation.utils.ExpressionEngine;
import husacct.define.task.JtreeController;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AnalyzedModuleComponent;

import org.junit.Before;
import org.junit.Test;

public class RegexTest {
	private ExpressionEngine softwareUnitController = new ExpressionEngine();
	
	@Before
	public void setupTest()
	{
		AnalyzedModuleComponent rootComponent = new AnalyzedModuleComponent("root", "Software Units", "root", "public");
		AnalyzedModuleComponent projectComponent = new AnalyzedModuleComponent(
				"hello313", "hello", "root", "public");
		AnalyzedModuleComponent softwareunit1 = new  AnalyzedModuleComponent("test", "test", "package", "public");
		projectComponent.addChild(softwareunit1);
		AnalyzedModuleComponent softwareunit2 = new  AnalyzedModuleComponent("test.hellur", "hellur", "package", "public");
		AnalyzedModuleComponent softwareunit3 = new  AnalyzedModuleComponent("test.facebook", "facebook", "package", "public");
		AnalyzedModuleComponent softwareunit4 = new  AnalyzedModuleComponent("test.twitter", "class", "package", "public");
		AnalyzedModuleComponent softwareunit5 = new  AnalyzedModuleComponent("test.facebook.twitter", "twitter", "class", "public");
		softwareunit1.addChild(softwareunit2);
		softwareunit1.addChild(softwareunit3);
		softwareunit1.addChild(softwareunit4);
		softwareunit3.addChild(softwareunit5);
		AnalyzedModuleComponent softwareunit6 = new  AnalyzedModuleComponent("hu", "hu", "package", "public");
		AnalyzedModuleComponent softwareunit7 = new  AnalyzedModuleComponent("hu.nl", "nl", "package", "public");
		AnalyzedModuleComponent softwareunit9 = new  AnalyzedModuleComponent("hu.nl.taal", "taal", "class", "public");
		AnalyzedModuleComponent softwareunit8 = new  AnalyzedModuleComponent("hu.mediatheek", "mediatheek", "class", "public");
		projectComponent.addChild(softwareunit6);
		softwareunit6.addChild(softwareunit7);
		softwareunit7.addChild(softwareunit9);
		softwareunit6.addChild(softwareunit8);
		AnalyzedModuleComponent softwareunit10 = new  AnalyzedModuleComponent("hu.nl.kijk", "kijk", "package", "public");
		AnalyzedModuleComponent softwareunit11 = new  AnalyzedModuleComponent("hu.nl.kijk.hier", "hier", "class", "public");
		AnalyzedModuleComponent softwareunit12 = new  AnalyzedModuleComponent("hu.nl.kijk.verder", "verder", "class", "public");
		AnalyzedModuleComponent softwareunit13 = new  AnalyzedModuleComponent("hu.nl.kijk.door", "door", "package", "public");
		AnalyzedModuleComponent softwareunit14 = new  AnalyzedModuleComponent("hu.nl.kijk.door.kies", "kies", "class", "public");
		AnalyzedModuleComponent softwareunit15 = new  AnalyzedModuleComponent("hu.nl.kijk.door.naar", "naar", "class", "public");
		softwareunit7.addChild(softwareunit10);
		softwareunit10.addChild(softwareunit11);
		softwareunit10.addChild(softwareunit12);
		softwareunit10.addChild(softwareunit13);
		softwareunit13.addChild(softwareunit14);
		softwareunit13.addChild(softwareunit15);
		rootComponent.addChild(projectComponent);
		JtreeController.instance().setCurrentTree(new AnalyzedModuleTree(rootComponent));
	
	
		
		
	}
	
	
	@Test
	public void testRegexPackage() {
		AnalyzedModuleTree  restultTree= JtreeController.instance().getResultTree();
		softwareUnitController.saveRegExToResultTree("*test","P");
	    AnalyzedModuleComponent rootOfResults = (AnalyzedModuleComponent) restultTree.getModel().getRoot();
		assertTrue(rootOfResults.getChildren().size()==1);
		AnalyzedModuleComponent result = (AnalyzedModuleComponent) rootOfResults.getChildren().get(0);
		assertTrue(result.getUniqueName().equals("test"));
		assertTrue(result.getName().equals("test"));
		assertTrue(result.getType().toLowerCase().equals("package"));
		
		
		
	
	}
	
	
	@Test
	public void testRegexClass() {
		AnalyzedModuleTree  restultTree= JtreeController.instance().getResultTree();
		softwareUnitController.saveRegExToResultTree("*kies","C");
	    AnalyzedModuleComponent rootOfResults = (AnalyzedModuleComponent) restultTree.getModel().getRoot();
		assertTrue(rootOfResults.getChildren().size()==1);
		AnalyzedModuleComponent result = (AnalyzedModuleComponent) rootOfResults.getChildren().get(0);
		assertTrue(result.getUniqueName().equals("hu.nl.kijk.door.kies"));
		assertTrue(result.getName().equals("kies"));
		assertTrue(result.getType().toLowerCase().equals("class"));
	}
	
	
	@Test
	public void testRegexPackAndClasses() {
		AnalyzedModuleTree  restultTree= JtreeController.instance().getResultTree();
		softwareUnitController.saveRegExToResultTree("*naar","PC");
	    AnalyzedModuleComponent rootOfResults = (AnalyzedModuleComponent) restultTree.getModel().getRoot();
		assertTrue(rootOfResults.getChildren().size()==1);
		AnalyzedModuleComponent result = (AnalyzedModuleComponent) rootOfResults.getChildren().get(0);
		assertTrue(result.getUniqueName().equals("hu.nl.kijk.door.naar"));
		assertTrue(result.getName().equals("naar"));
		assertTrue(result.getType().toLowerCase().equals("class"));
	
	}
	
	@Test
	public void testRegexPackAndClasses2() {
		AnalyzedModuleTree  restultTree= JtreeController.instance().getResultTree();
		softwareUnitController.saveRegExToResultTree("hu.mediatheek.*","PC");
	    AnalyzedModuleComponent rootOfResults = (AnalyzedModuleComponent) restultTree.getModel().getRoot();
		for (AbstractCombinedComponent it : rootOfResults.getChildren()) {
			System.out.println(it.getUniqueName());
		}
	    assertTrue(rootOfResults.getChildren().size()==1);
		AnalyzedModuleComponent result = (AnalyzedModuleComponent) rootOfResults.getChildren().get(0);
		assertTrue(result.getUniqueName().equals("hu.mediatheek"));
		assertTrue(result.getName().equals("mediatheek"));
		assertTrue(result.getType().toLowerCase().equals("class"));
	
	}

}
