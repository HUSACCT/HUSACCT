package husaccttest.define;

import static org.junit.Assert.*;
import husacct.define.analyzer.AnalyzedUnitComparator;
import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.task.JtreeController;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

public class ConsistencyTest {

	
	
	@Before
	public void setupTest()
	{
		AnalyzedModuleComponent rootComponent = new AnalyzedModuleComponent("root", "Software Units", "root", "public");
		AnalyzedModuleComponent softwareunit1 = new  AnalyzedModuleComponent("test", "test", "package", "public");
		
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
		JtreeController.instance().setCurrentTree(new AnalyzedModuleTree(rootComponent));
	//	URL propertiesFile = getClass().getResource("/husacct/common/resources/husacct.properties");
		//PropertyConfigurator.configure(propertiesFile);
		rootComponent.addChild(softwareunit6);
		rootComponent.addChild(softwareunit1);
		
	}
	
	
	
	public AnalyzedModuleTree lessUnits()
	{
		AnalyzedModuleComponent rootComponent = new AnalyzedModuleComponent("root", "Software Units", "root", "public");
		AnalyzedModuleComponent softwareunit1 = new  AnalyzedModuleComponent("test", "test", "package", "public");
		rootComponent.addChild(softwareunit1);
		return new AnalyzedModuleTree(rootComponent);
	}
	public AnalyzedModuleTree moreUnits()
	{
		setupTest();
		AnalyzedModuleComponent rootComponent=JtreeController.instance().getRootOfModel();
		AnalyzedModuleComponent softwareunit6 = new  AnalyzedModuleComponent("new", "new", "package", "public");
		AnalyzedModuleComponent softwareunit7 = new  AnalyzedModuleComponent("new.nl", "nl", "package", "public");
		AnalyzedModuleComponent softwareunit9 = new  AnalyzedModuleComponent("new.nl.taal", "taal", "class", "public");
		AnalyzedModuleComponent softwareunit8 = new  AnalyzedModuleComponent("new.mediatheek", "mediatheek", "class", "public");
		softwareunit6.addChild(softwareunit7);
		softwareunit7.addChild(softwareunit9);
		softwareunit6.addChild(softwareunit8);
		rootComponent.addChild(softwareunit6);
		Collections.sort(rootComponent.getChildren());
		return new  AnalyzedModuleTree(rootComponent);
		}

	
	@Test
	public void canRemoveAndTest() {
		
			AnalyzedModuleComponent root = JtreeController.instance().getRootOfModel();
			
		AnalyzedModuleComponent unitTobeRemoved =(AnalyzedModuleComponent) root.getChildren().get(0); 
		JtreeController.instance().getTree().removeTreeItem(unitTobeRemoved);
		
		root = JtreeController.instance().getRootOfModel();
		
		assertTrue(!Arrays.asList(root.getChildren()).contains(unitTobeRemoved));
		JtreeController.instance().getTree().restoreTreeItem(unitTobeRemoved);
		
		
		
		root = JtreeController.instance().getRootOfModel();

		
		assertTrue(root.getChildren().get(0).getUniqueName().toLowerCase().equals(unitTobeRemoved.getUniqueName().toLowerCase()));
		
		
		}
	
	
	@Test
	public void canReanalyzeEqual()
	{
		AnalyzedUnitComparator comparator = new AnalyzedUnitComparator();
		AnalyzedModuleComponent newData= JtreeController.instance().getRootOfModel();
		comparator.calucalteChanges(newData, newData);
		
		assertEquals(JtreeController.instance().getRootOfModel().getChildren(),newData.getChildren());
		
		
		
	
	
	}
	
	@Test
	public void canReanalyzeLess()
	{
	 AnalyzedUnitComparator comparator = new AnalyzedUnitComparator();
	 AnalyzedModuleTree newTree= lessUnits();
	 AnalyzedModuleComponent newData = (AnalyzedModuleComponent)newTree.getModel().getRoot();	
	 AnalyzedModuleComponent rootMainTree = JtreeController.instance().getRootOfModel();
	 Collections.sort(newData.getChildren());
	 Collections.sort(rootMainTree.getChildren());
		
		comparator.calucalteChanges(rootMainTree, newData);
		
		
		assertEquals(rootMainTree.getChildren(),newData.getChildren());
		
		
		
	
	
	}
	
	

	@Test
	public void canReanalyzeMore()
	{
	 AnalyzedUnitComparator comparator = new AnalyzedUnitComparator();
	 AnalyzedModuleTree newTree= moreUnits();
	 AnalyzedModuleComponent newData = (AnalyzedModuleComponent)newTree.getModel().getRoot();	
	 AnalyzedModuleComponent rootMainTree = JtreeController.instance().getRootOfModel();
	 Collections.sort(newData.getChildren());
	 Collections.sort(rootMainTree.getChildren());
		
		comparator.calucalteChanges(rootMainTree, newData);
		
		
		assertEquals(rootMainTree.getChildren(),newData.getChildren());
		
		
		
	
	
	}
	
	
	
	
	
	
	
	
	
	
	

}
